package vn.softdreams.easypos.web.rest.errors;

public final class ExceptionConstants {

    public static final String PRODUCT_GROUP_DUPLICATE_CODE = "PRODUCT_GROUP_DUPLICATE_CODE";
    public static final String PRODUCT_GROUP_DUPLICATE_CODE_VI = "Mã danh mục đã trùng";
    public static final String PRODUCT_GROUP_DUPLICATE_NAME = "PRODUCT_GROUP_DUPLICATE_NAME";
    public static final String PRODUCT_GROUP_DUPLICATE_NAME_VI = "Tên danh mục đã tồn tại";
    public static final String USER_NOT_FOUND = "USER_NOT_FOUND";
    public static final String BAD_REQUEST_MULTIPLE = "BAD_REQUEST_MULTIPLE";
    public static final String BAD_REQUEST = "BAD_REQUEST";
    public static final String BAD_REQUEST_VI = "Dữ liệu gửi lên không hợp lệ";
    public static final String USER_NOT_FOUND_VI = "Tài khoản không tồn tại";
    public static final String UNAUTHORIZED = "UNAUTHORIZED";
    public static final String UNAUTHORIZED_VI = "Bạn không có quyền truy cập chức năng này";
    public static final String INTERNAL_SERVER = "INTERNAL_SERVER";
    public static final String EXCEPTION_ERROR = "EXCEPTION_ERROR";
    public static final String EXCEPTION_ERROR_VI = "Lỗi hệ thống";
    public static final String BAD_CREDENTIALS = "BAD_CREDENTIALS";
    public static final String BAD_CREDENTIALS_VI = "Tài khoản không hợp lệ";

    public static final String TOTAL_PRE_TAX_IS_WRONG = "TOTAL_PRE_TAX_IS_WRONG";
    public static final String TOTAL_PRE_TAX_IS_WRONG_VI = "Tổng tiền trước thuế không đúng";

    public static final String VAT_AMOUNT_IS_WRONG = "VAT_AMOUNT_IS_WRONG";
    public static final String VAT_AMOUNT_IS_WRONG_VI = "Tổng tiền thuế không đúng";

    public static final String TOTAL_AMOUNT_IS_WRONG = "TOTAL_AMOUNT_IS_WRONG";
    public static final String TOTAL_AMOUNT_IS_WRONG_VI = "Tổng tiền thanh toán không đúng";

    public static final String CUSTOMER_NOT_FOUND = "CUSTOMER_NOT_FOUND";
    public static final String CUSTOMER_NOT_FOUND_VI = "Không tìm thấy thông tin khách hàng";
    public static final String SUPPLIER_NOT_FOUND_VI = "Không tìm thấy thông tin nhà cung cấp";
    public static final String CUSTOMER_SUPPLIER_NOT_FOUND_VI = "Không tìm thấy thông tin khách hàng/nhà cung cấp";

    public static final String CUSTOMER_ID_NOT_FOUND = "CUSTOMER_ID_NOT_FOUND";
    public static final String CUSTOMER_SUPPLIER_NOT_FOUND = "CUSTOMER_SUPPLIER_NOT_FOUND";
    public static final String CUSTOMER_ID_NOT_FOUND_VI = "Id khách hàng không hợp lệ";
    public static final String VAT_RATE_INVALID = "VAT_RATE_INVALID";
    public static final String VAT_RATE_INVALID_VI = "Thuế suất không hợp lệ";

    public static final String PRODUCT_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String PRODUCT_NOT_FOUND_VI = "Không tìm thấy thông tin sản phẩm";
    public static final String PRODUCT_ENABLE_TRACKING_INVALID_VI = "Sản phẩm này đã bật theo dõi tồn kho";
    public static final String PRODUCT_ENABLE_TRACKING_INVALID = "PRODUCT_ENABLE_TRACKING_INVALID";
    public static final String PRODUCT_ENABLE_TRACKING_FALSE_VI = "Sản phẩm này chưa bật theo dõi tồn kho";
    public static final String PRODUCT_ENABLE_TRACKING_FALSE = "PRODUCT_ENABLE_TRACKING_FALSE";

    public static final String PRODUCT_ENABLE_TRACKING_ERROR_VI = "Không bật tồn kho với sản phẩm có đơn vị tính chuyển đổi";
    public static final String PRODUCT_ENABLE_TRACKING_ERROR = "PRODUCT_ENABLE_TRACKING_ERROR";
    public static final String PRODUCT_NOT_DELETE = "PRODUCT_NOT_DELETE";
    public static final String PRODUCT_NOT_DELETE_VI = "Sản phẩm '@@name' có đơn hàng đang xử lý, không thể xóa";
    public static final String PRODUCT_DEFAULT_NOT_DELETE_VI = "Sản phẩm không thể xóa!";
    public static final String PRODUCT_PRODUCT_UNIT_NOT_DELETE = "PRODUCT_PRODUCT_UNIT_NOT_DELETE";
    public static final String PRODUCT_PRODUCT_UNIT_NOT_DELETE_VI = "Đơn vị tính đã phát sinh chứng từ, không thể xóa";

    public static final String PRODUCT_UNIT_NOT_UPDATE = "PRODUCT_UNIT_NOT_UPDATE";
    public static final String PRODUCT_UNIT_NOT_UPDATE_VI = "Sản phẩm đã phát sinh chứng từ, không thể thay đổi đơn vị tính";

    public static final String BILL_PRODUCTS_IS_EMPTY = "BILL_PRODUCTS_IS_EMPTY";
    public static final String BILL_PRODUCTS_IS_EMPTY_VI = "Danh sách sản phẩm rỗng";

    public static final String BILL_PAYMENTS_IS_EMPTY = "BILL_PAYMENTS_IS_EMPTY";
    public static final String BILL_PAYMENTS_IS_EMPTY_VI = "Danh sách thanh toán rỗng";

    public static final String BILL_DISCOUNT_IS_WRONG = "BILL_DISCOUNT_IS_WRONG";
    public static final String BILL_DISCOUNT_IS_WRONG_VI = "Giảm giá đơn hàng không đúng";

    public static final String BILL_ID_NOT_FOUND = "BILL_ID_NOT_FOUND";
    public static final String BILL_ID_NOT_FOUND_VI = "Không tìm thấy id đơn hàng";

    public static final String BILL_NOT_FOUND = "BILL_NOT_FOUND";
    public static final String BILL_NOT_FOUND_VI = "Không tìm thấy đơn hàng";

    public static final String BILL_STATUS_INVALID = "BILL_STATUS_INVALID";
    public static final String BILL_STATUS_INVALID_VI = "Trạng thái đơn hàng không hợp lệ";

    public static final String BILL_PRODUCT_NOT_VALID = "BILL_PRODUCT_NOT_VALID";
    public static final String BILL_PRODUCT_NOT_VALID_VI = "Sản phẩm [@@] đã cập nhật đơn vị tính. Vui lòng Chỉnh sửa hoặc Huỷ đơn hàng.";

    public static final String BILL_NOT_EXIST = "BILL_NOT_EXIST";
    public static final String BILL_NOT_EXIST_VI = "Đơn hàng không tồn tại";
    public static final String BILL_NOT_CANCEL = "BILL_NOT_CANCEL";
    public static final String BILL_NOT_CANCEL_VI = "Đơn hàng không được hủy";
    public static final String BILL_NOT_UPDATE = "BILL_NOT_UPDATE";
    public static final String BILL_NOT_UPDATE_VI = "Đơn hàng không được phép cập nhập";
    public static final String BILL_CANNOT_UPDATE = "BILL_CANNOT_UPDATE";
    public static final String BILL_CANNOT_UPDATE_VI = "Không thể sửa đơn nếu khách hàng hoặc sản phẩm đã xóa";
    public static final String BILL_CANNOT_DONE = "BILL_CANNOT_DONE";
    public static final String BILL_CANNOT_DONE_VI = "Không thể hoàn thành đơn nếu khách hàng hoặc sản phẩm đã xóa";
    public static final String BILL_NOT_DONE_PRODUCT_VI = "Không thể hoàn thành đơn có sản phẩm đã xóa";
    public static final String BILL_NOT_DONE_CUSTOMER_VI = "Không thể hoàn thành đơn có khách hàng đã xóa";
    public static final String BILL_CANNOT_DONE_CUSTOMER = "BILL_CANNOT_DONE_CUSTOMER";
    public static final String BILL_CANNOT_DONE_CUSTOMER_VI = "Không thể sửa đơn hàng có khách hàng đã xóa";
    public static final String BILL_CANNOT_DONE_PRODUCT = "BILL_CANNOT_DONE_PRODUCT";
    public static final String BILL_CANNOT_DONE_PRODUCT_VI = "Không thể sửa đơn hàng có sản phẩm đã xóa";

    public static final String BILL_AMOUNT_GREATER_DEBT = "BILL_AMOUNT_GREATE_DEBT";
    public static final String BILL_AMOUNT_GREATER_DEBT_VI = "Số tiền trả lớn hơn số tiền nợ";

    public static final String BILL_NOT_DEBT = "BILL_NOT_DEBT";
    public static final String BILL_NOT_DEBT_VI = "Đơn hàng không nợ";

    public static final String BILLS_INVALID = "BILLS_INVALID";
    public static final String BILLS_INVALID_VI = "Danh sách đơn hàng không hợp lệ";

    public static final String DATE_TIME_INVALID = "DATE_TIME_INVALID";
    public static final String DATE_TIME_INVALID_VI = "Ngày không đúng định dạng";

    public static final String ID_MUST_NOT_NULL = "ID_MUST_NOT_NULL";
    public static final String ID_MUST_NOT_NULL_VI = "ID không được trống";
    public static final String ID_INVALID = "ID_INVALID";
    public static final String ID_INVALID_VI = "ID không hợp lệ";
    public static final String ID_MUST_BE_NULL = "ID_MUST_BE_NULL";
    public static final String ID_MUST_BE_NULL_VI = "ID phải để trống";
    public static final String INVALID_EMAIL = "INVALID_EMAIL";
    public static final String INVALID_EMAIL_VI = "Email không hợp lệ";

    public static final String PRODUCT_CODE_DUPLICATED = "PRODUCT_CODE_DUPLICATED";
    public static final String PRODUCT_CODE_DUPLICATED_VI = "Trùng mã sản phẩm";
    public static final String PRODUCT_BARCODE_DUPLICATED = "PRODUCT_BARCODE_DUPLICATED";
    public static final String PRODUCT_BARCODE_DUPLICATED_VI = "Trùng mã vạch sản phẩm";
    public static final String INVALID_PRODUCT = "INVALID_PRODUCT";
    public static final String INVALID_PRODUCT_VI = "Sản phẩm không hợp lệ";

    public static final String INVALID_PRODUCT_GROUP = "INVALID_PRODUCT_GROUP";
    public static final String INVALID_PRODUCT_GROUP_VI = "Danh mục không hợp lệ";
    public static final String PRODUCT_GROUP_NOT_FOUND = "PRODUCT_NOT_FOUND";
    public static final String PRODUCT_GROUP_NOT_FOUND_VI = "Không tìm thấy danh mục";
    public static final String PRODUCT_GROUP_NOT_BLANK_VI = "Tên danh mục trống";

    public static final String EMPLOYEE_DELETE_FAIL_CODE = "EMPLOYEE_DELETE_FAIL_CODE";
    public static final String EMPLOYEE_DELETE_FAIL_VI = "Bạn không có quyền xóa nhân viên này";
    public static final String EMPLOYEE_NOT_FOUND_CODE = "EMPLOYEE_NOT_FOUND_CODE";
    public static final String EMPLOYEE_NOT_FOUND_VI = "Không tìm thấy nhân viên";
    public static final String EMPLOYEE_COM_ID_INVALID = "EMPLOYEE_COM_ID_INVALID";
    public static final String EMPLOYEE_USER_NAME_AND_PHONE_NOT_MATCH = "EMPLOYEE_USER_NAME_AND_PHONE_NOT_MATCH";
    public static final String EMPLOYEE_USER_NAME_AND_PHONE_NOT_MATCH_VI = "Username và số điện thoại phải trùng nhau.";

    public static final String EMPLOYEE_COM_ID_INVALID_VI = "Mã công ty không hợp lệ";
    public static final String PASSWORD_DUPLICATE_CODE = "PASSWORD_DUPLICATE_CODE";
    public static final String PASSWORD_DUPLICATE_VI = "Mật khẩu mới không được trùng mật khẩu cũ";
    public static final String PASSWORD_VALID = "PASSWORD_IN_VALID";
    public static final String PASSWORD_INVALID = "PASSWORD_INVALID";
    public static final String PASSWORD_VALID_VI = "Mật khẩu không đúng";
    public static final String PASSWORD_INVALID_VI = "Mật khẩu không hợp lệ";
    public static final String USER_ROLE_NOT_EXISTS_CODE = "USER_ROLE_NOT_EXISTS_CODE";
    public static final String USER_ROLE_NOT_EXISTS_VI = "Vai trò người dùng không hợp lệ";
    public static final String USER_ROLE_EXISTS_VI = "Vai trò vẫn còn người dùng được gán";
    public static final String USER_ROLE_EXISTS = "USER_ROLE_EXISTS";

    public static final String EMPLOYEE_ID_MUST_NOT_NULL_VI =
        "{\"code\": \"EMPLOYEE_ID_MUST_NOT_NULL_VI\",\"message\": \"Mã nhân viên không được trống\"}";
    public static final String EMPLOYEE_COM_ID_MUST_NOT_NULL =
        "{\"code\": \"EMPLOYEE_COM_ID_MUST_NOT_NULL\",\"message\": \"mã công ty của nhân viên không được để trống\"}";

    public static final String CONFIRM_PASSWORD_CODE_VI = "Mật khẩu và Mật khẩu xác nhận không trùng nhau";
    public static final String CONFIRM_PASSWORD_CODE = "CONFIRM_PASSWORD_CODE";

    public static final String PASSWORD_INVALID_SPACE_VI = "Mật khẩu không được chứa dấu cách";
    public static final String EMPLOYEE_NAME_MUST_NOT_EMPTY =
        "{\"code\": \"EMPLOYEE_NAME_MUST_NOT_EMPTY\",\"message\": \"Tên nhân viên không được để trống\"}";
    public static final String EMPLOYEE_ROLE_NAME_MUST_NOT_EMPTY =
        "{\"code\": \"EMPLOYEE_ROLE_NAME_MUST_NOT_EMPTY\",\"message\": \"Tên vai trò không được để trống\"}";
    public static final String EMPLOYEE_NAME_NOT_EMPTY_CODE = "EMPLOYEE_NAME_NOT_EMPTY_CODE";
    public static final String EMPLOYEE_NAME_NOT_EMPTY_VI = "Tên nhân viên không được để trống";
    public static final String EMPLOYEE_NAME_INVALID =
        "{\"code\": \"EMPLOYEE_NAME_INVALID\",\"message\": \"Tên của nhân viên không hợp lệ\"}";
    public static final String EMPLOYEE_ROLE_ID_MUST_NOT_NULL =
        "{\"code\": \"EMPLOYEE_ROLE_ID_MUST_NOT_NULL\",\"message\": \"mã vai trò của nhân viên không được để trống\"}";
    public static final String EMPLOYEE_USERNAME_MUST_NOT_NULL =
        "{\"code\": \"EMPLOYEE_USERNAME_MUST_NOT_NULL\",\"message\": \"Tên đăng nhập của nhân viên không được để trống\"}";
    public static final String EMPLOYEE_USERNAME_INVALID =
        "{\"code\": \"EMPLOYEE_USERNAME_INVALID\",\"message\": \"Tên đăng nhập của nhân viên không hợp lệ\"}";
    public static final String EMPLOYEE_MAIL_NOT_NULL =
        "{\"code\": \"EMPLOYEE_MAIL_NOT_NULL\",\"message\": \"Email nhân viên không để trống\"}";
    public static final String EMPLOYEE_MAIL_INVALID =
        "{\"code\": \"EMPLOYEE_MAIL_INVALID\",\"message\": \"Email nhân viên không hợp lệ\"}";
    public static final String COM_ID_INVALID = "COM_ID_INVALID";
    public static final String COM_ID_INVALID_VI = "mã công ty không hợp lệ";
    public static final String USER_NAME_EXISTED_VI = "Tên tài khoản đã tồn tại";
    public static final String USER_NAME_EXISTED_CODE = "USER_NAME_EXISTED_CODE";
    public static final String ROLE_NOT_FOUND_VI = "Vai trò không tồn tại";
    public static final String ROLE_NOT_FOUND = "ROLE_NOT_FOUND";
    public static final String ROLE_NAME_INVALID_VI = "Tên vai trò không hợp lệ";
    public static final String ROLE_NAME_INVALID = "ROLE_NAME_INVALID";

    public static final String TAX_AUTHORITY_CODE_INVALID = "TAX_AUTHORITY_CODE_INVALID";
    public static final String TAX_AUTHORITY_CODE_INVALID_VI = "Mã cơ quan thuế không hợp lệ";
    public static final String TAX_AUTHORITY_CODE_ALREADY = "TAX_AUTHORITY_CODE_ALREADY";
    public static final String TAX_AUTHORITY_CODE_ALREADY_VI = "Mã cơ quan thuế đã tồn tại";

    public static final String TAX_AUTHORITY_CODE_IS_NULL = "TAX_AUTHORITY_CODE_IS_NULL";
    public static final String TAX_AUTHORITY_CODE_IS_NULL_VI = "Mã cơ quan thuế không được bỏ trống";

    public static final String AMOUNT_IS_WRONG = "AMOUNT_IS_WRONG";
    public static final String AMOUNT_IS_WRONG_VI = "Tổng tiền không đúng";

