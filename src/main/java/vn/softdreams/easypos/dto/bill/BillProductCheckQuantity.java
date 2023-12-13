package vn.softdreams.easypos.dto.bill;

import java.io.Serializable;
import java.math.BigDecimal;

public interface BillProductCheckQuantity extends Serializable {
    // lấy số tổng lượng của từng sản phẩm trong bill theo product_product_unit_id
    // kiểm tra khi trả hàng
    Integer getProductProductUnitId();
    BigDecimal getTotalQuantity();
}
