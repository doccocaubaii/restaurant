package vn.softdreams.easypos.dto.company;

import java.io.Serializable;

public abstract class CompanyRequest implements Serializable {

    public Integer comId;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }
}
