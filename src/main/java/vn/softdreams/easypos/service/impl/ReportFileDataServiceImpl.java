package vn.softdreams.easypos.service.impl;

import net.sf.jasperreports.engine.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.ReportConstants;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.dto.RequestReport;
import vn.softdreams.easypos.dto.ResDynamicReportDTO;
import vn.softdreams.easypos.dto.product.*;
import vn.softdreams.easypos.dto.report.ReportRevenueCommonReq;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.service.ReportFileDataService;
import vn.softdreams.easypos.service.ReportManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.FieldConfig;
import vn.softdreams.easypos.service.dto.HeaderConfig;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.service.dto.RevenueCommonStatsResponse;
import vn.softdreams.easypos.util.Common;
import vn.softdreams.easypos.util.FastExcelUtils;
import vn.softdreams.easypos.util.Util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

import static vn.softdreams.easypos.util.FastExcelUtils.SetHeaderSalesProductStatsExcel;
import static vn.softdreams.easypos.util.FastExcelUtils.getCellStyleList;

@Service
@Transactional
public class ReportFileDataServiceImpl implements ReportFileDataService {

    private static final Logger log = LoggerFactory.getLogger(ReportFileDataServiceImpl.class);
    private final ReportManagementService reportManagementService;
    private final UserRepository userRepository;
    private final UserService userService;

