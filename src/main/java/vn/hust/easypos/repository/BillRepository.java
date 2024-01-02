package vn.hust.easypos.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.hust.easypos.domain.Bill;

public interface BillRepository extends JpaRepository<Bill, Integer>, BillRepositoryCustom {
    Optional<Bill> findByIdAndComId(Integer id, Integer companyId);
}
