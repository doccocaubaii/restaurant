package vn.hust.restaurant.constants;

public interface ResultConstants {
    public String SUCCESS = "SUCCESS";
    public String ERROR = "ERROR";
    public String FAIL = "FAIL";
    public String REASON_CREATE_SUCCESS = "REASON_CREATE_SUCCESS";
    public String UPDATE_STATUS_SUCCESS = "UPDATE_STATUS_SUCCESS";
    public String FALSE = "FALSE";
    public String SUCCESS_GET_LIST = "Lấy dữ liệu thành công";
    public String SUCCESS_GET_DETAIL = "Lấy chi tiết thành công";
    public String UPDATE_ERROR = "Cập nhật thất bại";
    public String SEND_TASK_LOG_ERROR = "Gửi taskLog thất bại";
    public String SEND_TASK_LOG_NOT_FOUND = "Gửi taskLog thất bại";

    public String DEVICE_CODE_GET_LIST_ERROR_VI = "Không tìm thấy mã thiết bị";
    public String DEVICE_CODE_GET_LIST_ERROR = "DEVICE_CODE_GET_LIST_ERROR";

    public String DEVICE_CODE_ALREADY_VI = "Thiết bị đã được đăng ký";
    public String DEVICE_CODE_ALREADY = "DEVICE_CODE_ALREADY";
    public String DEVICE_CODE_REGISTER_SUCCESS_VI = "Đăng ký thiết bị thành công";
    public String LOGIN_SUCCESS_VI = "Đăng nhập thành công";
    public String LOGIN_ERROR_VI = "Đăng nhập thất bại";
    public String LOGIN_ERROR = "LOGIN_ERROR";

    public String SUCCESS_SEND_DETAIL = "Gửi dữ liệu thành công";

    public String CREATE_SUCCESS_VI = "Thêm mới thành công!";
    public String CREATE_CONFIG_SUCCESS_VI = "Thêm mới cấu hình thành công!";
    public String UPDATE_CONFIG_SUCCESS_VI = "Cập nhật cấu hình thành công!";
    public String CREATE_CONFIG_EI_SUCCESS_VI = "Thêm mới cấu hình thành công!";
    public String GET_PRODUCT_GROUPS_SUCCESS_VI = "Lấy thông tin các danh mục thành công";
    public String GET_PRODUCT_GROUP_DETAIL_SUCCESS_VI = "Lấy chi tiết danh mục thành công";
    public String CREATE_PRODUCT_GROUP_SUCCESS_VI = "Thêm danh mục thành công";
    public String CREATE_PRODUCT_GROUP_FAIL_VI = "Danh sách trống, thêm danh mục thất bại";
    public String UPDATE_PRODUCT_GROUP_SUCCESS_VI = "Cập nhật thông tin danh mục thành công";
    public String DELETE_PRODUCT_GROUP_SUCCESS_VI = "Xóa danh mục thành công";
    public String ADD_PRODUCT_TO_PRODUCT_GROUP_SUCCESS_VI = "Thêm sản phẩm vào danh mục thành công";

    public String GET_PRODUCTS_SUCCESS_VI = "Lấy thông tin các sản phẩm thành công";
    public String DELETE_PRODUCT_SUCCESS_VI = "Xóa sản phẩm thành công";
    public String CREATE_PRODUCT_SUCCESS_VI = "Thêm sản phẩm thành công";
    public String CREATE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI = "Thêm đơn vị tính chuyển đổi thành công";
    public String UPDATE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI = "Cập nhật đơn vị tính chuyển đổi thành công";
    public String DELETE_PRODUCT_CONVERSION_UNIT_SUCCESS_VI = "Xóa đơn vị tính chuyển đổi thành công";
    public String GET_PRODUCT_CONVERSION_UNIT_SUCCESS_VI = "Lấy thông tin đơn vị tính chuyển đổi thành công";
    public String UPDATE_PRODUCT_SUCCESS_VI = "Cập nhật thông tin sản phẩm thành công";
    public String GET_PRODUCT_DETAIL_SUCCESS_VI = "Lấy chi tiết sản phẩm thành công";
    public String GET_ALL_PRODUCT_UNIT_SUCCESS = "Lấy danh sách đơn vị sản phẩm thành công";
    public String CREATE_PRODUCT_UNIT_SUCCESS = "Tạo mới đơn vị tính thành công";
    public String PRODUCT_UNIT_NOT_FOUND = "Không tìm thấy đơn vị tính";

