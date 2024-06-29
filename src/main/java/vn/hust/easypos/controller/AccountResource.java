package vn.hust.easypos.controller;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hust.easypos.service.dto.AdminUserDTO;
import vn.hust.easypos.service.dto.ChangePasswordDTO;
import vn.hust.easypos.service.dto.ResultDTO;
import vn.hust.easypos.service.dto.StaffDTO;
import vn.hust.easypos.service.impl.EmailService;
import vn.hust.easypos.service.impl.UserService;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    public AccountResource(UserService userService,
                           PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @GetMapping("/client/common/account")
    public AdminUserDTO getAccount() {
        return Optional
            .of(userService.getUserWithAuthorities())
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }

    @PostMapping("/client/common/encrypt")
    public String encryptPassword(@RequestBody String request) {
        return passwordEncoder.encode(request);
    }

    @PostMapping("/client/account/staff")
    public ResponseEntity<ResultDTO> createStaff(@RequestBody StaffDTO staffDTO) {
        ResultDTO result = userService.createStaff(staffDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/client/user/change-password")
    public ResponseEntity<ResultDTO> changePassword(@RequestBody ChangePasswordDTO request) {
        ResultDTO result = userService.changePassword(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @DeleteMapping("/client/account/staff/{id}")
    public ResponseEntity<ResultDTO> delStaff(@PathVariable(value = "id") @NotNull(message = ExceptionConstants.EMPLOYEE_NOT_FOUND_VI) Integer id) {
        ResultDTO result = userService.delStaff(id);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/client/account/staffs")
    public ResponseEntity<ResultDTO> getAllStaff(Pageable pageable, @RequestParam(required = false) String keyword) {
        ResultDTO resultDTO = userService.searchStaffs(pageable, keyword);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/account/active")
    public ResponseEntity<ResultDTO> activeAccout(@RequestParam String id, @RequestParam String otp ) {
        System.out.println("id ; " + id + " , otp :" + otp);
        ResultDTO resultDTO = userService.activeAccount(Integer.valueOf(id), otp);
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }

    @GetMapping("/client/account/send-email")
    public ResponseEntity<ResultDTO> sendEmail(@RequestParam String id) {
        this.emailService.sendEmailToUser(Integer.valueOf(id));
        ResultDTO resultDTO = new ResultDTO();
        return new ResponseEntity<>(resultDTO, HttpStatus.OK);
    }



    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

}
