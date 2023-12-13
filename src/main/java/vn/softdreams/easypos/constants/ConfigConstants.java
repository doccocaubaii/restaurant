package vn.softdreams.easypos.constants;

import java.util.List;

public interface ConfigConstants {
    interface BillConfig {
        Integer VAT_AMOUNT_8 = 8;
        Integer VAT_AMOUNT_10 = 10;

        List<Integer> VALUES = List.of(VAT_AMOUNT_8, VAT_AMOUNT_10);
    }

    interface ExtraConfig {
        String PLUS = "+";
        String MUL = "*";
        String SUB = "-";
        String DIV = "*";

        List<String> VALUES = List.of(PLUS, MUL, SUB, DIV);
    }

    interface Variable {
        String TOTAL_PRE_TAX = "total_pre_tax";
        String VAT_AMOUNT = "vat_amount";
        String VAT_RATE = "vat_rate";
        String SVC = "svc";

        List<String> TOTAL_VALUES = List.of(SVC, VAT_AMOUNT, TOTAL_PRE_TAX);

        List<String> SVC_VALUES = List.of(VAT_RATE);
    }

    interface Type {
        Integer NHAN_TUNG_DONG = 1;
        Integer NHAN_THEO_TONG = 2;

        List<Integer> VALUES = List.of(NHAN_TUNG_DONG, NHAN_THEO_TONG);
    }
}
