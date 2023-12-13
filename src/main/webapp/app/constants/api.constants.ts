// Đánh Constants theo tên chức năng hoặc api cụ thể

// Account
export const CHANGE_PASSWORD = '/client/user/change-password';
// end account

export const LOGIN = '/client/common/authenticate';
export const CHANGE_SESSION = '/client/common/change-session';
export const REGISTER = '/client/common/register';
export const ACCOUNT = '/client/common/account';
export const CREATE_USER = '/user/create';
export const PING = '/p/client/ping';
// Customer
export const GET_LIST_CUSTOMER = '/client/page/customer/get-all-with-paging';
export const CREATE_CUSTOMER = '/client/page/customer/create';
export const UPDATE_CUSTOMER = '/client/page/customer/update';
export const DELETE_CUSTOMER = '/client/page/customer/delete';

export const DELETE_MULTI_CUSTOMER = '/client/page/customer/delete-list';
export const EXPORT_CUSTOMER = '/client/page/customer/export-excel/export-excel';

export const GET_TAX_INFO = '/p/client/page/customer/by-taxCode/get-detail';
export const CUSTOMER_CARD_SAVE_POINT = '/client/page/card/save-point';
export const CUSTOMER_CARD_HISTORY_GET_ALL = '/client/page/card/history/get-all';
export const CUSTOMER_CARD_GET_DEFAULT = '/client/page/card/get-default';

export const CUSTOMER_BILL_HISTORY_RECEIVABLE_GET_ALL = '/client/page/bill/history-receivable/get-all';
// End Customer

// voucher
export const GET_VOUCHER_FOR_CUSTOMER = '/client/page/voucher/get-for-bill';
export const GET_LIST_VOUCHER = '/client/page/voucher/get-with-paging';
export const CHECK_VOUCHER_VALID = '/client/page/voucher/check-valid';
export const GET_LIST_PRODUCT_BY_PRODUCT_IDS = '/client/page/product-unit/get-by-ids';
export const GET_LIST_PRODUCT_BY_PRODUCT_GROUP_ID = '/client/page/product-group/get-by-ids';

export const GET_VOUCHER_FOR_CUSTOMER_WEB = '/client/page/voucher/get-for-bill';
export const GET_APPLY_VOUCHER = '/client/page/voucher/apply/detail';
export const CREATE_VOUCHER = '/client/page/voucher/create';
export const UPDATE_VOUCHER = '/client/page/voucher/update';
export const DELETE_VOUCHER = '/client/page/voucher/delete';

export const APPLY_VOUCHER = '/client/page/voucher/apply';
export const APPLY_ALL_VOUCHER = '/client/page/voucher/apply-all';
export const GET_VOUCHER_USAGE_DETAIL = '/client/page/voucher/usage/detail';
// End Voucher
// voucher

// product
export const GET_LIST_PRODUCT = '/client/page/product/get-with-paging';
export const GET_LIST_PRODUCT2 = '/client/page/product/get-with-paging2';
export const GET_LIST_PRODUCT_UNIT = '/client/page/product/get-all-product-unit';
export const GET_PRODUCT_BY_BARCODE = '/client/page/product/find-by-barcode/';
export const GET_ALL_CATEGORY_OFFLINE = '/client/page/product-group/get-all-for-offline';
export const GET_PRODUCT_UNIT = '/client/page/product/get-all-product-unit';
export const GET_PRODUCT_BY_ID = '/client/page/product/by-id/';
export const UPDATE_PRODUCT = '/client/page/product/update1';
export const CREATE_PRODUCT = '/client/page/product/create1';
export const DELETE_PRODUCT = '/client/page/product/delete/';
export const CREATE_UNIT = '/client/page/product/create-product-unit';
export const CREATE_CATEGORY = '/client/page/product-group/create';
export const UPDATE_CATEGORY = '/client/page/product-group/update';
export const DELETE_CATEGORY = '/client/page/product-group/delete';
export const GET_ALL_PRODUCT_OFFLINE = '/client/page/product-group/get-all-for-offline';
export const GET_ALL_PRODUCT = '/client/page/product/category-get-with-offline';
export const DELETE_PRODUCT_CONVERSION_UNIT = '/client/page/product/delete-product-conversion-unit';
export const GET_PRODUCT_PAGING = '/client/page/product/category-get-with-paging';
export const DELETE_PRODUCT_BY_ID = '/client/page/product/delete/';
export const DELETE_MULTI_PRODUCT = '/client/page/product/delete-list';
export const GET_CATEGORY_PAGING = '/client/page/product-group/get-with-paging';
export const GET_TOPPING_PAGING = '/client/page/topping-group/get-with-paging';
export const GET_TOPPING_GROUP_BY_ID = '/client/page/topping-group/by-id/';
export const GET_PRODUCT_TOPPING = '/client/page/product/get-all-product-topping';
export const UPDATE_TOPPING_GROUP = '/client/page/topping-group/update';
export const CREATE_TOPPING_GROUP = '/client/page/topping-group/create';
export const DELETE_TOPPING_GROUP = '/client/page/topping-group/delete/';
export const GET_COMPANY_CONFIGS = '/client/page/config/get-company-configs/';
export const DELETE_MULTI_CATEGORY = '/client/page/product-group/delete-list';
export const GET_PRODUCT_TOPPING_BY_ID = '/client/page/product/get-product-topping/';
export const UPDATE_UNIT = '/client/page/product-unit/update';
export const DELETE_UNIT = '/client/page/product-unit/delete/';
export const DELETE_MULTI_UNIT = '/client/page/product-unit/delete-list';
export const GET_UNIT_PAGING = '/client/page/product-unit/get-all';
export const GET_WITH_PAGING_BARCODE = '/client/page/product/bar-code/get-with-paging';
export const EXPORT_PRODUCT = '/client/page/product/export-excel/export-excel';
export const GET_PROCESSING_AREA = '/client/page/processing-area/get-with-paging';
export const GET_PRODUCT_NAME_BY_IDS = '/client/page/product-unit/get-by-ids';

