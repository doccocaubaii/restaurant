package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ProductUnit;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link ProductUnit}.
 */
public interface ProductUnitService {
    ProductUnit save(ProductUnit productUnit);

    ProductUnit update(ProductUnit productUnit);

    Optional<ProductUnit> partialUpdate(ProductUnit productUnit);

    Page<ProductUnit> findAll(Pageable pageable);

    Optional<ProductUnit> findOne(Integer id);

    void delete(Integer id);

    ResultDTO updateUnitNameProd(List<Integer> comIds);
}
