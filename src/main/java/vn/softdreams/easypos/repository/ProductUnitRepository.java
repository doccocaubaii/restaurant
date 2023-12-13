package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.ProductUnit;
import vn.softdreams.easypos.dto.UnitResponse;
import vn.softdreams.easypos.dto.productUnit.GetUnitEBId;
import vn.softdreams.easypos.dto.productUnit.ProductUnitResponse;
import vn.softdreams.easypos.dto.productUnit.UpdateUnitNameProd;
import vn.softdreams.easypos.dto.queue.ObjectAsyncResponse;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the ProductUnit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductUnitRepository extends JpaRepository<ProductUnit, Integer>, ProductUnitRepositoryCustom {
    Optional<ProductUnit> findByIdAndComId(Integer unitId, Integer companyId);

    @Query(value = "select * from product_unit where com_id = ?1 and id in ?2", nativeQuery = true)
    List<ProductUnit> searchAllByComIdAndIds(Integer companyId, List<Integer> ids);

    @Query(value = "select id, name from product_unit where com_id = ?1", nativeQuery = true)
    List<UnitResponse> findAllByComId(Integer comId);

    @Query(value = "select id, name, com_id comId, null type from product_unit where com_id in ?1", nativeQuery = true)
    List<ObjectAsyncResponse> findAllByComIds(List<Integer> comId);

    @Query(value = "select * from product_unit where com_id = ?1 and UPPER(name) = ?2 and active = 1", nativeQuery = true)
    Optional<ProductUnit> findByComIdAndUppercaseName(Integer companyId, String uppercaseName);

    @Query(value = "select * from product_unit where com_id = ?1", nativeQuery = true)
    List<ProductUnit> findAllProductUnitByComId(Integer comId);

    @Query(value = "select eb_id from product_unit where id = ?1 and com_id = ?2", nativeQuery = true)
    Integer findEbIdByIdAndComId(Integer id, Integer comId);

    @Query(
        value = "select distinct b.unit_id id, p.eb_id ebId from bill_product b join  product_unit p on b.unit_id = p.id where b.bill_id = ?1 and p.eb_id is not null",
        nativeQuery = true
    )
    List<ProductUnitResponse> findEbIdByBillId(Integer id);

    @Query(value = "select pu.id id, pu.eb_id unitEBId from product_unit pu where pu.com_id = ?1 and pu.id in ?2", nativeQuery = true)
    List<GetUnitEBId> getUnitEBIds(Integer comId, List<Integer> unitIds);

    @Query(
        value = "select distinct unit as unitName, com_id as comId from product " +
        "where unit not in ?1 and unit_id is null and com_id in ?2",
        nativeQuery = true
    )
    List<UpdateUnitNameProd> getAllUnitNotExist(List<String> units, List<Integer> comIds);

    @Query(value = "select name from product_unit where name is not null and com_id in ?1", nativeQuery = true)
    List<String> getAllUnitByComId(List<Integer> comIds);

    @Modifying
    @Query(
        value = "update p set unit_id = pu.id " +
        "from product p join product_unit pu on p.unit = pu.name and p.com_id = pu.com_id " +
        "where unit_id is null and unit is not null and p.com_id in ?1",
        nativeQuery = true
    )
    void updateUnitIdNull(List<Integer> comIds);

    @Query(value = "select * from product_unit where com_id = ?1 and UPPER(name) = ?2 and active = 1 and id <> ?3", nativeQuery = true)
    Optional<ProductUnit> findByComIdAndUppercaseName(Integer companyId, String uppercaseName, Integer id);

    List<ProductUnit> findAllByComIdAndIdIn(Integer comId, List<Integer> ids);
}