    public static final String INVOICE_ALREADY = "INVOICE_ALREADY";
    public static final String INVOICE_ALREADY_VI = "Hóa đơn đã tồn tại";

    public static final String BILL_TYPE_SELL = "BILL_TYPE_SELL";
    public static final String BILL_TYPE_SELL_VI = "Loại hình bán hàng không được có thuế";

    public static final String PATTERN_NOT_FOUND = "PATTERN_NOT_FOUND";
    public static final String PATTERN_NOT_FOUND_VI = "Không tìm thấy ký hiệu hóa đơn";

    public static final String PATTERN_IN_VALID = "PATTERN_NOT_FOUND";
    public static final String PATTERN_IN_VALID_VI = "Ký hiệu hóa đơn không hợp lệ";

    public static final String PRODUCT_IN_VALID = "PRODUCT_IN_VALID";
    public static final String PRODUCT_IN_VALID_VI = "Danh sách sản phẩm không hợp lệ";
    public static final String UNIT_IN_VALID = "UNIT_IN_VALID";
    public static final String UNIT_IN_VALID_VI = "Danh sách đơn vị không hợp lệ";

    public static final String UNIT_ID_NOT_NULL = "UNIT_ID_NOT_NULL";
    public static final String UNIT_ID_NOT_NULL_VI = "UnitID không được để trống";
    public static final String PRODUCT_CODE_INVALID_VI = "Mã sản phẩm không hợp lệ";
    public static final String PRODUCT_CODE_NOT_NULL_VI = "Mã sản phẩm không được để trống";

    public static final String PRINT_CONFIG_SAVE_ALL = "PRINT_CONFIG_SAVE_ALL";
    public static final String PRINT_CONFIG_SAVE_ALL_VI = "Lưu danh sách cấu hình máy in lỗi";

    public static final String PRODUCT_UNIT_SAVE_ALL_ERROR = "PRODUCT_UNIT_SAVE_ALL_ERROR";
    public static final String PRODUCT_UNIT_SAVE_ALL_ERROR_VI = "Lưu danh sách đơn vị sản phẩm lỗi";

    public static final String BUSINESS_TYPE_SAVE_ALL_ERROR = "BUSINESS_TYPE_SAVE_ALL_ERROR";
    public static final String BUSINESS_TYPE_SAVE_ALL_ERROR_VI = "Lưu danh sách phân loại nghiệp vụ lỗi";

    public static final String CONFIG_SAVE_DEFAULT_ERROR_CODE = "CONFIG_SAVE_DEFAULT_ERROR_CODE";
    public static final String CONFIG_SAVE_DEFAULT_ERROR_VI = "Lưu cấu hình lỗi";

    public static final String REGISTER_CONFIG_ERROR_CODE = "REGISTER_CONFIG_ERROR_CODE";
    public static final String REGISTER_CONFIG_ERROR_VI = "Đăng kí thông tin cấu hình EB88 không thành công";

    public static final String PAYMENT_METHOD_IS_VALID = "PAYMENT_METHOD_NOT_NULL";
    public static final String PAYMENT_METHOD_IS_VALID_VI = "Phương thức thanh toán không hợp lệ";

    public static final String ARISING_DATE_IS_VALID = "ARISING_DATE_IS_VALID";
    public static final String ARISING_DATE_IS_VALID_VI = "Ngày hóa đơn không được lớn hơn ngày hiện tại";
    public static final String DATE_TIME_INVALID_CODE = "DATE_TIME_INVALID_CODE";

    public static final String AMOUNT_IS_VALID = "AMOUNT_IS_VALID";
    public static final String AMOUNT_IS_VALID_VI = "Phương thức thanh toán không hợp lệ";
    public static final String QUANTITY_IS_VALID = "QUANTITY_IS_VALID";
    public static final String QUANTITY_IS_VALID_VI = "Số lượng phải lớn hơn 0";

    public static final String QUANTITY_IS_INVALID = "QUANTITY_IS_VALID";
    public static final String QUANTITY_IS_INVALID_VI = "Số lượng không đúng định dạng";

    public static final String ID_NOT_NULL = "{\"code\": \"ID_NOT_NULL\",\"message\": \"Id không được bỏ trống\"}";
    public static final String GROUP_ID_NOT_NULL =
        "{\"code\": \"GROUP_ID_NOT_NULL\",\"message\": \"Id nhóm sản phẩm không được bỏ trống\"}";

    public static final String TAX_AUTHORITY_NOT_NULL =
        "{\"code\": \"TAX_AUTHORITY_NOT_NULL\",\"message\": \"Mã cơ quan thuế không được để trống\"}";
    public static final String BILL_STATUS_NOT_NULL =
        "{\"code\": \"BILL_STATUS_NOT_NULL\",\"message\": \"Trạng thái đơn hàng không được để trống\"}";
    public static final String BILL_STATUS_IN_VALID =
        "{\"code\": \"BILL_STATUS_IN_VALID\",\"message\": \"Trạng thái đơn hàng không hợp lệ\"}";
    public static final String DESCRIPTION_MAX_LENGTH =
        "{\"code\": \"DESCRIPTION_MAX_LENGTH\",\"message\": \"Diễn giải vượt quá độ dài cho phép\"}";
    public static final String DESCRIPTION_PRODUCT_GROUP_MAX_LENGTH =
        "{\"code\": \"DESCRIPTION_PRODUCT_GROUP_MAX_LENGTH\",\"message\": \"Mô tả vượt quá độ dài cho phép\"}";
    public static final String BILL_ID_NOT_NULL = "{\"code\": \"BILL_ID_NOT_NULL\",\"message\": \"Id đơn hàng không được để trống\"}";
    public static final String BILL_ID_IN_VALID = "{\"code\": \"BILL_ID_IN_VALID\",\"message\": \"Id đơn hàng không hợp lệ\"}";
    public static final String BILL_CODE_NOT_NULL = "{\"code\": \"BILL_CODE_NOT_NULL\",\"message\": \"Mã đơn hàng không được để trống\"}";
    public static final String BILL_FUNDS_NOT_NULL = "{\"code\": \"BILL_FUNDS_NOT_NULL\",\"message\": \"Nguồn tiền không được để trống\"}";
    public static final String CUSTOMER_ID_NOT_NULL =
        "{\"code\": \"CUSTOMER_ID_NOT_NULL\",\"message\": \"Id khách hàng không được để trống\"}";
    public static final String CUSTOMER_NAME_NOT_NULL =
        "{\"code\": \"CUSTOMER_NAME_NOT_NULL\",\"message\": \"Tên khách hàng không được để trống\"}";
    public static final String BILL_DATE_NOT_NULL = "{\"code\": \"BILL_DATE_NOT_NULL\",\"message\": \"Ngày tạo không được để trống\"}";
    public static final String DELIVERY_TYPE_NOT_NULL =
        "{\"code\": \"DELIVERY_TYPE_NOT_NULL\",\"message\": \"Kiểu giao đơn không được để trống\"}";
    public static final String DELIVERY_TYPE_INVALID = "{\"code\": \"DELIVERY_TYPE_INVALID\",\"message\": \"Kiểu giao đơn không hợp lệ\"}";
    public static final String CUSTOMER_TYPE_INVALID =
        "{\"code\": \"CUSTOMER_TYPE_INVALID\",\"message\": \"Loại khách hàng/Nhà cung cấp không hợp lệ\"}";
    public static final String GENDER_INVALID = "{\"code\": \"GENDER_INVALID\",\"message\": \"Giới tính khách hàng không hợp lệ\"}";
    public static final String CUSTOMER_TYPE_NOT_NULL =
        "{\"code\": \"CUSTOMER_TYPE_NOT_NULL\",\"message\": \"Loại khách hàng/Nhà cung cấp không được để trống\"}";
    public static final String CITY_NOT_NULL = "{\"code\": \"CITY_NOT_NULL\",\"message\": \"Tỉnh/Thành phố không được để trống\"}";
    public static final String QUANTITY_NOT_NULL = "{\"code\": \"QUANTITY_NOT_NULL\",\"message\": \"Số lượng không được để trống\"}";
    public static final String QUANTITY_INVALID = "{\"code\": \"QUANTITY_INVALID\",\"message\": \"Số lượng phải lớn hơn 0\"}";
    public static final String AMOUNT_NOT_NULL = "{\"code\": \"AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền không được để trống\"}";
    public static final String AMOUNT_GREATER_THAN_ZERO =
        "{\"code\": \"AMOUNT_GREATER_THAN_ZERO\",\"message\": \"Thành tiền sản phẩm phải lớn hơn 0\"}";
    public static final String AMOUNT_DEBT_NOT_NULL =
        "{\"code\": \"AMOUNT_DEBT_NOT_NULL\",\"message\": \"Số tiền khách thanh toán nợ không được để trống\"}";
    public static final String AMOUNT_DEBT_INVALID =
        "{\"code\": \"AMOUNT_DEBT_INVALID\",\"message\": \"Số tiền khách thanh toán nợ không hợp lệ\"}";
    public static final String AMOUNT_INVALID = "{\"code\": \"AMOUNT_INVALID\",\"message\": \"Thành tiền không đúng định dạng\"}";
    public static final String TOTAL_PRE_TAX_INVALID =
        "{\"code\": \"TOTAL_PRE_TAX_INVALID\",\"message\": \"Tổng tiền trước thuế không đúng định dạng\"}";
    public static final String TOTAL_PRETAX_NOT_NULL =
        "{\"code\": \"TOTAL_PRETAX_NOT_NULL\",\"message\": \"Tổng tiền trước thuế không được bỏ trống\"}";
    public static final String TOTAL_PRETAX_INVALID =
        "{\"code\": \"TOTAL_PRETAX_INVALID\",\"message\": \"Tổng tiền trước thuế không đúng định dạng\"}";
    public static final String TOTAL_AMOUNT_NOT_NULL =
        "{\"code\": \"TOTAL_AMOUNT_NOT_NULL\",\"message\": \"Thành tiền phải lớn hơn hoặc bằng 0\"}";
    public static final String TOTAL_AMOUNT_INVALID =
        "{\"code\": \"TOTAL_AMOUNT_INVALID\",\"message\": \"Tổng tiền trước thuế không được để trống\"}";
    public static final String POSITION_ID_NOT_NULL =
        "{\"code\": \"POSITION_ID_NOT_NULL\",\"message\": \"Vị trí sản phẩm không được bỏ trống\"}";
    public static final String POSITION_ID_INVALID =
        "{\"code\": \"POSITION_ID_INVALID\",\"message\": \"Vị trí sản phẩm phải bắt đầu từ 1\"}";
    public static final String QUANTITY_PRODUCT_NOT_NULL =
        "{\"code\": \"QUANTITY_PRODUCT_NOT_NULL\",\"message\": \"Số lượng sản phẩm không được bỏ trống\"}";
    public static final String QUANTITY_PRODUCT_INVALID =
        "{\"code\": \"QUANTITY_PRODUCT_INVALID\",\"message\": \"Số lượng sản phẩm không đúng định dạng\"}";
    public static final String PRODUCT_NAME_NOT_NULL =
        "{\"code\": \"PRODUCT_NAME_NOT_NULL\",\"message\": \"Tên sản phẩm không được để trống\"}";

    public static final String PRODUCT_CODE_NOT_NULL =
        "{\"code\": \"PRODUCT_CODE_NOT_NULL\",\"message\": \"Mã sản phẩm không được để trống\"}";
    public static final String PRODUCT_OUT_PRICE_INVALID =
        "{\"code\": \"PRODUCT_OUT_PRICE_INVALID\",\"message\": \"Giá bán sản phẩm phải lớn hơn 0\"}";
    public static final String CONVERSION_OUT_PRICE_INVALID =
        "{\"code\": \"CONVERSION_OUT_PRICE_INVALID\",\"message\": \"Vui lòng nhập giá bán\"}";
    public static final String CONVERSION_UNIT_NAME_EMPTY =
        "{\"code\": \"CONVERSION_UNIT_NAME_EMPTY\",\"message\": \"Vui lòng chọn đơn vị tính chuyển đổi\"}";

    public static final String PRODUCT_ID_NOT_NULL = "{\"code\": \"PRODUCT_ID_NOT_NULL\",\"message\": \"Id sản phẩm không được để trống\"}";
    public static final String PRODUCT_ID_NOT_VALID =
        "{\"code\": \"PRODUCT_ID_NOT_VALID\",\"message\": \"Danh sách sản phẩm không hợp lệ\"}";
    public static final String UNIT_PRICE_NOT_NULL = "{\"code\": \"UNIT_PRICE_NOT_NULL\",\"message\": \"Đơn giá không được để trống\"}";
    public static final String UNIT_PRICE_INVALID = "{\"code\": \"UNIT_PRICE_INVALID\",\"message\": \"Đơn giá không hợp lệ\"}";
    public static final String PAYMENT_METHOD_NOT_NULL =
        "{\"code\": \"PAYMENT_METHOD_NOT_NULL\",\"message\": \"Phương thức thanh toán không được bỏ trống\"}";
    public static final String PRODUCTS_NOT_NULL =
        "{\"code\": \"PRODUCTS_NOT_NULL\",\"message\": \"Danh sách sản phẩm không được bỏ trống\"}";
    public static final String BILL_PAYMENTS_NOT_NULL =
        "{\"code\": \"BILL_PAYMENTS_NOT_NULL\",\"message\": \"Thông tin thanh toán không được bỏ trống\"}";
    public static final String BILL_PAYMENT_TIME_NOT_NULL =
        "{\"code\": \"BILL_PAYMENT_TIME_NOT_NULL\",\"message\": \"Thời điểm thanh toán không được bỏ trống\"}";
    public static final String PAYMENT_TIME_NOT_NULL =
        "{\"code\": \"PAYMENT_TIME_NOT_NULL\",\"message\": \"Thời điểm thanh toán không được bỏ trống\"}";
    public static final String INVOICE_ID_NOT_NULL = "{\"code\": \"INVOICE_ID_NOT_NULL\",\"message\": \"Id hóa đơn không được bỏ trống\"}";
    public static final String INVOICE_IKEY_NOT_NULL =
        "{\"code\": \"INVOICE_IKEY_NOT_NULL\",\"message\": \"Ikey hóa đơn không được bỏ trống\"}";
    public static final String COMPANY_ID_NOT_NULL = "{\"code\": \"COMPANY_ID_NOT_NULL\",\"message\": \"Id công ty không được bỏ trống\"}";
    public static final String FEATURE_NOT_NULL = "{\"code\": \"FEATURE_NOT_NULL\",\"message\": \"Loại sản phẩm không được bỏ trống\"}";
    public static final String INVOICE_COUNT_FAIL = "INVOICE_COUNT_FAIL";
    public static final String INVOICE_COUNT_FAIL_VI = "Danh sách hóa đơn không đúng";

    //Company
    public static final String COMPANY_ID_NOT_EXISTS_CODE = "COMPANY_ID_NOT_EXISTS_CODE";
    public static final String COMPANY_ID_NOT_EXISTS_VI = "ID Công ty không hợp lệ";
    public static final String COMPANY_NOT_EXISTS_CODE = "COMPANY_NOT_EXISTS_CODE";
    public static final String COMPANY_NOT_EXISTS_CODE_VI = "Công ty không tồn tại";
    public static final String COMPANY_ID_INVALID_CODE = "COMPANY_ID_INVALID_CODE";
    public static final String COMPANY_ID_INVALID_VI = "Công ty không hợp lệ";
    public static final String CONTENT_NOT_NULL = "CONTENT_NOT_NULL";
    public static final String CONTENT_NOT_NULL_vi = "Nội dung không được bỏ trống";
    public static final String TYPE_MESSAGE_VALID = "TYPE_MESSAGE_VALID";
    public static final String TYPE_MESSAGE_VALID_VI = "Loại message Không hợp lệ";
    public static final String RECEIVE_NOT_NULL = "RECEIVE_NOT_NULL";
    public static final String RECEIVE_NOT_NULL_VI = "Người nhận Không được bỏ trống";
    public static final String PHONE_NUMBER_IS_NOT_NULL =
        "{\"code\": \"PHONE_NUMBER_IS_NOT_NULL\",\"message\": \"Số điện thoại không được bỏ trống\"}";
    public static final String PHONE_NUMBER_IN_VALID = "{\"code\": \"PHONE_NUMBER_IN_VALID\",\"message\": \"Số điện thoại không hợp lệ\"}";
    public static final String PHONE_NUMBER_NOT_EMPTY =
        "{\"code\": \"PHONE_NUMBER_NOT_EMPTY\",\"message\": \"Không để trống số điện thoại\"}";
    public static final String USERNAME_NOT_NULL = "{\"code\": \"USERNAME_NOT_NULL\",\"message\": \"Tên đăng nhập không được bỏ trống\"}";
    public static final String USERNAME_NOT_NULL_CODE = "USERNAME_NOT_NULL_CODE";
    public static final String USERNAME_NOT_NULL_VI = "Tên đăng nhập không được bỏ trống";
    public static final String PASSWORD_IN_VALID = "{\"code\": \"PASSWORD_NOT_NULL\",\"message\": \"Mật khẩu không hợp lệ\"}";
    public static final String PASSWORD_NOT_BLANK = "{\"code\": \"PASSWORD_NOT_BLANK\",\"message\": \"Không để trống mật khẩu\"}";
    public static final String EMAIL_IN_VALID = "{\"code\": \"EMAIL_IN_VALID\",\"message\": \"Email không hợp lệ\"}";
    public static final String EMAIL_NOT_EMPTY = "{\"code\": \"EMAIL_NOT_EMPTY\",\"message\": \"Email không được để trống\"}";
    public static final String HASH_NOT_EMPTY = "{\"code\": \"HASH_NOT_EMPTY\",\"message\": \"Mã hash không được để trống\"}";
    public static final String TAX_CODE_IN_VALID = "{\"code\": \"TAX_CODE_IN_VALID\",\"message\": \"Mã số thuế không hợp lệ\"}";
    public static final String COMPANY_NAME_NOT_EMPTY =
        "{\"code\": \"COMPANY_NAME_NOT_EMPTY\",\"message\": \"Không để trống tên công ty\"}";
    public static final String COMPANY_NAME_NOT_EMPTY_CODE = "COMPANY_NAME_NOT_EMPTY_CODE";
    public static final String COMPANY_NAME_NOT_EMPTY_VI = "Không để trống tên công ty";
    public static final String COMPANY_ADDRESS_NOT_EMPTY =
        "{\"code\": \"COMPANY_ADDRESS_NOT_EMPTY\",\"message\": \"Không để trống địa chỉ công ty\"}";
    public static final String COMPANY_NAME_INVALID =
        "{\"code\": \"COMPANY_NAME_INVALID\",\"message\": \"Tên công ty phải có độ dài từ 5 kí tự trở lên\"}";
    public static final String COMPANY_TAX_CODE_NOT_EMPTY =
        "{\"code\": \"COMPANY_TAX_CODE_NOT_EMPTY\",\"message\": \"Không để trống mã số thuế công ty\"}";
    public static final String COMPANY_TAX_CODE_NOT_EMPTY_CODE = "COMPANY_TAX_CODE_NOT_EMPTY_CODE";
    public static final String COMPANY_TAX_CODE_NOT_EMPTY_VI = "Không để trống mã số thuế công ty";

