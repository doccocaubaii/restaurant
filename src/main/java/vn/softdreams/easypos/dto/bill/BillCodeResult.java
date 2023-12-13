package vn.softdreams.easypos.dto.bill;

import java.io.Serializable;

public interface BillCodeResult extends Serializable {
    Integer getId();
    String getBillCode();
    Integer getTypeInv();
}
