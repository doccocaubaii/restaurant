package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.EpMessage;

import java.util.List;

/**
 * Spring Data JPA repository for the EpMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EpMessageRepository extends JpaRepository<EpMessage, Integer> {
    List<EpMessage> findAllByReceive(String receiver);
}
