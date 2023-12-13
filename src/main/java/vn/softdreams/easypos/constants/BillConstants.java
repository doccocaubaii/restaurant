package vn.softdreams.easypos.constants;

public interface BillConstants {
    interface TypeInv {
        Integer BAN_HANG = 0;
        Integer MOT_THUE = 1;
        Integer NHIEU_THUE = 2;
    }

    interface PaymentMethod {
        String CASH = "1";
        String TRANSFER = "2";
        String CARD = "Thanh toán qua thẻ";
    }

    interface Status {
        Integer BILL_DONT_COMPLETE = 0;
        Integer BILL_COMPLETE = 1;
        Integer BILL_CANCEL = 2;
        Integer BILL_RETURN = 3;
    }

    interface DeliveryType {
        Integer TAI_CHO = 1;
        Integer MANG_VE = 2;
        Integer GIAO_HANG = 2;
    }

    interface DebtType {
        Integer MAC_DINH = 0;
        Integer NO_PHAI_THU = 1;
        Integer NO_PHAI_TRA = -1;
    }

    interface BillProductFeature {
        Integer HANG_HOA_DICH_VU = 1;
        Integer HANG_HOA_KHUYEN_MAI = 2;
        Integer CHIET_KHAU = 3;
        Integer GHI_CHU = 4;
    }

    interface ValidAuthorityCode {
        Character FIRST_DEFAULT = 'M';
        Character CONNECT = '-';

        Character HOA_DON_GTGT = '1';
        Character HOA_DON_BAN_HANG = '2';
        Character HOA_DON_KHAC = '5';
    }

    interface Buyer {
        String SHOW = "1";
        String HIDDEN = "0";
    }

    interface InvDynamicDiscountName {
        String SHOW = "1";
        String HIDDEN = "0";
    }

    interface Feature {
        Integer CHIET_KHAU = 3;
    }

    String DateConstants = "yyyyMMddHHmmssFF6";
}
