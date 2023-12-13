package vn.softdreams.easypos.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;

public interface TaskLogRepositoryCustom {
    Page<TaskLogItemResponse> filter(
        Pageable pageable,
        Integer status,
        String taxCode,
        Integer companyOwnerName,
        String fromDate,
        String toDate,
        String type
    );
}