    public static final String TITLE_PRINT_CONFIG_NOT_NULL =
        "{\"code\": \"TITLE_PRINT_CONFIG_NOT_NULL\",\"message\": \"Tên giá trị không được bỏ trống\"}";
    public static final String CODE_PRINT_CONFIG_NOT_NULL =
        "{\"code\": \"CODE_PRINT_CONFIG_NOT_NULL\",\"message\": \"Tiêu đề không được bỏ trống\"}";
    public static final String COMPANY_NAME_DUPLICATE_CODE = "COMPANY_NOT_EXISTS_CODE";
    public static final String COMPANY_NAME_DUPLICATE_VI = "Trùng tên công ty";

    public static final String COMPANY_ID_NOT_NULL_CODE = "COMPANY_ID_NOT_NULL_CODE";
    public static final String COMPANY_ID_NOT_NULL_VI = "Id công ty không để trống\"}";

    public static final String COMPANY_SAVE_NOT_VALID_VI = "Bạn không có quyền chỉnh sửa thông tin công ty";
    public static final String COMPANY_SAVE_NOT_VALID_CODE = "COMPANY_SAVE_NOT_VALID_CODE";
    //end company

    // company user
    public static final String COMPANY_USER_SERVICE_PACKAGE_NOT_EMPTY =
        "{\"code\": \"COMPANY_SERVICE_PACKAGE_NOT_EMPTY\",\"message\": \"Không để trống mã gói\"}";

    public static final String COMPANY_USER_PACKAGE_ID_NOT_NULL =
        "{\"code\": \"COMPANY_USER_PACKAGE_ID_NOT_NULL\",\"message\": \"Không để trống ID gói\"}";
    public static final String COMPANY_USER_PACKAGE_ID_NOT_NULL_CODE = "COMPANY_USER_PACKAGE_ID_NOT_NULL";
    public static final String COMPANY_USER_PACKAGE_ID_NOT_NULL_VI = "Không để trống ID gói";
    public static final String COMPANY_USER_SERVICE_PACKAGE_INVALID =
        "{\"code\": \"COMPANY_USER_SERVICE_PACKAGE_INVALID\",\"message\": \"Mã gói không hợp lệ\"}";
    public static final String COMPANY_USER_START_DATE_NOT_EMPTY =
        "{\"code\": \"COMPANY_USER_START_DATE_NOT_EMPTY\",\"message\": \"Không để trống ngày bắt đầu\"}";
    public static final String COMPANY_USER_END_DATE_NOT_EMPTY =
        "{\"code\": \"COMPANY_USER_END_DATE_NOT_EMPTY\",\"message\": \"Không để trống ngày kết thúc\"}";
    public static final String COMPANY_USER_PACK_COUNT_NOT_EMPTY =
        "{\"code\": \"COMPANY_USER_PACK_COUNT_NOT_EMPTY\",\"message\": \"Không để trống số lượng công ty\"}";
    public static final String COMPANY_USER_PACK_COUNT_INVALID =
        "{\"code\": \"COMPANY_USER_PACK_COUNT_INVALID\",\"message\": \"Số lượng công ty không hợp lệ\"}";
    public static final String HASH_CODE_NOT_NULL = "{\"code\": \"HASH_CODE_NOT_NULL\",\"message\": \"Mã xác thực không được bỏ trống\"}";
    public static final String DATE_INVALID_CODE = "DATE_INVALID_CODE";
    public static final String DATE_INVALID_VI = "Ngày tháng không hợp lệ";
    public static final String HASH_CODE_INVALID = "HASH_CODE_INVALID";
    public static final String HASH_CODE_INVALID_VI = "Mã xác thực không hợp lệ";
    public static final String HASH_CODE_NOT_NULL_CODE = "HASH_CODE_NOT_NULL_CODE";
    public static final String HASH_CODE_NOT_NULL_VI = "Không để trống mã xác thực";

    // end company user

    //Invoice
    public static final String INVOICE_NOT_FOUND = "INVOICE_NOT_FOUND";

    //End invoice
    public static final String PRODUCT_INVENTORY_COUNT_NULL = "PRODUCT_INVENTORY_COUNT_NULL";
    public static final String PRODUCT_INVENTORY_COUNT_NULL_VI = "Số lượng tồn kho không được để trống khi theo dõi tồn kho";
    public static final String PRODUCT_IN_PRICE_NULL = "PRODUCT_IN_PRICE_NULL";

    public static final String PRODUCT_IN_PRICE_NULL_VI = "Giá nhập không được để trống khi theo dõi tồn kho";
    public static final String UNIT_NOT_FOUND = "UNIT_NOT_FOUND";
    public static final String UNIT_NOT_FOUND_VI = "Không tìm thấy mã đơn vị";
    public static final String UNIT_MUST_NOT_EMPTY = "UNIT_MUST_NOT_EMPTY";
    public static final String UNIT_MUST_NOT_EMPTY_VI = "Mã đơn vị không được để trống nếu có tên đơn vị truyền vào";
    public static final String UNIT_NAME_NOT_INVALID = "UNIT_NAME_NOT_INVALID";
    public static final String UNIT_NAME_NOT_INVALID_VI = "Tên đơn vị khác với tên đơn vị unitId truyền vào";
    public static final String UNIT_NAME_MUST_NOT_EMPTY_VI = "Tên đơn vị không được để trống nếu có mã đơn vị truyền vào";
    public static final String UNIT_NAME_MUST_NOT_EMPTY = "UNIT_NAME_MUST_NOT_EMPTY";
    public static final String UNIT_NAME_IS_EXISTED_VI = "Đơn vị tính đã tồn tại";
    public static final String UNIT_NAME_IS_EXISTED = "UNIT_NAME_IS_EXISTED";
    public static final String UNIT_NAME_IS_NOT_VALID = "UNIT_NAME_IS_NOT_VALID";
    public static final String UNIT_NAME_IS_NOT_VALID_VI = "Sản phẩm có đơn vị tính chuyển đổi thì đơn vị tính chính không được để trống";
    public static final String CONVERSION_UNIT_IS_NOT_VALID_VI =
        "Sản phẩm có đơn vị tính chính thì cũng cần đưa vào danh sách đơn vị tính chuyển đổi";
    public static final String CONVERSION_UNIT_IS_NOT_VALID = "CONVERSION_UNIT_IS_NOT_VALID";

    public static final String MAIN_UNIT_IS_NOT_VALID = "MAIN_UNIT_IS_NOT_VALID";
    public static final String MAIN_UNIT_IS_NOT_VALID_VI = "Mỗi sản phẩm chỉ có 1 đơn vị tính chính";
    public static final String MAIN_UNIT_IS_NOT_MATCH = "MAIN_UNIT_IS_NOT_MATCH";
    public static final String MAIN_UNIT_IS_NOT_MATCH_VI = "Đơn vị tính chính không trùng khớp";
    public static final String DUPLICATE_CONVERSION_UNIT = "DUPLICATE_CONVERSION_UNIT";

    public static final String UNIT_IS_NOT_VALID_VI = "Danh sách đơn vị tính không hợp lệ";
    public static final String UNIT_IS_NOT_VALID = "UNIT_IS_NOT_VALID";
    public static final String DUPLICATE_CONVERSION_UNIT_VI = "Các đơn vị tính chuyển đổi không được trùng nhau";
    public static final String DUPLICATE_CONVERSION_UNIT_BARCODE = "DUPLICATE_CONVERSION_UNIT_BARCODE";
    public static final String DUPLICATE_CONVERSION_UNIT_BARCODE_VI = "Mã vạch các đơn vị tính không được trùng nhau";
    public static final String DUPLICATE_WITH_UNIT = "DUPLICATE_WITH_UNIT";
    public static final String UNIT_CAN_NOT_CHANGE_VI = "Sản phẩm '@@name' có đơn hàng đang xử lý, không thể cập nhật đơn vị tính";
    public static final String UNIT_CAN_NOT_CHANGE = "UNIT_CAN_NOT_CHANGE";
    public static final String DUPLICATE_WITH_UNIT_VI = "Các đơn vị tính chuyển đổi không được trùng đơn vị tính chính";
    public static final String CONVERT_RATE_INVALID = "CONVERT_RATE_INVALID";
    public static final String CONVERT_RATE_INVALID_VI = "Giá trị quy đổi phải lớn hơn 0";
    public static final String DESCRIPTION_INVALID = "DESCRIPTION_INVALID";
    public static final String DESCRIPTION_INVALID_VI = "Diễn giải sinh ra không khớp với đầu vào";
    public static final String PRODUCT_UNIT_NOT_FOUND = "PRODUCT_UNIT_NOT_FOUND";
    public static final String PRODUCT_UNIT_NOT_FOUND_VI = "Đơn vị tính không tồn tại";
    public static final String PRODUCT_UNIT_CANNOT_DELETE = "PRODUCT_UNIT_CANNOT_DELETE";
    public static final String PRODUCT_UNIT_CANNOT_DELETE_VI = "Đơn vị tính đã bị xóa trước đó";
    public static final String PRODUCT_UNIT_CANNOT_UPDATE_VI = "Đơn vị tính không thể sửa do còn đơn hàng đang xử lý";
    public static final String PRODUCT_UNIT_CANNOT_DELETE_VI2 = "Đơn vị tính không thể xóa do còn đơn hàng đang xử lý";
    public static final String PRODUCT_UNIT_CANNOT_DELETE_VI3 =
        "Không thể xóa đơn vị tính này do đang có liên kết với đơn vị tính chuyển đổi của sản phẩm [@@name]";

    public static final String UNIT_NAME_NOT_NULL = "{\"code\": \"UNIT_NAME_NOT_NULL\",\"message\": \"Tên đơn vị không được bỏ trống\"}";

    public static final String PRODUCT_CONVERSION_UNIT_NOT_FOUND = "PRODUCT_CONVERSION_UNIT_NOT_FOUND";
    public static final String PRODUCT_UNIT_NOT_DELETE_VI = "Đơn vị tính chính không được phép xóa khi đã phát sinh đơn vị tính chuyển đổi";
    public static final String PRODUCT_UNIT_NOT_DELETE = "PRODUCT_UNIT_NOT_DELETE";
    public static final String PRODUCT_CONVERSION_UNIT_NOT_FOUND_VI = "Đơn vị tính chuyển đổi không tồn tại";
    public static final String ID_CREATE_CONVERSION_NULL = "ID_CREATE_CONVERSION_NULL";
    public static final String ID_CREATE_CONVERSION_NULL_VI = "Khi thực hiên thêm mới đơn vị tính chuyển đổi thì không được truyền id";
    public static final String DESCRIPTION_NOT_NULL =
        "{\"code\": \"DESCRIPTION_NOT_NULL\",\"message\": \"Diễn giải cho đơn vị tính chuyển đổi không được để trống\"}";

    public static final String DEVICE_CODE_NOT_NULL =
        "{\"code\": \"DEVICE_CODE_NOT_NULL\",\"message\": \"Tên thiết bị không được để trống\"}";
    public static final String COM_ID_MUST_NOT_NULL =
        "{\"code\": \"PRODUCT_COM_ID_MUST_NOT_NULL\",\"message\": \"ID công ty không được để trống\"}";
    public static final String PRODUCT_NAME_MUST_NOT_EMPTY =
        "{\"code\": \"PRODUCT_NAME_MUST_NOT_NULL\",\"message\": \"Tên sản phẩm không được để trống\"}";
    public static final String PRODUCT_NAME_EMPTY_CODE = "PRODUCT_NAME_EMPTY_CODE";
    public static final String PRODUCT_NAME_EMPTY_VI = "Tên sản phẩm không được để trống";
    public static final String PRODUCT_CODE2_EMPTY_VI = "Mã sản phẩm không được để trống";
    public static final String PRODUCT_CODE2_EXISTS_VI = "Trùng mã sản phẩm";
    public static final String PRODUCT_CODE2_EXISTS = "PRODUCT_CODE2_EXISTS";
    public static final String PRODUCT_CODE2_EMPTY = "PRODUCT_CODE2_EMPTY";
    public static final String PRODUCT_OUT_PRICE_MUST_NOT_EMPTY_CODE = "PRODUCT_OUT_PRICE_MUST_NOT_EMPTY";
    public static final String PRODUCT_OUT_PRICE_MUST_NOT_EMPTY_VI = "Giá bán không được để trống";
    public static final String PRODUCT_UNIT_MUST_NOT_EMPTY =
        "{\"code\": \"PRODUCT_UNIT_MUST_NOT_EMPTY\",\"message\": \"Đơn vị không được để trống\"}";
    public static final String PRODUCT_UNIT_ID_MUST_NOT_EMPTY =
        "{\"code\": \"PRODUCT_UNIT_ID_MUST_NOT_EMPTY\",\"message\": \"Mã đơn vị không được để trống\"}";
    public static final String PRODUCT_OUT_PRICE_MUST_NOT_EMPTY =
        "{\"code\": \"PRODUCT_OUT_PRICE_MUST_NOT_EMPTY\",\"message\": \"Giá xuất ra không được để trống\"}";
    public static final String PRODUCT_IS_INVENTORY_MUST_NOT_EMPTY =
        "{\"code\": \"PRODUCT_IS_INVENTORY_MUST_NOT_EMPTY\",\"message\": \"Theo dõi đơn hàng không được để trống\"}";
    public static final String PRODUCT_VAT_RATE_INVALID =
        "{\"code\": \"PRODUCT_VAT_RATE_INVALID\",\"message\": \"Thuế suất có giá trị tối đa là 100\"}";
    public static final String PRODUCT_BARCODE_LENGTH_INVALID =
        "{\"code\": \"PRODUCT_BARCODE_LENGTH_INVALID\",\"message\": \"Mã vạch có độ dài tối đa là 50 ký tự\"}";
    public static final String PRODUCT_CODE_2_LENGTH_INVALID =
        "{\"code\": \"PRODUCT_CODE_2_LENGTH_INVALID\",\"message\": \"Mã sản phẩm có độ dài tối đa là 100 ký tự\"}";
    public static final String CONVERSION_RATE_MUST_NOT_EMPTY =
        "{\"code\": \"CONVERSION_RATE_MUST_NOT_EMPTY\",\"message\": \"Giá trị quy đổi phải lớn hơn 0\"}";
    public static final String CONVERSION_RATE_INVALID =
        "{\"code\": \"CONVERSION_RATE_INVALID\",\"message\": \"Tỷ lệ chuyển đổi chỉ chấp nhận số thập phân lẻ 2 chữ số\"}";
    public static final String DIRECT_SALE_NOT_NULL =
        "{\"code\": \"DIRECT_SALE_NOT_NULL\",\"message\": \"Lựa chọn có thể bán trực tiếp không được bỏ trống\"}";
    public static final String IS_PRIMARY_NOT_NULL =
        "{\"code\": \"IS_PRIMARY_NOT_NULL\",\"message\": \"Lựa chọn có phải là đơn vị tính chính hay không (isPrimary) không được bỏ trống\"}";
    public static final String FORMULA_MUST_NOT_EMPTY =
        "{\"code\": \"FORMULA_MUST_NOT_EMPTY\",\"message\": \"Phép tính chuyển đổi không được để trống\"}";
    public static final String FORMULA_INVALID = "{\"code\": \"FORMULA_INVALID\",\"message\": \"Phép tính chuyển đổi không hợp lệ\"}";

    public static final String INVALID_COM_ID = "INVALID_COM_ID";
    public static final String INVALID_COM_ID_VI = "{\"code\": \"INVALID_COM_ID_VI\",\"message\": \"Mã công ty không hợp lệ\"}";
    public static final String INVALID_COM_ID_VI_2 = "Mã công ty không hợp lệ";

