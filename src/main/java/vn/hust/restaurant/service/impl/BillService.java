package vn.hust.restaurant.service.impl;

import com.google.common.base.Strings;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.restaurant.config.Constants;
import vn.hust.restaurant.constants.BillConstants;
import vn.hust.restaurant.constants.ResultConstants;
import vn.hust.restaurant.domain.Bill;
import vn.hust.restaurant.domain.BillPayment;
import vn.hust.restaurant.domain.BillProduct;
import vn.hust.restaurant.domain.User;
import vn.hust.restaurant.repository.BillProductRepository;
import vn.hust.restaurant.repository.BillRepository;
import vn.hust.restaurant.repository.ProductRepository;
import vn.hust.restaurant.service.dto.ResultDTO;
import vn.hust.restaurant.service.dto.bill.*;
import vn.hust.restaurant.service.dto.product.ProductImagesResult;
import vn.hust.restaurant.service.util.Common;
import vn.hust.restaurant.web.rest.errors.BadRequestAlertException;
import vn.hust.restaurant.web.rest.errors.CustomException;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;
import vn.hust.restaurant.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public ResultDTO saveBill(BillCreateRequest billDTO, Integer status) {
        if (Strings.isNullOrEmpty(billDTO.getCustomerName())) billDTO.setCustomerName("Bàn " + billDTO.getTableId());
        Bill bill = new Bill();
        //       Copy data từ dto vào bill
        ResultDTO resultDTO = new ResultDTO();
        User user = userService.getUserWithAuthorities();
        bill = convertBill(user, billDTO);
        if (status != null) bill.setStatus(status);
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
                bill.setCode(billOld.getCode());
            }
        }

        BillPayment billPayment;
        if (isNew) {
            if (bill.getPayment() == null) {
                BillPayment payment = new BillPayment();
                payment.setPaymentMethod("Tiền mặt");
                payment.setAmount(bill.getAmount());
                bill.setPayment(payment);
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
//                billProductRepository.delete(billProduct);
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

    public ResultDTO getBillTemp(Integer tableId, Integer comId) {
        Optional<Bill> bill = billRepository.findByTableIdAndComIdAndStatus(tableId, comId, BillConstants.Status.BILL_DONT_COMPLETE);
        if (bill.isPresent()) {
            List<ProductImagesResult> productImagesResults = productRepository.findImagesByBillId(bill.get().getId());
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
            ExceptionConstants.BILL_NOT_FOUND,
            ExceptionConstants.BILL_NOT_FOUND_VI
        );
    }

    public ResultDTO saveTempBill(BillCreateRequest billDTO) {
        Optional<Bill> billO = billRepository.findByTableIdAndComIdAndStatus(billDTO.getTableId(), billDTO.getComId(), BillConstants.Status.BILL_DONT_COMPLETE);
        billDTO.setStatus(BillConstants.Status.BILL_DONT_COMPLETE);
        if (billO.isEmpty()) return saveBill(billDTO, null);
        Bill bill = billO.get();
        billDTO.setId(bill.getId());
        billDTO.setQuantity(bill.getQuantity().add(billDTO.getQuantity()));
        billDTO.setAmount(bill.getAmount().add(billDTO.getAmount()));
        billDTO.setTotalAmount(bill.getTotalAmount().add(billDTO.getTotalAmount()));
        joinBillProducts(bill.getProducts(), billDTO.getProducts());
        return saveBill(billDTO, null);
    }

    private void joinBillProducts(List<BillProduct> src, List<BillProductRequest> target) {
        for (int i = 0; i < src.size(); i++) {
            Boolean isAdded = false;
            BillProductRequest temp = modelMapper.map(src.get(i), BillProductRequest.class);
            for (int j = 0; j < target.size(); j++) {
                BillProductRequest rq = target.get(j);
                if (Objects.equals(temp.getProductCode(), rq.getProductCode())) {
                    rq.setAmount(rq.getAmount().add(temp.getAmount()));
                    rq.setTotalAmount(rq.getTotalAmount().add(temp.getTotalAmount()));
                    rq.setQuantity(rq.getQuantity().add(temp.getQuantity()));
                    isAdded = true;
                    break;
                }
            }
            if (!isAdded) target.add(temp);
        }
    }

    public ResultDTO completeBill(Integer billId) {
        Optional<Bill> billO = billRepository.findById(billId);
        if (billO.isPresent()) {
            Bill bill = billO.get();
            bill.setStatus(BillConstants.Status.BILL_COMPLETE);
            billRepository.save(bill);
        }
        return new ResultDTO("Thành công", "Thành công", true);
    }

    @Transactional
    public ResultDTO join(BillJoin joinDTO) {

        if (joinDTO.getType() == 0) {
            billRepository.updateStatusByBillId(joinDTO.getId(), 4);
            return new ResultDTO("Thành công", "Thành công", true);
        }
        Bill bill = billRepository.findById(joinDTO.getId()).orElseThrow( () -> new CustomException("Not found bill"));
        BillCreateRequest billCreateRequest = converBillToCreateRequest(bill);
        bill.setStatus(4);
        billRepository.save(bill);
        return saveTempBill(billCreateRequest);
    }

    private BillCreateRequest converBillToCreateRequest(Bill bill) {
        BillCreateRequest billCreateRequest = new BillCreateRequest();
        billCreateRequest.setTableId(bill.getTableId());
        billCreateRequest.setId(bill.getId());
        billCreateRequest.setComId(bill.getComId());
        billCreateRequest.setCustomerName(bill.getCustomerName());
        billCreateRequest.setDeliveryType(bill.getDeliveryType());
        billCreateRequest.setQuantity(bill.getQuantity());
        billCreateRequest.setAmount(bill.getAmount());
        billCreateRequest.setTotalAmount(bill.getTotalAmount());
        billCreateRequest.setStatus(bill.getStatus());
        billCreateRequest.setDescription(bill.getDescription());
        billCreateRequest.setPayment(convertPaymentToRequest(bill.getPayment()));
        billCreateRequest.setProducts(converProductToProductRQ(bill.getProducts()));
        return billCreateRequest;
    }

    private List<BillProductRequest> converProductToProductRQ(List<BillProduct> products) {
        List<BillProductRequest> list = new ArrayList<>();
        for (int i = 0; i < products.size(); i++){
            BillProductRequest billProductRequest = new BillProductRequest();
            billProductRequest.setProductId(products.get(i).getProductId().toString());
            billProductRequest.setProductName(products.get(i).getProductName());
            billProductRequest.setProductCode(products.get(i).getProductCode());
            billProductRequest.setQuantity(products.get(i).getQuantity());
            billProductRequest.setUnit(products.get(i).getUnit());
            billProductRequest.setUnitPrice(products.get(i).getUnitPrice());
            billProductRequest.setAmount(products.get(i).getAmount());
            billProductRequest.setTotalAmount(products.get(i).getTotalAmount());
            billProductRequest.setPosition(products.get(i).getPosition());
            list.add(billProductRequest);
        }
        return list;
    }

    private BillPaymentRequest convertPaymentToRequest(BillPayment payment) {
        BillPaymentRequest billPaymentRequest = new BillPaymentRequest();
        billPaymentRequest.setPaymentMethod(payment.getPaymentMethod());
        billPaymentRequest.setAmount(payment.getAmount());
        return billPaymentRequest;
    }
}
