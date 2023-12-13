package vn.softdreams.easypos.web.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;
import vn.softdreams.easypos.aop.CheckAuthorize;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Config;
import vn.softdreams.easypos.dto.authorities.LoginEasyInvoice;
import vn.softdreams.easypos.dto.config.*;
import vn.softdreams.easypos.dto.invoice.DeclarationRequest;
import vn.softdreams.easypos.dto.invoice.RegisterEasyInvoiceRequest;
import vn.softdreams.easypos.repository.ConfigRepository;
import vn.softdreams.easypos.security.AuthoritiesConstants;
import vn.softdreams.easypos.service.ConfigManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Config}.
 */
@RestController
@RequestMapping("/api")
public class ConfigManagementResource {

    private final Logger log = LoggerFactory.getLogger(ConfigManagementResource.class);

    private static final String ENTITY_NAME = "config";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigManagementService configManagementService;

    private final ConfigRepository configRepository;
    private final Validator validator;

    public ConfigManagementResource(
        ConfigManagementService configManagementService,
        ConfigRepository configRepository,
        Validator validator
    ) {
        this.configManagementService = configManagementService;
        this.configRepository = configRepository;
        this.validator = validator;
    }

    @PostMapping("/client/common/config/login/easy-invoice")
    public ResponseEntity<ResultDTO> loginEasyInvoice(@RequestBody LoginEasyInvoice loginEasyInvoiceDTO) {
        log.debug("REST request to save Config : {}", loginEasyInvoiceDTO);
        ResultDTO result = configManagementService.loginEasyInvoice(loginEasyInvoiceDTO);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/admin/client/common/config/login/easy-invoice")
    @CheckAuthorize(value = AuthoritiesConstants.AdminCompanyOwner.CONNECT_EASY_INVOICE)
    public ResponseEntity<ResultDTO> loginEasyInvoiceFromSystemAdmin(@RequestBody LoginEasyInvoice request) throws URISyntaxException {
        log.debug("REST request to save Config : {}", request);
        try {
            ResultDTO result = configManagementService.loginEasyInvoice(request);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ResultDTO error = new ResultDTO(ResultConstants.FAIL, e.getMessage(), false);
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @PostMapping("/client/common/config/login/register-and-publish")
    public ResponseEntity<ResultDTO> registerAndPublish(@RequestBody RegisterEasyInvoiceRequest registerEasyInvoiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save Config : {}", registerEasyInvoiceDTO);
        ResultDTO result = configManagementService.registerAndPublish(registerEasyInvoiceDTO);
        return ResponseEntity
            .created(new URI("/api/configs/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
            .body(result);
    }

    @PostMapping("/client/common/config/declaration-search")
    public ResponseEntity<ResultDTO> declarationSearch(@RequestBody DeclarationRequest declarationDTO) throws URISyntaxException {
        log.debug("REST request to save Config : {}", declarationDTO);
        ResultDTO result = configManagementService.declarationSearch(declarationDTO);
        return ResponseEntity
            .created(new URI("/api/configs/"))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ""))
            .body(result);
    }

    /**
     * {@code PUT  /configs/:id} : Updates an existing config.
     *
     * @param id the id of the config to save.
     * @param config the config to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated config,
     * or with status {@code 400 (Bad Request)} if the config is not valid,
     * or with status {@code 500 (Internal Server Error)} if the config couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/configs/{id}")
    public ResponseEntity<Config> updateConfig(@PathVariable(value = "id", required = false) final Integer id, @RequestBody Config config)
        throws URISyntaxException {
        log.debug("REST request to update Config : {}, {}", id, config);
        if (config.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, config.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Config result = configManagementService.update(config);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, config.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /configs/:id} : Partial updates given fields of an existing config, field will ignore if it is null
     *
     * @param id the id of the config to save.
     * @param config the config to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated config,
     * or with status {@code 400 (Bad Request)} if the config is not valid,
     * or with status {@code 404 (Not Found)} if the config is not found,
     * or with status {@code 500 (Internal Server Error)} if the config couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/configs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Config> partialUpdateConfig(
        @PathVariable(value = "id", required = false) final Integer id,
        @RequestBody Config config
    ) throws URISyntaxException {
        log.debug("REST request to partial update Config partially : {}, {}", id, config);
        if (config.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, config.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Config> result = configManagementService.partialUpdate(config);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, config.getId().toString())
        );
    }

    /**
     * {@code GET  /configs} : get all the configs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configs in body.
     */
    @GetMapping("/client/common/config/get-all-config")
    public ResponseEntity<ResultDTO> getAllConfigs() {
        log.debug("REST request to get a page of Configs");
        ResultDTO page = configManagementService.getAllConfigs();
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @GetMapping("/client/page/config/get-print-config/{comId}")
    public ResponseEntity<ResultDTO> getAllPrintConfigs(@PathVariable Integer comId, @RequestParam(required = false) Integer type) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.getAllPrintConfigs(comId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/config/get-company-configs/{comId}")
    public ResponseEntity<ResultDTO> getCompanyConfig(@PathVariable Integer comId) {
        log.debug("REST request to register and get authority code back : {}", comId);
        ResultDTO result = configManagementService.getRegisterInvoicePatterns(comId);
        return ResponseEntity.ok(result);
    }

    @PutMapping("/client/page/config/update-invoice-config")
    public ResponseEntity<ResultDTO> updateInvoiceConfig(@RequestBody InvoiceConfig invoiceConfig) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.updateInvoiceConfig(invoiceConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/config/update-print-config")
    public ResponseEntity<ResultDTO> updatePrintConfig(@RequestBody PrintConfigCompany printConfigCompany) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.updatePrintConfig(printConfigCompany);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/config/update-list-print-config")
    public ResponseEntity<ResultDTO> updateListPrintConfig(
        @RequestBody @Valid @NotNull(message = ExceptionConstants.PRINT_CONFIGS_NOT_NULL) List<PrintConfigCompany> printConfigs
    ) {
        log.debug("REST request to get a page of PrintConfigs");
        Common.validateInput(validator, ENTITY_NAME, printConfigs);
        ResultDTO result = configManagementService.updateListPrintConfig(printConfigs);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/get-device-code/by-name")
    public ResponseEntity<ResultDTO> getDeviceCodeByName(
        @RequestParam @NotBlank(message = ExceptionConstants.TAX_CODE_IN_VALID) String taxCode,
        @RequestParam @NotBlank(message = ExceptionConstants.DEVICE_CODE_NOT_NULL) String name
    ) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.getDeviceCodeByName(taxCode, name);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/common/register-owner-device")
    public ResponseEntity<ResultDTO> registerOwnerDevice(@RequestBody DeviceCode deviceCodeRequest) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.registerOwnerDevice(deviceCodeRequest);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/client/page/config/get-with-paging")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.VIEW)
    public ResponseEntity<ResultDTO> getWithPaging(
        Pageable pageable,
        @RequestParam(required = false) Integer companyId,
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate
    ) {
        log.debug("REST request to get all config with paging by keyword: {}", keyword);
        ResultDTO result = configManagementService.getWithPaging(pageable, companyId, keyword, fromDate, toDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/client/page/config/get-by-id/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.VIEW)
    public ResponseEntity<ResultDTO> getWithPaging(@NotNull @PathVariable("id") Integer id) {
        log.debug("REST request to get config detail by id: {}", id);
        ResultDTO result = configManagementService.getConfigDetail(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/admin/client/page/config/create")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.ADD)
    public ResponseEntity<ResultDTO> createConfig(@RequestBody ConfigSaveRequest request) {
        log.debug("REST request to create config by code: {}", request.getCode());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO result = configManagementService.save(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/client/page/config/update")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.EDIT)
    public ResponseEntity<ResultDTO> updateConfig(@RequestBody ConfigSaveRequest request) {
        log.debug("REST request to update config by id: {}", request.getId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO result = configManagementService.save(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/admin/client/page/config/delete/{id}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.DELETE)
    public ResponseEntity<ResultDTO> deleteConfig(@NotNull @PathVariable("id") Integer id) {
        log.debug("REST request to delete config by id: {}", id);
        ResultDTO result = configManagementService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/client/page/config/get-with-owner-id/{ownerId}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.VIEW)
    public ResponseEntity<ResultDTO> getWithOwnerId(@NotNull @PathVariable("ownerId") Integer ownerId) {
        log.debug("REST request to get config by ownerId: {}", ownerId);
        ResultDTO result = configManagementService.getWithOwnerId(ownerId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/admin/client/page/config/get-with-com-id/{comId}")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.VIEW)
    public ResponseEntity<ResultDTO> getWithComId(@NotNull @PathVariable("comId") Integer comId) {
        log.debug("REST request to get config by comId: {}", comId);
        ResultDTO result = configManagementService.getWithComId(comId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/client/page/config/update-by-owner-id")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.EDIT)
    public ResponseEntity<ResultDTO> updateByOwnerId(@RequestBody ConfigOwnerSaveRequest request) {
        log.debug("REST request to save config by ownerId: {}", request.getOwnerId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO result = configManagementService.saveConfigByOwnerId(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/admin/client/page/config/update-by-com-id")
    @CheckAuthorize(value = AuthoritiesConstants.AdminConfig.EDIT)
    public ResponseEntity<ResultDTO> updateByComId(@RequestBody ConfigOwnerSaveRequest request) {
        log.debug("REST request to save config by comId: {}", request.getOwnerId());
        Common.validateInput(validator, ENTITY_NAME, request);
        ResultDTO result = configManagementService.saveConfigByComId(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/config/update-sale-config")
    public ResponseEntity<ResultDTO> updateSellConfig(@RequestBody SellConfig sellConfig) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.updateSellConfig(sellConfig);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/config/update-easy-invoice-config")
    public ResponseEntity<ResultDTO> updateEasyInvoiceConfig(@RequestBody EasyInvoiceConfig config) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.updateEasyInvoiceConfig(config);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/config/update-display_config")
    public ResponseEntity<ResultDTO> updateDisplayConfig(@RequestBody ConfigSaveRequest config) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.updateDisplayConfig(config);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/config/get-display_config")
    public ResponseEntity<ResultDTO> getDisplayConfig() {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = configManagementService.getDisplayConfig();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
