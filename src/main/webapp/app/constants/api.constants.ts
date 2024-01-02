// Đánh Constants theo tên chức năng hoặc api cụ thể

// Account
export const CHANGE_PASSWORD = '/client/user/change-password';
// end account

// Customer
export const LOGIN = '/client/common/authenticate';
export const REGISTER = '/client/common/register';
export const ACCOUNT = '/client/common/account';
export const CREATE_USER = '/user/create';
export const PING = '/p/client/ping';
export const GET_LIST_CUSTOMER = '/client/page/customer/get-all-with-paging';
export const CREATE_CUSTOMER = '/client/page/customer/create';
// End Customer

// product
export const GET_PRODUCT_BY_ID = '/client/page/product/by-id/';
export const UPDATE_PRODUCT = '/client/page/product/update';
export const CREATE_PRODUCT = '/client/page/product/create';
export const DELETE_PRODUCT = '/client/page/product/delete';
export const GET_ALL_PRODUCT = '/client/page/product/category-get-with-offline';
export const GET_PRODUCT_PAGING = '/client/page/product/product-get-with-paging';
export const DELETE_PRODUCT_BY_ID = '/client/page/product/delete/';

// End product

// Invoice
export const LOGIN_INVOICE = '/client/common/config/login/easy-invoice';
export const GET_OWNER_INFO = '/client/common/get-owner-info/';
export const GET_COMPANY_CONFIG = '/client/page/config/get-company-configs/';
// End Invoice
// print config
export const GET_ALL_PRINT_CONFig = '/client/page/config/get-print-config/';
export const UPDATE_PRINT_CONFIG = '/client/page/config/update-print-config';
export const UPDATE_LIST_PRINT_CONFIG = '/client/page/config/update-list-print-config';
// end print-config

// Device
export const GET_DEVICE_CODE = '/client/common/get-device-code/by-name';
export const REGISTER_DEVICE = '/client/common/register-owner-device';
// EndDevice

// bill
export const GET_LIST_BILL = '/client/page/bill/get-with-paging';
export const GET_BILL_BY_ID = '/client/page/bill/by-id/';
export const GET_BILL_BY_CODE = '/client/page/bill/by-code/';
export const CREATE_BILL = '/client/page/bill/create';
export const UPDATE_BILL = '/client/page/bill/update';
export const CHECKOUT_BILL_BY_ID = '/client/page/bill/done-by-id';
export const CANCEL_BILL_BY_ID = '/client/page/bill/cancel-by-id';
export const PAY_OFF_DEBT_BILL = '/client/page/bill/pay-off-debt';
// End bill

// customer
export const GET_ALL_CUSTOMER_OFFLINE = '/client/page/customer/get-all-for-offline';
export const GET_LIST_CUSTOMER_UNIT = '/client/page/customer/get-all-customer-unit';
export const GET_CUSTOMER_BY_ID = '/client/page/customer/by-id/';
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
export const PRODUCT_PROFIT_STATUS = '/client/page/report/product-profit-status';
export const PRODUCT_SALE_STATUS = '/client/page/report/product-profit-status';
// End report
