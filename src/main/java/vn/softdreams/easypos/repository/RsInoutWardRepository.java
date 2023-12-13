package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.RsInoutWard;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the RsInoutWard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RsInoutWardRepository extends JpaRepository<RsInoutWard, Integer>, RsInoutWardRepositoryCustom {
    Optional<RsInoutWard> findOneByBillId(Integer billId);
    Optional<RsInoutWard> findOneByBillIdAndComId(Integer billId, Integer comId);
    Optional<RsInoutWard> findByIdAndComId(Integer id, Integer comId);
    Optional<RsInoutWard> findByNoAndComId(String no, Integer comId);

    @Query(value = "select rs.* from rs_inoutward rs where rs.eb_id = ?1 and rs.com_id = ?2", nativeQuery = true)
    Optional<RsInoutWard> findByEbIdAndComId(Integer ebId, Integer comId);

    @Query(value = "select top(1) * from rs_inoutward where com_id = ?1 order by update_time desc", nativeQuery = true)
    Optional<RsInoutWard> findOneByComId(Integer comId);

    @Query(
        value = "select count(*) " +
        "from rs_inoutward ri join rs_inoutward_detail rid on ri.id = rid.rs_inoutward_id " +
        "where ri.com_id = ?1 and ri.type = 1 and rid.product_id = ?2 and rid.unit_name = ?3 and ((ri.type_desc like N'%Nhập%' and ri.type = 1) or (ri.type_desc like N'%Xuất%' and ri.type = 2))",
        nativeQuery = true
    )
    Integer countAllByComIdAndProductIdAndUnitName(Integer comId, Integer productId, String unitName);

    @Query(
        value = "select * from rs_inoutward ri " +
        "where ri.id not in (select rs_inoutward_id from mc_payment where rs_inoutward_id is not null) and ri.type = 1 and ri.no like '%NK%' and ri.type_desc like N'%Nhập%'",
        nativeQuery = true
    )
    List<RsInoutWard> getAllMissing();
}
