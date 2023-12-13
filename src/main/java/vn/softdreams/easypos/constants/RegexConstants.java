package vn.softdreams.easypos.constants;

public interface RegexConstants {
    String TAX_AUTHORITY_CODE_REGEX = "M[1,2,5]{1}-2[0-9]{1}-[A-Z0-9]{5}-[\\d]{11}";
    // chứng minh thư
    String ID_NUMBER_REGEX_01 = "^[1-9]{1}[0-9]{8}$";
    // căn cước công dân
    String ID_NUMBER_REGEX_02 = "^0{1}[0-9]{11}$";
    String PHONE_NUMBER_REGEX =
        "^(0|\\+84)(\\s|\\.)?((2[0-9])|(3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
    String PHONE_NUMBER_MOBI_PONE_REGEX = "^(0|\\+84)(\\s|\\.)?((7[06-9])|(9[0|3]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
    String CUSTOMER_TAX_CODE_REGEX = "^(?:\\d{10}|\\d{10}-\\d{3})$";
    String EMAIL_REGEX =
        "^([\\w#!%$‘&+*/=?^_`{|}~-]+[\\w#!%$‘&+*/=?^_`.{|}~-]*[\\w#!%$‘&+*/=?^_`{|}~-]+){1,64}@[\\w]{2,63}[\\w-]*(\\.[\\w-]{2,63})*(\\.[a-zA-Z]{2,63})$";
    String HOURS_MINUTES = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
    String HOURS_MINUTES_FROM_TO = "^(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]-(0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$";
}
