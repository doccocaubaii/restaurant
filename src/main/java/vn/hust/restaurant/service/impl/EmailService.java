package vn.hust.restaurant.service.impl;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.hust.restaurant.domain.Otp;
import vn.hust.restaurant.domain.User;
import vn.hust.restaurant.repository.OtpRepository;
import vn.hust.restaurant.repository.UserRepository;
import vn.hust.restaurant.web.rest.errors.CustomException;

import java.util.Random;

@Service
@Transactional
public class EmailService {
    private final UserRepository userRepository;

    private final JavaMailSender mailSender;

    private final OtpRepository otpRepository;

    public EmailService(UserRepository userRepository, JavaMailSender mailSender, OtpRepository otpRepository) {
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.otpRepository = otpRepository;
    }

    public void sendEmailToUser(Integer id) {
        User user = userRepository.findById(id).orElseThrow(() -> new CustomException("Không tìm thấy user"));
        sendOtpEmail(user.getEmail(), generateOtp(id ,1));
    }

    public void sendOtpEmail(String toEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("gnhatminhg@gmail.com");
        message.setTo(toEmail);
        message.setSubject("Restaurant - OTP for Account Activation");
        message.setText(buildEmailBody(otp));
        mailSender.send(message);
    }

    private String buildEmailBody(String otp) {
        return "Dear User,\n\n"
            + "To activate your account, please use the following One-Time Password (OTP):\n\n"
            + otp + "\n\n"
            + "If you did not request this email, please ignore it.\n\n"
            + "Best regards,\n"
            + "Restaurant Team";
    }

    public String generateOtp(Integer userId, Integer type) {
        this.otpRepository.resetOtp(userId, type);
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000);
        String otpS = String.valueOf(otp);
        Otp otpE = new Otp(otpS, userId,type);
        this.otpRepository.save(otpE);
        return otpS;
    }
}
