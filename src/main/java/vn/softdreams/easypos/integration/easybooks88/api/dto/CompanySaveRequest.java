package vn.softdreams.easypos.integration.easybooks88.api.dto;

import org.json.JSONObject;

import java.io.Serializable;

public class CompanySaveRequest implements Serializable {

    private OrganizationUnitRequest organizationUnit;
    private Object organizationUnitOptionReport = new JSONObject("{}");

    public static class OrganizationUnitRequest {

        private Integer parentID;
        private Integer id;
        private Integer unitType = 0;
        private String organizationUnitCode;
        private String organizationUnitName;
        private String address;
        private String phoneNumber;
        private Boolean isActive = true;
        private Integer userID;

        public OrganizationUnitRequest() {}

        public Integer getParentID() {
            return parentID;
        }

        public void setParentID(Integer parentID) {
            this.parentID = parentID;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getUnitType() {
            return unitType;
        }

        public void setUnitType(Integer unitType) {
            this.unitType = unitType;
        }

        public String getOrganizationUnitCode() {
            return organizationUnitCode;
        }

        public void setOrganizationUnitCode(String organizationUnitCode) {
            this.organizationUnitCode = organizationUnitCode;
        }

        public String getOrganizationUnitName() {
            return organizationUnitName;
        }

        public void setOrganizationUnitName(String organizationUnitName) {
            this.organizationUnitName = organizationUnitName;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public Boolean getIsActive() {
            return isActive;
        }

        public void seIsActive(Boolean active) {
            isActive = active;
        }

        public Integer getUserID() {
            return userID;
        }

        public void setUserID(Integer userID) {
            this.userID = userID;
        }
    }

    public OrganizationUnitRequest getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnitRequest organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public Object getOrganizationUnitOptionReport() {
        return organizationUnitOptionReport;
    }

    public void setOrganizationUnitOptionReport(Object organizationUnitOptionReport) {
        this.organizationUnitOptionReport = organizationUnitOptionReport;
    }
}
