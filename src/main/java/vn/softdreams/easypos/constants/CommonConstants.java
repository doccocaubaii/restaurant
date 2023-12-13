package vn.softdreams.easypos.constants;

import vn.softdreams.easypos.dto.message.SMSIntegration;

import java.util.List;

public class CommonConstants {

    // API
    public static final String API_LOGIN = "/api/client/common/authenticate";
    public static final String API_ERROR = "/error";
    public static final String REGISTER = "/client/common/register";
    // end API

    // area, areaUnit
    public static final String AREA_NAME_CREATE_DEFAULT = "Khu vực 1";
    public static final String COMPANY_NAME_CREATE_DEFAULT = "Công ty 1";
    public static final String CUSTOMER_NAME_CREATE_DEFAULT = "Khách lẻ";
    public static final String CUSTOMER_CODE_DEFAULT = "KH1";
    public static final String PRODUCT_NAME_CREATE_DEFAULT = "Chiết khấu";
    public static final String BILL_PRODUCT_NAME_CREATE_DEFAULT = "Chiết khấu đơn hàng";
    public static final String PRODUCT_NAME_NOTE_CREATE_DEFAULT = "Ghi chú";
    public static final String PRODUCT_NAME_PROMOTION_CREATE_DEFAULT = "Khuyến mại";
    public static final String ERROR_NAME_ALL = "tất cả";
    // end area
    // bill
    public static final Integer BILL_DELIVERY_TYPE_IN_PLACE = 1;
    public static final Integer BILL_STATUS_NOT_COMPLETE = 0;
    //end bill

    //    TaskLog
    public static final String PUBLISH_INVOICE = "PUBLISH_INVOICE";

    // reservation
    public static final int RESERVATION_STATUS_DEFAULT = 0;
    public static final Integer RESERVATION_STATUS_CAME = 1;
    public static final Integer RESERVATION_STATUS_SELECTED = 2;
    public static final int RESERVATION_STATUS_CANCELED = 3;

    // end

    // register
    public static final Integer REGISTER_PASSWORD_LENGTH = 6;
    public static final Integer REGISTER_SEND_OTP_LENGTH = 6;
    public static final Integer TIME_OTP_AVAILABLE_SECOND = 300;

    // end register

    // forgot password
    public static final Integer MAX_TIME_REQUEST_FORGOT_PASS = 5;
    public static final Integer EXPIRED_TIME_FORGOT_PASS = 300;

    // end forgot password

    // customer
    public interface Customer {
        public static final Integer CUSTOMER_ACTIVE = 1;
        public static final Integer CUSTOMER_INACTIVE = 0;
        public static final Boolean CUSTOMER_ACTIVE_TRUE = true;
        public static final Boolean CUSTOMER_INACTIVE_FALSE = false;
    }

    // vatrate
    public interface VatRate {
        public static final Integer VAT_DEFAULT = -4;
    }

    public interface Sms {
        int UNICODE = 1;
        int NORMAL = 0;
        String USERNAME = "softdreams";
        String PASSWORD = "SoftDream@#$123";
        String BRANDNAME = "EASYCA.VN";
        String VIETTEL = "VTE";
        String VINAPHONE = "GPC";
        String MOBIFONE = "VMS";
        String VIETNAMMOBILE = "VNM";
        String GMOBILE = "BL";
    }

    public interface SmsIntegration {
        List<SMSIntegration> SmsList = List.of(new SMSIntegration("EASYCA.VN", "softdreams", "SoftDream@#$123", 1));

        class Process {

            public static SMSIntegration getDataByBrandName(String brandName) {
                for (SMSIntegration smsIntegration : SmsList) {
                    if (smsIntegration.getBrandName().equals(brandName)) {
                        return smsIntegration;
                    }
                }
                return null;
            }
        }
    }

    public static final Integer MAX_ROW_NUMBER_IMPORT_EXCEL = 2000;

    public interface Schedule {
        String DELETE_TASK_LOG = "0 0 1 ? * *";
    }
}
