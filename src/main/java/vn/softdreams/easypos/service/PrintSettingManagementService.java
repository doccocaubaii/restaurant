package vn.softdreams.easypos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.printSetting.SavePrintSettingRequest;
import vn.softdreams.easypos.dto.printTemplate.PrintTemplateDeleteRequest;
import vn.softdreams.easypos.dto.printTemplate.PrintTemplateRequest;
import vn.softdreams.easypos.dto.printTemplate.ProcessingPrintSettingRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface PrintSettingManagementService {
    ResultDTO savePrintTemplate(PrintTemplateRequest printTemplate);
    ResultDTO savePrintProcessing(List<ProcessingPrintSettingRequest> requests);

    ResultDTO getAllPrintTemplate();

    ResultDTO getPrintTemplateById(Integer id);

    ResultDTO getDataPrintTemplate(Integer comId, String code, Integer billId) throws JsonProcessingException;

    ResultDTO getDefaultTemplate(Integer type);

    ResultDTO deletePrintTemplate(PrintTemplateDeleteRequest type);

    ResultDTO getDataPrintTemplateDefault(String code) throws JsonProcessingException;
    ResultDTO getDataPrintByProcessingArea(Integer billId, Integer type) throws JsonProcessingException;

    ResultDTO createPrintSetting(SavePrintSettingRequest request);
    ResultDTO getWithPaging(Pageable pageable, String keyword);
    ResultDTO getById(Integer id);
    ResultDTO delete(Integer id);
}
