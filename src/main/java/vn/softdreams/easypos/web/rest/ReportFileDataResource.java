package vn.softdreams.easypos.web.rest;

import io.micrometer.core.annotation.Timed;
import net.sf.jasperreports.engine.JRException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.dto.RequestReport;
import vn.softdreams.easypos.dto.ResDynamicReportDTO;
import vn.softdreams.easypos.dto.product.HotSaleProductRequest;
import vn.softdreams.easypos.dto.product.SaleProductStatsRequest;
import vn.softdreams.easypos.dto.report.ReportRevenueCommonReq;
import vn.softdreams.easypos.service.ReportFileDataService;

@RestController
@RequestMapping("/api")
public class ReportFileDataResource {

    private final Logger log = LoggerFactory.getLogger(ReportFileDataService.class);
    private final ReportFileDataService reportFileService;

    public ReportFileDataResource(ReportFileDataService reportFileService) {
        this.reportFileService = reportFileService;
    }

    @PostMapping("/client/page/report-excel/product-hot-sales-stats")
    @Timed
    public ResponseEntity<byte[]> getExcelReportHotSalesStats(@RequestBody HotSaleProductRequest request) throws Exception {
        byte[] resDynamicReportDTO = reportFileService.getExcelReportHotSalesStats(request);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "hot-product.extension");
        return new ResponseEntity<>(resDynamicReportDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/client/page/report-pdf/product-hot-sales-stats")
    @Timed
    public ResponseEntity<byte[]> getPdfReportHotSalesStats(@RequestBody RequestReport requestReport) {
        HttpHeaders headers = new HttpHeaders();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        try {
            resDynamicReportDTO = reportFileService.getDataPdfReportHotSalesStats(requestReport);
            headers.setContentDispositionFormData("name", "name");
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");

            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            return new ResponseEntity<>(resDynamicReportDTO.getReportPDF(), headers, HttpStatus.OK);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/client/page/report/excel")
    @Timed
    public ResponseEntity<byte[]> getExcelReport(@RequestBody SaleProductStatsRequest requestReport) throws Exception {
        byte[] resDynamicReportDTO = reportFileService.getReportExcel(requestReport);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "filename.extension");
        return new ResponseEntity<>(resDynamicReportDTO, headers, HttpStatus.OK);
    }

    @PostMapping("/client/page/report/pdf")
    @Timed
    public ResponseEntity<Object> getPdfReport(@RequestBody RequestReport requestReport) {
        HttpHeaders headers = new HttpHeaders();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        try {
            resDynamicReportDTO = reportFileService.getDataPdfReport(requestReport);
            headers.setContentDispositionFormData("name", "name");
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");

            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            return new ResponseEntity<>(resDynamicReportDTO.getReportPDF(), headers, HttpStatus.OK);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/client/page/report-revenue-common/pdf")
    @Timed
    public ResponseEntity<Object> getPdfReportRevenueCommon(@RequestBody ReportRevenueCommonReq requestReport) {
        HttpHeaders headers = new HttpHeaders();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        try {
            resDynamicReportDTO = reportFileService.getDataPdfRevenueCommon(requestReport);
            headers.setContentDispositionFormData("name", "name");
            headers.setCacheControl(CacheControl.noCache());
            headers.setPragma("no-cache");

            headers.setContentType(MediaType.parseMediaType("application/pdf"));
            return new ResponseEntity<>(resDynamicReportDTO.getReportPDF(), headers, HttpStatus.OK);
        } catch (JRException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("/client/page/report-revenue-common/excel")
    @Timed
    public ResponseEntity<byte[]> getExcelReportRevenueCommon(@RequestBody ReportRevenueCommonReq requestReport) throws Exception {
        byte[] resDynamicReportDTO = reportFileService.getDataExcelRevenueCommon(requestReport);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        // Set the content disposition header to specify the file name
        headers.setContentDispositionFormData("attachment", "filename.extension");
        return new ResponseEntity<>(resDynamicReportDTO, headers, HttpStatus.OK);
    }
}
