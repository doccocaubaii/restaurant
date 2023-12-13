package vn.softdreams.easypos.constants;

public enum ConfigCode {
    EASYINVOICE_URL("easyinvoice_url"),
    EASYINVOICE_ACCOUNT("easyinvoice_account"),
    EASYINVOICE_PASSWORD("easyinvoice_password"),
    EB88_COM_ID("eb88_com_id"),
    EB88_REPOSITORY_ID("eb88_repository_id"),
    EB88_DEFAULT_USER("eb88_default_user"),
    ROUND_SCALE_AMOUNT("round_scale_amount"),
    ROUND_SCALE_UNIT_PRICE("round_scale_unit_price"),
    ROUND_SCALE_QUANTITY("round_scale_quantity");

    final String code;

    ConfigCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
