package vn.softdreams.easypos.constants;

public interface LoyaltyCardConstants {
    interface Status {
        Integer NGUNG_HOAT_DONG = 0;
        Integer HOAT_DONG = 1;
        Integer DA_XOA = -1;
    }

    interface Type {
        Integer NAP_TIEN = 0;
        Integer NAP_DIEM = 1;
        Integer TRU_DIEM = 2;
        Integer CHI_TIEN = 3;
        Integer CONG_DIEM = 4;
        Integer QUY_DOI = 5;
        Integer CONG_TIEN = 6;
    }
}
