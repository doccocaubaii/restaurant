package vn.softdreams.easypos.repository;

import vn.softdreams.easypos.dto.productUnit.ProductProductUnitResponse;

import java.util.List;

public interface ProductProductUnitCustom {
    List<ProductProductUnitResponse> getAllForOffline(Integer comId);
    List<ProductProductUnitResponse> getByProductId(Integer comId, Integer productId);
}
