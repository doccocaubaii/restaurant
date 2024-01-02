package vn.hust.easypos.service.impl;

import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.easypos.config.Constants;
import vn.hust.easypos.constants.BillConstants;
import vn.hust.easypos.constants.ResultConstants;
import vn.hust.easypos.domain.Bill;
import vn.hust.easypos.domain.BillPayment;
import vn.hust.easypos.domain.BillProduct;
import vn.hust.easypos.domain.User;
import vn.hust.easypos.repository.BillProductRepository;
import vn.hust.easypos.repository.BillRepository;
import vn.hust.easypos.repository.ProductRepository;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.bill.BillCreateRequest;
import vn.hust.easypos.service.dto.bill.BillItemResponse;
import vn.hust.easypos.service.dto.product.ProductImagesResult;
import vn.hust.easypos.service.dto.product.ProductResponse;
import vn.hust.easypos.service.util.Common;
import vn.hust.easypos.web.rest.errors.BadRequestAlertException;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;
import vn.hust.easypos.web.rest.errors.InternalServerException;

@Service
public class BillService {

    private static final String ENTITY_NAME = "BillService";

    private final Logger log = LoggerFactory.getLogger(BillService.class);

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final BillRepository billRepository;

    private final ProductRepository productRepository;

    private final BillProductRepository billProductRepository;

