package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.card.CarPolicySaveRequest;
import vn.softdreams.easypos.dto.loyaltyCard.SaveLoyaltyCardRequest;
import vn.softdreams.easypos.dto.loyaltyCard.SortRankCardRequest;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.service.CardManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.Validator;
import java.util.List;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.LoyaltyCard}.
 */
@RestController
@RequestMapping("/api")
public class CardManagementResource {

    private final Logger log = LoggerFactory.getLogger(CardManagementResource.class);

    private static final String ENTITY_NAME = "loyaltyCard";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardManagementService loyaltyCardService;
    private final Validator customValidator;

    public CardManagementResource(CardManagementService loyaltyCardService, Validator customValidator) {
        this.loyaltyCardService = loyaltyCardService;
        this.customValidator = customValidator;
    }

    @GetMapping("/client/page/card/get-all")
    public ResponseEntity<ResultDTO> getAllCustomerCard(
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Boolean isCountAll
    ) {
        ResultDTO resultDTO = loyaltyCardService.getAllCustomerCard(pageable, keyword, isCountAll);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/card/by-id/{id}")
    public ResponseEntity<ResultDTO> getCustomerCardById(@PathVariable Integer id) {
        ResultDTO resultDTO = loyaltyCardService.getDetailById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/card/create")
    public ResponseEntity<ResultDTO> createCustomerCard(@RequestBody SaveLoyaltyCardRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = loyaltyCardService.saveCard(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/card/update")
    public ResponseEntity<ResultDTO> updateCustomerCard(@RequestBody SaveLoyaltyCardRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = loyaltyCardService.saveCard(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/card/delete/{id}")
    public ResponseEntity<ResultDTO> deleteCustomerCard(@PathVariable Integer id) {
        ResultDTO resultDTO = loyaltyCardService.deleteCard(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/card/delete-list")
    public ResponseEntity<ResultDTO> deleteListCustomerCard(@RequestBody DeleteProductList req) {
        ResultDTO resultDTO = loyaltyCardService.deleteListCard(req);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/card/sort")
    public ResponseEntity<ResultDTO> sortCustomerCard(@RequestBody @Valid List<SortRankCardRequest> request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = loyaltyCardService.sortCard(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/card-policy/get-all")
    public ResponseEntity<ResultDTO> getAllCardPolicy(@RequestParam Integer comId) {
        ResultDTO resultDTO = loyaltyCardService.getAllCardPolicy(comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/card-policy/create")
    public ResponseEntity<ResultDTO> createCardPolicy(@RequestBody CarPolicySaveRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = loyaltyCardService.saveCardPolicy(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/card-policy/update")
    public ResponseEntity<ResultDTO> updateCardPolicy(@RequestBody CarPolicySaveRequest request) {
        if (request.getId() == null) {
            throw new BadRequestAlertException(
                ExceptionConstants.CARD_POLICY_ID_NULL_VI,
                ENTITY_NAME,
                ExceptionConstants.CARD_POLICY_ID_NULL
            );
        }
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = loyaltyCardService.saveCardPolicy(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/card/history/get-all")
    public ResponseEntity<ResultDTO> getAllHistory(
        @RequestParam Integer comId,
        @RequestParam Integer customerId,
        @RequestParam Integer type,
        @RequestParam(required = false) boolean getWithPaging,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        Pageable pageable
    ) {
        ResultDTO resultDTO = loyaltyCardService.getAllHistory(comId, customerId, type, getWithPaging, pageable, fromDate, toDate);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/card/get-default/{comId}")
    public ResponseEntity<ResultDTO> getCardDefault(@PathVariable Integer comId) {
        ResultDTO resultDTO = loyaltyCardService.getCardDefault(comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
