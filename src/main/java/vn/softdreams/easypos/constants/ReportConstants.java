package vn.softdreams.easypos.constants;

import vn.softdreams.easypos.service.dto.FieldConfig;
import vn.softdreams.easypos.service.dto.HeaderConfig;

import java.util.List;

public interface ReportConstants {
    double CentimeterToPixel = 37.795275591;

    interface ActionType {
        String PDF = "PDF";
    }

    interface THONG_KE_LOI_NHUAN_SAN_PHAM {
        List<HeaderConfig> HeaderConfig = List.of(
            new HeaderConfig("Mã sản phẩm", 8, 0, 2, 3),
            new HeaderConfig("Tên sản phẩm", 8, 3, 2, 3),
            new HeaderConfig("Số lượng sản phẩm", 8, 6, 2, 3),
            new HeaderConfig("Giá trị doanh thu", 8, 9, 2, 3),
            new HeaderConfig("Giá trị lợi nhuận", 8, 12, 2, 3)
        );

        List<FieldConfig> FieldConfig = List.of(
            new FieldConfig("id", "Integer", "text-left"),
            new FieldConfig("name", "String", "text-center"),
            new FieldConfig("quantity", "BigDecimal", "text-left"),
            new FieldConfig("revenue", "BigDecimal", "text-right"),
            new FieldConfig("profit", "BigDecimal", "text-right")
        );
    }

    interface THONG_KE_HANG_HOA_BAN_RA {
        List<HeaderConfig> HeaderConfig = List.of(
            new HeaderConfig("Số hóa đơn", 8, 0, 2, 3),
            new HeaderConfig("Mẫu số hóa đơn", 8, 3, 2, 3),
            new HeaderConfig("Tên khách hàng", 8, 6, 2, 3),
            new HeaderConfig("Mã số thuế khách hàng", 8, 9, 2, 3),
            new HeaderConfig("Doanh số bán chưa có thuế", 8, 12, 2, 3),
            new HeaderConfig("Thuế GTGT", 8, 15, 2, 3),
            new HeaderConfig("Ghi chú", 8, 18, 2, 3)
        );
        List<FieldConfig> FieldConfig = List.of(
            new FieldConfig("no", "String", "text-left"),
            new FieldConfig("pattern", "String", "text-left"),
            new FieldConfig("customerName", "String", "text-left"),
            new FieldConfig("customerTaxCode", "String", "text-left"),
            new FieldConfig("vatAmount", "BigDecimal", "text-right"),
            new FieldConfig("totalPreTax", "BigDecimal", "text-right"),
            new FieldConfig("description", "String", "text-left")
        );
    }

    interface THONG_KE_DOANH_THU_LOI_NHUAN {
        List<HeaderConfig> HeaderConfig = List.of(
            new HeaderConfig("Từ ngày", 8, 0, 2, 3),
            new HeaderConfig("Đến ngày", 8, 3, 2, 3),
            new HeaderConfig("Doanh thu", 8, 6, 2, 3),
            new HeaderConfig("Lợi nhuận", 8, 9, 2, 3)
        );
        List<FieldConfig> FieldConfig = List.of(
            new FieldConfig("fromDate", "String", "text-right"),
            new FieldConfig("toDate", "String", "text-right"),
            new FieldConfig("revenue", "BigDecimal", "text-right"),
            new FieldConfig("profit", "BigDecimal", "text-right")
        );
    }

    interface THONG_KE_SAN_PHAM_BAN_CHAY {
        List<HeaderConfig> HeaderConfig = List.of(
            new HeaderConfig("STT", 3, 0, 2, 1),
            new HeaderConfig("Tên sảm phẩm", 3, 1, 2, 3),
            new HeaderConfig("Tên đơn vị tính", 3, 4, 2, 3),
            new HeaderConfig("Tổng tiền doanh thu", 3, 7, 2, 3),
            new HeaderConfig("Tổng số lượng", 3, 10, 2, 3)
        );
        List<FieldConfig> FieldConfig = List.of(
            new FieldConfig("stt", "Integer", "text-center"),
            new FieldConfig("name", "String", "text-left"),
            new FieldConfig("unitName", "String", "text-center"),
            new FieldConfig("totalAmountStr", "String", "text-right"),
            new FieldConfig("totalQuantityStr", "String", "text-right")
        );
    }
}
