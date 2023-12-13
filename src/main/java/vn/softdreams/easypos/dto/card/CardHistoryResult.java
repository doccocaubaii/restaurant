package vn.softdreams.easypos.dto.card;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class CardHistoryResult implements Serializable {

    private Integer id;
    private String cardName;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime usageDate;

    private Integer type;

    private String typeName;
    private String billCode;
    private BigDecimal amount;
    private Double point;
    private String description;

    public CardHistoryResult(
        Integer id,
        String cardName,
        ZonedDateTime usageDate,
        Integer type,
        String billCode,
        BigDecimal amount,
        Double point,
        String description
    ) {
        this.id = id;
        this.cardName = cardName;
        this.usageDate = usageDate;
        this.type = type;
        this.billCode = billCode;
        this.amount = amount;
        this.point = point;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public ZonedDateTime getUsageDate() {
        return usageDate;
    }

    public void setUsageDate(ZonedDateTime usageDate) {
        this.usageDate = usageDate;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Double getPoint() {
        return point;
    }

    public void setPoint(Double point) {
        this.point = point;
    }
}
