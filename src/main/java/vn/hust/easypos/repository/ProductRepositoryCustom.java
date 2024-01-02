package vn.hust.easypos.repository;

import java.util.List;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hust.easypos.service.dto.product.ProductDetailResponse;
import vn.hust.easypos.service.dto.product.ProductResponse;

public interface ProductRepositoryCustom {
    Page<ProductDetailResponse> getWithPagingForProduct(Pageable pageable, Integer companyId, String keyword);

    ProductDetailResponse findByProductId(Integer companyId, Integer productId);

    List<ProductResponse> searchAllByComIdAndIdAndStatusTrue(Integer companyId, Set<Integer> ids);
}
