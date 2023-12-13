package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.PrintConfig;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.config.PrintConfigCompany;
import vn.softdreams.easypos.dto.config.PrintConfigRequest;
import vn.softdreams.easypos.repository.PrintConfigRepository;
import vn.softdreams.easypos.service.PrintConfigService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PrintConfig}.
 */
@Service
@Transactional
public class PrintConfigServiceImpl implements PrintConfigService {

    private final Logger log = LoggerFactory.getLogger(PrintConfigServiceImpl.class);

    private final PrintConfigRepository printConfigRepository;
    private final UserService userService;

    public PrintConfigServiceImpl(PrintConfigRepository printConfigRepository, UserService userService) {
        this.printConfigRepository = printConfigRepository;
        this.userService = userService;
    }

    @Override
    public ResultDTO save(PrintConfigRequest printConfigDTO) {
        log.debug("Request to save PrintConfig : {}", printConfigDTO);
        User user = userService.getUserWithAuthorities();
        PrintConfig printConfig = new PrintConfig();
        BeanUtils.copyProperties(printConfigDTO, printConfig);
        printConfig.setComId(user.getCompanyId());
        printConfigRepository.save(printConfig);
        return new ResultDTO(
            ResultConstants.SUCCESS,
            printConfigDTO.getId() != null ? ResultConstants.SUCCESS_UPDATE : ResultConstants.SUCCESS_CREATE,
            true
        );
    }

    @Override
    public PrintConfig update(PrintConfig printConfig) {
        log.debug("Request to update PrintConfig : {}", printConfig);
        return printConfigRepository.save(printConfig);
    }

    @Override
    public Optional<PrintConfig> partialUpdate(PrintConfig printConfig) {
        log.debug("Request to partially update PrintConfig : {}", printConfig);

        return printConfigRepository
            .findById(printConfig.getId())
            .map(existingPrintConfig -> {
                if (printConfig.getComId() != null) {
                    existingPrintConfig.setComId(printConfig.getComId());
                }
                if (printConfig.getCode() != null) {
                    existingPrintConfig.setCode(printConfig.getCode());
                }
                if (printConfig.getTitle() != null) {
                    existingPrintConfig.setTitle(printConfig.getTitle());
                }
                if (printConfig.getName() != null) {
                    existingPrintConfig.setName(printConfig.getName());
                }
                if (printConfig.getFontSize() != null) {
                    existingPrintConfig.setFontSize(printConfig.getFontSize());
                }
                if (printConfig.getAlignText() != null) {
                    existingPrintConfig.setAlignText(printConfig.getAlignText());
                }
                if (printConfig.getIsBold() != null) {
                    existingPrintConfig.setIsBold(printConfig.getIsBold());
                }
                if (printConfig.getIsPrint() != null) {
                    existingPrintConfig.setIsPrint(printConfig.getIsPrint());
                }
                if (printConfig.getIsHeader() != null) {
                    existingPrintConfig.setIsHeader(printConfig.getIsHeader());
                }
                if (printConfig.getIsEditable() != null) {
                    existingPrintConfig.setIsEditable(printConfig.getIsEditable());
                }
                if (printConfig.getVersion() != null) {
                    existingPrintConfig.setVersion(printConfig.getVersion());
                }

                return existingPrintConfig;
            })
            .map(printConfigRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findAll() {
        log.debug("Request to get all PrintConfigs");
        User user = userService.getUserWithAuthorities();
        List<PrintConfigCompany> printConfigList = printConfigRepository.findAllByCompanyID(user.getCompanyId(), null);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printConfigList, printConfigList.size());
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO findOne(Integer id) {
        log.debug("Request to get PrintConfig : {}", id);
        User user = userService.getUserWithAuthorities();
        Optional<PrintConfig> printConfig = printConfigRepository.findByIdAndComId(id, user.getCompanyId());
        if (printConfig.isPresent()) {
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_LOAD_DATA, true, printConfig.get(), 1);
        }
        throw new InternalServerException(ResultConstants.ERROR_LOAD_DATA_VI, "findOnePrintConfig", ResultConstants.ERROR_LOAD_DATA);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete PrintConfig : {}", id);
        printConfigRepository.deleteById(id);
    }
}
