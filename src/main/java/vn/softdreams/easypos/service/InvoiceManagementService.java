package vn.softdreams.easypos.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.dto.invoice.InvoiceRequest;
import vn.softdreams.easypos.dto.invoice.PublishInvoiceRequest;
import vn.softdreams.easypos.dto.invoice.SendIssuanceNoticeRequest;
import vn.softdreams.easypos.service.dto.PublishListRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.List;

public interface InvoiceManagementService {
    ResultDTO issueInvoices(List<PublishInvoiceRequest> invoiceStatusDTO);

    ResultDTO getInvoicePatterns(Integer comId) throws Exception;

    ResultDTO getOwnerInfo(Integer comId);

    ResultDTO getInvoicePdf(int invoiceId, String ikey) throws Exception;

    ResultDTO sendIssuanceNotice(SendIssuanceNoticeRequest request) throws Exception;
    ResultDTO getAllInvoices(
        Integer taxCheckStatus,
        String fromDate,
        String toDate,
        String pattern,
        String customerName,
        String no,
        Pageable pageable,
        boolean isCountAll
    );

    ResultDTO getInvoiceById(Integer id);
    ResultDTO updateInvoice(Integer id, InvoiceRequest invoiceRequest);

    ResultDTO deleteInvoiceNotPublish(Integer invoiceId) throws JsonProcessingException;

    ResultDTO publishListInvoice(PublishListRequest publishListRequest);
}