// End product

// area
export const GET_AREA_UNIT_BY_ID = '/client/page/area-unit/by-id/';
export const GET_ALL_LIST_AREA = '/client/page/area/get-all-with-paging';
export const GET_AREA_BY_ID = '/client/page/area/by-id/';
export const CREATE_AREA = '/client/page/area/create';
export const UPDATE_AREA = '/client/page/area/update';
export const DELETE_AREA = '/client/page/area/delete/';
// End area

//area-unit
export const CREATE_AREA_UNIT = '/client/page/area-unit/create';
export const UPDATE_AREA_UNIT = '/client/page/area-unit/update';
export const DELETE_AREA_UNIT = '/client/page/area-unit/delete';
// End

// Invoice
export const LOGIN_INVOICE = '/client/common/config/login/easy-invoice';
export const UPDATE_INFO_INVOICE_CONFIG = '/client/page/config/update-easy-invoice-config';
export const GET_OWNER_INFO = '/client/common/get-owner-info/';
export const GET_COMPANY_CONFIG = '/client/page/config/get-company-configs/';
export const UPDATE_INVOICE_CONFIG = '/client/page/config/update-invoice-config';
export const UPDATE_SALE_INVOICE_CONFIG = '/client/page/config/update-sale-config';
export const GET_INVOICE_WITH_PAGING = '/client/page/invoice/get-with-paging';
export const PUBLISH_INVOICE_LIST = '/client/page/invoice/publish-list';
export const FIND_INVOICE_BY_ID = '/client/page/invoice/by-id/';
export const PUBLISH_INVOICE = '/client/page/invoice/publish';
export const DELETE_INVOICE = '/client/page/invoice/delete/';
export const VIEW_INVOICE_PDF = '/client/page/invoice/view-pdf';
export const SEND_MAIL_INVOICE = '/client/page/invoice/send-mail';
export const MODIFY_INVOICE = '/client/page/invoice/modify/';
export const SYNC_INVOICE = '/client/page/invoice/sync/get-data';
export const SYNC_INVOICE_SAVE = '/client/page/invoice/sync/save-data';
// End Invoice
// print config
export const GET_ALL_PRINT_CONFig = '/client/page/config/get-print-config/';
export const UPDATE_PRINT_CONFIG = '/client/page/config/update-print-config';
export const UPDATE_LIST_PRINT_CONFIG = '/client/page/config/update-list-print-config';
export const GET_DISPLAY_CONFIG = '/client/page/config/get-display_config';
export const UPDATE_DISPLAY_CONFIG = '/client/page/config/update-display_config';
// end print-config

// Device

