package vn.softdreams.easypos.constants;

public interface CustomerConstants {
    public interface Active {
        Integer ACTIVE = 1;
        Integer INACTIVE = 0;
        Boolean TRUE = true;
        Boolean FALSE = false;
    }

    interface Type {
        Integer CUSTOMER = 1;
        Integer CUSTOMER_AND_SUPPLIER = 2;
        Integer SUPPLIER = 3;
        Integer OTHER = 4;
    }

    interface TypeName {
        String CUSTOMER = "CUSTOMER";
        String CUSTOMER_AND_SUPPLIER = "CUSTOMER_SUPPLIER";
        String SUPPLIER = "SUPPLIER";
        String OTHER = "OTHER";
    }

    interface TypeNameVi {
        String CUSTOMER = "Khách hàng";
        String CUSTOMER_AND_SUPPLIER = "Khách hàng và Nhà cung cấp";
        String SUPPLIER = "Nhà cung cấp";
    }

    interface IMPORT_EXCEL {
        Integer NAME = 0;
        Integer TYPE = 1;
        Integer CODE2 = 2;
        Integer ADDRESS = 3;
        Integer DISTRICT = 4;
        Integer CITY = 5;
        Integer PHONE = 6;
        Integer EMAIL = 7;
        Integer TAX_CODE = 8;
        Integer ID_NUMBER = 9;
        Integer DESCRIPTION = 10;
    }
}
