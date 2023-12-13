package vn.softdreams.easypos.constants;

import java.util.List;
import java.util.Map;

public interface LoyaltyCardUsageConstants {
    interface Type {
        Integer NAP_TIEN = 0;
        Integer NAP_DIEM = 1;
        Integer TRU_DIEM = 2;
        Integer CHI_TIEN = 3;
        Integer CONG_DIEM = 4;
        Integer QUY_DOI = 5;
        Integer CONG_TIEN = 6;
    }

    interface TypeRequestCancelBill {
        List<Integer> values = List.of(Type.CONG_DIEM, Type.CHI_TIEN);
    }

    interface TypeName {
        String NAP_TIEN = "Nạp tiền";
        String NAP_DIEM = "Nạp điểm";
        String TRU_DIEM = "Trừ điểm";
        String CHI_TIEN = "Chi tiền";
        String CONG_DIEM = "Cộng điểm";
        String QUY_DOI = "Quy đổi";
        String CONG_TIEN = "Cộng tiền";
    }

    interface TypeCustomerCardUpdateRequest {
        List<Integer> values = List.of(0, 1, 2, 3);

        Integer NAP_TIEN = 0;
        Integer CONG_DIEM = 1;
        Integer TRU_DIEM = 2;
        Integer QUY_DOI = 3;
    }

    interface TypeMap {
        Map<Integer, String> TYPE_MAP = Map.ofEntries(
            Map.entry(Type.NAP_TIEN, TypeName.NAP_TIEN),
            Map.entry(Type.NAP_DIEM, TypeName.NAP_DIEM),
            Map.entry(Type.TRU_DIEM, TypeName.TRU_DIEM),
            Map.entry(Type.CHI_TIEN, TypeName.CHI_TIEN),
            Map.entry(Type.CONG_DIEM, TypeName.CONG_DIEM),
            Map.entry(Type.QUY_DOI, TypeName.QUY_DOI),
            Map.entry(Type.CONG_TIEN, TypeName.CONG_TIEN)
        );
    }
}
