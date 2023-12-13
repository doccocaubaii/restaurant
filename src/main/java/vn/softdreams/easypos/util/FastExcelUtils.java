package vn.softdreams.easypos.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.service.dto.FieldConfig;
import vn.softdreams.easypos.service.dto.HeaderConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class FastExcelUtils {

    private static final String LEFT = "text-left";
    private static final String RIGHT = "text-right";
    private static final String CENTER = "text-center";

    public static void SetHeaderExcel(
        Integer selectOrg,
        org.apache.poi.ss.usermodel.Workbook workbook,
        User user,
        SXSSFSheet sheet,
        Integer totalColumn,
        String nameCirculars,
        String titleReport,
        String fromDateAndToDate,
        List<CellStyle> cellStyleList
    ) {
        Font fontDf = workbook.createFont();
        fontDf.setFontName("Times New Roman");
        fontDf.setBold(false);
        fontDf.setFontHeightInPoints((short) 11);
        Font fontB = workbook.createFont();
        fontB.setFontName("Times New Roman");
        fontB.setBold(true);
        fontB.setFontHeightInPoints((short) 11);
        Font fontBI = workbook.createFont();
        fontBI.setFontName("Times New Roman");
        fontBI.setBold(true);
        fontBI.setItalic(true);
        fontBI.setFontHeightInPoints((short) 11);
        Font fontI = workbook.createFont();
        fontI.setFontName("Times New Roman");
        fontI.setBold(false);
        fontI.setItalic(true);
        fontI.setFontHeightInPoints((short) 11);
        CellStyle styleHeaderB = workbook.createCellStyle();
        styleHeaderB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderB.setWrapText(true);
        styleHeaderB.setFont(fontB);
        CellStyle styleHeaderN = workbook.createCellStyle();
        styleHeaderN.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderN.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderN.setWrapText(true);
        styleHeaderN.setFont(fontDf);
        CellStyle styleHeaderI = workbook.createCellStyle();
        styleHeaderI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderI.setWrapText(true);
        styleHeaderI.setFont(fontI);
        int totalColumnR = totalColumn;
        totalColumn = Math.max(totalColumn, 5);
        int center = (int) Math.floor(totalColumn * 2 / 3);
        Row row = sheet.createRow(5);
        Cell cell = row.createCell(0);
        cell.setCellValue("Thống Kê Lợi Nhuận Sản Phẩm");
        cell.setCellStyle(cellStyleList.get(8));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 13));
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        Row row6 = sheet.createRow(6);
        Cell cell601 = row6.createCell(0);
        cell601.setCellValue(fromDateAndToDate);
        cell601.setCellStyle(cellStyleList.get(4));
        sheet.addMergedRegion(new CellRangeAddress(row6.getRowNum(), row6.getRowNum(), 0, 13));
        CellUtil.setAlignment(cell601, HorizontalAlignment.CENTER);
    }

    public static void SetHeaderSalesProductStatsExcel(
        Integer selectOrg,
        org.apache.poi.ss.usermodel.Workbook workbook,
        User user,
        SXSSFSheet sheet,
        Integer totalColumn,
        String nameCirculars,
        String titleReport,
        String fromDateAndToDate,
        List<CellStyle> cellStyleList,
        String title,
        Integer numberCol
    ) {
        Font fontDf = workbook.createFont();
        fontDf.setFontName("Times New Roman");
        fontDf.setBold(false);
        fontDf.setFontHeightInPoints((short) 11);
        Font fontB = workbook.createFont();
        fontB.setFontName("Times New Roman");
        fontB.setBold(true);
        fontB.setFontHeightInPoints((short) 11);
        Font fontBI = workbook.createFont();
        fontBI.setFontName("Times New Roman");
        fontBI.setBold(true);
        fontBI.setItalic(true);
        fontBI.setFontHeightInPoints((short) 11);
        Font fontI = workbook.createFont();
        fontI.setFontName("Times New Roman");
        fontI.setBold(false);
        fontI.setItalic(true);
        fontI.setFontHeightInPoints((short) 11);
        CellStyle styleHeaderB = workbook.createCellStyle();
        styleHeaderB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderB.setWrapText(true);
        styleHeaderB.setFont(fontB);
        CellStyle styleHeaderN = workbook.createCellStyle();
        styleHeaderN.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderN.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderN.setWrapText(true);
        styleHeaderN.setFont(fontDf);
        CellStyle styleHeaderI = workbook.createCellStyle();
        styleHeaderI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderI.setWrapText(true);
        styleHeaderI.setFont(fontI);
        int totalColumnR = totalColumn;
        totalColumn = Math.max(totalColumn, 5);
        int center = (int) Math.floor(totalColumn * 2 / 3);
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue(title);
        cell.setCellStyle(cellStyleList.get(8));
        sheet.addMergedRegion(new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, numberCol * 3 - 1));
        CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
        Row row6 = sheet.createRow(1);
        Cell cell601 = row6.createCell(0);
        cell601.setCellValue(fromDateAndToDate);
        cell601.setCellStyle(cellStyleList.get(4));
        sheet.addMergedRegion(new CellRangeAddress(row6.getRowNum(), row6.getRowNum(), 0, numberCol * 3 - 1));
        CellUtil.setAlignment(cell601, HorizontalAlignment.CENTER);
    }

    public static void genBodyDynamicReportExcelProfit(
        SXSSFSheet sheet,
        List<FieldConfig> fieldConfig,
        List<HeaderConfig> headerConfig,
        List data,
        int startRow,
        List<CellStyle> cellStyleList
    ) {
        for (HeaderConfig config : headerConfig) {
            Row rowHeader = sheet.getRow(config.getRow());
            if (rowHeader == null) {
                rowHeader = sheet.createRow(config.getRow());
            }
            Cell cell = rowHeader.createCell(config.getCol());
            cell.setCellValue(config.getName());
            if (config.getColspan() != 1 || config.getRowspan() != 1) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                    config.getRow(),
                    config.getRow() + config.getRowspan() - 1,
                    config.getCol(),
                    config.getCol() + config.getColspan() - 1
                );
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            cell.setCellStyle(cellStyleList.get(1));
            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            CellUtil.setVerticalAlignment(cell, org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            startRow = config.getRow() + 1;
        }
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow + 1);
            int fieldSize = fieldConfig.size();
            for (int j = 0; j < fieldSize; j++) {
                Cell cell = row.createCell(j * 3);
                CellRangeAddress cellRangeAddress1 = new CellRangeAddress(row.getRowNum(), row.getRowNum(), j * 3, (j + 1) * 3 - 1);
                sheet.addMergedRegion(cellRangeAddress1);
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                Object fieldValue = ReflectionUtils.getFieldValue(data.get(i), fieldConfig.get(j).getName());
                cell.setCellStyle(cellStyleList.get(3));
                String formattedData;
                if (fieldValue != null) {
                    switch (fieldConfig.get(j).getAlign()) {
                        case LEFT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
                            break;
                        case RIGHT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
                            break;
                        case CENTER:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                            break;
                    }
                    if (fieldValue instanceof String) {
                        formattedData = fieldValue.toString();
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) fieldValue).doubleValue());
                        cell.setCellStyle(cellStyleList.get(5));
                    } else if (fieldValue instanceof LocalDate) {
                        formattedData = ((LocalDate) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof LocalDateTime) {
                        formattedData = ((LocalDateTime) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY_HHMM));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof Integer) {
                        if (((Integer) fieldValue).doubleValue() != 0) {
                            cell.setCellValue(((Integer) fieldValue).doubleValue());
                        }
                    }
                }
                if (fieldConfig.get(j).getName().equalsIgnoreCase("description")) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(i + startRow + 1, i + startRow + 1, 1, 7);
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                    RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                } else if (fieldConfig.get(j).getName().equalsIgnoreCase("amount")) {
                    CellRangeAddress cellRangeAddress = new CellRangeAddress(i + startRow + 1, i + startRow + 1, 8, 9);
                    sheet.addMergedRegion(cellRangeAddress);
                    RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                    RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                }
            }
        }
    }

    public static void genDataReportExcelProfit(
        SXSSFSheet sheet,
        List<FieldConfig> fieldConfig,
        List<HeaderConfig> headerConfig,
        List data,
        int startRow,
        List<CellStyle> cellStyleList
    ) {
        for (HeaderConfig config : headerConfig) {
            Row rowHeader = sheet.getRow(config.getRow());
            if (rowHeader == null) {
                rowHeader = sheet.createRow(config.getRow());
            }
            Cell cell = rowHeader.createCell(config.getCol());
            cell.setCellValue(config.getName());
            if (config.getColspan() != 1 || config.getRowspan() != 1) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                    config.getRow(),
                    config.getRow() + config.getRowspan() - 1,
                    config.getCol(),
                    config.getCol() + config.getColspan() - 1
                );
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            cell.setCellStyle(cellStyleList.get(1));
            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            CellUtil.setVerticalAlignment(cell, org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            startRow = config.getRow() + 1;
        }
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow + 1);
            int fieldSize = fieldConfig.size();
            for (int j = 0; j < fieldSize; j++) {
                Cell cell = row.createCell(j * 3);
                CellRangeAddress cellRangeAddress1 = new CellRangeAddress(row.getRowNum(), row.getRowNum(), j * 3, (j + 1) * 3 - 1);
                sheet.addMergedRegion(cellRangeAddress1);
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                Object fieldValue = ReflectionUtils.getFieldValue(data.get(i), fieldConfig.get(j).getName());
                cell.setCellStyle(cellStyleList.get(3));
                String formattedData;
                if (fieldValue != null) {
                    switch (fieldConfig.get(j).getAlign()) {
                        case LEFT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
                            break;
                        case RIGHT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
                            break;
                        case CENTER:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                            break;
                    }
                    if (fieldValue instanceof String) {
                        formattedData = fieldValue.toString();
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) fieldValue).doubleValue());
                        cell.setCellStyle(cellStyleList.get(5));
                    } else if (fieldValue instanceof LocalDate) {
                        formattedData = ((LocalDate) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof LocalDateTime) {
                        formattedData = ((LocalDateTime) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY_HHMM));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof Integer) {
                        if (((Integer) fieldValue).doubleValue() != 0) {
                            cell.setCellValue(((Integer) fieldValue).doubleValue());
                        }
                    }
                }
            }
        }
    }

    public static List<CellStyle> getCellStyleList(SXSSFWorkbook workbook) {
        List<CellStyle> cellStyleList = new ArrayList<>();

        Font fontDf = workbook.createFont();
        fontDf.setFontName("Times New Roman");
        fontDf.setBold(false);
        fontDf.setFontHeightInPoints((short) 11);

        Font fontDf1 = workbook.createFont();
        fontDf1.setFontName("Times New Roman");
        fontDf1.setBold(false);
        fontDf1.setFontHeightInPoints((short) 6);

        Font fontB = workbook.createFont();
        fontB.setFontName("Times New Roman");
        fontB.setBold(true);
        fontB.setFontHeightInPoints((short) 11);

        Font fontC = workbook.createFont();
        fontC.setFontName("Times New Roman");
        fontC.setBold(true);
        fontC.setFontHeightInPoints((short) 14);

        Font fontBI = workbook.createFont();
        fontBI.setFontName("Times New Roman");
        fontBI.setBold(true);
        fontBI.setItalic(true);
        fontBI.setFontHeightInPoints((short) 11);

        Font fontH = workbook.createFont();
        fontH.setFontName("Times New Roman");
        fontH.setBold(true);
        fontH.setItalic(false);
        fontH.setUnderline((byte) 1);
        fontH.setFontHeightInPoints((short) 11);

        Font fontI = workbook.createFont();
        fontI.setFontName("Times New Roman");
        fontI.setBold(false);
        fontI.setItalic(true);
        fontI.setFontHeightInPoints((short) 11);

        Font fontRed = workbook.createFont();
        fontRed.setFontName("Times New Roman");
        fontRed.setColor(Font.COLOR_RED);
        fontRed.setFontHeightInPoints((short) 11);

        // 0. Đậm k border
        CellStyle styleHeaderB = workbook.createCellStyle();
        styleHeaderB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderB.setWrapText(true);
        styleHeaderB.setFont(fontB);
        cellStyleList.add(styleHeaderB);

        // 1. Đậm có border
        CellStyle styleHeaderBorderB = workbook.createCellStyle();
        styleHeaderBorderB.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderBorderB.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderBorderB.setWrapText(true);
        styleHeaderBorderB.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderB.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderB.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderB.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderB.setFont(fontB);
        cellStyleList.add(styleHeaderBorderB);

        // 2. Thường k border
        CellStyle styleHeaderN = workbook.createCellStyle();
        styleHeaderN.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderN.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderN.setWrapText(true);
        styleHeaderN.setFont(fontDf);
        cellStyleList.add(styleHeaderN);

        // 3. Thường có border
        CellStyle styleHeaderBorderN = workbook.createCellStyle();
        styleHeaderBorderN.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderBorderN.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderBorderN.setWrapText(true);
        styleHeaderBorderN.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderN.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderN.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderN.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderN.setFont(fontDf);
        cellStyleList.add(styleHeaderBorderN);

        // 4. Nghiêng k border
        CellStyle styleHeaderI = workbook.createCellStyle();
        styleHeaderI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderI.setWrapText(true);
        styleHeaderI.setFont(fontI);
        cellStyleList.add(styleHeaderI);

        // 5. Số thường có border
        DataFormat format = workbook.createDataFormat();
        CellStyle styleNumber = workbook.createCellStyle();
        styleNumber.cloneStyleFrom(cellStyleList.get(3));
        styleNumber.setDataFormat(format.getFormat("#,##0"));
        cellStyleList.add(styleNumber);

        // 6. Số đậm có border
        CellStyle styleNumberBold = workbook.createCellStyle();
        styleNumberBold.cloneStyleFrom(cellStyleList.get(1));
        styleNumberBold.setDataFormat(format.getFormat("#,##0"));
        cellStyleList.add(styleNumberBold);

        // 7. Đậm k border title
        CellStyle styleHeaderTitle = workbook.createCellStyle();
        styleHeaderTitle.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderTitle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderTitle.setWrapText(true);
        styleHeaderTitle.setFont(fontC);
        cellStyleList.add(styleHeaderTitle);

        // 8. Đậm nghiêng k có border
        CellStyle styleTextBI = workbook.createCellStyle();
        styleTextBI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleTextBI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTextBI.setWrapText(true);
        styleTextBI.setFont(fontBI);
        cellStyleList.add(styleTextBI);

        // 9. đậm nghiêng có border
        CellStyle styleTextBorderBI = workbook.createCellStyle();
        styleTextBorderBI.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleTextBorderBI.cloneStyleFrom(cellStyleList.get(1));
        styleTextBorderBI.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTextBorderBI.setWrapText(true);
        styleTextBorderBI.setFont(fontBI);
        cellStyleList.add(styleTextBorderBI);

        // 10. Thường có border dưới
        CellStyle styleHeaderBorderBottom = workbook.createCellStyle();
        styleHeaderBorderBottom.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderBorderBottom.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderBorderBottom.setWrapText(true);
        styleHeaderBorderBottom.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderBottom.setFont(fontDf);
        cellStyleList.add(styleHeaderBorderBottom);

        // 11. Thường nghiêng k có border trên
        CellStyle styleNoneBorderTop = workbook.createCellStyle();
        styleNoneBorderTop.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleNoneBorderTop.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleNoneBorderTop.setWrapText(true);
        styleNoneBorderTop.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderTop.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderTop.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderTop.setFont(fontI);
        cellStyleList.add(styleNoneBorderTop);

        // 12. Thường k có border phải
        CellStyle styleNoneBorderRight = workbook.createCellStyle();
        styleNoneBorderRight.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleNoneBorderRight.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleNoneBorderRight.setWrapText(true);
        styleNoneBorderRight.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderRight.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderRight.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderRight.setFont(fontDf);
        cellStyleList.add(styleNoneBorderRight);

        // 13. Đậm nghiêng k có border trái
        CellStyle styleNoneBorderLeft = workbook.createCellStyle();
        styleNoneBorderLeft.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleNoneBorderLeft.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleNoneBorderLeft.setWrapText(true);
        styleNoneBorderLeft.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderLeft.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderLeft.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleNoneBorderLeft.setFont(fontBI);
        cellStyleList.add(styleNoneBorderLeft);

        // 14. Thường không border, chữ nhỏ góc trên
        CellStyle styleHeaderBorder1 = workbook.createCellStyle();
        styleHeaderBorder1.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderBorder1.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderBorder1.setWrapText(true);
        styleHeaderBorder1.setFont(fontDf1);
        cellStyleList.add(styleHeaderBorder1);

        //15.Đậm in hoa có gạch chân không border
        CellStyle styleTextH = workbook.createCellStyle();
        styleTextH.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleTextH.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleTextH.setWrapText(true);
        styleTextH.setFont(fontH);
        cellStyleList.add(styleTextH);

        // 16. Chữ đỏ
        CellStyle styleColorRed = workbook.createCellStyle();
        styleColorRed.cloneStyleFrom(cellStyleList.get(5));
        styleColorRed.setFont(fontRed);
        cellStyleList.add(styleColorRed);

        // 17. Thường có border trên
        CellStyle styleHeaderBorderTop = workbook.createCellStyle();
        styleHeaderBorderTop.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        styleHeaderBorderTop.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        styleHeaderBorderTop.setWrapText(true);
        styleHeaderBorderTop.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN);
        styleHeaderBorderTop.setFont(fontDf);
        cellStyleList.add(styleHeaderBorderTop);

        return cellStyleList;
    }

    public static void genDataReportExcelHotSalesStats(
        SXSSFSheet sheet,
        List<FieldConfig> fieldConfig,
        List<HeaderConfig> headerConfig,
        List data,
        int startRow,
        List<CellStyle> cellStyleList
    ) {
        for (HeaderConfig config : headerConfig) {
            Row rowHeader = sheet.getRow(config.getRow());
            if (rowHeader == null) {
                rowHeader = sheet.createRow(config.getRow());
            }
            Cell cell = rowHeader.createCell(config.getCol());
            cell.setCellValue(config.getName());
            if (config.getColspan() != 1 || config.getRowspan() != 1) {
                CellRangeAddress cellRangeAddress = new CellRangeAddress(
                    config.getRow(),
                    config.getRow() + config.getRowspan() - 1,
                    config.getCol(),
                    config.getCol() + config.getColspan() - 1
                );
                sheet.addMergedRegion(cellRangeAddress);
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress, sheet);
            }
            cell.setCellStyle(cellStyleList.get(1));
            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            CellUtil.setVerticalAlignment(cell, org.apache.poi.ss.usermodel.VerticalAlignment.CENTER);
            startRow = config.getRow() + 1;
        }
        int dataSize = data.size();
        for (int i = 0; i < dataSize; i++) {
            Row row = sheet.createRow(i + startRow + 1);
            int fieldSize = fieldConfig.size();
            for (int j = 0; j < fieldSize; j++) {
                Cell cell;
                CellRangeAddress cellRangeAddress1;
                if (j == 0) {
                    cell = row.createCell(0);
                    cellRangeAddress1 = new CellRangeAddress(row.getRowNum(), row.getRowNum(), 0, 0);
                } else {
                    cell = row.createCell((j * 3) - 2);
                    cellRangeAddress1 = new CellRangeAddress(row.getRowNum(), row.getRowNum(), (j * 3) - 2, j * 3);
                    sheet.addMergedRegion(cellRangeAddress1);
                }
                RegionUtil.setBorderTop(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderRight(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderBottom(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                RegionUtil.setBorderLeft(org.apache.poi.ss.usermodel.BorderStyle.THIN, cellRangeAddress1, sheet);
                Object fieldValue = ReflectionUtils.getFieldValue(data.get(i), fieldConfig.get(j).getName());
                cell.setCellStyle(cellStyleList.get(3));
                String formattedData;
                if (fieldValue != null) {
                    switch (fieldConfig.get(j).getAlign()) {
                        case LEFT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.LEFT);
                            break;
                        case RIGHT:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.RIGHT);
                            break;
                        case CENTER:
                            CellUtil.setAlignment(cell, org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
                            break;
                    }
                    if (fieldValue instanceof String) {
                        formattedData = fieldValue.toString();
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof BigDecimal) {
                        cell.setCellValue(((BigDecimal) fieldValue).doubleValue());
                        cell.setCellStyle(cellStyleList.get(5));
                    } else if (fieldValue instanceof LocalDate) {
                        formattedData = ((LocalDate) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof LocalDateTime) {
                        formattedData = ((LocalDateTime) fieldValue).format(DateTimeFormatter.ofPattern(DateUtil.C_DD_MM_YYYY_HHMM));
                        cell.setCellValue(formattedData);
                    } else if (fieldValue instanceof Integer) {
                        if (((Integer) fieldValue).doubleValue() != 0) {
                            cell.setCellValue(((Integer) fieldValue).doubleValue());
                        }
                    }
                }
            }
        }
    }
}
