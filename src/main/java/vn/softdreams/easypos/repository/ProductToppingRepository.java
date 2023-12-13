package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Product;
import vn.softdreams.easypos.domain.ProductTopping;
import vn.softdreams.easypos.dto.toppingGroup.IToppingCount;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupItem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data JPA repository for the ProductTopping entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductToppingRepository extends JpaRepository<ProductTopping, Integer>, ProductToppingRepositoryCustom {
    List<ProductTopping> findAllByToppingGroupId(Integer id);

    @Query(value = "select distinct product_id from product_topping where topping_group_id = ?1", nativeQuery = true)
    List<Integer> findProductIdByToppingGroupId(Integer id);

    @Query(
        value = "select distinct ppu.id from product_topping pt " +
        "join product_product_unit ppu on pt.product_id = ppu.product_id " +
        "where topping_id in ?1",
        nativeQuery = true
    )
    List<Integer> findProductIdByToppingIdInIds(Set<Integer> ids);

    @Query(
        value = "select distinct topping_group_id from product_topping where product_id = ?1 and topping_group_id is not null",
        nativeQuery = true
    )
    List<Integer> findToppingGroupIdByProductId(Integer id);

    List<ProductTopping> findAllByProductIdAndToppingGroupId(Integer productId, Integer toppingGroupId);
    List<ProductTopping> findAllByProductId(Integer productId);
    List<ProductTopping> findAllByProductIdOrToppingId(Integer productId, Integer toppingId);
    List<ProductTopping> findAllByToppingId(Integer toppingId);
    Optional<ProductTopping> findOneByToppingIdAndProductIdAndToppingGroupIdIsNull(Integer toppingId, Integer productId);

    @Query(
        value = "select distinct p.* from product_topping pt  " +
        "                join product p on p.id = pt.topping_id  " +
        "                where product_id = ?1 and topping_group_id = ?2",
        nativeQuery = true
    )
    List<Product> findDistinctByProductIdAndToppingGroupId(Integer productId, Integer toppingGroupId);

    @Query(
        value = "select distinct tg.id, tg.name, tg.required_optional requiredOptional, pt.product_id productId " +
        "from product_topping pt join topping_group tg on pt.topping_group_id = tg.id " +
        "where pt.product_id = ?1",
        nativeQuery = true
    )
    List<ToppingGroupItem> findToppingGroupByProductId(Integer productId);

    @Query(
        value = "select distinct tg.id, tg.name, tg.required_optional requiredOptional, pt.product_id productId " +
        "from product_topping pt join topping_group tg on pt.topping_group_id = tg.id " +
        " where tg.com_id = ?1",
        nativeQuery = true
    )
    List<ToppingGroupItem> findAllToppingGroup(Integer comId);

    @Query(value = "select distinct product_id from product_topping", nativeQuery = true)
    List<Integer> findAllProductId();

    @Query(
        value = "select topping_group_id as toppingGroupId, count(distinct product_id) as number from product_topping where topping_group_id is not null group by topping_group_id",
        nativeQuery = true
    )
    List<IToppingCount> getProductLink();

    @Query(
        value = "select topping_group_id as toppingGroupId, count(*) as number from topping_topping_group group by topping_group_id",
        nativeQuery = true
    )
    List<IToppingCount> getProductInside();
}
