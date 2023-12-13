package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.BillConstants;
import vn.softdreams.easypos.constants.CustomerConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.VoucherConstants;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.card.CardItemResult;
import vn.softdreams.easypos.dto.customer.CustomerItemResult;
import vn.softdreams.easypos.dto.voucher.*;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.VoucherWebManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

/**
 * Service Implementation for managing {@link Voucher}.
 */
@Service
@Transactional
public class VoucherWebManagementServiceImpl implements VoucherWebManagementService {

    private static final String ENTITY_NAME = "voucher";
    private final Logger log = LoggerFactory.getLogger(VoucherManagementServiceImpl.class);

    private final VoucherRepository voucherRepository;
    private final VoucherCompanyRepository voucherCompanyRepository;
    private final UserService userService;
    private final CompanyRepository companyRepository;
    private final LoyaltyCardRepository loyaltyCardRepository;
    private final CustomerRepository customerRepository;
    private final CustomerCardRepository customerCardRepository;
    private final VoucherApplyRepository voucherApplyRepository;
    private final VoucherUsageRepository voucherUsageRepository;
    private final BillRepository billRepository;

    public VoucherWebManagementServiceImpl(
        VoucherRepository voucherRepository,
        VoucherCompanyRepository voucherCompanyRepository,
        UserService userService,
        CompanyRepository companyRepository,
        LoyaltyCardRepository loyaltyCardRepository,
        CustomerRepository customerRepository,
        CustomerCardRepository customerCardRepository,
        VoucherApplyRepository voucherApplyRepository,
        VoucherUsageRepository voucherUsageRepository,
        BillRepository billRepository
    ) {
        this.voucherRepository = voucherRepository;
        this.voucherCompanyRepository = voucherCompanyRepository;
        this.userService = userService;
        this.companyRepository = companyRepository;
        this.loyaltyCardRepository = loyaltyCardRepository;
        this.customerRepository = customerRepository;
        this.customerCardRepository = customerCardRepository;
        this.voucherApplyRepository = voucherApplyRepository;
        this.voucherUsageRepository = voucherUsageRepository;
        this.billRepository = billRepository;
    }