// Warehouse
export const GET_ALL_TRANSACTIONS = '/page/rs-inoutward/get-all-transactions';
export const GET_TRANSACTION_DETAIL = '/page/rs-inoutward/by-id/';
export const CREATE_INOUTWARD = '/page/rs-inoutward/create';
export const UPDATE_INOUTWARD = '/page/rs-inoutward/update';
export const DELETE_INOUTWARD = '/page/rs-inoutward/delete';
export const GET_ALL_CUSTOMER = '/client/page/customer/get-all-for-offline';
export const GET_ALL_CUSTOMER_PAGING = '/client/page/customer/get-all-with-paging';
export const GET_PRODUCT = '/client/page/product/get-with-paging';
// End warehouse
export const GET_DEVICE_CODE = '/client/common/get-device-code/by-name';
export const REGISTER_DEVICE = '/client/common/register-owner-device';
// EndDevice

// product-group
export const GET_ALL_PRODUCT_GROUP_OFFLINE = '/client/page/product-group/get-all-for-offline';
export const GET_LIST_PRODUCT_GROUP = '/client/page/product-group/get-with-paging';
export const GET_PRODUCT_GROUP_BY_ID = '/client/page/product-group/by-id/';
export const CREATE_PRODUCT_GROUP = '/client/page/product-group/create';
export const UPDATE_PRODUCT_GROUP = '/client/page/product-group/update';
export const DELETE_PRODUCT_GROUP = '/client/page/product-group/delete';
export const GET_PRODUCT_BY_PRODUCT_ID = '/client/page/product-group/by-product-id/';
export const GET_PRODUCT_BY_PRODUCT_GROUP_ID = '/client/page/product-group/get-by-ids';
// End product-group

// bill
export const GET_LIST_BILL = '/client/page/bill/get-with-paging';
export const GET_BILL_BY_ID = '/client/page/bill/by-id/';
export const GET_BILL_BY_CODE = '/client/page/bill/by-code/';
export const CREATE_BILL = '/client/page/bill/create';
export const UPDATE_BILL = '/client/page/bill/update';
export const CHECKOUT_BILL_BY_ID = '/client/page/bill/done-by-id';
export const CANCEL_BILL_BY_ID = '/client/page/bill/cancel-by-id';
export const PAY_OFF_DEBT_BILL = '/client/page/bill/pay-off-debt';
export const SYNC_BILL = '/client/page/offline-bill/sync';
export const BILL_CANCEL = '/client/page/bill/complete/cancel-by-id';
export const GET_DATA_PRINT_TEMPALTE = '/client/common/print-template/get-data';
export const RETURN_BILL = '/client/page/bill/complete/return-by-id';
// End bill

// area-unit
// End area-unit

// customer
export const GET_ALL_CUSTOMER_OFFLINE = '/client/page/customer/get-all-for-offline';
export const GET_LIST_CUSTOMER_UNIT = '/client/page/customer/get-all-customer-unit';
export const GET_CUSTOMER_BY_ID = '/client/page/customer/by-id/';
export const SAVE_POINT = '/client/page/card/save-point/';
// End customer

// report
export const REVENUE_COMMON_STATUS = '/client/page/home/revenue-common-stats';
export const BILL_COMMON_STATUS = '/client/page/home/bill-common-stats';
export const INVOICE_COMMON_STATUS = '/client/page/home/invoice-common-stats';
export const REVENUE_COST_PRICE_BY_PRODUCT = '/client/page/report/revenue-cost-price-by-product';
export const GENERAL_INPUT_OUTPUT_INVENTORY = '/client/page/report/general-input-output-inventory';
export const INVENTORY_COMMON_STATUS = '/client/page/report/inventory-common-stats';

export const RECENT_ACTIVITES = '/client/page/report/recent-activites';
export const INVENTORY_COMMON_STATUS_V2 = '/client/page/report/inventory-common-stats-v2';
export const PRODUCT_PROFIT_STATUS = '/client/page/report/product-profit-stats';
export const PRODUCT_SALE_STATUS = '/client/page/report/product-sales-stats';
export const PRODUCT_HOT_SALES_STATS = '/client/page/report/product-hot-sales-stats';
export const ACTIVITY_HISTORY_STATS = '/api/client/page/report/activity-history-stats';
// End report

//Staff
export const GET_STAFF_WITH_PAGING = '/client/page/employee/get-with-paging';
export const GET_ALL_ROLES = '/client/page/role/get-with-paging';
export const CREATE_STAFF = '/client/page/employee/create';
export const UPDATE_STAFF = '/client/page/employee/update';
export const DELETE_STAFF = '/client/page/employee/delete';
export const FIND_STAFF = '/client/page/employee/by-id/';
export const DELETE_MULTI_STAFF = '/client/page/employee/delete-list';

