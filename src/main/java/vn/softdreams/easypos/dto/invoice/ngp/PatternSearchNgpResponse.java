package vn.softdreams.easypos.dto.invoice.ngp;

import vn.softdreams.easypos.dto.invoice.DeclarationGiaPhatResponse;

import java.util.List;

public class PatternSearchNgpResponse {

    private String status;
    private String message;
    private List<DeclarationGiaPhatResponse> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DeclarationGiaPhatResponse> getData() {
        return data;
    }

    public void setData(List<DeclarationGiaPhatResponse> data) {
        this.data = data;
    }
}
