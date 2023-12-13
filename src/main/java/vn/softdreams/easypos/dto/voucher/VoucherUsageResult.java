package vn.softdreams.easypos.dto.voucher;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public interface VoucherUsageResult extends Serializable {
    Integer getVoucherId();
    String getBillCode();

    @JsonFormat(pattern = Constants.ZONED_DATE_FORMAT)
    ZonedDateTime getCreateTime();

    String getCustomerName();
    String getCompanyName();
    BigDecimal getBillValue();
    BigDecimal getVoucherValue();
}
