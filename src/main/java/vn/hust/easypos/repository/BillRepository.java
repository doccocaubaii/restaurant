package vn.hust.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import vn.hust.easypos.domain.Bill;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer>, BillRepositoryCustom {
    Optional<Bill> findByIdAndComId(Integer id, Integer companyId);

    Optional<Bill> findByTableIdAndComIdAndStatus(Integer tableId, Integer comId, Integer status);

    @Modifying
    @Query(value = "update bill set status = ?2 where id = ?1 ", nativeQuery = true)
    void updateStatusByBillId(Integer billId, Integer status);
}
