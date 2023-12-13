package vn.softdreams.easypos.service;

import vn.softdreams.easypos.service.dto.ResultDTO;

public interface StatisticManagementService {
    ResultDTO getBillStats(Integer comId, String fromDate, String toDate);
    ResultDTO getInvoiceStats(Integer comId, String fromDate, String toDate);
}
