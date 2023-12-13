package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.OwnerDevice;

import java.util.Optional;

/**
 * Spring Data JPA repository for the OwnerDevice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface OwnerDeviceRepository extends JpaRepository<OwnerDevice, Integer>, OwnerDeviceRepositoryCustom {
    @Query(
        value = "select od.device_code from owner_device od join company_owner co on od.owner_id = co.id where co.tax_code = ?1 and od.name = ?2",
        nativeQuery = true
    )
    Optional<String> getDeviceCodeByTaxCodeAndName(String taxCode, String name);

    @Query(value = "select od.device_code from owner_device od  where od.owner_id = ?1 and od.name = ?2", nativeQuery = true)
    Optional<String> getDeviceCodeByOwnerIDAndCode(Integer ownerId, String name);

    @Query(
        value = "select count(*)  " +
        "from owner_device od  " +
        "          join company_owner co on od.owner_id = co.id  " +
        "          join company c on c.com_owner_id = co.id  " +
        "where c.id = ?1 ",
        nativeQuery = true
    )
    Integer countAllByCompanyId(Integer comId);
}
