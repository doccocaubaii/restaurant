package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.Customer;
import vn.softdreams.easypos.dto.queue.ObjectAsyncResponse;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Customer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>, CustomerRepositoryCustom {
    Optional<Customer> findByIdAndComIdAndActive(Integer id, Integer companyId, Boolean active);

    Optional<Customer> findOneByIdAndComId(Integer id, Integer comId);

    List<Customer> findAllByComIdAndActive(Integer comId, Boolean active);

    Integer countByComIdAndCode2(Integer comId, String code2);

    Integer countAllByIdAndComId(Integer id, Integer comId);

    @Query(value = "select count(*) from customer where id in ?1 and com_id = ?2", nativeQuery = true)
    Integer countAllByIdsAndComId(List<Integer> customerIds, Integer companyId);

    Optional<Customer> findOneByComIdAndNameAndCode2(Integer comId, String name, String code2);

    Integer countAllByComIdAndType(Integer comId, Integer type);

    @Query(value = "select c.code from customer c where c.id = ?1 and c.com_id = ?2 and c.type in ?3", nativeQuery = true)
    String getCodeByIdAndComIdAndType(Integer id, Integer comId, List<Integer> type);

    @Query(
        value = "select id, name, com_id comId, type from customer where com_id in ?1 and active = 1 and code != ?2 ",
        nativeQuery = true
    )
    List<ObjectAsyncResponse> findAllByComIds(List<Integer> ids, String codeDefault);

    @Query(
        value = "select lower(c.code2) from customer c where c.com_id = ?1 and c.active = 1 and c.code2 is not null ",
        nativeQuery = true
    )
    List<String> findCode2ByComId(Integer comId);

    List<Customer> findByComIdAndIdInAndActiveTrue(Integer comId, List<Integer> ids);

    @Query(value = "select * from customer c where c.com_id = ?1 and c.id in ?2 and c.active = 1", nativeQuery = true)
    List<Customer> findAllByComIdAndIds(Integer comId, List<Integer> ids);

    @Query(value = "select c.id from customer c where c.com_id = ?1 and c.id in ?2 and c.active = 1", nativeQuery = true)
    List<Integer> checkAllIds(Integer comId, List<Integer> ids);

    @Query(value = "select c.id from customer c where c.com_id = ?1 and c.active = 1 and c.type in ?2", nativeQuery = true)
    List<Integer> getAllIdByComIdAndTypeIn(Integer comId, List<Integer> types);

    @Query(
        value = "select c.id from customer c where c.com_id = ?1 and c.active = 1 and c.type in ?2 and c.code <> 'KH1' and c.id not in (select cc.customer_id from customer_card cc where cc.com_id = ?1)",
        nativeQuery = true
    )
    List<Integer> getAllCustomerForDefaultCard(Integer comId, List<Integer> types);

    @Query(value = "select c.id from customer c where c.com_id = ?1 and c.active = 1 and c.id not in ?2", nativeQuery = true)
    List<Integer> getAllByComIdAndIdNotIn(Integer comId, List<Integer> ids);

    @Query(nativeQuery = true, value = "select * from customer where code2= :code2 and name= :name and active =1")
    Customer getCustomerByCode2(@Param("code2") String code2, @Param("name") String name);
}