    // Area
    public static final String AREA_ID_NOT_NULL = "{\"code\": \"AREA_ID_NOT_NULL\",\"message\": \"Id khu vực không để trống\"}";
    public static final String AREA_NAME_NOT_EMPTY = "{\"code\": \"AREA_NAME_NOT_EMPTY\",\"message\": \"Tên khu vực không để trống\"}";
    public static final String AREA_NAME_NOT_EMPTY_CODE = "AREA_NAME_NOT_EMPTY_CODE";
    public static final String AREA_NAME_NOT_EMPTY_VI = "Tên khu vực không để trống";
    public static final String AREA_NAME_INVALID =
        "{\"code\": \"AREA_NAME_INVALID\",\"message\": \"Tên khu vực không vượt quá 255 kí tự\"}";
    public static final String AREA_NOT_EXISTS = "Khu vực này không tồn tại";
    public static final String AREA_NOT_EXISTS_CODE = "AREA_NOT_EXISTS_CODE";
    public static final String AREA_NAME_DUPLICATE_CODE = "AREA_NAME_DUPLICATE_CODE";
    public static final String AREA_NAME_DUPLICATE_VI = "Tên khu vực đã tồn tại";
    public static final String AREA_NAME_INVALID_CODE = "AREA_NAME_INVALID_CODE";
    public static final String AREA_NAME_INVALID_VI = "Tên khu vực không hợp lệ";
    public static final String AREA_DELETE_FAIL_01_CODE = "AREA_DELETE_FAIL_01_CODE";
    public static final String AREA_DELETE_FAIL_01_VI = "Bạn không thể xóa khu vực cuối cùng trong cửa hàng";
    public static final String AREA_DELETE_FAIL_02_CODE = "AREA_DELETE_FAIL_02_CODE";
    public static final String AREA_DELETE_FAIL_02_VI = "Bạn không thể xóa khu vực này vì còn bàn đang làm việc";
    // end area

    // AreaUnit
    public static final String AREA_UNIT_ID_NOT_NULL = "{\"code\": \"AREA_UNIT_ID_NOT_NULL\",\"message\": \"Id bàn không để trống\"}";
    public static final String AREA_UNIT_NAME_NOT_EMPTY =
        "{\"code\": \"AREA_UNIT_NAME_NOT_EMPTY\",\"message\": \"Tên bàn không để trống\"}";
    public static final String AREA_UNIT_NAME_NOT_EMPTY_CODE = "AREA_UNIT_NAME_NOT_EMPTY_CODE";
    public static final String AREA_UNIT_NAME_NOT_EMPTY_VI = "Tên bàn không để trống";
    public static final String AREA_UNIT_NAME_INVALID =
        "{\"code\": \"AREA_NAME_INVALID\",\"message\": \"Tên bàn không vượt quá 255 kí tự\"}";
    public static final String AREA_UNIT_NAME_DUPLICATE_CODE = "AREA_UNIT_NAME_DUPLICATE_CODE";
    public static final String AREA_UNIT_NAME_DUPLICATE_VI = "Tên bàn đã trùng tại khu vực này";
    public static final String AREA_UNIT_NAME_INVALID_01_CODE = "AREA_UNIT_NAME_INVALID_01_CODE";
    public static final String AREA_UNIT_NAME_INVALID_01_VI = "Tên bàn không hợp lệ";
    public static final String AREA_UNIT_NOT_EXISTS = "Bàn này không tồn tại";
    public static final String AREA_UNIT_NOT_EXISTS_CODE = "AREA_UNIT_NOT_EXISTS_CODE";
    public static final String AREA_UNIT_UPDATE_FAIL_CODE = "AREA_UNIT_UPDATE_CODE";
    public static final String AREA_UNIT_UPDATE_FAIL_VI = "Bàn đang phục vụ, không thể chỉnh sửa";
    public static final String AREA_UNIT_DELETE_FAIL_CODE = "AREA_UNIT_DELETE_FAIL_CODE";
    public static final String AREA_UNIT_DELETE_FAIL_VI = "Bàn đang phục vụ, không thể xoá";

    // end areaUnit

    // Customer
    public static final String CUSTOMER_PHONE_INVALID =
        "{\"code\": \"CUSTOMER_PHONE_INVALID\",\"message\": \"Số điện thoại khách hàng không hợp lệ\"}";
    public static final String CUSTOMER_CANNOT_DELETE = "CUSTOMER_CANNOT_DELETE";
    public static final String CUSTOMER_CANNOT_DELETE_VI = "Khách hàng '@@name' có đơn hàng đang xử lý, không thể xóa";

    // end customer

    // reservation
    public static final String RESERVATION_NOT_EXISTS = "Phiếu đặt chỗ không tồn tại";
    public static final String RESERVATION_NOT_EXISTS_CODE = "RESERVATION_NOT_EXISTS_CODE";
    public static final String RESERVATION_ID_NOT_NULL =
        "{\"code\": \"RESERVATION_ID_NOT_NULL\",\"message\": \"Không để trống Id phiếu đặt bàn\"}";
    public static final String RESERVATION_CUSTOMER_NAME_NOT_EMPTY =
        "{\"code\": \"RESERVATION_CUSTOMER_NAME_NOT_EMPTY\",\"message\": \"Không để trống tên người đặt bàn\"}";
    public static final String RESERVATION_CUSTOMER_NAME_NOT_EMPTY_CODE = "RESERVATION_CUSTOMER_NAME_NOT_EMPTY_CODE";
    public static final String RESERVATION_CUSTOMER_NAME_NOT_EMPTY_VI = "Không để trống tên người đặt bàn";
    public static final String RESERVATION_CUSTOMER_NAME_INVALID =
        "{\"code\": \"RESERVATION_CUSTOMER_NAME_INVALID\",\"message\": \"Tên người đặt bàn không vượt qu 255 ký tự\"}";
    public static final String RESERVATION_CUSTOMER_PHONE_INVALID =
        "{\"code\": \"RESERVATION_CUSTOMER_PHONE_INVALID\",\"message\": \"Số điện thoại người đặt không hợp lệ\"}";
    public static final String RESERVATION_ORDER_DATE_INVALID =
        "{\"code\": \"RESERVATION_ORDER_DATE_INVALID\",\"message\": \"Ngày tháng đặt chỗ không hợp lệ\"}";
    public static final String RESERVATION_ORDER_DATE_INVALID_CODE = "RESERVATION_ORDER_DATE_INVALID_CODE";
    public static final String RESERVATION_ORDER_DATE_INVALID_VI = "Ngày tháng đặt chỗ không hợp lệ";
    public static final String RESERVATION_ORDER_DATE_EMPTY_CODE = "RESERVATION_ORDER_DATE_EMPTY_CODE";
    public static final String RESERVATION_ORDER_DATE_EMPTY_VI = "Không để trống ngày tháng đặt chỗ";

    public static final String RESERVATION_ORDER_TIME_INVALID =
        "{\"code\": \"RESERVATION_ORDER_TIME_INVALID\",\"message\": \"Thời gian đặt chỗ không hợp lệ\"}";
    public static final String RESERVATION_ORDER_TIME_INVALID_CODE = "RESERVATION_ORDER_TIME_INVALID_CODE";
    public static final String RESERVATION_ORDER_TIME_INVALID_VI = "Thời gian đặt chỗ không hợp lệ";
    public static final String RESERVATION_ARRIVAL_TIME_INVALID_CODE = "RESERVATION_ARRIVAL_TIME_INVALID_CODE";
    public static final String RESERVATION_ARRIVAL_TIME_INVALID_VI = "Thời gian người đặt đã đến không hợp lệ";
    public static final String RESERVATION_ARRIVAL_TIME_EMPTY =
        "{\"code\": \"RESERVATION_ARRIVAL_TIME_EMPTY\",\"message\": \"Không để trống thời gian người đặt đã đến\"}";
    public static final String RESERVATION_STATUS_INVALID =
        "{\"code\": \"RESERVATION_STATUS_INVALID\",\"message\": \"Trạng thái đặt chỗ không hợp lệ\"}";
    public static final String RESERVATION_PEOPLE_COUNT_INVALID =
        "{\"code\": \"RESERVATION_PEOPLE_COUNT_INVALID\",\"message\": \"Số lượng người phải lớn hơn 0\"}";
    public static final String RESERVATION_NOTE_INVALID =
        "{\"code\": \"RESERVATION_NOTE_INVALID\",\"message\": \"Ghi chú thông tin đặt chỗ không vượt quá 255 ký tự\"}";

    // end reservation

    // Customer
    public static final String CUSTOMER_NAME_NOT_EMPTY =
        "{\"code\": \"CUSTOMER_NAME_NOT_EMPTY\",\"message\": \"Tên khách hàng không để trống\"}";
    public static final String CUSTOMER_NAME_NOT_EMPTY_CODE = "CUSTOMER_NAME_NOT_EMPTY";
    public static final String CUSTOMER_NAME_NOT_EMPTY_VI = "Tên khách hàng/nhà cung cấp trống";
    public static final String CUSTOMER_TYPE_NOT_NULL_VI = "Loại khách hàng/nhà cung cấp trống";
    public static final String CUSTOMER_PHONE_INVALID_VI = "Số điện thoại không hợp lệ";
    public static final String CUSTOMER_EMAIL_INVALID_VI = "Email không hợp lệ";
    public static final String CUSTOMER_ID_NOT_NULL_CODE = "CUSTOMER_ID_NOT_NULL_CODE";
    public static final String CUSTOMER_NAME_INVALID =
        "{\"code\": \"CUSTOMER_NAME_INVALID\",\"message\": \"Tên khách hàng không vượt quá 400 kí tự\"}";
    public static final String CUSTOMER_CODE2_INVALID =
        "{\"code\": \"CUSTOMER_CODE2_INVALID\",\"message\": \"Mã khách hàng không vượt quá 100 kí tự\"}";
    public static final String CUSTOMER_ADDRESS_INVALID =
        "{\"code\": \"CUSTOMER_ADDRESS_INVALID\",\"message\": \"Địa chỉ không vượt quá 400 kí tự\"}";
    public static final String CUSTOMER_EMAIL_INVALID =
        "{\"code\": \"CUSTOMER_EMAIL_INVALID\",\"message\": \"Email đăng nhập không hợp lệ\"}";
    public static final String CUSTOMER_TAX_CODE_INVALID_CODE = "CUSTOMER_TAX_CODE_INVALID_CODE";
    public static final String CUSTOMER_TAX_CODE_INVALID_VI = "Mã số thuế không hợp lệ";
    public static final String CUSTOMER_ID_NUMBER_INVALID_VI = "CCCD không hợp lệ";
    public static final String CUSTOMER_ID_NUMBER_INVALID =
        "{\"code\": \"CUSTOMER_ID_NUMBER_INVALID\",\"message\": \"Căn cước công dân không hợp lệ\"}";
    public static final String CUSTOMER_CODE2_EXISTS_CODE = "CUSTOMER_CODE2_EXISTS_CODE";
    public static final String CUSTOMER_CODE2_EXISTS_VI = "Mã khách hàng/ nhà cung cấp đã tồn tại";
    public static final String CUSTOMER_DEFAULT_ERROR_VI = "Không cập nhật/xóa khách lẻ";
    public static final String CUSTOMER_DEFAULT_ERROR_CODE = "CUSTOMER_DEFAULT_ERROR_CODE";

    public static final String CUSTOMER_LIST_INVALID_VI = "Danh sách khách hàng không hợp lệ";
    public static final String CUSTOMER_LIST_INVALID = "CUSTOMER_LIST_INVALID";
    public static final String CUSTOMER_CARD_DUPLICATE_CODE_INVALID = "CUSTOMER_CARD_DUPLICATE_CODE_INVALID";
    public static final String CUSTOMER_CARD_DUPLICATE_CODE_INVALID_VI = "Mã thẻ đã tồn tại";
    public static final String CUSTOMER_CARD_NOT_FOUND = "CUSTOMER_CARD_NOT_FOUND";
    public static final String CUSTOMER_CARD_NOT_FOUND_VI = "Thông tin thẻ không tồn tại";
    public static final String CUSTOMER_CARD_ID_NOT_NULL = "CUSTOMER_CARD_ID_NOT_NULL";
    public static final String CUSTOMER_CARD_ID_NOT_NULL_VI = "ID thẻ không để trống";
    public static final String CUSTOMER_UPDATE_CARD_AMOUNT_EMPTY_VI = "Không để trống số tiền";
    public static final String CUSTOMER_UPDATE_CARD_AMOUNT_EMPTY = "CUSTOMER_UPDATE_CARD_AMOUNT_EMPTY";
    public static final String CUSTOMER_UPDATE_CARD_POINT_EMPTY_VI = "Không để trống số điểm";
    public static final String CUSTOMER_UPDATE_CARD_POINT_EMPTY = "CUSTOMER_UPDATE_CARD_POINT_EMPTY";
    public static final String CUSTOMER_UPDATE_CARD_POINT_INVALID = "CUSTOMER_UPDATE_CARD_POINT_INVALID";
    public static final String CUSTOMER_UPDATE_CARD_POINT_INVALID_VI = "Số điểm quy đổi không hợp lệ";
    public static final String CUSTOMER_UPDATE_CARD_AMOUNT_INVALID = "CUSTOMER_UPDATE_CARD_AMOUNT_INVALID";
    public static final String CUSTOMER_UPDATE_CARD_AMOUNT_INVALID_VI = "Số tiền quy đổi không hợp lệ";
    public static final String CUSTOMER_CARD_USED_NOT_FOUND_VI = "Khách hàng này chưa có thẻ";
    public static final String CUSTOMER_CARD_USED_NOT_FOUND = "CUSTOMER_CARD_USED_NOT_FOUND";
    public static final String CUSTOMER_CARD_CODE_NOT_FOUND = "CUSTOMER_CARD_CODE_NOT_FOUND";
    public static final String CUSTOMER_CARD_CODE_NOT_FOUND_VI = "Mã thẻ KH/NCC không để trống";

    public static final String CUSTOMER_CARD_UPDATE_TYPE_INVALID = "CUSTOMER_CARD_TYPE_INVALID";
    public static final String CUSTOMER_CARD_UPDATE_TYPE_INVALID_VI = "Loại cập nhật không hợp lệ";
    public static final String CUSTOMER_CARD_UPDATE_TYPE_NOT_NULL =
        "{\"code\": \"CUSTOMER_CARD_UPDATE_TYPE_NOT_NULL\",\"message\": \"Loại cập nhật không để trống\"}";
    public static final String CUSTOMER_CARD_UPDATE_POINT_INVALID = "CUSTOMER_CARD_UPDATE_POINT_INVALID";
    public static final String CUSTOMER_CARD_UPDATE_POINT_INVALID_VI = "Số điểm quy đổi không hợp lệ";
    public static final String CUSTOMER_CARD_UPDATE_POINT_MINUS_INVALID = "CUSTOMER_CARD_UPDATE_POINT_MINUS_INVALID";
    public static final String CUSTOMER_CARD_UPDATE_POINT_MINUS_INVALID_VI = "Số điểm bị trừ phải nhỏ hơn hoặc bằng số điểm hiện tại";
    public static final String CUSTOMER_BIRTH_DATE_INVALID = "CUSTOMER_BIRTH_DATE_INVALID";
    public static final String CUSTOMER_BIRTH_DATE_INVALID_VI = "Ngày sinh không hợp lệ";
    public static final String CUSTOMER_UPDATE_TYPE_INVALID = "CUSTOMER_UPDATE_TYPE_INVALID";
    public static final String CUSTOMER_UPDATE_TYPE_INVALID_VI = "Khách hàng %s đã có thẻ, không thể cập nhật";
    // end customer

    //Invoice
    public static final String INVOICE_NOT_FOUND_VI = "Không tìm thấy hóa đon";
    public static final String CODE2_NOT_NULL = "CODE2_NOT_NULL";
    public static final String CODE2_NOT_NULL_VI = "Mã đơn hàng không đươc bỏ trống";
    public static final String INVOICE_NOT_CANCEL = "INVOICE_NOT_CANCEL";
    public static final String INVOICE_NOT_CANCEL_VI = "Hóa đơn không được xóa";

    //End invoice

    // taskLog
    public static final String TASK_LOG_NOT_FOUND = "TASK_LOG_NOT_FOUND";
    public static final String TASK_LOG_NOT_FOUND_VI = "Không tìm thấy nhật ký công việc";
    public static final String TASK_LOG_COM_ID_NOT_FOUND = "TASK_LOG_COM_ID_NOT_FOUND";
    public static final String TASK_LOG_COM_ID_NOT_FOUND_VI = "Không tìm thấy Id công ty";
    public static final String TASK_LOG_ID_NOT_FOUND = "TASK_LOG_ID_NOT_FOUND";
    public static final String TASK_LOG_ID_NOT_FOUND_VI = "Không tìm thấy Id nhật ký";
    public static final String TASK_LOG_ID_NOT_NULL =
        "{\"code\": \"TASK_LOG_ID_NOT_NULL\",\"message\": \"Id nhật ký không được để trống\"}";
    public static final String TASK_LOG_TYPE_NOT_NULL =
        "{\"code\": \"TASK_LOG_TYPE_NOT_NULL\",\"message\": \"Loại nhật ký không được để trống\"}";

    //end TaskLog

    // package
    public static final String PACKAGE_NOT_FOUND_CODE = "PACKAGE_NOT_FOUND_CODE";
    public static final String PACKAGE_NOT_FOUND_VI = "Không tìm thấy mã gói";

    // end package

    // role
    public static final String ROLE_DEFAULT_NOT_FOUND_VI = "Không tìm thấy quyền mặc định";
    public static final String ROLE_NOT_FOUND_CODE = "ROLE_NOT_FOUND_CODE";

    // end role

