package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.CompanyUser;

public interface CompanyUserRepositoryCustom {
    Page<CompanyUser> getAllCompanyUsers(Pageable pageable);
}
