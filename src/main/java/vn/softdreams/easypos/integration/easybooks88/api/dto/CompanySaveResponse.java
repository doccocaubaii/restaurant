package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

// response create sub company from EB88
public class CompanySaveResponse implements Serializable {

    @JsonProperty
    private OrganizationUnit organizationUnit;

    @JsonProperty
    private OrganizationUnitOptionReport organizationUnitOptionReport;

    public static class OrganizationUnit {

        private Integer id;
        private Integer parentID;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getParentID() {
            return parentID;
        }

        public void setParentID(Integer parentID) {
            this.parentID = parentID;
        }
    }

    public static class OrganizationUnitOptionReport {

        private Integer id;
        private Integer organizationUnitID;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Integer getOrganizationUnitID() {
            return organizationUnitID;
        }

        public void setOrganizationUnitID(Integer organizationUnitID) {
            this.organizationUnitID = organizationUnitID;
        }
    }

    public OrganizationUnit getOrganizationUnit() {
        return organizationUnit;
    }

    public void setOrganizationUnit(OrganizationUnit organizationUnit) {
        this.organizationUnit = organizationUnit;
    }

    public OrganizationUnitOptionReport getOrganizationUnitOptionReport() {
        return organizationUnitOptionReport;
    }

    public void setOrganizationUnitOptionReport(OrganizationUnitOptionReport organizationUnitOptionReport) {
        this.organizationUnitOptionReport = organizationUnitOptionReport;
    }
}
