package vn.hust.easypos.repository;

import java.util.List;
import vn.hust.easypos.service.dto.company.CompanyResult;

public interface CompanyRepositoryCustom {
    //    CompanyResult getCompanyById(Integer companyId);

    List<CompanyResult> findAllCompanyCustomByUserID(Integer id);
    //    Page<CompanyResult> getAllWithPaging(
    //        Pageable pageable,
    //        List<String> ownerIds,
    //        Integer ownerId,
    //        String keyword,
    //        String fromDate,
    //        String toDate
    //    );
}
