package vn.softdreams.easypos.constants;

public interface BusinessTypeConstants {
    interface RsInWard {
        String INITIAL = "N2"; //	Khởi tạo kho 2- Nhập kho
        String EDIT_QUANTITY = "N3"; //	Sửa tồn kho 2- Nhập kho
        String EDIT_IN_PRICE = "N4"; //	Sửa giá vốn 2- Nhập kho
        String IN_WARD = "N5"; //	Nhập hàng 2- Nhập kho
        String MC_PAYMENT = "C7"; //	Nhập hàng 1 - Chi
    }

    interface RsOutWard {
        String EDIT_QUANTITY = "X2"; //	Sửa tồn kho 3- Xuất kho
        String EDIT_IN_PRICE = "X3"; //	Sửa giá vốn 3- Xuất kho
        String OUT_WARD = "X4"; //	Bán hàng 3- Xuất kho
        String MC_RECEIPT = "T10"; //	Nhập hàng 0 - Thu
    }

    interface TypeName {
        String RECEIPT = "0";
        String PAYMENT = "1";
        String CANCEL_BILL = "Hủy hàng";
        String RETURN_BILL = "Trả hàng";
    }

    interface Type {
        Integer RECEIPT = 0;
        Integer PAYMENT = 1;
    }

    interface Code {
        String OUT_PRODUCT = "T7";
        String RECEIPT_UNKNOWN = "T1";
        String PAYMENT_UNKNOWN = "C1";
        String OUT_UNKNOWN = "X1";
        String OUT_WARD = "T10";
        String IN_WARD = "C7";
        String DEBT_COLLECTION = "T4"; // thu nợ
        String CANCEL_BILL = "C14";
        String RETURN_BILL = "C13";
    }

    interface Bill {
        String CANCEL = "C14"; // huỷ hàng
        String RETURN = "C13"; // trả hàng
    }
}
