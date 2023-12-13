package vn.softdreams.easypos.dto.voucher;

import java.io.Serializable;

public class VoucherApplyCardDetail implements Serializable {

    private Integer id;
    private Integer voucherId;
    private Integer cardId;
    private String cardName;
    private Integer totalCustomer;

    public VoucherApplyCardDetail() {}

    public VoucherApplyCardDetail(Integer id, Integer voucherId, Integer cardId, String cardName, Integer totalCustomer) {
        this.id = id;
        this.voucherId = voucherId;
        this.cardId = cardId;
        this.cardName = cardName;
        this.totalCustomer = totalCustomer;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getVoucherId() {
        return voucherId;
    }

    public void setVoucherId(Integer voucherId) {
        this.voucherId = voucherId;
    }

    public Integer getCardId() {
        return cardId;
    }

    public void setCardId(Integer cardId) {
        this.cardId = cardId;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public Integer getTotalCustomer() {
        return totalCustomer;
    }

    public void setTotalCustomer(Integer totalCustomer) {
        this.totalCustomer = totalCustomer;
    }
}
