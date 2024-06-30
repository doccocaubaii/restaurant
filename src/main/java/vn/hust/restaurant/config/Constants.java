package vn.hust.restaurant.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Application constants.
 */
public final class Constants {

    public static final String HASH_EASY_POS_MD5 = "c0d0ae95e47f72ebf05527b3be47520c";
    // Regex for acceptable logins
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";
    public static final String PHONE_NUMBER_REGEX =
        "^(0|\\+84)(\\s|\\.)?((3[2-9])|(5[689])|(7[06-9])|(8[1-689])|(9[0-46-9]))(\\d)(\\s|\\.)?(\\d{3})(\\s|\\.)?(\\d{3})$";
    public static final String PATTERN_REGEX = "^([1-6])([C|K])(\\d{2})([A-Z]{3})$";
    public static final String TAX_AUTHORITY_CODE_DEFAULT = "00-00-00000-00000000000";
    public static final String PATTERN_MAIL = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
    public static final String CONFIG_VALUE =
        "[{\"code\":\"StoreName\",\"title\":\"Tên cửa hàng\",\"name\":\"Công ty cổ phần đầu tư và công nghệ SoftDreams\",\"fontSize\":22,\"alignText\":1,\"isBold\":true,\"isPrint\":true,\"isHeader\":true,\"isEditable\":true,\"version\":1},{\"code\":\"PhoneNumber\",\"title\":\"Số điện thoại\",\"name\":\"Hotline: 1900 3369\",\"fontSize\":22,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":true,\"version\":1},{\"code\":\"StoreAddress\",\"title\":\"Địa chỉ cửa hàng\",\"name\":\"8 Phạm Hùng, Mễ Trì, Nam Từ Liêm, Hà Nội\",\"fontSize\":22,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":true,\"version\":1},{\"code\":\"Title\",\"title\":\"Tiêu đề\",\"name\":\"HÓA ĐƠN BÁN HÀNG\",\"fontSize\":20,\"alignText\":1,\"isBold\":true,\"isPrint\":true,\"isHeader\":true,\"isEditable\":true,\"version\":1},{\"code\":\"InvTime\",\"title\":\"Mã đơn và thời gian in\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":false,\"version\":1},{\"code\":\"TaxAuthorityCode\",\"title\":\"Mã Cơ quan Thuế\",\"name\":\"\",\"fontSize\":16,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":true,\"version\":1},{\"code\":\"CustomerName\",\"title\":\"Khách\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":false,\"version\":1},{\"code\":\"CustomerTax\",\"title\":\"Mã số thuế\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":false,\"version\":1},{\"code\":\"CustomerCitizenID\",\"title\":\"CCCD\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":true,\"isEditable\":false,\"version\":1},{\"code\":\"TotalProd\",\"title\":\"Tạm tính\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"DiscountInv\",\"title\":\"Giảm giá thêm\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"Total\",\"title\":\"Tổng cộng\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"TotalVatAmount\",\"title\":\"Tổng tiền thuế\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"TotalAmount\",\"title\":\"Khách phải trả\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"CusAmount\",\"title\":\"Khách trả\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"Refund\",\"title\":\"Tiền thừa\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":false,\"version\":1},{\"code\":\"PaymentMethod\",\"title\":\"Thanh toán \",\"name\":\"\",\"fontSize\":18,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":true,\"version\":1},{\"code\":\"Footer\",\"title\":\"Chân hóa đơn\",\"name\":\"CẢM ƠN QUÝ KHÁCH\",\"fontSize\":15,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":true,\"version\":1},{\"code\":\"QrSearch\",\"title\":\"Mã QR tra cứu\",\"name\":\"\",\"fontSize\":15,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":true,\"version\":1},{\"code\":\"PortalLink\",\"title\":\"Tra cứu hoá đơn tại\",\"name\":null,\"fontSize\":15,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":true,\"version\":1},{\"code\":\"Fkey\",\"title\":\"Mã tra cứu\",\"name\":\"\",\"fontSize\":15,\"alignText\":1,\"isBold\":false,\"isPrint\":true,\"isHeader\":false,\"isEditable\":true,\"version\":1},{\"code\":\"Description\",\"title\":\"Mô tả\",\"name\":\"\",\"fontSize\":18,\"alignText\":0,\"isBold\":false,\"isPrint\":false,\"isHeader\":false,\"isEditable\":false,\"version\":1}]";

