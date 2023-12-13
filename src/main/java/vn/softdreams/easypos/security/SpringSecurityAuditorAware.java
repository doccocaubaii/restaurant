package vn.softdreams.easypos.security;

import com.google.common.base.Strings;
import org.json.JSONObject;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

/**
 * Implementation of {@link AuditorAware} based on Spring Security.
 */
@Component
public class SpringSecurityAuditorAware implements AuditorAware<Integer> {

    @Override
    public Optional<Integer> getCurrentAuditor() {
        Integer userID = 0;
        //TODO: hardcode giá trị để ghi vào updater trong các entity
        if (SecurityContextHolder.getContext().getAuthentication() == null) return Optional.of(0);
        if (!Strings.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())) {
            String[] chunks = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            JSONObject json = null;

            try {
                json = new JSONObject(payload);
                userID = Integer.parseInt(json.get("id").toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Optional.of(userID);
    }
}