    public ReportFileDataServiceImpl(
        ReportManagementService reportManagementService,
        UserRepository userRepository,
        UserService userService
    ) {
        this.reportManagementService = reportManagementService;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Override
    public byte[] getReportExcel(SaleProductStatsRequest requestReport) throws Exception {
        byte[] result = null;
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        result = getExcelSalesProductStats(requestReport);
        resDynamicReportDTO.setReportPDF(result);
        return result;
    }

    byte[] getExcelSalesProductStats(SaleProductStatsRequest requestReport) throws Exception {
        User user = userService.getUserWithAuthorities();
        LocalDate fromDate = LocalDate.parse(requestReport.getFromDate());
        LocalDate toDate = LocalDate.parse(requestReport.getToDate());
        ResultDTO productSales = reportManagementService.saleProductStats(requestReport);
        SaleProductStatsResponse res = (SaleProductStatsResponse) productSales.getData();
        List<SaleProductStatsResult> detail = res.getDetail();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook();
            // fill du lieu sheet 0
            SXSSFSheet sheet = workbook.createSheet("Thổng kê hàng hóa");
            int start = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleCustom = workbook.createCellStyle();
            styleCustom.setFont(fontDf);
            styleCustom.setBorderRight(BorderStyle.THIN);
            styleCustom.setWrapText(true);
            styleCustom.setFont(fontDf);
            styleCustom.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleCustom.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderSalesProductStatsExcel(
                user.getCompanyId(),
                workbook,
                user,
                sheet,
                0,
                null,
                "Thong_Ke_Hang_Hoa_Ban_Ra",
                Util.getPeriod(fromDate, toDate),
                getCellStyleList(workbook),
                "Thống Kê Lợi Nhuận Sản Phẩm",
                7
            );
            if (detail.size() == 0) {
                Row row = sheet.createRow(9);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 20);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            int countRow = 0;
            List<HeaderConfig> headerConfigs = ReportConstants.THONG_KE_HANG_HOA_BAN_RA.HeaderConfig;
            List<FieldConfig> configList = ReportConstants.THONG_KE_HANG_HOA_BAN_RA.FieldConfig;
            FastExcelUtils.genDataReportExcelProfit(sheet, configList, headerConfigs, detail, 10, getCellStyleList(workbook));
            workbook.write(bos);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    @Override
    public byte[] getExcelReportHotSalesStats(HotSaleProductRequest request) throws Exception {
        byte[] result = null;
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        result = fileExcelReportHotSalesStats(request);
        resDynamicReportDTO.setReportPDF(result);
        return result;
    }

    byte[] fileExcelReportHotSalesStats(HotSaleProductRequest request) throws Exception {
        User user = userService.getUserWithAuthorities();
        LocalDate fromDate = LocalDate.parse(request.getFromDate());
        LocalDate toDate = LocalDate.parse(request.getToDate());
        ResultDTO hotProductSales = reportManagementService.hotSaleProductStats(
            request.getComId(),
            request.getFromDate(),
            request.getToDate(),
            request.getType()
        );
        String type;
        if (request.getType() == 1) {
            type = "theo số lượng giảm dần";
        } else {
            type = "theo doanh thu giảm dần";
        }
        HotSaleProductResponse res = (HotSaleProductResponse) hotProductSales.getData();
        List<HotSaleProductResult> detail = res.getDetail();
        Integer stt = 0;
        for (HotSaleProductResult hotSaleProductResult : detail) {
            stt++;
            hotSaleProductResult.setStt(stt);
            hotSaleProductResult.setTotalAmountStr(currencyFormatter(hotSaleProductResult.getTotalAmount()));
            hotSaleProductResult.setTotalQuantityStr(currencyFormatter(hotSaleProductResult.getTotalQuantity()));
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SXSSFWorkbook workbook = null;
        try {
            workbook = new SXSSFWorkbook();
            // fill du lieu sheet 0
            SXSSFSheet sheet = workbook.createSheet("Thổng kê sản phẩm bán chạy");
            int start = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleCustom = workbook.createCellStyle();
            styleCustom.setFont(fontDf);
            styleCustom.setBorderRight(BorderStyle.THIN);
            styleCustom.setWrapText(true);
            styleCustom.setFont(fontDf);
            styleCustom.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleCustom.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderSalesProductStatsExcel(
                user.getCompanyId(),
                workbook,
                user,
                sheet,
                0,
                null,
                "Thong_Ke_San_Pham_Ban_Chay",
                Util.getPeriod(fromDate, toDate),
                getCellStyleList(workbook),
                "Thổng kê sản phẩm bán chạy " + type,
                5
            );
            if (detail.size() == 0) {
                Row row = sheet.createRow(9);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 20);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            int countRow = 0;
            List<HeaderConfig> headerConfigs = ReportConstants.THONG_KE_SAN_PHAM_BAN_CHAY.HeaderConfig;
            List<FieldConfig> configList = ReportConstants.THONG_KE_SAN_PHAM_BAN_CHAY.FieldConfig;
            FastExcelUtils.genDataReportExcelHotSalesStats(sheet, configList, headerConfigs, detail, 10, getCellStyleList(workbook));
            //            Kí
            countRow = 16;
            CellStyle makerStyle1 = workbook.createCellStyle();
            makerStyle1.setBorderTop(BorderStyle.NONE);
            makerStyle1.setBorderBottom(BorderStyle.NONE);
            makerStyle1.setBorderLeft(BorderStyle.NONE);
            makerStyle1.setBorderRight(BorderStyle.NONE);
            makerStyle1.setAlignment(HorizontalAlignment.CENTER);
            makerStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
            makerStyle1.setFont(fontDf);
            makerStyle1.setWrapText(true);

            CellStyle makerStyle2 = workbook.createCellStyle();
            makerStyle2.setBorderTop(BorderStyle.NONE);
            makerStyle2.setBorderBottom(BorderStyle.NONE);
            makerStyle2.setBorderLeft(BorderStyle.NONE);
            makerStyle2.setBorderRight(BorderStyle.NONE);
            makerStyle2.setAlignment(HorizontalAlignment.CENTER);
            makerStyle2.setVerticalAlignment(VerticalAlignment.CENTER);
            makerStyle2.setFont(fontDf);
            makerStyle2.setWrapText(true);
            Row rowMaker1 = sheet.createRow(countRow + 1);
            Row rowMaker2 = sheet.createRow(countRow + 2);
            sheet.addMergedRegion(new CellRangeAddress(countRow + 1, countRow + 1, 0, 3));
            sheet.addMergedRegion(new CellRangeAddress(countRow + 1, countRow + 1, 9, 12));
            sheet.addMergedRegion(new CellRangeAddress(countRow + 2, countRow + 2, 0, 3));
            sheet.addMergedRegion(new CellRangeAddress(countRow + 2, countRow + 2, 9, 12));
            sheet.addMergedRegion(new CellRangeAddress(countRow, countRow, 9, 12));
            Cell titleMaker = rowMaker1.createCell(0);
            titleMaker.setCellStyle(makerStyle1);
            titleMaker.setCellValue("Người lập");
            Cell signTitle = rowMaker2.createCell(0);
            signTitle.setCellStyle(makerStyle2);
            signTitle.setCellValue("(Ký, ghi rõ họ tên)");

            Row rowDateTime = sheet.createRow(countRow);
            Cell dateTime = rowDateTime.createCell(9);
            dateTime.setCellStyle(makerStyle2);
            dateTime.setCellValue("Ngày.....tháng.....năm");
            Cell titleSigner = rowMaker1.createCell(9);
            titleSigner.setCellStyle(makerStyle1);
            titleSigner.setCellValue("Người đại diện");
            Cell signer = rowMaker2.createCell(9);
            signer.setCellStyle(makerStyle2);
            signer.setCellValue("(Ký tên, đóng dấu)");
            workbook.write(bos);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    @Override
    public ResDynamicReportDTO getDataPdfReport(RequestReport requestReport) throws JRException {
        byte[] result = null;
        Map<String, Object> parameter = new HashMap<>();
        User user = userService.getUserWithAuthorities();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        JasperReport jasperReport;
        String reportName = "THONG_KE_HH_BAN_RA";
        SaleProductStatsRequest req = new SaleProductStatsRequest();
        req.setComId(requestReport.getComId());
        req.setPattern(requestReport.getPattern());
        req.setStatus(requestReport.getStatus());
        req.setFromDate(requestReport.getFromDateStr());
        req.setToDate(requestReport.getToDateStr());
        ResultDTO productSales = reportManagementService.saleProductStats(req);
        SaleProductStatsResponse res = (SaleProductStatsResponse) productSales.getData();
        List<SaleProductStatsResult> detail = res.getDetail();
        for (int i = 0; i < detail.size(); i++) {
            BigDecimal totalPreTax = detail.get(i).getTotalPreTax().setScale(2, RoundingMode.HALF_UP);
            detail.get(i).setTotalPreTax(totalPreTax);

            BigDecimal vatAmount = detail.get(i).getVatAmount().setScale(2, RoundingMode.HALF_UP);
            detail.get(i).setVatAmount(vatAmount);
        }
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            // define the target format
            SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");

            // convert and format fromDate
            String fromDateStr = res.getFromDate();
            Date fromDate = fromFormat.parse(fromDateStr);
            String formattedFromDate = toFormat.format(fromDate);

            // convert and format toDate
            String toDateStr = res.getToDate();
            Date toDate = fromFormat.parse(toDateStr);
            String formattedToDate = toFormat.format(toDate);

            parameter.put("fromDateStr", formattedFromDate);
            parameter.put("toDateStr", formattedToDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        jasperReport = Common.getCompiledFile("sales-product/" + reportName + ".jasper", reportName + ".jrxml");

        result = Common.generateReportPDF(detail, parameter, jasperReport);
        resDynamicReportDTO.setReportPDF(result);

        return resDynamicReportDTO;
    }

    @Override
    public ResDynamicReportDTO getDataPdfReportHotSalesStats(RequestReport requestReport) throws JRException {
        byte[] result = null;
        Map<String, Object> parameter = new HashMap<>();
        User user = userService.getUserWithAuthorities();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        JasperReport jasperReport;
        String reportName = "THONG_KE_SAN_PHAM_BAN_CHAY";
        HotSaleProductRequest req = new HotSaleProductRequest();
        req.setComId(requestReport.getComId());
        req.setFromDate(String.valueOf(requestReport.getFromDate()));
        req.setToDate(String.valueOf(requestReport.getToDate()));
        req.setType(requestReport.getType());
        ResultDTO productHotSales = reportManagementService.hotSaleProductStats(
            req.getComId(),
            req.getFromDate(),
            req.getToDate(),
            req.getType()
        );
        HotSaleProductResponse res = (HotSaleProductResponse) productHotSales.getData();
        List<HotSaleProductResult> detail = res.getDetail();
        String type;
        if (req.getType() == 1) {
            type = "theo số lượng giảm dần";
        } else {
            type = "theo doanh thu giảm dần";
        }
        parameter.put("typeStr", type);
        Integer stt = 0;
        for (int i = 0; i < detail.size(); i++) {
            stt++;
            detail.get(i).setStt(stt);
            BigDecimal totalQuantity = detail.get(i).getTotalQuantity().setScale(2, RoundingMode.HALF_UP);
            detail.get(i).setTotalQuantity(totalQuantity);

            BigDecimal totalAmount = detail.get(i).getTotalAmount().setScale(2, RoundingMode.HALF_UP);
            detail.get(i).setTotalAmount(totalAmount);

            detail.get(i).setTotalAmountStr(currencyFormatter(totalAmount));
            detail.get(i).setTotalQuantityStr(currencyFormatter(totalQuantity));
        }
        try {
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            // define the target format
            SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");

            // convert and format fromDate
            String fromDateStr = res.getFromDate();
            Date fromDate = fromFormat.parse(fromDateStr);
            String formattedFromDate = toFormat.format(fromDate);

            // convert and format toDate
            String toDateStr = res.getToDate();
            Date toDate = fromFormat.parse(toDateStr);
            String formattedToDate = toFormat.format(toDate);

            parameter.put("fromDateStr", coverDate(formattedFromDate));
            parameter.put("toDateStr", coverDate(formattedToDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jasperReport = Common.getCompiledFile("hot-product/" + reportName + ".jasper", reportName + ".jrxml");
        result = Common.generateReportPDF(detail, parameter, jasperReport);
        resDynamicReportDTO.setReportPDF(result);

        return resDynamicReportDTO;
    }

    @Override
    public ResDynamicReportDTO getDataPdfRevenueCommon(ReportRevenueCommonReq requestReport) throws JRException {
        byte[] result = null;
        Map<String, Object> parameter = new HashMap<>();
        User user = userService.getUserWithAuthorities();
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        JasperReport jasperReport;
        String reportName = "THONG_KE_DOANH_THU_LOI_NHUAN";
        List<RevenueCommonStatsResponse.Detail> detail = new ArrayList<>();
        ResultDTO revenueCommon = new ResultDTO();
        try {
            revenueCommon =
                reportManagementService.getBillRevenue(
                    requestReport.getComId(),
                    requestReport.getFromDate(),
                    requestReport.getToDate(),
                    requestReport.getType()
                );
            RevenueCommonStatsResponse res = (RevenueCommonStatsResponse) revenueCommon.getData();
            detail = res.getDetail();
            SimpleDateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
            // define the target format
            SimpleDateFormat toFormat = new SimpleDateFormat("dd-MM-yyyy");

            // convert and format fromDate
            String fromDateStr = res.getFromDate();
            Date fromDate = fromFormat.parse(fromDateStr);
            String formattedFromDate = toFormat.format(fromDate);

            // convert and format toDate
            String toDateStr = res.getToDate();
            Date toDate = fromFormat.parse(toDateStr);
            String formattedToDate = toFormat.format(toDate);

            parameter.put("fromDate", formattedFromDate);
            parameter.put("toDate", formattedToDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        jasperReport = Common.getCompiledFile("revenue-common/" + reportName + ".jasper", reportName + ".jrxml");

        result = Common.generateReportPDF(detail, parameter, jasperReport);
        resDynamicReportDTO.setReportPDF(result);

        return resDynamicReportDTO;
    }

    @Override
    public byte[] getDataExcelRevenueCommon(ReportRevenueCommonReq requestReport) throws Exception {
        byte[] result = null;
        ResDynamicReportDTO resDynamicReportDTO = new ResDynamicReportDTO();
        result = getExcelRevenueCommon(requestReport);
        resDynamicReportDTO.setReportPDF(result);
        return result;
    }

    byte[] getExcelRevenueCommon(ReportRevenueCommonReq requestReport) throws Exception {
        User user = userService.getUserWithAuthorities();
        LocalDate fromDate = LocalDate.parse(requestReport.getFromDate());
        LocalDate toDate = LocalDate.parse(requestReport.getToDate());
        List<RevenueCommonStatsResponse.Detail> detail = new ArrayList<>();
        ResultDTO revenueCommon = new ResultDTO();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        SXSSFWorkbook workbook = null;
        try {
            revenueCommon =
                reportManagementService.getBillRevenue(
                    requestReport.getComId(),
                    requestReport.getFromDate(),
                    requestReport.getToDate(),
                    requestReport.getType()
                );
            RevenueCommonStatsResponse res = (RevenueCommonStatsResponse) revenueCommon.getData();
            detail = res.getDetail();
            workbook = new SXSSFWorkbook();
            // fill du lieu sheet 0
            SXSSFSheet sheet = workbook.createSheet("Thổng kê hàng hóa");
            int start = 10;
            Font fontDf = workbook.createFont();
            fontDf.setFontName("Times New Roman");
            CellStyle style = workbook.createCellStyle();
            style.setFont(fontDf);
            style.setBorderBottom(BorderStyle.THIN);
            style.setBorderTop(BorderStyle.THIN);
            style.setBorderRight(BorderStyle.THIN);
            style.setBorderLeft(BorderStyle.THIN);
            style.setWrapText(true);
            style.setFont(fontDf);
            style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            CellStyle styleCustom = workbook.createCellStyle();
            styleCustom.setFont(fontDf);
            styleCustom.setBorderRight(BorderStyle.THIN);
            styleCustom.setWrapText(true);
            styleCustom.setFont(fontDf);
            styleCustom.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleCustom.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Font fontB = workbook.createFont();
            fontB.setFontName("Times New Roman");
            fontB.setBold(true);
            CellStyle styleB = workbook.createCellStyle();
            styleB.setFont(fontB);
            styleB.setBorderBottom(BorderStyle.THIN);
            styleB.setBorderTop(BorderStyle.THIN);
            styleB.setBorderRight(BorderStyle.THIN);
            styleB.setBorderLeft(BorderStyle.THIN);
            styleB.setWrapText(true);
            styleB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
            styleB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            // set header
            SetHeaderSalesProductStatsExcel(
                user.getCompanyId(),
                workbook,
                user,
                sheet,
                0,
                null,
                "Thong_Ke_Doanh_Thu_Loi_Nhuan",
                Util.getPeriod(fromDate, toDate),
                getCellStyleList(workbook),
                "Thống kê doanh thu - lợi nhuận",
                4
            );
            if (detail.size() == 0) {
                Row row = sheet.createRow(9);
                CellRangeAddress cellRangeAddress = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 20);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            int countRow = 0;
            List<HeaderConfig> headerConfigs = ReportConstants.THONG_KE_DOANH_THU_LOI_NHUAN.HeaderConfig;
            List<FieldConfig> configList = ReportConstants.THONG_KE_DOANH_THU_LOI_NHUAN.FieldConfig;
            FastExcelUtils.genDataReportExcelProfit(sheet, configList, headerConfigs, detail, 10, getCellStyleList(workbook));
            workbook.write(bos);
        } catch (Exception e) {
            log.error(e.getMessage());
        } finally {
            try {
                if (workbook != null) {
                    workbook.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bos.toByteArray();
    }

    public String currencyFormatter(BigDecimal money) {
        NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
        numberFormat.setMinimumFractionDigits(0);
        numberFormat.setMaximumFractionDigits(2);
        try {
            return numberFormat.format(numberFormat.parse(money.toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String coverDate(String date) {
        return date.replace("-", "/");
    }
}