    // register
    public static final String USER_NAME_TRIAL_DUPLICATE_VI = "%s này đã được đăng ký trước đó, vui lòng kiểm tra lại!";
    public static final String USER_NAME_TRIAL_DUPLICATE = "USER_NAME_DUPLICATE_VI";
    public static final String START_DATE_INVALID_VI = "Ngày bắt đầu và ngày kết thúc không hợp lệ";
    public static final String START_DATE_INVALID_CODE = "START_DATE_INVALID_CODE";
    public static final String START_TO_INVALID_VI = "Ngày kết thúc tài khoản phải lớn hơn ngày khởi tạo";
    public static final String START_TO_INVALID_CODE = "START_TO_INVALID_CODE";
    public static final String USER_NAME_INVALID = "{\"code\": \"USER_NAME_INVALID\",\"message\": \"Tên đăng nhập không hợp lệ\"}";
    public static final String USER_NAME_DUPLICATE_VI = "Tên tài khoản đã tồn tại";
    public static final String USER_NAME_DUPLICATE = "USER_NAME_DUPLICATE_VI";
    public static final String REGISTER_UPDATE_COMPANY_INVALID_VI = "Không thể cập nhật công ty chi nhánh";
    public static final String REGISTER_UPDATE_COMPANY_INVALID = "REGISTER_UPDATE_COMPANY_INVALID";
    public static final String PHONE_NUMBER_INVALID = "PHONE_NUMBER_INVALID";
    public static final String EMAIL_NULL_WITH_MOBI_PONE = "EMAIL_NULL_WITH_MOBI_PONE";
    public static final String EMAIL_NULL_WITH_MOBI_PONE_VI =
        "Hệ thống chưa hỗ trợ số mobiphone. Bạn vui lòng nhập email để hệ thống gửi mã xác thực về email!";

    public static final String REGISTER_OTP_INVALID_VI = "Mã xác thực không hợp lệ";
    public static final String REGISTER_OTP_INVALID = "REGISTER_OTP_INVALID";
    public static final String REGISTER_OTP_INCORRECT_VI = "Mã xác thực không chính xác hoặc đã hết hạn";

    // receipt - payment
    public static final String RECEIPT_NOT_EXISTS = "Phiếu thu này không tồn tại";
    public static final String RECEIPT_NOT_EXISTS_CODE = "RECEIPT_NOT_EXISTS_CODE";
    public static final String RECEIPT_INVALID = "Phiếu thu không hợp lệ";
    public static final String RECEIPT_INVALID_CODE = "RECEIPT_INVALID_CODE";
    public static final String RECEIPT_PAYMENT_CREATE_PROHIBITED_VI = "Không được phép tạo phiếu thu/chi";
    public static final String RECEIPT_PAYMENT_CREATE_PROHIBITED = "RECEIPT_PAYMENT_CREATE_PROHIBITED";
    public static final String RECEIPT_CREATE_PROHIBITED = "Không được phép tạo phiếu thu";
    public static final String RECEIPT_UPDATE_PROHIBITED = "Không được phép cập nhật phiếu thu";
    public static final String RECEIPT_UPDATE_PROHIBITED_CODE = "RECEIPT_UPDATE_PROHIBITED_CODE";
    public static final String RECEIPT_DELETE_PROHIBITED_CODE_VI = "Không được phép xóa phiếu thu";
    public static final String RECEIPT_DELETE_PROHIBITED_CODE = "RECEIPT_DELETE_PROHIBITED_CODE";
    public static final String PAYMENT_INVALID = "Phiếu chi không hợp lệ";
    public static final String PAYMENT_INVALID_CODE = "PAYMENT_INVALID_CODE";
    public static final String PAYMENT_NOT_EXISTS = "Phiếu chi này không tồn tại";
    public static final String PAYMENT_NOT_EXISTS_CODE = "PAYMENT_NOT_EXISTS_CODE";
    public static final String PAYMENT_UPDATE_PROHIBITED = "Không được phép cập nhật phiếu chi";
    public static final String PAYMENT_UPDATE_PROHIBITED_CODE = "PAYMENT_UPDATE_PROHIBITED_CODE";
    public static final String PAYMENT_DELETE_PROHIBITED_CODE_VI = "Không được phép xóa phiếu chi";
    public static final String PAYMENT_DELETE_PROHIBITED_CODE = "PAYMENT_DELETE_PROHIBITED_CODE";
    public static final String TYPE_INVALID_VI = "Loại giao dịch không hợp lệ";
    public static final String TYPE_INVALID = "TYPE_INVALID";
    public static final String AMOUNT_INVALID_VI = "Số tiền phải lớn hơn 0";
    public static final String AMOUNT_INVALID_CODE = "AMOUNT_INVALID_CODE";

    // end

    // statistic
    public static final String STATISTIC_DATE_INVALID_FORMAT_CODE = "STATISTIC_DATE_INVALID_FORMAT_CODE";
    public static final String STATISTIC_DATE_INVALID_FORMAT_VI = "Thời gian không đúng định dạng";
    public static final String STATISTIC_FROM_DATE_INVALID_CODE = "STATISTIC_FROM_DATE_INVALID_CODE";
    public static final String STATISTIC_FROM_DATE_INVALID_VI = "Thời gian bắt đầu không hợp lệ";

    public static final String STATISTIC_TO_DATE_INVALID_CODE = "STATISTIC_TO_DATE_INVALID_CODE";
    public static final String STATISTIC_TO_DATE_INVALID_VI = "Thời gian kết thúc không hợp lệ";

    public static final String DELETE_OWNER_ERROR = "DELETE_OWNER_ERROR";
    public static final String DELETE_OWNER_ERROR_VI = "Không thể xóa chủ cửa hàng";

    public static final String DELETE_EMPLOYEE_ERROR = "DELETE_OWNER_ERROR";
    public static final String DELETE_EMPLOYEE_ERROR_VI = "Không thể xóa chính bạn";
    public static final String EMPLOYEE_NOT_FOUND = "EMPLOYEE_NOT_FOUND";

    // end statistic

    public static final String HASH_INCORRECT_CODE = "HASH_INCORRECT_CODE";
    public static final String HASH_INCORRECT_VI = "Mã hash không trùng khớp";
    public static final String TOKEN_EXPIRED = "TOKEN_EXPIRED";
    public static final String TOKEN_EXPIRED_VI = "Phiên đăng nhập đã hết hạn";

    public static final String OTP_NOT_NULL = "{\"code\": \"OTP_NOT_NULL\",\"message\": \"Mã xác minh không được bỏ trống\"}";
    public static final String OTP_INVALID = "OTP_INVALID";
    public static final String OTP_INVALID_VI = "Mã xác minh không hợp lệ";

    public static final String MAX_TIME_REQUEST_FORGOT = "MAX_TIME_REQUEST_FORGOT";
    public static final String MAX_TIME_REQUEST_FORGOT_VI = "Đã vượt quá số lần nhận OTP. Quý khách vui lòng thử lại sau 30 phút.";

    public static final String OTP_INCORRECT = "OTP_INCORRECT";
    public static final String OTP_INCORRECT_VI = "Mã xác minh không chính xác hoặc đã hết hạn";

    // rs_inout_ward
    public static final String RS_INOUT_WARD_ID_NOT_FOUND = "RS_INOUT_WARD_ID_NOT_FOUND";
    public static final String RS_INOUT_WARD_ID_NOT_FOUND_VI = "Không tìm thấy thông tin nhập/xuất kho";
    public static final String RS_INOUT_WARD_ID_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_ID_NOT_NULL\",\"message\": \"Id chứng từ không để trống\"}";
    public static final String RS_INOUT_WARD_CODE_NOT_BLANK =
        "{\"code\": \"RS_INOUT_WARD_CODE_NOT_BLANK\",\"message\": \"Mã chứng từ không để trống\"}";
    public static final String RS_INOUT_WARD_INVALID_CODE = "RS_INOUT_WARD_INVALID_CODE";
    public static final String RS_INOUT_WARD_INVALID_VI = "Chứng từ nhập/xuất kho không hợp lệ";

    public static final String RS_INOUT_WARD_INVALID_ID_VI = "ID chứng từ nhập/xuất kho không hợp lệ";
    public static final String RS_INOUT_WARD_INVALID_ID = "RS_INOUT_WARD_INVALID_ID";
    public static final String RS_INOUT_WARD_CODE_NOT_FOUND_VI = "Không tìm thấy mã chứng từ nhập/xuất kho";
    public static final String RS_INOUT_WARD_CODE_NOT_FOUND = "RS_INOUT_WARD_CODE_NOT_FOUND";
    public static final String RS_INOUT_WARD_TYPE_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_TYPE_NOT_NULL\",\"message\": \"Loại giao dịch không để trống\"}";
    public static final String RS_INOUT_WARD_TYPE_INVALID =
        "{\"code\": \"RS_INOUT_WARD_TYPE_INVALID\",\"message\": \"Loại giao dịch không hợp lệ\"}";
    public static final String RS_INOUT_WARD_TYPE_INVALID_CODE = "RS_INOUT_WARD_TYPE_INVALID_CODE";
    public static final String RS_INOUT_WARD_TYPE_INVALID_VI = "Loại giao dịch không hợp lệ";
    public static final String RS_INOUT_WARD_TYPE_DESC_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_TYPE_DESC_NOT_NULL\",\"message\": \"Mô tả loại chứng từ không để trống\"}";
    public static final String RS_INOUT_WARD_QUANTITY_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_QUANTITY_NOT_NULL\",\"message\": \"Tổng số lượng không để trống\"}";
    public static final String RS_INOUT_WARD_QUANTITY_INVALID =
        "{\"code\": \"RS_INOUT_WARD_QUANTITY_INVALID\",\"message\": \"Tổng số lượng không để trống\"}";
    public static final String RS_INOUT_WARD_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền hàng không để trống\"}";
    public static final String RS_INOUT_WARD_DISCOUNT_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_DISCOUNT_AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền chiết khấu không để trống\"}";
    public static final String RS_INOUT_WARD_COST_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_COST_AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền chi phí phát sinh không để trống\"}";
    public static final String RS_INOUT_WARD_TOTAL_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_TOTAL_AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền thanh toán không để trống\"}";
    public static final String RS_INOUT_WARD_DETAIL_NOT_EMPTY =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_NOT_EMPTY\",\"message\": \"Chi tiết nhập kho không để trống\"}";
    public static final String RS_INOUT_WARD_PAYMENT_METHOD_NOT_BLANK =
        "{\"code\": \"RS_INOUT_WARD_PAYMENT_METHOD_NOT_BLANK\",\"message\": \"Hình thức thanh toán không để trống\"}";
    public static final String RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS = "RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS";
    public static final String RS_INOUT_WARD_PRODUCT_LIST_NOT_EXISTS_VI = "Danh sách sản phẩm không hợp lệ";
    public static final String RS_INOUT_WARD_PRODUCT_UNIT_LIST_INVALID = "RS_INOUT_WARD_PRODUCT_UNIT_LIST_INVALID";
    public static final String RS_INOUT_WARD_PRODUCT_UNIT_LIST_INVALID_VI = "Danh sách sản phẩm và đơn vị tính không hợp lệ";
    public static final String RS_INOUT_WARD_DETAIL_POSITION_NOT_EMPTY =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_POSITION_NOT_EMPTY\",\"message\": \"Số thứ tự sản phẩm không để trống\"}";
    public static final String RS_INOUT_WARD_DETAIL_POSITION_INVALID_EMPTY =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_POSITION_INVALID_EMPTY\",\"message\": \"Số thứ tự sản phẩm không hợp lệ\"}";
    public static final String RS_INOUT_WARD_DETAIL_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_AMOUNT_NOT_NULL\",\"message\": \"Thành tiền sản phẩm không để trống\"}";
    public static final String RS_INOUT_WARD_DETAIL_DISCOUNT_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_DISCOUNT_AMOUNT_NOT_NULL\",\"message\": \"Tiền chiết khấu sản phẩm không để trống\"}";
    public static final String RS_INOUT_WARD_DETAIL_TOTAL_AMOUNT_NOT_NULL =
        "{\"code\": \"RS_INOUT_WARD_DETAIL_TOTAL_AMOUNT_NOT_NULL\",\"message\": \"Tổng tiền thanh toán cho sản phẩm không để trống\"}";
    public static final String RS_OUT_WARD_QUANTITY_INVALID = "RS_OUT_WARD_QUANTITY_INVALID";
    public static final String RS_OUT_WARD_QUANTITY_INVALID_VI = "Số lượng xuất kho không vượt quá số lượng tồn kho.";
    // end

    // mcPayment, mcReceipt
    public static final String MC_PAYMENT_NOT_FOUND_VI = "Không tìm thấy thông tin phiếu chi";
    public static final String MC_PAYMENT_NOT_FOUND = "MC_PAYMENT_NOT_FOUND";
    public static final String MC_RECEIPT_NOT_FOUND_VI = "Không tìm thấy thông tin phiếu thu";
    public static final String MC_RECEIPT_NOT_FOUND = "MC_RECEIPT_NOT_FOUND";

    public static final String MC_PAYMENT_RECEIPT_NOT_FOUND_VI = "Không tìm thấy thông tin phiếu thu/chi";
    public static final String MC_PAYMENT_RECEIPT_NOT_FOUND = "MC_PAYMENT_RECEIPT_NOT_FOUND";

    // end

    // business type
    public static final String BUSINESS_TYPE_NOT_FOUND_VI = "Loại nghiệp vụ không tồn tại";
    public static final String BUSINESS_TYPE_NOT_FOUND = "BUSINESS_TYPE_NOT_FOUND";
    public static final String BUSINESS_TYPE_NAME_EXISTED_VI = "Tên loại nghiệp vụ đã tồn tại";
    public static final String BUSINESS_TYPE_NAME_EXISTED = "BUSINESS_TYPE_NAME_EXISTED";

    public static final String BUSINESS_TYPE_NAME_INVALID_VI = "Tên loại nghiệp vụ không hợp lệ hoặc bỏ trống";
    public static final String BUSINESS_TYPE_NAME_INVALID = "BUSINESS_TYPE_NAME_INVALID";
    public static final String BUSINESS_TYPE_NAME_UPDATE_BANNED_VI = "Không được phép cập nhật kiểu nghiệp vụ";
    public static final String BUSINESS_TYPE_NAME_UPDATE_BANNED = "BUSINESS_TYPE_NAME_UPDATE_BANNED";

    //end

    // config
    public static final String CONFIG_EXT_FORMULA_INVALID = "CONFIG_EXT_FORMULA_INVALID";
    public static final String CONFIG_EXT_FORMULA_INVALID_VI = "Phép tính cấu hình không hợp lệ";
    public static final String CONFIG_EXT_POSITION_INVALID = "CONFIG_EXT_POSITION_INVALID";
    public static final String CONFIG_EXT_POSITION_INVALID_VI = "Thứ tự cấu hình không hợp lệ";
    public static final String CONFIG_EXT_VALUE_NOT_NULL = "CONFIG_EXT_VALUE_NOT_NULL";
    public static final String CONFIG_EXT_VALUE_NOT_NULL_VI = "Giá trị dịch vụ không để trống";
    public static final String CONFIG_EXT_TYPE_NOT_NULL = "CONFIG_EXT_TYPE_NOT_NULL";
    public static final String CONFIG_EXT_TYPE_NOT_NULL_VI = "Loại dịch vụ không để trống";
    public static final String CONFIG_EXT_VARIABLE_INVALID = "CONFIG_EXT_VARIABLE_NOT_NULL";
    public static final String CONFIG_EXT_VARIABLE_INVALID_VI = "Trường tính toán không hợp lệ";
    public static final String CONFIG_EXT_BILL_INVALID = "CONFIG_EXT_BILL_INVALID";
    public static final String CONFIG_EXT_BILL_INVALID_VI = "Thông cấu hình thuế suất đơn hàng không hợp lệ";
    public static final String SYSTEM_INVALID_ERROR = "SYSTEM_INVALID_ERROR";
    public static final String SYSTEM_INVALID_ERROR_VI = "Cấu hình đến hệ thống Hóa đơn điện tử không chính xác (sai hệ thống)";
    public static final String PUBLISH_EASY_INVOICE_ERROR = "PUBLISH_EASY_INVOICE_ERROR";
    public static final String PUBLISH_EASY_INVOICE_ERROR_VI = "Cấu hình đến hệ thống Hóa đơn điện tử không chính xác (sai URL)";

    public static final String CONFIG_INV_DYNAMIC_INVALID_VI = "Thông tin cấu hình không cho phép nhập nội dung giảm giá";
    public static final String CONFIG_INV_DYNAMIC_INVALID_CODE = "CONFIG_INV_DYNAMIC_INVALID_CODE";

    public static final String GET_URL_EASY_INVOICE_ERROR = "GET_URL_EASY_INVOICE_ERROR";
    public static final String GET_URL_EASY_INVOICE_ERROR_VI = "Không lấy được danh sách mẫu số hóa đơn";

    public static final String LOGIN_EASY_INVOICE_ERROR = "LOGIN_EASY_INVOICE_ERROR";
    public static final String LOGIN_EASY_INVOICE_ERROR_VI = "Đăng nhập vào hệ thống easy-invoice thất bại";
    public static final String CONFIG_OVER_STOCK_INVALID =
        "{\"code\": \"CONFIG_OVER_STOCK_INVALID\",\"message\": \"Giá trị cho phép tồn kho không hợp lệ\"}";
    public static final String CONFIG_OVER_STOCK_INVALID_VI = "Thông tin cấu hình cho phép tồn kho không hợp lệ";
    public static final String CONFIG_OVER_STOCK_INVALID_CODE = "CONFIG_OVER_STOCK_NULL";
    public static final String CONFIG_IS_BUYER_INVALID_VI = "Thông tin cấu hình không cho phép nhập tên người mua";
    public static final String CONFIG_IS_BUYER_INVALID_CODE = "CONFIG_IS_BUYER_INVALID_CODE";
    public static final String TAX_REDUCTION_BILL_CODE = "TAX_REDUCTION_BILL_CODE";
    public static final String TAX_REDUCTION_BILL_VI = "Vui lòng Hoàn thành hoặc Hủy đơn hàng trước khi thay đổi Loại hình giảm thuế";
    // end confg

