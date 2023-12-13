package vn.softdreams.easypos.constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface VoucherConstants {
    interface Status {
        Integer NOT_RUN = 0;
        Integer RUNNING = 1;
        Integer STOP = 2;
        Integer DONE = 3;
        Integer DELETED = -1;
    }

    interface Type {
        Integer VOUCHER_DISCOUNT = 300;
        Integer VOUCHER_DISCOUNT_PERCENTAGE_WEB = 1;
        Integer VOUCHER_DISCOUNT_AMOUNT_WEB = 2;
        Integer VOUCHER_DISCOUNT_PERCENTAGE = 0;
        Integer VOUCHER_DISCOUNT_AMOUNT = 1;
    }

    interface TypeRequestGetAll {
        Integer IS_VOUCHER = 1;
        Integer IS_PROMOTIONS = 2;
        List<Integer> VALID = List.of(IS_VOUCHER, IS_PROMOTIONS);
    }

    interface DiscountType {
        Integer VOUCHER_DISCOUNT = 300;
        Integer VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT = 100;
        Integer VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT = 102;
        Integer VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT = 200;
        List<Integer> VALID = List.of(
            VOUCHER_DISCOUNT,
            VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT,
            VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT,
            VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT
        );

        List<Integer> CONDITION_EXTEND = List.of(
            VOUCHER_DISCOUNT_BILL_TOTAL_AMOUNT,
            VOUCHER_DISCOUNT_BILL_BONUS_PRODUCT,
            VOUCHER_DISCOUNT_BILL_BUY_AND_BONUS_PRODUCT
        );
    }

    interface ExtTimeConditionType {
        Integer MONTHS = 0;
        Integer DAYS = 1;
        Integer DAYS_OF_THE_WEEK = 2;
        Integer TIME_SLOTS = 3;
        Integer IGNORE_DAYS = 4;
    }

    interface ExtTimeConditionMap {
        Map<Integer, String> VALUE = new HashMap<>() {
            {
                put(ExtTimeConditionType.MONTHS, ExtTimeConditionTypeName.MONTHS);
                put(ExtTimeConditionType.DAYS, ExtTimeConditionTypeName.DAYS);
                put(ExtTimeConditionType.DAYS_OF_THE_WEEK, ExtTimeConditionTypeName.DAYS_OF_THE_WEEK);
                put(ExtTimeConditionType.TIME_SLOTS, ExtTimeConditionTypeName.TIME_SLOTS);
                put(ExtTimeConditionType.IGNORE_DAYS, ExtTimeConditionTypeName.IGNORE_DAYS);
            }
        };
    }

    interface ExtTimeConditionTypeName {
        String MONTHS = "months";
        String DAYS = "days";
        String DAYS_OF_THE_WEEK = "days_of_the_week";
        String TIME_SLOTS = "time_slots";
        String IGNORE_DAYS = "ignore_days";
        List<String> VALID = List.of(MONTHS, DAYS, DAYS_OF_THE_WEEK, TIME_SLOTS, IGNORE_DAYS);
    }

    interface ExtTimeDOWMap {
        String MONDAY = "MON";
        String TUESDAY = "TUE";
        String WEDNESDAY = "WED";
        String THURSDAY = "THU";
        String FRIDAY = "FRI";
        String SATURDAY = "SAT";
        String SUNDAY = "SUN";
        List<String> VALUES = List.of(MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY);
        Map<Integer, String> VALUE_MAP = new HashMap<>() {
            {
                put(2, MONDAY);
                put(3, TUESDAY);
                put(4, WEDNESDAY);
                put(5, THURSDAY);
                put(6, FRIDAY);
                put(7, SATURDAY);
                put(8, SUNDAY);
            }
        };
    }

    interface ApplyType {
        Integer APPLY_CARD = 1;
        Integer APPLY_CUSTOMER = 2;
        Integer APPLY_ALL_CUSTOMER = 3;
        String APPLY_CARD_NAME = "Thẻ";
        String APPLY_CUSTOMER_NAME = "Khách hàng";
    }

    interface Description {
        String DEFAULT = "Giảm %s cho giá trị đơn hàng";
        String TOTAL_AMOUNT_MAX_NULL = "Giảm %s cho giá trị đơn hàng từ %s trở lên";
        String TOTAL_AMOUNT_DEFAULT = "Giảm %s cho giá trị đơn hàng từ %s đến %s";
        String BONUS_PRODUCT_MAX_NULL = "Tặng %s sản phẩm %s cho giá trị đơn hàng từ %s trở lên";
        String BONUS_PRODUCT_DEFAULT = "Tặng %s sản phẩm %s cho giá trị đơn hàng từ %s đến %s";
        String BUY_AND_BONUS_AMOUNT_DEFAULT = "Mua %s sản phẩm %s được khuyến mãi %s sản phẩm %s";
    }

    String DESCRIPTION = "Giảm %s cho giá trị đơn hàng % đến %s";
}
