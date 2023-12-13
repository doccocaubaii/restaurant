package vn.softdreams.easypos.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.constants.TaskLogConstants;
import vn.softdreams.easypos.domain.ProductUnit;
import vn.softdreams.easypos.domain.TaskLog;
import vn.softdreams.easypos.dto.TaskLogSendQueue;
import vn.softdreams.easypos.dto.productUnit.UpdateUnitNameProd;
import vn.softdreams.easypos.integration.easybooks88.queue.dto.ProductUnitTask;
import vn.softdreams.easypos.repository.ProductRepository;
import vn.softdreams.easypos.repository.ProductUnitRepository;
import vn.softdreams.easypos.repository.TaskLogRepository;
import vn.softdreams.easypos.service.ProductManagementService;
import vn.softdreams.easypos.service.ProductUnitService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.util.List;
import java.util.Optional;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.UNIT_NAME_IS_EXISTED;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.UNIT_NAME_IS_EXISTED_VI;

@Service
@Transactional
public class ProductUnitServiceImpl implements ProductUnitService {

    private final Logger log = LoggerFactory.getLogger(ProductUnitServiceImpl.class);

    private final ProductUnitRepository productUnitRepository;
    private final ProductManagementService productManagementService;
    private final ProductRepository productRepository;
    private final TaskLogRepository taskLogRepository;
    private final TransactionTemplate transactionTemplate;
    private final UserService userService;
    private final String ENTITY_NAME = "product_unit";

    public ProductUnitServiceImpl(
        ProductUnitRepository productUnitRepository,
        ProductManagementService productManagementService,
        ProductRepository productRepository,
        TaskLogRepository taskLogRepository,
        TransactionTemplate transactionTemplate,
        UserService userService
    ) {
        this.productUnitRepository = productUnitRepository;
        this.productManagementService = productManagementService;
        this.productRepository = productRepository;
        this.taskLogRepository = taskLogRepository;
        this.transactionTemplate = transactionTemplate;
        this.userService = userService;
    }

    @Override
    public ProductUnit save(ProductUnit productUnit) {
        log.debug("Request to save ProductUnit : {}", productUnit);
        return productUnitRepository.save(productUnit);
    }

    @Override
    public ProductUnit update(ProductUnit productUnit) {
        log.debug("Request to update ProductUnit : {}", productUnit);
        return productUnitRepository.save(productUnit);
    }

    @Override
    public Optional<ProductUnit> partialUpdate(ProductUnit productUnit) {
        log.debug("Request to partially update ProductUnit : {}", productUnit);

        return productUnitRepository
            .findById(productUnit.getId())
            .map(existingProductUnit -> {
                if (productUnit.getComId() != null) {
                    existingProductUnit.setComId(productUnit.getComId());
                }
                if (productUnit.getName() != null) {
                    existingProductUnit.setName(productUnit.getName());
                }
                if (productUnit.getDescription() != null) {
                    existingProductUnit.setDescription(productUnit.getDescription());
                }
                if (productUnit.getEbId() != null) {
                    existingProductUnit.setEbId(productUnit.getEbId());
                }
                if (productUnit.getActive() != null) {
                    existingProductUnit.setActive(productUnit.getActive());
                }

                return existingProductUnit;
            })
            .map(productUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductUnit> findAll(Pageable pageable) {
        log.debug("Request to get all ProductUnits");
        return productUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductUnit> findOne(Integer id) {
        log.debug("Request to get ProductUnit : {}", id);
        return productUnitRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete ProductUnit : {}", id);
        productUnitRepository.deleteById(id);
    }

    @Override
    public ResultDTO updateUnitNameProd(List<Integer> comIds) {
        List<String> units = productUnitRepository.getAllUnitByComId(comIds);
        if (units.isEmpty()) {
            units.add("");
        }
        List<UpdateUnitNameProd> unitNames = productUnitRepository.getAllUnitNotExist(units, comIds);
        for (UpdateUnitNameProd unit : unitNames) {
            String checkName = unit.getUnitName().trim().toUpperCase();
            Optional<ProductUnit> productUnitOptional = productUnitRepository.findByComIdAndUppercaseName(unit.getComId(), checkName);
            if (productUnitOptional.isPresent()) throw new BadRequestAlertException(
                UNIT_NAME_IS_EXISTED_VI,
                ENTITY_NAME,
                UNIT_NAME_IS_EXISTED
            );

            ProductUnit productUnit = new ProductUnit();
            productUnit.setComId(unit.getComId());
            productUnit.setName(unit.getUnitName());
            productUnit.setActive(true);
            TaskLogSendQueue taskLogSendQueue = transactionTemplate.execute(status -> {
                productUnitRepository.save(productUnit);
                //Thêm vào taskLog để xử lý
                try {
                    return createAndPublishQueueProductUnitTask(
                        unit.getComId(),
                        productUnit.getId(),
                        TaskLogConstants.Type.EB_CREATE_PRODUCT_UNIT
                    );
                } catch (Exception e) {
                    log.error("Can not create queue task for eb88 creating product unit: {}", e.getMessage());
                }
                return null;
            });
            if (taskLogSendQueue != null) {
                userService.sendTaskLog(taskLogSendQueue);
            }
        }
        productUnitRepository.updateUnitIdNull(comIds);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.UPDATE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI, true);
    }

    private TaskLogSendQueue createAndPublishQueueProductUnitTask(int comId, int productUnitId, String taskType) throws Exception {
        ProductUnitTask task = new ProductUnitTask();
        task.setComId("" + comId);
        task.setProductUnitId("" + productUnitId);
        TaskLog taskLog = new TaskLog();
        taskLog.setComId(comId);
        taskLog.setStatus(TaskLogConstants.Status.PROCESSING);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        taskLog.setContent(objectMapper.writeValueAsString(task));
        taskLog.setType(taskType);
        taskLog = taskLogRepository.save(taskLog);
        return new TaskLogSendQueue(taskLog.getId().toString(), taskLog.getType());
    }
}