    // bill
    public static final String CREATE_BILL_QUANTITY_OUT_STOCK = "CREATE_BILL_QUANTITY_OUT_STOCK";
    public static final String CREATE_BILL_QUANTITY_OUT_STOCK_VI = "Sản phẩm vượt quá số lượng tồn kho";
    public static final String DISCOUNT_VAT_RATE_NULL = "DISCOUNT_VAT_RATE_NULL";
    public static final String DISCOUNT_VAT_RATE_NULL_VI = "Thuế suất giảm trừ không được để trống";
    public static final String DISCOUNT_VAT_AMOUNT_NULL = "DISCOUNT_VAT_AMOUNT_NULL";
    public static final String DISCOUNT_VAT_AMOUNT_NULL_VI = "Tiền giảm trừ không được để trống";
    public static final String DISCOUNT_PRODUCT_NULL = "DISCOUNT_PRODUCT_NULL";
    public static final String DISCOUNT_PRODUCT_NULL_VI = "Không tìm thấy sản phẩm giảm trừ";
    public static final String DISCOUNT_AMOUNT_NOT_MATCH = "DISCOUNT_AMOUNT_NOT_MATCH";
    public static final String DISCOUNT_AMOUNT_NOT_MATCH_VI = "Số tiền giảm trừ không khớp";
    public static final String RETURN_BILL_QUANTITY_INVALID_VI = "Số lượng trả của sản phẩm %s vượt quá số lượng ở đơn hàng";
    public static final String RETURN_BILL_QUANTITY_INVALID = "RETURN_BILL_QUANTITY_INVALID_VI";
    public static final String DISCOUNT_VAT_RATE_IS_NULL = "DISCOUNT_VAT_RATE_IS_NULL";
    public static final String DISCOUNT_VAT_RATE_IS_NULL_VI = "Đơn hàng không có giảm trừ thuế";

    // end bill

    // import excelProduct
    public static final String PRODUCT_GROUP_EMPTY = "PRODUCT_GROUP_EMPTY";
    public static final String PRODUCT_GROUP_EMPTY_VI = "Danh sách loại sản phẩm trống trên hệ thống EasyPos";
    public static final String PRODUCT_GROUP_NOT_EXISTS = "PRODUCT_GROUP_NOT_EXISTS";
    public static final String PRODUCT_GROUP_NOT_EXISTS_VI = "Loại sản phẩm không tồn tại";
    public static final String PRODUCT_IMPORT_EXCEL_FAIL_VI = "Nhập sản phẩm thất bại";
    public static final String PRODUCT_IMPORT_EXCEL_FAIL = "PRODUCT_IMPORT_EXCEL_FAIL";

    //

    // package
    public static final String PACKAGE_CODE_NOT_FOUND_CODE = "PACKAGE_CODE_NOT_FOUND_CODE";
    public static final String PACKAGE_CODE_NOT_FOUND_VI = "Không tìm thấy mã gói";
    public static final String PACKAGE_DEFAULT_NOT_FOUND_CODE = "PACKAGE_DEFAULT_NOT_FOUND_CODE";
    public static final String PACKAGE_DEFAULT_NOT_FOUND_VI = "Không tìm thấy gói dùng thử";
    public static final String PACKAGE_NOT_EXISTS_CODE = "PACKAGE_NOT_EXISTS_CODE";
    public static final String PACKAGE_NOT_EXISTS_VI = "Gói này không tồn tại";
    public static final String PACKAGE_NAME_NOT_BLANK = "{\"code\": \"PACKAGE_NAME_NOT_BLANK\",\"message\": \"Tên gói không để trống\"}";
    public static final String PACKAGE_EPX_NOT_NULL =
        "{\"code\": \"PACKAGE_EPX_NOT_NULL\",\"message\": \"Hạn sử dụng của gói không để trống\"}";
    public static final String PACKAGE_EPX_INVALID =
        "{\"code\": \"PACKAGE_EPX_INVALID\",\"message\": \"Hạn sử dụng của gói không hợp lệ\"}";
    public static final String PACKAGE_STATUS_NOT_NULL =
        "{\"code\": \"PACKAGE_STATUS_NOT_NULL\",\"message\": \"Trạng thái gói không để trống\"}";
    public static final String PACKAGE_STATUS_INVALID =
        "{\"code\": \"PACKAGE_STATUS_INVALID\",\"message\": \"Trạng thái gói không hợp lệ\"}";
    public static final String PACKAGE_CODE_INVALID = "{\"code\": \"PACKAGE_CODE_INVALID\",\"message\": \"Mã gói không hợp lệ\"}";
    public static final String PACKAGE_ID_NOT_NULL_CODE = "PACKAGE_ID_NOT_NULL_CODE";
    public static final String PACKAGE_ID_NOT_NULL_VI = "Không để trống id gói";
    public static final String PACKAGE_NAME_DUPLICATE_CODE = "PACKAGE_NAME_DUPLICATE_CODE";
    public static final String PACKAGE_NAME_DUPLICATE_VI = "Trùng tên gói";
    public static final String PACKAGE_CREATE_EB88_ERROR_CODE = "PACKAGE_CREATE_EB88_ERROR_CODE";
    public static final String PACKAGE_CREATE_EB88_ERROR_VI = "Không tạo được gói bên EB88";

    // end package

    // company owner
    public static final String OWNER_USER_NOT_FOUND_CODE = "OWNER_USER_NOT_FOUND_CODE";
    public static final String OWNER_USER_NOT_FOUND_VI = "Chủ sở hữu không tồn tại";
    public static final String OWNER_NAME_NOT_EMPTY = "{\"code\": \"OWNER_NAME_NOT_EMPTY\",\"message\": \"Không để trống tên công ty\"}";
    public static final String OWNER_NAME_INVALID = "{\"code\": \"OWNER_NAME_INVALID\",\"message\": \"Tên công ty không hợp lệ\"}";
    public static final String OWNER_ID_NOT_EXISTS_CODE = "OWNER_ID_NOT_EXISTS_CODE";
    public static final String OWNER_ID_NOT_EXISTS_VI = "Công ty cha không tồn tại";
    public static final String OWNER_ID_NOT_NULL = "{\"code\": \"OWNER_ID_NOT_NULL\",\"message\": \"Id công ty cha không để trống\"}";
    // end company owner

    // config
    public static final String CONFIG_NOT_EXISTS_CODE = "CONFIG_NOT_EXISTS_CODE";
    public static final String CONFIG_NOT_EXISTS_VI = "Không tìm thấy thông tin cấu hình";
    public static final String CONFIG_CODE_NOT_BLANK =
        "{\"code\": \"CONFIG_CODE_NOT_BLANK\",\"message\": \"Không để trống mã code cấu hình\"}";
    public static final String CONFIG_CODE_EXISTS_CODE = "CONFIG_CODE_EXISTS_CODE";
    public static final String CONFIG_CODE_EXISTS_VI = "Mã code đã tồn tại";

    public static final String CONFIG_COMPANY_NULL_EB88 = "Thông tin cấu hình công ty trống";

    public static final String CONFIG_USERNAME_INVALID_EB88 = "Thông tin cấu hình người dùng không hợp lệ";

    // end config

    // business
    public static final String BUSINESS_NOT_EXISTS_CODE = "BUSINESS_NOT_EXISTS_CODE";
    public static final String BUSINESS_NOT_EXISTS_VI = "Loại hình kinh doanh không tồn tại";

    // end business

    // import excel
    public static final String EXCEL_FILE_ERROR_VI = "Tệp excel không đúng định dạng";
    public static final String EXCEL_FILE_ERROR = "EXCEL_FILE_ERROR";
    public static final String EXCEL_FILE_PRODUCT_NAME_BLANK = "Tên sản phẩm trống";
    public static final String EXCEL_FILE_PRODUCT_CODE2_BLANK = "Mã sản phẩm trống";
    public static final String EXCEL_FILE_PRODUCT_GROUP_NOT_EXISTS = "Nhóm sản phẩm không tồn tại";
    public static final String EXCEL_FILE_PRODUCT_BAR_CODE_DUPLICATE = "Trùng mã vạch sản phẩm";
    public static final String EXCEL_FILE_PRODUCT_CODE2_DUPLICATE = "Trùng mã sản phẩm";
    public static final String EXCEL_FILE_PRODUCT_SALE_PRICE_NULL = "Giá bán trống";

    // end import

    public static final String PRINT_CONFIG_NOT_FOUND = "PRINT_CONFIG_NOT_FOUND";
    public static final String PRINT_CONFIG_NOT_FOUND_VI = "Không tìm thấy thông tin cấu hình máy in";
    public static final String PRINT_CONFIGS_NOT_NULL =
        "{\"code\": \"PRINT_CONFIGS_NOT_NULL\",\"message\": \"Danh sách cấu hình máy in không được để trống\"}";

    // report
    public static final String REPORT_HOT_SALES_TYPE_INVALID = "REPORT_HOT_SALES_TYPE_INVALID";
    public static final String REPORT_HOT_SALES_TYPE_INVALID_VI = "Phân loại sản phẩm bán chạy không hợp lệ";

    // end report

    // integration
    public static final String RECEIVE_INTEGRATION_NOT_NULL =
        "{\"code\": \"RECEIVE_INTEGRATION_NOT_NULL\",\"message\": \"Người nhận không được để trống\"}";
    public static final String SUBJECT_INTEGRATION_NOT_NULL =
        "{\"code\": \"SUBJECT_INTEGRATION_NOT_NULL\",\"message\": \"Tiêu đề không được để trống\"}";
    public static final String TEXT_INTEGRATION_NOT_NULL =
        "{\"code\": \"TEXT_INTEGRATION_NOT_NULL\",\"message\": \"Nội dung không được để trống\"}";
    public static final String SERVICE_INTEGRATION_NOT_NULL =
        "{\"code\": \"SERVICE_INTEGRATION_NOT_NULL\",\"message\": \"Hệ thống nguồn không được để trống\"}";
    public static final String BRANCH_NAME_INTEGRATION_NOT_NULL =
        "{\"code\": \"BRAND_NAME_INTEGRATION_NOT_NULL\",\"message\": \"Tên thương hiệu không được để trống\"}";
    public static final String BRANCH_NAME_INTEGRATION_NOT_FOUND_VI = "Tên thương hiệu [@@brandName] không tồn tại trong hệ thống";
    public static final String BRANCH_NAME_INTEGRATION_NOT_FOUND = "BRANCH_NAME_INTEGRATION_NOT_FOUND";
    public static final String RECEIVE_INVALID = "RECEIVE_INVALID";
    public static final String RECEIVE_INVALID_VI = "Người nhận không hợp lệ";

    // end integration

    // card
    public static final String CARD_NAME_NOT_NULL = "{\"code\": \"CARD_NAME_NOT_NULL\",\"message\": \"Tên thẻ không được để trống\"}";
    public static final String CARD_STATUS_INVALID = "{\"code\": \"CARD_STATUS_INVALID\",\"message\": \"Trạng thái thẻ không hợp lệ\"}";
    public static final String CARD_RANK_NOT_NULL = "{\"code\": \"CARD_RANK_NOT_NULL\",\"message\": \"Thứ tự thẻ không được bỏ trống\"}";
    public static final String CARD_ID_NOT_NULL = "{\"code\": \"CARD_ID_NOT_NULL\",\"message\": \"ID thẻ không được bỏ trống\"}";
    public static final String CARD_NOT_FOUND = "CARD_NOT_FOUND";
    public static final String CARD_NOT_FOUND_VI = "Không tìm thấy thông tin thẻ";
    public static final String CARD_NAME_EXIST = "CARD_NAME_EXIST";
    public static final String CARD_NAME_EXIST_VI = "Tên thẻ đã tồn tại";
    public static final String CARD_DEFAULT_EXIST = "CARD_DEFAULT_EXIST";
    public static final String CARD_NAME_NOT_NULL_VI = "Tên thẻ không được để trống";
    public static final String CARD_NAME_NOT_NULL_CODE = "CARD_NAME_NOT_NULL_CODE";
    public static final String CARD_DEFAULT_EXIST_VI = "Chỉ được phép tối đa 1 thẻ mặc định";
    public static final String CARD_LIST_INVALID = "CARD_LIST_INVALID";
    public static final String CARD_LIST_INVALID_VI = "Danh sách thẻ không hợp lệ";
    public static final String PAYMENT_BY_CARD_INVALID = "PAYMENT_BY_CARD_INVALID";
    public static final String PAYMENT_BY_CARD_INVALID_VI = "Thanh toán qua thẻ không áp dụng cho Khách lẻ";
    public static final String CARD_NOT_EXIST = "CARD_NOT_EXIST";
    public static final String CARD_NOT_EXIST_VI = "Khách hàng chưa có thẻ";
    public static final String CARD_BALANCE_INVALID = "CARD_BALANCE_INVALID";
    public static final String CARD_BALANCE_INVALID_VI = "Số dư trong thẻ không đủ";
    public static final String CARD_POLICY_RUNNING = "CARD_POLICY_RUNNING";
    public static final String CARD_POLICY_RUNNING_VI = "Thẻ này không thể xóa do còn chính sách đang chạy";

    public static final String CARD_HISTORY_CUSTOMER_LIST_EMPTY =
        "{\"code\": \"CARD_HISTORY_CUSTOMER_LIST_EMPTY\",\"message\": \"Danh sách khách hàng không để trống\"}";
    public static final String DEFAULT_CARD_NOT_STOP = "DEFAULT_CARD_NOT_STOP";
    public static final String DEFAULT_CARD_NOT_STOP_VI = "Thẻ mặc định bắt buộc hoạt động";
    public static final String DEFAULT_CARD_NOT_DELETE = "DEFAULT_CARD_NOT_DELETE";
    public static final String DEFAULT_CARD_NOT_DELETE_VI = "Không được phép xóa thẻ mặc định";
    public static final String DEFAULT_CARD_NOT_DELETE_VN = "Để hủy thẻ mặc định này, vui lòng chọn thẻ khác làm thẻ mặc định mới.";
    public static final String SORT_CARD_INVALID_VI = "Thẻ mặc định không thể được sắp xếp (luôn là Hạng 1).";
    public static final String SORT_CARD_INVALID = "SORT_CARD_INVALID";

    // end card

    // card policy
    public static final String CARD_POLICY_ID_NULL_VI = "Id chính sách thẻ không để trống";
    public static final String CARD_POLICY_ID_NULL = "LOYALTY_CARD_POLICY_ID_NULL";
    public static final String CARD_POLICY_CONDITION_INVALID_VI = "Điều kiệp áp dụng chính sách không hợp lệ";
    public static final String CARD_POLICY_CONDITION_INVALID = "CARD_POLICY_CONDITION_INVALID";
    public static final String CARD_POLICY_NOT_FOUND_VI = "Chính sách thẻ không tồn tại";
    public static final String CARD_POLICY_NOT_FOUND = "CARD_POLICY_NOT_FOUND";
    public static final String CARD_POLICY_EXISTS_VI = "Chính sách thẻ đã tồn tại";
    public static final String CARD_POLICY_EXISTS = "CARD_POLICY_EXISTS";
    public static final String CARD_POLICY_CARDS_EMPTY_VI = "Danh sách thẻ áp dụng trống";
    public static final String CARD_POLICY_CARDS_EMPTY = "CARD_POLICY_CARDS_EMPTY";
    public static final String LOYALTY_CARD_LIST_INVALID_VI = "Danh sách thẻ không hợp lệ";
    public static final String LOYALTY_CARD_LIST_INVALID = "LOYALTY_CARD_LIST_INVALID";
    public static final String LOYALTY_CARD_START_DATE_INVALID_VI = "Ngày áp dụng không hợp lệ";
    public static final String LOYALTY_CARD_START_DATE_INVALID = "LOYALTY_CARD_START_DATE_INVALID";
    public static final String CARD_POLICY_UPGRADE_TYPE_INVALID =
        "{\"code\": \"CARD_POLICY_UPGRADE_TYPE_INVALID\",\"message\": \"Căn cứ nâng hạng không hợp lệ\"}";
    public static final String CARD_POLICY_CONDITIONS_NOT_NULL =
        "{\"code\": \"CARD_POLICY_CONDITIONS_NOT_NULL\",\"message\": \"Không để trống danh sách thẻ áp dụng\"}";
    public static final String CARD_POLICY_START_DATE_NOT_NULL =
        "{\"code\": \"CARD_POLICY_START_DATE_NOT_NULL\",\"message\": \"Không để trống ngày bắt đầu áp dụng\"}";
    public static final String CARD_POLICY_CARD_ID_NOT_NULL =
        "{\"code\": \"CARD_POLICY_CARD_ID_NOT_NULL\",\"message\": \"Không để trống ID thẻ\"}";
    public static final String CARD_POLICY_CARD_IS_DEFAULT_NOT_NULL =
        "{\"code\": \"CARD_POLICY_CARD_IS_DEFAULT_NOT_NULL\",\"message\": \"Không để loại thẻ\"}";
    public static final String CARD_POLICY_ACCUM_VALUE_NOT_NULL =
        "{\"code\": \"CARD_POLICY_ACCUM_VALUE_NOT_NULL\",\"message\": \"Không để trống giá trị tích điểm\"}";
    public static final String CARD_POLICY_ACCUM_VALUE_INVALID =
        "{\"code\": \"CARD_POLICY_ACCUM_VALUE_INVALID\",\"message\": \"Giá trị tích điểm không hợp lệ\"}";
    public static final String CARD_POLICY_REDEEM_VALUE_NOT_NULL =
        "{\"code\": \"CARD_POLICY_REDEEM_VALUE_NOT_NULL\",\"message\": \"Không để trống giá trị quy đổi\"}";
    public static final String CARD_POLICY_REDEEM_VALUE_INVALID =
        "{\"code\": \"CARD_POLICY_REDEEM_VALUE_INVALID\",\"message\": \"Giá trị quy đổi không hợp lệ\"}";
    public static final String CARD_POLICY_UPGRADE_VALUE_NOT_NULL = "CARD_POLICY_UPGRADE_VALUE_NOT_NULL";
    public static final String CARD_POLICY_UPGRADE_VALUE_NOT_NULL_VI = "Không để trống giá trị nâng hạng";
    public static final String CARD_POLICY_UPGRADE_VALUE_INVALID =
        "{\"code\": \"CARD_POLICY_UPGRADE_VALUE_INVALID\",\"message\": \"Giá trị nâng hạng không hợp lệ\"}";
    public static final String CARD_POLICY_UPGRADE_VALUE_INVALID_CODE = "CARD_POLICY_UPGRADE_VALUE_INVALID_CODE";
    public static final String CARD_POLICY_UPGRADE_VALUE_INVALID_VI = "Giá trị nâng hạng %s không hợp lệ";
    public static final String CARD_POLICY_UPGRADE_TIME_INVALID =
        "{\"code\": \"CARD_POLICY_UPGRADE_TIME_INVALID\",\"message\": \"Thời gian nâng hạng không hợp lệ\"}";
    public static final String CARD_POLICY_SAVE_CARD_EXISTS = "CARD_POLICY_SAVE_CARD_EXISTS";
    public static final String CARD_POLICY_SAVE_CARD_EXISTS_VI = "%s đã được áp dụng ở chính sách khác";

