package vn.softdreams.easypos.constants;

public interface OTPConstants {
    interface Status {
        Integer DEFAULT = 0;
        Integer USED = 1;
    }

    interface Type {
        Integer FORGOT_PASS = 1;
        Integer REGISTER = 2;
    }
}
