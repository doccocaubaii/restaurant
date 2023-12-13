import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { PosCustomerOrderPage } from './pages/order/customer-order/customer-order.component';
import { LoginPage } from './pages/login/login.component';
import { DashboardPage } from './pages/dashboard/dashboard.component';
import { OrderPage } from './pages/order/order-list/order.component';
import { PosCustomerOrderOfflinePage } from './pages/order/customer-order-offline/customer-order-offline.component';
import { CustomerComponent } from './pages/customer/customer.component';
import { InvoiceConfigurationComponent } from './pages/invoice/invoice-configuration/invoice-configuration.component';
import { InvoiceListComponent } from './pages/invoice/list/invoice-list.component';
import { ProductCategoryComponent } from './pages/product-category/product-category.component';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AreaComponent } from './pages/area/area/area.component';
import { OrderGuard } from './shared/guard/order-guard.guard';
import {
  AREA,
  AREA_BY_ID,
  AREA_UNIT,
  BILL,
  BILL_OFFLINE,
  BILL_ORDER,
  BILL_ORDER_BY_ID,
  BILL_ORDER_OFFLINE,
  CARD,
  CARD_POLICY,
  CHANGE_FORGOT_PASSWORD,
  CHANGE_PASSWORD,
  COMPANY,
  CUSTOMER,
  DESIGN_CONFIGURATION,
  EMPLOYEE,
  FORGOT_PASSWORD,
  HOME,
  INVOICE,
  INVOICE_CONFIG,
  INVOICE_EI,
  LOGIN,
  ORDER_PRINT_CONFIG,
  PERMISSIONS,
  PROCESSING,
  PROCESSING_AREA_LIST,
  PRODUCT,
  PRODUCT_GROUP,
  PRODUCT_TOPPING,
  PRODUCT_UNIT,
  PROMOTIONS,
  RECEIPT_PAYMENT,
  REGISTER,
  REPORT_ACTIVITY_HISTORY,
  REPORT_INVENTORY_STATS,
  REPORT_PRODUCT_HOT_SALES,
  REPORT_PRODUCT_PROFIT,
  REPORT_PRODUCT_SALES,
  REPORT_PROFIT,
  REPORT_REVENUE_COMMON,
  RS_INOUT_WARD,
  SECURITY_POLICY,
  VOUCHER,
} from './constants/app.routing.constants';
import { ManageAreaComponent } from './pages/area/manage-area.component';
import { StaffComponent } from './pages/staff/staff.component';
import { PasswordComponent } from './account/password/password.component';
import { CompanyComponent } from './pages/company/company.component';
import { CardComponent } from './pages/card/card.component';
import { CardPolicyComponent } from './pages/card/card-policy/card-policy.component';
import { PasswordResetInitComponent } from './account/password-reset/init/password-reset-init.component';
import { PasswordResetFinishComponent } from './account/password-reset/finish/password-reset-finish.component';
import { ProductSalesStatsComponent } from './pages/report/product-sales-stats/product-sales-stats.component';
import { InventoryStatsComponent } from './pages/report/inventory-stats/inventory-stats.component';
import { ProductToppingComponent } from './pages/product-topping/product-topping.component';
import { ListProcessingAreaComponent } from './pages/processing-area/list-processing-area/list-processing-area.component';
import { ProcessingComponent } from './pages/processing/processing.component';
import { ReceiptPaymentComponent } from './pages/receipt-payment/list/receipt-payment.component';
import { ProductUnitComponent } from './pages/product/unit/product-unit.component';
import { VoucherComponent } from './pages/voucher/voucher.component';
import { ProductHotSaleComponent } from './pages/report/product-hot-sale/product-hot-sale.component';
import { ActivityHistoryComponent } from './pages/report/activity-history/activity-history.component';
import { PromotionsComponent } from './pages/promotions/promotions.component';
import { OrderPrintConfigComponent } from './pages/print-config/order-print-config/order-print-config.component';
import { DesignConfigurationComponent } from './pages/config/design/design-configuration.component';
import { RevenueCommonStatsComponent } from './pages/report/revenue-common-stats/revenue-common-stats.component';
import { SecurityPolicyComponent } from './pages/security-policy/security-policy.component';
import { InvoiceEiListComponent } from './pages/invoice/invoice-ei-list/invoice-ei-list.component';
import { ReportProductProfitComponent } from './pages/report/report-product-profit/report-product-profit.component';
import { RolesComponent } from './pages/roles/roles-list/roles.component';
import { Authority } from './config/authority.constants';
import { RegisterComponent } from './pages/register/register.component';
import { AuthGuard } from './shared/service/auth-guard.service';
import { OrderListOfflineComponent } from './pages/order/order-list-offline/order-list-offline.component';

