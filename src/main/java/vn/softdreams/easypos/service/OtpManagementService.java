package vn.softdreams.easypos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import vn.softdreams.easypos.domain.Otp;
import vn.softdreams.easypos.dto.user.OTPCheckRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;

import java.util.Optional;

/**
 * Service Interface for managing {@link Otp}.
 */
public interface OtpManagementService {
    Otp save(Otp otp);
    Otp update(Otp otp);
    Optional<Otp> partialUpdate(Otp otp);
    Page<Otp> findAll(Pageable pageable);
    Optional<Otp> findOne(Integer id);
    void delete(Integer id);
    ResultDTO checkOtp(OTPCheckRequest request);
}
