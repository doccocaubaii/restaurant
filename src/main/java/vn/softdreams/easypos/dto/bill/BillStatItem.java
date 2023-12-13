package vn.softdreams.easypos.dto.bill;

import java.math.BigDecimal;

public class BillStatItem {

    private String time;
    private BigDecimal money;

    public BillStatItem() {}

    public BillStatItem(String time, BigDecimal money) {
        this.time = time;
        this.money = money;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}
