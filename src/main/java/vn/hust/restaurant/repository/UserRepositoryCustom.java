package vn.hust.restaurant.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hust.restaurant.service.dto.StaffResponse;

public interface UserRepositoryCustom {
    Page<StaffResponse> searchStaffs(Pageable pageable, String keyword, Integer companyId);
}
