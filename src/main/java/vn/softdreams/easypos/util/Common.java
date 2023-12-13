package vn.softdreams.easypos.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.base.Strings;
import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperRunManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.*;
import vn.softdreams.easypos.domain.*;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.service.dto.CustomPdf;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;
import vn.softdreams.easypos.web.rest.errors.RequestAlertException;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import javax.xml.bind.DatatypeConverter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

/**
 * @author congnd
 */
public class Common {

    private static final Logger log = LoggerFactory.getLogger(Common.class);

    /**
     * @param sort
     * @param params
     * @return
     */
    public static String addSort(Sort sort, Map<String, Object> params) {
        StringBuilder orderSql = new StringBuilder();
        StringBuilder valueParam = new StringBuilder();
        if (sort == null) {
            return "";
        }

        int i = 0;
        for (Sort.Order order : sort) {
            if (!Strings.isNullOrEmpty(order.getProperty())) {
                if (i > 0) {
                    valueParam.append(", ");
                }
                StringTokenizer stringTokenizer = new StringTokenizer(order.getProperty());
                if (stringTokenizer.countTokens() == 1) {
                    valueParam.append(order.getProperty());
                } else {
                    return null;
                }

                valueParam.append(" ");
                String direction = "";
                if (Strings.isNullOrEmpty(order.getDirection().toString())) {
                    direction = "ASC";
                } else {
                    if (
                        order.getDirection().toString().toUpperCase().equals("ASC") ||
                        order.getDirection().toString().toUpperCase().equals("DESC")
                    ) {
                        direction = order.getDirection().toString();
                    } else {
                        return null;
                    }
                }
                valueParam.append(direction);
                i++;
            }
        }
        if (!Strings.isNullOrEmpty(valueParam.toString())) {
            orderSql.append("ORDER BY " + valueParam);
            //            orderSql.append(" :propertyParam");
            //            params.put("propertyParam", valueParam);
        }

        return orderSql.toString();
    }

    /**
     * @Author phuonghv
     */
    public static String addMultiSort(Sort sort, Map<String, Object> params) {
        StringBuilder orderSql = new StringBuilder();
        StringBuilder valueParam = new StringBuilder();
        if (sort == null) {
            return "";
        }

        int i = 0;
        for (Sort.Order order : sort) {
            if (!Strings.isNullOrEmpty(order.getProperty())) {
                if (i > 0) {
                    valueParam.append(", ");
                }
                StringTokenizer stringTokenizer = new StringTokenizer(order.getProperty());
                if (stringTokenizer.countTokens() == 1) {
                    valueParam.append(order.getProperty());
                } else {
                    return null;
                }

                valueParam.append(" ");
                String direction = "";
                if (Strings.isNullOrEmpty(order.getDirection().toString())) {
                    direction = "ASC";
                } else {
                    if (
                        order.getDirection().toString().toUpperCase().equals("ASC") ||
                        order.getDirection().toString().toUpperCase().equals("DESC")
                    ) {
                        direction = order.getDirection().toString();
                    } else {
                        return null;
                    }
                }
                valueParam.append(direction);
                i++;
            }
        }
        if (!Strings.isNullOrEmpty(valueParam.toString())) {
            orderSql.append("ORDER BY " + valueParam);
            //            orderSql.append(" :propertyParam");
            //            params.put("propertyParam", valueParam);
        }

        return orderSql.toString();
    }