    public static final String PRODUCT_UNIT_JSON =
        "[{\"name\":\"Bao\",\"description\":null,\"active\":true},{\"name\":\"Bình\",\"description\":null,\"active\":true},{\"name\":\"Bộ\",\"description\":null,\"active\":true},{\"name\":\"Cái\",\"description\":null,\"active\":true},{\"name\":\"Cây\",\"description\":null,\"active\":true},{\"name\":\"Chai\",\"description\":null,\"active\":true},{\"name\":\"Chiếc\",\"description\":null,\"active\":true},{\"name\":\"cm\",\"description\":\"Xentimét\",\"active\":true},{\"name\":\"Cuốn\",\"description\":null,\"active\":true},{\"name\":\"Cuộn\",\"description\":null,\"active\":true},{\"name\":\"Điếu\",\"description\":null,\"active\":true},{\"name\":\"dm\",\"description\":\"Đêximét\",\"active\":true},{\"name\":\"g\",\"description\":\"Gam\",\"active\":true},{\"name\":\"Giờ\",\"description\":null,\"active\":true},{\"name\":\"Gói\",\"description\":null,\"active\":true},{\"name\":\"Hộp\",\"description\":null,\"active\":true},{\"name\":\"Két\",\"description\":null,\"active\":true},{\"name\":\"kg\",\"description\":\"Kilôgam\",\"active\":true},{\"name\":\"Khối\",\"description\":null,\"active\":true},{\"name\":\"Kwh\",\"description\":\"Kilôoát/giờ\",\"active\":true},{\"name\":\"Lần\",\"description\":null,\"active\":true},{\"name\":\"l\",\"description\":\"Lít\",\"active\":true},{\"name\":\"Lọ\",\"description\":null,\"active\":true},{\"name\":\"m\",\"description\":\"Mét\",\"active\":true},{\"name\":\"m2\",\"description\":\"Mét vuông\",\"active\":true},{\"name\":\"m3\",\"description\":\"Mét khối\",\"active\":true},{\"name\":\"ml\",\"description\":\"Mililít\",\"active\":true},{\"name\":\"mm\",\"description\":\"Milimet\",\"active\":true},{\"name\":\"Ngày\",\"description\":null,\"active\":true},{\"name\":\"Phút\",\"description\":null,\"active\":true},{\"name\":\"Quyển\",\"description\":null,\"active\":true},{\"name\":\"Tạ\",\"description\":null,\"active\":true},{\"name\":\"Tấn\",\"description\":null,\"active\":true},{\"name\":\"Thùng\",\"description\":null,\"active\":true},{\"name\":\"Túi\",\"description\":null,\"active\":true},{\"name\":\"Tuýp\",\"description\":null,\"active\":true},{\"name\":\"Vỉ\",\"description\":null,\"active\":true},{\"name\":\"Viên\",\"description\":null,\"active\":true},{\"name\":\"w\",\"description\":\"Oát\",\"active\":true},{\"name\":\"Yến\",\"description\":null,\"active\":true}]";
    public static final String CONFIG_VALUE_JSON =
        "[{  \"code\": \"over_stock\",  \"value\": \"0\",  \"description\": \"0- Không cho phép xuất quá số lượng tồn\\n1- Cho phép xuất quá số lượng tồn\"},{  \"code\": \"type_discount\",  \"value\": \"3\",  \"description\": \"0- Không giảm giá\\n1- Giảm giá theo sản phẩm\\n2- Giảm giá theo đơn hàng\\n3- Giảm giá cả 2 loại\"},{  \"code\": \"invoice_method\",  \"value\": \"1\",  \"description\": \"Cách xuất hóa đơn\\n0- Tự động\\n1- Thủ công\"},{  \"code\": \"invoice_pattern\",  \"value\": null,  \"description\": \"Ký hiệu hóa đơn\"},{  \"code\": \"invoice_type\",  \"value\": \"0\",  \"description\": \"Loại hình hóa đơn\\n0- Hóa đơn bán hàng\\n1- Hóa đơn GTGT một thuế\\n2- Hóa đơn GTGT nhiều thuế\"},{  \"code\": \"round_scale_amount\",  \"value\": \"0\",  \"description\": \"Số lượng số phần thập phân dành cho các trường thành tiền. Mặc định = 0.\"}, {  \"code\": \"round_scale_unit_price\",  \"value\": \"6\",  \"description\": \"Số lượng số phần thập phân dành cho các trường đơn giá. Mặc định = 6\"}, {  \"code\": \"round_scale_quantity\",  \"value\": \"6\",  \"description\": \"Số lượng số phần thập phân dành cho các trường số lượng. Mặc định = 6\"}]\n";
    public static final String BUSINESS_TYPE_JSON =
        "[ {  \"businessTypeCode\": \"T1\",  \"businessTypeName\": \"Chưa phân loại\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T2\",  \"businessTypeName\": \"Lương, thưởng\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T3\",  \"businessTypeName\": \"Bán tài sản\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T4\",  \"businessTypeName\": \"Thu nợ\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T5\",  \"businessTypeName\": \"Vay nợ\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T6\",  \"businessTypeName\": \"Tạm ứng lương\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T7\",  \"businessTypeName\": \"Xuất hàng\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T8\",  \"businessTypeName\": \"Thanh lý\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T9\",  \"businessTypeName\": \"Thu lãi\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T10\",  \"businessTypeName\": \"Bán hàng\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T11\",  \"businessTypeName\": \"Bổ sung vốn\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"T12\",  \"businessTypeName\": \"Khác\",  \"type\": \"0 - Thu\" }, {  \"businessTypeCode\": \"C1\",  \"businessTypeName\": \"Chưa phân loại\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C2\",  \"businessTypeName\": \"Mặt bằng, thuê nhà\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C3\",  \"businessTypeName\": \"Trả lãi, trả nợ\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C4\",  \"businessTypeName\": \"Ăn uống\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C5\",  \"businessTypeName\": \"Điện, nước, internet\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C6\",  \"businessTypeName\": \"Mua sắm\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C7\",  \"businessTypeName\": \"Nhập hàng\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C8\",  \"businessTypeName\": \"Đóng gói hàng hóa\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C9\",  \"businessTypeName\": \"Thuế, phí\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C10\",  \"businessTypeName\": \"Quảng cáo\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C11\",  \"businessTypeName\": \"Thiết bị, dụng cụ\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"C12\",  \"businessTypeName\": \"Khác\",  \"type\": \"1 - Chi\" }, {  \"businessTypeCode\": \"N1\",  \"businessTypeName\": \"Chưa phân loại (Mặc định)\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"N2\",  \"businessTypeName\": \"Khởi tạo kho\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"N3\",  \"businessTypeName\": \"Sửa tồn kho\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"N4\",  \"businessTypeName\": \"Sửa giá vốn\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"N5\",  \"businessTypeName\": \"Nhập hàng\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"N6\",  \"businessTypeName\": \"Khác\",  \"type\": \"2- Nhập kho\" }, {  \"businessTypeCode\": \"X1\",  \"businessTypeName\": \"Chưa phân loại\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X2\",  \"businessTypeName\": \"Sửa tồn kho\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X3\",  \"businessTypeName\": \"Sửa giá vốn\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X4\",  \"businessTypeName\": \"Bán hàng\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X5\",  \"businessTypeName\": \"Xóa sản phẩm\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X6\",  \"businessTypeName\": \"Hủy\",  \"type\": \"3- Xuất \" }, {  \"businessTypeCode\": \"X7\",  \"businessTypeName\": \"Khác\",  \"type\": \"3- Xuất \" }]";
    public static final String SYSTEM = "system";
    public static final String RECEIVABLE_REASON = "Ghi nợ cho đơn hàng ";
    public static final String MC_RECEIPT_REASON = "Thanh toán đơn hàng ";
    public static final String DEFAULT_LANGUAGE = "en";
    public static final int IMAGE_SIZE = 200;
    public static final String[] IMAGE_FORMAT = new String[] { "png", "jpg", "jpeg", "gif" };
    public static final String ZONED_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String ZONED_DATE_TIME_RESERVATION_FORMAT = "yyyy-MM-dd HH:mm";
    public static final String ZONED_DATE_FORMAT = "yyyy-MM-dd";
    public static final String ZONED_DATE_TIME_INVOICE_FORMAT = "dd/MM/yyyy";
    public static final String ZONED_TIME_FORMAT = "HH:mm";
    public static final String ZONED_TIME_ADMIN_WEB_FORMAT = "yyyy-MM-dd'T'HH:mm";
    //    Link
    public static final String BASE_URL_EASY_INVOICE = ".easyinvoice.vn";
    public static final String BASE_HTTP = "http://";
    public static final String BASE_HTTPS = "https://";
    public static final String BASE_COMPANY_URL = "https://sadmin.easyinvoice.vn/api/manager/getcompany";
    public static final String BASE_ACCOUNT = "&username=mobileapi&password=feuYuKN$5v9W";
    //    public static final String BASE_COMPANY_URL = "http://sadmin78.softdreams.vn/api/manager/getcompany";
    //    public static final String BASE_ACCOUNT = "&username=Hieult&password=Lehieu@270495";
    public static final String BASE_SEND_MESSAGE_URL = "http://125.212.226.79:9020/service/sms_api";
    // Code-SEQ
    public static final String VND = "VND";
    public static final String SEQ = "SEQ";

