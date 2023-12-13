package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.dto.bill.BillStatsResponse;
import vn.softdreams.easypos.dto.bill.BillStatsResult;
import vn.softdreams.easypos.dto.invoice.InvoiceStatsResponse;
import vn.softdreams.easypos.dto.invoice.InvoiceStatsResult;
import vn.softdreams.easypos.repository.BillRepository;
import vn.softdreams.easypos.repository.InvoiceRepository;
import vn.softdreams.easypos.service.StatisticManagementService;
import vn.softdreams.easypos.service.UserService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
@Transactional
public class StatisticManagementServiceImpl implements StatisticManagementService {

    private static final String ENTITY_NAME = "StatisticManagementServiceImpl";

    private final Logger log = LoggerFactory.getLogger(RsInoutWardManagementServiceImpl.class);

    private final SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.ZONED_DATE_FORMAT);
    private final InvoiceRepository invoiceRepository;
    private final BillRepository billRepository;

    private final UserService userService;

    public StatisticManagementServiceImpl(InvoiceRepository invoiceRepository, BillRepository billRepository, UserService userService) {
        this.invoiceRepository = invoiceRepository;
        this.billRepository = billRepository;
        this.userService = userService;
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getBillStats(Integer comId, String fromDate, String toDate) {
        log.info(ENTITY_NAME + "_getBillStats: REST to get bill stats with comId: {}", comId);
        userService.getUserWithAuthorities(comId);
        checkFromDate(fromDate);

        BillStatsResult billStatsResult = billRepository.getBillStats(comId, fromDate, checkToDate(toDate));
        BillStatsResponse billStatsResponse = new BillStatsResponse(
            comId,
            fromDate,
            toDate,
            billStatsResult.getProcessingCount(),
            billStatsResult.getAllCount(),
            ZonedDateTime.now()
        );
        log.debug(ENTITY_NAME + "_getBillStats: " + ResultConstants.GET_BILL_STATS);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_BILL_STATS, true, billStatsResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public ResultDTO getInvoiceStats(Integer comId, String fromDate, String toDate) {
        log.info(ENTITY_NAME + "_getInvoiceStats: REST to get invoice stats with comId: {}", comId);
        userService.getUserWithAuthorities(comId);
        checkFromDate(fromDate);

        InvoiceStatsResult invoiceStatsResult = invoiceRepository.getInvoiceStats(comId, fromDate, checkToDate(toDate));
        InvoiceStatsResponse invoiceStatsResponse = new InvoiceStatsResponse(
            comId,
            fromDate,
            toDate,
            invoiceStatsResult.getNewCount(),
            invoiceStatsResult.getProcessingCount(),
            invoiceStatsResult.getDoneCount(),
            invoiceStatsResult.getAllCount(),
            ZonedDateTime.now()
        );
        log.debug(ENTITY_NAME + "_getInvoiceStats: " + ResultConstants.GET_INVOICE_STATS);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.GET_INVOICE_STATS, true, invoiceStatsResponse);
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
