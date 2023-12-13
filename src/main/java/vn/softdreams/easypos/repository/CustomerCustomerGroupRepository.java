package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.CustomerCustomerGroup;

/**
 * Spring Data JPA repository for the CustomerCustomerGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerCustomerGroupRepository extends JpaRepository<CustomerCustomerGroup, Integer> {}
