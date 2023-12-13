package vn.softdreams.easypos.dto.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ExtraConfig implements Serializable {

    private List<ExtraConfigItem> serviceCharge;
    private List<ExtraConfigItem> totalAmount;

    public List<ExtraConfigItem> getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(List<ExtraConfigItem> serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public List<ExtraConfigItem> getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(List<ExtraConfigItem> totalAmount) {
        this.totalAmount = totalAmount;
    }
}
