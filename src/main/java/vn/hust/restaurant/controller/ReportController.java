package vn.hust.restaurant.controller;



import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.hust.restaurant.service.dto.ResultDTO;
import vn.hust.restaurant.service.impl.ReportService;

@RestController
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/client/page/home/revenue-common-stats")
    //    @CheckAuthorize(value = AuthoritiesConstants.Report.VIEW)
    public ResultDTO revenueCommonStats(
        @RequestParam Integer comId,
        @RequestParam String fromDate,
        @RequestParam String toDate,
        @RequestParam(required = false) String fromHour,
        @RequestParam(required = false) String toHour,
        @RequestParam(required = false) Integer type,
        @RequestParam(required = false) Boolean isChart,
        Pageable pageable,
        @RequestParam(required = false) Boolean isPaging
    ) throws Exception {
        return reportService.getBillRevenue(comId, fromDate, toDate, fromHour, toHour, type, pageable, isPaging, isChart);
    }

    @GetMapping("/client/page/home/bill-common-stats")
    public ResponseEntity<ResultDTO> getBillCommonStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate
    ) {
        return new ResponseEntity<>(reportService.getBillStats(comId, fromDate, toDate), HttpStatus.OK);
    }
}
