package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.toppingGroup.ToppingGroupResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItemOffline;

import java.util.List;

public interface ToppingGroupCustomRepository {
    Page<ToppingGroupResponse> getALlWithPaging(Pageable pageable, Integer comId, String keyword, Boolean isCountAll);

    List<ToppingItem> getForProductDetail(Integer productId);
    List<ToppingItemOffline> getForProductOffline(List<Integer> productIds);
}
