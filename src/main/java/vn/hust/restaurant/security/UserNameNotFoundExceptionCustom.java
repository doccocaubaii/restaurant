package vn.hust.restaurant.security;

import org.springframework.security.core.AuthenticationException;

public class UserNameNotFoundExceptionCustom extends AuthenticationException {

    public UserNameNotFoundExceptionCustom(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UserNameNotFoundExceptionCustom(String msg) {
        super(msg);
    }
}
