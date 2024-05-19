package vn.hust.easypos.repository;

import vn.hust.easypos.service.dto.company.CompanyResult;

import java.util.List;

public interface CompanyRepositoryCustom {
    //    CompanyResult getCompanyById(Integer companyId);

    List<CompanyResult> findAllCompanyByCompanyID(Integer id);
    //    Page<CompanyResult> getAllWithPaging(
    //        Pageable pageable,
    //        List<String> ownerIds,
    //        Integer ownerId,
    //        String keyword,
    //        String fromDate,
    //        String toDate
    //    );
}
