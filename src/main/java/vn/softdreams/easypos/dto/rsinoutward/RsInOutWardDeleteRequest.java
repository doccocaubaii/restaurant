package vn.softdreams.easypos.dto.rsinoutward;

import com.fasterxml.jackson.annotation.JsonProperty;
import vn.softdreams.easypos.web.rest.errors.ExceptionConstants;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class RsInOutWardDeleteRequest implements Serializable {

    @JsonProperty("comId")
    @NotNull(message = ExceptionConstants.COMPANY_ID_NOT_NULL)
    private Integer comId;

    @JsonProperty("rsInoutwardId")
    @NotNull(message = ExceptionConstants.RS_INOUT_WARD_ID_NOT_NULL)
    private Integer rsInoutwardId;

    @JsonProperty("rsInoutwardCode")
    @NotBlank(message = ExceptionConstants.RS_INOUT_WARD_CODE_NOT_BLANK)
    private String rsInoutwardCode;

    public RsInOutWardDeleteRequest() {}

    public RsInOutWardDeleteRequest(Integer comId, Integer rsInoutwardId, String rsInoutwardCode) {
        this.comId = comId;
        this.rsInoutwardId = rsInoutwardId;
        this.rsInoutwardCode = rsInoutwardCode;
    }

    public Integer getComId() {
        return comId;
    }

    public void setComId(Integer comId) {
        this.comId = comId;
    }

    public Integer getRsInoutwardId() {
        return rsInoutwardId;
    }

    public void setRsInoutwardId(Integer rsInoutwardId) {
        this.rsInoutwardId = rsInoutwardId;
    }

    public String getRsInoutwardCode() {
        return rsInoutwardCode == null ? null : rsInoutwardCode.trim();
    }

    public void setRsInoutwardCode(String rsInoutwardCode) {
        this.rsInoutwardCode = rsInoutwardCode;
    }
}
