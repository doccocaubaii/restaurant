package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.company.CompanyResult;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.companyUser.CompanyOwnerResponse;

import java.util.List;

public interface CompanyRepositoryCustom {
    CompanyResult getCompanyById(Integer companyId);

    List<CompanyResult> findAllCompanyCustomByUserID(Integer id);

    CompanyOwnerResponse findCompanyOwnerByCompanyId(Integer comId);

    List<CompanyResultItem> findAllCompany(String input);

    Page<CompanyResult> getAllWithPaging(
        Pageable pageable,
        List<String> ownerIds,
        Integer ownerId,
        String keyword,
        String fromDate,
        String toDate
    );
}
