package vn.softdreams.easypos.dto.product;

import java.math.BigDecimal;

public interface ProcessingProductItem {
    Integer getId();
    String getStatus();
    BigDecimal getQuantity();
}
