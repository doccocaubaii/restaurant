package vn.softdreams.easypos.dto.invoice;

public class declNormalInv {

    private Boolean noCode;
    private Boolean vatInv;
    private Boolean invTransfer;

    public Boolean getNoCode() {
        return noCode;
    }

    public void setNoCode(Boolean noCode) {
        this.noCode = noCode;
    }

    public Boolean getVatInv() {
        return vatInv;
    }

    public void setVatInv(Boolean vatInv) {
        this.vatInv = vatInv;
    }

    public Boolean getInvTransfer() {
        return invTransfer;
    }

    public void setInvTransfer(Boolean invTransfer) {
        this.invTransfer = invTransfer;
    }
}
