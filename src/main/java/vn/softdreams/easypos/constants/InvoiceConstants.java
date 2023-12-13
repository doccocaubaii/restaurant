package vn.softdreams.easypos.constants;

import java.util.List;

public interface InvoiceConstants {
    interface InvoiceMethod {
        Integer TU_DONG = 0;
        Integer THU_CONG = 1;
        Integer HOA_DON_MOI_TAO_LAP = 2;
    }

    interface TaxCheckStatus {
        Integer DANG_KIEM_TRA = -1;
        Integer KHONG_HOP_LE = -2;
        Integer HOP_LE = 1;
        Integer CHUA_PHAT_HANH = 0;
    }

    interface VatRate {
        Integer KTT = -2; // Không tính thuế
        Integer KCT = -1; // Không chịu thuế

        List<Integer> SALE_PRODUCT_STATS_REQUEST = List.of(KCT, KTT);
    }

    interface Status {
        Integer DRAFT = 0;
        Integer PUBLISHED = 1;
        Integer CANCEL = 5;
        Integer THAY_THE = 3;
        Integer DIEU_CHINH = 4;
        Integer BI_HUY = 5;

        String THAY_THE_STRING = "Hoá đơn bị thay thế";
        String DIEU_CHINH_STRING = "Hoá đơn bị điều chỉnh";
        String BI_HUY_STRING = "Hoá đơn bị huỷ";
    }
}
