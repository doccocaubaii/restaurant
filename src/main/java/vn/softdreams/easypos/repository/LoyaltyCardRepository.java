package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.LoyaltyCard;
import vn.softdreams.easypos.dto.card.CardDefaultItem;
import vn.softdreams.easypos.dto.card.CardItemResult;
import vn.softdreams.easypos.dto.card.CardRankItem;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the LoyaltyCard entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LoyaltyCardRepository extends JpaRepository<LoyaltyCard, Integer>, LoyaltyCardCustom {
    Optional<LoyaltyCard> findOneByComIdAndNameIgnoreCaseAndStatusIn(Integer comId, String name, List<Integer> status);
    Optional<LoyaltyCard> findByComIdAndIsDefaultAndStatusIn(Integer comId, Boolean isDefault, List<Integer> status);
    Optional<LoyaltyCard> findByComIdAndIsDefault(Integer comId, Boolean isDefault);
    Integer countAllByComIdAndStatusIn(Integer comId, List<Integer> status);

    @Query(value = "select count(*) from loyalty_card lc where com_id = ?1 and name = ?2 and status in ?3 and id <> ?4", nativeQuery = true)
    Integer checkDuplicateName(Integer comId, String name, List<Integer> status, Integer id);

    @Query(
        value = "select count(*) from loyalty_card lc where com_id = ?1 and is_default = 1 and status in ?2 and id <> ?3",
        nativeQuery = true
    )
    Integer checkDuplicateIsDefault(Integer comId, List<Integer> status, Integer id);

    Optional<LoyaltyCard> findByIdAndComId(Integer id, Integer comId);
    List<LoyaltyCard> findByComIdAndRankIsGreaterThanEqualAndIdIsNot(Integer comId, Integer rank, Integer id);
    List<LoyaltyCard> findByComIdAndRankIsLessThan(Integer comId, Integer rank);
    List<LoyaltyCard> findAllByComIdAndStatusIn(Integer comId, List<Integer> status);
    List<LoyaltyCard> findAllByComIdAndIdInAndStatusIn(Integer comId, List<Integer> ids, List<Integer> status);

    @Query(
        value = "with t as ( " +
        "    select rank from loyalty_card lc join customer_card cc on lc.id = cc.card_id " +
        "    where lc.com_id = ?1 and card_id = ?2) " +
        "select top(1) id from loyalty_card lc, t where com_id = ?1 and lc.rank > t.rank and lc.status = 1 order by lc.rank",
        nativeQuery = true
    )
    Integer getNextRank(Integer comId, Integer cardId);

    @Query(value = "select * from loyalty_card c where c.com_id = ?1 and c.status = 1 order by c.rank", nativeQuery = true)
    List<LoyaltyCard> getAllByComId(Integer comId);

    @Query(value = "select c.id from loyalty_card c where c.com_id = ?1 and c.status = 1 order by c.rank", nativeQuery = true)
    List<Integer> getAllIdsByComId(Integer comId);

    @Query(value = "select c.id from loyalty_card c where c.com_id = ?1 and c.is_default = 1 and c.status = 1", nativeQuery = true)
    Integer getCardIdDefault(Integer comId);

    @Query(
        value = "select c.id id, c.name name from loyalty_card c where c.com_id = ?1 and c.is_default = 1 and c.status = 1",
        nativeQuery = true
    )
    Optional<CardDefaultItem> getCardDefault(Integer comId);

    List<LoyaltyCard> findAllByComIdAndIdIn(Integer comId, List<Integer> ids);

    @Query(value = "select lc.id from loyalty_card lc where lc.com_id = ?1 and lc.id in ?2 and lc.status = 1", nativeQuery = true)
    List<Integer> checkAllIds(Integer comId, List<Integer> ids);

    @Query(
        value = "select lc.id cardId, lc.name cardName, count(distinct cc.customer_id) totalCustomer " +
        "from loyalty_card lc left join customer_card cc on lc.id = cc.card_id " +
        "left join customer c on cc.customer_id = c.id and c.active = 1 where lc.com_id = ?1 and lc.status != -1 and lc.normalized_name like %?2% " +
        "group by lc.id, lc.name ",
        countQuery = "select count(*) from loyalty_card where com_id = ?1 and status != -1",
        nativeQuery = true
    )
    Page<CardItemResult> findAllByComId(Integer comId, String keyword, Pageable pageable);

    List<CardRankItem> findAllByIdInOrderByRank(List<Integer> cardIds);

    @Query(value = "select lc.id from loyalty_card lc where lc.com_id = ?1 and lc.id not in ?2 and lc.status = 1", nativeQuery = true)
    List<Integer> getAllByComIdAndIdNotIn(Integer comId, List<Integer> ids);

    Optional<LoyaltyCard> findByComIdAndRankAndStatusIn(Integer comId, Integer rank, List<Integer> status);

    @Query(value = "select * from loyalty_card c where c.com_id = ?1 and c.status = 1 order by c.rank", nativeQuery = true)
    List<LoyaltyCard> getAllByComIdOrderByRank(Integer comId);

    @Query(
        value = "with t as ( " +
        "            select rank from loyalty_card lc join customer_card cc on lc.id = cc.card_id" +
        "            where lc.com_id = ?1 and card_id = ?2)" +
        "            select top(1) id from loyalty_card lc, t where com_id = ?1 and lc.rank < t.rank and lc.status = 1 order by lc.rank desc",
        nativeQuery = true
    )
    Integer getPreviousRank(Integer comId, Integer cardId);
}
