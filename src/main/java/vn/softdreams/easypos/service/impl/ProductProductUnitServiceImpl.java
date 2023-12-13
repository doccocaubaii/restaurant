package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ProductConstant;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Product;
import vn.softdreams.easypos.domain.ProductProductUnit;
import vn.softdreams.easypos.domain.ProductUnit;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;
import vn.softdreams.easypos.dto.productUnit.SaveConversionUnitRequest;
import vn.softdreams.easypos.repository.ProductProductUnitRepository;
import vn.softdreams.easypos.repository.ProductRepository;
import vn.softdreams.easypos.repository.ProductUnitRepository;
import vn.softdreams.easypos.service.ProductProductUnitService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

import static vn.softdreams.easypos.constants.ResultConstants.SUCCESS;
import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

/**
 * Service Implementation for managing {@link ProductProductUnit}.
 */
@Service
@Transactional
public class ProductProductUnitServiceImpl implements ProductProductUnitService {

    private final Logger log = LoggerFactory.getLogger(ProductProductUnitServiceImpl.class);

    private final ProductProductUnitRepository productProductUnitRepository;
    private final ProductRepository productRepository;
    private final UserService userService;
    private final ProductUnitRepository productUnitRepository;
    private String ENTITY_NAME = "productProductUnit";

    public ProductProductUnitServiceImpl(
        ProductProductUnitRepository productProductUnitRepository,
        ProductRepository productRepository,
        UserService userService,
        ProductUnitRepository productUnitRepository
    ) {
        this.productProductUnitRepository = productProductUnitRepository;
        this.productRepository = productRepository;
        this.userService = userService;
        this.productUnitRepository = productUnitRepository;
    }

    @Override
    public ProductProductUnit save(ProductProductUnit productProductUnit) {
        log.debug("Request to save ProductProductUnit : {}", productProductUnit);
        return productProductUnitRepository.save(productProductUnit);
    }

    @Override
    public ProductProductUnit update(ProductProductUnit productProductUnit) {
        log.debug("Request to update ProductProductUnit : {}", productProductUnit);
        return productProductUnitRepository.save(productProductUnit);
    }

