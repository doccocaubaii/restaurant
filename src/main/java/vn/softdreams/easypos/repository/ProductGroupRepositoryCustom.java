package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.productGroup.ProductGroupOfflineResponse;
import vn.softdreams.easypos.dto.productGroup.ProductGroupOnlineResponse;
import vn.softdreams.easypos.dto.productGroup.ProductGroupResult;

import java.util.List;

public interface ProductGroupRepositoryCustom {
    Page<ProductGroupOnlineResponse> searchProductGroupByKeywordForPage(
        Pageable pageable,
        String keyword,
        Integer companyId,
        Boolean isLoadAll,
        boolean paramCheckAll,
        List<Integer> ids
    );

    List<ProductGroupResult> getAllForOffline(Integer comId, List<Integer> productIds);

    List<ProductGroupOfflineResponse> getAllProductGroupsForOffline(Integer companyId);
}
