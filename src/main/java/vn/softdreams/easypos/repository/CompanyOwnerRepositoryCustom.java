package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.companyOwner.OwnerResult;

public interface CompanyOwnerRepositoryCustom {
    Page<OwnerResult> getAllForAdmin(Pageable pageable, String keyword, String fromDate, String toDate);
}
