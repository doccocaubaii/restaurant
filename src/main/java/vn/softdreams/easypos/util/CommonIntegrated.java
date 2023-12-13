package vn.softdreams.easypos.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.util.DigestUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import vn.softdreams.easypos.config.Constants;
import vn.softdreams.easypos.constants.CommonConstants;
import vn.softdreams.easypos.constants.ResultConstants;
import vn.softdreams.easypos.domain.EpMessage;
import vn.softdreams.easypos.domain.MessageIntegration;
import vn.softdreams.easypos.dto.invoice.*;
import vn.softdreams.easypos.dto.invoice.ngp.PatternNGPRequest;
import vn.softdreams.easypos.dto.invoice.ngp.PatternNGPResponse;
import vn.softdreams.easypos.dto.invoice.ngp.PatternSearchNgpResponse;
import vn.softdreams.easypos.dto.message.SMSIntegration;
import vn.softdreams.easypos.dto.message.SendMessageReponse;
import vn.softdreams.easypos.dto.message.SendMessageRequest;
import vn.softdreams.easypos.service.dto.ResultDTO;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;
import vn.softdreams.easypos.web.rest.errors.InternalServerException;

import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class CommonIntegrated {

    private static final Logger log = LoggerFactory.getLogger(Common.class);

    public static ResultDTO registerAndPublish(
        RegisterEasyInvoiceRequest registerEasyInvoiceDTO,
        String taxCode,
        String userName,
        String password,
        RestTemplate restTemplate
    ) {
        String token = genAuthentication(userName, password, "post");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<RegisterEasyInvoiceRequest> request = new HttpEntity<>(registerEasyInvoiceDTO, headers);
        String url = Constants.BASE_HTTP + taxCode + Constants.BASE_URL_EASY_INVOICE + "/api/declaration/registerAndPublish";
        ResponseEntity<Object> response = null;
        try {
            response = restTemplate.postForEntity(url, request, Object.class);
        } catch (HttpStatusCodeException ex) {
            String value = ex.getResponseBodyAsString().contains("Message")
                ? new JSONObject(ex.getResponseBodyAsString()).get("Message").toString()
                : ResultConstants.ERROR;
            throw new InternalServerException(value, value, ResultConstants.ERROR);
        }
        return new ResultDTO(ResultConstants.SUCCESS, ResultConstants.SUCCESS_REGISTER_INVOICE, true, response);
    }

    public static CompanyUrlResponse getUrlByUserNameAndTaxCode(String taxCode, RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = Constants.BASE_COMPANY_URL + "?taxcode=" + taxCode + Constants.BASE_ACCOUNT;
        try {
            HttpEntity<?> entity = new HttpEntity<>(headers);
            HttpEntity<CompanyUrlResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, CompanyUrlResponse.class);
            return response.getBody();
        } catch (Exception ex) {
            log.error(ex.getMessage());
            throw new InternalServerException(
                ExceptionConstants.PUBLISH_EASY_INVOICE_ERROR_VI,
                "easy-invoice",
                ExceptionConstants.PUBLISH_EASY_INVOICE_ERROR
            );
        }
    }

    public static List<DeclarationResponse> declarationSearch(
        DeclarationRequest declarationDTO,
        String taxCode,
        String userName,
        String password,
        String invoiceUrl,
        RestTemplate restTemplate
    ) {
        String token = genAuthentication(userName, password, "post");
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", token);
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeclarationRequest> request = new HttpEntity<>(declarationDTO, headers);
        String url = invoiceUrl + "/api/declaration/search";
        ResponseEntity<DeclarationResponse[]> response = null;
        try {
            response = restTemplate.postForEntity(url, request, DeclarationResponse[].class);
        } catch (HttpStatusCodeException ex) {
            String value = ex.getResponseBodyAsString().contains("Message")
                ? new JSONObject(ex.getResponseBodyAsString()).get("Message").toString()
                : ResultConstants.ERROR;
            throw new InternalServerException(value, value, ResultConstants.ERROR);
        }
        return List.of(response.getBody());
    }

    public static List<DeclarationGiaPhatResponse> declarationSearchNgoGiaPhat(
        DeclarationGiaPhatRequest declarationDTO,
        String userName,
        String password,
        String invoiceUrl,
        RestTemplate restTemplate
    ) {
        String token = genAuthenticationNgoGiaPhat("POST", userName, password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", token);
        headers.set("X-HTTP-Method-Override", "POST");
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<DeclarationGiaPhatRequest> request = new HttpEntity<>(declarationDTO, headers);
        String url = invoiceUrl + "/api/invoices/patterns";
        ResponseEntity<PatternSearchNgpResponse> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, PatternSearchNgpResponse.class);
            return response.getBody().getData();
        } catch (HttpStatusCodeException ex) {
            String value = ex.getResponseBodyAsString().contains("Message")
                ? new JSONObject(ex.getResponseBodyAsString()).get("Message").toString()
                : ResultConstants.ERROR;
            throw new InternalServerException(value, value, ResultConstants.ERROR);
        }
    }

    public static PatternNGPResponse getPatternNgoGiaPhat(
        DeclarationGiaPhatRequest declarationDTO,
        String userName,
        String password,
        String invoiceUrl,
        RestTemplate restTemplate
    ) {
        String token = genAuthenticationNgoGiaPhat("POST", userName, password);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authentication", token);
        headers.set("X-HTTP-Method-Override", "POST");
        headers.setContentType(MediaType.APPLICATION_JSON);
        PatternNGPRequest req = new PatternNGPRequest();
        req.setUsername(userName);
        req.setPassword(password);
        req.setTaxcode(declarationDTO.getTaxcode());
        HttpEntity<PatternNGPRequest> request = new HttpEntity<>(req, headers);
        String url = invoiceUrl + "/api/account/verify";
        ResponseEntity<PatternNGPResponse> response = null;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, request, PatternNGPResponse.class);
            return response.getBody();
        } catch (HttpStatusCodeException ex) {
            String value = ex.getResponseBodyAsString().contains("Message")
                ? new JSONObject(ex.getResponseBodyAsString()).get("Message").toString()
                : ResultConstants.ERROR;
            throw new InternalServerException(value, value, ResultConstants.ERROR);
        }
    }

    private static String makeId() {
        StringBuilder text = new StringBuilder();
        String possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        for (int i = 0; i < 10; i++) text.append(possible.charAt((int) Math.floor(Math.random() * possible.length())));
        return text.toString();
    }

    public static String genAuthentication(String username, String password, String method) {
        Date localDate = new Date();
        long timestamp = localDate.getTime() / 1000;
        String nonce = makeId();
        String signatureRawData = String.format("%s%s%s", method.toUpperCase(), timestamp, nonce);
        String signature = new String(Base64.getEncoder().encodeToString(DigestUtils.md5Digest(signatureRawData.getBytes())));

        return String.format("%s:%s:%s:%s:%s", signature, nonce, timestamp, username, password);
    }

    public static String genAuthenticationNgoGiaPhat(String method, String username, String password) {
        DateTime epochStart = new DateTime(1970, 01, 01, 0, 0, 0, DateTimeZone.UTC);
        long millis = DateTime.now(DateTimeZone.UTC).getMillis() - epochStart.getMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String timestamp = String.valueOf(seconds);
        String nonce = UUID.randomUUID().toString().toLowerCase();
        String signatureRawData = method.toUpperCase() + timestamp + nonce;
        String hashMD5 = null;
        try {
            hashMD5 = md5(signatureRawData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashMD5 + ":" + nonce + ":" + timestamp + ":" + username + ":" + password;
    }

    private static String md5(String rawData) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(rawData.getBytes());
        return Base64.getEncoder().encodeToString(md.digest());
    }

    public static SendMessageReponse sendMessage(EpMessage epMessage, RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        SendMessageRequest sendMessage = new SendMessageRequest();
        sendMessage.setUser(CommonConstants.Sms.USERNAME);
        sendMessage.setPass(CommonConstants.Sms.PASSWORD);
        sendMessage.setTranId(epMessage.getId() != null ? epMessage.getId().toString() : "easyPos");
        sendMessage.setBrandName(CommonConstants.Sms.BRANDNAME);
        sendMessage.setDataEncode(CommonConstants.Sms.UNICODE);
        sendMessage.setPhone(epMessage.getReceive());
        sendMessage.setMess(epMessage.getTextContent());

        HttpEntity<SendMessageRequest> request = new HttpEntity<>(sendMessage, headers);
        log.error("request value : " + sendMessage);
        ResponseEntity<SendMessageReponse> sendMessageResponse = restTemplate.exchange(
            Constants.BASE_SEND_MESSAGE_URL,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<>() {}
        );
        log.error("send_message_reponse: " + sendMessageResponse.getBody());
        return sendMessageResponse.getBody();
    }

    public static SendMessageReponse sendMessageIntegration(MessageIntegration epMessage, RestTemplate restTemplate) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        SMSIntegration smsIntegration = CommonConstants.SmsIntegration.Process.getDataByBrandName(epMessage.getBrandName());
        if (smsIntegration == null) {
            SendMessageReponse response = new SendMessageReponse();
            response.setCode(2);
            response.setMessage(ExceptionConstants.BRANCH_NAME_INTEGRATION_NOT_FOUND_VI.replace("@@brandName", epMessage.getBrandName()));
            return response;
        }
        SendMessageRequest sendMessage = new SendMessageRequest();
        sendMessage.setUser(smsIntegration.getUsername());
        sendMessage.setPass(smsIntegration.getPassword());
        sendMessage.setTranId(epMessage.getId() != null ? epMessage.getId().toString() : "easyPos");
        sendMessage.setBrandName(smsIntegration.getBrandName());
        sendMessage.setDataEncode(smsIntegration.getUnicode());
        sendMessage.setPhone(epMessage.getReceive());
        sendMessage.setMess(epMessage.getTextContent());

        HttpEntity<SendMessageRequest> request = new HttpEntity<>(sendMessage, headers);
        log.error("request value : " + sendMessage);
        ResponseEntity<SendMessageReponse> sendMessageResponse = restTemplate.exchange(
            Constants.BASE_SEND_MESSAGE_URL,
            HttpMethod.POST,
            request,
            new ParameterizedTypeReference<>() {}
        );
        log.error("send_message_reponse: " + sendMessageResponse.getBody());
        return sendMessageResponse.getBody();
    }
}
