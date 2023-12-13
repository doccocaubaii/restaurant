package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupUpdateRequest;
import vn.softdreams.easypos.repository.ToppingGroupRepository;
import vn.softdreams.easypos.service.ToppingGroupService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;

/**
 * REST controller for managing {@link vn.softdreams.easypos.domain.ToppingGroup}.
 */
@RestController
@RequestMapping("/api")
public class ToppingGroupResource {

    private final Logger log = LoggerFactory.getLogger(ToppingGroupResource.class);

    private static final String ENTITY_NAME = "toppingGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ToppingGroupService toppingGroupService;

    private final ToppingGroupRepository toppingGroupRepository;
    private final Validator customValidator;

    public ToppingGroupResource(
        ToppingGroupService toppingGroupService,
        ToppingGroupRepository toppingGroupRepository,
        Validator customValidator
    ) {
        this.toppingGroupService = toppingGroupService;
        this.toppingGroupRepository = toppingGroupRepository;
        this.customValidator = customValidator;
    }

    @PostMapping("/client/page/topping-group/create")
    public ResponseEntity<ResultDTO> createToppingGroup(@RequestBody ToppingGroupUpdateRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        log.debug("REST request to save ToppingGroup : {}", request);
        ResultDTO resultDTO = toppingGroupService.save(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/topping-group/update")
    public ResponseEntity<ResultDTO> updateToppingGroup(@RequestBody ToppingGroupUpdateRequest request) {
        Common.validateInput(customValidator, ENTITY_NAME, request);
        ResultDTO resultDTO = toppingGroupService.save(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/topping-group/get-with-paging")
    public ResponseEntity<ResultDTO> getAllToppingGroups(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam Integer comId,
        @RequestParam(required = false) Boolean isCountAll,
        @RequestParam(required = false) String keyword
    ) {
        log.debug("REST request to get a page of ToppingGroups");
        ResultDTO resultDTO = toppingGroupService.getAllWithPaging(pageable, comId, keyword, isCountAll);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/topping-group/by-id/{id}")
    public ResponseEntity<ResultDTO> getToppingGroup(
        @PathVariable(value = "id") @NotNull(message = ExceptionConstants.TOPPING_GROUP_ID_NOT_NULL) Integer id
    ) {
        log.debug("REST request to get ToppingGroup : {}", id);
        ResultDTO resultDTO = toppingGroupService.getToppingGroupDetail(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/get-all-product-topping")
    public ResponseEntity<ResultDTO> getListToppingForProduct(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) Integer id,
        @RequestParam(required = false) Boolean isSingleList,
        @RequestParam(required = false) String keyword
    ) {
        ResultDTO resultDTO = toppingGroupService.getListToppingForProduct(page, size, id, isSingleList, keyword);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/product/get-product-topping/{id}")
    public ResponseEntity<ResultDTO> getProductToppingForBill(@PathVariable Integer id) {
        ResultDTO resultDTO = toppingGroupService.getListToppingByProductId(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/topping-group/delete/{id}")
    public ResponseEntity<ResultDTO> deleteToppingGroup(
        @PathVariable(value = "id") @NotNull(message = ExceptionConstants.TOPPING_GROUP_ID_NOT_NULL) Integer id
    ) {
        log.debug("REST request to delete ToppingGroup : {}", id);
        ResultDTO resultDTO = toppingGroupService.delete(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
