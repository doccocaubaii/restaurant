package vn.softdreams.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.LoyaltyCardUsage;
import vn.softdreams.easypos.dto.customer.CustomerCardItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the LoyaltyCardUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoyaltyCardUsageRepository extends JpaRepository<LoyaltyCardUsage, Integer>, LoyaltyCardUsageRepositoryCustom {
    List<LoyaltyCardUsage> findAllByComId(Integer comId);
    List<LoyaltyCardUsage> findAllByComIdAndCustomerIdIn(Integer comId, List<Integer> ids);
    List<LoyaltyCardUsage> findAllByComIdAndCardIdAndCustomerId(Integer comId, Integer cardId, Integer customerId);
    List<LoyaltyCardUsage> findByComIdAndRefIdAndType(Integer comId, Integer refId, Integer type);

    @Query(
        value = "select lcu.customer_id customerId, sum(lcu.amount) totalAccumulate from loyalty_card_usage lcu " +
        "where lcu.com_id = ?1 and lcu.type = 0 and lcu.customer_id in ?2 and lcu.type = ?3 group by lcu.customer_id",
        nativeQuery = true
    )
    List<CustomerCardItem> getDataResetRankByDeposit(Integer comId, List<Integer> cardId, Integer type);

    @Query(
        value = "select lcu.customer_id customerId, sum(iif(lcu.amount = ?3, lcu.amount, lcu.amount * (-1))) totalAccumulate from loyalty_card_usage lcu " +
        "where lcu.com_id = ?1 and lcu.customer_id in ?2 and lcu.ref_id is not null group by lcu.customer_id",
        nativeQuery = true
    )
    List<CustomerCardItem> getDataResetRankBySpending(Integer comId, List<Integer> customerIds, Integer typePlus);

    @Query(
        value = "select sum(iif(lcu.type = 4, lcu.amount, lcu.amount * (-1))) from loyalty_card_usage lcu " +
        "where lcu.com_id = ?1 and lcu.customer_id = ?2 and lcu.ref_id is not null and lcu.type in ?3 group by lcu.customer_id",
        nativeQuery = true
    )
    BigDecimal getTotalAmountBySpendingAfterCancelBill(Integer comId, Integer customerId, List<Integer> types);

    @Query(
        value = "select sum(lcu.amount) from loyalty_card_usage lcu where lcu.com_id = ?1 and lcu.customer_id = ?2 and lcu.type = ?3 ",
        nativeQuery = true
    )
    BigDecimal getTotalAmountWithCustomerIdAndType(Integer comId, Integer customerId, Integer type);

    Optional<LoyaltyCardUsage> getTopByComIdAndCustomerIdOrderByUpdateTime(Integer comId, Integer customerId);

    @Query(
        value = "select sum(iif(lcu.type = ?3, lcu.point, iif(lcu.type = ?5 or lcu.type = ?4, lcu.point * (-1), 0)))  from loyalty_card_usage lcu " +
        "where lcu.com_id = ?1 and lcu.customer_id = ?2",
        nativeQuery = true
    )
    Integer getPointAccumValueByCustomerId(Integer comId, Integer customerId, Integer congDiem, Integer truDiem, Integer quyDoi);

    List<LoyaltyCardUsage> findOneByComIdAndRefIdAndType(Integer comId, Integer refId, Integer type);
}
