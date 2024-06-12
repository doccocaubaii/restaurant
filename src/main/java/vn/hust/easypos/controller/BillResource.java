package vn.hust.easypos.controller;

import jakarta.validation.Validator;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import vn.hust.easypos.domain.Bill;
import vn.hust.easypos.domain.User;
import vn.hust.easypos.repository.UserRepository;
import vn.hust.easypos.security.DomainUserDetailsService;
import vn.hust.easypos.service.dto.BillCancelRequest;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.bill.BillCompleteRequest;
import vn.hust.easypos.service.dto.bill.BillCreateRequest;
import vn.hust.easypos.service.impl.BillService;
import vn.hust.easypos.service.util.Common;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;

import java.net.URISyntaxException;

@RestController
@RequestMapping("/api")
public class BillResource {

    private static final String ENTITY_NAME = "Bill";
    private final Validator customValidator;
    private final BillService billService;
    private final Logger log = LoggerFactory.getLogger(BillResource.class);

    private final UserRepository userRepository;
    private final DomainUserDetailsService domainUserDetailsService;

    public BillResource(Validator customValidator, BillService billService, UserRepository userRepository, DomainUserDetailsService domainUserDetailsService) {
        this.customValidator = customValidator;
        this.billService = billService;
        this.userRepository = userRepository;
        this.domainUserDetailsService = domainUserDetailsService;
    }

    @PostMapping("/client/page/bill/create")
    public ResponseEntity<ResultDTO> createBill(@RequestBody BillCreateRequest billDTO) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, billDTO);
//        log.debug("REST request to save Bill : {}", billDTO);
        ResultDTO result = billService.saveBill(billDTO);
        Bill bill = (Bill) result.getData();
        result.setData(new BillCancelRequest(bill.getId(), bill.getCode()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/page/bill/temp-create")
    public ResponseEntity<ResultDTO> createTempBill(@RequestBody BillCreateRequest billDTO) throws URISyntaxException {
        Common.validateInput(customValidator, ENTITY_NAME, billDTO);
        setAuthen(billDTO.getComId());
        ResultDTO result = billService.saveTempBill(billDTO);
        Bill bill = (Bill) result.getData();
        result.setData(new BillCancelRequest(bill.getId(), bill.getCode()));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    private void setAuthen(Integer comId) {
        User user = userRepository.findById(comId).orElseThrow();
        UserDetails userDetails = domainUserDetailsService.loadUserByUsername(user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @GetMapping("/client/page/bill/get-with-paging")
    public ResponseEntity<ResultDTO> getAllBills(
        Pageable pageable,
        @RequestParam(required = false) Integer status,
        @RequestParam(required = false) String fromDate,
        @RequestParam(required = false) String toDate,
        @RequestParam(required = false) String keyword
    ) {
        log.debug("REST request to get a page of Bills");
        ResultDTO resultDTO = billService.searchBills(pageable, status, fromDate, toDate, keyword);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/by-id/{id}")
    public ResponseEntity<ResultDTO> getBillById(
        @PathVariable(value = "id") @NotNull(message = ExceptionConstants.BILL_ID_NOT_NULL) Integer id
    ) {
        log.debug("REST request to get Bill detail with id: {}", id);
        ResultDTO resultDTO = billService.getBillById(id);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/page/bill/temp")
    public ResponseEntity<ResultDTO> getBillTemp(
        @RequestParam Integer tableId, @RequestParam Integer comId
    ) {
        ResultDTO resultDTO = billService.getBillTemp(tableId, comId);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @PostMapping("/client/page/bill/done-by-id")
    public ResponseEntity<ResultDTO> doneById(
        @RequestBody BillCompleteRequest request
    ) {
        ResultDTO resultDTO = billService.completeBill(request.getBillId());
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }
}