    public BillService(
        UserService userService,
        ModelMapper modelMapper,
        BillRepository billRepository,
        ProductRepository productRepository,
        BillProductRepository billProductRepository
    ) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.billRepository = billRepository;
        this.productRepository = productRepository;
        this.billProductRepository = billProductRepository;
    }

    public ResultDTO saveBill(BillCreateRequest billDTO) {
        Bill bill = new Bill();
        //       Copy data từ dto vào bill
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities();
        bill = convertBill(user, billDTO);
        billRepository.save(bill);
        log.debug("Save Bill success : {}", bill);
        resultDTO.setStatus(true);
        if (billDTO.getId() == null) {
            resultDTO.setReason(ResultConstants.CREATE_BILL_SUCCESS);
        } else {
            resultDTO.setReason(ResultConstants.UPDATE_BILL_SUCCESS);
        }
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setData(bill);
        return resultDTO;
    }

    public Bill convertBill(User user, BillCreateRequest billDTO) {
        Bill bill = new Bill();
        bill = modelMapper.map(billDTO, Bill.class);
        Bill billOld = new Bill();
        boolean isNew = bill.getId() == null;
        if (!isNew) {
            Optional<Bill> billOptional = billRepository.findByIdAndComId(bill.getId(), user.getCompanyId());
            if (billOptional.isEmpty()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BILL_NOT_FOUND_VI,
                    ExceptionConstants.BILL_NOT_FOUND_VI,
                    ExceptionConstants.BILL_NOT_FOUND
                );
            } else {
                billOld = billOptional.get();
                if (!Objects.equals(billOld.getStatus(), BillConstants.Status.BILL_DONT_COMPLETE)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_NOT_UPDATE
                    );
                }
                Set<Integer> ids = new HashSet<>();
                for (BillProduct billProduct : billOld.getProducts()) {
                    if (billProduct.getProductId() != null) {
                        ids.add(billProduct.getProductId());
                    }
                }
                List<ProductResponse> products = productRepository.searchAllByComIdAndIdAndStatusTrue(user.getCompanyId(), ids);
                if (products.size() != ids.size()) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.BILL_CANNOT_UPDATE_VI,
                        ExceptionConstants.BILL_NOT_UPDATE_VI,
                        ExceptionConstants.BILL_CANNOT_UPDATE
                    );
                }
                bill.setCode(billOld.getCode());
            }
        }

        BillPayment billPayment;
        if (isNew) {
            if (bill.getPayment() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.BILL_PAYMENTS_IS_EMPTY_VI,
                    ENTITY_NAME,
                    ExceptionConstants.BILL_PAYMENTS_IS_EMPTY
                );
            }
            billPayment = bill.getPayment();
        } else {
            billPayment = billOld.getPayment();
            bill.setStatus(BillConstants.Status.BILL_DONT_COMPLETE);
        }
        if (bill.getQuantity().compareTo(BigDecimal.ZERO) == 0) {
            throw new BadRequestAlertException(
                ExceptionConstants.QUANTITY_IS_VALID_VI,
                ExceptionConstants.QUANTITY_IS_VALID_VI,
                ExceptionConstants.QUANTITY_INVALID
            );
        }
        if (billPayment.getAmount() == null) {
            billPayment.setAmount(BigDecimal.ZERO);
        }

        int scale = 0;
        bill.setAmount(Common.roundMoney(bill.getAmount(), scale));

        LocalDateTime billDate = Common.convertStringToZoneDateTime(billDTO.getBillDate(), Constants.ZONED_DATE_TIME_FORMAT);
        if (billDate != null) {
            bill.setBillDate(billDate);
        }

        bill.setComId(user.getCompanyId());

        if (Strings.isNullOrEmpty(bill.getCustomerName())) {
            bill.setCustomerName(Constants.CUSTOMER_NAME);
        }

        // Không tính lại giá tiền nên hiện đang không check tiền
        for (BillProduct billProductOb : bill.getProducts()) {
            Integer productId = billProductOb.getProductId();
            if (Strings.isNullOrEmpty(billProductOb.getUnit())) {
                billProductOb.setUnit(null);
            }
            billProductOb.setBill(bill);
            billProductOb.setProductNormalizedName(Common.normalizedName(Arrays.asList(billProductOb.getProductName())));
        }

        // TỰ động insert mã đơn hàng trường code
        if (isNew) {
            bill.setCode(userService.genCode(user.getCompanyId(), Constants.DON_HANG));
        }
        billPayment.setBill(bill);
        bill.setPayment(billPayment);

        // Xóa bill_product trước khi update
        if (!isNew) {
            for (BillProduct billProduct : billOld.getProducts()) {
                billProduct.setBill(null);
                billProductRepository.delete(billProduct);
            }
        }
        bill.setCustomerNormalizedName(Common.normalizedName(Arrays.asList(bill.getCustomerName())));
        return bill;
    }

    @Transactional(readOnly = true)
    public ResultDTO searchBills(Pageable pageable, Integer status, String fromDate, String toDate, String keyword) {
        ResultDTO resultDTO = new ResultDTO();
        log.debug("Request to get Bills by status");
        User user = userService.getUserWithAuthorities();

        Page<BillItemResponse> page = billRepository.searchBills(pageable, status, fromDate, toDate, keyword, user.getCompanyId());
        resultDTO.setMessage(ResultConstants.SUCCESS);
        resultDTO.setReason(ResultConstants.SUCCESS_GET_LIST);
        resultDTO.setStatus(true);
        resultDTO.setData(page.getContent());
        resultDTO.setCount((int) page.getTotalElements());
        return resultDTO;
    }

    public ResultDTO getBillById(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<Bill> bill = billRepository.findByIdAndComId(id, user.getCompanyId());
        if (bill.isPresent()) {
            List<ProductImagesResult> productImagesResults = productRepository.findImagesByBillId(id);
            if (!productImagesResults.isEmpty()) {
                Map<Integer, String> mapImages = productImagesResults
                    .stream()
                    .collect(Collectors.toMap(ProductImagesResult::getId, ProductImagesResult::getImage));
                for (BillProduct item : bill.get().getProducts()) {
                    if (mapImages.containsKey(item.getProductId())) {
                        item.setImageUrl(mapImages.get(item.getProductId()));
                    }
                }
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, bill, 1);
        }

        throw new InternalServerException(
            ExceptionConstants.BILL_NOT_FOUND,
            ExceptionConstants.BILL_NOT_FOUND + id,
            ExceptionConstants.BILL_NOT_FOUND_VI
        );
    }
}
