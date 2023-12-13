package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.json.JSONArray;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.card.*;
import vn.softdreams.easypos.dto.customer.CustomerCardItem;
import vn.softdreams.easypos.dto.customer.CustomerResponse;
import vn.softdreams.easypos.dto.customerCard.CustomerCardRankItem;
import vn.softdreams.easypos.dto.loyaltyCard.LoyaltyCardItem;
import vn.softdreams.easypos.dto.loyaltyCard.LoyaltyCardResultItem;
import vn.softdreams.easypos.dto.loyaltyCard.SaveLoyaltyCardRequest;
import vn.softdreams.easypos.dto.loyaltyCard.SortRankCardRequest;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.repository.*;
import vn.softdreams.easypos.service.CardManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.DataResponse;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link LoyaltyCard}.
 */
@Service
@Transactional
public class CardManagementServiceImpl implements CardManagementService {

    private final Logger log = LoggerFactory.getLogger(CardManagementServiceImpl.class);
    private static final String ENTITY_NAME = "card";

    private final LoyaltyCardRepository loyaltyCardRepository;
    private final CustomerCardRepository customerCardRepository;
    private final ModelMapper modelMapper;
    private final UserService userService;
    private final CardPolicyRepository cardPolicyRepository;
    private final LoyaltyCardUsageRepository loyaltyCardUsageRepository;
    private final CustomerRepository customerRepository;
    private final CustomerManagementServiceImpl customerManagementService;
    private final VoucherApplyRepository voucherApplyRepository;

    public CardManagementServiceImpl(
        LoyaltyCardRepository loyaltyCardRepository,
        CustomerCardRepository customerCardRepository,
        CardPolicyRepository cardPolicyRepository,
        ModelMapper modelMapper,
        UserService userService,
        LoyaltyCardUsageRepository loyaltyCardUsageRepository,
        CustomerRepository customerRepository,
        CustomerManagementServiceImpl customerManagementService,
        VoucherApplyRepository voucherApplyRepository
    ) {
        this.loyaltyCardRepository = loyaltyCardRepository;
        this.customerCardRepository = customerCardRepository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.cardPolicyRepository = cardPolicyRepository;
        this.loyaltyCardUsageRepository = loyaltyCardUsageRepository;
        this.customerRepository = customerRepository;
        this.customerManagementService = customerManagementService;
        this.voucherApplyRepository = voucherApplyRepository;
    }

