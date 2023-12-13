package vn.softdreams.easypos.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.OTPConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.Otp;
import vn.softdreams.easypos.dto.user.OTPCheckRequest;
import vn.softdreams.easypos.repository.OtpRepository;
import vn.softdreams.easypos.service.OtpManagementService;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Otp}.
 */
@Service
@Transactional
public class OtpManagementServiceImpl implements OtpManagementService {

    private final Logger log = LoggerFactory.getLogger(OtpManagementServiceImpl.class);

    private final OtpRepository otpRepository;
    private static final String ENTITY_NAME = "otp";

    public OtpManagementServiceImpl(OtpRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    @Override
    public Otp save(Otp otp) {
        log.debug("Request to save Otp : {}", otp);
        return otpRepository.save(otp);
    }

    @Override
    public Otp update(Otp otp) {
        log.debug("Request to update Otp : {}", otp);
        return otpRepository.save(otp);
    }

    @Override
    public Optional<Otp> partialUpdate(Otp otp) {
        log.debug("Request to partially update Otp : {}", otp);

        return otpRepository
            .findById(otp.getId())
            .map(existingOtp -> {
                if (otp.getUsername() != null) {
                    existingOtp.setUsername(otp.getUsername());
                }
                if (otp.getOtp() != null) {
                    existingOtp.setOtp(otp.getOtp());
                }

                return existingOtp;
            })
            .map(otpRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Otp> findAll(Pageable pageable) {
        log.debug("Request to get all Otps");
        return otpRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Otp> findOne(Integer id) {
        log.debug("Request to get Otp : {}", id);
        return otpRepository.findById(id);
    }

    @Override
    public void delete(Integer id) {
        log.debug("Request to delete Otp : {}", id);
        otpRepository.deleteById(id);
    }

    @Override
    public ResultDTO checkOtp(OTPCheckRequest request) {
        if (request.getOtp().length() != CommonConstants.REGISTER_PASSWORD_LENGTH) {
            throw new BadRequestAlertException(ExceptionConstants.OTP_INVALID_VI, ENTITY_NAME, ExceptionConstants.OTP_INVALID);
        }
        Optional<Otp> otpOptional = otpRepository.findByUsernameAndOtpAndType(
            request.getUsername(),
            request.getOtp(),
            OTPConstants.Type.FORGOT_PASS
        );
        if (otpOptional.isEmpty()) {
            throw new BadRequestAlertException(ExceptionConstants.OTP_INCORRECT_VI, ENTITY_NAME, ExceptionConstants.OTP_INCORRECT);
        }
        Otp otp = otpOptional.get();
        if (ZonedDateTime.now().isAfter(otp.getExpiredTime()) || otp.getStatus().equals(OTPConstants.Status.USED)) {
            throw new BadRequestAlertException(ExceptionConstants.OTP_INCORRECT_VI, ENTITY_NAME, ExceptionConstants.OTP_INCORRECT);
        }
        otp.setStatus(OTPConstants.Status.USED);
        otpRepository.save(otp);
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.CHECK_OTP_SUCCESS, true, request);
    }
}