//staff-end

// receipt-payment
export const GET_LIST_RECEIPT_PAYMENT = '/page/receipt-payment/get-all-transactions';
export const GET_LIST_BUSSINESS_TYPE = '/page/business-type/get-all-transactions';
export const GET_RECEIPT_PAYMENT_BY_ID = '/page/receipt-payment/by-id/';
export const CREATE_RECEIPT_PAYMENT = '/page/receipt-payment/create';
export const UPDATE_RECEIPT_PAYMENT = '/page/receipt-payment/update';
export const DELETE_RECEIPT_PAYMENT = '/page/receipt-payment/delete';
export const DELETE_LIST_RECEIPT_PAYMENT = '/page/receipt-payment/delete-list';
export const CREATE_BUSINESS_TYPE = '/page/business-type/create';
// End receipt-payment
// End warehouse
// import
export const IMPORT_EXCEL_VALIDATE_PRODUCT = '/client/page/product/import-excel/validate';
export const IMPORT_EXCEL_SAVE_PRODUCT = '/client/page/product/import-excel/save-data';

export const IMPORT_EXCEL_VALIDATE_PRODUCT_GROUP = '/client/page/product-group/import-excel/validate';
export const IMPORT_EXCEL_SAVE_PRODUCT_GROUP = '/client/page/product-group/import-excel/save-data';

export const IMPORT_EXCEL_VALIDATE_CUSTOMER = '/client/page/customer/import-excel/validate';
export const IMPORT_EXCEL_SAVE_CUSTOMER = '/client/page/customer/import-excel/save-data';
// end import

// export
export const EXPORT_EXCEL_CUSTOMER = '/client/page/customer/export-excel/error-data';
export const EXPORT_EXCEL_CUSTOMER_ALL = '/client/page/customer/export-excel/export-excel';
export const EXPORT_EXCEL_PRODUCT = '/client/page/product/export-excel/error-data';
export const EXPORT_EXCEL_PRODUCT_GROUP = '/client/page/product-group/export-excel/error-data';
// end

// card
export const GET_LIST_CARD = '/client/page/card/get-all';
export const GET_CARD_BY_ID = '/client/page/card/by-id/';
export const CREATE_CARD = '/client/page/card/create';
export const UPDATE_CARD = '/client/page/card/update';
export const SORT_CARD = '/client/page/card/sort';
export const DELETE_CARD = '/client/page/card/delete';
export const DELETE_LIST_CARD = '/client/page/card/delete-list';
//end card

// company
export const GET_COMPANY_WITH_PAGING = '/page/company/get-with-paging';
export const GET_COMPANY_OWNER = '/page/company/get-company-owner/{userId}';
export const CREATE_COMPANY = '/page/company/create';
export const UPDATE_COMPANY = '/page/company/update';
// end company

// card-policy
export const CARD_POLICY_GET_ALL = '/client/page/card-policy/get-all';
export const CARD_POLICY_CREATE = '/client/page/card-policy/create';
export const CARD_POLICY_UPDATE = '/client/page/card-policy/update';
// end
// notify
export const GET_VERSION_UPDATE_TODAY = '/client/page/version-update/get-today';
// End notify

// report
export const GET_REPORT_PRODUCT_HOT_SALES = '/client/page/report/product-hot-sales-stats';
export const GET_EXCEL_PRODUCT_HOT_SALES = '/client/page/report-excel/product-hot-sales-stats';
export const GET_PDF_PRODUCT_HOT_SALES = '/client/page/report-pdf/product-hot-sales-stats';

// report product profit
export const REPORT_PRODUCT_PROFIT = '/client/page/report/product-profit-stats';
export const EXPORT_EXCEL_PRODUCT_PROFIT = '/client/page/excel';
export const EXPORT_PDF_PRODUCT_PROFIT = '/client/page/dynamic-report/profit-stats';
// card
export const ENABLE_INVENTORY_TRACKING = '/client/page/product/enable-inventory-tracking';
// processing
export const GET_FOR_PROCESSING = '/client/page/processing-area/get-for-processing';
export const CHANGE_STATUS_DISH = '/client/page/processing-area/update-status';
export const DELETE_DISH = '/client/page/processing-area/delete-dish';
// end processing

// print-setting
export const GET_LIST_SETTING = '/client/page/processing-area/get-with-paging';
export const GET_LIST_PRINTER = '/client/common/print-setting/get-all-printer';
export const GET_LIST_PRINT_TEMPLATE = '/client/common/print-template/get-all';
export const UPDATE_LIST_SETTING = '/client/common/print-setting/update';
// end

