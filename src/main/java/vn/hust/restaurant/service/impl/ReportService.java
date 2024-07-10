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
import vn.hust.restaurant.service.dto.StaticResponse;
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
        String fromDate,
        String toDate
    ) throws ParseException {
        User user = userService.getUserWithAuthorities();
        LocalDate date1 = LocalDate.parse(fromDate);
        LocalDate date2 = LocalDate.parse(toDate);

        long daysBetween = ChronoUnit.DAYS.between(date1, date2) + 1;
        if (daysBetween == 0) {
            daysBetween = 1;
        }
        int scale = 0;
        List<BillStatItem> revenues = billRepository.getBillMoney(user.getCompanyId(), fromDate, toDate);
        List<BillStatItem> fullRevenues = new ArrayList<>();
        for (LocalDate date3 = date1; !date3.isAfter(date2); date3 = date3.plusDays(1)) {
            String date3Str = date3.toString();
            Optional<BillStatItem> existingItem = revenues.stream()
                .filter(item -> Objects.equals(item.getTime(), date3Str))
                .findFirst();

            if (existingItem.isPresent()) {
                fullRevenues.add(existingItem.get());
            } else {
                fullRevenues.add(new BillStatItem(date3Str, new BigDecimal("0")));
            }
        }
        List<BillStatItem>  pieChart = billRepository.getPieChart(user.getCompanyId(), fromDate, toDate);
        StaticResponse staticResponse = new StaticResponse(fullRevenues, pieChart);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_GET_BILL_REVENUE, true, staticResponse, 1);
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
