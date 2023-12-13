package vn.softdreams.easypos.web.rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.softdreams.easypos.integration.easybooks88.api.EB88ApiClient;
import vn.softdreams.easypos.integration.easybooks88.api.dto.*;
import vn.softdreams.easypos.service.impl.EmailServiceImpl;

@RestController
@RequestMapping(path = "/api/p")
public class EB88ApiResource {

    private final EB88ApiClient eb88ApiClient;
    private final EmailServiceImpl emailService;

    public EB88ApiResource(EB88ApiClient eb88ApiClient, EmailServiceImpl emailService) {
        this.eb88ApiClient = eb88ApiClient;
        this.emailService = emailService;
    }

    //    @GetMapping(path = "/inventory-report")
    //    public ResponseEntity<GetInventoryReportResponse[]> testInventoryReport(@RequestParam String fromDate, @RequestParam String toDate)
    //        throws Exception {
    //        GetInventoryReportResponse[] response = eb88ApiClient.getInventoryReport(fromDate, toDate);
    //        return ResponseEntity.ok(response);
    //    }

    //    @GetMapping(path = "/cashflow-report")
    //    public ResponseEntity<GetCashFlowReportResponse> testCashFlowReport(@RequestParam String fromDate, @RequestParam String toDate)
    //        throws Exception {
    //        GetCashFlowReportResponse response = eb88ApiClient.getCashFlowReport(fromDate, toDate);
    //        return ResponseEntity.ok(response);
    //    }

    @GetMapping(path = "/sales-report")
    public ResponseEntity<GetSalesReportResponse[]> testSalesReport(@RequestParam String fromDate, @RequestParam String toDate)
        throws Exception {
        GetSalesReportResponse[] response = eb88ApiClient.getSalesReport(fromDate, toDate);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/profit-report")
    public ResponseEntity<GetProfitReportResponse[]> testProfitReport(@RequestParam String fromDate, @RequestParam String toDate)
        throws Exception {
        GetProfitReportResponse[] response = eb88ApiClient.getProfitReport(fromDate, toDate);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/sales-detail-report")
    public ResponseEntity<GetSalesDetailReportResponse> testSalesReportDetail(@RequestBody GetSalesDetailReportRequest request)
        throws Exception {
        GetSalesDetailReportResponse response = eb88ApiClient.getSalesDetailReport(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/inventory-detail-report")
    public ResponseEntity<GetInventoryDetailReportResponse> testSalesReportDetail(@RequestBody GetInventoryDetailReportRequest request)
        throws Exception {
        GetInventoryDetailReportResponse response = eb88ApiClient.getInventoryDetailReport(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/send-mail/test")
    public ResponseEntity<String> testSendMail() {
        emailService.sendAccountCredentials("minhth@softdreams.vn", "minhth@softdreams.vn", "abc123");
        return ResponseEntity.ok("Sent");
    }
}
