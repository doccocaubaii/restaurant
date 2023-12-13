package vn.softdreams.easypos.service;

import net.sf.jasperreports.engine.JRException;
import vn.softdreams.easypos.dto.RequestReport;
import vn.softdreams.easypos.dto.ResDynamicReportDTO;
import vn.softdreams.easypos.dto.product.HotSaleProductRequest;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.dto.report.ReportRevenueCommonReq;

public interface ReportFileDataService {
    byte[] getReportExcel(SaleProductStatsRequest requestReport) throws Exception;
    byte[] getExcelReportHotSalesStats(HotSaleProductRequest request) throws Exception;
    ResDynamicReportDTO getDataPdfReport(RequestReport requestReport) throws JRException;
    ResDynamicReportDTO getDataPdfReportHotSalesStats(RequestReport requestReport) throws JRException;
    ResDynamicReportDTO getDataPdfRevenueCommon(ReportRevenueCommonReq requestReport) throws JRException;
    byte[] getDataExcelRevenueCommon(ReportRevenueCommonReq requestReport) throws Exception;
}
