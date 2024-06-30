package vn.hust.restaurant.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import vn.hust.restaurant.domain.Product;
import vn.hust.restaurant.service.dto.product.ProductImagesResult;

public interface ProductRepository extends JpaRepository<Product, Integer>, ProductRepositoryCustom {
    Optional<Product> findByIdAndComIdAndActive(Integer id, Integer comId, Boolean active);

    Optional<Product> findByIdAndComId(Integer id, Integer comId);

    @Query(
        value = "select p.id, p.image from bill_product bp left join product p on bp.product_id = p.id where bp.bill_id = ?1 and p.image is not null",
        nativeQuery = true
    )
    List<ProductImagesResult> findImagesByBillId(Integer id);
}
