package vn.hust.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hust.easypos.domain.Bill;

import java.util.Optional;

public interface BillRepository extends JpaRepository<Bill, Integer>, BillRepositoryCustom {
    Optional<Bill> findByIdAndComId(Integer id, Integer companyId);

    Optional<Bill> findByTableIdAndComIdAndStatus(Integer tableId, Integer comId, Integer status);
}
