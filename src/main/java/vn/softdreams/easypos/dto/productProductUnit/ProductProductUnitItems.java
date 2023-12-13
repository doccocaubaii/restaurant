package vn.softdreams.easypos.dto.productProductUnit;

import java.io.Serializable;
import java.math.BigDecimal;

public interface ProductProductUnitItems extends Serializable {
    Integer getProductId();
    String getProductName();
    String getProductCode();
    BigDecimal getOnHand();
    Boolean getInventoryTracking();
    Integer getOverStock();
    Integer getProductProductUnitId();
    Integer getProductUnitId();
    String getProductUnitName();
}
