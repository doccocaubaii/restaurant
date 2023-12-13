package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.config.ConfigOwnerResult;
import vn.softdreams.easypos.dto.config.ConfigResult;
import vn.softdreams.easypos.dto.config.ConfigStatusResult;

import java.util.List;

public interface ConfigRepositoryCustom {
    Page<ConfigResult> getWithPaging(Integer comId, Pageable pageable, String keyword, String fromDate, String toDate);

    List<ConfigOwnerResult> getConfigsByCompanyOwnerOrCompany(Integer ownerId, List<String> codes, Integer companyId);

    List<ConfigStatusResult> getConfigsStatusByOwner(List<Integer> ownerIds, List<String> codes);
    List<ConfigStatusResult> getConfigsStatusByCompanies(List<Integer> companies, List<String> codes);
}
