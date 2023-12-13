package vn.softdreams.easypos.dto.processingRequest;

import java.math.BigDecimal;

public interface ToppingNumber {
    Integer getId();
    Integer getRequestDetailId();
    BigDecimal getNumber();
}
