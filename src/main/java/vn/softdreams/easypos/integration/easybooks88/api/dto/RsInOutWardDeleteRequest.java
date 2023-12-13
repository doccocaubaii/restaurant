package vn.softdreams.easypos.integration.easybooks88.api.dto;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

public class RsInOutWardDeleteRequest implements Serializable {

    @NotBlank
    private Integer comId;

    @NotBlank
    private String rsInOutWardCode;

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public String getRsInOutWardCode() {
        return rsInOutWardCode;
    }

    public void setRsInOutWardCode(String rsInOutWardCode) {
        this.rsInOutWardCode = rsInOutWardCode;
    }
}
