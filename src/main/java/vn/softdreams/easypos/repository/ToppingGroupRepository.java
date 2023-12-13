package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ToppingGroup;
import vn.softdreams.easypos.dto.toppingGroup.ToppingRequiredItem;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Spring Data SQL repository for the ToppingGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToppingGroupRepository extends JpaRepository<ToppingGroup, Integer>, ToppingGroupCustomRepository {
    Optional<ToppingGroup> findByIdAndComId(Integer id, Integer comId);
    Optional<ToppingGroup> findOneByComIdAndName(Integer comId, String name);
    List<ToppingGroup> findAllByComId(Integer comId, Pageable pageable);
    List<ToppingGroup> findAllByComIdAndIdIn(Integer comId, Set<Integer> ids);

    @Query(
        value = "select distinct product_id productId, topping_group_id toppingGroupId " +
        "from product_topping pt " +
        "join topping_group tg on pt.topping_group_id = tg.id " +
        "where product_id in ?1 and topping_group_id is not null and required_optional = 1",
        nativeQuery = true
    )
    List<ToppingRequiredItem> getAllRequiredGroupByProductId(List<Integer> productId);

    @Query(
        value = "select distinct topping_group_id from topping_topping_group where product_id = ?1 and topping_group_id is not null",
        nativeQuery = true
    )
    List<Integer> findToppingGroupIdByProductId(Integer id);

    @Query(
        value = "with t as ( " +
        "    select bp.parent_id " +
        "    from bill b join bill_product bp on b.id = bp.bill_id " +
        "    where b.status = 0 and bp.parent_id is not null " +
        ") " +
        "select count(topping_group_id) " +
        "from t join bill_product bp on t.parent_id = bp.id " +
        "join product_topping pt on bp.product_id = pt.product_id " +
        "where topping_group_id is not null and topping_group_id = ?1 and bp.product_id = ?2",
        nativeQuery = true
    )
    Integer checkDeleteGroup(Integer toppingGroupId, Integer productId);

    @Query(
        value = "with t as ( " +
        "    select bp.parent_id " +
        "    from bill b join bill_product bp on b.id = bp.bill_id " +
        "    where b.status = 0 and bp.parent_id is not null " +
        ") " +
        "select count(topping_group_id) " +
        "from t join bill_product bp on t.parent_id = bp.id " +
        "join product_topping pt on bp.product_id = pt.product_id " +
        "where topping_group_id is not null and topping_group_id = ?1",
        nativeQuery = true
    )
    Integer checkDeleteGroup(Integer toppingGroupId);

    @Query(
        value = "with t as ( " +
        "    select bp.parent_id " +
        "    from bill b join bill_product bp on b.id = bp.bill_id " +
        "    where b.status = 0 and bp.product_id = ?1 and bp.parent_id is not null " +
        ") " +
        "select count(*) " +
        "from t join bill_product bp on t.parent_id = bp.id " +
        "join product_topping pt on bp.product_id = pt.product_id " +
        "where topping_group_id is null and topping_id = ?1",
        nativeQuery = true
    )
    Integer checkDeleteProduct(Integer productId);
}
