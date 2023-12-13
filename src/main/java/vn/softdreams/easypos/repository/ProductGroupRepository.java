package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProductGroup;
import vn.softdreams.easypos.dto.productGroup.ProductGroupItem;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ProductGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductGroupRepository extends JpaRepository<ProductGroup, Integer>, ProductGroupRepositoryCustom {
    //    @Query(value = "select count(*) from PRODUCT_GROUP where com_id = ?1 and code = ?2", nativeQuery = true)
    //    Integer countAllByCompanyIdAndCode(Integer comId, String code);
    //
    //    @Query(value = "SELECT code FROM PRODUCT_GROUP WHERE id = ?1", nativeQuery = true)
    //    String getCodeById(Integer id);
    //
    //    @Query(value = "SELECT * FROM PRODUCT_GROUP WHERE id = ?1 AND com_id = ?2", nativeQuery = true)
    //    Optional<ProductGroup> findById(Integer id, Integer comId);

    //    @Query(
    //        value = "SELECT * FROM product_product_group ppg JOIN product_group pg " +
    //        "on ppg.product_group_id = pg.id " +
    //        "WHERE ppg.product_id = ?1 and pg.com_id = ?2",
    //        nativeQuery = true
    //    )

    //    @Query(value = "SELECT * FROM product_group WHERE id = ?1 AND com_id = ?2", nativeQuery = true)
    Optional<ProductGroup> getProductGroupByIdAndComId(Integer id, Integer comId);

    @Query(
        value = "SELECT pg.* FROM product_group pg " +
        "JOIN product_product_group ppg ON pg.id = ppg.product_group_id " +
        "WHERE ppg.product_id = ?1 AND pg.com_id = ?2",
        nativeQuery = true
    )
    List<ProductGroup> findAllByProductId(Integer productId, Integer comId);

    @Query(value = "SELECT * FROM product_group WHERE id IN (?1) AND com_Id = ?2", nativeQuery = true)
    List<ProductGroup> findAllByIdAndComId(List<Integer> productGroupIds, Integer comId);

    Optional<ProductGroup> findByIdAndComId(Integer id, Integer comId);

    @Modifying
    @Query(value = "delete from product_group where id = ?1", nativeQuery = true)
    void deleteByIdFromProductGroup(Integer id);

    @Modifying
    @Query(value = "delete from product_product_group where product_group_id = ?1", nativeQuery = true)
    void deleteByIdFromProductProductGroup(Integer id);

    @Modifying
    @Query(value = "delete from product_product_group where product_group_id in ?1", nativeQuery = true)
    void deleteByIdsFromProductProductGroup(List<Integer> ids);

    @Modifying
    @Query(value = "delete from product_group where id in ?1", nativeQuery = true)
    void deleteByIdsFromProductGroup(List<Integer> ids);

    Integer countAllByComIdAndName(Integer comId, String name);

    @Query(value = "select count(*) from product_group pg where  id <> ?1 and pg.com_id = ?2 and name = ?3", nativeQuery = true)
    Integer countAllByComIdAndIDAndName(Integer id, Integer comId, String name);

    @Query(
        value = "select ppg.product_group_id id from product_product_group ppg join product p on ppg.product_id = p.id where com_id = ?1 and product_id = ?2",
        nativeQuery = true
    )
    List<Integer> getAllByComIdAndProductId(Integer comId, Integer productId);

    List<ProductGroup> findAllByComId(Integer comId);

    @Query(value = "select lower(name) from product_group where com_id = ?1", nativeQuery = true)
    List<String> findAllNameByComId(Integer comId);

    @Query(value = "select id from product_group where com_id = ?1", nativeQuery = true)
    List<Integer> findAllIdByComId(Integer comId);

    @Query(value = "select id, name from product_group where com_id = ?1 and id in ?2", nativeQuery = true)
    List<ProductGroupItem> getAllByIds(Integer comId, List<Integer> ids);
}
