package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.toppingGroup.ProductToppingItemResponse;
import vn.softdreams.easypos.dto.toppingGroup.ToppingItem;

import java.util.List;
import java.util.Map;

public interface ProductToppingRepositoryCustom {
    List<ToppingItem> getListToppingGroupForBill(Map<Integer, List<Integer>> parentIdMap);
    List<ProductToppingItemResponse> findToppingGroupForBill(Integer productId);
    List<ProductToppingItemResponse> findToppingGroupForOffline(Integer comId);
}
