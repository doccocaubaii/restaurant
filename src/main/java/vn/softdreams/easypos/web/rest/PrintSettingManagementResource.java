package vn.softdreams.easypos.web.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.printSetting.SavePrintSettingRequest;
import vn.softdreams.easypos.dto.printTemplate.PrintTemplateDeleteRequest;
import vn.softdreams.easypos.dto.printTemplate.PrintTemplateRequest;
import vn.softdreams.easypos.dto.printTemplate.ProcessingPrintSettingRequest;
import vn.softdreams.easypos.service.PrintSettingManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;

import javax.validation.Validator;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class PrintSettingManagementResource {

    private final Logger log = LoggerFactory.getLogger(PrintSettingManagementResource.class);

    private final PrintSettingManagementService printSettingManagementService;
    private final Validator customValidator;
    private static final String ENTITY_NAME = "Expenditure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public PrintSettingManagementResource(PrintSettingManagementService printSettingManagementService, Validator customValidator) {
        this.printSettingManagementService = printSettingManagementService;
        this.customValidator = customValidator;
    }

    @PostMapping("/client/common/print-template/create")
    public ResponseEntity<ResultDTO> createPrintTemplate(@RequestBody PrintTemplateRequest printTemplate) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, printTemplate);
        log.debug("REST request to save PrintConfig : {}", printTemplate);
        if (printTemplate.getId() != null) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_CREATE_ID, ResultConstants.ERROR_CREATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        ResultDTO result = printSettingManagementService.savePrintTemplate(printTemplate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/common/print-template/update")
    public ResponseEntity<ResultDTO> updatePrintTemplate(@RequestBody PrintTemplateRequest printTemplate) throws URISyntaxException {
        log.error("updatePrintConfig_request {}", printTemplate);
        if (printTemplate.getId() == null) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_UPDATE_ID, ResultConstants.ERROR_UPDATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        ResultDTO result = printSettingManagementService.savePrintTemplate(printTemplate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/print-template/get-all")
    public ResponseEntity<ResultDTO> getAllPrintTemplate() {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printSettingManagementService.getAllPrintTemplate();
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/print-template/get-default")
    public ResponseEntity<ResultDTO> getDefaultTemplate(@RequestParam Integer type) {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printSettingManagementService.getDefaultTemplate(type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/print-template/get-data")
    public ResponseEntity<ResultDTO> getDataPrintTemplate(
        @RequestParam Integer comId,
        @RequestParam(required = false) String code,
        @RequestParam Integer billId
    ) throws JsonProcessingException {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printSettingManagementService.getDataPrintTemplate(comId, code, billId);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/print-template/get-data-default")
    public ResponseEntity<ResultDTO> getDataPrintTemplateDefault(@RequestParam(required = false) String code)
        throws JsonProcessingException {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printSettingManagementService.getDataPrintTemplateDefault(code);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/common/print-template/get-by-processing-area")
    public ResponseEntity<ResultDTO> getDataByProcessingArea(@RequestParam Integer billId, @RequestParam(required = false) Integer type)
        throws JsonProcessingException {
        log.debug("REST request to get a page of PrintConfigs");
        ResultDTO result = printSettingManagementService.getDataPrintByProcessingArea(billId, type);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/common/print-setting/update")
    public ResponseEntity<ResultDTO> updatePrintProcessingArea(@RequestBody List<ProcessingPrintSettingRequest> requests) {
        log.error("updatePrintProcessingArea {}", requests);
        for (ProcessingPrintSettingRequest request : requests) {
            Common.validateInput(customValidator, ENTITY_NAME, request);
        }
        ResultDTO result = printSettingManagementService.savePrintProcessing(requests);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    /**
     * {@code GET  /client/common/print-configs/:id} : get the "id" printConfig.
     *
     * @param id the id of the printConfig to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the printConfig, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/client/common/print-template/{id}")
    public ResponseEntity<ResultDTO> getPrintTemplateById(@PathVariable Integer id) {
        log.debug("REST request to get PrintConfig : {}", id);
        ResultDTO printConfig = printSettingManagementService.getPrintTemplateById(id);
        return new ResponseEntity<>(printConfig, HttpStatus.OK);
    }

    @PutMapping("/client/common/print-template/delete")
    public ResponseEntity<ResultDTO> deletePrintTemplate(@RequestBody PrintTemplateDeleteRequest printTemplate) throws URISyntaxException {
        log.error("updatePrintConfig_request {}", printTemplate);
        if (printTemplate.getId() == null) {
            return new ResponseEntity<>(
                new ResultDTO(ResultConstants.ERROR_UPDATE_ID, ResultConstants.ERROR_UPDATE_ID_VI, false),
                HttpStatus.OK
            );
        }
        ResultDTO result = printSettingManagementService.deletePrintTemplate(printTemplate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/print-setting/get-with-paging")
    public ResponseEntity<ResultDTO> getWithPaging(Pageable pageable, @RequestParam(required = false) String keyword) {
        log.debug("REST request to get a page of Print Setting");
        ResultDTO result = printSettingManagementService.getWithPaging(pageable, keyword);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/page/print-setting/by-id/{id}")
    public ResponseEntity<ResultDTO> getDetailById(@PathVariable Integer id) {
        log.debug("REST request to get a page of Print Setting");
        ResultDTO result = printSettingManagementService.getById(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/print-setting/create")
    public ResponseEntity<ResultDTO> createPrintConfig(@RequestBody SavePrintSettingRequest req) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, req);
        log.debug("REST request to save PrintSetting : {}", req);
        ResultDTO result = printSettingManagementService.createPrintSetting(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/print-setting/update")
    public ResponseEntity<ResultDTO> updatePrintConfig(@RequestBody SavePrintSettingRequest req) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, req);
        log.debug("REST request to save PrintSetting : {}", req);
        ResultDTO result = printSettingManagementService.createPrintSetting(req);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/client/page/print-setting/delete/{id}")
    public ResponseEntity<ResultDTO> deletePrintConfig(@PathVariable Integer id) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, id);
        log.debug("REST request to delete PrintSetting : {}", id);
        ResultDTO result = printSettingManagementService.delete(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
