package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.ProcessingAreaProduct;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.processingAreaProduct.ProcessingAreaProductItemResponse;
import vn.softdreams.easypos.dto.processingAreaProduct.ProductProcessingArea;
import vn.softdreams.easypos.repository.ProcessingAreaProductRepository;
import vn.softdreams.easypos.repository.ProcessingAreaProductRepositoryCustom;
import vn.softdreams.easypos.service.ProcessingAreaProductManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.util.Optional;

@Service
@Transactional
public class ProcessingAreaProductManagementServiceImpl implements ProcessingAreaProductManagementService {

    private final Logger log = LoggerFactory.getLogger(ProcessingAreaProductManagementServiceImpl.class);
    private final String ENTITY_NAME = "processing-area-product";
    private final ProcessingAreaProductRepository processingAreaProductRepository;
    private final ProcessingAreaProductRepositoryCustom processingAreaProductRepositoryCustom;
    private final UserService userService;

    public ProcessingAreaProductManagementServiceImpl(
        ProcessingAreaProductRepository processingAreaProductRepository,
        ProcessingAreaProductRepositoryCustom processingAreaProductRepositoryCustom,
        UserService userService
    ) {
        this.processingAreaProductRepository = processingAreaProductRepository;
        this.processingAreaProductRepositoryCustom = processingAreaProductRepositoryCustom;
        this.userService = userService;
    }

    @Override
    public ResultDTO getProductByProcessingAreaId(Integer processingAreaId, Pageable pageable) {
        log.debug("Request to filter processingAreaProduct");
        Page<ProcessingAreaProductItemResponse> page = processingAreaProductRepositoryCustom.getProductByProcessingAreaId(
            processingAreaId,
            pageable
        );
        return new ResultDTO(
            ResultConstants.SUCCESS,
            ResultConstants.SUCCESS_GET_LIST,
            true,
            page.getContent(),
            (int) page.getTotalElements()
        );
    }

    @Override
    public ResultDTO delete(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ProcessingAreaProduct> result = processingAreaProductRepository.findByIdAndComId(id, user.getCompanyId());
        if (result.isPresent()) {
            processingAreaProductRepository.deleteById(result.get().getId());
        } else {
            throw new BadRequestAlertException(
                ExceptionConstants.PROCESSING_AREA_PRODUCT_NOT_EXIT_VI,
                ENTITY_NAME,
                ExceptionConstants.PROCESSING_AREA_PRODUCT_NOT_EXIT
            );
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.DELETE_PROCESSING_AREA_PRODUCT_SUCCESS, true, result.get());
    }

    @Override
    public ResultDTO getAllProductProductUnitIdNotPaId(Integer comId, Integer processingAreaId) {
        User user = userService.getUserWithAuthorities();
        Page<ProductProcessingArea> page = processingAreaProductRepositoryCustom.getAllProductProductUnitIdNotPaId(
            user.getCompanyId(),
            processingAreaId
        );
        Integer count = 0;
        if (page.getContent().size() > 0) {
            for (ProductProcessingArea item : page.getContent()) {
                count++;
            }
            return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS, true, page.getContent(), count);
        }
        return new ResultDTO(ResultConstants.FAIL, ResultConstants.FAIL, true, count);
    }
}
