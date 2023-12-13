package vn.softdreams.easypos.integration.easybooks88.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.Valid;

public class RegisterCompanyResponse extends CommonResponse {

    @JsonProperty
    private int status;

    @JsonProperty
    private String message;

    @Valid
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public static class Data {

        @JsonProperty
        private Long ebCompanyId;

        @JsonProperty
        private String ebUsername;

        @JsonProperty
        private Long ebRepositoryId;

        public Long getEbCompanyId() {
            return ebCompanyId;
        }

        public void setEbCompanyId(Long ebCompanyId) {
            this.ebCompanyId = ebCompanyId;
        }

        public String getEbUsername() {
            return ebUsername;
        }

        public void setEbUsername(String ebUsername) {
            this.ebUsername = ebUsername;
        }

        public Long getEbRepositoryId() {
            return ebRepositoryId;
        }

        public void setEbRepositoryId(Long ebRepositoryId) {
            this.ebRepositoryId = ebRepositoryId;
        }
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }
}
