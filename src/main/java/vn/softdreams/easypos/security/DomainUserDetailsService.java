package vn.softdreams.easypos.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.UserConstants;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.repository.UserRepository;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

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
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);

        //        if (new EmailValidator().isValid(login, null)) {
        //            return userRepository
        //                .findOneWithAuthoritiesByEmailIgnoreCase(login)
        //                .map(user -> createSpringSecurityUser(login, user))
        //                .orElseThrow(() -> new UsernameNotFoundException("User with email " + login + " was not found in the database"));
        //        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        Optional<User> userOptional = userRepository.findOneByUsernameActive(lowercaseLogin);
        if (!userOptional.isEmpty()) {
            return createSpringSecurityUser(lowercaseLogin, userOptional.get());
        } else {
            throw new UserNameNotFoundExceptionCustom("USER_NOT_FOUND");
        }
    }

    private org.springframework.security.core.userdetails.User createSpringSecurityUser(String lowercaseLogin, User user) {
        if (user.getStatus() != UserConstants.Status.ACTIVATE) {
            throw new UserNotActivatedException("ACCOUNT_NOT_ACTIVE");
        }
        //        List<GrantedAuthority> grantedAuthorities = user
        //            .getAuthorities()
        //            .stream()
        //            .map(Authority::getName)
        //            .map(SimpleGrantedAuthority::new)
        //            .collect(Collectors.toList());
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), Arrays.asList());
    }
}
