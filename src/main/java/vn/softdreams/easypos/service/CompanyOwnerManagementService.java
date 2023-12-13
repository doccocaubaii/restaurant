package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.companyOwner.CompanyOwnerRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface CompanyOwnerManagementService {
    ResultDTO getAllWithPaging(Pageable pageable, String keyword);

    ResultDTO getAllForAdmin(Pageable pageable, String keyword, String fromDate, String toDate);

    ResultDTO save(CompanyOwnerRequest company);

    ResultDTO getDetailById(Integer id);
    ResultDTO getById(Integer id);

    ResultDTO getByUserId(Integer id);
    ResultDTO getOwnerItems(Pageable pageable, String keyword);
}