    // end card policy

    // voucher
    public static final String VOUCHER_CODE_NOT_BLANK = "{\"code\": \"VOUCHER_CODE_NOT_BLANK\",\"message\": \"Mã voucher không để trống\"}";
    public static final String VOUCHER_ID_NOT_BLANK = "{\"code\": \"VOUCHER_CODE_NOT_BLANK\",\"message\": \"Id voucher không để trống\"}";
    public static final String VOUCHER_DISCOUNT_NOT_NULL =
        "{\"code\": \"VOUCHER_DISCOUNT_NOT_NULL\",\"message\": \"Giá trị khuyến mãi không để trống\"}";
    public static final String VOUCHER_DISCOUNT_NOT_VALID =
        "{\"code\": \"VOUCHER_DISCOUNT_NOT_VALID\",\"message\": \"Giá trị khuyến mãi không hợp lệ\"}";
    public static final String VOUCHER_ID_NOT_NULL = "VOUCHER_ID_NOT_NULL";
    public static final String VOUCHER_ID_NOT_NULL_VI = "ID voucher không để trống";
    public static final String VOUCHER_CODE_INVALID = "{\"code\": \"VOUCHER_CODE_INVALID\",\"message\": \"Mã voucher không hợp lệ\"}";
    public static final String VOUCHER_NAME_NOT_BLANK =
        "{\"code\": \"VOUCHER_NAME_NOT_BLANK\",\"message\": \"Tên voucher không để trống\"}";
    public static final String VOUCHER_TYPE_NOT_NULL = "{\"code\": \"VOUCHER_TYPE_NOT_NULL\",\"message\": \"Loại voucher không để trống\"}";
    public static final String VOUCHER_DISCOUNT_TYPE_NOT_NULL =
        "{\"code\": \"VOUCHER_DISCOUNT_TYPE_NOT_NULL\",\"message\": \"Loại giảm giá voucher không để trống\"}";
    public static final String VOUCHER_BILL_MIN_VALUE_NOT_NULL = "VOUCHER_BILL_MIN_VALUE_NOT_NULL";
    public static final String VOUCHER_BILL_MIN_VALUE_NOT_NULL_VI = "Giá trị giảm giá đơn hàng không để trống";
    public static final String VOUCHER_BILL_MIN_VALUE_INVALID =
        "{\"code\": \"VOUCHER_BILL_MIN_VALUE_INVALID\",\"message\": \"Giá trị giảm giá đơn hàng không hợp lệ\"}";
    public static final String VOUCHER_BILL_GET_QUANTITY_INVALID =
        "{\"code\": \"VOUCHER_BILL_GET_QUANTITY_INVALID\",\"message\": \"Số lượng hàng khuyến mãi không hợp lệ\"}";
    public static final String VOUCHER_BILL_BUY_QUANTITY_INVALID =
        "{\"code\": \"VOUCHER_BILL_BUY_QUANTITY_INVALID\",\"message\": \"Số lượng hàng mua không hợp lệ\"}";
    public static final String VOUCHER_BILL_GET_QUANTITY_NOT_NULL = "VOUCHER_BILL_GET_QUANTITY_NOT_NULL";
    public static final String VOUCHER_BILL_GET_QUANTITY_NOT_NULL_VI = "Số lượng hàng khuyến mãi không để trống";
    public static final String VOUCHER_BILL_BUY_QUANTITY_NOT_NULL = "VOUCHER_BILL_BUY_QUANTITY_NOT_NULL";
    public static final String VOUCHER_BILL_MAX_VALUE_INVALID_VI = "Giá khởi điểm không hợp lệ %s";
    public static final String VOUCHER_BILL_MAX_VALUE_INVALID = "VOUCHER_BILL_MAX_VALUE_NOT_NULL";

    public static final String VOUCHER_BILL_MIN_MAX_VALUE_INVALID_VI = "Khoảng giá không hợp lệ %s - %s";
    public static final String VOUCHER_BILL_MIN_MAX_VALUE_INVALID = "VOUCHER_BILL_MIN_MAX_VALUE_INVALID_VI";
    public static final String VOUCHER_BILL_BUY_QUANTITY_NOT_NULL_VI = "Số lượng hàng mua không để trống";
    public static final String VOUCHER_GET_PRODUCT_NOT_NULL = "VOUCHER_GET_PRODUCT_NOT_NULL";
    public static final String VOUCHER_GET_PRODUCT_NOT_NULL_VI = "Danh sách sản phẩm/ nhóm sản phẩm khuyến mãi không để trống";
    public static final String VOUCHER_GET_PRODUCT_GROUP_NOT_NULL = "VOUCHER_PRODUCT_GROUP_NOT_NULL";
    public static final String VOUCHER_GET_PRODUCT_GROUP_NOT_NULL_VI = "Danh sách nhóm sản phẩm không để trống";
    public static final String VOUCHER_BUY_PRODUCT_NOT_NULL = "VOUCHER_BUY_PRODUCT_NOT_NULL";
    public static final String VOUCHER_BUY_PRODUCT_NOT_NULL_VI = "Danh sách sản phẩm/ nhóm sản phẩm mua không để trống";
    public static final String VOUCHER_GET_PRODUCT_INVALID = "VOUCHER_GET_PRODUCT_INVALID";
    public static final String VOUCHER_GET_PRODUCT_INVALID_VI = "Sản phẩm khuyến mãi không hợp lệ";
    public static final String VOUCHER_GET_PRODUCT_GROUP_INVALID = "VOUCHER_PRODUCT_GROUP_INVALID";
    public static final String VOUCHER_GET_PRODUCT_GROUP_INVALID_VI = "Nhóm sản phẩm không hợp lệ";
    public static final String VOUCHER_BUY_PRODUCT_INVALID = "VOUCHER_BUY_PRODUCT_INVALID";
    public static final String VOUCHER_BUY_PRODUCT_INVALID_VI = "Sản phẩm mua không hợp lệ";
    public static final String VOUCHER_CONDITION_NOT_NULL = "VOUCHER_CONDITION_NOT_NULL";
    public static final String VOUCHER_CONDITION_NOT_NULL_VI = "Điều kiện voucher không để trống";
    // ext time condition
    public static final String VOUCHER_EXT_TIME_TYPE_NOT_NULL = "VOUCHER_EXT_TIME_TYPE_NOT_NULL";
    public static final String VOUCHER_EXT_TIME_TYPE_NOT_NULL_VI = "Kiểu thời gian không để trống";
    public static final String VOUCHER_EXT_TIME_TYPE_INVALID = "VOUCHER_EXT_TIME_TYPE_INVALID";
    public static final String VOUCHER_EXT_TIME_TYPE_INVALID_VI = "Kiểu thời gian không hợp lệ";
    public static final String VOUCHER_EXT_TIME_VALUE_INVALID = "VOUCHER_EXT_TIME_VALUE_INVALID";
    public static final String VOUCHER_EXT_TIME_VALUE_INVALID_VI = "Giá trị thời gian không hợp lệ";
    public static final String VOUCHER_EXT_TIME_VALUE_NOT_NULL = "VOUCHER_EXT_TIME_VALUE_NOT_NULL";
    public static final String VOUCHER_EXT_TIME_VALUE_NOT_NULL_VI = "Giá trị thời gian không để trống";
    public static final String VOUCHER_EXT_TIME_TYPE_DUPLICATE = "VOUCHER_EXT_TIME_TYPE_DUPLICATE";
    public static final String VOUCHER_EXT_TIME_TYPE_DUPLICATE_VI = "Thời gian áp dụng đã tồn tại: %s";
    public static final String VOUCHER_CONDITION_INVALID = "VOUCHER_CONDITION_INVALID";
    public static final String VOUCHER_CONDITION_INVALID_VI = "Điều kiện áp dụng không hợp lệ";
    public static final String VOUCHER_DIFF_CONDITION_INVALID = "VOUCHER_DIFF_CONDITION_INVALID";
    public static final String VOUCHER_DIFF_CONDITION_INVALID_VI = "Điều kiện khác không hợp lệ";
    // end ext time condition
    public static final String VOUCHER_DISCOUNT_TYPE_INVALID_CODE =
        "{\"code\": \"VOUCHER_DISCOUNT_TYPE_INVALID_CODE\",\"message\": \"Giá trị khuyến mại không hợp lệ\"}";
    public static final String VOUCHER_APPLY_TYPE_NOT_NULL =
        "{\"code\": \"VOUCHER_APPLY_TYPE_NOT_NULL\",\"message\": \"Loại áp dụng voucher không để trống\"}";
    public static final String VOUCHER_APPLY_TYPE_INVALID =
        "{\"code\": \"VOUCHER_APPLY_TYPE_INVALID\",\"message\": \"Loại áp dụng voucher không hợp lệ\"}";
    public static final String VOUCHER_VALUE_NOT_NULL =
        "{\"code\": \"VOUCHER_VALUE_NOT_NULL\",\"message\": \"Giá trị voucher không để trống\"}";
    public static final String VOUCHER_VALUE_INVALID = "VOUCHER_VALUE_INVALID";
    public static final String VOUCHER_VALUE_INVALID_VI = "Giá trị voucher không hợp lệ";
    public static final String VOUCHER_START_DATE_NOT_BLANK =
        "{\"code\": \"VOUCHER_START_DATE_NOT_BLANK\",\"message\": \"Ngày áp dụng voucher không để trống\"}";
    public static final String VOUCHER_END_DATE_NOT_BLANK =
        "{\"code\": \"VOUCHER_END_DATE_NOT_BLANK\",\"message\": \"Ngày kết thúc voucher không để trống\"}";
    public static final String VOUCHER_CODE_EXISTS = "VOUCHER_CODE_EXISTS";
    public static final String VOUCHER_CODE_EXISTS_VI = "Mã voucher đã tồn tại";
    public static final String VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID = "VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID";
    public static final String VOUCHER_DISCOUNT_PERCENT_VALUE_INVALID_VI = "Giá trị giảm giá không hợp lệ";
    public static final String VOUCHER_DISCOUNT_PERCENT_VALUE_NULL = "VOUCHER_DISCOUNT_PERCENT_VALUE_NULL";
    public static final String VOUCHER_DISCOUNT_PERCENT_VALUE_NULL_VI = "Giá trị giảm giá không để trống";
    public static final String VOUCHER_LIST_ID_NOT_EMPTY =
        "{\"code\": \"VOUCHER_LIST_ID_NOT_EMPTY\",\"message\": \"Danh sách voucher áp dụng không để trống\"}";
    public static final String VOUCHER_LIST_ID_INVALID = "VOUCHER_LIST_ID_INVALID";
    public static final String VOUCHER_LIST_ID_INVALID_VI = "Danh sách voucher không hợp lệ";
    public static final String VOUCHER_LIST_CUSTOMER_APPLY_NOT_EMPTY = "VOUCHER_LIST_CUSTOMER_APPLY_NOT_EMPTY";
    public static final String VOUCHER_LIST_CUSTOMER_APPLY_NOT_EMPTY_VI = "Danh sách KH/NCC áp dụng trống";
    public static final String VOUCHER_LIST_CARD_APPLY_NOT_EMPTY = "VOUCHER_LIST_CARD_APPLY_NOT_EMPTY";
    public static final String VOUCHER_LIST_CARD_APPLY_NOT_EMPTY_VI = "Danh sách thẻ áp dụng trống";
    public static final String VOUCHER_LIST_CARD_APPLY_INVALID = "VOUCHER_LIST_CARD_APPLY_INVALID";
    public static final String VOUCHER_LIST_CARD_APPLY_INVALID_VI = "Danh sách thẻ áp dụng không hợp lệ";
    public static final String VOUCHER_LIST_CUSTOMER_APPLY_INVALID = "VOUCHER_LIST_CUSTOMER_APPLY_INVALID";
    public static final String VOUCHER_LIST_CUSTOMER_APPLY_INVALID_VI = "Danh sách KH/NCC áp dụng không hợp lệ";
    public static final String VOUCHER_NOT_FOUND_VI = "Voucher không tồn tại";
    public static final String VOUCHER_NOT_FOUND = "VOUCHER_NOT_FOUND";

    public static final String VOUCHER_NOT_RUNNING_VI = "Voucher [@@name] không còn khả dụng";
    public static final String VOUCHER_IS_END_DATE_VI = "Voucher [@@name] đã hết hạn";

    public static final String CONFIG_VOUCHER_INVALID_VI = "Cấu hình hệ thống không cho phép sử dụng voucher";
    public static final String CONFIG_VOUCHER_INVALID = "CONFIG_VOUCHER_INVALID";
    public static final String CUSTOMER_ID_NOT_NULL_VI = "ID khách hàng không đuược bỏ trống khi áp dụng voucher";
    public static final String VOUCHER_DISCOUNT_TYPE_INVALID = "VOUCHER_DISCOUNT_TYPE_INVALID";
    public static final String VOUCHER_DISCOUNT_TYPE_INVALID_VI = "Loại giảm giá không hợp lệ";
    // end voucher

    // voucher_company
    public static final String VOUCHER_COMPANY_NOT_FOUND_VI = "Thông tin voucher tại công ty không tồn tại";
    public static final String VOUCHER_COMPANY_NOT_FOUND = "VOUCHER_COMPANY_NOT_FOUND";

    // end voucher_company

