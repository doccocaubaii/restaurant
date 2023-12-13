package vn.softdreams.easypos.dto.invoice;

import java.io.Serializable;

public class DeclarationGiaPhatRequest implements Serializable {

    private String taxcode;

    public DeclarationGiaPhatRequest() {}

    public DeclarationGiaPhatRequest(String taxcode) {
        this.taxcode = taxcode;
    }

    public String getTaxcode() {
        return taxcode;
    }

    public void setTaxcode(String taxcode) {
        this.taxcode = taxcode;
    }
}
