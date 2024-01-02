package vn.hust.easypos.controller;

import java.util.Optional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.hust.easypos.service.dto.AdminUserDTO;
import vn.hust.easypos.service.impl.UserService;

@RestController
@RequestMapping("/api")
public class AccountResource {

    private static class AccountResourceException extends RuntimeException {

        private AccountResourceException(String message) {
            super(message);
        }
    }

    private final UserService userService;

    public AccountResource(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/client/common/account")
    public AdminUserDTO getAccount() {
        return Optional
            .of(userService.getUserWithAuthorities())
            .map(AdminUserDTO::new)
            .orElseThrow(() -> new AccountResourceException("User could not be found"));
    }
}