    public String GET_EMPLOYEE_DETAIL_SUCCESS_VI = "Lấy chi tiết nhân viên thành công";
    public String GET_EMPLOYEES_SUCCESS_VI = "Lấy danh sách nhân viên thành công";

    public String SUCCESS_CHANGE_PASSWORD = "Thay đổi mật khẩu thành công!";
    public String UPDATE_EMPLOYEE_SUCCESS_VI = "Cập nhật thông tin nhân viên thành công";
    public String DELETE_EMPLOYEE_SUCCESS_VI = "Xóa nhân viên thành công";
    //    Invoice
    public String UPDATE_INVOICE_SUCCESS = "UPDATE_INVOICE_SUCCESS";
    public String UPDATE_INVOICE_SUCCESS_VI = "Cập nhật hóa đơn thành công";
    public String DELETE_INVOICE_SUCCESS_VI = "Xóa hóa đơn thành công";
    //    end-Invoice
    // Customer
    public static final String CUSTOMER_GET_ALL_SUCCESS_CODE_VI = "Lấy danh sách khách hàng thành công";
    public static final String CUSTOMER_CREATE_SUCCESS_CODE_VI = "Thêm mới khách hàng thành công";
    public static final String CUSTOMER_CREATE_FAIL_CODE_VI = "Thêm mới khách thất bại";
    public static final String CUSTOMER_UPDATE_SUCCESS_CODE_VI = "Cập nhật khách hàng thành công";
    public static final String CUSTOMER_DELETE_SUCCESS_CODE_VI = "Xoá khách hàng thành công";
    public static final String CUSTOMER_DETAIL_SUCCESS_CODE_VI = "Lấy thông tin khách hàng thành công";

    public static final String CUSTOMER_NOT_FOUND_CODE_VI = "Không tìm thấy khách hàng";
    public static final String CUSTOMER_INVALID_ID_AND_COMPANY_ID_CODE_VI = "Id khách hàng và Id công ty không hợp lệ";
    // end customer

    public String CREATE_EMPLOYEE_SUCCESS_VI = "Tạo nhân viên thành công";

    public String GET_ROLES_SUCCESS_VI = "Lấy danh sách vai trò thành công";
    public String SUCCESS_SEQ_CREATE = "Tạo mới SEQ thành công!";
    public String SUCCESS_REGISTER_CREATE = "Đăng ký tài khoản thành công!";
    public String SUCCESS_BILL_STATUS = "Cập nhật trạng thái thành công!";
    public String CREATE_BILL_SUCCESS = "Tạo mới đơn hàng thành công!";
    public String UPDATE_BILL_SUCCESS = "Cập nhật đơn hàng thành công!";
    //    bill
    public String ERROR_PAYMENT_METHOD = "ERROR_PAYMENT_METHOD";
    public String ERROR_PAYMENT_METHOD_VI = "Hình thức thanh toán không được bỏ trống";
    public String SUCCESS_DEVICE_CODE = "Lấy mã thiết bị thành công";
    public String SUCCESS_GET_COMPANY = "Lấy thông tin công ty thành công!";
    public String SUCCESS_REGISTER_INVOICE = "Đăng ký hóa đơn điện tử thành công!";
    public String SUCCESS_REGISTER_SEARCH = "Lấy dữ liệu thành công";
    public String SUCCESS_CONFIG_STORE = "Lấy thông tin cấu hình cửa hàng thành công";
    public String CONFIG_STORE_EMPTY = "Thông tin cấu hình cửa hàng trống";
    public String CONFIG_STORE_AVAILABLE = "Đã có thông tin cấu hình cửa hàng";
    public String SUCCESS_CONFIG_INVOICE = "Lấy thông tin cấu hình hóa đơn thành công";
    public String SUCCESS_CREATE = "Tạo mới thành công!";
    public String SUCCESS_UPDATE = "Cập nhật thành công!";
    public String SUCCESS_LOAD_DATA = "lấy dữ liệu thành công!";

