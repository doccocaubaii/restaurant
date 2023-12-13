package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.domain.CompanyOwner;
import vn.softdreams.easypos.dto.company.CompanyCreateRequest;
import vn.softdreams.easypos.dto.company.CompanyUpdateRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface CompanyManagementService {
    Company save(Company company);

    Company update(Company company);

    CompanyOwner saveOwner(CompanyOwner company);

    Page<CompanyOwner> findAll(Pageable pageable);

    ResultDTO getAllWithPaging(Pageable pageable, Integer ownerId, String keyword, String fromDate, String toDate);

    ResultDTO update(CompanyUpdateRequest company);

    ResultDTO create(CompanyCreateRequest company);

    ResultDTO getById(Integer id);

    ResultDTO getAllByOwnerId(Integer ownerId);

    ResultDTO getAllItems(String keyword);
}
