package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.card.CardHistoryResult;

import java.util.List;

public interface LoyaltyCardUsageRepositoryCustom {
    Page<CardHistoryResult> getAllHistory(
        Integer comId,
        Integer customerId,
        List<Integer> type,
        Pageable pageable,
        String fromDate,
        String toDate
    );
}
