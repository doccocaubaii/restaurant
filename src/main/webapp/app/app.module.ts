import { ErrorHandler, LOCALE_ID, NgModule } from '@angular/core';
import { DatePipe, registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import { DragDropModule } from '@angular/cdk/drag-drop';
import {
  NgbActiveModal,
  NgbDateAdapter,
  NgbDateParserFormatter,
  NgbDatepickerConfig,
  NgbDatepickerI18n,
  NgbDatepickerModule,
  NgbTimepickerModule,
} from '@ng-bootstrap/ng-bootstrap';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import './config/dayjs';
import { SharedModule } from 'app/shared/shared.module';
import { TranslationModule } from 'app/shared/language/translation.module';
import { AppRoutingModule } from './app-routing.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { NgbDateDayjsAdapter } from './config/datepicker-adapter';
import { fontAwesomeIcons } from './config/font-awesome-icons';
import { httpInterceptorProviders } from 'app/core/interceptor/index';
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';
import { PosMenuStockPage } from './pages/pos/menu-stock/menu-stock.component';
import { PosTableBookingPage } from './pages/pos/table-booking/table-booking.component';
import { DashboardPage } from './pages/dashboard/dashboard.component';
import { PosKitchenOrderPage } from './pages/pos/kitchen-order/kitchen-order.component';
import { PosCounterCheckoutPage } from './pages/pos/counter-checkout/counter-checkout.component';
import { PosCustomerOrderPage } from './pages/order/customer-order/customer-order.component';
import { ThemePanelComponent } from './layouts/theme-panel/theme-panel.component';
import { FloatSubMenuComponent } from './layouts/float-sub-menu/float-sub-menu.component';
import { PanelComponent } from './layouts/panel/panel.component';
import { TopMenuComponent } from './layouts/top-menu/top-menu.component';
import { SidebarRightComponent } from './layouts/sidebar-right/sidebar-right.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { HeaderComponent } from './layouts/header/header.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { FullCalendarModule } from '@fullcalendar/angular';
import { HIGHLIGHT_OPTIONS, HighlightModule } from 'ngx-highlightjs';
import { LoadingBarRouterModule } from '@ngx-loading-bar/router';
import { NgApexchartsModule } from 'ng-apexcharts';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import { PERFECT_SCROLLBAR_CONFIG, PerfectScrollbarConfigInterface, PerfectScrollbarModule } from 'ngx-perfect-scrollbar';
import { TrendModule } from 'ngx-trend';
import { NgxEditorModule } from 'ngx-editor';
import { adapterFactory } from 'angular-calendar/date-adapters/date-fns';
import { CalendarModule, DateAdapter } from 'angular-calendar';
import { ColorSketchModule } from 'ngx-color/sketch';

import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import interactionPlugin from '@fullcalendar/interaction';
import listPlugin from '@fullcalendar/list';
import bootstrapPlugin from '@fullcalendar/bootstrap';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TableDataPage } from './pages/tables/table-data/table-data.component';
import { TableBasicPage } from './pages/tables/table-basic/table-basic.component';
import { LoginPage } from './pages/login/login.component';
import { OrderPage } from './pages/order/order-list/order.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { PosCustomerOrderOfflinePage } from './pages/order/customer-order-offline/customer-order-offline.component';
import { SidebarOption } from './utils/SidebarOption';
import { NgSelectModule } from '@ng-select/ng-select';
import { ConfirmCheckoutComponent } from './pages/order/customer-order/confirm-checkout/confirm-checkout.component';
import { DiscountTaxOrderComponent } from './pages/order/customer-order/discount-tax-order/discount-tax-order.component';
import { DiscountTaxProductComponent } from './pages/order/customer-order/discount-tax-product/discount-tax-product.component';
import { SetVatRateProductComponent } from './pages/order/customer-order/set-vat-rate-product/set-vat-rate-product.component';
import { ViewDetailOrderComponent } from './pages/order/order-list/view-detail-order/view-detail-order.component';
import { DiscountProductComponent } from './pages/order/customer-order/discount-tax-product/discount-product/discount-product.component';
import { ShowListActionOrderComponent } from './pages/order/order-list/view-detail-order/show-list-action-order/show-list-action-order.component';
import { CustomerComponent } from './pages/customer/customer.component';
import { CustomerSaveComponent } from './pages/customer/customer-save/customer-save.component';
import { ToastrModule } from 'ngx-toastr';
import { ConnectInvoiceComponent } from './pages/invoice/invoice-connect/connect-invoice.component';
import { InvoiceConfigurationComponent } from './pages/invoice/invoice-configuration/invoice-configuration.component';
import { InvoiceListComponent } from './pages/invoice/list/invoice-list.component';
import { InvoiceDetailComponent } from './pages/invoice/invoice-detail/invoice-detail.component';
import { ManageAreaComponent } from './pages/area/manage-area.component';
import { UnitComponent } from './pages/area/unit/unit.component';
import { CreateAreaComponent } from './pages/area/create-area/create-area.component';
import { CreateUnitComponent } from './pages/area/unit/create-unit/create-unit.component';
import { PrintConfigComponent } from './layouts/modal/print-config/print-config.component';
import { DeviceComponent } from './layouts/modal/device/device.component';
import { UpdateInvoiceComponent } from './layouts/modal/invoice-update/update-invoice.component';
import { DeleteReceiptPaymentComponent } from './pages/receipt-payment/delete/delete-receipt-payment.component';
import { DeleteMultiDetailComponent } from './pages/receipt-payment/delete-multi-detail/delete-multi-detail.component';
import { LoadingOption } from './utils/loadingOption';
import { StaffComponent } from './pages/staff/staff.component';
import { CreateStaffComponent } from './pages/staff/create-staff/create-staff.component';
import { ProductCategoryComponent } from './pages/product-category/product-category.component';
import { NgxCurrencyModule } from 'ngx-currency';
import { AccountModule } from './account/account.module';
import { AreaComponent } from './pages/area/area/area.component';
import { PosInvoiceComponent } from './pages/order/customer-order/pos-invoice/pos-invoice.component';
import { OrderOfflineManagementComponent } from './pages/order/order-list-offline/order-offline-management.component';
import { UpdateReceiptPaymentComponent } from './pages/receipt-payment/update/update-receipt-payment.component';
import { DateFormatDirective } from './shared/directive/date-format.directive';
import { NgbDateCustomParserFormatter } from './layouts/config/service/datepicker-popup';
import { NgbDatepickerI18nVi } from './config/ngb-datepicker-i18n-vi';
import { ContentOption } from './utils/contentOption';
import { PosInvoiceTableComponent } from './pages/order/customer-order/pos-invoice-table/pos-invoice-table.component';
import { KitchenTableComponent } from './pages/order/customer-order/pos-invoice/kitchen-table/kitchen-table.component';
import { PosInvoicePrintComponent } from './pages/order/customer-order/pos-invoice/invoice/pos-invoice-print.component';
import { ImportProductComponent } from './pages/import/excel/product/import-product.component';
import { ImportProductGroupComponent } from './pages/import/excel/productGroup/import-product-group.component';
import { ImportChooseFileComponent } from './pages/import/excel/common/chooseFile/import-choose-file.component';
import { ImportCustomerComponent } from './pages/import/excel/customer/import-customer.component';
import { VatRateDiscountOrderComponent } from './pages/order/customer-order/vat-rate-discount-order/vat-rate-discount-order.component';
import { ProductToppingComponent } from './pages/product-topping/product-topping.component';
import { CardPolicyComponent } from './pages/card/card-policy/card-policy.component';
import { CardComponent } from './pages/card/card.component';
import { CardModalComponent } from './pages/card/modal/card-modal.component';
import { ProductDetailComponent } from './pages/order/customer-order/product-detail/product-detail.component';
import { CompanyComponent } from './pages/company/company.component';
import { CompanyModalCreateComponent } from './pages/company/company-modal-create/company-modal-create.component';
import { CompanyModalUpdateComponent } from './pages/company/company-modal-update/company-modal-update.component';
import { CustomerCardHistoryComponent } from './pages/customer/customer-card/modal/card-history/customer-card-history.component';
import { ReceiptPaymentComponent } from './pages/receipt-payment/list/receipt-payment.component';
import { CreateBusinessTypeComponent } from './pages/receipt-payment/create-business-type/create-business-type.component';
import { ModalCreateToppingGroupComponent } from './pages/product-topping/modal-create-topping-group/modal-create-topping-group.component';
import { VoucherOrderComponent } from './pages/order/customer-order/voucher-order/voucher-order.component';
import { ProductOrderToppingComponent } from './pages/order/customer-order/product-topping/product-topping.component';
import { ProductToppingListComponent } from './pages/order/customer-order/product-topping-list/product-topping-list.component';
import { ModalConfirmCancelBillComponent } from './pages/order/order-list/modal-confirm-cancel-bill/modal-confirm-cancel-bill.component';
import { CustomerCardModalComponent } from './pages/customer/customer-card/modal/card-save/customer-card-modal.component';
import { TimeFormatDirective } from './shared/directive/time-format.directive';
import { PromotionSaveComponent } from './pages/promotions/save/promotion-save.component';
import { PromotionsDetailComponent } from './pages/promotions/detail/promotions-detail.component';
import { PromotionsComponent } from './pages/promotions/promotions.component';
import { ConvertPointComponent } from './pages/order/customer-order/convert-point/convert-point.component';
import { DateFormatCustomPipe } from './shared/directive/date-format-pipe.directive';
import { OrderListOfflineComponent } from './pages/order/order-list-offline/order-list-offline.component';
import { NotifyComponent } from './layouts/modal/notify/notify.component';
import { ProductHotSaleComponent } from './pages/report/product-hot-sale/product-hot-sale.component';
import { VoucherComponent } from './pages/voucher/voucher.component';
import { SaveVoucherComponent } from './pages/voucher/save-voucher/save-voucher.component';
import { ApplyVoucherComponent } from './pages/voucher/apply-voucher/apply-voucher.component';
import { ProductVoucherComponent } from './pages/order/customer-order/voucher-order/product-voucher/product-voucher.component';
import { DesignConfigurationComponent } from './pages/config/design/design-configuration.component';
import { CKEditorModule } from 'ckeditor4-angular';
import { CkeditorPrintConfigComponent } from './layouts/modal/ckeditor-print-config/ckeditor-print-config.component';
import { ProcessingComponent } from './pages/processing/processing.component';
import { ListProcessingAreaComponent } from './pages/processing-area/list-processing-area/list-processing-area.component';
import { ModalCreateProcessingAreaComponent } from './pages/processing-area/modal-create-processing-area/modal-create-processing-area.component';
import { CreateProductProcessingAreaComponent } from './pages/processing-area/create-product-processing-area/create-product-processing-area.component';
import { ConfirmChangeComponent } from './pages/processing-area/confirm-change/confirm-change.component';
import { OrderPrintConfigComponent } from './pages/print-config/order-print-config/order-print-config.component';
import { PaymentPrintConfigComponent } from './pages/print-config/payment-print-config/payment-print-config.component';
import { PrintSettingComponent } from './pages/processing/print-setting/print-setting.component';
import { RegisterComponent } from './pages/register/register.component';
import { NgOtpInputModule } from 'ng-otp-input';
import { SendOtpComponent } from './pages/register/send-otp/send-otp.component';
import { TaxCodeDirective } from './shared/directive/tax-code.directive';
import { ProductUnitComponent } from './pages/product/unit/product-unit.component';
import { MultiDatePicker } from './layouts/multi-date-picker/multi-date-picker';
import { PromotionUsageComponent } from './pages/promotions/usage/promotion-usage.component';

