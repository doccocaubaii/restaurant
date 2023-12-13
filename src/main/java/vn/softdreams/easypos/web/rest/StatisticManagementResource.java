package vn.softdreams.easypos.web.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vn.softdreams.easypos.service.StatisticManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api")
public class StatisticManagementResource {

    private final StatisticManagementService statisticManagementService;

    public StatisticManagementResource(StatisticManagementService statisticManagementService) {
        this.statisticManagementService = statisticManagementService;
    }

    @GetMapping("/client/page/home/bill-common-stats")
    public ResponseEntity<ResultDTO> getBillCommonStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate
    ) {
        return new ResponseEntity<>(statisticManagementService.getBillStats(comId, fromDate, toDate), HttpStatus.OK);
    }

    @GetMapping("/client/page/home/invoice-common-stats")
    public ResponseEntity<ResultDTO> getInvoiceCommonStats(
        @NotNull @RequestParam Integer comId,
        @NotBlank @RequestParam String fromDate,
        @NotBlank @RequestParam String toDate
    ) {
        return new ResponseEntity<>(statisticManagementService.getInvoiceStats(comId, fromDate, toDate), HttpStatus.OK);
    }
    //    @GetMapping("/client/page/home/revenue-common-stats")
    //    public ResponseEntity<ResultDTO> getBillRevenue(
    //        @RequestParam Integer comId,
    //        @RequestParam String fromDate,
    //        @RequestParam String toDate,
    //        @RequestParam(required = false) Integer type
    //    ) throws ParseException {
    //        ResultDTO resultDTO = statisticManagementService.getBillRevenue(comId, fromDate, toDate, type);
    //        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    //    }
}
