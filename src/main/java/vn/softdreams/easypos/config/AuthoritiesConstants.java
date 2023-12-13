package vn.softdreams.easypos.config;

public class AuthoritiesConstants {

    public interface Employee {
        public static final String EMPLOYEE = "01";
        public static final String ADD_EMPLOYEE = "0101";
        public static final String UPDATE_EMPLOYEE = "0102";
        public static final String DELETE_EMPLOYEE = "0103";
        public static final String VIEW_ALL_EMPLOYEE = "0104";
    }

    public interface Role {
        public static final String ROLE = "02";
        public static final String VIEW_ROLE = "0201";
        public static final String ADD_ROLE = "0202";
        public static final String UPDATE_ROLE = "0203";
        public static final String DELETE_ROLE = "0204";
    }

    public interface Bill {
        public static final String ROLE = "03";
        public static final String VIEW_BILL = "0301";
        public static final String ADD_BILL = "0302";
        public static final String UPDATE_BILL = "0303";
        public static final String PRINT_OR_SEND_BILL = "0304";
    }

    public interface Product {
        public static final String PRODUCT = "04";
        public static final String VIEW_PRODUCT = "0401";
        public static final String ADD_PRODUCT = "0402";
        public static final String UPDATE_PRODUCT = "0403";
        public static final String DELETE_PRODUCT = "0404";
        public static final String COST_PRICE_PRODUCT = "0405";
        public static final String UPDATE_COST_PRICE_PRODUCT = "0406";
    }

    public interface Customer {
        public static final String CUSTOMER = "05";
        public static final String VIEW_CUSTOMER = "0501";
        public static final String ADD_CUSTOMER = "0502";
        public static final String UPDATE_CUSTOMER = "0503";
        public static final String ADD_UPDATE_DELETE_NOTE = "0504";
        public static final String CALL_OR_SMS_CUSTOMER = "0505";
        public static final String DELETE_CUSTOMER = "0506";
    }

    public interface PrintSetting {
        public static final String PRINT_SETTING = "06";
        public static final String PRINT_BILL = "0601";
        public static final String BAR_CODE_BILL = "0602";
    }

    public interface Expenditure {
        public static final String EXPENDITURE = "07";
        public static final String VIEW_TRANSACTION_EXPENDITURE = "0701";
        public static final String ANALYSIS_EXPENDITURE = "0702";
        public static final String DOWNLOAD_REPORT_TRANSACTION_EXPENDITURE = "0703";
        public static final String CREATE_EXPENSE_OR_REVENUE = "0704";
        public static final String UPDATE_EXPENSE_OR_REVENUE = "0705";
        public static final String DELETE_EXPENSE_OR_REVENUE = "0706";
    }

    public interface Money {
        public static final String MONEY = "08";
        public static final String VIEW_MONEY = "0801";
        public static final String ADD_MONEY = "0802";
        public static final String UPDATE_MONEY = "0803";
        public static final String DELETE_MONEY = "0804";
        public static final String TRANSFER_MONEY = "0805";
    }

    public interface DebitBock {
        public static final String DEBIT_BOOK = "09";
        public static final String CREATE_TRANSACTION_DEBIT = "0901";
        public static final String UPDATE_TRANSACTION_DEBIT = "0902";
        public static final String VIEW_TRANSACTION_DEBIT = "0903";
        public static final String REMIND_CALENDAR_DEBIT = "0904";
        public static final String DOWNLOAD_REPORT_TRANSACTION_DEBIT = "0905";
        public static final String DELETE_TRANSACTION_DEBIT = "0906";
        public static final String REMIND_DEBIT = "0907";
    }

    public interface Category {
        public static final String CATEGORY = "10";
        public static final String ADD_CATEGORY = "1001";
        public static final String VIEW_CATEGORY = "1002";
        public static final String DELETE_MONEY = "1003";
        public static final String UPDATE_MONEY = "1004";
    }
}
