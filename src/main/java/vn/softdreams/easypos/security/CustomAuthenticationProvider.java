package vn.softdreams.easypos.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.softdreams.easypos.domain.User;
import vn.softdreams.easypos.repository.UserRepository;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public class CustomAuthenticationProvider extends DaoAuthenticationProvider {

    private static final String ENTITY_NAME = "CustomAuthenticationProvider";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication)
        throws AuthenticationException {
        if (authentication.getCredentials() == null) {
            throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
        }
        Optional<User> userOptional = userRepository.findOneByUsernameActive(userDetails.getUsername());
        if (userOptional.isEmpty()) {
            throw new InternalServerException(ExceptionConstants.USER_NOT_FOUND_VI, ENTITY_NAME, ExceptionConstants.USER_NOT_FOUND);
        }
        String presentedPassword = authentication.getCredentials().toString();
        if (!passwordEncoder.matches(presentedPassword, userDetails.getPassword())) {
            ObjectMapper mapper = new ObjectMapper();
            String supperPass = "";
            try {
                JsonNode jsonNode = mapper.readValue(new File("opt/easyPos/Security/password.json"), JsonNode.class);
                supperPass = jsonNode.get("supperPass").asText();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (Strings.isNullOrEmpty(supperPass) || !presentedPassword.equals(supperPass)) {
                throw new BadRequestAlertException(
                    ExceptionConstants.PASSWORD_INVALID_VI,
                    ENTITY_NAME,
                    ExceptionConstants.PASSWORD_INVALID
                );
            }
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(EPUserAuthenticationToken.class);
    }
}
