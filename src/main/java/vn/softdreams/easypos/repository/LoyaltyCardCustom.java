package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.loyaltyCard.LoyaltyCardResultItem;

import java.util.List;

public interface LoyaltyCardCustom {
    Page<LoyaltyCardResultItem> getAllCustomerCard(
        Pageable pageable,
        Integer comId,
        String keyword,
        Integer id,
        Boolean isCountAll,
        Boolean paramCheckAll,
        List<Integer> ids
    );
}
