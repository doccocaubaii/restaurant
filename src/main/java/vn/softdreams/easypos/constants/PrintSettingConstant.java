package vn.softdreams.easypos.constants;

import java.util.HashMap;
import java.util.Map;

public interface PrintSettingConstant {
    interface Type {
        Integer BLUETOOTH = 0;
        Integer WIFI = 1;
    }

    Map<Integer, String> typeTemplateNameMap = new HashMap<>() {
        {
            put(0, "ORDER_PRINT");
            put(1, "BILL_PRINT");
            put(2, "INVOICE_PRINT");
            put(3, "CANCEL_PRINT");
            put(4, "USE_PRINT");
        }
    };

    Map<String, Integer> typeTemplateMap = new HashMap<>() {
        {
            put("ORDER_PRINT", 0);
            put("BILL_PRINT", 1);
            put("INVOICE_PRINT", 2);
            put("CANCEL_PRINT", 3);
            put("USE_PRINT", 4);
        }
    };
}
