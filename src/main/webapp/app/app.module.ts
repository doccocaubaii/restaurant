import { LOCALE_ID, NgModule } from '@angular/core';
import { registerLocaleData } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import locale from '@angular/common/locales/en';
import { BrowserModule, Title } from '@angular/platform-browser';
import { ServiceWorkerModule } from '@angular/service-worker';
import { FaIconLibrary } from '@fortawesome/angular-fontawesome';
import { NgxWebstorageModule } from 'ngx-webstorage';
import dayjs from 'dayjs/esm';
import {
  NgbDateAdapter,
  NgbDatepickerConfig,
  NgbDatepickerModule,
  NgbTimepickerModule
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
import { DashboardPage } from './pages/dashboard/dashboard.component';
import { PosCounterCheckoutPage } from './pages/pos/counter-checkout/counter-checkout.component';
import { PosCustomerOrderPage } from './pages/pos/customer-order/customer-order.component';
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
import { NgApexchartsModule } from 'ng-apexcharts';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { NgxChartsModule } from '@swimlane/ngx-charts';
import {
  PERFECT_SCROLLBAR_CONFIG,
  PerfectScrollbarConfigInterface,
  PerfectScrollbarModule
} from 'ngx-perfect-scrollbar';
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
import { LoginPage } from './pages/login/login.component';
import { OrderPage } from './pages/don-hang/order.component';
import { MatDatepickerModule } from '@angular/material/datepicker';
import { MatNativeDateModule } from '@angular/material/core';
import { MatFormFieldModule } from '@angular/material/form-field';
import { SidebarOption } from './utils/SidebarOption';
import { NgSelectModule } from '@ng-select/ng-select';
import { ProductDetailComponent } from './pages/pos/customer-order/product-detail/product-detail.component';
import { ConfirmCheckoutComponent } from './pages/pos/customer-order/confirm-checkout/confirm-checkout.component';
import {
  DiscountTaxProductComponent
} from './pages/pos/customer-order/discount-tax-product/discount-tax-product.component';
import { ViewDetailOrderComponent } from './pages/don-hang/view-detail-order/view-detail-order.component';
import {
  ShowListActionOrderComponent
} from './pages/don-hang/view-detail-order/show-list-action-order/show-list-action-order.component';
import { ToastrModule } from 'ngx-toastr';

import { LoadingOption } from './utils/loadingOption';
import { PosInvoiceComponent } from './pages/pos/customer-order/pos-invoice/pos-invoice.component';
import { NgxCurrencyModule } from 'ngx-currency';
import { AccountModule } from './account/account.module';
import { BaseFormProductComponent } from './pages/base-form-product/base-form-product/base-form-product.component';
import {
  BaseCreateProductComponent
} from './pages/base-create-product/base-create-product/base-create-product.component';
import { KhachqrScComponent } from './pages/pos/customer-order/khachqr-sc/khachqr-sc.component';
import { RxStompService } from './rxStomp/rx-stomp.service';
import { rxStompServiceFactory } from './rxStomp/rx-stomp-service-factory';
import { WatchBillComponent } from './pages/pos/customer-order/watch-bill/watch-bill.component';
import { QrCodeComponent } from './pages/qr-code/qr-code.component';
import { StaffComponent } from './pages/staff/staff.component';
import { ModalCreateStaffComponent } from './pages/staff/modal-create-staff/modal-create-staff.component';
import { ModalDeleteStaffComponent } from './pages/staff/modal-delete-staff/modal-delete-staff.component';
import { EmailActiveComponent } from './pages/email-active/email-active.component';
import { KitchenComponent } from './pages/kitchen/kitchen.component';

const DEFAULT_PERFECT_SCROLLBAR_CONFIG: PerfectScrollbarConfigInterface = {
  suppressScrollX: true
};
FullCalendarModule.registerPlugins([dayGridPlugin, timeGridPlugin, interactionPlugin, listPlugin, bootstrapPlugin]);

// @ts-ignore
@NgModule({
  imports: [
    BrowserModule,
    SharedModule,
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
      useFactory: adapterFactory
    }),
    FormsModule,
    HighlightModule,
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
    NgxCurrencyModule
  ],

  providers: [
    Title,
    SidebarOption,
    LoadingOption,
    { provide: LOCALE_ID, useValue: 'en' },
    { provide: NgbDateAdapter, useClass: NgbDateDayjsAdapter },
    {
      provide: PERFECT_SCROLLBAR_CONFIG,
      useValue: DEFAULT_PERFECT_SCROLLBAR_CONFIG
    },
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        lineNumbersLoader: () => import('highlightjs-line-numbers.js'), // Optional, only if you want the line numbers
        languages: {
          typescript: () => import('highlight.js/lib/languages/typescript'),
          css: () => import('highlight.js/lib/languages/css'),
          xml: () => import('highlight.js/lib/languages/xml')
        }
      }
    },
    {
      provide: RxStompService,
      useFactory: rxStompServiceFactory
    },
    httpInterceptorProviders
  ],
  declarations: [
    ShowListActionOrderComponent,
    MainComponent,
    NavbarComponent,
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
    PosCustomerOrderPage,
    LoginPage,
    ViewDetailOrderComponent,
    OrderPage,
    ProductDetailComponent,
    ConfirmCheckoutComponent,
    DiscountTaxProductComponent,
    OrderPage,
    PosInvoiceComponent,
    BaseFormProductComponent,
    BaseCreateProductComponent,
    KhachqrScComponent,
    WatchBillComponent,
    QrCodeComponent,
    StaffComponent,
    ModalCreateStaffComponent,
    ModalDeleteStaffComponent,
    EmailActiveComponent,
    KitchenComponent
  ],
  bootstrap: [MainComponent]
})
export class AppModule {
  constructor(applicationConfigService: ApplicationConfigService, iconLibrary: FaIconLibrary, dpConfig: NgbDatepickerConfig) {
    applicationConfigService.setEndpointPrefix(SERVER_API_URL);
    registerLocaleData(locale);
    iconLibrary.addIcons(...fontAwesomeIcons);
    dpConfig.minDate = { year: dayjs().subtract(100, 'year').year(), month: 1, day: 1 };
  }
}