// notification

export const GET_LIST_NOTIFICATION = '/client/page/notification/get-with-paging';
export const UPDATE_NOTIFICATION = '/client/page/notification/update-status';
export const COUNT_NOTIFICATION = '/client/page/notification/count-unread';
// end

//processing-area
export const FILTER_PROCESSING_AREA = '/client/page/processing-area/get-with-paging';
export const CREATE_PROCESSING_AREA = '/client/page/processing-area/create';
export const DELETE_PROCESSING_AREA = '/client/page/processing-area/delete/';
export const FIND_PROCESSING_AREA = '/client/page/processing-area/find/';
export const FIND_PRODUCT_PROCESSING_AREA = '/client/page/product-processing-area/get-with-paging';
export const UPDATE_PROCESSING_AREA = '/client/page/processing-area/update';
export const FIND_LIST_PRODUCT_PRODUCT_UNIT_ID = '/client/page/processing-area-product/get-all-productProductUnitId-with-not-PaId';
export const PRODUCT_PROCESSING_AREA = '/client/page/processing-area-product/get-with-processingAreaId';
//end processing-area

// processing-area
export const DELETE_PROCESSING_AREA_PRODUCT = '/client/page/processing-area-product/delete/';
export const CHECK_PRODUCT_PROCESSING_AREA_PRODUCT = '/client/page/processing-area-product/check-product/';
//end processing-area

// print config

export const GET_ALL_PRINT_CONFIG = '/client/common/print-template/get-all';
export const CREATE_PRINT_CONFIG = '/client/common/print-template/create';
export const EDIT_PRINT_CONFIG = '/client/common/print-template/update';
export const GET_DATA_DEFAULT = '/client/common/print-template/get-data-default';
export const GET_PRINT_COFIG_DEFAULT = '/client/common/print-template/get-default';
export const DELETE_PRINT_CONFIG = '/client/common/print-template/delete';

// report
export const GET_INVOICE_PATTERNS = '/client/common/get-register-invoice-patterns/';
export const GET_REPORT_PRODUCT_SALES = '/client/page/report/product-sales-stats';
export const GET_INVENTORY_STATS = '/client/page/report/inventory-common-stats-v2';
export const GET_PREVIEW_INVENTORY_STATS = '/client/page/product/report-get-with-paging';
export const INVENTORY_STATS_PDF = '/client/page/pdf-report/inventory-common-stats-v2';
export const INVENTORY_STATS_EXCEL = '/client/page/report-excel/inventory-stats-v2';
export const EXPORT_EXCEL = '/client/page/report/excel';
export const EXPORT_PDF = '/client/page/report/pdf';
export const GET_REPORT_REVENUE_COMMON = '/client/page/home/revenue-common-stats';
export const GET_EXCEL_REVENUE_COMMON = '/client/page/report-revenue-common/excel';
export const GET_PDF_REVENUE_COMMON = '/client/page/report-revenue-common/pdf';
export const GET_ACTIVITY_HISTORY = '/client/page/report/activity-history-stats';
export const EXPORT_EXCEL_ACTIVITY_HISTORY = '/client/page/report-excel/activity-history-stats';
export const EXPORT_PDF_ACTIVITY_HISTORY = '/client/page/report-pdf/activity-history-stats';
export const GET_RECENT_ACTIVITY = '/client/page/report/recent-activites';
export const BARCODE_EXCEL = '/client/page/report-excel/barcode';

// register
export const REGISTER_SEND_OTP = '/client/common/register/otp/send';
export const REGISTER_CHECK_OTP = '/client/common/register/otp/check';
// end register

//role
export const FILTER_ROLES = '/client/page/role/get-with-paging';
export const CREATE_ROLES = '/client/page/role/create';
export const UPDATE_ROLES = '/client/page/role/update';
export const DELETE_ROLES = '/client/page/role/delete/';
//end role

//permissions
export const FILTER_PERMISSION = '/client/page/role/permission/get-all';
export const FIND_ROLE = '/client/page/role/by-id/';
//end permissions

// company
export const FILTER_COMPANY = '/client/page/company/get-with-paging';
//company

// new-feature
export const GET_ALL_NEW_FEATURE = '/client/page/version-update/new-feature';
export const NEWEST_NEW_FEATURE = '/client/page/version-update/newest-new-feature';
//end
