package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ToppingToppingGroup;
import vn.softdreams.easypos.dto.UnitResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingRequiredItem;

import java.util.List;

/**
 * Spring Data JPA repository for the ToppingToppingGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ToppingToppingGroupRepository extends JpaRepository<ToppingToppingGroup, Integer> {
    @Query(
        value = "select product_id productId, topping_group_id toppingGroupId from topping_topping_group where topping_group_id in ?1",
        nativeQuery = true
    )
    List<ToppingRequiredItem> findProductIdByToppingGroupId(List<Integer> toppingGroupId);

    List<ToppingToppingGroup> findAllByToppingGroupId(Integer toppingGroupId);
    List<ToppingToppingGroup> findAllByProductId(Integer productId);

    @Query(
        value = "with t as ( " +
        "    select bp.parent_id " +
        "    from bill b join bill_product bp on b.id = bp.bill_id " +
        "    where b.status = 0 and bp.product_id = ?1 and bp.parent_id is not null " +
        ") " +
        "select count(topping_group_id) " +
        "from t join bill_product bp on t.parent_id = bp.id " +
        "join product_topping pt on bp.product_id = pt.product_id " +
        "where topping_group_id is not null and topping_group_id = ?2",
        nativeQuery = true
    )
    Integer checkDeleteProductInGroup(Integer productId, Integer toppingGroupId);

    @Query(
        value = "select COUNT(*) from bill b join bill_product bp on b.id = bp.bill_id where b.status = 0 and bp.product_id = ?1 and bp.parent_id is not null",
        nativeQuery = true
    )
    Integer checkUpdateProduct(Integer productId);

    @Query(
        value = "select topping_group_id id, tg.name name " +
        "from topping_topping_group ttg " +
        "join topping_group tg on ttg.topping_group_id = tg.id " +
        "where topping_group_id in ?1 " +
        "group by topping_group_id, tg.name " +
        "having count(product_id) = 1",
        nativeQuery = true
    )
    List<UnitResponse> getEmptyToppingGroup(List<Integer> ids);
}
