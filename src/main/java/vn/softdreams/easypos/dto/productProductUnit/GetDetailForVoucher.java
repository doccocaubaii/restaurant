package vn.softdreams.easypos.dto.productProductUnit;

import java.math.BigDecimal;

public interface GetDetailForVoucher {
    Integer getId();
    String getProductName();
    String getUnitName();
    BigDecimal getOnHand();
    BigDecimal getSalePrice();
    String getImage();
}
