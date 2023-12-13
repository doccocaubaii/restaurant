package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.CustomerCard;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the CustomerCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerCardRepository extends JpaRepository<CustomerCard, Integer>, CustomerCardRepositoryCustom {
    List<CustomerCard> getAllByComId(Integer comId);
    List<CustomerCard> getAllByComIdAndCardId(Integer comId, Integer cardId);
    List<CustomerCard> getAllByComIdAndCustomerIdIn(Integer comId, List<Integer> ids);
    Optional<CustomerCard> findByCustomerIdAndComId(Integer cusId, Integer comId);

    @Query(value = "select count(*) from customer_card cc where cc.com_id = ?1 and cc.code = ?2", nativeQuery = true)
    Integer checkCodeDuplicate(Integer comId, Integer code);

    @Query(
        value = "select cc.* from customer_card cc join loyalty_card lc on lc.id = cc.card_id where cc.customer_id = ?1 and cc.card_id = ?2 and lc.status = 1",
        nativeQuery = true
    )
    Optional<CustomerCard> getCustomerCardByCustomerIdAndCardId(Integer customerId, Integer cardId);

    @Query(
        value = "select cc.* from customer_card cc join loyalty_card lc on lc.id = cc.card_id where cc.customer_id = ?1 and lc.status = 1",
        nativeQuery = true
    )
    Optional<CustomerCard> getCustomerCardByCustomerId(Integer customerId);

    @Query(value = "select cc.* from customer_card cc where cc.customer_id = ?1 ", nativeQuery = true)
    Optional<CustomerCard> findOneByCustomerId(Integer customerId);

    @Query(
        value = "select cc.* from customer_card cc join loyalty_card lc on lc.id = cc.card_id where cc.com_id = ?1 and cc.customer_id in ?2 and lc.status = 1",
        nativeQuery = true
    )
    List<CustomerCard> getAllByCustomerIds(Integer comId, List<Integer> customerIds);

    @Query(
        value = "select cc.* from customer_card cc left join customer c on c.id = cc.customer_id " +
        "where cc.com_id = ?1 and cc.card_id in ?2 and c.active = 1;",
        nativeQuery = true
    )
    List<CustomerCard> getAllCustomerIdsByCardIds(Integer comId, List<Integer> cardIds);

    @Query(
        value = "select cc.* from customer_card cc join loyalty_card lc on lc.id = cc.card_id where cc.com_id = ?1 and cc.card_id in ?2 and lc.status = 1",
        nativeQuery = true
    )
    List<CustomerCard> getAllByCardIds(Integer comId, List<Integer> cardIds);
}