import { ProductSalesStatsComponent } from './pages/report/product-sales-stats/product-sales-stats.component';
import { InventoryStatsComponent } from './pages/report/inventory-stats/inventory-stats.component';
import { PreviewInventoryStatsComponent } from './pages/report/inventory-stats/preview/preview-inventory-stats.component';
import { ModalSearchInitComponent } from './pages/report/product-sales-stats/modal-search-init/modal-search-init.component';

import { MqttModule } from 'ngx-mqtt';
import { ActivityHistoryComponent } from './pages/report/activity-history/activity-history.component';
import { RevenueCommonStatsComponent } from './pages/report/revenue-common-stats/revenue-common-stats.component';
import { SearchInitRevenueCommonComponent } from './pages/report/revenue-common-stats/modal-search-init/modal-search-init.component';
import { AreaOrderComponent } from './pages/area/area-order/area-order.component';

import { SecurityPolicyComponent } from './pages/security-policy/security-policy.component';
import { InvoiceSyncEIComponent } from './pages/invoice/invoice-sync-ei/invoice-sync-ei.component';
import { InvoiceEiListComponent } from './pages/invoice/invoice-ei-list/invoice-ei-list.component';
import { ReportProductProfitComponent } from './pages/report/report-product-profit/report-product-profit.component';
import { SearchInitProductProfitComponent } from './pages/report/report-product-profit/search-init-product-profit/search-init-product-profit.component';
import { ElementRefOption } from './utils/elementRef';
import { ErrorHandlerComponent } from './shared/error-handler/error-handler.component';
import { NotificationComponent } from './layouts/notification/notification.component';
import { TreeViewComponent } from './pages/roles/tree-view/tree-view.component';
import { TreeViewItemComponent } from './pages/roles/tree-view-item/tree-view-item.component';
import { RolesComponent } from './pages/roles/roles-list/roles.component';
import { CreateRolesComponent } from './pages/roles/create-roles/create-roles.component';
import { ConfirmDeleteComponent } from './pages/roles/confirm-delete/confirm-delete.component';

