package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.softdreams.easypos.domain.CardPolicy;
import vn.softdreams.easypos.dto.card.CardPolicyItems;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the CardPolicy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CardPolicyRepository extends JpaRepository<CardPolicy, Integer> {
    Optional<CardPolicy> findOneByIdAndComIdAndStatus(Integer id, Integer comId, Integer status);
    Optional<CardPolicy> findOneByComIdAndUpgradeTypeAndStatusIn(Integer comId, Integer upgradeType, List<Integer> status);
    List<CardPolicyItems> findAllByComIdAndStatusIn(Integer comId, List<Integer> status, Pageable pageable);
    Optional<CardPolicy> findByComIdAndUpgradeTypeAndStatus(Integer comId, Integer upgradeType, Integer status);
    List<CardPolicy> findByComIdAndStatus(Integer comId, Integer status);
    Optional<CardPolicyItems> findOneByComIdAndStatusIn(Integer comId, List<Integer> status);
    Optional<CardPolicy> getOneByComIdAndStatusIn(Integer comId, List<Integer> status);
}
