package vn.softdreams.easypos.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";
    public static final String SYSTEM_ADMIN = "SYSTEM_ADMIN";

    public static final String SALE = "ROLE_SALE";
    public static final String INVENTORY = "ROLE_INVENTORY";
    public static final String EMPLOYEE = "ROLE_EMPLOYEE";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    private AuthoritiesConstants() {}

    public interface Employee {
        public static final String EMPLOYEE = "01";
        public static final String ADD = "0101";
        public static final String DELETE = "0102";
        public static final String UPDATE = "0103";
        public static final String VIEW = "0104";
    }

    public interface Role {
        public static final String ROLE = "02";
        public static final String VIEW = "0201";
        public static final String ADD = "0202";
        public static final String UPDATE = "0203";
        public static final String DELETE = "0204";
    }

    public interface Customer {
        public static final String ROLE = "03";
        public static final String VIEW = "0301";
        public static final String ADD = "0302";
        public static final String UPDATE = "0303";
        public static final String DELETE = "0304";
    }

    public interface ProductGroup {
        public static final String GROUP = "04";
        public static final String ADD = "0401";
        public static final String VIEW = "0402";
        public static final String DELETE = "0403";
        public static final String UPDATE = "0404";
    }

    public interface Product {
        public static final String PRODUCT = "05";
        public static final String VIEW = "0501";
        public static final String ADD = "0502";
        public static final String UPDATE = "0503";
        public static final String DELETE = "0504";
        public static final String PRINT_BAR_CODE = "0505";
    }

    public interface Invoice {
        public static final String INVOICE = "06";
        public static final String VIEW = "0601";
        public static final String ADD = "0602";
        public static final String UPDATE = "0603";
        public static final String DELETE = "0604";
        public static final String PRINT_SHARE = "0605";
        public static final String CANCEL = "0606";
        public static final String COMPLETE_PAY = "0607";
    }

    public interface Bill {
        public static final String BILL = "07";
        public static final String RELEASE = "0701";
        public static final String VIEW = "0702";
        public static final String SEND_MAIL = "0703";
        public static final String PRINT_SHARE = "0704";
        public static final String UPDATE = "0705";
        public static final String CANCEL = "0706";
    }

    public interface AreaUnit {
        public static final String AREA_UNIT = "08";
        public static final String RELEASE = "0801";
        public static final String ADD = "0802";
        public static final String VIEW = "0803";
        public static final String DELETE = "0804";
        public static final String UPDATE = "0805";
    }

    public interface Area {
        public static final String AREA = "09";
        public static final String ADD = "0901";
        public static final String VIEW = "0902";
        public static final String DELETE = "0903";
        public static final String UPDATE = "0904";
    }

    public interface Reservation {
        public static final String RESERVATION = "10";
        public static final String ADD = "1001";
        public static final String UPDATE = "1002";
        public static final String DELETE = "1003";
        public static final String VIEW = "1004";
    }

    public interface ConnectAccountEI {
        public static final String CONNECT_ACCOUNT_EI = "11";
    }

    public interface Register_Update_Declaration {
        public static final String REGISTER_UPDATE_DECLARATION = "12";
    }

    public interface Config {
        public static final String CONFIG = "13";
        public static final String VIEW = "1301";
        public static final String UPDATE = "1302";
    }

    public interface PrintConfig {
        public static final String PRINT_CONFIG = "14";
        public static final String VIEW = "1401";
        public static final String UPDATE = "1402";
    }

    public interface RsInward {
        public static final String RS_INWARD = "15";
        public static final String ADD = "1501";
        public static final String VIEW = "1502";
        public static final String DELETE = "1503";
        public static final String UPDATE = "1504";
    }

    public interface RsOutward {
        public static final String RS_OUTWARD = "16";
        public static final String ADD = "1601";
        public static final String VIEW = "1602";
        public static final String UPDATE = "1603";
    }

    public interface Revenue {
        public static final String REVENUE = "17";
        public static final String ADD = "1701";
        public static final String VIEW = "1702";
        public static final String DELETE = "1703";
        public static final String UPDATE = "1704";
    }

    public interface Expense {
        public static final String EXPENSE = "18";
        public static final String ADD = "1801";
        public static final String VIEW = "1802";
        public static final String DELETE = "1803";
        public static final String UPDATE = "1804";
    }

    public interface Debt {
        public static final String DEBT = "19";
        public static final String ADD = "1901";
        public static final String VIEW = "1902";
        public static final String DELETE = "1903";
        public static final String UPDATE = "1904";
    }

    public interface Report {
        public static final String REPORT = "20";
        public static final String INVENTORY = "2001";
        public static final String REVENUE_EXPENSE = "2002";
        public static final String PROFIT = "2003";
        public static final String STORE = "2004";
    }

    public interface Support {
        public static final String REPORT = "20";
        public static final String INVENTORY = "2001";
        public static final String REVENUE_EXPENSE = "2002";
        public static final String PROFIT = "2003";
        public static final String STORE = "2004";
    }

    public interface AdminCompanyOwner {
        public static final String COMPANY_OWNER_MANAGEMENT = "21";
        public static final String VIEW = "2101";
        public static final String CONNECT_EASY_INVOICE = "2102";
        public static final String EDIT = "2103";
    }

    public interface AdminCompany {
        public static final String COMPANY_MANAGEMENT = "22";
        public static final String VIEW = "2201";
        public static final String ASYNC_TASK_LOG = "2203";
        public static final String IMPORT_FILE = "2202";
        public static final String ADD = "2204";
        public static final String EDIT = "2205";
    }

    public interface AdminDevice {
        public static final String DEVICE_MANAGEMENT = "23";
        public static final String VIEW = "2301";
    }

    public interface AdminTaskLog {
        public static final String TASK_LOG_MANAGEMENT = "24";
        public static final String VIEW = "2401";
        public static final String RESEND_TASK_LOG = "2402";
    }

    public interface AdminConfig {
        public static final String CONFIG_MANAGEMENT = "25";
        public static final String VIEW = "2501";
        public static final String ADD = "2502";
        public static final String EDIT = "2503";
        public static final String DELETE = "2504";
    }
}
