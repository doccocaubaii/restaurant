package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.TaskLog;
import vn.softdreams.easypos.dto.company.CompanyResultItem;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncCheckInvoiceRequest;
import vn.softdreams.easypos.dto.queue.TaskLogAsyncRequest;
import vn.softdreams.easypos.dto.taskLog.TaskLogItemResponse;
import vn.softdreams.easypos.dto.taskLog.TaskLogResultItem;
import vn.softdreams.easypos.dto.taskLog.TaskLogSendQueueRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link TaskLog}.
 */
public interface TaskLogService {
    TaskLog save(TaskLog taskLog);

    TaskLog update(TaskLog taskLog);

    Optional<TaskLog> partialUpdate(TaskLog taskLog);

    Page<TaskLog> findAll(Pageable pageable);

    void delete(Integer id);

    ResultDTO findTaskLogById(Integer id);

    Page<TaskLogItemResponse> filter(
        Pageable pageable,
        Integer status,
        String taxAuthorityCode,
        Integer companyOwnerName,
        String fromDate,
        String toDate,
        String type
    );

    List<CompanyResultItem> initDataCompanyCombobox(String input);
    List<TaskLogResultItem> initDataStatusCombobox(String input);
    List<TaskLogResultItem> initDataTypeCombobox(String input);
    ResultDTO sendQueueAgain(TaskLogSendQueueRequest request);

    ResultDTO unitCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO productCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO productUpdateAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO createBillAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO invoiceErrorAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO customerCreateAsync(TaskLogAsyncRequest taskLogAsyncRequest);

    ResultDTO checkInvoiceErrorAsync(TaskLogAsyncCheckInvoiceRequest taskLogAsyncRequest);
}
