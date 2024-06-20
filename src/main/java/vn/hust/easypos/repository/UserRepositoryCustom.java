package vn.hust.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.hust.easypos.service.dto.StaffResponse;

public interface UserRepositoryCustom {
    Page<StaffResponse> searchStaffs(Pageable pageable, String keyword, Integer companyId);
}
