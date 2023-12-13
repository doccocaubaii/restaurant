package vn.softdreams.easypos.service.auto_service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.ProductTopping;

import java.util.Optional;

/**
 * Service Interface for managing {@link ProductTopping}.
 */
public interface ProductToppingService {
    /**
     * Save a productTopping.
     *
     * @param productTopping the entity to save.
     * @return the persisted entity.
     */
    ProductTopping save(ProductTopping productTopping);

    /**
     * Updates a productTopping.
     *
     * @param productTopping the entity to update.
     * @return the persisted entity.
     */
    ProductTopping update(ProductTopping productTopping);

    /**
     * Partially updates a productTopping.
     *
     * @param productTopping the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ProductTopping> partialUpdate(ProductTopping productTopping);

    /**
     * Get all the productToppings.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ProductTopping> findAll(Pageable pageable);

    /**
     * Get the "id" productTopping.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ProductTopping> findOne(Integer id);

    /**
     * Delete the "id" productTopping.
     *
     * @param id the id of the entity.
     */
    void delete(Integer id);
}
