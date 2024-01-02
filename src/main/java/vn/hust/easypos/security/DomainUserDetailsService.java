package vn.hust.easypos.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import vn.hust.easypos.constants.UserConstants;
import vn.hust.easypos.domain.User;
import vn.hust.easypos.repository.UserRepository;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String login) {
        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userOptional = userRepository.findOneByUsername(lowercaseLogin);
        if (!userOptional.isEmpty()) {
            return createSpringSecurityUser(userOptional.get());
        } else {
            throw new UserNameNotFoundExceptionCustom("USER_NOT_FOUND");
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(User user) {
        if (user.getStatus() != UserConstants.Status.ACTIVATE) {
            throw new UserNotActivatedException("ACCOUNT_NOT_ACTIVE");
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), grantedAuthorities);
    }
}
