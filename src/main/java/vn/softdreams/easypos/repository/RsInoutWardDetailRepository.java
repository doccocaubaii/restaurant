package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.RsInoutWardDetail;

@Repository
public interface RsInoutWardDetailRepository extends JpaRepository<RsInoutWardDetail, Integer> {}
