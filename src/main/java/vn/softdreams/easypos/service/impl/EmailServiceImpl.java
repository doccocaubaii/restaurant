package vn.softdreams.easypos.service.impl;

import com.google.common.base.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.domain.EpMessage;
import vn.softdreams.easypos.dto.message.SendMessageReponse;
import vn.softdreams.easypos.repository.EpMessageRepository;
import vn.softdreams.easypos.util.CommonIntegrated;
import vn.softdreams.easypos.web.rest.InvoiceManagementResource;

import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl {

    private final JavaMailSender emailSender;
    private final Logger log = LoggerFactory.getLogger(InvoiceManagementResource.class);
    private final EpMessageRepository epMessageRepository;
    private final RestTemplate restTemplate;

    public EmailServiceImpl(JavaMailSender emailSender, EpMessageRepository epMessageRepository, RestTemplate restTemplate) {
        this.emailSender = emailSender;
        this.epMessageRepository = epMessageRepository;
        this.restTemplate = restTemplate;
    }

    private void sendMessage(String to, String subject, String html, String text) {
        try {
            MimeMessage mimeMessage = emailSender.createMimeMessage();
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setFrom("easypos-noreply@softdreams.vn");
            messageHelper.setTo(to);
            messageHelper.setSubject(subject);
            messageHelper.setText(text, html);
            EpMessage epMessage = new EpMessage();
            epMessage.setType(Constants.MESSAGE_TYPE_MAIL);
            epMessage.setReceive(to);
            epMessage.setSubject(subject);
            epMessage.setHtmlContent(html);
            epMessageRepository.save(epMessage);
            emailSender.send(mimeMessage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendMessagePhoneNumber(String to, String subject, String text) {
        try {
            EpMessage epMessage = new EpMessage();
            epMessage.setType(Constants.MESSAGE_TYPE_SMS);
            epMessage.setReceive(to);
            epMessage.setSubject(subject);
            epMessage.setTextContent(text);
            epMessageRepository.save(epMessage);
            SendMessageReponse sendMessageReponse = CommonIntegrated.sendMessage(epMessage, restTemplate);
            epMessage.setStatus(sendMessageReponse.getCode() == 1 ? 1 : 2);
            if (epMessage.getStatus() != 1 && !Strings.isNullOrEmpty(sendMessageReponse.getMessage())) {
                epMessage.setErrorMessage(sendMessageReponse.getMessage());
            }
            epMessageRepository.save(epMessage);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendAccountCredentials(String to, String account, String password) {
        try {
            String subject = "EasyPOS - Thông tin khởi tạo tài khoản";
            String html =
                "<h3>Kính gửi quý khách hàng thông tin truy cập hệ thống EasyPOS của Softdreams</h3>\n" +
                "\n" +
                "<p><li>Tài khoản: @@account</li></p>\n" +
                "<p><li>Mật khẩu: @@password</li></p>\n" +
                "\n" +
                "<p>Để đảm bảo bảo mật, quý khách vui lòng đổi mật khẩu khi sử dụng ứng dụng</p>\n" +
                "<p>Cảm ơn quý khách đã sử dụng dịch vụ!</p>\n" +
                "<p>Softdreams JSC</p>";
            String text =
                "Kính gửi quý khách hàng thông tin truy cập hệ thống EasyPOS của Softdreams\n" +
                "\n" +
                "Tài khoản: @@account\n" +
                "Mật khẩu: @@password\n" +
                "\n" +
                "Để đảm bảo bảo mật, quý khách vui lòng đổi mật khẩu khi sử dụng ứng dụng\n" +
                "Cảm ơn quý khách đã sử dụng dịch vụ!\n" +
                "Softdreams JSC";

            html = html.replace("@@account", account).replace("@@password", password);
            text = text.replace("@@account", account).replace("@@password", password);
            sendMessage(to, subject, html, text);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendResetPassword(String to, String otp) {
        try {
            String subject = "[EasyPOS] - Đặt lại mật khẩu đăng nhập";
            String html =
                "<h3>ĐẶT LẠI MẬT KHẨU ĐĂNG NHẬP PHẦN MỀM EASYPOS</h3>\n" +
                "\n" +
                "<p><i>Kính gửi: Quý khách hàng!</i></p>\n" +
                "\n" +
                "<p>Hệ thống nhận được yêu cầu đặt lại mật khẩu cho tài khoản của quý khách trên phần mềm EasyPOS.</p>\n" +
                "\n" +
                "<p style=\"font-weight:bold\">Mã xác minh của quý khách là: @@otp</p>\n" +
                "\n" +
                "<p>Mã này có hiệu lực trong 5 phút, nếu quá 5 phút quý khách vui lòng sử dụng lại chức năng quên mật khẩu để hệ thống cấp mã xác minh mới. </p>\n" +
                "\n" +
                "<p>Trân trọng!</p>\n" +
                "\n" +
                "<p><b>Công ty CP đầu tư công nghệ và thương mại SoftDreams</b></p>\n" +
                "\n" +
                "<p><i>(Đây là email tự động, vui lòng không phản hồi lại email này)</i></p><div class=\"yj6qo\"></div><div class=\"adL\"></div>";
            String text =
                "ĐẶT LẠI MẬT KHẨU ĐĂNG NHẬP PHẦN MỀM EASYPOS\n" +
                "\n" +
                "Kính gửi: Quý khách hàng!\n" +
                "\n" +
                "Hệ thống nhận được yêu cầu đặt lại mật khẩu cho tài khoản của quý khách trên phần mềm EasyPos\n" +
                "\n" +
                "Mã xác minh của quý khách là: @@otp\n" +
                "\n" +
                "Mã này có hiệu lực trong 5 phút, nếu quá 5 phút quý khách vui lòng sử dụng lại chức năng quên mật khẩu để hệ thống cấp mã xác minh mới\n" +
                "\n" +
                "Trân trọng!\n" +
                "\n" +
                "Công ty CP đầu tư công nghệ và thương mại SoftDreams\n" +
                "\n" +
                "(Đây là email tự động, vui lòng không phản hồi lại email này)";

            html = html.replace("@@otp", otp);
            text = text.replace("@@otp", otp);
            sendMessage(to, subject, html, text);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    public void sendOTPRegister(String to, String otp) {
        try {
            String subject = "EasyPOS - Mã xác thực đăng kí dùng thử";
            String html =
                "<h3>Kính gửi quý khách hàng mã xác thực đăng kí dùng thử hệ thống EasyPOS của Softdreams</h3>\n" +
                "\n" +
                "<p><li>Mã xác thực: @@otp</li></p>\n" +
                "\n" +
                "<p>Để đảm bảo bảo mật, quý khách vui lòng không tiết lộ mã xác thực với bất kỳ ai</p>\n" +
                "<p>Cảm ơn quý khách đã sử dụng dịch vụ!</p>\n" +
                "<p>Softdreams JSC</p>";
            String text =
                "Kính gửi quý khách hàng mã xác thực đăng kí dùng thử hệ thống EasyPOS của Softdreams\n" +
                "\n" +
                "Mã xác thực: @@otp\n" +
                "\n" +
                "Để đảm bảo bảo mật, quý khách vui lòng không tiết lộ mã xác thực với bất kỳ ai<\n" +
                "Cảm ơn quý khách đã sử dụng dịch vụ!\n" +
                "Softdreams JSC";

            html = html.replace("@@otp", otp);
            text = text.replace("@@otp", otp);
            sendMessage(to, subject, html, text);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
    }

    // dùng để test, tránh spam send mail
    public String sendAccountCredentials1(String to, String account, String password) {
        try {
            String subject = "EasyPOS - Thông tin khởi tạo tài khoản";
            String text =
                "Kính gửi quý khách hàng thông tin truy cập hệ thống EasyPOS của Softdreams\n" +
                "\n" +
                "Tài khoản: @@account\n" +
                "Mật khẩu: @@password\n" +
                "\n" +
                "Để đảm bảo bảo mật, quý khách vui lòng đổi mật khẩu khi sử dụng ứng dụng\n" +
                "Cảm ơn quý khách đã sử dụng dịch vụ!\n" +
                "Softdreams JSC";

            return text.replace("@@account", account).replace("@@password", password);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return null;
    }
}