    @Override
    public ResultDTO getAllCustomerCard(Pageable pageable, String keyword, Boolean isCountAll) {
        User user = userService.getUserWithAuthorities();
        Page<LoyaltyCardResultItem> loyaltyCardItems = loyaltyCardRepository.getAllCustomerCard(
            null,
            user.getCompanyId(),
            keyword,
            null,
            isCountAll,
            false,
            null
        );
        Map<Integer, LoyaltyCardItem> response = new LinkedHashMap<>();
        Map<Integer, List<CustomerResponse>> cusMap = new HashMap<>();
        for (LoyaltyCardResultItem item : loyaltyCardItems.getContent()) {
            if (!Strings.isNullOrEmpty(item.getCode()) && item.getCode().equals(CommonConstants.CUSTOMER_CODE_DEFAULT)) {
                continue;
            }
            LoyaltyCardItem cardItem = new LoyaltyCardItem();
            List<CustomerResponse> customerResponses = new ArrayList<>();
            if (cusMap.containsKey(item.getId())) {
                customerResponses = cusMap.get(item.getId());
            }
            if (item.getCustomerId() != null) {
                CustomerResponse customerResponse = modelMapper.map(item, CustomerResponse.class);
                customerResponse.setId(item.getCustomerId());
                customerResponse.setName(item.getCustomerName());
                customerResponse.setComId(user.getCompanyId());
                customerResponses.add(customerResponse);
            }
            if (response.containsKey(item.getId())) {
                cardItem = response.get(item.getId());
            } else {
                cardItem.setId(item.getId());
                cardItem.setName(item.getName());
                cardItem.setIsDefault(item.getIsDefault());
                cardItem.setRank(item.getRank());
                cardItem.setStatus(item.getStatus());
            }
            cardItem.setCount(customerResponses.size());
            cardItem.setCustomers(customerManagementService.getCardBalance(user.getCompanyId(), customerResponses));
            cusMap.put(item.getId(), customerResponses);
            response.put(item.getId(), cardItem);
        }
        List<LoyaltyCardItem> result = new ArrayList<>(response.values());
        result = result.subList(0, Math.min(pageable.getPageSize(), response.values().size()));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_LIST, true, result, result.size());
    }

    @Override
    public ResultDTO saveCard(SaveLoyaltyCardRequest request) {
        User user = userService.getUserWithAuthorities();
        boolean isNew = request.getId() == null;
        Integer transferCard = -1;
        boolean changeToDefaultCard = Boolean.FALSE;
        if (request.getIsDefault() == null) {
            request.setIsDefault(Boolean.FALSE);
        }
        if (Strings.isNullOrEmpty(request.getName().trim())) {
            throw new BadRequestAlertException(
                ExceptionConstants.CARD_NAME_NOT_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.CARD_NAME_NOT_NULL_CODE
            );
        }
        request.setName(request.getName().trim());
        LoyaltyCard loyaltyCard;
        if (isNew) {
            loyaltyCard = new LoyaltyCard();
        } else {
            Optional<LoyaltyCard> optional = loyaltyCardRepository.findByIdAndComId(request.getId(), user.getCompanyId());
            if (optional.isEmpty()) {
                throw new BadRequestAlertException(ExceptionConstants.CARD_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CARD_NOT_FOUND);
            }
            loyaltyCard = optional.get();
        }
        List<Integer> status = List.of(LoyaltyCardConstants.Status.NGUNG_HOAT_DONG, LoyaltyCardConstants.Status.HOAT_DONG);
        if (isNew) {
            Optional<LoyaltyCard> loyaltyCardOptional = loyaltyCardRepository.findOneByComIdAndNameIgnoreCaseAndStatusIn(
                user.getCompanyId(),
                request.getName(),
                status
            );
            if (loyaltyCardOptional.isPresent()) {
                throw new BadRequestAlertException(ExceptionConstants.CARD_NAME_EXIST_VI, ENTITY_NAME, ExceptionConstants.CARD_NAME_EXIST);
            }
            if (request.getIsDefault() && Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DEFAULT_CARD_NOT_STOP_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DEFAULT_CARD_NOT_STOP
                );
            }
            Optional<LoyaltyCard> optionalLoyaltyCard = loyaltyCardRepository.findByComIdAndIsDefaultAndStatusIn(
                user.getCompanyId(),
                Boolean.TRUE,
                status
            );
            if (optionalLoyaltyCard.isPresent()) {
                if (request.getIsDefault()) {
                    transferCard = optionalLoyaltyCard.get().getId();
                }
            } else {
                changeToDefaultCard = Boolean.TRUE;
            }
        } else {
            Integer count = loyaltyCardRepository.checkDuplicateName(user.getCompanyId(), request.getName(), status, request.getId());
            if (count > 0) {
                throw new BadRequestAlertException(ExceptionConstants.CARD_NAME_EXIST_VI, ENTITY_NAME, ExceptionConstants.CARD_NAME_EXIST);
            }
            if (loyaltyCard.getIsDefault() && Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DEFAULT_CARD_NOT_STOP_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DEFAULT_CARD_NOT_STOP
                );
            }
            if (Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)) {
                Integer previousRank = loyaltyCardRepository.getPreviousRank(user.getCompanyId(), request.getId());
                if (previousRank != null) {
                    List<CustomerCard> customerCards = customerCardRepository.getAllByComIdAndCardId(user.getCompanyId(), request.getId());
                    customerCards.forEach(customerCard -> customerCard.setCardId(previousRank));
                }
            }
            Optional<LoyaltyCard> optionalLoyaltyCard = loyaltyCardRepository.findByComIdAndIsDefaultAndStatusIn(
                user.getCompanyId(),
                Boolean.TRUE,
                status
            );
            if (request.getIsDefault()) {
                if (Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)) {
                    throw new BadRequestAlertException(
                        ExceptionConstants.DEFAULT_CARD_NOT_STOP_VI,
                        ENTITY_NAME,
                        ExceptionConstants.DEFAULT_CARD_NOT_STOP
                    );
                }
                if (optionalLoyaltyCard.isPresent()) {
                    if (!Objects.equals(optionalLoyaltyCard.get().getId(), request.getId())) {
                        transferCard = optionalLoyaltyCard.get().getId();
                    }
                }
            } else {
                if (optionalLoyaltyCard.isPresent()) {
                    if (Objects.equals(optionalLoyaltyCard.get().getId(), request.getId())) {
                        throw new BadRequestAlertException(
                            ExceptionConstants.DEFAULT_CARD_NOT_DELETE_VN,
                            ENTITY_NAME,
                            ExceptionConstants.DEFAULT_CARD_NOT_DELETE
                        );
                    } else if (Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)) {
                        resetCardPolicy(
                            request.getId(),
                            user.getCompanyId(),
                            Boolean.FALSE,
                            Boolean.TRUE,
                            Boolean.FALSE,
                            Boolean.FALSE,
                            Boolean.FALSE,
                            null
                        );
                    }
                }
            }
            resetCardPolicy(
                request.getId(),
                user.getCompanyId(),
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.TRUE,
                Boolean.FALSE,
                request.getName()
            );
        }
        BeanUtils.copyProperties(request, loyaltyCard);
        loyaltyCard.setComId(user.getCompanyId());
        if (isNew) {
            loyaltyCard.setRank(loyaltyCardRepository.countAllByComIdAndStatusIn(user.getCompanyId(), status) + 1);
        }
        loyaltyCard.setNormalizedName(Common.normalizedName(Arrays.asList(request.getName())));
        loyaltyCardRepository.save(loyaltyCard);
        if (isNew && request.getIsDefault()) {
            resetCardPolicy(
                loyaltyCard.getId(),
                user.getCompanyId(),
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.TRUE,
                Boolean.FALSE,
                Boolean.FALSE,
                loyaltyCard.getName()
            );
        }
        if (changeToDefaultCard) {
            List<Integer> ids = customerRepository.getAllCustomerForDefaultCard(
                user.getCompanyId(),
                List.of(CustomerConstants.Type.CUSTOMER, CustomerConstants.Type.CUSTOMER_AND_SUPPLIER)
            );
            List<CustomerCard> nullCardIds = customerCardRepository.getAllByComIdAndCardId(user.getCompanyId(), null);
            List<CustomerCard> customerCards = new ArrayList<>();
            for (Integer id : ids) {
                CustomerCard customerCard = new CustomerCard();
                customerCard.setCustomerId(id);
                customerCard.setComId(user.getCompanyId());
                customerCard.setCardId(loyaltyCard.getId());
                customerCard.setAmount(BigDecimal.ZERO);
                customerCard.setPoint(0);
                customerCard.setStartDate(ZonedDateTime.now());
                customerCards.add(customerCard);
            }
            for (CustomerCard customerCard : nullCardIds) {
                customerCard.setCardId(loyaltyCard.getId());
                customerCards.add(customerCard);
            }
            customerCardRepository.saveAll(customerCards);
        }
        if (transferCard != -1 && Objects.equals(request.getStatus(), LoyaltyCardConstants.Status.HOAT_DONG)) {
            Optional<LoyaltyCard> loyaltyCardOptional = loyaltyCardRepository.findByIdAndComId(transferCard, user.getCompanyId());
            if (loyaltyCardOptional.isPresent()) {
                LoyaltyCard card = loyaltyCardOptional.get();
                card.setDefault(Boolean.FALSE);
                List<CustomerCard> customerCards = customerCardRepository.getAllByComIdAndCardId(user.getCompanyId(), card.getId());
                for (CustomerCard customerCard : customerCards) {
                    customerCard.setCardId(loyaltyCard.getId());
                }
            }
            resetCardPolicy(
                loyaltyCard.getId(),
                user.getCompanyId(),
                Boolean.TRUE,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                Boolean.FALSE,
                loyaltyCard.getName()
            );
        }
        if (isNew) {
            resetRankAfterSaveCard(request, user, loyaltyCard, null);
        } else {
            resetRankAfterSaveCard(request, user, loyaltyCard, loyaltyCard.getRank());
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            isNew ? ResultConstants.CREATE_CARD_SUCCESS : ResultConstants.UPDATE_CARD_SUCCESS,
            true
        );
    }

    private void resetCardPolicy(
        Integer cardId,
        Integer comId,
        Boolean changeDefault,
        Boolean changeStatus,
        Boolean createCard,
        Boolean updateCard,
        Boolean deleteCard,
        String cardName
    ) {
        List<CardPolicy> cardPolicyList = cardPolicyRepository.findByComIdAndStatus(comId, CardPolicyConstants.Status.RUNNING);
        if (cardPolicyList == null || cardPolicyList.isEmpty()) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
            String dateString = formatter.format(ZonedDateTime.now());
            CardPolicy cardPolicy = new CardPolicy();
            cardPolicy.setComId(comId);
            cardPolicy.setUpgradeType(CardPolicyConstants.UpgradeType.TOTAL_SPENDING);
            cardPolicy.setStatus(CardPolicyConstants.Status.RUNNING);
            cardPolicy.setStartTime(Common.convertStringToDateTime(dateString, Constants.ZONED_DATE_FORMAT));
            List<CardPolicyConditions> conditionsSave = new ArrayList<>();
            if (createCard) {
                CardPolicyConditions conditions = new CardPolicyConditions();
                conditions.setUpgradeValue(BigDecimal.ZERO);
                conditions.setCardId(cardId);
                conditions.setCardName(cardName);
                conditionsSave.add(conditions);
            }
            JSONArray convertConditionToJson = new JSONArray(conditionsSave);
            cardPolicy.setConditions(convertConditionToJson.toString());
            cardPolicyRepository.save(cardPolicy);
        }
        for (CardPolicy cardPolicy : cardPolicyList) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<CardPolicyConditions> conditionsSave = new ArrayList<>();
            if (createCard) {
                CardPolicyConditions conditions = new CardPolicyConditions();
                conditions.setUpgradeValue(BigDecimal.ZERO);
                conditions.setCardId(cardId);
                conditions.setCardName(cardName);
                conditionsSave.add(conditions);
            }
            if (cardPolicy.getConditions() != null) {
                try {
                    boolean isExist = Boolean.FALSE;
                    CardPolicyConditions[] conditions = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                    for (CardPolicyConditions condition : conditions) {
                        if (Objects.equals(condition.getCardId(), cardId)) {
                            isExist = Boolean.TRUE;
                            if (changeDefault) {
                                condition.setUpgradeValue(BigDecimal.ZERO);
                            }
                            if (updateCard) {
                                condition.setCardName(cardName);
                            }
                            if (changeStatus || deleteCard) {
                                continue;
                            }
                        }
                        conditionsSave.add(condition);
                    }
                    if (!isExist && !createCard && !updateCard) {
                        CardPolicyConditions condition = new CardPolicyConditions();
                        condition.setUpgradeValue(BigDecimal.ZERO);
                        condition.setCardId(cardId);
                        condition.setCardName(cardName);
                        conditionsSave.add(condition);
                    }
                    JSONArray convertConditionToJson = new JSONArray(conditionsSave);
                    cardPolicy.setConditions(convertConditionToJson.toString());
                } catch (JsonProcessingException e) {
                    log.error("Error when convert card policy condition " + e.getMessage());
                }
            }
        }
    }

    private void resetRankAfterSaveCard(SaveLoyaltyCardRequest request, User user, LoyaltyCard loyaltyCard, Integer oldRank) {
        if (request.getIsDefault()) {
            List<LoyaltyCard> loyaltyCardList;
            if (oldRank == null) {
                loyaltyCardList =
                    loyaltyCardRepository.findAllByComIdAndStatusIn(
                        user.getCompanyId(),
                        List.of(LoyaltyCardConstants.Status.NGUNG_HOAT_DONG, LoyaltyCardConstants.Status.HOAT_DONG)
                    );
            } else {
                loyaltyCardList = loyaltyCardRepository.findByComIdAndRankIsLessThan(user.getCompanyId(), oldRank);
            }
            loyaltyCard.setRank(1);
            for (LoyaltyCard card : loyaltyCardList) {
                if (Objects.equals(card.getId(), loyaltyCard.getId())) {
                    continue;
                }
                card.setRank(card.getRank() + 1);
            }
        }
    }

    @Override
    public ResultDTO getDetailById(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<LoyaltyCard> loyaltyCardOptional = loyaltyCardRepository.findByIdAndComId(id, user.getCompanyId());
        if (loyaltyCardOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.CARD_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CARD_NOT_FOUND);
        }
        LoyaltyCardItem cardItem = new LoyaltyCardItem();
        List<LoyaltyCardResultItem> loyaltyCards = loyaltyCardRepository
            .getAllCustomerCard(null, user.getCompanyId(), null, id, Boolean.TRUE, false, null)
            .getContent();
        List<CustomerResponse> customerResponses = new ArrayList<>();
        if (!loyaltyCards.isEmpty()) {
            for (LoyaltyCardResultItem item : loyaltyCards) {
                if (cardItem.getId() == null) {
                    cardItem.setId(item.getId());
                    cardItem.setName(item.getName());
                    cardItem.setIsDefault(item.getIsDefault());
                    cardItem.setRank(item.getRank());
                    cardItem.setStatus(item.getStatus());
                }
                if (item.getCustomerId() != null) {
                    CustomerResponse customerResponse = modelMapper.map(item, CustomerResponse.class);
                    customerResponse.setId(item.getCustomerId());
                    customerResponse.setName(item.getCustomerName());
                    customerResponse.setComId(user.getCompanyId());
                    customerResponses.add(customerResponse);
                }
            }
            cardItem.setCount(customerResponses.size());
            cardItem.setCustomers(customerManagementService.getCardBalance(user.getCompanyId(), customerResponses));
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_DETAIL, true, cardItem);
    }

    @Override
    public ResultDTO deleteCard(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<LoyaltyCard> loyaltyCardOptional = loyaltyCardRepository.findByIdAndComId(id, user.getCompanyId());
        if (loyaltyCardOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.CARD_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.CARD_NOT_FOUND);
        }
        LoyaltyCard loyaltyCard = loyaltyCardOptional.get();
        if (loyaltyCard.getIsDefault()) {
            throw new BadRequestAlertException(
                ExceptionConstants.DEFAULT_CARD_NOT_DELETE_VI,
                ENTITY_NAME,
                ExceptionConstants.DEFAULT_CARD_NOT_DELETE
            );
        }
        //        List<CardPolicy> cardPolicyList = cardPolicyRepository.findByComIdAndStatus(
        //            user.getCompanyId(),
        //            CardPolicyConstants.Status.RUNNING
        //        );
        //        for (CardPolicy cardPolicy : cardPolicyList) {
        //            ObjectMapper objectMapper = new ObjectMapper();
        //            if (cardPolicy.getConditions() != null) {
        //                try {
        //                    CardPolicyConditions[] conditions = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
        //                    for (CardPolicyConditions condition : conditions) {
        //                        if (Objects.equals(condition.getCardId(), loyaltyCard.getId())) {
        //                            throw new BadRequestAlertException(
        //                                ExceptionConstants.CARD_POLICY_RUNNING_VI,
        //                                ENTITY_NAME,
        //                                ExceptionConstants.CARD_BALANCE_INVALID
        //                            );
        //                        }
        //                    }
        //                } catch (JsonProcessingException e) {
        //                    log.error("Error when convert card policy condition" + e.getMessage());
        //                }
        //            }
        //        }
        loyaltyCard.setStatus(LoyaltyCardConstants.Status.DA_XOA);
        Integer previousRank = loyaltyCardRepository.getPreviousRank(user.getCompanyId(), id);
        if (previousRank != null) {
            List<CustomerCard> customerCards = customerCardRepository.getAllByComIdAndCardId(user.getCompanyId(), id);
            customerCards.forEach(customerCard -> customerCard.setCardId(previousRank));
        }

        // set rank
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.findByComIdAndRankIsGreaterThanEqualAndIdIsNot(
            user.getCompanyId(),
            loyaltyCard.getRank(),
            id
        );
        for (LoyaltyCard card : loyaltyCards) {
            card.setRank(card.getRank() - 1);
        }
        loyaltyCards.add(loyaltyCard);
        List<VoucherApply> voucherApplies = voucherApplyRepository.findAllByComIdAndApplyIdAndApplyType(
            user.getCompanyId(),
            loyaltyCard.getId(),
            1
        );
        resetCardPolicy(id, user.getCompanyId(), Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.FALSE, Boolean.TRUE, null);
        voucherApplyRepository.deleteAll(voucherApplies);
        loyaltyCardRepository.saveAll(loyaltyCards);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_CARD_SUCCESS, true);
    }

    @Override
    public ResultDTO deleteListCard(DeleteProductList req) {
        User user = userService.getUserWithAuthorities(req.getComId());
        List<LoyaltyCardResultItem> loyaltyCardItems = loyaltyCardRepository
            .getAllCustomerCard(null, user.getCompanyId(), req.getKeyword(), null, Boolean.TRUE, req.getParamCheckAll(), req.getIds())
            .getContent();
        List<Object> listError = new ArrayList<>();
        List<Integer> idDelete = new ArrayList<>();
        Map<Integer, CardPolicyConditions> conditionsMap = new LinkedHashMap<>();
        Map<Integer, LoyaltyCardItem> response = new HashMap<>();
        for (LoyaltyCardResultItem item : loyaltyCardItems) {
            if (!response.containsKey(item.getId())) {
                LoyaltyCardItem cardItem = new LoyaltyCardItem();
                cardItem.setId(item.getId());
                cardItem.setName(item.getName());
                cardItem.setIsDefault(item.getIsDefault());
                cardItem.setRank(item.getRank());
                cardItem.setStatus(item.getStatus());
                response.put(item.getId(), cardItem);
            }
        }
        List<LoyaltyCardItem> cardItems = new ArrayList<>(response.values());
        List<CardPolicy> cardPolicyOptional = cardPolicyRepository.findByComIdAndStatus(
            user.getCompanyId(),
            CardPolicyConstants.Status.RUNNING
        );
        for (CardPolicy cardPolicy : cardPolicyOptional) {
            ObjectMapper objectMapper = new ObjectMapper();
            if (cardPolicy.getConditions() != null) {
                try {
                    CardPolicyConditions[] conditions = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                    for (CardPolicyConditions condition : conditions) {
                        conditionsMap.put(condition.getCardId(), condition);
                    }
                } catch (JsonProcessingException e) {
                    log.error("Error when convert card policy condition" + e.getMessage());
                }
            }
        }

        for (LoyaltyCardItem item : cardItems) {
            if (conditionsMap.containsKey(item.getId())) {
                item.setNote(ExceptionConstants.CARD_POLICY_RUNNING_VI);
                listError.add(item);
            } else {
                idDelete.add(item.getId());
            }
        }
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.findAllByComIdAndIdIn(user.getCompanyId(), idDelete);
        for (LoyaltyCard card : loyaltyCards) {
            card.setStatus(LoyaltyCardConstants.Status.DA_XOA);
        }
        List<LoyaltyCard> loyaltyCardList = loyaltyCardRepository.findAllByComIdAndStatusIn(
            user.getCompanyId(),
            List.of(LoyaltyCardConstants.Status.NGUNG_HOAT_DONG, LoyaltyCardConstants.Status.HOAT_DONG)
        );
        loyaltyCardList.sort(Comparator.comparingInt(LoyaltyCard::getRank));
        int rank = 1;
        for (LoyaltyCard card : loyaltyCardList) {
            card.setRank(rank++);
        }
        DataResponse result = new DataResponse();
        result.setCountAll(loyaltyCardItems.size());
        result.setCountFalse(listError.size());
        result.setDataFalse(listError);
        result.setCountSuccess(result.getCountAll() - result.getCountFalse());
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_LIST_PRODUCT_UNIT_SUCCESS, true, result);
    }

    @Override
    public ResultDTO sortCard(List<SortRankCardRequest> request) {
        User user = userService.getUserWithAuthorities();
        List<Integer> status = List.of(LoyaltyCardConstants.Status.NGUNG_HOAT_DONG, LoyaltyCardConstants.Status.HOAT_DONG);
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.findAllByComIdAndStatusIn(user.getCompanyId(), status);
        if (loyaltyCards.size() != request.size()) {
            throw new BadRequestAlertException(ExceptionConstants.CARD_LIST_INVALID_VI, ENTITY_NAME, ExceptionConstants.CARD_LIST_INVALID);
        }
        Optional<LoyaltyCard> loyaltyCardDefaultOptional = loyaltyCardRepository.findByComIdAndIsDefaultAndStatusIn(
            user.getCompanyId(),
            Boolean.TRUE,
            status
        );
        List<Integer> ids = new ArrayList<>();
        Set<Integer> ranks = new HashSet<>();
        for (SortRankCardRequest req : request) {
            if (Objects.equals(req.getId(), loyaltyCardDefaultOptional.get().getId()) && req.getRank() != 1) {
                throw new BadRequestAlertException(
                    ExceptionConstants.SORT_CARD_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.SORT_CARD_INVALID
                );
            }
            if (ranks.contains(req.getRank())) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_LIST_INVALID
                );
            }
            if (req.getRank() < 1 || req.getRank() > loyaltyCards.size()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_LIST_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_LIST_INVALID
                );
            }
            ids.add(req.getId());
            ranks.add(req.getRank());
        }
        List<LoyaltyCard> cardList = loyaltyCardRepository.findAllByComIdAndIdInAndStatusIn(user.getCompanyId(), ids, status);
        if (cardList.size() != request.size()) {
            throw new BadRequestAlertException(ExceptionConstants.CARD_LIST_INVALID_VI, ENTITY_NAME, ExceptionConstants.CARD_LIST_INVALID);
        }
        request.sort(Comparator.comparingInt(SortRankCardRequest::getRank));
        Map<Integer, Integer> cardListMap = request
            .stream()
            .collect(Collectors.toMap(SortRankCardRequest::getRank, SortRankCardRequest::getId));
        List<CardPolicy> cardPolicyOptional = cardPolicyRepository.findByComIdAndStatus(
            user.getCompanyId(),
            CardPolicyConstants.Status.RUNNING
        );
        Map<Integer, BigDecimal> conditionsMap = new HashMap<>();
        for (CardPolicy cardPolicy : cardPolicyOptional) {
            ObjectMapper objectMapper = new ObjectMapper();
            if (cardPolicy.getConditions() != null) {
                try {
                    CardPolicyConditions[] conditions = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                    for (CardPolicyConditions condition : conditions) {
                        conditionsMap.put(condition.getCardId(), condition.getUpgradeValue());
                    }
                } catch (JsonProcessingException e) {
                    log.error("Error when convert card policy condition " + e.getMessage());
                }
            }
        }
        Map<Integer, BigDecimal> rankMap = new LinkedHashMap<>();
        for (Map.Entry<Integer, Integer> entry : cardListMap.entrySet()) {
            if (conditionsMap.containsKey(entry.getValue())) {
                rankMap.put(entry.getKey(), conditionsMap.get(entry.getValue()));
            }
        }
        BigDecimal preUpgradeRank = null;
        for (Map.Entry<Integer, BigDecimal> entry : rankMap.entrySet()) {
            if (preUpgradeRank != null) {
                if (preUpgradeRank.compareTo(entry.getValue()) > 0) {
                    Optional<LoyaltyCard> loyaltyCardOptional = loyaltyCardRepository.findByComIdAndRankAndStatusIn(
                        user.getCompanyId(),
                        entry.getKey(),
                        List.of(LoyaltyCardConstants.Status.HOAT_DONG, LoyaltyCardConstants.Status.NGUNG_HOAT_DONG)
                    );
                    throw new BadRequestAlertException(
                        String.format(ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_INVALID_VI, loyaltyCardOptional.get().getName()),
                        ENTITY_NAME,
                        ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_INVALID_CODE
                    );
                }
            }
            preUpgradeRank = entry.getValue();
        }
        Map<Integer, Integer> sortMap = request
            .stream()
            .collect(Collectors.toMap(SortRankCardRequest::getId, SortRankCardRequest::getRank));
        for (LoyaltyCard card : cardList) {
            if (sortMap.containsKey(card.getId())) {
                card.setRank(sortMap.get(card.getId()));
            }
        }
        loyaltyCardRepository.saveAll(cardList);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SORT_CARD_SUCCESS, true);
    }

    @Override
    public ResultDTO saveCardPolicy(CarPolicySaveRequest request) {
        Integer comId = request.getComId();
        Integer upgradeType = request.getUpgradeType();
        Integer upgradeTypeOld = null;
        userService.getUserWithAuthorities(comId);
        Common.checkFromDate(request.getFromDate(), Constants.ZONED_DATE_FORMAT);

        CardPolicy cardPolicy = new CardPolicy();
        CardPolicyConditions[] conditionsOld;
        LinkedHashMap<Integer, CustomerCardRankItem> cardMap = new LinkedHashMap<>();
        if (request.getId() != null) {
            Optional<CardPolicy> cardPolicyOptional = cardPolicyRepository.findOneByIdAndComIdAndStatus(
                request.getId(),
                comId,
                CardPolicyConstants.Status.RUNNING
            );
            if (cardPolicyOptional.isEmpty()) {
                throw new InternalServerException(
                    ExceptionConstants.CARD_POLICY_NOT_FOUND_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_POLICY_NOT_FOUND
                );
            }
            cardPolicy = cardPolicyOptional.get();
            upgradeTypeOld = cardPolicy.getUpgradeType();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                conditionsOld = objectMapper.readValue(cardPolicy.getConditions(), CardPolicyConditions[].class);
                if (conditionsOld != null) {
                    for (CardPolicyConditions item : conditionsOld) {
                        cardMap.put(item.getCardId(), null);
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("Card policy conditions old invalid");
            }
        } else {
            Optional<CardPolicy> cardPolicyOptional = cardPolicyRepository.getOneByComIdAndStatusIn(comId, getStatusAvailable());
            if (cardPolicyOptional.isPresent()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_POLICY_EXISTS_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_POLICY_EXISTS
                );
            }
        }

        cardPolicy.setComId(comId);
        cardPolicy.setNote(request.getNote());
        cardPolicy.setGenDescription(null);
        cardPolicy.setUpgradeType(upgradeType);
        cardPolicy.setStatus(1);

        List<CardPolicyConditions> conditions = request.getConditions();
        Map<Integer, CardPolicyConditions> cardPolicyConditionsMap = new HashMap<>();
        List<Integer> cardIds = new ArrayList<>();
        conditions.forEach(c -> {
            if (!c.getIsDefault() && c.getUpgradeValue() == null) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_NOT_NULL_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_NOT_NULL
                );
            }
            if (c.getCardId() != null) {
                cardPolicyConditionsMap.put(c.getCardId(), c);
                cardIds.add(c.getCardId());
            }
        });
        List<CardPolicyConditions> conditionsSave = new ArrayList<>();
        List<LoyaltyCard> loyaltyCards = checkCardAvailable(comId, new ArrayList<>(cardIds));
        BigDecimal upgradeValue = BigDecimal.valueOf(-1);
        for (LoyaltyCard card : loyaltyCards) {
            if (cardPolicyConditionsMap.containsKey(card.getId())) {
                CardPolicyConditions conditionRequest = cardPolicyConditionsMap.get(card.getId());
                if (upgradeValue.compareTo(conditionRequest.getUpgradeValue()) >= 0) {
                    throw new BadRequestAlertException(
                        String.format(ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_INVALID_VI, card.getName()),
                        ENTITY_NAME,
                        ExceptionConstants.CARD_POLICY_UPGRADE_VALUE_INVALID_CODE
                    );
                }
                upgradeValue = conditionRequest.getUpgradeValue();
                CardPolicyConditions condition = new CardPolicyConditions();
                condition.setDesc(String.format(CardPolicyConstants.Condition.DESC, card.getName()));
                condition.setCardId(card.getId());
                condition.setCardName(card.getName());
                condition.setAccumValue(conditionRequest.getAccumValue());
                condition.setRedeemValue(conditionRequest.getRedeemValue());
                condition.setUpgradeValue(!conditionRequest.getIsDefault() ? upgradeValue : BigDecimal.ZERO);
                condition.setUpgradeTime(conditionRequest.getUpgradeTime());
                conditionsSave.add(condition);
                cardMap.put(
                    card.getId(),
                    new CustomerCardRankItem(
                        card.getId(),
                        card.getRank(),
                        conditionRequest.getUpgradeValue(),
                        conditionRequest.getUpgradeTime()
                    )
                );
            }
        }
        JSONArray convertConditionToJson = new JSONArray(conditionsSave);
        cardPolicy.setConditions(convertConditionToJson.toString());
        cardPolicy.setStartTime(Common.convertStringToDateTime(request.getFromDate(), Constants.ZONED_DATE_FORMAT));
        cardPolicyRepository.save(cardPolicy);

        resetRankAfterSavePolicy(comId, upgradeType, cardMap, upgradeTypeOld);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SAVE_CARD_POLICY_SUCCESS, true);
    }

    private List<Integer> getAllCardIdsExistsInPolicyConditions(Integer comId, Integer upgradeType) {
        Integer upgradeTypeExistsCheck = CardPolicyConstants.UpgradeType.TOTAL_SPENDING;
        if (Objects.equals(upgradeType, CardPolicyConstants.UpgradeType.TOTAL_SPENDING)) {
            upgradeTypeExistsCheck = CardPolicyConstants.UpgradeType.TOTAL_DEPOSIT;
        }
        CardPolicy cardPolicyExists = null;
        Optional<CardPolicy> cardPolicyExistsOptional = cardPolicyRepository.findOneByComIdAndUpgradeTypeAndStatusIn(
            comId,
            upgradeTypeExistsCheck,
            getStatusAvailable()
        );
        List<Integer> cardIdsExists = new ArrayList<>();
        if (cardPolicyExistsOptional.isPresent()) {
            cardPolicyExists = cardPolicyExistsOptional.get();
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                CardPolicyConditions[] conditionsExists = objectMapper.readValue(
                    cardPolicyExists.getConditions(),
                    CardPolicyConditions[].class
                );
                for (CardPolicyConditions exist : conditionsExists) {
                    if (exist != null) {
                        cardIdsExists.add(exist.getCardId());
                    }
                }
            } catch (JsonProcessingException e) {
                log.error("Get old card policy condition fail: " + e.getMessage());
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_POLICY_CONDITION_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_POLICY_CONDITION_INVALID
                );
            }
        }
        return cardIdsExists;
    }

    private List<LoyaltyCard> checkCardAvailable(Integer comId, List<Integer> cardIds) {
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.getAllByComIdOrderByRank(comId);
        if (loyaltyCards.isEmpty()) {
            throw new InternalServerException(
                ExceptionConstants.LOYALTY_CARD_LIST_INVALID_VI,
                ENTITY_NAME + "_policy",
                ExceptionConstants.LOYALTY_CARD_LIST_INVALID
            );
        }
        List<Integer> cardIdsResult = loyaltyCards.stream().map(LoyaltyCard::getId).collect(Collectors.toList());
        cardIds.forEach(c -> {
            if (!cardIdsResult.contains(c)) {
                throw new InternalServerException(
                    ExceptionConstants.LOYALTY_CARD_LIST_INVALID_VI + " id: " + c,
                    ENTITY_NAME + "_policy",
                    ExceptionConstants.LOYALTY_CARD_LIST_INVALID
                );
            }
        });
        return loyaltyCards;
    }

    private void resetRankAfterSavePolicy(
        Integer comId,
        Integer type,
        LinkedHashMap<Integer, CustomerCardRankItem> cardMap,
        Integer typeOld
    ) {
        if (Objects.equals(type, typeOld)) {
            List<CustomerCardItem> customerCardAccumulate;
            List<Integer> cardIds = new ArrayList<>(cardMap.keySet());
            List<CustomerCard> customerCards = customerCardRepository.getAllByCardIds(comId, cardIds);
            List<Integer> customerIds = customerCards.stream().map(CustomerCard::getCustomerId).collect(Collectors.toList());
            if (Objects.equals(type, CardPolicyConstants.UpgradeType.TOTAL_SPENDING)) {
                customerCardAccumulate =
                    loyaltyCardUsageRepository.getDataResetRankBySpending(comId, customerIds, LoyaltyCardUsageConstants.Type.CONG_DIEM);
            } else {
                customerCardAccumulate =
                    loyaltyCardUsageRepository.getDataResetRankByDeposit(comId, customerIds, LoyaltyCardConstants.Type.NAP_TIEN);
            }
            if (!customerCardAccumulate.isEmpty()) {
                Map<Integer, BigDecimal> accumMap = new HashMap<>();
                customerCardAccumulate.forEach(c -> accumMap.put(c.getCustomerId(), c.getTotalAccumulate()));
                Set<Integer> customerIDs = new HashSet<>();
                for (CustomerCard item : customerCards) {
                    BigDecimal accumValue = accumMap.get(item.getCustomerId());
                    if (accumValue != null) {
                        boolean isUpRank = false;
                        for (Map.Entry<Integer, CustomerCardRankItem> entry : cardMap.entrySet()) {
                            Integer cardId = entry.getKey();
                            if (entry.getValue() != null) {
                                CustomerCardRankItem cardRequestItem = entry.getValue();
                                BigDecimal requestValue = cardRequestItem.getUpgradeValue();
                                if (accumValue.compareTo(requestValue) >= 0) {
                                    item.setCardId(cardId);
                                    customerIDs.add(item.getCustomerId());
                                    isUpRank = true;
                                }
                                if (!isUpRank && accumValue.compareTo(requestValue) < 0) {
                                    item.setCardId(cardId);
                                    break;
                                }
                            }
                        }
                    }
                }
                customerCardRepository.saveAll(customerCards);
                log.debug("Reset rank for customer success with id {}", customerIDs);
            }
        }
    }

    private static List<Integer> getStatusAvailable() {
        return List.of(CardPolicyConstants.Status.NOT_RUN, CardPolicyConstants.Status.RUNNING);
    }

    private static List<Integer> getUpgradeType() {
        return List.of(CardPolicyConstants.UpgradeType.TOTAL_SPENDING, CardPolicyConstants.UpgradeType.TOTAL_DEPOSIT);
    }

    @Override
    public ResultDTO getAllCardPolicy(Integer comId) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
        Optional<CardPolicyItems> policyItemsOptional = cardPolicyRepository.findOneByComIdAndStatusIn(comId, getStatusAvailable());
        CardPolicyResponse response;
        ObjectMapper objectMapper = new ObjectMapper();
        CardPolicyConditions[] data;
        List<LoyaltyCard> loyaltyCards = loyaltyCardRepository.getAllByComIdOrderByRank(comId);
        if (policyItemsOptional.isPresent()) {
            CardPolicyItems item = policyItemsOptional.get();
            try {
                data = objectMapper.readValue(item.getConditions(), CardPolicyConditions[].class);
                List<CardPolicyConditions> conditions = new ArrayList<>();
                Map<Integer, CardPolicyConditions> cardConditionMap = new HashMap<>();
                for (CardPolicyConditions condition : data) {
                    cardConditionMap.put(condition.getCardId(), condition);
                }
                for (LoyaltyCard c : loyaltyCards) {
                    CardPolicyConditions condition = new CardPolicyConditions();
                    if (cardConditionMap.containsKey(c.getId())) {
                        condition = cardConditionMap.get(c.getId());
                        condition.setChecked(true);
                    } else {
                        condition.setCardId(c.getId());
                        condition.setCardName(c.getName());
                        condition.setChecked(false);
                    }
                    condition.setIsDefault(c.getIsDefault());
                    conditions.add(condition);
                }
                ZonedDateTime startTime = item.getStartTime();
                String startTimeFormat = null;
                if (startTime != null) {
                    startTimeFormat = dateTimeFormatter.format(startTime);
                }
                response = new CardPolicyResponse(comId, item.getId(), item.getUpgradeType(), conditions, startTimeFormat);
            } catch (JsonProcessingException e) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CARD_POLICY_CONDITION_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CARD_POLICY_CONDITION_INVALID
                );
            }
        } else {
            response = new CardPolicyResponse(comId, null, null, setConditionForPolicy(loyaltyCards), null);
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.GET_ALL_CARD_POLICY_SUCCESS,
            true,
            response,
            response.getConditions().size()
        );
    }

    private List<CardPolicyConditions> setConditionForPolicy(List<LoyaltyCard> loyaltyCards) {
        List<CardPolicyConditions> conditions = new ArrayList<>();
        for (LoyaltyCard c : loyaltyCards) {
            CardPolicyConditions condition = new CardPolicyConditions();
            condition.setCardId(c.getId());
            condition.setCardName(c.getName());
            condition.setChecked(false);
            condition.setIsDefault(c.getIsDefault());
            conditions.add(condition);
        }
        return conditions;
    }

    @Override
    public ResultDTO getAllHistory(
        Integer comId,
        Integer customerId,
        Integer type,
        boolean getWithPaging,
        Pageable pageable,
        String fromDate,
        String toDate
    ) {
        userService.getUserWithAuthorities(comId);
        Page<CardHistoryResult> resultPage;
        List<CardHistoryResult> content;
        if (Objects.equals(customerRepository.countAllByIdAndComId(customerId, comId), 1)) {
            if (!getWithPaging) {
                pageable = PageRequest.of(0, 10);
            }
            List<Integer> typeQuery;
            // lịch sử tích điểm
            if (Objects.equals(type, 1)) {
                typeQuery =
                    List.of(
                        LoyaltyCardUsageConstants.Type.NAP_DIEM,
                        LoyaltyCardUsageConstants.Type.TRU_DIEM,
                        LoyaltyCardUsageConstants.Type.CONG_DIEM,
                        LoyaltyCardUsageConstants.Type.QUY_DOI
                    );
            } else {
                // lịch sử chi tiêu
                typeQuery =
                    List.of(
                        LoyaltyCardUsageConstants.Type.NAP_TIEN,
                        LoyaltyCardUsageConstants.Type.CHI_TIEN,
                        LoyaltyCardUsageConstants.Type.QUY_DOI,
                        LoyaltyCardUsageConstants.Type.CONG_TIEN
                    );
            }
            resultPage = loyaltyCardUsageRepository.getAllHistory(comId, customerId, typeQuery, pageable, fromDate, toDate);
            content = resultPage.getContent();
            Map<Integer, String> typeMap = LoyaltyCardUsageConstants.TypeMap.TYPE_MAP;
            if (!content.isEmpty()) {
                content.forEach(c -> {
                    if (typeMap.containsKey(c.getType())) {
                        c.setTypeName(typeMap.get(c.getType()));
                    }
                });
            }
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_NOT_FOUND_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_NOT_FOUND
            );
        }
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.CARD_USAGE_GET_ALL_SUCCESS,
            true,
            resultPage.getContent(),
            (int) resultPage.getTotalElements()
        );
    }

    @Override
    public ResultDTO getCardDefault(Integer comId) {
        userService.getUserWithAuthorities(comId);
        Optional<CardDefaultItem> cardDefault = loyaltyCardRepository.getCardDefault(comId);
        return cardDefault
            .map(cardDefaultItem -> new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CARD_GET_DEFAULT_SUCCESS, true, cardDefaultItem))
            .orElseGet(() -> new ResultDTO(ResultConstants.FAIL, ResultConstants.CARD_GET_DEFAULT_FAIL, false, null));
    }
}