    /**
     * @param query
     * @param params
     * @Author hieugie
     * <p>
     * Hàm chung set param cho query
     */
    public static void setParams(Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Entry<String, Object>> set = params.entrySet();
            for (Entry<String, Object> obj : set) {
                if (obj.getValue() == null) query.setParameter(obj.getKey(), "");
                //                else if ((obj.getKey().equals("propertyParam") && !addParam));
                else query.setParameter(obj.getKey(), obj.getValue());
            }
        }
    }

    /**
     * @param query
     * @param params
     * @Author hieugie
     * <p>
     * Hàm chung set param và pageable cho query
     * Set lại giá trị offset trong trong hợp offset > tổng số bản ghi tìm được
     */
    public static void setParamsWithPageable(
        @NotNull Query query,
        Map<String, Object> params,
        @NotNull Pageable pageable,
        @NotNull Number total
    ) {
        if (params != null && !params.isEmpty()) {
            Set<Entry<String, Object>> set = params.entrySet();
            for (Entry<String, Object> obj : set) {
                //                if (!(obj.getKey().equals("propertyParam") && !addParam)) {
                query.setParameter(obj.getKey(), obj.getValue());
                //                }
            }
        }
        //        if (total.intValue() < (int)pageable.getOffset()) {
        //            pageable = PageRequest.of(0, pageable.getPageSize(), pageable.getSort());
        //        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable) {
        if (params != null && !params.isEmpty()) {
            Set<Entry<String, Object>> set = params.entrySet();
            for (Entry<String, Object> obj : set) {
                //                if (!(obj.getKey().equals("propertyParam") && !addParam)) {
                query.setParameter(obj.getKey(), obj.getValue());
                //                }
            }
        }
        //        if (total.intValue() < (int)pageable.getOffset()) {
        //            pageable = PageRequest.of(0, pageable.getPageSize(), pageable.getSort());
        //        }
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    /**
     * @param query    câu sql
     * @param params   tham số truyền vào
     * @param pageable thông tin phân trang
     *                 <p>
     *                 Thêm điều kiện ngày tháng nằm trong khoảng cho câu lệnh sql
     */
    public static void setPageableSQL(@NotNull StringBuilder query, Map<String, Object> params, @NotNull Pageable pageable) {
        query.append(" OFFSET :firstResult ROWS FETCH NEXT :maxResults ROWS ONLY ");
        params.put("firstResult", (int) pageable.getOffset());
        params.put("maxResults", pageable.getPageSize());
        //        String toReplace = "WHERE 1 = 1 AND";
        //        int index = -1;
        //        index = query.indexOf("WHERE 1 = 1 AND");
        //        if (index <= 0) {
        //            index = query.indexOf("WHERE 1 = 1");
        //            if (index > 0) {
        //                query.replace(index, index + toReplace.length(), " ");
        //            }
        //        } else {
        //            query.replace(index, index + toReplace.length(), "WHERE");
        //        }
    }

    public static void replaceWhere(@NotNull StringBuilder query) {
        String toReplace = "WHERE 1 = 1 AND";
        int index = -1;
        index = query.indexOf("WHERE 1 = 1 AND");
        if (index <= 0) {
            toReplace = "WHERE 1 = 1";
            index = query.indexOf(toReplace);
            if (index > 0) {
                query.replace(index, index + toReplace.length(), " ");
            }
        } else {
            query.replace(index, index + toReplace.length(), "WHERE");
        }
    }

    /**
     * @param fromDate   từ ngày - string yyyyMMdd
     * @param toDate     đến ngày - string yyyyMMdd
     * @param params     map các params
     * @param sqlBuilder câu sql
     * @param columnName tên cột ngày tháng cần truy vấn
     * @author dungvm
     * <p>
     * Thêm điều kiện ngày tháng nằm trong khoảng cho câu lệnh sql
     */
    public static void addDateSearch(
        String fromDate,
        String toDate,
        Map<String, Object> params,
        StringBuilder sqlBuilder,
        String columnName
    ) {
        if (!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append(
                "AND :fromDate" +
                columnName +
                " <= TO_CHAR(" +
                columnName +
                ", 'yyyyMMdd') AND :toDate" +
                columnName +
                " " +
                ">= TO_CHAR(" +
                columnName +
                ", 'yyyyMMdd') "
            );
            params.put("fromDate" + columnName, fromDate);
            params.put("toDate" + columnName, toDate);
        } else if (!Strings.isNullOrEmpty(fromDate)) {
            sqlBuilder.append("AND :fromDate" + columnName + " <= TO_CHAR(" + columnName + ", 'yyyyMMdd') ");
            params.put("fromDate" + columnName, fromDate);
        } else if (!Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append("AND :toDate" + columnName + " >= TO_CHAR(" + columnName + ", 'yyyyMMdd') ");
            params.put("toDate" + columnName, toDate);
        }
    }

    /**
     * @param fromDate   từ ngày - string yyyyMMdd
     * @param toDate     đến ngày - string yyyyMMdd
     * @param params     map các params
     * @param sqlBuilder câu sql
     * @param columnName tên cột ngày tháng cần truy vấn
     * @author phuonghv
     * <p>
     * Thêm điều kiện ngày tháng nằm trong khoảng cho câu lệnh sql
     */
    public static void addDateSearchCustom(
        String fromDate,
        String toDate,
        Map<String, Object> params,
        StringBuilder sqlBuilder,
        String columnName,
        String param
    ) {
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            sqlBuilder.append(" AND " + columnName);
            if ((!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate))) {
                sqlBuilder.append(">= :from" + param + " AND " + columnName + " < :to" + param + " ");
                params.put("from" + param, fromDate);
                params.put("to" + param, addOneDate(toDate));
            } else if (!Strings.isNullOrEmpty(fromDate)) {
                sqlBuilder.append(" >= :from" + param + " ");
                params.put("from" + param, fromDate);
            } else if (!Strings.isNullOrEmpty(toDate)) {
                sqlBuilder.append(" < :to" + param + " ");
                params.put("to" + param, addOneDate(toDate));
            }
        }
    }

    public static String addOneDate(String dateStr) {
        try {
            DateFormat formatter = new SimpleDateFormat(Constants.ZONED_DATE_FORMAT);
            Date date = formatter.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            return formatter.format(cal.getTime());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    public static void addDateSearchCustom(
        String fromDate,
        String toDate,
        Map<String, Object> params,
        StringBuilder sqlBuilder,
        String columnFromDate,
        String columnToDate,
        String param
    ) {
        if (!Strings.isNullOrEmpty(fromDate) || !Strings.isNullOrEmpty(toDate)) {
            if ((!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate))) {
                sqlBuilder.append(" and " + columnFromDate);
                sqlBuilder.append("<= :from" + param + " and " + columnToDate + " >= :to" + param + " ");
                params.put("from" + param, fromDate);
                params.put("to" + param, toDate);
            } else if (!Strings.isNullOrEmpty(fromDate)) {
                sqlBuilder.append(" and " + columnFromDate);
                sqlBuilder.append(" <= :from" + param + " ");
                params.put("from" + param, fromDate);
            } else if (!Strings.isNullOrEmpty(toDate)) {
                sqlBuilder.append(" and " + columnToDate);
                sqlBuilder.append(" >= :to" + param + " ");
                params.put("to" + param, toDate);
            }
        }
    }

    public static BigDecimal getBigDecimal(Object object) {
        return object != null ? (BigDecimal) object : null;
    }

    public static Float getFloat(Object object) {
        return object != null ? ((BigDecimal) object).floatValue() : null;
    }

    public static Integer getInteger(Object object) {
        return object != null ? ((BigDecimal) object).intValue() : null;
    }

    public static Integer getInteger(String object) {
        try {
            if (!Strings.isNullOrEmpty(object)) {
                return Integer.parseInt(object);
            }
        } catch (Exception ignore) {}
        return null;
    }

    public static Long getLong(Object object) {
        return object != null ? ((BigDecimal) object).longValue() : null;
    }

    public static Double getDouble(Object object) {
        return object != null ? ((BigDecimal) object).doubleValue() : null;
    }

    public static boolean getBoolean(Object object) {
        return object != null && ((BigDecimal) object).intValue() == 1;
    }

    /**
     * @param dateStr
     * @return
     * @author kienpv
     * <p>
     * convert string to string format yyyy/mm/dd
     */
    public static String converDate(String dateStr) {
        try {
            DateFormat formatter = new SimpleDateFormat("E MMM dd yyyy HH:mm:ss Z");
            Date date = formatter.parse(dateStr);
            System.out.println(date);

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            String formatedDate = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
            System.out.println("formatedDate : " + formatedDate);
            return formatedDate;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    public static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);
        //        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER, true);
        objectMapper.configure(JsonGenerator.Feature.ESCAPE_NON_ASCII, true);
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        objectMapper.registerModule(new JavaTimeModule());
        return objectMapper;
    }

    public static String convertObjectToString(Object object) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String trimField(String field) {
        if (field == null) return null;
        return field.trim();
    }

    public static Map<String, Object> convertJsonToMap(String json) {
        try {
            if (StringUtils.isNotEmpty(json)) {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(json, Map.class);
            }
        } catch (Exception e) {}
        return null;
    }

    public static String appendLike(String param) {
        StringBuilder builder = new StringBuilder();
        builder.append("%").append(param).append("%");
        return builder.toString();
    }

    public static <K extends Comparable<? super K>, V> Comparator<Entry<K, V>> comparingByKey() {
        return (Comparator<Entry<K, V>> & Serializable) (c1, c2) -> c1.getKey().compareTo(c2.getKey());
    }

    public static String convertLocalDateToString(LocalDate date, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern != null ? pattern : "yyyy-MM-dd");
        return date.format(formatter);
    }

    public static LocalDate convertStringToLocalDatePattern(String date, String pattern) {
        DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(date, formatter1);
    }

    public static String convertDate(LocalDate dateStr) {
        try {
            String formatedDate =
                dateStr.getYear() +
                "" +
                (dateStr.getMonthValue() < 10 ? "0" + dateStr.getMonthValue() : dateStr.getMonthValue()) +
                "" +
                (dateStr.getDayOfMonth() < 10 ? "0" + dateStr.getDayOfMonth() : dateStr.getDayOfMonth()) +
                "";
            return formatedDate;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return "";
        }
    }

    public static Instant convertStringToInstant(String dateString) {
        if (dateString != null) {
            Date date = null;
            try {
                date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dateString);
            } catch (ParseException e) {
                return null;
            }
            return date.toInstant();
        } else {
            return null;
        }
    }

    public static String convertInstantToString(Instant dateInstant) {
        if (dateInstant != null) {
            return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.systemDefault()).format(dateInstant);
        }
        return "";
    }

    public static String convertInstantToString(Instant dateInstant, String pattern) {
        if (dateInstant != null) {
            return DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault()).format(dateInstant);
        }
        return "";
    }

    public static String convertInstantToStringFormat(Instant dateInstant, String format) {
        if (dateInstant != null) {
            return DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault()).format(dateInstant);
        }
        return "";
    }

    public static String Sha256(String message) {
        //        String digest = null;
        //        try {
        //            MessageDigest md = MessageDigest.getInstance("SHA-256");
        //            byte[] hash = md.digest(message.getBytes("UTF-8"));
        //
        //            // converting byte array to Hexadecimal String
        //            StringBuilder sb = new StringBuilder(2 * hash.length);
        //            for (byte b : hash) {
        //                sb.append(String.format("%02x", b & 0xff));
        //            }
        //
        //            digest = sb.toString();
        //        } catch (UnsupportedEncodingException ex) {
        //            digest = "";
        //            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
        //            // null, ex);
        //        } catch (NoSuchAlgorithmException ex) {
        //            // Logger.getLogger(StringReplace.class.getName()).log(Level.SEVERE,
        //            // null, ex);
        //            digest = "";
        //        }
        return null;
    }

    public static String GetAmountInWords(long soTien, String currency, boolean includeDot) {
        Integer TCKHAC_DocTienBangChu = 1;
        int lan, i;
        long so;
        String ketQua = "";
        int[] viTri = new int[6];
        if (soTien < 0) return "Số tiền âm!";
        if (soTien == 0) {
            if (TCKHAC_DocTienBangChu == 1) {
                return "Không đồng chẵn!";
            } else {
                return "Không đồng!";
            }
        }
        so = soTien;
        //Kiểm tra số quá lớn
        if (soTien > Long.parseLong("8999999999999999")) {
            return "";
        }
        viTri[5] = (int) (so / Long.parseLong("1000000000000000"));
        so = so - Long.parseLong(String.valueOf(viTri[5])) * Long.parseLong("1000000000000000");
        viTri[4] = (int) (so / Long.parseLong("1000000000000"));
        so = so - Long.parseLong(String.valueOf(viTri[4])) * Long.parseLong("1000000000000");
        viTri[3] = (int) (so / 1000000000);
        so = so - Long.parseLong(String.valueOf(viTri[3])) * 1000000000;
        viTri[2] = (int) (so / 1000000);
        viTri[1] = (int) ((so % 1000000) / 1000);
        viTri[0] = (int) (so % 1000);
        if (viTri[5] > 0) {
            lan = 5;
        } else if (viTri[4] > 0) {
            lan = 4;
        } else if (viTri[3] > 0) {
            lan = 3;
        } else if (viTri[2] > 0) {
            lan = 2;
        } else if (viTri[1] > 0) {
            lan = 1;
        } else {
            lan = 0;
        }
        for (i = lan; i >= 0; i--) {
            boolean isDoc = String.valueOf(viTri[i]).length() < 3 && i < lan;
            String tmp = ReadGroupOfThree(viTri[i], isDoc);
            ketQua = ketQua.concat(tmp);
            if (viTri[i] != 0) ketQua += Tien[i];
            if ((i > 0) && (!Strings.isNullOrEmpty(tmp))) ketQua += "";
        }
        if (ketQua.endsWith(",")) ketQua = ketQua.substring(0, ketQua.length() - 1);
        ketQua = ketQua.trim();

        return ketQua.substring(0, 1).toUpperCase() + ketQua.substring(1) + currency + (includeDot ? "." : "");
    }

    private static final String[] Tien = { "", " nghìn", " triệu", " tỷ", " nghìn tỷ", " triệu tỷ" };
    private static final String[] ChuSo = { " không", " một", " hai", " ba", " bốn", " năm", " sáu", " bảy", " tám", " chín" };

    private static String ReadGroupOfThree(int baso, boolean isDoc0) {
        int DDSo_DocTienLe = 0;
        String ketQua = "";
        int tram = (int) (baso / 100);
        int chuc = (int) ((baso % 100) / 10);
        int donvi = baso % 10;
        if ((tram == 0) && (chuc == 0) && (donvi == 0)) return "";
        if (tram != 0 || isDoc0) {
            ketQua += ChuSo[tram] + " trăm";
            if ((chuc == 0) && (donvi != 0)) {
                if (DDSo_DocTienLe == 0) {
                    ketQua += " linh";
                } else {
                    ketQua += " lẻ";
                }
            }
        }
        if ((chuc != 0) && (chuc != 1)) {
            ketQua += ChuSo[chuc] + " mươi";
            if ((chuc == 0) && (donvi != 0)) if (DDSo_DocTienLe == 0) {
                ketQua = ketQua + " linh";
            } else {
                ketQua = ketQua + " lẻ";
            }
        }
        if (chuc == 1) ketQua += " mười";
        switch (donvi) {
            case 1:
                if ((chuc != 0) && (chuc != 1)) {
                    ketQua += " mốt";
                } else {
                    ketQua += ChuSo[donvi];
                }
                break;
            case 5:
                if (chuc == 0) {
                    ketQua += ChuSo[donvi];
                } else {
                    ketQua += " lăm";
                }
                break;
            default:
                if (donvi != 0) {
                    ketQua += ChuSo[donvi];
                }
                break;
        }
        return ketQua;
    }

    public static String deCodeBase64(String data) throws UnsupportedEncodingException {
        byte[] decodedBytes = Base64.getDecoder().decode(data.replace("", ""));
        return new String(decodedBytes, StandardCharsets.UTF_8);
    }

    public static Integer getCompanyID(User user, Integer companyID) {
        if (user.getManager()) {
            return companyID;
        }
        return user.getCompanyId();
    }

    public static void checkFileInvalid(String fileName, String[] otherList) {
        //        Check xem có đúng định dạng file không
        boolean isImgage = Arrays.asList(otherList).contains(fileName);
        if (isImgage) {
            throw new BadRequestAlertException("error", "account", "fileInvalid");
        }
    }

    public static String saveFile(MultipartFile fileItem, String[] fileFormats, String path, HttpServletRequest httpRequest) {
        try {
            path = path + "/";
            String pathDomain = "https://app.easyposs.vn" + ImagePathConstants.PATH_FILE;
            String fileName = fileItem.getOriginalFilename();
            Common.checkFileInvalid(fileName.substring(fileName.lastIndexOf(".")), fileFormats);
            fileName = fileName.replaceAll("[^a-zA-Z0-9.]", "");
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
            File compressedImageFile = new File(ImagePathConstants.ROOT_PATH_LINUX + path + fileName);
            if (!compressedImageFile.getParentFile().exists()) {
                compressedImageFile.getParentFile().mkdirs();
            }
            InputStream inputStream = fileItem.getInputStream();
            OutputStream outputStream = new FileOutputStream(compressedImageFile);
            float imageQuality = 0.5f;
            //Create the buffered image
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            //Get image writers
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName("jpg");

            if (!imageWriters.hasNext()) throw new IllegalStateException("Writers Not Found!!");

            ImageWriter imageWriter = (ImageWriter) imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            //Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);

            //Created image
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
            // close all streams
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();
            return pathDomain + path + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveFile(String urlImage, String fileName, String[] fileFormats, String path, HttpServletRequest httpRequest) {
        try {
            path = path + "/";
            String pathDomain = "https://app.easyposs.vn" + ImagePathConstants.PATH_FILE;
            Common.checkFileInvalid(fileName.substring(fileName.lastIndexOf(".")), fileFormats);
            String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_";
            File compressedImageFile = new File(ImagePathConstants.ROOT_PATH_LINUX + path + fileName);
            if (!compressedImageFile.getParentFile().exists()) {
                compressedImageFile.getParentFile().mkdirs();
            }
            URL url = new URL(urlImage);
            URLConnection connection = url.openConnection();
            InputStream inputStream = connection.getInputStream();
            OutputStream outputStream = new FileOutputStream(compressedImageFile);
            float imageQuality = 0.5f;
            //Create the buffered image
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            //Get image writers
            String fileFormat = fileName.substring(fileName.lastIndexOf(".") + 1);
            Iterator<ImageWriter> imageWriters = ImageIO.getImageWritersByFormatName(fileFormat);

            if (!imageWriters.hasNext()) throw new IllegalStateException("Writers Not Found!!");

            ImageWriter imageWriter = (ImageWriter) imageWriters.next();
            ImageOutputStream imageOutputStream = ImageIO.createImageOutputStream(outputStream);
            imageWriter.setOutput(imageOutputStream);

            ImageWriteParam imageWriteParam = imageWriter.getDefaultWriteParam();

            //Set the compress quality metrics
            imageWriteParam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            imageWriteParam.setCompressionQuality(imageQuality);
            //Created image
            imageWriter.write(null, new IIOImage(bufferedImage, null, null), imageWriteParam);
            // close all streams
            inputStream.close();
            outputStream.close();
            imageOutputStream.close();
            imageWriter.dispose();
            return pathDomain + path + fileName;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BigDecimal roundMoney(BigDecimal money) {
        return money.setScale(4, RoundingMode.HALF_UP);
    }

    public static BigDecimal roundMoney(BigDecimal money, int value) {
        return money.setScale(value, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateAfterVatRate(BigDecimal money, Integer vatRate) {
        return roundMoney(money.multiply(BigDecimal.valueOf(vatRate).divide(BigDecimal.valueOf(100.0))), Constants.ROUNDING);
    }

    public static boolean isNullOrZeroBigDecimal(BigDecimal value) {
        return value == null || value.compareTo(BigDecimal.ZERO) == 0;
    }

    public static boolean checkTaxAuthorityCode(String taxAuthorityCode) {
        if (Strings.isNullOrEmpty(taxAuthorityCode)) {
            throw new InternalServerException(
                ExceptionConstants.TAX_AUTHORITY_CODE_IS_NULL,
                Constants.MST,
                ExceptionConstants.TAX_AUTHORITY_CODE_IS_NULL_VI
            );
        }
        String regex = "M[1,2,5]{1}-2[0-9]{1}-[A-Z0-9]{5}-[\\d]{11}";
        return taxAuthorityCode.matches(regex);
    }

    public static JsonNode readBackupData(String urlBackup) {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode jsonNode = null;
        try {
            URL url = new URL(urlBackup);
            jsonNode = mapper.readValue(url, JsonNode.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return jsonNode;
    }

    public static void validateInput(Validator customValidator, String entityName, Object... objects) {
        Set<String> violationSet = new HashSet<>();
        for (Object o : objects) {
            Set<ConstraintViolation<Object>> violations = customValidator.validate(o);
            if (violations.isEmpty()) continue;
            String messages = violations.stream().reduce("", (acc, ele) -> acc + ", " + ele.getMessage(), String::concat);
            violationSet.add(messages);
        }
        if (!violationSet.isEmpty()) {
            String errorMessage = String.join(",", violationSet);
            errorMessage = errorMessage.substring(2);
            throw new RequestAlertException(
                ExceptionConstants.BAD_REQUEST_VI,
                entityName,
                ExceptionConstants.BAD_REQUEST_MULTIPLE,
                errorMessage
            );
            //            List<ErrorMessageRequest> errorMessageRequests = Arrays.asList(errorMessage);

        }
    }

    public static void checkCustomerTaxCode(String taxCode) {
        if (!taxCode.matches(RegexConstants.CUSTOMER_TAX_CODE_REGEX)) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_TAX_CODE_INVALID_CODE
            );
        }
    }

    public static Integer getCompanyIdContext(SecurityContext context) {
        if (!Strings.isNullOrEmpty(context.getAuthentication().getCredentials().toString())) {
            String[] chunks = context.getAuthentication().getCredentials().toString().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            try {
                ObjectMapper mapper = new ObjectMapper();
                JwtDTO jwtDTO = mapper.readValue(payload, JwtDTO.class);
                return jwtDTO.getCompanyId();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static ZonedDateTime convertStringToDateTime(String dateTime, String pattern) {
        try {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            return LocalDate.parse(dateTime, dateTimeFormatter).atStartOfDay(ZoneId.systemDefault());
        } catch (Exception ex) {
            throw new BadRequestAlertException(
                ExceptionConstants.DATE_TIME_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.DATE_TIME_INVALID_VI
            );
        }
    }

    public static ZonedDateTime convertStringToZoneDateTime(String dateTime, String pattern) {
        if (!Strings.isNullOrEmpty(dateTime)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
                return ZonedDateTime.parse(dateTime.replace("/", "-"), formatter);
            } catch (Exception ex) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DATE_TIME_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DATE_TIME_INVALID
                );
            }
        }
        return null;
    }

    public static void checkStartAndEndDate(ZonedDateTime startDate, ZonedDateTime endDate) {
        Long start = startDate.toInstant().toEpochMilli();
        Long end = endDate.toInstant().toEpochMilli();
        if (start.compareTo(end) > 0) {
            throw new InternalServerException(
                ExceptionConstants.START_DATE_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.START_DATE_INVALID_CODE
            );
        }
    }

    public static String md5(String rawData) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(rawData.getBytes());
        return DatatypeConverter.printHexBinary(md.digest()).toLowerCase();
    }

    public static String unAccent(String s) { // bỏ dấu chuỗi
        if (!Strings.isNullOrEmpty(s)) {
            String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            // return pattern.matcher(temp).replaceAll("");
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return "";
    }

    public static List<Map<String, String>> readDataFromExcel(MultipartFile file) throws IOException {
        List<Map<String, String>> dataList = new ArrayList<>();
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        Sheet sheet = workbook.getSheetAt(0);
        Row headerRow = sheet.getRow(0);

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            Map<String, String> data = new HashMap<>();

            for (int j = 0; j < row.getLastCellNum(); j++) {
                Cell cell = row.getCell(j);
                if (headerRow.getCell(j) != null && cell != null) {
                    data.put(headerRow.getCell(j).getStringCellValue(), cell.toString());
                }
            }

            dataList.add(data);
        }

        workbook.close();

        return dataList;
    }

    public static String convertDataToJson(List<Map<String, String>> dataList) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(dataList);
    }

    public static List<String> getInvoiceConfigCodes() {
        List<String> codes = Arrays.asList(
            EasyInvoiceConstants.EASYINVOICE_URL,
            EasyInvoiceConstants.EASYINVOICE_LOOKUP,
            EasyInvoiceConstants.EASYINVOICE_ACCOUNT,
            EasyInvoiceConstants.EASYINVOICE_PASS
        );
        return codes;
    }

    public static List<String> getEBConfigCodes() {
        return Arrays.asList(
            EasyInvoiceConstants.EB88_COM_ID,
            EasyInvoiceConstants.EB88_DEFAULT_USER,
            EasyInvoiceConstants.EB88_REPOSITORY_ID
        );
    }

    public static List<String> getConfigDefaultCodes() {
        return Arrays.asList(
            EasyBookConstants.EB88_GROUP_NAME,
            EasyBookConstants.EB88_DEFAULT_USER,
            EasyInvoiceConstants.EASYINVOICE_URL,
            EasyInvoiceConstants.INVOICE_TYPE,
            EasyInvoiceConstants.INVOICE_PATTERN,
            EasyInvoiceConstants.INVOICE_METHOD,
            Constants.OVER_STOCK_CODE,
            Constants.IS_BUYER_CODE,
            Constants.INV_DYNAMIC_DISCOUNT_NAME_CODE,
            Constants.TAX_REDUCTION_CODE,
            Constants.VOUCHER_APPLY_CODE,
            Constants.COMBINE_VOUCHER_APPLY_CODE,
            Constants.PUSH_VOUCHER_DISCOUNT_INVOICE_CODE,
            EasyInvoiceConstants.TYPE_DISCOUNT,
            EasyInvoiceConstants.ROUND_SCALE_AMOUNT,
            EasyInvoiceConstants.ROUND_SCALE_UNIT_PRICE,
            EasyInvoiceConstants.ROUND_SCALE_QUANTITY,
            EasyInvoiceConstants.DISCOUNT_VAT,
            Constants.BUSINESS_TYPE,
            EasyInvoiceConstants.DISPLAY_CONFIG
        );
    }

    public static void checkDateTime(String dateTime, String pattern) {
        if (!Strings.isNullOrEmpty(dateTime)) {
            try {
                SimpleDateFormat formatter = new SimpleDateFormat(pattern);
                formatter.setLenient(false);
                formatter.parse(dateTime);
            } catch (Exception ex) {
                throw new BadRequestAlertException(
                    ExceptionConstants.DATE_TIME_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.DATE_TIME_INVALID
                );
            }
        }
    }

    // check toDate for api report
    public static String checkToDate(String toDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
        try {
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(toDate);
            Date dateNow = new Date();
            if (date.getTime() > dateNow.getTime()) {
                return formatter.format(ZonedDateTime.now());
            }
            return toDate;
        } catch (ParseException exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_VI,
                ENTITY_NAME,
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_CODE
            );
        }
    }

    public static void checkFromDate(String fromDate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        try {
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(fromDate);
            Date dateNow = new Date();
            if (date.getTime() < dateFormat.parse(dateFormat.format(dateNow)).getTime()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.LOYALTY_CARD_START_DATE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.LOYALTY_CARD_START_DATE_INVALID
                );
            }
        } catch (ParseException exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_VI,
                ENTITY_NAME,
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_CODE
            );
        }
    }

    public static void checkEmail(String email) {
        if (!Strings.isNullOrEmpty(email)) {
            if (!email.matches(Constants.PATTERN_MAIL)) {
                throw new BadRequestAlertException(ExceptionConstants.INVALID_EMAIL_VI, ENTITY_NAME, ExceptionConstants.INVALID_EMAIL);
            }
        }
    }

    public static String formatBigDecimal(BigDecimal value) {
        String valueString = String.valueOf(value);
        String[] values = valueString.split("\\.");
        if (values.length > 1) {
            String valueIntegerPart = values[0];
            String valueDecimalPart = values[1];
            for (int i = valueDecimalPart.length() - 1; i >= 0; i--) {
                if (valueDecimalPart.charAt(i) == '0') {
                    valueDecimalPart = valueDecimalPart.substring(0, i);
                }
            }
            if (Strings.isNullOrEmpty(valueDecimalPart)) {
                return valueIntegerPart;
            }
            return valueIntegerPart + "." + valueDecimalPart;
        }
        return value.toString();
    }

    public static String formatMoney(BigDecimal money) {
        DecimalFormat decimalFormat = new DecimalFormat("#,###");
        return decimalFormat.format(money);
    }

    public static String normalizedName(List<String> input) {
        StringBuilder result = new StringBuilder();
        if (!input.isEmpty()) {
            for (int i = 0; i < input.size(); i++) {
                String appendString = input.get(i);
                if (!Strings.isNullOrEmpty(appendString)) {
                    if (i > 0) {
                        result.append("|");
                    }
                    result.append(unAccent(appendString));
                }
            }
        }
        return result.toString().trim().replace(" ", "").toLowerCase();
    }

    public static String getRawValueExcel(CellValue cellValue) {
        Object result = null;
        if (cellValue != null) {
            switch (cellValue.getCellType()) {
                case BOOLEAN:
                    result = cellValue.getBooleanValue();
                    break;
                case NUMERIC:
                    result = cellValue.getNumberValue();
                    break;
                case STRING:
                    result = cellValue.getStringValue().trim();
                    break;
                case FORMULA:
                    result = cellValue.getNumberValue();
                    break;
            }
        }
        if (result != null) {
            return result.toString().replaceAll(String.valueOf((char) 160), "");
        }
        return null;
    }

    public static List<ProductUnit> createUnitDefault(Integer companyId) {
        ObjectMapper objectMapper = new ObjectMapper();
        ProductUnit[] data = null;
        try {
            data = objectMapper.readValue(Constants.PRODUCT_UNIT_JSON, ProductUnit[].class);
        } catch (JsonProcessingException e) {
            throw new InternalServerException(
                ExceptionConstants.PRODUCT_UNIT_SAVE_ALL_ERROR_VI,
                ENTITY_NAME,
                ExceptionConstants.PRODUCT_UNIT_SAVE_ALL_ERROR
            );
        }
        List<ProductUnit> productUnits = new ArrayList<>();
        for (ProductUnit product : data) {
            product.setComId(companyId);
            productUnits.add(product);
        }
        objectMapper.disable(SerializationFeature.INDENT_OUTPUT);
        return productUnits;
    }

    public static List<PrintConfig> createPrintConfig(User user, Integer companyId, String companyName) {
        ObjectMapper objectMapper = new ObjectMapper();
        PrintConfig[] data = null;
        try {
            data = objectMapper.readValue(Constants.CONFIG_VALUE, PrintConfig[].class);
            List<PrintConfig> printConfigs = Arrays.asList(data);
            for (PrintConfig printConfig : printConfigs) {
                printConfig.setComId(companyId);
                if (printConfig.getCode().equals("StoreName")) {
                    printConfig.setName(companyName);
                }
                if (printConfig.getCode().equals("PhoneNumber")) {
                    printConfig.setName("Hotline: " + user.getPhoneNumber());
                }
                if (printConfig.getCode().equals("StoreAddress")) {
                    printConfig.setName(user.getAddress());
                }
                if (printConfig.getCode().equals("CustomerTax")) {
                    printConfig.setName(user.getTaxCode());
                }
            }
            return printConfigs;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerException(
                ExceptionConstants.PRINT_CONFIG_SAVE_ALL_VI,
                ENTITY_NAME,
                ExceptionConstants.PRINT_CONFIG_SAVE_ALL
            );
        }
    }

    public static List<PrintTemplate> createPrintTemplateDefault(List<PrintTemplate> printTemplates, Integer comId) {
        List<PrintTemplate> result = new ArrayList<>();
        for (PrintTemplate printTemplate : printTemplates) {
            PrintTemplate template = new PrintTemplate();
            BeanUtils.copyProperties(printTemplate, template);
            template.setId(null);
            template.setComId(comId);
            template.setIsDefault(Boolean.FALSE);
            result.add(template);
        }
        return result;
    }

    public static Area createAreaDefault(Integer companyId) {
        Area area = new Area();
        area.setName(CommonConstants.AREA_NAME_CREATE_DEFAULT);
        area.setComId(companyId);
        area.setNormalizedName(Common.normalizedName(Arrays.asList(area.getName())));
        return area;
    }

    public static Customer createCustomerDefault(Integer companyId) {
        Customer customer = new Customer();
        customer.setComId(companyId);
        customer.setName(CommonConstants.CUSTOMER_NAME_CREATE_DEFAULT);
        customer.setActive(CustomerConstants.Active.TRUE);
        customer.setType(CustomerConstants.Type.CUSTOMER_AND_SUPPLIER);
        customer.setNormalizedName(Common.normalizedName(Arrays.asList(CommonConstants.CUSTOMER_NAME_CREATE_DEFAULT)));
        return customer;
    }

    public static Product createProductDefault(Integer companyId, String name) {
        Product product = new Product();
        product.setComId(companyId);
        product.setName(name);
        product.setActive(ProductConstant.Active.ACTIVE_TRUE);
        product.setNormalizedName(Common.normalizedName(Arrays.asList(product.getName())));
        return product;
    }

    public static ProductProductUnit createProductProductUnitDefault(Integer companyId, Integer productId) {
        ProductProductUnit productProductUnit = new ProductProductUnit();
        productProductUnit.setComId(companyId);
        productProductUnit.setProductId(productId);
        productProductUnit.setDirectSale(Boolean.TRUE);
        productProductUnit.setOnHand(BigDecimal.ZERO);
        productProductUnit.setFormula(false);
        productProductUnit.setIsPrimary(true);
        productProductUnit.setConvertRate(BigDecimal.ONE);
        return productProductUnit;
    }

    public static List<BusinessType> createBusinessTypeDefault(Integer companyId) {
        ObjectMapper objectMapper = new ObjectMapper();
        vn.softdreams.easypos.domain.BusinessType[] data = null;
        try {
            data = objectMapper.readValue(Constants.BUSINESS_TYPE_JSON, vn.softdreams.easypos.domain.BusinessType[].class);
            List<vn.softdreams.easypos.domain.BusinessType> businessTypes = new ArrayList<>();
            for (BusinessType businessType : data) {
                businessType.setComId(companyId);
                businessTypes.add(businessType);
            }
            return businessTypes;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            throw new InternalServerException(
                ExceptionConstants.BUSINESS_TYPE_SAVE_ALL_ERROR_VI,
                ENTITY_NAME,
                ExceptionConstants.BUSINESS_TYPE_SAVE_ALL_ERROR
            );
        }
    }

    public static Workbook readFileExcelTemplate(String filePath) {
        try {
            URL url = new URL(filePath);
            InputStream inputStream = url.openStream();
            Workbook workbook = WorkbookFactory.create(inputStream);

            inputStream.close();
            return workbook;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] writeWorkbookToByte(Workbook workbook) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            workbook.write(bos);
            byte[] excelBytes = bos.toByteArray();
            bos.close();
            return excelBytes;
        } catch (IOException exception) {
            exception.printStackTrace();
        }
        return null;
    }

    public static CellStyle highLightErrorCell(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.WHITE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBottomBorderColor(IndexedColors.RED.getIndex());
        style.setBorderRight(BorderStyle.THIN);
        style.setRightBorderColor(IndexedColors.RED.getIndex());
        style.setBorderTop(BorderStyle.THIN);
        style.setTopBorderColor(IndexedColors.RED.getIndex());
        style.setBorderLeft(BorderStyle.THIN);
        style.setLeftBorderColor(IndexedColors.RED.getIndex());

        Font font = workbook.createFont();
        font.setFontName("Arial");
        font.setColor(HSSFColor.HSSFColorPredefined.RED.getIndex());
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        return style;
    }

    public static Comment setCommentErrorCell(Workbook workbook, Sheet sheet, Cell cell, String message) {
        CreationHelper creationHelper = workbook.getCreationHelper();
        Drawing drawing = sheet.createDrawingPatriarch();
        ClientAnchor anchor = creationHelper.createClientAnchor();
        anchor.setCol1(cell.getColumnIndex());
        anchor.setCol2(cell.getColumnIndex() + 1);
        anchor.setRow1(cell.getRowIndex());
        anchor.setRow2(cell.getRowIndex() + 5);

        Comment comment = drawing.createCellComment(anchor);
        RichTextString commentText = creationHelper.createRichTextString(message);
        comment.setString(commentText);

        return comment;
    }

    public static int getMaxRowNumberImportExcel(int rowNumber) {
        int result = CommonConstants.MAX_ROW_NUMBER_IMPORT_EXCEL;
        if (rowNumber < result) {
            result = rowNumber;
        }
        return result;
    }

    public static boolean checkTimeFromTo(String from, String to, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        try {
            Date dateFrom = sdf.parse(from);
            Date dateTo = sdf.parse(to);
            return dateFrom.compareTo(dateTo) < 0;
        } catch (ParseException e) {
            // Handle exception (although in this case, it should not occur as we have already validated the input)
            e.printStackTrace();
            return false;
        }
    }

    public static ZonedDateTime addTimeToDateTime(ZonedDateTime dateTime, String timeStr) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(Constants.ZONED_TIME_FORMAT);
        LocalTime additionalTime = LocalTime.parse(timeStr, timeFormatter);

        return dateTime.plusHours(additionalTime.getHour()).plusMinutes(additionalTime.getMinute());
    }

    public static Integer getDateDiff(String fromDate, String toDate, String pattern) {
        DateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        Date date1;
        Date date2;

        try {
            date1 = simpleDateFormat.parse(fromDate);
            date2 = simpleDateFormat.parse(toDate);
            long getDiff = date2.getTime() - date1.getTime();
            long getDaysDiff = getDiff / (24 * 60 * 60 * 1000);
            return (int) getDaysDiff;
        } catch (Exception e) {
            e.printStackTrace();
        }
        throw new BadRequestAlertException(ExceptionConstants.DATE_TIME_INVALID_VI, ENTITY_NAME, ExceptionConstants.DATE_TIME_INVALID);
    }

    public static String checkBirthdate(String birthdate, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
        try {
            dateFormat.setLenient(false);
            Date date = dateFormat.parse(birthdate);
            Date dateNow = new Date();
            if (date.getTime() > dateNow.getTime()) {
                throw new BadRequestAlertException(
                    ExceptionConstants.CUSTOMER_BIRTH_DATE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.CUSTOMER_BIRTH_DATE_INVALID
                );
            }
            return birthdate;
        } catch (ParseException exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.CUSTOMER_BIRTH_DATE_INVALID_VI,
                ENTITY_NAME,
                ExceptionConstants.CUSTOMER_BIRTH_DATE_INVALID
            );
        }
    }

    public static byte[] convertHTmlToImage(CustomPdf customPdf) {
        String htmlCode = customPdf.getHtml();

        try {
            // Tạo một BufferedImage
            int width = 800;
            int height = 600;
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphics = image.createGraphics();

            // Render HTML từ template Thymeleaf vào BufferedImage
            org.thymeleaf.context.Context context = new org.thymeleaf.context.Context();
            context.setVariable("htmlCode", htmlCode);
            TemplateEngine templateEngine = new TemplateEngine();
            String processedHtml = templateEngine.process("image-template", context);
            graphics.drawString(processedHtml, 0, 0);

            // Chuyển đổi BufferedImage thành byte array
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "png", byteArrayOutputStream);
            byte[] imageBytes = byteArrayOutputStream.toByteArray();

            return imageBytes;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    static byte[] convertImageToByteArray(BufferedImage image) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image, "png", outputStream);
        return outputStream.toByteArray();
    }

    public static byte[] convertHtmlToPdf(CustomPdf customPdf) {
        if (customPdf.getType().equals("PDF")) {
            return convertHtmlToPdfCustom(customPdf);
        } else {
            return null;
        }
    }

    public static byte[] convertHtmlToPdfCustom(CustomPdf customPdf) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfDocument pdf = new PdfDocument(new PdfWriter(outputStream));
            pdf.setTagged();
            Document document = new Document(pdf);
            switch (customPdf.getPageSize()) {
                case "A3":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A3));
                        break;
                    }
                case "A4":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A4));
                        break;
                    }
                case "A5":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A5));
                        break;
                    }
                case "A6":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A6));
                        break;
                    }
                case "A7":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A7));
                        break;
                    }
                case "A8":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A8));
                        break;
                    }
                case "A9":
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A9));
                        break;
                    }
                case "CUSTOM":
                    {
                        PageSize customPageSize = new PageSize(customPdf.getWidth(), customPdf.getHeight());

                        document.getPdfDocument().setDefaultPageSize(customPageSize);
                        break;
                    }
                default:
                    {
                        document.getPdfDocument().setDefaultPageSize(new PageSize(PageSize.A4));
                    }
            }

            // Set margins (left, right, top, bottom)
            document.setMargins(
                customPdf.getMarginTop(),
                customPdf.getMarginRight(),
                customPdf.getMarginBottom(),
                customPdf.getMarginLeft()
            );

            ConverterProperties properties = new ConverterProperties();
            properties.setBaseUri(""); // No base URI since you're passing HTML directly
            List<IElement> elements = HtmlConverter.convertToElements(new ByteArrayInputStream(customPdf.getHtml().getBytes()), properties);

            for (IElement element : elements) {
                document.add((IBlockElement) element);
            }

            document.close();

            return outputStream.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JasperReport getCompiledFile(String jasperPath, String jrxmlPath) throws JRException {
        File reportFile = null;
        try {
            reportFile = ResourceUtils.getFile("src/main/webapp/content/report/" + jasperPath);
        } catch (FileNotFoundException e) {
            for (StackTraceElement ex : e.getStackTrace()) {
                log.error(ex.toString());
            }
            e.printStackTrace();
        }
        // If compiled file is not found, then compile XML template
        //		if (reportFile == null || !reportFile.exists()) {
        //			JasperCompileManager.compileReportToFile(RESOURSE + jrxmlPath, RESOURSE + jasperPath);
        //		}
        BufferedInputStream bufferedInputStream = null;
        try {
            bufferedInputStream = new BufferedInputStream(new FileInputStream(reportFile.getPath()));
        } catch (FileNotFoundException e) {
            log.error("FileNotFoundException " + e.getMessage());
        }

        return (JasperReport) JRLoader.loadObject(bufferedInputStream);
    }

    public static byte[] generateReportPDF(List<?> dataSource, Map parameters, JasperReport jasperReport) throws JRException {
        byte[] bytes = null;
        JRBeanCollectionDataSource beanDataSource = new JRBeanCollectionDataSource(dataSource);
        bytes = JasperRunManager.runReportToPdf(jasperReport, parameters, beanDataSource);

        return bytes;
    }
}