    //topping
    public static final String TOPPING_GROUP_ID_NOT_NULL =
        "{\"code\": \"TOPPING_GROUP_ID_NOT_NULL\",\"message\": \"Id nhóm bán kèm không được để trống\"}";
    public static final String TOPPING_ID_NOT_NULL =
        "{\"code\": \"TOPPING_ID_NOT_NULL\",\"message\": \"Id sản phẩm bán kèm/nhóm bán kèm không được để trống\"}";
    public static final String IS_TOPPING_NOT_NULL =
        "{\"code\": \"IS_TOPPING_NOT_NULL\",\"message\": \"Lựa chọn có phải là sản phẩm bán kèm không được để trống\"}";
    public static final String TOPPING_GROUP_NAME_NOT_NULL =
        "{\"code\": \"TOPPING_GROUP_NAME_NOT_NULL\",\"message\": \"Tên nhóm bán kèm không được để trống\"}";
    public static final String TOPPING_PRODUCT_NOT_NULL =
        "{\"code\": \"TOPPING_PRODUCT_NOT_NULL\",\"message\": \"Danh sách sản phẩm không được để trống\"}";
    public static final String TOPPING_GROUP_NOT_FOUND = "TOPPING_GROUP_NOT_FOUND";
    public static final String TOPPING_GROUP_NOT_FOUND_VI = "Không tìm thấy nhóm bán kèm";
    public static final String TOPPING_PRODUCT_NOT_NULL_CODE = "TOPPING_PRODUCT_NOT_NULL_CODE";
    public static final String TOPPING_PRODUCT_NOT_NULL_VI = "Danh sách sản phẩm không được để trống";
    public static final String MISSING_REQUIRED_GROUP_CODE = "MISSING_REQUIRED_GROUP_CODE";
    public static final String MISSING_REQUIRED_GROUP_VI = "Sản phẩm [@@] bắt buộc phải bán cùng sản phẩm bán kèm. Vui lòng kiểm tra lại";
    public static final String DUPLICATE_TOPPING_PRODUCT_NAME_CODE = "DUPLICATE_TOPPING_PRODUCT_NAME_CODE";
    public static final String DUPLICATE_TOPPING_PRODUCT_NAME_VI = "Tên nhóm bán kèm đã tồn tại";
    public static final String DUPLICATE_TOPPING_PRODUCT_ID_CODE = "DUPLICATE_TOPPING_PRODUCT_ID_CODE";
    public static final String DUPLICATE_TOPPING_PRODUCT_ID_VI = "Các sản phẩm bán kèm trong một nhóm không được trùng nhau";
    public static final String PRODUCT_LIST_INVALID_CODE = "PRODUCT_LIST_INVALID_CODE";
    public static final String PRODUCT_LIST_INVALID_VI = "Danh sách sản phẩm không hợp lệ";
    public static final String TOPPING_LIST_INVALID_CODE = "TOPPING_LIST_INVALID_CODE";
    public static final String TOPPING_LIST_INVALID_VI = "Danh sách sản phẩm bán kèm không hợp lệ";
    public static final String TOPPING_GROUP_LIST_INVALID_CODE = "TOPPING_GROUP_LIST_INVALID_CODE";
    public static final String TOPPING_GROUP_LIST_INVALID_VI = "Danh sách nhóm sản phẩm bán kèm không hợp lệ";
    public static final String PARENT_ID_NOT_NULL_CODE = "PARENT_ID_NOT_NULL_CODE";
    public static final String PARENT_ID_NOT_NULL_VI = "Là sản phẩm bán kèm thì ID sản phẩm chính không được để trống ";
    public static final String TOPPING_ITSELF_CODE = "TOPPING_ITSELF_CODE";
    public static final String TOPPING_ITSELF_VI = "Sản phẩm không thể bán kèm cùng chính nó";
    public static final String TOPPING_ITSELF_GROUP_CODE = "TOPPING_ITSELF_GROUP_CODE";
    public static final String TOPPING_ITSELF_GROUP_VI = "Sản phẩm không thể bán kèm cùng các nhóm bán kèm chứa chính nó";
    public static final String PRODUCT_TOPPING_IN_VALID = "PRODUCT_TOPPING_IN_VALID";
    public static final String PRODUCT_TOPPING_IN_VALID_VI = "Danh sách sản phẩm bán kèm không hợp lệ";
    public static final String PRODUCT_TOPPING_CANNOT_DELETE = "PRODUCT_TOPPING_CANNOT_DELETE";
    public static final String PRODUCT_TOPPING_CANNOT_DELETE_VI =
        "Không thể xóa sản phẩm [@@] khỏi nhóm bán kèm do còn đơn hàng đang xử lý";
    public static final String PRODUCT_TOPPING_CANNOT_UPDATE = "PRODUCT_TOPPING_CANNOT_UPDATE";
    public static final String PRODUCT_TOPPING_CANNOT_UPDATE_VI = "Không thể sửa sản phẩm [@@] do còn đơn hàng đang xử lý";
    public static final String TOPPING_GROUP_CANNOT_DELETE = "TOPPING_GROUP_CANNOT_DELETE";
    public static final String TOPPING_GROUP_CANNOT_DELETE_VI = "Không thể xóa nhóm bán kèm [@@] do còn đơn hàng đang xử lý";
    public static final String TOPPING_GROUP_CANNOT_UPDATE = "TOPPING_GROUP_CANNOT_UPDATE";
    public static final String TOPPING_GROUP_CANNOT_UPDATE_VI = "Không thể sửa tên nhóm bán kèm [@@] do còn đơn hàng đang xử lý";
    public static final String PRODUCT_IN_GROUP_CANNOT_DELETE = "PRODUCT_IN_GROUP_CANNOT_DELETE";
    public static final String PRODUCT_IN_GROUP_CANNOT_DELETE_VI =
        "Không thể hủy liên kết với nhóm bán kèm [@@] do còn đơn hàng đang xử lý";
    public static final String PRODUCT_IN_TOPPING_CANNOT_DELETE = "PRODUCT_IN_TOPPING_CANNOT_DELETE";
    public static final String PRODUCT_IN_TOPPING_CANNOT_DELETE_VI = "Không thể hủy liên kết với sản phẩm [@@] do còn đơn hàng đang xử lý";

    public static final String PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE = "PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE";
    public static final String PRODUCT_IN_TOPPING_GROUP_CANNOT_DELETE_VI =
        "[@@1] là sản phẩm bán cuối cùng thuộc nhóm bàn kèm [@@2], không thể bỏ";

    //topping end
    // tax_reduction_type
    public static final String NOT_TAX_REDUCTION = "NOT_TAX_REDUCTION";
    public static final String NOT_TAX_REDUCTION_VI = "Sản phẩm [@@] không được có giảm trừ thuế riêng";
    public static final String VAT_AMOUNT_NOT_VALID = "VAT_AMOUNT_NOT_VALID";
    public static final String VAT_AMOUNT_NOT_VALID_VI = "Sản phẩm [@@] phải có tổng tiền thuế lớn hơn 0";

    //end atx_reduction_type

    // voucher
    public static final String VOUCHER_TYPE_INVALID = "{\"code\": \"VOUCHER_TYPE_INVALID\",\"message\": \"Loại voucher không hợp lệ\"}";

    public static final String VOUCHER_APPLY_LIST_EMPTY =
        "{\"code\": \"VOUCHER_APPLY_LIST_EMPTY\",\"message\": \"Danh sách áp dụng trống\"}";
    public static final String VOUCHER_STATUS_NOT_NULL =
        "{\"code\": \"VOUCHER_STATUS_NOT_NULL\",\"message\": \"Trạng thái voucher không để trống\"}";
    public static final String VOUCHER_STATUS_INVALID =
        "{\"code\": \"VOUCHER_STATUS_INVALID\",\"message\": \"Trạng thái voucher không hợp lệ\"}";
    public static final String VOUCHER_ID_NOT_EMPTY =
        "{\"code\": \"VOUCHER_ID_NOT_EMPTY\",\"message\": \"Voucher áp dụng không để trống\"}";
    public static final String VOUCHER_ID_INVALID = "VOUCHER_ID_INVALID";
    public static final String VOUCHER_ID_INVALID_VI = "Voucher không hợp lệ";
    public static final String VOUCHER_LIST_APPLY_EMPTY = "VOUCHER_LIST_APPLY_INVALID";
    public static final String VOUCHER_LIST_APPLY_EMPTY_VI = "Danh sách áp dụng trống";
    public static final String VOUCHER_DELETE_BILL_INVALID = "VOUCHER_DELETE_INVALID";
    public static final String VOUCHER_DELETE_BILL_INVALID_VI = "Không thể xoá voucher khi có đơn hàng đang xử lý";
    public static final String VOUCHER_UPDATE_BILL_INVALID = "VOUCHER_UPDATE_BILL_INVALID";
    public static final String VOUCHER_UPDATE_BILL_INVALID_VI = "Không thể cập nhật voucher khi có đơn hàng đang xử lý";

    // end voucher

    //discount vat
    public static final String SPGC_EXIST_IN_GTGT_VI = "Hóa đơn giá trị gia tăng không được phép có sản phẩm ghi chú";
    public static final String SPGC_EXIST_IN_GTGT = "SPGC_EXIST_IN_GTGT";
    public static final String FEATURE_INVALID_VI = "Tính chất hàng hóa không hợp lệ";
    public static final String FEATURE_INVALID = "FEATURE_INVALID";

    // end discount vat

    // processing area
    public static final String QUANTITY_DELETE_LIST_INVALID_VI = "Số lượng hủy món không hợp lệ";
    public static final String QUANTITY_DELETE_LIST_INVALID = "QUANTITY_DELETE_LIST_INVALID";
    public static final String DELETE_LIST_INVALID_VI = "Không thể hủy món đã cung ứng";
    public static final String DELETE_LIST_INVALID = "DELETE_LIST_INVALID";
    public static final String PRODUCT_ID_NOT_NULL_VI = "ID sản phẩm không được để trống";
    public static final String PRODUCT_ID_NOT_NULL_CODE = "PRODUCT_ID_NOT_NULL";
    public static final String AREA_UNIT_ID_NOT_NULL_VI = "ID bàn không được để trống";
    public static final String AREA_UNIT_ID_NOT_NULL_CODE = "AREA_UNIT_ID_NOT_NULL_CODE";
    public static final String PROCESSING_PRODUCT_NOT_FOUND_VI = "Không tìm thấy món ăn";
    public static final String PROCESSING_PRODUCT_NOT_FOUND = "PROCESSING_PRODUCT_NOT_FOUND";
    public static final String PROCESSING_PRODUCT_QUANTITY_INVALID_VI = "Số lượng món ăn không hợp lệ";
    public static final String PROCESSING_PRODUCT_QUANTITY_INVALID = "PROCESSING_PRODUCT_QUANTITY_INVALID";
    public static final String REQUEST_DETAIL_ID_NOT_NULL_VI = "ID yêu cầu chế biến không được để trống";
    public static final String REQUEST_DETAIL_ID_NOT_NULL = "REQUEST_DETAIL_ID_NOT_NULL";
    public static final String PROCESSING_REQUEST_NOT_FOUND_VI = "Thông báo chế biến không tồn tại";
    public static final String PROCESSING_REQUEST_NOT_FOUND = "PROCESSING_REQUEST_NOT_FOUND";
    public static final String PROCESSING_AREA_NOT_FOUND_VI = "Không tìm thấy khu vực chế biến";
    public static final String PROCESSING_AREA_NOT_FOUND = "PROCESSING_AREA_NOT_FOUND";
    public static final String KITCHEN_TYPE_NOT_NULL =
        "{\"code\": \"KITCHEN_TYPE_NOT_NULL\",\"message\": \"Phân loại món ăn không được để trống\"}";
    public static final String KITCHEN_REQUEST_DETAIL_ID_NOT_NULL =
        "{\"code\": \"KITCHEN_REQUEST_DETAIL_ID_NOT_NULL\",\"message\": \"ID yêu cầu chế biến không được để trống\"}";
    public static final String KITCHEN_IS_TOPPING_NOT_NULL =
        "{\"code\": \"KITCHEN_IS_TOPPING_NOT_NULL\",\"message\": \"Lựa chọn có là bán kèm không được để trống\"}";
    public static final String KITCHEN_DONE_WITH_TOPPING_NOT_NULL =
        "{\"code\": \"KITCHEN_DONE_WITH_TOPPING_NOT_NULL\",\"message\": \"Lựa chọn có thao tác cùng sản phẩm bán kèm không được để trống\"}";
    public static final String KITCHEN_CHANGE_STATUS_TYPE_NOT_NULL =
        "{\"code\": \"KITCHEN_CHANGE_STATUS_TYPE_NOT_NULL\",\"message\": \"Loại chuyển trạng thái không được để trống\"}";
    public static final String KITCHEN_STATUS_NOT_NULL =
        "{\"code\": \"KITCHEN_STATUS_NOT_NULL\",\"message\": \"Trạng thái không được để trống\"}";
    public static final String KITCHEN_ID_NOT_NULL =
        "{\"code\": \"KITCHEN_ID_NOT_NULL\",\"message\": \"ID thông báo chế biến không được để trống\"}";

    // end

    // notification
    public static final String NOTIFICATION_NOT_FOUND_VI = "Không tìm thấy thông báo";
    public static final String NOTIFICATION_NOT_FOUND = "NOTIFICATION_NOT_FOUND";

    // end notification
    //     processing_area
    public static final String PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B_VI = "Không phải loại hình kinh doanh nhà hàng";
    public static final String PROCESSING_AREA_BUSINESS_TYPE_NOT_F_AND_B = "BUSINESS TYPE NOT F&B";
    public static final String COM_ID_NOT_VALID_VI = "ComId không hợp lệ";
    public static final String COM_ID_NOT_VALID = "COM ID NAME NOT VALID";
    public static final String PROCESSING_AREA_NAME_NOT_BLANK =
        "{\"code\": \"PROCESSING_AREA_NAME_NOT_BLANK\",\"message\": \"Tên khu vực không được để trống\"}";
    public static final String NAME_NOT_VALID_VI = "Tên không hợp lệ";
    public static final String DUPLICATE_NAME_VI = "Trùng tên khu vực";
    public static final String DUPLICATE_NAME = "DUPLICATE NAME";
    public static final String NAME_NOT_VALID = "NAME NOT VALID";
    public static final String PROCESSING_AREA_ACTIVE_NOT_VALID_VI = "active phải là 0 hoặc 1";
    public static final String PROCESSING_AREA_ACTIVE_NOT_VALID = "ACTIVE MUST 0 OR 1";
    public static final String PROCESSING_AREA_SETTING_NOT_VALID_VI = "setting phải là 0 hoặc 1 hoặc 2";
    public static final String PROCESSING_AREA_SETTING_NOT_VALID = "SETTING MUST 0 OR 1 OR 2";
    public static final String PROCESSING_AREA_NOT_EXIT_VI = "Không tồn tại khu vực chế biến này";
    public static final String PROCESSING_AREA_NOT_EXIT = "PROCESSING AREA NOT EXIT";
    public static final String PROCESSING_AREA_DELETE_VI = "Khu vực đã bị xoá";
    public static final String PROCESSING_AREA_DELETE = "PROCESSING AREA DELETE";
    // end processing_area

    // processing_area_product
    public static final String PROCESSING_AREA_PRODUCT_NOT_EXIT_VI = "Không tồn tại sản phẩm trong khu vực chế biến";
    public static final String PROCESSING_AREA_PRODUCT_NOT_EXIT = "PROCESSING AREA PRODUCT NOT EXIT";

    //    start printTemplate

    public static final String PRINT_TEMPLATE_CODE_DUPLICATE_VI = "Mã mẫu in đã tồn tại";
    public static final String PRINT_TEMPLATE_CODE_DUPLICATE = "PRINT_TEMPLATE_CODE_DUPLICATE";

    public static final String PRINT_TEMPLATE_CODE_NOT_NULL =
        "{\"code\": \"PRINT_TEMPLATE_CODE_NOT_NULL\",\"message\": \"Mã mẫu in không được để trống\"}";
    public static final String PRINT_TEMPLATE_PAGE_SIZE_NOT_NULL =
        "{\"code\": \"PRINT_TEMPLATE_PAGE_SIZE_NOT_NULL\",\"message\": \"Khổ in  không được để trống\"}";
    public static final String PRINT_PROCESSING_AREA_NOT_NULL =
        "{\"code\": \"PRINT_PROCESSING_AREA_NOT_NULL\",\"message\": \"Khu vực chế biến không được để trống\"}";

    public static final String PRINT_TEMPLATE_COM_ID_NOT_NULL =
        "{\"code\": \"PRINT_TEMPLATE_PAGE_SIZE_NOT_NULL\",\"message\": \"Công ty không hợp lệ\"}";

    public static final String PRINT_TEMPLATE_ID_NOT_NULL =
        "{\"code\": \"PRINT_TEMPLATE_PAGE_SIZE_NOT_NULL\",\"message\": \"ID không được để trống\"}";
    //    end printTemplate
    // print-setting
    public static final String PRINT_SETTING_NOT_FOUND_VI = "Không tìm thấy thông tin cấu hình máy in";
    public static final String PRINT_SETTING_NOT_FOUND = "PRINT_SETTING_NOT_FOUND";
    public static final String BILL_NOT_PROCESSING_AREA_VI = "Đơn hàng không có khu vực chế biến";
    public static final String BILL_NOT_PROCESSING_AREA = "BILL_NOT_PROCESSING_AREA";
    public static final String PRINT_SETTING_INVALID_VI = "Máy in ở khu vực [@@name] có nhiều hơn 1 khổ in";
    public static final String PRINT_SETTING_INVALID = "PRINT_SETTING_INVALID";
    public static final String PRINT_IP_ADDRESS_NOT_NULL_VI = "Địa chỉ IP không được để trống";
    public static final String PRINT_IP_ADDRESS_NOT_NULL = "PRINT_IP_ADDRESS_NOT_NULL";
    public static final String PRINT_IP_ADDRESS_DUPLICATE_VI = "Địa chỉ IP đã tồn tại";
    public static final String PRINT_IP_ADDRESS_DUPLICATE = "PRINT_IP_ADDRESS_DUPLICATE";
    public static final String DUPLICATE_PRINT_NAME_VI = "Tên máy in đã tồn tại";
    public static final String DUPLICATE_PRINT_NAME = "DUPLICATE_PRINT_NAME";
    public static final String PRINTER_NAME_NOT_NULL = "{\"code\": \"PRINTER_NAME_NOT_NULL\",\"message\": \"Tên máy in không để trống\"}";
    public static final String PRINTER_TYPE_NOT_NULL = "{\"code\": \"PRINTER_TYPE_NOT_NULL\",\"message\": \"Loại máy in không để trống\"}";
    public static final String PROCESSING_AREA_ID_NOT_NULL =
        "{\"code\": \"PROCESSING_AREA_ID_NOT_NULL\",\"message\": \"ID khu vực không để trống\"}";

    // end

    // login
    public static final String OWNER_PACKAGE_NOT_FOUND = "OWNER_PACKAGE_NOT_FOUND";
    public static final String OWNER_PACKAGE_NOT_FOUND_ID = "Không tìm thấy thông tin gói";
    public static final String TRIAL_EXPIRED = "TRIAL_EXPIRED";
    public static final String TRIAL_EXPIRED_VI =
        "Thời hạn dùng thử của phần mềm ĐÃ HẾT HẠN. Để tiếp tục sử dụng phần mềm, bạn vui lòng gia hạn để sử dụng phần mềm.";
    public static final String ACCOUNT_EXPIRED = "ACCOUNT_EXPIRED";
    public static final String ACCOUNT_EXPIRED_VI =
        "Tài khoản của bạn đã bị khóa hoặc hết hạn phần mềm. Để tiếp tục sử dụng phần mềm, bạn vui lòng thanh toán hoặc gia hạn để sử dụng phần mềm";

    // end login

    private ExceptionConstants() {}
}
