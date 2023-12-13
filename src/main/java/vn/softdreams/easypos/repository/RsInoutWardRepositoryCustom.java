package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.rsInoutWardDetail.GetOneByIdDetailResponse;
import vn.softdreams.easypos.dto.rsinoutward.GetOneByIdResponse;
import vn.softdreams.easypos.dto.rsinoutward.RsInOutWardStatusResult;

import java.util.List;

public interface RsInoutWardRepositoryCustom {
    Object getAllTransactions(
        Integer comId,
        String fromDate,
        String toDate,
        Integer type,
        String keyword,
        Boolean getWithPaging,
        Pageable pageable
    );

    RsInOutWardStatusResult getTotalAmountStatus(Integer comId, String fromDate, String toDate, Integer type, String keyword);

    GetOneByIdResponse getOneById(Integer id, Integer companyId);

    List<GetOneByIdDetailResponse> getDetailByRsInoutWardId(Integer id);
}
