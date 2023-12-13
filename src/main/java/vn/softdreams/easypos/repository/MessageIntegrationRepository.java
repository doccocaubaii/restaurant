package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.MessageIntegration;

/**
 * Spring Data JPA repository for the EpMessage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MessageIntegrationRepository extends JpaRepository<MessageIntegration, Integer> {}
