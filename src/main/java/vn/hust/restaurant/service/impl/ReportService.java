package vn.hust.restaurant.service.impl;

import static vn.hust.restaurant.constants.StatisticConstants.BY_MONTH;

import com.google.common.base.Strings;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.restaurant.config.Constants;
import vn.hust.restaurant.constants.ResultConstants;
import vn.hust.restaurant.constants.StatisticConstants;
import vn.hust.restaurant.domain.User;
import vn.hust.restaurant.repository.BillRepository;
import vn.hust.restaurant.service.dto.ResultDTO;
import vn.hust.restaurant.service.dto.RevenueCommonStatsResponse;
import vn.hust.restaurant.service.dto.bill.BillStatItem;
import vn.hust.restaurant.service.dto.bill.BillStatsResponse;
import vn.hust.restaurant.service.dto.bill.BillStatsResult;
import vn.hust.restaurant.service.dto.bill.RevenueByMonth;
import vn.hust.restaurant.service.util.Common;
import vn.hust.restaurant.service.util.Util;
import vn.hust.restaurant.web.rest.errors.BadRequestAlertException;
import vn.hust.restaurant.web.rest.errors.ExceptionConstants;

@Service
public class ReportService {

    final String BYMONTH = "yyyy/MM";
    final String BYDAY = "yyyy/MM/dd";
    final String BYHOUR = "yyyy/MM/dd HH";
    private final UserService userService;

    private final BillRepository billRepository;

    private static final String ENTITY_NAME = "StatisticManagementServiceImpl";

