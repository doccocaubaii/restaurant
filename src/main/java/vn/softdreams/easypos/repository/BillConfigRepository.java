package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.BillConfig;

@Repository
public interface BillConfigRepository extends JpaRepository<BillConfig, Integer> {}