    public static final String PHIEU_THU = "PT";
    public static final String PHIEU_CHI = "PC";
    public static final String DON_HANG = "DH";
    public static final String NHAP_KHO = "NK";
    public static final String XUAT_KHO = "XK";
    public static final String NO_PHAI_THU = "NTH";
    public static final String NO_PHAI_TRA = "NTR";

    public static final String PRODUCT_CODE_DEFAULT = "SP1";
    public static final String PRODUCT_CODE = "SP";
    public static final String PRODUCT_GROUP_CODE = "NSP";
    public static final String CUSTOMER_CODE = "KH";
    public static final String SUPPLIER_CODE = "NCC";
    public static final String CUSTOMER_SUPPLIER_CODE = "KHNCC";
    public static final String USER_CODE = "NV";

    public static final String DEVICE_ID = "DEVICE";

    public static final String RECEIVABLE_CODE = "RE";
    public static final String DEVICE_CODE = "DEVICE"; // phiếu thu từ bán hàng
    public static final String PHIEU_THU_BAN_HANG = "Phiếu thu bán hàng";
    public static final String PHIEU_CHI_BAN_HANG = "Phiếu chi bán hàng";
    public static final String THU_TU_BAN_HANG = "Thu từ bán hàng";
    public static final String XUAT_KHO_BAN_HANG = "Xuất kho bán hàng";
    public static final String NHAP_KHO_HUY_HANG = "Nhập kho huỷ hàng";
    public static final String KHOI_TAO_KHO = "Khởi tạo kho";
    public static final String SUA_TON_KHO = "Sửa tồn kho";
    public static final String SUA_GIA_VON = "Sửa giá vốn";
    public static final String MST = "MST:";
    public static final String ID = "ID:";
    public static final String CUSTOMER_NAME = "Khách lẻ";
    public static final String DISCOUNT = "Chiết khấu";
    public static final String DEFAULT_PRODUCT_CODE = "SP1";

