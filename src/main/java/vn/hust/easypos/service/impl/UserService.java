package vn.hust.easypos.service.impl;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.easypos.config.Constants;
import vn.hust.easypos.domain.User;
import vn.hust.easypos.repository.CompanyRepository;
import vn.hust.easypos.repository.UserRepository;
import vn.hust.easypos.security.SecurityUtils;
import vn.hust.easypos.security.UserNameNotFoundExceptionCustom;
import vn.hust.easypos.service.dto.authorities.AuthenticationDTO;
import vn.hust.easypos.service.dto.authorities.JwtDTO;
import vn.hust.easypos.service.dto.company.CompanyResult;
import vn.hust.easypos.web.rest.errors.ExceptionConstants;
import vn.hust.easypos.web.rest.errors.InternalServerException;
import vn.hust.easypos.web.rest.vm.LoginVM;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final CompanyRepository companyRepository;

    public UserService(UserRepository userRepository, CompanyRepository companyRepository) {
        this.userRepository = userRepository;
        this.companyRepository = companyRepository;
    }

    public AuthenticationDTO getAuthoritiesAndInfo(LoginVM loginVM) {
        Optional<User> userOptional = userRepository.findOneByUsername(loginVM.getUsername());
        AuthenticationDTO authenticationDTO = new AuthenticationDTO();
        Integer comId = loginVM.getCompanyId();
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            List<CompanyResult> companyUsers = companyRepository.findAllCompanyCustomByUserID(user.getId());
            authenticationDTO.setActivate(false);
            for (CompanyResult com : companyUsers) {
                if (companyUsers.size() == 1 || (com.getId() != null && Objects.equals(com.getId(), comId))) {
                    authenticationDTO.setActivate(true);
                    authenticationDTO.setCompanyName(com.getName());
                    authenticationDTO.setCompanyId(com.getId());
                    break;
                }
            }
            if (loginVM.getCompanyId() != null && authenticationDTO.getCompanyId() == null) {
                throw new UserNameNotFoundExceptionCustom("COMPANY_NOT_FOUND");
            }
            authenticationDTO.setCompanies(companyUsers);
            //            }
            Set<String> authorities = new HashSet<>();
            authenticationDTO.setActivate(true);
            authenticationDTO.setAuthentication(
                new UsernamePasswordAuthenticationToken(
                    loginVM.getUsername(),
                    null,
                    authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())
                )
            );
            authenticationDTO.setId(user.getId());
            authenticationDTO.setFullName(user.getFullName());
            authenticationDTO.setUsername(user.getUsername());
            return authenticationDTO;
        }
        return null;
    }

    @Transactional(readOnly = true)
    public User getUserWithAuthorities() {
        Optional<User> userOptional = SecurityUtils.getCurrentUserLogin().flatMap(userRepository::findOneByUsername);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            JwtDTO jwtDTO = getInfoJwt();
            user.setCompanyId(jwtDTO.getCompanyId());
            user.setAuthorities(new HashSet<>());
            return user;
        }
        throw new InternalServerException(ExceptionConstants.USERNAME_NOT_NULL_VI, ExceptionConstants.USERNAME_NOT_NULL_CODE);
    }

    public JwtDTO getInfoJwt() {
        if (!Strings.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())) {
            String[] chunks = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            try {
                ObjectMapper mapper = new ObjectMapper();
                mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                JwtDTO jwtDTO = mapper.readValue(payload, JwtDTO.class);
                return jwtDTO;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public Integer getCompanyId() {
        User user = getUserWithAuthorities();
        return user.getCompanyId();
    }

    public String generateRandomString() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            randomString.append(characters.charAt(index));
        }

        return randomString.toString();
    }

    public String genCode(Integer comId, String code) {
        String value = generateRandomString();
        String seqName = getSeqName(comId, code) + value;
        return seqName;
    }

    public String getSeqName(Integer comId, String code) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(code);
        stringBuilder.append("_");
        stringBuilder.append(comId);
        stringBuilder.append("_");
        stringBuilder.append(Constants.SEQ);
        return stringBuilder.toString();
    }
}
