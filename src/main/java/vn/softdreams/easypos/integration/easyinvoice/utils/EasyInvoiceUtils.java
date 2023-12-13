package vn.softdreams.easypos.integration.easyinvoice.utils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.springframework.http.HttpMethod;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class EasyInvoiceUtils {

    private static final String BASE_URL_REGEX = "^((http|https)://)(\\d{10}|\\d{13})(.(softdreams|easyinvoice|easyinvoice.com).vn)$";

    public static String generateAuthToken(HttpMethod method, String username, String password) throws Exception {
        DateTime epochStart = new DateTime(1970, 01, 01, 0, 0, 0, DateTimeZone.UTC);
        long millis = DateTime.now(DateTimeZone.UTC).getMillis() - epochStart.getMillis();
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        String timestamp = String.valueOf(seconds);
        String nonce = UUID.randomUUID().toString().toLowerCase();
        String signatureRawData = method.name().toUpperCase() + timestamp + nonce;
        String hashMD5 = md5(signatureRawData);
        return hashMD5 + ":" + nonce + ":" + timestamp + ":" + username + ":" + password;
    }

    private static String md5(String rawData) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(rawData.getBytes());
        return Base64.getEncoder().encodeToString(md.digest());
    }

    public static boolean isValidURL(String url) throws MalformedURLException, URISyntaxException {
        try {
            new URL(url).toURI();
            return url.matches(BASE_URL_REGEX);
        } catch (MalformedURLException | URISyntaxException e) {
            return false;
        }
    }
}
