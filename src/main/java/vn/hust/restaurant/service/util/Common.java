package vn.hust.restaurant.service.util;

import static org.hibernate.id.IdentifierGenerator.ENTITY_NAME;

import com.google.common.base.Strings;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.multipart.MultipartFile;
import vn.hust.restaurant.config.Constants;
import vn.hust.restaurant.web.rest.errors.BadRequestAlertException;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;
import vn.hust.restaurant.web.rest.errors.RequestAlertException;

public class Common {

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
        }

        return orderSql.toString();
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

    public static void setParams(Query query, Map<String, Object> params) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                if (obj.getValue() == null) query.setParameter(obj.getKey(), "");
                //                else if ((obj.getKey().equals("propertyParam") && !addParam));
                else query.setParameter(obj.getKey(), obj.getValue());
            }
        }
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

    public static String unAccent(String s) { // bỏ dấu chuỗi
        if (!Strings.isNullOrEmpty(s)) {
            String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
            Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
            // return pattern.matcher(temp).replaceAll("");
            return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replace("đ", "d");
        }
        return "";
    }

    /**
     * @param query
     * @param params
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
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
                query.setParameter(obj.getKey(), obj.getValue());
            }
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
    }

    public static void setParamsWithPageable(@NotNull Query query, Map<String, Object> params, @NotNull Pageable pageable) {
        if (params != null && !params.isEmpty()) {
            Set<Map.Entry<String, Object>> set = params.entrySet();
            for (Map.Entry<String, Object> obj : set) {
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

    public static String saveFile(MultipartFile fileItem, String[] fileFormats, String path, HttpServletRequest httpRequest) {
        //        String[] otherList = new String[] { "png", "jpg", "jpeg", "gif" };
        // Lưu ảnh dưới dạng (MaCMND-TenAnh)
        String pathDomain = "" + ImagePathConstants.PATH_FILE;
        path = path + "/";
        if (fileItem != null) {
            String fileName = fileItem.getOriginalFilename();
            Common.checkFileInvalid(fileName.substring(fileName.lastIndexOf(".")), fileFormats);
            String url = "";
            try {
                fileName =
                    Util.writeBytesToFile(
                        fileItem.getBytes(),
                        ImagePathConstants.ROOT_PATH_WINDOWS + path,
                        fileName,
                        true,
                        Constants.IMAGE_SIZE
                    );
                url = pathDomain + path + fileName;
                return url;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return "";
        }
    }

    public static void checkFileInvalid(String fileName, String[] otherList) {
        //        Check xem có đúng định dạng file không
        boolean isImgage = Arrays.asList(otherList).contains(fileName);
        if (isImgage) {
            throw new BadRequestAlertException("error", "account", "fileInvalid");
        }
    }

    public static LocalDateTime convertStringToZoneDateTime(String dateTime, String pattern) {
        if (!Strings.isNullOrEmpty(dateTime)) {
            try {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.systemDefault());
                return LocalDateTime.parse(dateTime, formatter);
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

    public static BigDecimal roundMoney(BigDecimal money, int value) {
        return money.setScale(value, RoundingMode.HALF_UP);
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
            sqlBuilder.append(" and " + columnName);
            if ((!Strings.isNullOrEmpty(fromDate) && !Strings.isNullOrEmpty(toDate))) {
                sqlBuilder.append(">= :from" + param + " and " + columnName + " < DATE_ADD(:to" + param + ",INTERVAL 1 DAY) ");
                params.put("from" + param, fromDate);
                params.put("to" + param, toDate);
            } else if (!Strings.isNullOrEmpty(fromDate)) {
                sqlBuilder.append(" >= :from" + param + " ");
                params.put("from" + param, fromDate);
            } else if (!Strings.isNullOrEmpty(toDate)) {
                sqlBuilder.append(" <= :to" + param + " ");
                params.put("to" + param, toDate);
            }
        }
    }
}
