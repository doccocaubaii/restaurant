package vn.softdreams.easypos.dto.customer;

import java.math.BigDecimal;

public interface CustomerCardItem {
    Integer getCustomerId();
    BigDecimal getTotalAccumulate();
}