    private final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ZONED_DATE_FORMAT);

    public ReportService(UserService userService, BillRepository billRepository) {
        this.userService = userService;
        this.billRepository = billRepository;
    }

    public ResultDTO getBillRevenue(
        Integer comId,
        String fromDate,
        String toDate,
        String fromHour,
        String toHour,
        Integer type,
        Pageable pageable,
        Boolean isPaging,
        Boolean isChart
    ) throws ParseException {
        User user = userService.getUserWithAuthorities();
        if (type == null) {
            type = BY_MONTH;
        }
        LocalDate date1 = LocalDate.parse(fromDate);
        LocalDate date2 = LocalDate.parse(toDate);

        long daysBetween = ChronoUnit.DAYS.between(date1, date2) + 1;
        if (daysBetween == 0) {
            daysBetween = 1;
        }
        if (type.equals(StatisticConstants.BY_DAY) && daysBetween > 60 && isChart != null && isChart) {
            type = BY_MONTH;
        }
        int scale = 0;
        String format = getFormat(fromHour, toHour, type);
        List<BillStatItem> revenues = billRepository.getBillMoney(user.getCompanyId(), fromDate, toDate, format);
        List<RevenueByMonth> bills = processData(revenues);

        List<RevenueByMonth> revenueByMonthList = getAllRevenueEvenZero(bills, type, user, fromDate, toDate, fromHour, toHour);
        int count = revenueByMonthList.size();
        RevenueCommonStatsResponse response = new RevenueCommonStatsResponse();
        response.setFromDate(fromDate);
        response.setToDate(toDate);
        response.setComId(comId);
        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalProfit = BigDecimal.ZERO;
        List<RevenueCommonStatsResponse.Detail> list = new ArrayList<>();
        Map<String, BigDecimal> averageRevenueMap = new LinkedHashMap<>();
        Map<String, BigDecimal> averageProfitMap = new LinkedHashMap<>();
        if (!type.equals(StatisticConstants.BY_HOUR)) {
            for (RevenueByMonth revenue : revenueByMonthList) {
                totalRevenue = totalRevenue.add(revenue.getRevenue());
                totalProfit = totalProfit.add(revenue.getProfit());
                RevenueCommonStatsResponse.Detail detail = new RevenueCommonStatsResponse.Detail();
                setDetail(detail, type, revenue);
                list.add(detail);
            }
            if (type.equals(BY_MONTH) && !list.isEmpty()) {
                RevenueCommonStatsResponse.Detail detail = list.get(0);
                detail.setFromDate(fromDate);
                list.set(0, detail);

                RevenueCommonStatsResponse.Detail lastDetail = list.get(list.size() - 1);
                lastDetail.setToDate(toDate);
                list.set(list.size() - 1, lastDetail);
            }
        } else {
            for (RevenueByMonth revenue : revenueByMonthList) {
                String key = revenue.getMonth().substring(revenue.getMonth().lastIndexOf(" ") + 1);
                if (averageRevenueMap.containsKey(key)) {
                    averageRevenueMap.put(key, averageRevenueMap.get(key).add(revenue.getRevenue()));
                } else {
                    averageRevenueMap.put(key, revenue.getRevenue());
                }
                if (averageProfitMap.containsKey(key)) {
                    averageProfitMap.put(key, averageProfitMap.get(key).add(revenue.getProfit()));
                } else {
                    averageProfitMap.put(key, revenue.getProfit());
                }
            }
            count = averageRevenueMap.size();
            for (Map.Entry<String, BigDecimal> entry : averageRevenueMap.entrySet()) {
                RevenueCommonStatsResponse.Detail detail = new RevenueCommonStatsResponse.Detail();
                setDetailAverage(detail, entry.getKey(), entry.getValue(), averageProfitMap.get(entry.getKey()), daysBetween);
                list.add(detail);
                totalRevenue = totalRevenue.add(detail.getRevenue());
                totalProfit = totalProfit.add(detail.getProfit());
                detail.setRevenue(Common.roundMoney(detail.getRevenue(), scale));
                detail.setProfit(Common.roundMoney(detail.getProfit(), scale));
            }
        }
        if (isPaging != null && isPaging && pageable != null) {
            int start = Math.min(list.size(), pageable.getPageNumber() * pageable.getPageSize());
            int end = Math.min((start + pageable.getPageSize()), list.size());
            list = list.subList(start, end);
        }

        response.setDetail(list);
        response.setRevenue(Common.roundMoney(totalRevenue, scale));
        response.setProfit(Common.roundMoney(totalProfit, scale));
        response.setCreateTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern(Constants.ZONED_DATE_TIME_FORMAT)));
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_BILL_REVENUE, true, response, count);
    }

    private String getFormat(String fromHour, String toHour, Integer type) {
        String format = "";
        if (Objects.equals(type, BY_MONTH)) {
            format = BYMONTH;
        } else if (Objects.equals(type, StatisticConstants.BY_DAY)) {
            format = BYDAY;
        } else if (Objects.equals(type, StatisticConstants.BY_HOUR)) {
            if (
                (!Strings.isNullOrEmpty(fromHour) && Strings.isNullOrEmpty(toHour)) ||
                (!Strings.isNullOrEmpty(toHour) && Strings.isNullOrEmpty(fromHour))
            ) {
                throw new BadRequestAlertException(
                    ExceptionConstants.HOUR_REVENUE_DATE_NOT_NULL_VI,
                    ExceptionConstants.HOUR_REVENUE_DATE_NOT_NULL_VI,
                    ExceptionConstants.HOUR_REVENUE_DATE_NOT_NULL
                );
            }
            format = BYHOUR;
        }
        return format;
    }

    private List<BillStatItem> processExpense(List<BillStatItem> expenses, List<BillStatItem> cancelExpenses) {
        List<BillStatItem> result = new ArrayList<>();
        Map<String, BillStatItem> expenseMap = new HashMap<>();
        Map<String, BillStatItem> cancelMap = new HashMap<>();
        for (BillStatItem item : expenses) {
            expenseMap.put(item.getTime(), item);
        }
        for (BillStatItem item : cancelExpenses) {
            cancelMap.put(item.getTime(), item);
        }
        for (Map.Entry<String, BillStatItem> entry : expenseMap.entrySet()) {
            if (!cancelMap.containsKey(entry.getKey())) {
                result.add(entry.getValue());
            } else {
                BillStatItem item = entry.getValue();
                item.setMoney(item.getMoney().subtract(cancelMap.get(entry.getKey()).getMoney()));
                result.add(item);
                cancelMap.remove(entry.getKey());
            }
        }
        for (Map.Entry<String, BillStatItem> entry : cancelMap.entrySet()) {
            BillStatItem item = entry.getValue();
            item.setMoney(BigDecimal.ZERO.subtract(item.getMoney()));
            result.add(item);
        }
        result.sort(Comparator.comparing(BillStatItem::getTime));
        return result;
    }

    private List<RevenueByMonth> processData(List<BillStatItem> revenues) {
        List<RevenueByMonth> list = new ArrayList<>();
        int index = 0;
        for (int i = 0; i < revenues.size(); i++) {
            try {
                BillStatItem revenue = revenues.get(i);
                {
                    list.add(new RevenueByMonth(revenue.getTime(), revenue.getMoney(), revenue.getMoney()));
                    index++;
                }
            } catch (Exception e) {
                list.add(new RevenueByMonth(revenues.get(i).getTime(), revenues.get(i).getMoney(), revenues.get(i).getMoney()));
            }
        }
        return list;
    }

    private List<RevenueByMonth> getAllRevenueEvenZero(
        List<RevenueByMonth> bills,
        Integer type,
        User user,
        String fromDate,
        String toDate,
        String fromHour,
        String toHour
    ) throws ParseException {
        List<RevenueByMonth> revenueByMonthList = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        Calendar calendarToDate = Calendar.getInstance();
        List<String> monthList = new ArrayList<>();
        SimpleDateFormat dateFormat = null;
        if (type.equals(StatisticConstants.BY_MONTH)) {
            dateFormat = new SimpleDateFormat(BYMONTH);
        } else if (type.equals(StatisticConstants.BY_DAY)) {
            dateFormat = new SimpleDateFormat(BYDAY);
        } else if (type.equals(StatisticConstants.BY_HOUR)) {
            dateFormat = new SimpleDateFormat(BYMONTH);
        }
        calendar.setTime(new SimpleDateFormat(Constants.ZONED_DATE_FORMAT).parse(fromDate));
        calendarToDate.setTime(new SimpleDateFormat(Constants.ZONED_DATE_FORMAT).parse(toDate));
        while (calendar.getTime().before(new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT).parse(toDate + " 23:59:59"))) {
            String monthString = dateFormat.format(calendar.getTime());
            monthList.add(monthString);
            if (type.equals(StatisticConstants.BY_MONTH)) {
                calendar.add(Calendar.MONTH, 1);
            } else if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.add(Calendar.DATE, 1);
            } else if (type.equals(StatisticConstants.BY_HOUR)) {
                calendar.add(Calendar.HOUR, 1);
            }
        }
        List<String> monthRevenue = new ArrayList<>();
        for (RevenueByMonth revenue : bills) {
            monthRevenue.add(revenue.getMonth());
            revenueByMonthList.add(revenue);
        }
        for (String month : monthList) {
            if (!monthRevenue.contains(month)) {
                revenueByMonthList.add(
                    new RevenueByMonth(
                        month,
                        BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY),
                        BigDecimal.ZERO.setScale(6, RoundingMode.UNNECESSARY)
                    )
                );
            }
        }
        Collections.sort(revenueByMonthList);
        if (Objects.equals(type, StatisticConstants.BY_HOUR) && !Strings.isNullOrEmpty(fromHour) && !Strings.isNullOrEmpty(toHour)) {
            List<RevenueByMonth> result = new ArrayList<>();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(BYHOUR);
            LocalTime startTime = LocalTime.parse(fromHour, DateTimeFormatter.ofPattern(Constants.ZONED_TIME_FORMAT));
            LocalTime endTime = LocalTime.parse(toHour, DateTimeFormatter.ofPattern(Constants.ZONED_TIME_FORMAT));

            for (RevenueByMonth revenue : revenueByMonthList) {
                LocalDateTime dateTime = LocalDateTime.parse(revenue.getMonth(), formatter);
                LocalTime time = dateTime.toLocalTime();

                if (!time.isBefore(startTime) && time.isBefore(endTime)) {
                    result.add(revenue);
                }
            }
            return result;
        }
        return revenueByMonthList;
    }

    private void setDetail(RevenueCommonStatsResponse.Detail detail, Integer type, RevenueByMonth revenue) throws ParseException {
        if (type.equals(StatisticConstants.BY_MONTH)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.ZONED_DATE_FORMAT);
            String[] parts = revenue.getMonth().split("/");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);

            YearMonth yearMonth = YearMonth.of(year, month);
            LocalDateTime startDate = yearMonth.atDay(1).atTime(LocalTime.MIN);
            LocalDateTime endDate = yearMonth.atEndOfMonth().atTime(LocalTime.MAX);
            detail.setFromDate(startDate.format(formatter));
            detail.setToDate(endDate.format(formatter));
        } else if (type.equals(StatisticConstants.BY_DAY) || type.equals(StatisticConstants.BY_HOUR)) {
            SimpleDateFormat inputFormat;
            if (type.equals(StatisticConstants.BY_DAY)) {
                inputFormat = new SimpleDateFormat(Util.DATE_PATTERN_2);
            } else {
                inputFormat = new SimpleDateFormat("yyyy/MM/dd HH");
            }
            SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.ZONED_DATE_TIME_FORMAT);
            Date date = inputFormat.parse(revenue.getMonth());
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.set(Calendar.HOUR, 0);
            }
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            detail.setFromDate(outputFormat.format(calendar.getTime()));

            if (type.equals(StatisticConstants.BY_DAY)) {
                calendar.set(Calendar.HOUR, 23);
            }
            calendar.set(Calendar.MINUTE, 59);
            calendar.set(Calendar.SECOND, 59);
            detail.setToDate(outputFormat.format(calendar.getTime()));
        }
        detail.setRevenue(revenue.getRevenue().setScale(6, RoundingMode.HALF_UP));
        detail.setProfit(revenue.getProfit().setScale(6, RoundingMode.HALF_UP));
    }

    private void setDetailAverage(RevenueCommonStatsResponse.Detail detail, String time, BigDecimal revenue, BigDecimal profit, long days)
        throws ParseException {
        SimpleDateFormat inputFormat = new SimpleDateFormat("HH");
        SimpleDateFormat outputFormat = new SimpleDateFormat(Constants.ZONED_TIME_FORMAT2);
        Date date = inputFormat.parse(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        detail.setFromDate(outputFormat.format(calendar.getTime()));

        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        detail.setToDate(outputFormat.format(calendar.getTime()));

        detail.setRevenue(revenue.divide(BigDecimal.valueOf(days), RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP));
        detail.setProfit(profit.divide(BigDecimal.valueOf(days), RoundingMode.HALF_UP).setScale(6, RoundingMode.HALF_UP));
    }

    @Transactional(readOnly = true)
    public ResultDTO getBillStats(Integer comId, String fromDate, String toDate) {
        log.info(ENTITY_NAME + "_getBillStats: REST to get bill stats with comId: {}", comId);
        userService.getUserWithAuthorities();
        checkFromDate(fromDate);

        BillStatsResult billStatsResult = billRepository.getBillStats(comId, fromDate, checkToDate(toDate));
        BillStatsResponse billStatsResponse = new BillStatsResponse(
            comId,
            fromDate,
            toDate,
            billStatsResult.getProcessingCount(),
            billStatsResult.getAllCount(),
            LocalDateTime.now()
        );
        log.debug(ENTITY_NAME + "_getBillStats: " + ResultConstants.GET_BILL_STATS);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_BILL_STATS, true, billStatsResponse);
    }

    private void checkFromDate(String fromDate) {
        try {
            long months = ChronoUnit.MONTHS.between(LocalDate.parse(fromDate).withDayOfMonth(1), LocalDate.now().withDayOfMonth(1));
            if (months > 18) {
                throw new BadRequestAlertException(
                    ExceptionConstants.STATISTIC_FROM_DATE_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.STATISTIC_FROM_DATE_INVALID_CODE
                );
            }
        } catch (DateTimeParseException exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_VI,
                ENTITY_NAME,
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_CODE
            );
        }
    }

    private String checkToDate(String toDate) {
        try {
            Date date = dateFormat.parse(toDate);
            Date dateNow = new Date();
            if (date.after(dateNow)) {
                return dateFormat.format(dateNow);
            }
        } catch (ParseException exception) {
            throw new BadRequestAlertException(
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_VI,
                ENTITY_NAME,
                ExceptionConstants.STATISTIC_DATE_INVALID_FORMAT_CODE
            );
        }
        return toDate;
    }
}
