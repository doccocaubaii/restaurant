package vn.softdreams.easypos.constants;

public interface PackageConstants {
    interface DefaultValueForEB88 {
        Integer LIMITED_VOUCHER = -1;
        Integer LIMITED_TIME = -1;
        Integer LIMITED_USER_DEFAULT = -1;
        Integer LIMITED_COMPANY_DEFAULT = 1;
        Integer COM_TYPE = 1;
        String HASH = "143a508b559cd1005507f03bef80f1e4";
    }

    interface Status {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
    }

    interface StatusResponse {
        Integer SUCCESS = 1;
        Integer FAIL = 0;
    }

    interface Type {
        String TRIAL = "DUNGTHU";
    }
}
