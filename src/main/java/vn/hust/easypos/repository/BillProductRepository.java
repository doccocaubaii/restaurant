package vn.hust.easypos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.hust.easypos.domain.BillProduct;

public interface BillProductRepository extends JpaRepository<BillProduct, Integer> {}
