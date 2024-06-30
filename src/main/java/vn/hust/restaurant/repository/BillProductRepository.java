package vn.hust.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hust.restaurant.domain.BillProduct;

public interface BillProductRepository extends JpaRepository<BillProduct, Integer> {}
