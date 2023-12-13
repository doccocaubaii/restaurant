package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.domain.ProductTopping;
import vn.softdreams.easypos.repository.ProductToppingRepository;
import vn.softdreams.easypos.service.auto_service.ProductToppingService;

import java.util.Optional;

/**
 * Service Implementation for managing {@link ProductTopping}.
 */
@Service
@Transactional
public class ProductToppingServiceImpl implements ProductToppingService {

    private final Logger log = LoggerFactory.getLogger(ProductToppingServiceImpl.class);

    private final ProductToppingRepository productToppingRepository;

    public ProductToppingServiceImpl(ProductToppingRepository productToppingRepository) {
        this.productToppingRepository = productToppingRepository;
    }

    @Override
    public ProductTopping save(ProductTopping productTopping) {
        log.debug("Request to save ProductTopping : {}", productTopping);
        return productToppingRepository.save(productTopping);
    }

    @Override
    public ProductTopping update(ProductTopping productTopping) {
        log.debug("Request to update ProductTopping : {}", productTopping);
        return productToppingRepository.save(productTopping);
    }

    @Override
    public Optional<ProductTopping> partialUpdate(ProductTopping productTopping) {
        log.debug("Request to partially update ProductTopping : {}", productTopping);

        return productToppingRepository
            .findById(productTopping.getId())
            .map(existingProductTopping -> {
                if (productTopping.getProductId() != null) {
                    existingProductTopping.setProductId(productTopping.getProductId());
                }
                if (productTopping.getToppingId() != null) {
                    existingProductTopping.setToppingId(productTopping.getToppingId());
                }
                if (productTopping.getToppingGroupId() != null) {
                    existingProductTopping.setToppingGroupId(productTopping.getToppingGroupId());
                }

                return existingProductTopping;
            })
            .map(productToppingRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductTopping> findAll(Pageable pageable) {
        log.debug("Request to get all ProductToppings");
        return productToppingRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductTopping> findOne(Integer id) {
        log.debug("Request to get ProductTopping : {}", id);
        return productToppingRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete ProductTopping : {}", id);
        productToppingRepository.deleteById(id);
    }
}