    @Override
    public ResultDTO saveVoucher(VoucherWebSaveRequest request) {
        log.debug("request save voucher");
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        ZonedDateTime from = Common.convertStringToDateTime(request.getStartTime(), Constants.ZONED_DATE_FORMAT);
        ZonedDateTime to = Common.convertStringToDateTime(request.getEndTime(), Constants.ZONED_DATE_FORMAT);
        if (from != null && to != null) {
            Common.checkStartAndEndDate(from, to);
        }
        Voucher voucher = new Voucher();
        Integer requestId = request.getId();
        if (request.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestAlertException(VOUCHER_VALUE_INVALID_VI, ENTITY_NAME, VOUCHER_VALUE_INVALID);
        }

        if (requestId != null) {
            Optional<Voucher> voucherOptional = voucherRepository.findOneByIdIgnoreStatus(
                requestId,
                comId,
                VoucherConstants.Status.DELETED
            );
            if (voucherOptional.isEmpty()) {
                throw new BadRequestAlertException(VOUCHER_NOT_FOUND_VI, ENTITY_NAME, VOUCHER_NOT_FOUND);
            }
            voucher = voucherOptional.get();
            List<Integer> billIds = voucherUsageRepository.getBillIdByVoucherId(comId, voucher.getId());
            if (!billIds.isEmpty()) {
                Integer countStatusBillNotCompleted = billRepository.countStatusBillNotCompleted(
                    billIds,
                    BillConstants.Status.BILL_DONT_COMPLETE
                );
                if (countStatusBillNotCompleted != null && countStatusBillNotCompleted > 0) {
                    throw new BadRequestAlertException(VOUCHER_UPDATE_BILL_INVALID_VI, ENTITY_NAME, VOUCHER_UPDATE_BILL_INVALID);
                }
            }
        }
        if (requestId == null || !request.getCode().equalsIgnoreCase(voucher.getCode())) {
            Integer checkCodeDuplicate = voucherCompanyRepository.checkCodeDuplicate(
                comId,
                request.getCode().toLowerCase(),
                VoucherConstants.Status.DELETED
            );
            if (checkCodeDuplicate >= 1) {
                throw new BadRequestAlertException(VOUCHER_CODE_EXISTS_VI, ENTITY_NAME, VOUCHER_CODE_EXISTS);
            }
        }

        voucher.setName(request.getName());
        voucher.setNormalizedName(Common.normalizedName(List.of(request.getName())));
        voucher.setCode(request.getCode());
        voucher.setType(VoucherConstants.Type.VOUCHER_DISCOUNT);
        voucher.setStatus(request.getStatus());

        String desc;
        List<VoucherDiscountCondition> conditionSave = new ArrayList<>();
        List<VoucherDiscountConditionDetail> discountConditionDetailSave = new ArrayList<>();
        VoucherDiscountCondition condition = new VoucherDiscountCondition();
        VoucherDiscountConditionDetail discountConditionDetail = new VoucherDiscountConditionDetail();
        if (request.getType().equals(VoucherConstants.Type.VOUCHER_DISCOUNT_PERCENTAGE_WEB)) {
            if (request.getValue().compareTo(new BigDecimal(100)) > 0) {
                throw new BadRequestAlertException(
                    VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID_VI,
                    ENTITY_NAME,
                    VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID
                );
            }
            desc = "% trên giá trị đơn hàng";
            discountConditionDetail.setDiscountPercent(Double.parseDouble(request.getValue().toString()));
        } else {
            desc = " đồng";
            discountConditionDetail.setDiscountValue(request.getValue());
        }

        discountConditionDetail.setType(VoucherConstants.Type.VOUCHER_DISCOUNT);
        String description = "Voucher giảm giá " + Common.formatBigDecimal(request.getValue()) + desc;
        discountConditionDetail.setDesc(description);
        discountConditionDetailSave.add(discountConditionDetail);
        JSONArray detailJson = new JSONArray(discountConditionDetailSave);

        condition.setType(VoucherConstants.Type.VOUCHER_DISCOUNT);
        condition.setData(detailJson.toString());
        condition.setNote("");
        conditionSave.add(condition);
        JSONArray conditionJson = new JSONArray(conditionSave);

        voucher.setDiscountConditions(conditionJson.toString());
        voucher.setStartTime(Common.convertStringToDateTime(request.getStartTime(), Constants.ZONED_DATE_FORMAT));
        ZonedDateTime dateTimeConvert = Common.convertStringToDateTime(request.getEndTime(), Constants.ZONED_DATE_FORMAT);
        ZonedDateTime result = dateTimeConvert.plusDays(1);
        result = result.minusSeconds(1);
        voucher.setEndTime(result);
        voucher.setActive(true);
        voucher.setGenDescription(description);
        voucherRepository.save(voucher);

        VoucherCompany voucherCompany = new VoucherCompany();
        if (requestId == null) {
            voucherCompany.setComId(comId);
            voucherCompany.setCompanyName(companyRepository.getNameById(comId));
            voucherCompany.setVoucherId(voucher.getId());
            voucherCompany.setAutoApply(true);
        } else {
            Optional<VoucherCompany> voucherCompanyOptional = voucherCompanyRepository.findOneByVoucherIdAndComId(voucher.getId(), comId);
            if (voucherCompanyOptional.isEmpty()) {
                throw new BadRequestAlertException(VOUCHER_COMPANY_NOT_FOUND_VI, ENTITY_NAME, VOUCHER_COMPANY_NOT_FOUND);
            }
            voucherCompany = voucherCompanyOptional.get();
        }
        voucherCompany.setVoucherCode(voucher.getCode());
        voucherCompanyRepository.save(voucherCompany);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            requestId != null ? ResultConstants.VOUCHER_UPDATE_SUCCESS : ResultConstants.VOUCHER_CREATE_SUCCESS,
            true
        );
    }

    @Override
    public ResultDTO applyVoucher(VoucherApplyRequest request) {
        log.debug("request apply voucher");
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        Integer applyType = request.getApplyType();
        List<VoucherApplyItemRequest> applyItem = request.getApplyItem();
        List<Integer> applyIds = new ArrayList<>();
        List<Integer> voucherApplyIds = new ArrayList<>();
        List<Integer> cardIdsDelete = new ArrayList<>();
        //        List<Integer> voucherIds = request.getVoucherIds();
        Integer voucherId = request.getVoucherId();
        checkVoucherId(comId, voucherId);
        for (VoucherApplyItemRequest item : applyItem) {
            if (item.getId() != null) {
                voucherApplyIds.add(item.getId());
                if (Objects.equals(applyType, VoucherConstants.ApplyType.APPLY_CARD)) {
                    cardIdsDelete.add(item.getApplyId());
                }
            }
            if (item.getId() == null && !Objects.equals(applyType, VoucherConstants.ApplyType.APPLY_ALL_CUSTOMER)) {
                applyIds.add(item.getApplyId());
            }
        }
        List<VoucherApply> voucherAppliesDelete = new ArrayList<>();
        if (!cardIdsDelete.isEmpty() && Objects.equals(applyType, VoucherConstants.ApplyType.APPLY_CARD)) {
            voucherAppliesDelete = voucherApplyRepository.getAllByComIdAndApplyIdInAndApplyType(comId, cardIdsDelete, applyType);
        } else if (!voucherApplyIds.isEmpty() && Objects.equals(applyType, VoucherConstants.ApplyType.APPLY_CUSTOMER)) {
            voucherAppliesDelete = voucherApplyRepository.getAllByComIdAndIdInAndApplyType(comId, voucherApplyIds, applyType);
        }
        if (!voucherAppliesDelete.isEmpty()) {
            log.debug("delete voucherApply");
            voucherApplyRepository.deleteAll(voucherAppliesDelete);
        }

        if (!applyIds.isEmpty()) {
            saveApplyVoucher(comId, applyType, applyIds, voucherId);
        }

        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.VOUCHER_APPLY_SUCCESS, true);
    }

    private void checkVoucherId(Integer comId, Integer voucherId) {
        Integer voucherIdsResult = voucherRepository.checkVoucherById(
            comId,
            voucherId,
            List.of(VoucherConstants.Status.NOT_RUN, VoucherConstants.Status.RUNNING)
        );
        if (voucherIdsResult == null) {
            throw new BadRequestAlertException(VOUCHER_ID_INVALID_VI, ENTITY_NAME, VOUCHER_ID_INVALID);
        }
        //        else if (voucherIdsResult.size() != voucherIds.size()) {
        //            for (Integer voucherId : voucherIds) {
        //                if (!voucherIdsResult.contains(voucherId)) {
        //                    throw new BadRequestAlertException(
        //                        VOUCHER_LIST_ID_INVALID_VI + " id: " + voucherId,
        //                        ENTITY_NAME,
        //                        VOUCHER_LIST_ID_INVALID
        //                    );
        //                }
        //            }
        //        }
    }

    private void checkApplyIds(Integer comId, Integer type, List<Integer> applyIds) {
        Map<String, String> errorMap = new HashMap<>();
        if (type.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
            if (applyIds.isEmpty()) {
                errorMap.put(VOUCHER_LIST_CARD_APPLY_NOT_EMPTY, VOUCHER_LIST_CARD_APPLY_NOT_EMPTY_VI);
            } else {
                List<Integer> cardIds = loyaltyCardRepository.checkAllIds(comId, applyIds);
                if (cardIds.isEmpty()) {
                    errorMap.put(VOUCHER_LIST_CARD_APPLY_INVALID, VOUCHER_LIST_CARD_APPLY_INVALID_VI);
                } else if (cardIds.size() != applyIds.size()) {
                    for (Integer applyId : applyIds) {
                        if (!cardIds.contains(applyId)) {
                            errorMap.put(VOUCHER_LIST_CARD_APPLY_INVALID, VOUCHER_LIST_CARD_APPLY_INVALID_VI + " id: " + applyId);
                        }
                    }
                }
            }
        } else {
            if (applyIds.isEmpty()) {
                errorMap.put(VOUCHER_LIST_CUSTOMER_APPLY_NOT_EMPTY, VOUCHER_LIST_CUSTOMER_APPLY_NOT_EMPTY_VI);
            } else {
                List<Integer> customerIds = customerRepository.checkAllIds(comId, applyIds);
                if (customerIds.isEmpty()) {
                    errorMap.put(VOUCHER_LIST_CUSTOMER_APPLY_INVALID, VOUCHER_LIST_CUSTOMER_APPLY_INVALID_VI);
                } else if (customerIds.size() != applyIds.size()) {
                    for (Integer applyId : applyIds) {
                        if (!customerIds.contains(applyId)) {
                            errorMap.put(VOUCHER_LIST_CUSTOMER_APPLY_INVALID, VOUCHER_LIST_CUSTOMER_APPLY_INVALID_VI + " id: " + applyId);
                        }
                    }
                }
            }
        }
        if (!errorMap.isEmpty()) {
            for (Map.Entry<String, String> item : errorMap.entrySet()) {
                throw new BadRequestAlertException(item.getValue(), ENTITY_NAME, item.getKey());
            }
        }
    }

    private List<Integer> getAllStatusForListPaging() {
        return List.of(
            VoucherConstants.Status.NOT_RUN,
            VoucherConstants.Status.RUNNING,
            VoucherConstants.Status.STOP,
            VoucherConstants.Status.DONE
        );
    }

    @Override
    public ResultDTO getWithPaging(Integer comId, String keyword, Pageable pageable, String fromDate, String toDate) {
        Page<VoucherWebResponse> responsePage = voucherRepository.getWithPagingWeb(
            comId,
            pageable,
            keyword,
            getAllStatusForListPaging(),
            fromDate,
            toDate
        );
        log.debug("request get all voucher");
        List<VoucherWebResponse> responses = new ArrayList<>();
        if (!responsePage.isEmpty()) {
            responses = responsePage.getContent();
            List<Integer> voucherIds = responses.stream().map(VoucherWebResponse::getId).collect(Collectors.toList());

            MultiValuedMap<Integer, String> applyTypeMap = getApplyTypeName(voucherIds);
            ObjectMapper objectMapper = new ObjectMapper();
            responses.forEach(r -> {
                if (applyTypeMap.containsKey(r.getId())) {
                    String applyTypeName = "";
                    List<String> applyTypeNameList = new ArrayList<>(applyTypeMap.get(r.getId()));
                    if (!applyTypeNameList.isEmpty()) {
                        applyTypeName = applyTypeNameList.toString().substring(1, applyTypeNameList.toString().length() - 1);
                    }
                    r.setApplyType(applyTypeName);
                }
                if (!Strings.isNullOrEmpty(r.getDiscountCondition())) {
                    try {
                        VoucherDiscountCondition[] conditions = objectMapper.readValue(
                            r.getDiscountCondition(),
                            VoucherDiscountCondition[].class
                        );
                        for (VoucherDiscountCondition condition : conditions) {
                            if (Objects.equals(condition.getType(), VoucherConstants.Type.VOUCHER_DISCOUNT)) {
                                VoucherDiscountConditionDetail[] conditionDetails = objectMapper.readValue(
                                    condition.getData(),
                                    VoucherDiscountConditionDetail[].class
                                );
                                VoucherDiscountConditionDetail detail = conditionDetails[0];
                                if (detail.getDiscountPercent() != null) {
                                    r.setDiscountPercent(detail.getDiscountPercent());
                                }
                                if (detail.getDiscountValue() != null) {
                                    r.setDiscountValue(detail.getDiscountValue());
                                }
                                r.setDesc(detail.getDesc());
                            }
                        }
                    } catch (JsonProcessingException e) {
                        throw new BadRequestAlertException(
                            VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID_VI,
                            ENTITY_NAME,
                            VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID
                        );
                    }
                }
                r.setDiscountCondition(null);
            });
        }

        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.VOUCHER_GET_ALL_SUCCESS,
            true,
            responses,
            (int) responsePage.getTotalElements()
        );
    }

    @Override
    public ResultDTO getVoucherApplyDetail(
        Integer comId,
        Integer voucherId,
        Integer type,
        Pageable pageable,
        String keyword,
        Boolean getDefault
    ) {
        userService.getUserWithAuthorities(comId);
        List<Object> response = new ArrayList<>();
        List<VoucherApplyCustomerDetail> applyCustomerResult = new ArrayList<>();
        List<VoucherApplyCardDetail> applyCardResult = new ArrayList<>();
        checkVoucherId(comId, voucherId);
        Integer sizePage = null;
        if (Objects.equals(type, VoucherConstants.ApplyType.APPLY_CARD)) {
            Page<CardItemResult> cardResultPage = loyaltyCardRepository.findAllByComId(
                comId,
                Common.normalizedName(List.of(keyword == null ? "" : keyword)),
                pageable
            );
            List<CardItemResult> cards = cardResultPage.getContent();
            if (!cards.isEmpty()) {
                sizePage = (int) cardResultPage.getTotalElements();
                List<Integer> cardIds = cards.stream().map(CardItemResult::getCardId).collect(Collectors.toList());
                List<VoucherApplyCardItem> voucherApplyCardItems = new ArrayList<>();
                if (!getDefault) {
                    voucherApplyCardItems =
                        voucherApplyRepository.getAllVoucherApplyByVoucherIdAndCard(
                            comId,
                            VoucherConstants.ApplyType.APPLY_CARD,
                            voucherId,
                            cardIds
                        );
                }
                Map<Integer, VoucherApplyCardItem> applyMap = new HashMap<>();
                if (!voucherApplyCardItems.isEmpty()) {
                    voucherApplyCardItems.forEach(apply -> applyMap.put(apply.getCardId(), apply));
                }
                cards.forEach(c -> {
                    VoucherApplyCardDetail item = new VoucherApplyCardDetail();
                    item.setCardId(c.getCardId());
                    item.setCardName(c.getCardName());
                    item.setTotalCustomer(c.getTotalCustomer());
                    if (applyMap.containsKey(c.getCardId())) {
                        VoucherApplyCardItem detailItem = applyMap.get(c.getCardId());
                        item.setId(detailItem.getId());
                        item.setVoucherId(detailItem.getVoucherId());
                    }
                    applyCardResult.add(item);
                });
                response = new ArrayList<>(applyCardResult);
            }
        } else {
            Page<CustomerItemResult> customerPage = customerRepository.getAllCustomerItem(comId, pageable, keyword);
            List<CustomerItemResult> customers = customerPage.getContent();
            if (!customers.isEmpty()) {
                sizePage = (int) customerPage.getTotalElements();
                List<Integer> customerIds = customers.stream().map(CustomerItemResult::getCustomerId).collect(Collectors.toList());
                List<VoucherApplyCustomerItem> voucherApplyCustomerItems = new ArrayList<>();
                if (!getDefault) {
                    voucherApplyCustomerItems =
                        voucherApplyRepository.getAllVoucherApplyByVoucherIdAndCustomer(
                            comId,
                            VoucherConstants.ApplyType.APPLY_CUSTOMER,
                            voucherId,
                            customerIds
                        );
                }
                Map<Integer, VoucherApplyCustomerItem> applyMap = new HashMap<>();
                if (!voucherApplyCustomerItems.isEmpty()) {
                    voucherApplyCustomerItems.forEach(apply -> applyMap.put(apply.getCustomerId(), apply));
                }
                customers.forEach(c -> {
                    VoucherApplyCustomerDetail item = new VoucherApplyCustomerDetail();
                    item.setCustomerId(c.getCustomerId());
                    item.setCustomerName(c.getCustomerName());
                    item.setTaxCode(c.getTaxCode());
                    item.setCode2(c.getCode2());
                    if (applyMap.containsKey(c.getCustomerId())) {
                        VoucherApplyCustomerItem detailItem = applyMap.get(c.getCustomerId());
                        item.setId(detailItem.getId());
                        item.setVoucherId(detailItem.getVoucherId());
                    }
                    applyCustomerResult.add(item);
                });
                response = new ArrayList<>(applyCustomerResult);
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.VOUCHER_APPLY_GET_DETAIL_SUCCESS, true, response, sizePage);
    }

    /**
     * -  applyItem sẽ là danh sách ngoại lệ
     *      +   1= Chọn tất cả
     *      +   2= Xoá tất cả
     */
    @Override
    public ResultDTO applyAllVoucher(VoucherApplyAllRequest request) {
        log.debug("request apply all by voucher");
        Integer comId = request.getComId();
        userService.getUserWithAuthorities(comId);
        Integer voucherId = request.getVoucherId();
        checkVoucherId(comId, voucherId);
        Integer applyType = request.getApplyType();
        Integer checkAllType = request.getCheckAllType();
        List<VoucherApplyItemRequest> requestItem = request.getApplyItem();
        List<Integer> requestIds = new ArrayList<>();
        List<Integer> requestApplyIds = new ArrayList<>();
        requestApplyIds.add(-1);
        requestIds.add(-1);
        requestItem.forEach(item -> {
            requestIds.add(item.getId());
            requestApplyIds.add(item.getApplyId());
        });
        List<Integer> cardIdsAll = new ArrayList<>();
        List<Integer> customerIdsAll = new ArrayList<>();
        List<Integer> applyIds = new ArrayList<>();

        if (applyType.equals(1)) {
            cardIdsAll = loyaltyCardRepository.getAllIdsByComId(comId);
        } else {
            customerIdsAll =
                customerRepository.getAllIdByComIdAndTypeIn(
                    comId,
                    List.of(CustomerConstants.Type.CUSTOMER, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER)
                );
        }

        List<VoucherApply> voucherAppliesOld;
        List<VoucherApply> voucherAppliesDelete;
        if (checkAllType.equals(1)) {
            // xoá những bản ghi đã có ở voucher_apply
            voucherAppliesDelete = voucherApplyRepository.getAllByComIdAndIdIn(comId, requestIds);
            if (applyType.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
                voucherAppliesOld =
                    voucherApplyRepository.getAllVoucherApplyByCard(comId, VoucherConstants.ApplyType.APPLY_CARD, cardIdsAll, voucherId);
            } else {
                voucherAppliesOld =
                    voucherApplyRepository.getAllVoucherApplyByCustomer(
                        comId,
                        VoucherConstants.ApplyType.APPLY_CUSTOMER,
                        customerIdsAll,
                        voucherId
                    );
            }
            // lọc item cũ
            for (VoucherApply item : voucherAppliesOld) {
                if (!customerIdsAll.isEmpty()) {
                    customerIdsAll.remove(item.getCustomerId());
                } else if (!cardIdsAll.isEmpty()) {
                    cardIdsAll.remove(item.getApplyId());
                }
            }

            for (Integer item : requestApplyIds) {
                if (applyType.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
                    cardIdsAll.remove(item);
                    continue;
                }
                customerIdsAll.remove(item);
            }

            applyIds = applyType.equals(VoucherConstants.ApplyType.APPLY_CARD) ? cardIdsAll : customerIdsAll;
        } else {
            List<Integer> applyIdsOld;
            if (applyType.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
                voucherAppliesDelete = voucherApplyRepository.getAllVoucherApplyCardAndIdNotIn(comId, applyType, requestApplyIds);
                applyIdsOld =
                    voucherApplyRepository.getAllIdsByComIdAndApplyIdInAndApplyTypeCard(
                        comId,
                        requestApplyIds,
                        VoucherConstants.ApplyType.APPLY_CARD
                    );
            } else {
                voucherAppliesDelete = voucherApplyRepository.getAllVoucherApplyCustomerAndIdNotIn(comId, applyType, requestApplyIds);
                applyIdsOld =
                    voucherApplyRepository.getAllIdsByComIdAndApplyIdInAndApplyTypeCustomer(
                        comId,
                        requestApplyIds,
                        VoucherConstants.ApplyType.APPLY_CUSTOMER
                    );
            }
            applyIdsOld.add(-1);
            applyIdsOld.forEach(requestApplyIds::remove);
            applyIds = requestApplyIds;
        }

        if (!voucherAppliesDelete.isEmpty()) {
            voucherApplyRepository.deleteAll(voucherAppliesDelete);
        }
        if (!applyIds.isEmpty()) {
            saveApplyVoucher(comId, applyType, applyIds, voucherId);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.VOUCHER_APPLY_SUCCESS, true);
    }

    private void saveApplyVoucher(Integer comId, Integer applyType, List<Integer> applyIds, Integer voucherId) {
        checkApplyIds(comId, applyType, applyIds);
        List<VoucherApply> voucherApplies = new ArrayList<>();

        List<CustomerCard> customerCards;
        MultiValuedMap<Integer, Integer> cardMap = new HashSetValuedHashMap<>();
        if (applyType.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
            customerCards = customerCardRepository.getAllCustomerIdsByCardIds(comId, applyIds);
            if (!customerCards.isEmpty()) {
                customerCards.forEach(cc -> cardMap.put(cc.getCardId(), cc.getCustomerId()));
            }
            if (cardMap.keySet().size() != applyIds.size()) {
                applyIds.forEach(id -> {
                    if (!cardMap.containsKey(id)) {
                        cardMap.put(id, null);
                    }
                });
            }
        }
        if (applyType.equals(VoucherConstants.ApplyType.APPLY_CARD)) {
            for (Integer cardId : cardMap.keySet()) {
                if (cardMap.get(cardId) != null) {
                    List<Integer> customerIds = new ArrayList<>(cardMap.get(cardId));
                    for (Integer customerId : customerIds) {
                        VoucherApply voucherApply = new VoucherApply();
                        voucherApply.setComId(comId);
                        voucherApply.setVoucherId(voucherId);
                        voucherApply.setCustomerId(customerId);
                        voucherApply.setApplyId(cardId);
                        voucherApply.setApplyType(VoucherConstants.ApplyType.APPLY_CARD);
                        voucherApplies.add(voucherApply);
                    }
                } else {
                    VoucherApply voucherApply = new VoucherApply();
                    voucherApply.setComId(comId);
                    voucherApply.setVoucherId(voucherId);
                    voucherApply.setApplyId(cardId);
                    voucherApply.setApplyType(VoucherConstants.ApplyType.APPLY_CARD);
                    voucherApplies.add(voucherApply);
                }
            }
        } else {
            for (Integer customerId : applyIds) {
                VoucherApply voucherApply = new VoucherApply();
                voucherApply.setComId(comId);
                voucherApply.setVoucherId(voucherId);
                voucherApply.setCustomerId(customerId);
                voucherApply.setApplyType(VoucherConstants.ApplyType.APPLY_CUSTOMER);
                voucherApplies.add(voucherApply);
            }
        }
        voucherApplyRepository.saveAll(voucherApplies);
    }

    public MultiValuedMap<Integer, String> getApplyTypeName(List<Integer> voucherIds) {
        MultiValuedMap<Integer, String> applyTypeMap = new HashSetValuedHashMap<>();
        List<VoucherApplyTypeItem> applyTypes = voucherApplyRepository.getAllApplyTypeByVoucherId(voucherIds);
        if (!applyTypes.isEmpty()) {
            applyTypes.forEach(type -> {
                if (Objects.equals(type.getApplyType(), VoucherConstants.ApplyType.APPLY_CARD)) {
                    applyTypeMap.put(type.getVoucherId(), VoucherConstants.ApplyType.APPLY_CARD_NAME);
                } else if (Objects.equals(type.getApplyType(), VoucherConstants.ApplyType.APPLY_CUSTOMER)) {
                    applyTypeMap.put(type.getVoucherId(), VoucherConstants.ApplyType.APPLY_CUSTOMER_NAME);
                }
            });
        }
        return applyTypeMap;
    }

    @Override
    public ResultDTO deleteVoucher(Integer id) {
        log.debug("request delete voucher");
        User user = userService.getUserWithAuthorities();
        Integer comId = user.getCompanyId();
        Optional<Voucher> voucherOptional = voucherRepository.findOneByIdIgnoreStatus(id, comId, VoucherConstants.Status.DELETED);
        if (voucherOptional.isEmpty()) {
            throw new BadRequestAlertException(VOUCHER_NOT_FOUND_VI, ENTITY_NAME, VOUCHER_NOT_FOUND);
        }
        Voucher voucher = voucherOptional.get();
        List<Integer> billIds = voucherUsageRepository.getBillIdByVoucherId(comId, id);
        if (!billIds.isEmpty()) {
            Integer countStatusBillNotCompleted = billRepository.countStatusBillNotCompleted(
                billIds,
                BillConstants.Status.BILL_DONT_COMPLETE
            );
            if (countStatusBillNotCompleted != null && countStatusBillNotCompleted > 0) {
                throw new BadRequestAlertException(VOUCHER_DELETE_BILL_INVALID_VI, ENTITY_NAME, VOUCHER_DELETE_BILL_INVALID);
            }
        }
        voucher.setStatus(VoucherConstants.Status.DELETED);
        voucherRepository.save(voucher);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.VOUCHER_DELETE_SUCCESS, true);
    }

    @Override
    public ResultDTO getAllVoucherForBill(Integer customerId, String keyword, Pageable pageable) {
        User user = userService.getUserWithAuthorities();
        Optional<Customer> customerOptional = customerRepository.findOneByIdAndComId(customerId, user.getCompanyId());
        if (customerOptional.isEmpty() || !customerOptional.get().getActive()) {
            throw new BadRequestAlertException(CUSTOMER_NOT_FOUND_VI, ENTITY_NAME, CUSTOMER_NOT_FOUND);
        }
        Page<VoucherWebResponse> responsePage = voucherRepository.getVoucherForBillWeb(user.getCompanyId(), pageable, keyword, customerId);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.VOUCHER_GET_ALL_SUCCESS,
            true,
            processDataForVoucher(responsePage),
            (int) responsePage.getTotalElements()
        );
    }

    @Override
    public ResultDTO checkValidVoucher(List<Integer> ids) {
        User user = userService.getUserWithAuthorities();
        List<Voucher> voucherList = voucherRepository.findAllByComIdAndIdIn(user.getCompanyId(), ids);
        List<VoucherValidResponse> responses = new ArrayList<>();
        for (Voucher voucher : voucherList) {
            VoucherValidResponse validResponse = new VoucherValidResponse();
            BeanUtils.copyProperties(voucher, validResponse);
            if (!Objects.equals(voucher.getStatus(), VoucherConstants.Status.RUNNING)) {
                validResponse.setErrorMessage(VOUCHER_NOT_RUNNING_VI.replace("@@name", voucher.getName()));
                responses.add(validResponse);
            } else if (voucher.getStartTime().isAfter(ZonedDateTime.now())) {
                validResponse.setErrorMessage(VOUCHER_NOT_RUNNING_VI.replace("@@name", voucher.getName()));
                responses.add(validResponse);
            } else if (voucher.getEndTime().isBefore(ZonedDateTime.now())) {
                validResponse.setErrorMessage(VOUCHER_IS_END_DATE_VI.replace("@@name", voucher.getName()));
                responses.add(validResponse);
            }
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, responses.isEmpty() ? null : responses);
    }

    private List<VoucherWebResponse> processDataForVoucher(Page<VoucherWebResponse> responsePage) {
        List<VoucherWebResponse> responses = new ArrayList<>();
        if (!responsePage.isEmpty()) {
            responses = responsePage.getContent();
            List<Integer> voucherIds = responses.stream().map(VoucherWebResponse::getId).collect(Collectors.toList());

            MultiValuedMap<Integer, String> applyTypeMap = getApplyTypeName(voucherIds);
            ObjectMapper objectMapper = new ObjectMapper();
            responses.forEach(r -> {
                if (applyTypeMap.containsKey(r.getId())) {
                    String applyTypeName = "";
                    List<String> applyTypeNameList = new ArrayList<>(applyTypeMap.get(r.getId()));
                    if (!applyTypeNameList.isEmpty()) {
                        applyTypeName = applyTypeNameList.toString().substring(1, applyTypeNameList.toString().length() - 1);
                    }
                    r.setApplyType(applyTypeName);
                }
                if (!Strings.isNullOrEmpty(r.getDiscountCondition())) {
                    try {
                        VoucherDiscountCondition[] conditions = objectMapper.readValue(
                            r.getDiscountCondition(),
                            VoucherDiscountCondition[].class
                        );
                        for (VoucherDiscountCondition condition : conditions) {
                            if (Objects.equals(condition.getType(), VoucherConstants.Type.VOUCHER_DISCOUNT)) {
                                VoucherDiscountConditionDetail[] conditionDetails = objectMapper.readValue(
                                    condition.getData(),
                                    VoucherDiscountConditionDetail[].class
                                );
                                VoucherDiscountConditionDetail detail = conditionDetails[0];
                                if (detail.getDiscountPercent() != null) {
                                    r.setDiscountPercent(detail.getDiscountPercent());
                                }
                                if (detail.getDiscountValue() != null) {
                                    r.setDiscountValue(detail.getDiscountValue());
                                }
                                r.setDesc(detail.getDesc());
                            }
                        }
                    } catch (JsonProcessingException e) {
                        throw new BadRequestAlertException(
                            VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID_VI,
                            ENTITY_NAME,
                            VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID
                        );
                    }
                }
                r.setDiscountCondition(null);
            });
        }
        return responses;
    }
}
