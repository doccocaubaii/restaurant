package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.product.ProductUnitResponse;

import java.util.List;

public interface ProductUnitRepositoryCustom {
    List<ProductUnitResponse> findByComId(Integer comId, String keyword);
    Page<ProductUnitResponse> searchProductUnit(Pageable pageable, Integer comId, String keyword, boolean paramCheckAll, List<Integer> ids);
}
