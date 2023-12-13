package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.CompanyUser;

import java.util.Optional;

public interface CompanyUserManagementService {
    CompanyUser save(CompanyUser companyUser);

    CompanyUser update(CompanyUser companyUser);

    Optional<CompanyUser> partialUpdate(CompanyUser companyUser);

    Page<CompanyUser> findAll(Pageable pageable);

    Optional<CompanyUser> findOne(Integer id);

    void delete(Integer id);
}
