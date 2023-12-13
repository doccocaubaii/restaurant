package vn.softdreams.easypos.web.rest;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.domain.Customer;
import vn.softdreams.easypos.dto.customer.CustomerCreateRequest;
import vn.softdreams.easypos.dto.customer.CustomerUpdateCardRequest;
import vn.softdreams.easypos.dto.customer.CustomerUpdateRequest;
import vn.softdreams.easypos.dto.customer.CustomerValidateResponse;
import vn.softdreams.easypos.dto.product.DeleteProductList;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.CustomerManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerManagementResource {

    private final Logger log = LoggerFactory.getLogger(CustomerManagementResource.class);

    private final CustomerManagementService customerManagementService;
    private final Validator validator;
    private final UserService userService;
    private final ModelMapper modelMapper;

    private static final String ENTITY_NAME = "Customer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public CustomerManagementResource(
        CustomerManagementService customerManagementService,
        Validator validator,
        UserService userService,
        ModelMapper modelMapper
    ) {
        this.customerManagementService = customerManagementService;
        this.validator = validator;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/client/page/customer/get-all-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.VIEW)
    public ResponseEntity<ResultDTO> getAllWithPaging(
        Pageable pageable,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) Boolean isCountAll,
        @RequestParam(required = false) Boolean isHiddenDefault
    ) {
        return ResponseEntity
            .ok()
            .body(
                customerManagementService.getAllWithPaging(
                    pageable,
                    keyword,
                    type,
                    isCountAll != null && isCountAll,
                    isHiddenDefault,
                    false,
                    null
                )
            );
    }

    @GetMapping("/client/page/customer/get-all-for-offline")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.VIEW)
    public ResponseEntity<ResultDTO> getAllForOffline(@RequestParam(required = false) Integer type) {
        return ResponseEntity.ok().body(customerManagementService.getAllForOffline(type));
    }

    @PostMapping("/client/page/customer/create")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.ADD)
    public ResponseEntity<ResultDTO> createCustomer(@RequestBody CustomerCreateRequest customerRequest) {
        Common.validateInput(validator, ENTITY_NAME, customerRequest);
        return new ResponseEntity<>(customerManagementService.create(customerRequest), HttpStatus.OK);
    }

    @PutMapping("/client/page/customer/update")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.UPDATE)
    public ResponseEntity<ResultDTO> updateCustomer(@RequestBody CustomerUpdateRequest customerRequest) {
        Common.validateInput(validator, ENTITY_NAME, customerRequest);
        return new ResponseEntity<>(customerManagementService.update(customerRequest), HttpStatus.OK);
    }

    @PutMapping("/client/page/customer/delete/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.DELETE)
    public ResponseEntity<ResultDTO> deleteCustomer(@NotNull @PathVariable Integer id) {
        return new ResponseEntity<>(customerManagementService.deleteCustomer(id), HttpStatus.OK);
    }

    @GetMapping("/client/page/customer/by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.VIEW)
    public ResponseEntity<ResultDTO> findCustomerById(@NotNull @PathVariable Integer id) {
        return ResponseEntity.ok().body(customerManagementService.findById(id));
    }

    @PostMapping("/client/page/customer/import-excel")
    public ResponseEntity<ResultDTO> importExcel(@RequestParam("file") MultipartFile file, @RequestParam Integer comId) {
        ResultDTO resultDTO = customerManagementService.importExel(file, comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/customer/import-excel/validate")
    public ResponseEntity<ResultDTO> validateImportExcel(
        @RequestParam("file") MultipartFile file,
        @RequestParam Integer comId,
        @RequestParam Integer indexSheet
    ) {
        ResultDTO resultDTO = customerManagementService.validateImportExcel(file, comId, indexSheet);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/customer/import-excel/save-data")
    public ResponseEntity<ResultDTO> saveDataImportExcel(@NotNull @RequestBody List<CustomerCreateRequest> request) {
        ResultDTO result = customerManagementService.saveDataImportExcel(request);
        if (result.isStatus()) {
            List<Customer> resultData = Arrays.asList(modelMapper.map(result.getData(), Customer[].class));
            customerManagementService.syncDataAfterImport(resultData);
            result.setData(resultData.size());
        } else {
            result.setData(null);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/customer/export-excel/error-data")
    public ResponseEntity<byte[]> exportErrorData(@RequestBody List<CustomerValidateResponse> request) {
        ResultDTO resultDTO = customerManagementService.exportErrorData(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "filename.extension");

        return new ResponseEntity<>((byte[]) resultDTO.getData(), headers, HttpStatus.OK);
    }

    @GetMapping("/p/client/page/customer/by-taxCode/get-detail")
    public ResponseEntity<ResultDTO> getDetailByTaxCode(@RequestParam String taxCode) {
        ResultDTO resultDTO = customerManagementService.getDataByTaxCode(taxCode);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/card/save-point")
    public ResponseEntity<ResultDTO> updateCard(@RequestBody CustomerUpdateCardRequest request) {
        ResultDTO resultDTO = customerManagementService.updateCard(request);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PutMapping("/client/page/customer/delete-list")
    @CheckAuthorize(value = AuthoritiesConstants.Customer.DELETE)
    public ResponseEntity<ResultDTO> deleteListCustomer(@RequestBody DeleteProductList req) {
        return new ResponseEntity<>(customerManagementService.deleteListCustomer(req), HttpStatus.OK);
    }
}
