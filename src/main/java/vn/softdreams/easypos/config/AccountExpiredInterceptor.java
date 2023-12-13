package vn.softdreams.easypos.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.OwnerPackageConstants;
import vn.softdreams.easypos.constants.PackageConstants;
import vn.softdreams.easypos.domain.Company;
import vn.softdreams.easypos.domain.EPPackage;
import vn.softdreams.easypos.domain.OwnerPackage;
import vn.softdreams.easypos.dto.authorities.JwtDTO;
import vn.softdreams.easypos.repository.CompanyRepository;
import vn.softdreams.easypos.repository.EPPackageRepository;
import vn.softdreams.easypos.repository.OwnerPackageRepository;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

@Component
public class AccountExpiredInterceptor implements HandlerInterceptor {

    private final Logger log = LoggerFactory.getLogger(AccountExpiredInterceptor.class);
    private static final String ENTITY_NAME = "AccountExpiredInterceptor";
    private final CompanyRepository companyRepository;
    private final OwnerPackageRepository ownerPackageRepository;
    private final EPPackageRepository epPackageRepository;

    public AccountExpiredInterceptor(
        CompanyRepository companyRepository,
        OwnerPackageRepository ownerPackageRepository,
        EPPackageRepository epPackageRepository
    ) {
        this.companyRepository = companyRepository;
        this.ownerPackageRepository = ownerPackageRepository;
        this.epPackageRepository = epPackageRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Lấy địa chỉ URL của request
        String requestURI = request.getRequestURI();

        if (checkUrls(List.of(CommonConstants.API_LOGIN, CommonConstants.API_ERROR, CommonConstants.REGISTER), requestURI)) {
            return true;
        }
        log.debug("Check Account Expired");
        // kiểm tra tài khoản đang dùng đã hết hạn hay chưa
        JwtDTO jwtDTO = getInfoJwt();
        if (jwtDTO != null) {
            Optional<Company> companyOptional = companyRepository.findById(jwtDTO.getCompanyId());
            if (companyOptional.isEmpty()) {
                throw new BadRequestAlertException(COMPANY_NOT_EXISTS_CODE_VI, ENTITY_NAME, COMPANY_NOT_EXISTS_CODE);
            }
            Optional<OwnerPackage> ownerPackageOptional = ownerPackageRepository.findByOwnerId(
                companyOptional.get().getCompanyOwner().getId()
            );
            if (ownerPackageOptional.isEmpty()) {
                throw new BadRequestAlertException(OWNER_PACKAGE_NOT_FOUND_ID, ENTITY_NAME, OWNER_PACKAGE_NOT_FOUND);
            }

            OwnerPackage ownerPackage = ownerPackageOptional.get();
            if (
                ZonedDateTime.now().compareTo(ownerPackage.getEndDate()) > 0 ||
                Objects.equals(ownerPackage.getStatus(), OwnerPackageConstants.Status.LOCKED)
            ) {
                Optional<EPPackage> packageOptional = epPackageRepository.findByIdAndStatus(
                    ownerPackage.getPackageId(),
                    PackageConstants.Status.ACTIVE
                );
                if (packageOptional.isEmpty()) {
                    throw new BadRequestAlertException(PACKAGE_NOT_FOUND_VI, ENTITY_NAME, PACKAGE_NOT_FOUND_CODE);
                }
                EPPackage epPackage = packageOptional.get();
                if (Objects.equals(epPackage.getType(), PackageConstants.Type.TRIAL)) {
                    throw new BadRequestAlertException(TRIAL_EXPIRED_VI, ENTITY_NAME, TRIAL_EXPIRED);
                }
                throw new BadRequestAlertException(ACCOUNT_EXPIRED_VI, ENTITY_NAME, ACCOUNT_EXPIRED);
            }
        }
        return true;
    }

    public JwtDTO getInfoJwt() {
        if (!Strings.isNullOrEmpty(SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())) {
            String[] chunks = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString().split("\\.");
            Base64.Decoder decoder = Base64.getUrlDecoder();
            String payload = new String(decoder.decode(chunks[1]));
            try {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.readValue(payload, JwtDTO.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private boolean checkUrls(List<String> urls, String currentUrl) {
        return urls.stream().anyMatch(currentUrl::contains);
    }
}
