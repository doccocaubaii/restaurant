package vn.softdreams.easypos.integration.easybooks88.util;

import com.google.common.base.Strings;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Utils {

    private static final String EMAIL_REGEX = "^([\\w+-.%]+@[\\w\\-.]+\\.[A-Za-z]{2,4},?)*(?<!,)+$";

    public static boolean isValid(String dateStr) {
        DateFormat sdf = new SimpleDateFormat(dateStr);
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        return email.matches(EMAIL_REGEX);
    }

    public static Set<String> validateInput(Validator validator, Object object) {
        Set<String> violationSet = new HashSet<>();
        if (object instanceof Collection<?>) {
            for (Object o : (Collection) object) violationSet.addAll(validateInput(validator, o));
        } else {
            Set<ConstraintViolation<Object>> violations = validator.validate(object);
            if (violations.isEmpty()) return violationSet;
            String messages;
            if (violations.size() > 1) {
                messages = violations.stream().reduce("", (acc, ele) -> acc + ", " + ele.getMessage(), String::concat);
            } else {
                messages = violations.stream().reduce("", (acc, ele) -> acc + ele.getMessage(), String::concat);
            }
            if (!Strings.isNullOrEmpty(messages)) {
                violationSet.add(messages);
            }
        }
        return violationSet;
    }
}
