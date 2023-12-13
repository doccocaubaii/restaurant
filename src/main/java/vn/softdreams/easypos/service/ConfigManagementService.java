package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Config;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.authorities.ConfigInvoice;
import vn.softdreams.easypos.dto.authorities.LoginEasyInvoice;
import vn.softdreams.easypos.dto.config.*;
import vn.softdreams.easypos.dto.invoice.DeclarationRequest;
import vn.softdreams.easypos.dto.invoice.RegisterEasyInvoiceRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Config}.
 */
public interface ConfigManagementService {
    /**
     * Updates a config.
     *
     * @param config the entity to update.
     * @return the persisted entity.
     */
    Config update(Config config);

    /**
     * Partially updates a config.
     *
     * @param config the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Config> partialUpdate(Config config);

    /**
     * Get all the configs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */

    /**
     * Get the "id" config.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Config> findOne(Integer id);

    /**
     * Delete the "id" config.
     *
     * @param id the id of the entity.
     */
    ResultDTO delete(Integer id);

    ResultDTO loginEasyInvoice(LoginEasyInvoice loginEasyInvoiceDTO);

    ResultDTO getAllConfigs();

    ResultDTO registerAndPublish(RegisterEasyInvoiceRequest registerEasyInvoiceDTO);

    ResultDTO declarationSearch(DeclarationRequest declarationDTO);

    ResultDTO getAllPrintConfigs(Integer comId, Integer type);

    ResultDTO getRegisterInvoicePatterns(Integer comId);

    ResultDTO updateInvoiceConfig(InvoiceConfig invoiceConfig);

    ResultDTO updatePrintConfig(PrintConfigCompany printConfigCompany);
    ResultDTO updateListPrintConfig(List<PrintConfigCompany> printConfigs);

    ResultDTO getDeviceCodeByName(String taxCode, String name);

    ResultDTO registerOwnerDevice(DeviceCode deviceCodeRequest);

    ResultDTO getInfoConfigByCompanyId(Integer companyId);

    ResultDTO getWithPaging(Pageable pageable, Integer companyId, String keyword, String fromDate, String toDate);

    ResultDTO getConfigDetail(Integer id);

    ResultDTO save(ConfigSaveRequest request);

    ResultDTO getWithOwnerId(Integer ownerId);

    ResultDTO getWithComId(Integer comId);

    ResultDTO saveConfigByOwnerId(ConfigOwnerSaveRequest request);

    ResultDTO saveConfigByComId(ConfigOwnerSaveRequest request);

    ConfigInvoice getConfigInvoiceByCompanyID(User user);
    ResultDTO updateSellConfig(SellConfig sellConfig);
    ResultDTO updateEasyInvoiceConfig(EasyInvoiceConfig easyInvoiceConfig);

    ResultDTO updateDisplayConfig(ConfigSaveRequest config);

    ResultDTO getDisplayConfig();
}