    //    bill
    public String ERROR_CREATE_ID = "ERROR_CREATE_ID";
    public String ERROR_LOAD_DATA = "ERROR_CREATE_ID";
    public String ERROR_LOAD_DATA_VI = "Không tìm thấy dữ liệu";
    public String ERROR_CREATE_ID_VI = "Tạo mới không được truyền Id";
    public String ERROR_UPDATE_ID = "ERROR_UPDATE_ID";
    public String ERROR_UPDATE_ID_VI = "Không tìm thấy Id";
    public String ERROR_DEVICE_CODE = "ERROR_DEVICE_CODE";
    public String ERROR_GET_COMPANY = "ERROR_GET_COMPANY";
    public String ERROR_GET_COMPANY_VI = "Lấy thông tin compay thất bại";
    public String ERROR_DEVICE_CODE_VI = "Không tìm thấy thông tin thiết bị";
    public String ERROR_SEQ_CREATE = "Tạo mới SEQ thất bại";
    public String ERROR_REGISTER_ALREADY = "ERROR_REGISTER_ALREADY";
    public String ERROR_REGISTER_ALREADY_VI = "Tài khoản đã tồn tại!";
    public String ERROR_REGISTER_PHONE_NUMBER = "ERROR_REGISTER_PHONE_NUMBER";
    public String ERROR_REGISTER_PHONE_NUMBER_VI = "Số điện thoại không hợp lệ!";
    public String GET_ALL_INVOICES_SUCCESS = "Lấy danh sách hóa đơn thành công";
    public String GET_INVOICE_DETAIL_SUCCESS = "Lấy chi tiết hóa đơn thành công";

    // area
    public static final String AREA_GET_ALL_SUCCESS_VI = "Lấy danh sách khu vực thành công";
    public static final String AREA_GET_ALL_DETAIL_SUCCESS_VI = "Lấy chi tiết khu vực, bàn thành công";
    public static final String AREA_GET_DETAIL_SUCCESS_VI = "Lấy chi tiết khu vực thành công";
    public static final String AREA_CREATE_SUCCESS_VI = "Tạo khu vực thành công";
    public static final String AREA_UPDATE_SUCCESS_VI = "Cập nhật khu vực thành công";
    public static final String AREA_DELETE_SUCCESS_VI = "Xoá khu vực thành công";

    public static final String AREA_NOT_EXISTS_VI = "Khu vực này không tồn tại";
    public static final String AREA_DUPLICATE_VI = "Tên khu vực đã tồn tại";
    public static final String AREA_NAME_ERROR_VI = "Tên khu vực không hợp lệ";
    public static final String AREA_DELETE_FAIL_VI_01 = "Bạn không thể xóa khu vực cuối cùng trong cửa hàng";
    public static final String AREA_DELETE_FAIL_VI_02 = "Bạn không thể xóa khu vực này vì còn bàn đang phục vụ";
    // end area

    //areaUnit
    public static final String AREA_UNIT_CREATE_SUCCESS_VI = "Tạo bàn thành công";
    public static final String AREA_UNIT_UPDATE_SUCCESS_VI = "Cập nhật bàn thành công";
    public static final String AREA_UNIT_DELETE_SUCCESS_VI = "Xoá bàn thành công";
    public static final String AREA_UNIT_GET_DETAIL_SUCCESS_VI = "Lấy thông tin chi tiết bàn thành công";

    public static final String AREA_UNIT_DUPLICATE_NAME_VI = "Tên bàn đã trùng tại khu vực này";
    public static final String AREA_UNIT_NAME_ERROR_VI = "Tên bàn không hợp lệ";
    public static final String AREA_UNIT_NOT_EXISTS_VI = "Bàn này không tồn tại";
    public static final String AREA_UNIT_UPDATE_FAIL_VI = "Bàn đang phục vụ, không thể chỉnh sửa";
    public static final String AREA_UNIT_DELETE_FAIL_VI = "Bàn đang phục vụ, không thể xoá";
    //end areaUnit

