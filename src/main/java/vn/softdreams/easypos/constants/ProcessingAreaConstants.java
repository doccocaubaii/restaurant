package vn.softdreams.easypos.constants;

import java.util.HashMap;
import java.util.Map;

public interface ProcessingAreaConstants {
    String NO_PARENT = "Topping lẻ";
    String NO_AREA = "Mang về";

    interface Status {
        Integer PROCESSING = 0;
        Integer PROCESSED = 1;
        Integer DELIVERED = 2;
        Integer DONE = 3;
        Integer CANCELED = 4;
    }

    interface StatusName {
        String IN_DON = "";
        String DANG_CHE_BIEN = "DANG_CHE_BIEN";
        String CHO_CUNG_UNG = "CHO_CUNG_UNG";
        String HOAN_THANH = "HOAN_THANH";
        String HUY = "HUY";
        String NOTIFIED = "NOTIFIED";
        String PROCESSING = "PROCESSING";
        String PROCESSED = "PROCESSED";
        String DONE = "DONE";
        String DELIVERED = "DELIVERED";
        String CANCELED = "CANCELED";
    }

    interface StatusMap {
        Map<Integer, String> statusMap = new HashMap<>() {
            {
                put(Status.PROCESSING, StatusName.PROCESSING);
                put(Status.PROCESSED, StatusName.PROCESSED);
                put(Status.DELIVERED, StatusName.DELIVERED);
                put(Status.DONE, StatusName.DONE);
                put(Status.CANCELED, StatusName.CANCELED);
            }
        };
    }

    //    interface STATUS {
    //        String NOTIFIED = "NOTIFIED";
    //        String PROCESSING = "PROCESSING";
    //        String DONE = "DONE";
    //        String DELIVERED = "DELIVERED";
    //        String CANCELED = "CANCELED";
    //    }

    interface Setting {
        Integer MAY_IN = 0;
        Integer NHA_BEP = 1;
        Integer CA_HAI = 2;
    }

    interface Type {
        Integer UU_TIEN = 1;
        Integer THEO_MON = 2;
        Integer THEO_BAN = 3;
    }

    interface ChangeStatusType {
        Integer MOT = 1;
        Integer TAT_CA = 2;
    }

    interface PrintType {
        Integer TAO_DON = 0;
        Integer CHE_BIEN = 2;
        Integer HUY_MON = 3;
    }

    interface ProcessingAreaActive {
        Integer DELETE = -1;
        Integer NOT_ACTIVE = 0;
        Integer ACTIVE = 1;
    }

    interface ProcessingAreaSetting {
        Integer PRINT = 0;
        Integer KITCHEN = 1;
        Integer BOTH = 2;
    }
}
