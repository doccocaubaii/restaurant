package vn.softdreams.easypos.dto.invoice;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DeclarationGiaPhatResponse implements Serializable {

    @JsonProperty("startDate")
    private String startDate;

    @JsonProperty("pattern")
    private String pattern;

    @JsonProperty("status")
    private Integer status;

    public DeclarationGiaPhatResponse() {}

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