    // reservation
    public static final String RESERVATION_CREATE_SUCCESS_VI = "Đặt chỗ thành công";
    public static final String RESERVATION_UPDATE_SUCCESS_VI = "Cập nhật trạng thái chỗ thành công";
    public static final String RESERVATION_GET_ALL_SUCCESS_VI = "Lấy danh sách đặt chỗ thành công";
    public static final String RESERVATION_GET_DETAIL_SUCCESS_VI = "Lấy thông tin đặt chỗ thành công";
    public final String RESERVATION_DELETE_SUCCESS_VI = "Xoá thông tin đặt chỗ thành công";
    // end reservation

    // Customer

    public static final String CUSTOMER_INVALID_CODE2_VI = "Trùng mã khách hàng";
    // end customer

    // Repository
    public static final String REPOSITORY_INITIALIZE_SUCCESS = "Khởi tạo kho hàng thành công";
    public static final String REPOSITORY_GET_ALL_SUCCESS = "Lấy danh sách tồn kho thành công";
    public static final String REPOSITORY_GET_COMMON_STATUS_SUCCESS = "Lấy chỉ số về tồn kho thành công";
    public static final String REPOSITORY_LEDGER_GET_ALL_SUCCESS = "Lấy danh sổ kho thành công";
    public static final String REPOSITORY_LEDGER_GET_COMMON_STATUS_SUCCESS = "Lấy chỉ số về sổ kho thành công";

    public static final String REPOSITORY_PRODUCT_NOT_EXISTS = "Sản phẩm không tồn tại";
    public static final String REPOSITORY_GET_ALL_FAIL = "Danh sách kho hàng trống";
    public static final String REPOSITORY_GET_COMMON_STATUS_FAIL = "Chỉ số về tồn kho trống";
    // end repository

    // RsInoutWard
    public static final String RS_IN_WARD_DETAIL_CREATE_SUCCESS = "Lưu chi tiết điều chỉnh kho hàng thành công";
    public static final String RS_IN_WARD_CREATE_SUCCESS = "Điều chỉnh tăng kho hàng thành công";
    public static final String RS_OUT_WARD_CREATE_SUCCESS = "Điều chỉnh giảm kho hàng thành công";
    public static final String RS_IN_OUT_WARD_CREATE_SUCCESS = "Tạo mới chứng từ nhập/xuất kho thành công";
    public static final String RS_IN_OUT_WARD_CREATE_FAIL = "Tạo mới chứng từ nhập/xuất kho thất bại";
    public static final String RS_OUT_WARD_DELETE_SUCCESS = "Xóa đơn nhập/xuất hàng thành công";
    public static final String RS_INOUT_WARD_DETAIL_SUCCESS = "Lấy đơn nhập/xuất hàng thành công";
    public static final String RS_IN_WARD_TRANSACTION_SUCCESS = "Lấy danh sách giao dịch nhập kho thành công";
    public static final String RS_OUT_WARD_TRANSACTION_SUCCESS = "Lấy danh sách giao dịch xuất kho thành công";
    public static final String RS_INOUT_WARD_TRANSACTION_SUCCESS = "Lấy danh sách giao dịch nhập/xuất kho thành công";

    public String CREATE_INVOICE_NOT_FOUND = "CREATE_INVOICE_NOT_FOUND";
    public String CREATE_INVOICE_NOT_FOUND_VI = "Không tìm thấy hóa đơn!";
    public String ERROR_REGISTER_PHONE_NUMBER_CODE = "ERROR_REGISTER_PHONE_NUMBER_CODE";
    public String ERROR_CONFIG = "ERROR_CONFIG";
    public String ERROR_CONFIG_VI = "Không tìm thấy thông tin cấu hình!";

    // report, statistic
    public String GET_REVENUE_PROFIT_STATS = "Lấy chỉ số thống kê cơ bản về doanh thu, lợi nhuận thành công";
    public String GET_BILL_STATS = "Lấy các chỉ số thống kê cơ bản về đơn hàng thành công";
    public String GET_INVOICE_STATS = "Lấy các chỉ số thống kê cơ bản về hóa đơn điện tử thành công";
    // end report, statistic

