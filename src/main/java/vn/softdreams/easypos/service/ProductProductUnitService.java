package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ProductProductUnit;
import vn.softdreams.easypos.dto.productUnit.SaveConversionUnitRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link ProductProductUnit}.
 */
public interface ProductProductUnitService {
    ProductProductUnit save(ProductProductUnit productProductUnit);
    ProductProductUnit update(ProductProductUnit productProductUnit);
    Optional<ProductProductUnit> partialUpdate(ProductProductUnit productProductUnit);
    Page<ProductProductUnit> findAll(Pageable pageable);
    Optional<ProductProductUnit> findOne(Integer id);
    void delete(Integer id);
    ResultDTO saveConversionUnit(SaveConversionUnitRequest request);
    ResultDTO updateConversionUnit(SaveConversionUnitRequest request);
    ResultDTO getByProductId(Integer productId);
    ResultDTO deleteConversionUnit(Integer id);
}
