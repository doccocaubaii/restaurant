package vn.softdreams.easypos.constants;

import vn.softdreams.easypos.web.rest.errors.BadRequestAlertException;

import java.math.BigDecimal;
import java.util.Objects;

import static vn.softdreams.easypos.web.rest.errors.ExceptionConstants.*;

public interface ProductConstant {
    interface Active {
        Boolean ACTIVE_TRUE = true;
        Boolean ACTIVE_FALSE = false;
    }

    interface VatRate {
        int VAT_RATE_DEFAULT = -4;
        int VAT_RATE_KCT = -1;
        int VAT_RATE_KTT = -2;
        int VAT_RATE_OTHER = -3;
    }

    interface CONVERSION_UNIT {
        Integer MUL_FORMULA = 0;
        Integer DIV_FORMULA = 1;
    }

    interface TAX_REDUCTION_TYPE {
        String GIAM_TRU_CHUNG = "0";
        String GIAM_TRU_RIENG = "1";
    }

    class VALIDATE {

        private static final String ENTITY_NAME = "ProductUnit";

        public static void validateConversionUnit(BigDecimal convertRate, Integer conversionUnitId, Integer unitId) {
            if (Objects.equals(unitId, conversionUnitId)) {
                throw new BadRequestAlertException(DUPLICATE_WITH_UNIT_VI, ENTITY_NAME, DUPLICATE_WITH_UNIT);
            }
            if (convertRate.compareTo(BigDecimal.ZERO) < 0) {
                throw new BadRequestAlertException(CONVERT_RATE_INVALID_VI, ENTITY_NAME, CONVERT_RATE_INVALID);
            }
        }

        public static String generateDescription(String conversionUnit, BigDecimal convertRate, String unitName, Integer formula) {
            StringBuilder descriptionBuilder = new StringBuilder();
            if (convertRate.compareTo(BigDecimal.ZERO) > 0) {
                descriptionBuilder.append("1 ");
                descriptionBuilder.append(conversionUnit);
                descriptionBuilder.append(" = ");
                if (Objects.equals(formula, ProductConstant.CONVERSION_UNIT.DIV_FORMULA)) {
                    descriptionBuilder.append("1/");
                }
                descriptionBuilder.append(formatConvertRate(convertRate));
                descriptionBuilder.append(" ");
                descriptionBuilder.append(unitName);
            }
            return descriptionBuilder.toString();
        }

        private static String formatConvertRate(BigDecimal convert) {
            return convert.stripTrailingZeros().toPlainString();
        }
    }

    interface IMPORT_EXCEL {
        Integer CODE2 = 0;
        Integer NAME = 1;
        Integer BARCODE = 2;
        Integer GROUP = 3;
        Integer IN_PRICE = 4;
        Integer OUT_PRICE = 5;
        Integer UNIT = 6;
        Integer INVENTORY_TRACKING = 7;
        Integer INVENTORY_COUNT = 8;
        Integer VAT_RATE = 9;
        Integer DESCRIPTION = 10;
    }
}