    // reservation
    public final String RESERVATION_CREATE_SUCCESS_CODE = "RESERVATION_CREATE_SUCCESS_CODE";
    public final String RESERVATION_UPDATE_SUCCESS_CODE = "RESERVATION_UPDATE_SUCCESS_CODE";
    public final String RESERVATION_GET_ALL_SUCCESS_CODE = "RESERVATION_GET_ALL_SUCCESS_CODE";
    public final String RESERVATION_GET_DETAIL_SUCCESS_CODE = "RESERVATION_GET_DETAIL_SUCCESS_CODE";
    public final String RESERVATION_ORDER_DATE_NOT_EXISTS_CODE = "RESERVATION_ORDER_DATE_NOT_EXISTS_CODE";
    public final String RESERVATION_NOT_EXISTS_CODE = "RESERVATION_NOT_EXISTS_CODE";
    public final String RESERVATION_STATUS_NOT_EXISTS_CODE = "RESERVATION_STATUS_NOT_EXISTS_CODE";
    // end reservation

    // taskLog
    public final String TASK_LOG_CREATE_SUCCESS_VI = "Tạo mới nhật ký nhiệm vụ thành công";
    public final String TASK_LOG_UPDATE_SUCCESS_VI = "Cập nhật nhật ký nhiệm vụ thành công";
    public final String TASK_LOG_DELETE_SUCCESS_VI = "Xoá nhật ký nhiệm vụ thành công";
    // end taskLog

    // Customer
    public final String CUSTOMER_GET_ALL_SUCCESS_VI = "Lấy danh sách khách hàng thành công";
    public final String CUSTOMER_UPDATE_SUCCESS_VI = "Cập nhật khách hàng thành công";
    public final String CUSTOMER_DELETE_SUCCESS_VI = "Xoá khách hàng thành công";
    public final String CUSTOMER_DETAIL_SUCCESS_VI = "Lấy thông tin khách hàng thành công";
    public final String CUSTOMER_NOT_FOUND_VI = "Không tìm thấy khách hàng";
    public final String CUSTOMER_NOT_FOUND_CODE = "CUSTOMER_NOT_FOUND_CODE";
    // end customer

    // package
    public final String PACKAGE_SAVE_SUCCESS_VI = "Lưu thông tin gói thành công";
    // end package

    // inventory common stats v2
    public final String INVENTORY_COMMON_STATS_V2_SUCCESS_VI = "Lấy chỉ số thống kê tồn kho cơ bản v2 thành công";
    // end
    // product
    public String PRODUCT_IMPORT_EXCEL_ERROR = "Danh sách sản phẩm không hợp lệ";
    // end product

    // Company owner
    public final String COMPANY_OWNER_CREATE_SUCCESS_VI = "Tạo mới công ty thành công";
    public final String COMPANY_OWNER_GET_SUCCESS_VI = "Lấy thông tin công ty thành công";
    public final String COMPANY_OWNER_DELETE_SUCCESS_VI = "Xóa thông tin công ty thành công";
    public final String COMPANY_OWNER_SAVE_SUCCESS_VI = "Lưu thông tin công ty thành công";
    // end company owner

    // config
    public String CONFIG_GET_ALL_SUCCESS_VI = "Lấy danh sách cấu hình thành công";
    public String CONFIG_GET_DETAIL_SUCCESS_VI = "Lấy thông tin cấu hình thành công";
    public String CONFIG_SAVE_SUCCESS_VI = "Lưu thông tin cấu hình thành công";
    public String CONFIG_SAVE_FAIL_VI = "Lưu thông tin cấu hình thất bại";
    public String CONFIG_DELETE_SUCCESS_VI = "Xóa thông tin cấu hình thành công";
    // end config

    // owner device
    public String OWNER_DEVICE_GET_ALL_SUCCESS_VI = "Lấy danh sách thiết bị thành công";
    // end owner device

    // user
    public String USER_GET_ALL_SUCCESS_VI = "Lấy danh sách người dùng thành công";
    // end user

    // business
    public String BUSINESS_GET_ALL_SUCCESS_VI = "Lấy danh sách loại hình kinh doanh thành công";
    // end business

    public final String SUCCESS_GET_BILL_REVENUE = "Lấy thông tin doanh thu thành công";
}
