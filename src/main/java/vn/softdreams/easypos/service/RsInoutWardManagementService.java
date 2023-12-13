package vn.softdreams.easypos.service;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardCreateRequest;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardDeleteRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

public interface RsInoutWardManagementService {
    ResultDTO getAllTransactions(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean getWithPaging,
        Pageable pageable
    );

    ResultDTO getOneById(Integer id);

    ResultDTO create(RsInOutWardCreateRequest request) throws Exception;

    ResultDTO deleteByIdAndCode(RsInOutWardDeleteRequest request);
}
