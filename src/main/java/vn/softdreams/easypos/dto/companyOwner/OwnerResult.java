package vn.softdreams.easypos.dto.companyOwner;

import com.fasterxml.jackson.annotation.JsonFormat;
import vn.softdreams.easypos.config.Constants;

import java.io.Serializable;
import java.time.ZonedDateTime;

public class OwnerResult implements Serializable {

    private Integer id;
    private String name;
    private String address;
    private String taxCode;
    private String ownerName;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime createTime;

    @JsonFormat(pattern = Constants.ZONED_DATE_TIME_FORMAT)
    private ZonedDateTime updateTime;

    private Boolean statusConfig;

    public OwnerResult() {}

    public OwnerResult(
        Integer id,
        String name,
        String address,
        String taxCode,
        String ownerName,
        ZonedDateTime createTime,
        ZonedDateTime updateTime
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.taxCode = taxCode;
        this.ownerName = ownerName;
        this.createTime = createTime;
        this.updateTime = updateTime;
    }

    public OwnerResult(
        Integer id,
        String name,
        String address,
        String taxCode,
        String ownerName,
        ZonedDateTime createTime,
        ZonedDateTime updateTime,
        Boolean statusConfig
    ) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.taxCode = taxCode;
        this.ownerName = ownerName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.statusConfig = statusConfig;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public ZonedDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(ZonedDateTime createTime) {
        this.createTime = createTime;
    }

    public ZonedDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(ZonedDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getStatusConfig() {
        return statusConfig;
    }

    public void setStatusConfig(Boolean statusConfig) {
        this.statusConfig = statusConfig;
    }
}