const MQTT_SERVICE_OPTIONS: any = {
  hostname: 'websocket.easyposs.vn',
  port: 8883,
  protocol: 'wss',
  username: 'EposSocket',
  password: 'App#Epos123@SOCKET',
  connectOnCreate: false,
};
import { NewFeatureComponent } from './pages/new-feature/new-feature.component';
import { NgxTinymceModule } from 'ngx-tinymce';
import { ProductVoucherQuantityComponent } from './pages/order/customer-order/product-voucher-quantity/product-voucher-quantity.component';
const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true,
};
FullCalendarModule.registerPlugins([dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin, bootstrapPlugin]);

// @ts-ignore
@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
    MqttModule.forRoot(MQTT_SERVICE_OPTIONS),
    BrowserAnimationsModule,
    // jhipster-needle-angular-add-module JHipster will add new module here

    AppRoutingModule,
    // Set this to true to enable service worker (PWA)
    ServiceWorkerModule.register('ngsw-worker.js', { enabled: false }),
    HttpClientModule,
    NgxWebstorageModule.forRoot({ prefix: 'jhi', separator: '-', caseSensitive: true }),
    TranslationModule,
    CalendarModule.forRoot({
      provide: DateAdapter,
      useFactory: adapterFactory,
    }),
    FormsModule,
    HighlightModule,
    LoadingBarRouterModule,
    NgApexchartsModule,
    NgbDatepickerModule,
    NgbTimepickerModule,
    NgxDatatableModule,
    NgxEditorModule,
    NgxChartsModule,
    PerfectScrollbarModule,
    ReactiveFormsModule,
    TrendModule,
    ColorSketchModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatFormFieldModule,
    NgSelectModule,
    AccountModule,
    ToastrModule.forRoot(),
    NgxCurrencyModule,
    DragDropModule,
    CKEditorModule,
    NgOtpInputModule,
    NgxTinymceModule.forRoot({
      baseURL: './assets/tinymce/',
    }),
  ],

  providers: [
    Title,
    SidebarOption,
    LoadingOption,
    ElementRefOption,
    ContentOption,
    DatePipe,
    NgbActiveModal,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateParserFormatter, useClass: NgbDateCustomParserFormatter },
    { provide: NgbDatepickerI18n, useClass: NgbDatepickerI18nVi },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },

    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG,
    },
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        lineNumbersLoader: () => import('highlightjs-line-numbers.js'), // Optional, only if you want the line numbers
        languages: {
          typescript: () => import('highlight.js/lib/languages/typescript'),
          css: () => import('highlight.js/lib/languages/css'),
          xml: () => import('highlight.js/lib/languages/xml'),
        },
      },
    },
    httpInterceptorProviders,
    { provide: ErrorHandler, useClass: ErrorHandlerComponent },
  ],
  declarations: [
    ShowListActionOrderComponent,
    MainComponent,
    NavbarComponent,
    NotificationComponent,
    ErrorComponent,
    PageRibbonComponent,
    FooterComponent,
    HeaderComponent,
    SidebarComponent,
    SidebarRightComponent,
    TopMenuComponent,
    PanelComponent,
    FloatSubMenuComponent,
    ThemePanelComponent,
    DashboardPage,
    PosCounterCheckoutPage,
    PosKitchenOrderPage,
    PosCustomerOrderPage,
    PosCustomerOrderOfflinePage,
    PosTableBookingPage,
    PosMenuStockPage,
    LoginPage,
    ViewDetailOrderComponent,
    TableDataPage,
    TableBasicPage,
    OrderPage,
    ConfirmCheckoutComponent,
    DiscountTaxOrderComponent,
    DiscountTaxProductComponent,
    SetVatRateProductComponent,
    DiscountProductComponent,
    ConnectInvoiceComponent,
    ProductDetailComponent,
    InvoiceConfigurationComponent,
    TableDataPage,
    TableBasicPage,
    OrderPage,
    InvoiceListComponent,
    InvoiceDetailComponent,
    ManageAreaComponent,
    UnitComponent,
    CreateAreaComponent,
    CreateUnitComponent,
    PrintConfigComponent,
    DeviceComponent,
    UpdateInvoiceComponent,
    CustomerComponent,
    CustomerSaveComponent,
    PosInvoiceComponent,
    PosInvoiceTableComponent,
    OrderOfflineManagementComponent,
    ReceiptPaymentComponent,
    KitchenTableComponent,
    PosInvoicePrintComponent,
    AreaComponent,
    ProductCategoryComponent,
    StaffComponent,
    CreateStaffComponent,
    DateFormatDirective,
    CompanyComponent,
    CompanyModalCreateComponent,
    CompanyModalUpdateComponent,
    VatRateDiscountOrderComponent,
    ImportProductComponent,
    ImportProductGroupComponent,
    ImportChooseFileComponent,
    ImportCustomerComponent,
    CustomerCardModalComponent,
    CardComponent,
    CardPolicyComponent,
    CardModalComponent,
    CustomerCardHistoryComponent,
    ReceiptPaymentComponent,
    UpdateReceiptPaymentComponent,
    DeleteReceiptPaymentComponent,
    DeleteMultiDetailComponent,
    CreateBusinessTypeComponent,
    VoucherComponent,
    SaveVoucherComponent,
    ApplyVoucherComponent,
    OrderListOfflineComponent,
    VatRateDiscountOrderComponent,
    ModalCreateToppingGroupComponent,
    CustomerCardModalComponent,
    CardComponent,
    CardPolicyComponent,
    CardModalComponent,
    ReceiptPaymentComponent,
    UpdateReceiptPaymentComponent,
    DeleteReceiptPaymentComponent,
    ProductUnitComponent,
    DateFormatCustomPipe,
    ConvertPointComponent,
    CustomerCardHistoryComponent,
    ProductToppingComponent,
    ModalCreateToppingGroupComponent,
    ReportProductProfitComponent,
    ProductDetailComponent,
    VoucherOrderComponent,
    ProductOrderToppingComponent,
    ProductToppingListComponent,
    ModalConfirmCancelBillComponent,
    ListProcessingAreaComponent,
    ModalCreateProcessingAreaComponent,
    CreateProductProcessingAreaComponent,
    ProductHotSaleComponent,
    InventoryStatsComponent,
    ProductSalesStatsComponent,
    RevenueCommonStatsComponent,
    ActivityHistoryComponent,
    NotifyComponent,
    ModalSearchInitComponent,
    SearchInitRevenueCommonComponent,
    ReportProductProfitComponent,
    SearchInitProductProfitComponent,
    PreviewInventoryStatsComponent,
    ConvertPointComponent,
    VoucherComponent,
    SaveVoucherComponent,
    ApplyVoucherComponent,
    NotifyComponent,
    PromotionsComponent,
    PromotionsDetailComponent,
    PromotionSaveComponent,
    TimeFormatDirective,
    ProductVoucherComponent,
    DesignConfigurationComponent,
    CkeditorPrintConfigComponent,
    ProcessingComponent,
    OrderPrintConfigComponent,
    PaymentPrintConfigComponent,
    CkeditorPrintConfigComponent,
    MultiDatePicker,
    PrintSettingComponent,
    DesignConfigurationComponent,
    ConfirmChangeComponent,
    ProductSalesStatsComponent,
    InventoryStatsComponent,
    PreviewInventoryStatsComponent,
    ModalSearchInitComponent,
    RolesComponent,
    CreateRolesComponent,
    TreeViewComponent,
    TreeViewItemComponent,
    RegisterComponent,
    SendOtpComponent,
    TaxCodeDirective,
    ActivityHistoryComponent,
    RevenueCommonStatsComponent,
    SearchInitRevenueCommonComponent,
    ProductHotSaleComponent,
    SecurityPolicyComponent,
    AreaOrderComponent,
    InvoiceSyncEIComponent,
    InvoiceEiListComponent,
    CompanyComponent,
    CompanyModalCreateComponent,
    CompanyModalUpdateComponent,
    InvoiceSyncEIComponent,
    InvoiceEiListComponent,
    ConfirmDeleteComponent,
    AreaOrderComponent,
    PromotionUsageComponent,
    ReportProductProfitComponent,
    SearchInitProductProfitComponent,
    ProductUnitComponent,
    RolesComponent,
    CreateRolesComponent,
    TreeViewComponent,
    TreeViewItemComponent,
    ConfirmDeleteComponent,
    PromotionsComponent,
    PromotionsDetailComponent,
    PromotionSaveComponent,
    TimeFormatDirective,
    MultiDatePicker,
    PromotionUsageComponent,
    NewFeatureComponent,
    ProductVoucherQuantityComponent,
  ],
  bootstrap: [MainComponent],
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
