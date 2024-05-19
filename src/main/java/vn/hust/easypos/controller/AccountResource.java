package vn.hust.easypos.controller;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import vn.hust.easypos.service.dto.AdminUserDTO;
import vn.hust.easypos.service.impl.UserService;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AccountResource(UserService userService,
                           PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
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

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

}