const routes: Routes = [
  { path: '', redirectTo: 'login', pathMatch: 'full' },
  { path: HOME, component: DashboardPage, title: 'Trang chủ', data: { title: 'home' }, canActivate: [AuthGuard] },
  { path: BILL, component: OrderPage, title: 'Đơn hàng', data: { title: 'bill' }, canActivate: [AuthGuard] },
  { path: LOGIN, component: LoginPage, title: 'Đăng nhập', data: { title: 'login' } },
  { path: AREA_BY_ID, component: ManageAreaComponent, title: 'Chi tiết khu vực', canActivate: [AuthGuard] },
  { path: HOME, component: DashboardPage, title: 'Trang chủ', data: { title: 'home' }, canActivate: [AuthGuard] },
  { path: BILL, component: OrderPage, title: 'Đơn hàng', data: { title: 'bill' }, canActivate: [AuthGuard] },
  {
    path: BILL_OFFLINE,
    component: OrderListOfflineComponent,
    title: 'Đơn hàng offline',
    data: { title: 'bill' },
    canActivate: [AuthGuard],
  },
  { path: AREA_BY_ID, component: ManageAreaComponent, title: 'Chi tiết khu vực', canActivate: [AuthGuard] },
  { path: REPORT_REVENUE_COMMON, component: RevenueCommonStatsComponent, title: 'Báo cáo lợi nhuận', canActivate: [AuthGuard] },
  { path: REPORT_ACTIVITY_HISTORY, component: ActivityHistoryComponent, title: 'Báo cáo hoạt động gần đây', canActivate: [AuthGuard] },
  {
    path: BILL_ORDER,
    component: PosCustomerOrderPage,
    canDeactivate: [OrderGuard],
    canActivate: [AuthGuard],
    title: 'Bán hàng',
    data: { permissions: Authority.BILL_ADD, title: 'Pos customer order page' },
  },
  {
    path: BILL_ORDER_BY_ID,
    component: PosCustomerOrderPage,
    canDeactivate: [OrderGuard],
    title: 'Chi tiết Bán hàng',
    data: {
      title: 'Pos customer order page',
    },
    canActivate: [AuthGuard],
  },
  {
    path: BILL_ORDER_OFFLINE,
    component: PosCustomerOrderOfflinePage,
    title: 'Bán hàng offline',
    data: { title: 'Pos customer order page' },
    canActivate: [AuthGuard],
  },
  // {path: 'pos/kitchen-order', component: PosKitchenOrderPage, data: {title: 'Pos kitchen order page'}},
  // {path: 'pos/counter-checkout', component: PosCounterCheckoutPage, data: {title: 'Pos counter checkout'}},
  // {path: 'pos/table-booking', component: PosTableBookingPage, data: {title: 'Pos table booking'}},
  {
    path: RS_INOUT_WARD,
    title: 'Kho',
    loadChildren: () => import('./pages/warehouse/warehouse.module').then(m => m.WarehouseModule),
    canActivate: [AuthGuard],
    data: { permissions: [Authority.RS_OUTWARD_VIEW, Authority.RS_INWARD_VIEW] },
  },
  {
    path: PRODUCT,
    title: 'Sản phẩm',
    loadChildren: () => import('./pages/product/product.module').then(m => m.ProductModule),
    canActivate: [AuthGuard],
  },
  // { path: INVOICE_CONNECT, component: ConnectInvoiceComponent, title: 'Sản phẩm', data: { title: 'Basic table' } },
  { path: INVOICE, component: InvoiceListComponent, title: 'Hoá đơn', data: { title: 'Basic table' }, canActivate: [AuthGuard] },
  { path: CHANGE_PASSWORD, component: PasswordComponent, title: 'Đổi mật khẩu', canActivate: [AuthGuard] },

  // report-preview
  { path: COMPANY, component: CompanyComponent, title: 'Danh sách cửa hàng', canActivate: [AuthGuard] },
  { path: FORGOT_PASSWORD, component: PasswordResetInitComponent, title: 'Quên mật khẩu', canActivate: [AuthGuard] },
  { path: CHANGE_FORGOT_PASSWORD, component: PasswordResetFinishComponent, title: 'Quên mật khẩu', canActivate: [AuthGuard] },
  { path: RECEIPT_PAYMENT, component: ReceiptPaymentComponent, title: 'Thu Chi', canActivate: [AuthGuard] },
  { path: PRODUCT_UNIT, component: ProductUnitComponent, title: 'Đơn vị tính', canActivate: [AuthGuard] },
  { path: REPORT_PRODUCT_SALES, component: ProductSalesStatsComponent, title: 'Thống kê hàng hóa bán ra', canActivate: [AuthGuard] },
  { path: REPORT_PRODUCT_HOT_SALES, component: ProductHotSaleComponent, title: 'Thống kê sản phẩm bán chạy', canActivate: [AuthGuard] },
  { path: REPORT_PRODUCT_PROFIT, component: ReportProductProfitComponent, title: 'Thống kê lợi nhuận sản phẩm', canActivate: [AuthGuard] },
  { path: REPORT_INVENTORY_STATS, component: InventoryStatsComponent, title: 'Thống kê tồn kho', canActivate: [AuthGuard] },
  { path: PROMOTIONS, component: PromotionsComponent, title: 'Chương trình khuyến mại', canActivate: [AuthGuard] },
  { path: ORDER_PRINT_CONFIG, component: OrderPrintConfigComponent, title: 'Mẫu in đơn hàng', canActivate: [AuthGuard] },
  { path: DESIGN_CONFIGURATION, component: DesignConfigurationComponent, title: 'Cấu hình hiển thị' },
  {
    path: PROCESSING_AREA_LIST,
    component: ListProcessingAreaComponent,
    title: 'Danh sách khu vực chế biến',
    canActivate: [AuthGuard],
    data: { permissions: Authority.PROCESSING_AREA_VIEW },
  },
  {
    path: PERMISSIONS,
    component: RolesComponent,
    title: 'Phân quyền',
    canActivate: [AuthGuard],
    data: { permissions: Authority.ROLE_VIEW },
  },
  { path: REGISTER, component: RegisterComponent, title: 'Đăng kí tài khoản' },
  {
    path: INVOICE_CONFIG,
    component: InvoiceConfigurationComponent,
    title: 'Cấu hình',
    data: { permissions: Authority.COMPANY_CONFIG_VIEW, title: 'Basic table' },
    canActivate: [AuthGuard],
  },
  {
    path: INVOICE,
    component: InvoiceListComponent,
    title: 'Hoá đơn',
    data: { permissions: Authority.INVOICE_VIEW, title: 'Basic table' },
    canActivate: [AuthGuard],
  },
  { path: VOUCHER, component: VoucherComponent, title: 'Voucher', canActivate: [AuthGuard], data: { permissions: Authority.VOUCHER_VIEW } },
  {
    path: CUSTOMER,
    component: CustomerComponent,
    title: 'Khách hàng',
    canActivate: [AuthGuard],
    data: { permissions: Authority.CUSTOMER_VIEW },
  },
  {
    path: EMPLOYEE,
    component: StaffComponent,
    title: 'Nhân viên',
    canActivate: [AuthGuard],
    data: { permissions: Authority.EMPLOYEE_VIEW },
  },
  { path: AREA, component: AreaComponent, title: 'Khu vực', canActivate: [AuthGuard], data: { permissions: Authority.AREA_VIEW } },
  {
    path: AREA_UNIT,
    component: ManageAreaComponent,
    title: 'Bàn',
    canActivate: [AuthGuard],
    data: { permissions: Authority.AREA_UNIT_VIEW },
  },
  {
    path: PRODUCT_GROUP,
    component: ProductCategoryComponent,
    title: 'Nhóm sản phẩm',
    canActivate: [AuthGuard],
    data: { permissions: Authority.GROUP_VIEW },
  },
  { path: CARD, component: CardComponent, title: 'Thẻ', canActivate: [AuthGuard], data: { permissions: Authority.CARD_VIEW } },
  {
    path: CARD_POLICY,
    component: CardPolicyComponent,
    title: 'Chính sách thẻ',
    canActivate: [AuthGuard],
    data: { permissions: Authority.CARD_POLICY_VIEW },
  },
  { path: CHANGE_PASSWORD, component: PasswordComponent, title: 'Đổi mật khẩu', canActivate: [AuthGuard] },
  { path: FORGOT_PASSWORD, component: PasswordResetInitComponent, title: 'Quên mật khẩu', canActivate: [AuthGuard] },
  {
    path: CHANGE_FORGOT_PASSWORD,
    component: PasswordResetFinishComponent,
    title: 'Quên mật khẩu',
    canActivate: [AuthGuard],
  },
  {
    path: RECEIPT_PAYMENT,
    component: ReceiptPaymentComponent,
    title: 'Thu Chi',
    canActivate: [AuthGuard],
    data: { permissions: [Authority.REVENUE_VIEW, Authority.EXPENSE_VIEW] },
  },
  {
    path: PRODUCT_TOPPING,
    component: ProductToppingComponent,
    title: 'Sản phẩm bán kèm',
    canActivate: [AuthGuard],
    data: { permissions: Authority.TOPPING_VIEW },
  },
  {
    path: PROCESSING,
    component: ProcessingComponent,
    title: 'Bếp',
    canActivate: [AuthGuard],
    data: { permissions: Authority.KITCHEN_VIEW },
  },
  { path: DESIGN_CONFIGURATION, component: DesignConfigurationComponent, title: 'Cấu hình hiển thị' },
  { path: REGISTER, component: RegisterComponent, title: 'Đăng kí tài khoản' },
  { path: SECURITY_POLICY, component: SecurityPolicyComponent, title: 'Chính sách bảo mật dữ liệu' },
  {
    path: REPORT_PRODUCT_SALES,
    component: ProductSalesStatsComponent,
    data: { permissions: Authority.REPORT_VIEW },
    title: 'Thống kê hàng hóa bán ra',
    canActivate: [AuthGuard],
  },
  {
    path: REPORT_INVENTORY_STATS,
    component: InventoryStatsComponent,
    data: { permissions: Authority.REPORT_VIEW },
    title: 'Thống kê tồn kho',
    canActivate: [AuthGuard],
  },
  { path: REGISTER, component: RegisterComponent, title: 'Đăng kí tài khoản' },
  {
    path: REPORT_PRODUCT_HOT_SALES,
    component: ProductHotSaleComponent,
    data: { permissions: Authority.REPORT_VIEW },
    title: 'Thống kê sản phẩm bán chạy',
    canActivate: [AuthGuard],
  },
  { path: SECURITY_POLICY, component: SecurityPolicyComponent, title: 'Chính sách bảo mật dữ liệu', canActivate: [AuthGuard] },
  { path: INVOICE_EI, component: InvoiceEiListComponent, title: 'Hoá đơn EasyInvoice', canActivate: [AuthGuard] },
  { path: PRODUCT_UNIT, component: ProductUnitComponent, title: 'Đơn vị tính' },
  { path: INVOICE_EI, component: InvoiceEiListComponent, title: 'Hoá đơn EasyInvoice', canActivate: [AuthGuard] },
  {
    path: REPORT_PRODUCT_PROFIT,
    component: ReportProductProfitComponent,
    data: { permissions: Authority.REPORT_VIEW },
    title: 'Thống kê lợi nhuận sản phẩm',
    canActivate: [AuthGuard],
  },
  { path: SECURITY_POLICY, component: SecurityPolicyComponent, title: 'Chính sách bảo mật dữ liệu', canActivate: [AuthGuard] },
  {
    path: PRODUCT_UNIT,
    component: ProductUnitComponent,
    title: 'Đơn vị tính',
    canActivate: [AuthGuard],
    data: { permissions: Authority.GROUP_VIEW },
  },
  {
    path: PROMOTIONS,
    component: PromotionsComponent,
    title: 'Chương trình khuyến mại',
    canActivate: [AuthGuard],
    data: { permissions: Authority.VOUCHER_VIEW },
  },
  {
    path: INVOICE_EI,
    component: InvoiceEiListComponent,
    title: 'Hoá đơn EasyInvoice',
    canActivate: [AuthGuard],
    data: { permissions: Authority.EASY_INVOICE_CONFIG_VIEW },
  },
  { path: REPORT_REVENUE_COMMON, component: RevenueCommonStatsComponent, title: 'Lợi nhuận (Ước tính)' },
];

@NgModule({
  imports: [
    RouterModule.forRoot(routes, {
      scrollPositionRestoration: 'enabled',
      anchorScrolling: 'enabled',
      enableTracing: false,
    }),
    BrowserModule,
    FormsModule,
    ReactiveFormsModule,
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