    @Override
    public Optional<ProductProductUnit> partialUpdate(ProductProductUnit productProductUnit) {
        log.debug("Request to partially update ProductProductUnit : {}", productProductUnit);

        return productProductUnitRepository
            .findById(productProductUnit.getId())
            .map(existingProductProductUnit -> {
                if (productProductUnit.getComId() != null) {
                    existingProductProductUnit.setComId(productProductUnit.getComId());
                }
                if (productProductUnit.getProductId() != null) {
                    existingProductProductUnit.setProductId(productProductUnit.getProductId());
                }
                if (productProductUnit.getProductUnitId() != null) {
                    existingProductProductUnit.setProductUnitId(productProductUnit.getProductUnitId());
                }
                if (productProductUnit.getUnitName() != null) {
                    existingProductProductUnit.setUnitName(productProductUnit.getUnitName());
                }
                if (productProductUnit.getIsPrimary() != null) {
                    existingProductProductUnit.setIsPrimary(productProductUnit.getIsPrimary());
                }
                if (productProductUnit.getConvertRate() != null) {
                    existingProductProductUnit.setConvertRate(productProductUnit.getConvertRate());
                }
                if (productProductUnit.getFormula() != null) {
                    existingProductProductUnit.setFormula(productProductUnit.getFormula());
                }
                if (productProductUnit.getPurchasePrice() != null) {
                    existingProductProductUnit.setPurchasePrice(productProductUnit.getPurchasePrice());
                }
                if (productProductUnit.getSalePrice() != null) {
                    existingProductProductUnit.setSalePrice(productProductUnit.getSalePrice());
                }
                if (productProductUnit.getDirectSale() != null) {
                    existingProductProductUnit.setDirectSale(productProductUnit.getDirectSale());
                }
                if (productProductUnit.getDescription() != null) {
                    existingProductProductUnit.setDescription(productProductUnit.getDescription());
                }

                return existingProductProductUnit;
            })
            .map(productProductUnitRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductProductUnit> findAll(Pageable pageable) {
        log.debug("Request to get all ProductProductUnits");
        return productProductUnitRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductProductUnit> findOne(Integer id) {
        log.debug("Request to get ProductProductUnit : {}", id);
        return productProductUnitRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete ProductProductUnit : {}", id);
        productProductUnitRepository.deleteById(id);
    }

    @Override
    public ResultDTO saveConversionUnit(SaveConversionUnitRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        ProductProductUnit unit = new ProductProductUnit();
        validateInput(request, user, Boolean.TRUE);
        Optional<Product> productOptional = productRepository.findByIdAndComId(request.getProductId(), user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();
        if (request.getSalePrice().equals(BigDecimal.ZERO)) {
            BigDecimal price = product.getSalePrice();
            request.setSalePrice(
                Objects.equals(request.getFormula(), ProductConstant.CONVERSION_UNIT.MUL_FORMULA)
                    ? price.multiply(request.getConvertRate())
                    : price.divide(request.getConvertRate(), new MathContext(7))
            );
        }
        if (request.getConvertRate().compareTo(BigDecimal.ZERO) == 0) {
            request.setConvertRate(BigDecimal.ONE);
        }
        BeanUtils.copyProperties(request, unit);
        unit.setProductUnitId(request.getProductUnitId());
        unit.setComId(user.getCompanyId());
        unit.setFormula(Objects.equals(request.getFormula(), ProductConstant.CONVERSION_UNIT.MUL_FORMULA) ? Boolean.FALSE : Boolean.TRUE);
        unit.setUnitName(request.getUnitName());
        unit.setIsPrimary(Boolean.FALSE);
        if (unit.getDirectSale() == null) {
            unit.setDirectSale(Boolean.TRUE);
        }
        String generateDescription = ProductConstant.VALIDATE.generateDescription(
            request.getUnitName(),
            request.getConvertRate(),
            product.getUnit(),
            request.getFormula()
        );
        if (!Strings.isNullOrEmpty(request.getDescription())) {
            if (!generateDescription.trim().equalsIgnoreCase(request.getDescription().trim())) {
                throw new BadRequestAlertException(DESCRIPTION_INVALID_VI, ENTITY_NAME, DESCRIPTION_INVALID);
            }
            unit.setDescription(generateDescription);
        } else {
            unit.setDescription(generateDescription);
        }
        unit.setUnitNormalizedName(Common.normalizedName(Arrays.asList(unit.getUnitName())));
        productProductUnitRepository.save(unit);
        ResultDTO result = new ResultDTO();
        result.setMessage(SUCCESS);
        result.setReason(ResultConstants.CREATE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI);
        result.setStatus(Boolean.TRUE);
        return result;
    }

    @Override
    public ResultDTO updateConversionUnit(SaveConversionUnitRequest request) {
        User user = userService.getUserWithAuthorities(request.getComId());
        validateInput(request, user, Boolean.FALSE);
        Optional<Product> productOptional = productRepository.findByIdAndComId(request.getProductId(), user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();
        Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndProductUnitId(
            request.getProductId(),
            user.getCompanyId(),
            request.getProductUnitId()
        );
        if (unitOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_UNIT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_UNIT_NOT_FOUND);
        }
        ProductProductUnit unit = unitOptional.get();
        if (request.getConvertRate().compareTo(BigDecimal.ZERO) == 0) {
            request.setConvertRate(BigDecimal.ONE);
        }
        if (request.getDirectSale() == null) {
            request.setDirectSale(Boolean.TRUE);
        }
        BeanUtils.copyProperties(request, unit);
        unit.setFormula(Objects.equals(request.getFormula(), ProductConstant.CONVERSION_UNIT.MUL_FORMULA) ? Boolean.FALSE : Boolean.TRUE);
        unit.setUnitName(request.getUnitName());
        String generateDescription = ProductConstant.VALIDATE.generateDescription(
            request.getUnitName(),
            request.getConvertRate(),
            product.getUnit(),
            request.getFormula()
        );
        if (!Strings.isNullOrEmpty(request.getDescription())) {
            if (!generateDescription.trim().equalsIgnoreCase(request.getDescription().trim())) {
                throw new BadRequestAlertException(DESCRIPTION_INVALID_VI, ENTITY_NAME, DESCRIPTION_INVALID);
            }
            unit.setDescription(generateDescription);
        } else {
            unit.setDescription(generateDescription);
        }
        productProductUnitRepository.save(unit);
        ResultDTO result = new ResultDTO();
        result.setMessage(SUCCESS);
        result.setReason(ResultConstants.UPDATE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI);
        result.setStatus(Boolean.TRUE);
        return result;
    }

    private void validateInput(SaveConversionUnitRequest request, User user, boolean isNew) {
        if (isNew) {
            Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByProductIdAndComIdAndProductUnitId(
                request.getProductId(),
                user.getCompanyId(),
                request.getProductUnitId()
            );
            if (unitOptional.isPresent()) {
                throw new BadRequestAlertException(DUPLICATE_CONVERSION_UNIT_VI, ENTITY_NAME, DUPLICATE_CONVERSION_UNIT);
            }
        }
        if (request.getProductUnitId() != 0) {
            Optional<ProductUnit> productUnit = productUnitRepository.findByIdAndComId(request.getProductUnitId(), user.getCompanyId());
            if (productUnit.isEmpty()) {
                throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
            }
            if (!productUnit.get().getName().equals(request.getUnitName())) {
                throw new BadRequestAlertException(UNIT_NAME_NOT_INVALID_VI, ENTITY_NAME, UNIT_NAME_NOT_INVALID);
            }
        } else if (!Strings.isNullOrEmpty(request.getUnitName())) {
            throw new BadRequestAlertException(UNIT_MUST_NOT_EMPTY_VI, ENTITY_NAME, UNIT_MUST_NOT_EMPTY);
        }

        Optional<Product> productOptional = productRepository.findByIdAndComId(request.getProductId(), user.getCompanyId());
        if (productOptional.isEmpty()) {
            throw new BadRequestAlertException(PRODUCT_NOT_FOUND_VI, ENTITY_NAME, PRODUCT_NOT_FOUND);
        }

        Product product = productOptional.get();
        if (product.getUnitId() == null || product.getUnitId() == 0) {
            throw new BadRequestAlertException(UNIT_NAME_IS_NOT_VALID_VI, ENTITY_NAME, UNIT_NAME_IS_NOT_VALID);
        }
        ProductConstant.VALIDATE.validateConversionUnit(request.getConvertRate(), request.getProductUnitId(), product.getUnitId());
    }

    @Override
    public ResultDTO getByProductId(Integer productId) {
        User user = userService.getUserWithAuthorities();
        List<ProductProductUnitResponse> result = new ArrayList<>();
        List<ProductProductUnit> units = productProductUnitRepository.findAllByProductIdAndComId(productId, user.getCompanyId());
        for (ProductProductUnit unit : units) {
            ProductProductUnitResponse response = new ProductProductUnitResponse();
            BeanUtils.copyProperties(unit, response);
            response.setConvertRate(response.getConvertRate().setScale(2));
            if (unit.getFormula() != null) {
                response.setFormula(
                    unit.getFormula() ? ProductConstant.CONVERSION_UNIT.DIV_FORMULA : ProductConstant.CONVERSION_UNIT.MUL_FORMULA
                );
            }
            result.add(response);
        }
        return new ResultDTO(SUCCESS, ResultConstants.GET_PRODUCT_CONVERSION_UNIT_SUCCESS_VI, Boolean.TRUE, result, result.size());
    }

    @Override
    public ResultDTO deleteConversionUnit(Integer id) {
        User user = userService.getUserWithAuthorities();
        Optional<ProductProductUnit> unitOptional = productProductUnitRepository.findByIdAndComId(id, user.getCompanyId());
        if (unitOptional.isEmpty()) {
            throw new BadRequestAlertException(UNIT_NOT_FOUND_VI, ENTITY_NAME, UNIT_NOT_FOUND);
        }
        productProductUnitRepository.delete(unitOptional.get());
        return new ResultDTO(SUCCESS, ResultConstants.DELETE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI, Boolean.TRUE);
    }
}