    public static final Integer ROUNDING = 0;
    public static final Integer MESSAGE_TYPE_MAIL = 1;
    public static final Integer MESSAGE_TYPE_SMS = 2;
    public static final List<String> ROUND_SCALE = new ArrayList<>(
        List.of(
            "round_scale_amount",
            "round_scale_unit_price",
            "round_scale_quantity",
            "easyinvoice_url",
            "easyinvoice_account",
            "easyinvoice_pass"
        )
    );

    public static final String ROUND_SCALE_AMOUNT = "round_scale_amount";
    public static final String ROUND_SCALE_UNIT_PRICE = "round_scale_unit_price";
    public static final String ROUND_SCALE_QUANTITY = "round_scale_quantity";
    public static final String EASYINVOICE_URL = "easyinvoice_url";
    public static final String EASYINVOICE_ACCOUNT = "easyinvoice_account";
    public static final String EASYINVOICE_PASS = "easyinvoice_pass";
    public static final String SEND_MESSAGE_REGISTER_CONTENT =
        "Quy khach da dang ky tai khoan thanh cong tai ung dung restaurant. Tai khoan va mat khau cua quy khach la: ";
    public static final String SEND_MESSAGE_REGISTER_SUBJECT = "restaurant - Thông tin khởi tạo tài khoản";
    public static final String SEND_MESSAGE_REGISTER_COMPANY_CONTENT =
        "Quy khach vua duoc khoi tao them don vi su dung moi. Vui long dang nhap he thong restaurant voi thong tin cua quy khach da dang ky tu ban dau.";
    public static final String SEND_MESSAGE_REGISTER_COMPANY_SUBJECT = "restaurant - Thông tin khởi tạo thêm đơn vị sử dụng mới";
    public static final String SEND_MESSAGE_REGISTER =
        "Quy khach da dang ky tai khoan thanh cong tai ung dung restaurant. Tai khoan va mat khau cua quy khach la: ";
    public static final String EB88_COM_ID = "eb88_com_id";

    public static final Integer RS_INWARD_TYPE = 1;
    public static final Integer RS_OUTWARD_TYPE = 2;
    public static final String CODE_PREFIX_EP = "EP";

    //taskLog status
    public static final String PROCESSING_STATUS = "Processing";
    public static final String OK_STATUS = "OK";
    public static final String FAILED_STATUS = "Failed";
    public static final String PROCESSING_STATUS_VI = "Đang xử lý";
    public static final String OK_STATUS_VI = "Thành công";
    public static final String FAILED_STATUS_VI = "Thất bại";
    public static final String ZONED_TIME_FORMAT2 = "HH:mm:ss";

    public interface TableName {
        public static final String CUSTOMER = "CUSTOMER";
        public static final String PRODUCT = "PRODUCT";
        public static final String DEVICE = "OWNER_DEVICE";
        public static final String PRODUCT_GROUP = "PRODUCT_GROUP";
    }

    private Constants() {}
}
