import {
  Component,
  Input,
  Output,
  EventEmitter,
  ElementRef,
  HostListener,
  ViewChild,
  OnInit,
  AfterViewChecked,
  AfterViewInit,
  Renderer2,
  ViewEncapsulation,
} from '@angular/core';
import * as global from '../../config/globals';
import appMenus from '../../config/app-menus';
import appSettings from '../../config/app-settings';
import { slideUp } from '../composables/slideUp.js';
import { slideToggle } from '../composables/slideToggle.js';
import { Authority } from '../../config/authority.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { LoginService } from '../../pages/login/login.service';
import { AccountService } from '../../core/auth/account.service';
import { Account } from '../../core/auth/account.model';
import { UtilsService } from '../../utils/Utils.service';
import { SidebarOption } from '../../utils/SidebarOption';
import { BaseComponent } from '../../shared/base/base.component';
import { last_user } from '../../object-stores.constants';
import {
  AREA,
  AREA_UNIT,
  BILL,
  BILL_OFFLINE,
  BILL_ORDER,
  BILL_ORDER_OFFLINE,
  CUSTOMER,
  EMPLOYEE,
  HOME,
  INVOICE,
  INVOICE_CONFIG,
  COMPANY,
  PRODUCT,
  PRODUCT_GROUP,
  RECEIPT_PAYMENT,
  RS_INOUT_WARD,
  CHANGE_PASSWORD,
  REPORT_PRODUCT_SALES,
  REPORT_INVENTORY_STATS,
  VOUCHER,
  CARD,
  CARD_POLICY,
  PRODUCT_TOPPING,
  DESIGN_CONFIGURATION,
  PROCESSING,
  PROCESSING_AREA_LIST,
  REPORT_ACTIVITY_HISTORY,
  REPORT_REVENUE_COMMON,
  REPORT_PRODUCT_HOT_SALES,
  SECURITY_POLICY,
  INVOICE_EI,
  REPORT_PRODUCT_PROFIT,
  PRODUCT_UNIT,
  PROMOTIONS,
} from '../../constants/app.routing.constants';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../pages/order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { IStaff } from '../../entities/staff/staff.model';
import { CreateStaffComponent } from '../../pages/staff/create-staff/create-staff.component';
import { PasswordComponent } from '../../account/password/password.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { PreviewInventoryStatsComponent } from '../../pages/report/inventory-stats/preview/preview-inventory-stats.component';
import { InventoryStatsService } from '../../pages/report/inventory-stats/inventory-stats.service';
import { ModalSearchInitComponent } from '../../pages/report/product-sales-stats/modal-search-init/modal-search-init.component';
import { SearchInitRevenueCommonComponent } from '../../pages/report/revenue-common-stats/modal-search-init/modal-search-init.component';
import { SearchInitProductProfitComponent } from '../../pages/report/report-product-profit/search-init-product-profit/search-init-product-profit.component';
import { InvoiceConfiguration } from '../../pages/order/model/bill-payment.model';
import { lastValueFrom, Subscription } from 'rxjs';
import { EventManager, EventWithContent } from '../../core/util/event-manager.service';
import { InvoiceService } from '../../pages/invoice/service/invoice.service';
import { AlertError } from '../../shared/alert/alert-error.model';
import {
  EXTEND,
  ICON_ACCOUNT,
  ICON_AREA,
  ICON_AREA_UNIT,
  ICON_BILL,
  ICON_BILL_ORDER,
  ICON_CARD_POLICY,
  ICON_CHANGE_PASSWORD,
  ICON_COMPANY,
  ICON_CUSTOMER,
  ICON_DESIGN_CONFIG,
  ICON_EMPLOYEE,
  ICON_GROUP_CART,
  ICON_GROUP_CUSTOMER,
  ICON_GROUP_PRODUCT,
  ICON_GROUP_REPORT,
  ICON_GRPOUP_INVOICE_CONFIG,
  ICON_HOME,
  ICON_INVENTORY_STATS,
  ICON_INVOICE,
  ICON_INVOICE_CONFIG,
  ICON_LIST_CARD,
  ICON_LOGOUT,
  ICON_PROCESSING,
  ICON_PROCESSING_AREA,
  ICON_PRODUCT,
  ICON_PRODUCT_GROUP,
  ICON_PRODUCT_TOPPING,
  ICON_PRODUCT_UNIT,
  ICON_RECEIPT_PAYMENT,
  ICON_REPORT_ACTIVITY_HISTORY,
  ICON_REPORT_HOT_SALE,
  ICON_REPORT_PRODUCT_PROFIT,
  ICON_REPORT_PRODUCT_SALES,
  ICON_REPORT_REVENUE,
  ICON_RS_INOUT_WARD,
  ICON_SAVE,
  ICON_SECURITY_POLICY,
  ICON_TABLE_AREA,
  ICON_VOUCHER,
  LOGO,
} from '../../shared/other/icon';

@Component({
  selector: 'sidebar',
  templateUrl: './sidebar.component.html',
  styleUrls: ['./sidebar.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class SidebarComponent extends BaseComponent implements OnInit, AfterViewChecked {
  @ViewChild('sidebarScrollbar', { static: false }) private sidebarScrollbar: any | ElementRef;
  @Output() appSidebarMinifiedToggled = new EventEmitter<boolean>();
  @Output() hideMobileSidebar = new EventEmitter<boolean>();
  @Output() setPageFloatSubMenu = new EventEmitter();
  @Output() appSidebarEndMobileToggled = new EventEmitter<boolean>();
  @Output() appSidebarNone = new EventEmitter<boolean>();

  @Output() appSidebarMobileToggled = new EventEmitter<boolean>();
  @Input() appSidebarTransparent;
  @Input() appSidebarGrid;
  @Input() appSidebarFixed;
  @Input() appSidebarMinified;

  authorProductView = Authority.PRODUCT_VIEW;
  authorGroupView = Authority.GROUP_VIEW;
  authorTopping = Authority.TOPPING_VIEW;
  authorBillView = Authority.BILL_VIEW;
  authorBillAdd = Authority.BILL_ADD;
  authorInvoiceView = Authority.INVOICE_VIEW;
  authorVouchesView = Authority.VOUCHER_VIEW;
  authorAreaUnitView = Authority.AREA_UNIT_VIEW;
  authorProcessingAreaView = Authority.PROCESSING_AREA_VIEW;
  authorAreaView = Authority.AREA_VIEW;
  authorRevenueView = Authority.REVENUE_VIEW;
  authorExpenseView = Authority.EXPENSE_VIEW;
  authorCustomerView = Authority.CUSTOMER_VIEW;
  authorCardView = Authority.CARD_VIEW;
  authorCardPolicyView = Authority.CARD_POLICY_VIEW;
  authorKitchenView = Authority.KITCHEN_VIEW;
  authorEmployeeView = Authority.EMPLOYEE_VIEW;
  authorRsInWardView = Authority.RS_INWARD_VIEW;
  authorRsOutWardView = Authority.RS_OUTWARD_VIEW;
  authorRoleView = Authority.ROLE_VIEW;
  authorCOMPANY_CONFIG_VIEW = Authority.COMPANY_CONFIG_VIEW;
  authorDESIGN_CONFIGURATION_VIEW = Authority.DESIGN_CONFIGURATION_VIEW;
  authorInOutWard: string[] = [Authority.RS_INWARD_VIEW, Authority.RS_OUTWARD_VIEW];
  authorRECEIPT_PAYMENT: string[] = [Authority.REVENUE_VIEW, Authority.EXPENSE_VIEW];
  authorProductManagement: string[] = [Authority.GROUP_VIEW, Authority.PRODUCT_VIEW, Authority.TOPPING_VIEW];
  authorAreaManagement: string[] = [Authority.AREA_UNIT_VIEW, Authority.AREA_VIEW, Authority.PROCESSING_AREA_VIEW];
  authorCardManagement: string[] = [Authority.CARD_VIEW, Authority.CARD_POLICY_VIEW];
  isAuthorCardManagement = false;
  authorInvoiceConfig: string[] = [Authority.COMPANY_CONFIG_VIEW, Authority.DESIGN_CONFIGURATION_VIEW];
  authorCustomerManagement: string[] = [Authority.CUSTOMER_VIEW];
  authorReportView = Authority.REPORT_VIEW;
  authorCompanyView = Authority.COMPANY_VIEW;
  authorVoucherManagement: string[] = [Authority.VOUCHER_VIEW];
  authorEASY_INVOICE_CONFIG_VIEW = Authority.EASY_INVOICE_CONFIG_VIEW;
  authorInvoiceManagement: string[] = [Authority.INVOICE_VIEW, Authority.EASY_INVOICE_CONFIG_VIEW];
  menus: any = appMenus;
  appSettings: any = appSettings;
  appSidebarFloatSubMenu;
  appSidebarFloatSubMenuHide;
  appSidebarFloatSubMenuHideTime = 250;
  appSidebarFloatSubMenuTop;
  appSidebarFloatSubMenuLeft = '60px';
  appSidebarFloatSubMenuRight;
  appSidebarFloatSubMenuBottom;
  appSidebarFloatSubMenuArrowTop;
  appSidebarFloatSubMenuArrowBottom;
  appSidebarFloatSubMenuLineTop;
  appSidebarFloatSubMenuLineBottom;
  appSidebarFloatSubMenuOffset;

  mobileMode;
  desktopMode;
  scrollTop;
  currentAccount: any;
  lastCompany: any;
  lastUser: any;
  latestUrl: string;
  private modalRef: NgbModalRef;
  isShowCompanyConfig = true;
  invoiceConfiguration: InvoiceConfiguration;
  httpListener: Subscription;
  mapAuthority: Map<string, any>;
  constructor(
    private eRef: ElementRef,
    private invoiceService: InvoiceService,
    private eventManager: EventManager,
    private accountService: AccountService,
    public utilsService: UtilsService,
    public sidebarOption: SidebarOption,
    private router: Router,
    private loginService: LoginService,
    private service: InventoryStatsService,
    private modalService: NgbModal,
    private route: Router,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
    this.utilsService.statusConnectingWebSocket$.subscribe(data => {
      this.latestUrl = HOME;
    });
    // sidebarOption.isShow = false;
    if (window.innerWidth <= 767) {
      this.mobileMode = true;
      this.desktopMode = false;
    } else {
      this.mobileMode = false;
      this.desktopMode = true;
    }
    this.httpListener = eventManager.subscribe('easyPosFrontEndApp.saveConfig', (response: EventWithContent<unknown> | string) => {
      let configs = JSON.parse((response as EventWithContent<AlertError>).content.message);
      this.isShowCompanyConfig = this.checkConditionCompanyConfig(configs);
    });
  }

  toggleSidebarMobileOpen() {}

  async ngOnInit() {
    const pathname = location.pathname;
    this.latestUrl = pathname.slice(1, pathname.length);
    this.lastCompany = await this.getCompany();
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    this.mapAuthority = this.covertStringArrToMap(this.lastUser.permissions);
    this.isAuthorCardManagement = this.mapAuthority.has(Authority.CARD_VIEW) || this.mapAuthority.has(Authority.CARD_POLICY_VIEW);
    this.accountService.identity().subscribe(account => (this.currentAccount = account));

    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
    this.invoiceConfiguration.displayConfig = JSON.parse(this.invoiceConfiguration.displayConfig);
    this.isShowCompanyConfig = this.checkConditionCompanyConfig(this.invoiceConfiguration?.displayConfig);
  }

  toggleNavProfile(e: any) {
    e.preventDefault();

    var targetSidebar = <HTMLElement>document.querySelector('.app-sidebar:not(.app-sidebar-end)');
    var targetMenu = e.target.closest('.menu-profile');
    var targetProfile = <HTMLElement>document.querySelector('#appSidebarProfileMenu');
    var expandTime = targetSidebar && targetSidebar.getAttribute('data-disable-slide-animation') ? 0 : 250;

    if (targetProfile && targetProfile.style) {
      if (targetProfile.style.display == 'block') {
        targetMenu.classList.remove('active');
      } else {
        targetMenu.classList.add('active');
      }
      slideToggle(targetProfile, expandTime);
      targetProfile.classList.toggle('expand');
    }
  }

  toggleAppSidebarMinified() {
    this.appSidebarMinifiedToggled.emit(true);
    this.scrollTop = 40;
  }

  toggleAppSidebarMobile() {
    this.appSidebarMobileToggled.emit(true);
  }

  calculateAppSidebarFloatSubMenuPosition() {
    var targetTop = this.appSidebarFloatSubMenuOffset.top;
    var direction = document.body.style.direction;
    var windowHeight = window.innerHeight;

    setTimeout(() => {
      let targetElm = <HTMLElement>document.querySelector('.app-sidebar-float-submenu-container');
      let targetSidebar = <HTMLElement>document.getElementById('sidebar');
      var targetHeight = targetElm.offsetHeight;
      this.appSidebarFloatSubMenuRight = 'auto';
      this.appSidebarFloatSubMenuLeft = this.appSidebarFloatSubMenuOffset.width + targetSidebar.offsetLeft + 'px';

      if (windowHeight - targetTop > targetHeight) {
        this.appSidebarFloatSubMenuTop = this.appSidebarFloatSubMenuOffset.top + 'px';
        this.appSidebarFloatSubMenuBottom = 'auto';
        this.appSidebarFloatSubMenuArrowTop = '20px';
        this.appSidebarFloatSubMenuArrowBottom = 'auto';
        this.appSidebarFloatSubMenuLineTop = '20px';
        this.appSidebarFloatSubMenuLineBottom = 'auto';
      } else {
        this.appSidebarFloatSubMenuTop = 'auto';
        this.appSidebarFloatSubMenuBottom = '0';

        var arrowBottom = windowHeight - targetTop - 21;
        this.appSidebarFloatSubMenuArrowTop = 'auto';
        this.appSidebarFloatSubMenuArrowBottom = arrowBottom + 'px';
        this.appSidebarFloatSubMenuLineTop = '20px';
        this.appSidebarFloatSubMenuLineBottom = arrowBottom + 'px';
      }
    }, 0);
  }

  showAppSidebarFloatSubMenu(menu, e) {
    if (this.appSettings.appSidebarMinified) {
      clearTimeout(this.appSidebarFloatSubMenuHide);

      this.appSidebarFloatSubMenu = menu;
      this.appSidebarFloatSubMenuOffset = e.target.getBoundingClientRect();
      this.calculateAppSidebarFloatSubMenuPosition();
    }
  }

  hideAppSidebarFloatSubMenu() {
    this.appSidebarFloatSubMenuHide = setTimeout(() => {
      this.appSidebarFloatSubMenu = '';
    }, this.appSidebarFloatSubMenuHideTime);
  }

  remainAppSidebarFloatSubMenu() {
    clearTimeout(this.appSidebarFloatSubMenuHide);
  }

  appSidebarSearch(e: any) {
    var targetValue = e.target.value;
    targetValue = targetValue.toLowerCase();

    if (targetValue) {
      var elms = [].slice.call(
        document.querySelectorAll(
          '.app-sidebar:not(.app-sidebar-end) .menu > .menu-item:not(.menu-profile):not(.menu-header):not(.menu-search), .app-sidebar:not(.app-sidebar-end) .menu-submenu > .menu-item'
        )
      );
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.add('d-none');
        });
      }
      var elms = [].slice.call(document.querySelectorAll('.app-sidebar:not(.app-sidebar-end) .has-text'));
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.remove('has-text');
        });
      }
      var elms = [].slice.call(document.querySelectorAll('.app-sidebar:not(.app-sidebar-end) .expand'));
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.remove('expand');
        });
      }
      var elms = [].slice.call(
        document.querySelectorAll(
          '.app-sidebar:not(.app-sidebar-end) .menu > .menu-item:not(.menu-profile):not(.menu-header):not(.menu-search) > .menu-link, .app-sidebar .menu-submenu > .menu-item > .menu-link'
        )
      );
      if (elms) {
        elms.map(function (elm: any) {
          var targetText = elm.textContent;
          targetText = targetText.toLowerCase();
          if (targetText.search(targetValue) > -1) {
            var targetElm = elm.closest('.menu-item');
            if (targetElm) {
              targetElm.classList.remove('d-none');
              targetElm.classList.add('has-text');
            }

            var targetElm = elm.closest('.menu-item.has-sub');
            if (targetElm) {
              var targetElm = targetElm.querySelector('.menu-submenu .menu-item.d-none');
              if (targetElm) {
                targetElm.classList.remove('d-none');
              }
            }

            var targetElm = elm.closest('.menu-submenu');
            if (targetElm) {
              targetElm.style.display = 'block';

              var targetElm = targetElm.querySelector('.menu-item:not(.has-text)');
              if (targetElm) {
                targetElm.classList.add('d-none');
              }

              var targetElm = elm.closest('.has-sub:not(.has-text)');
              if (targetElm) {
                targetElm.classList.remove('d-none');
                targetElm.classList.add('expand');

                var targetElm = targetElm.closest('.has-sub:not(.has-text)');
                if (targetElm) {
                  targetElm.classList.remove('d-none');
                  targetElm.classList.add('expand');
                }
              }
            }
          }
        });
      }
    } else {
      var elms = [].slice.call(
        document.querySelectorAll(
          '.app-sidebar:not(.app-sidebar-end) .menu > .menu-item:not(.menu-profile):not(.menu-header):not(.menu-search).has-sub .menu-submenu'
        )
      );
      if (elms) {
        elms.map(function (elm: any) {
          elm.removeAttribute('style');
        });
      }

      var elms = [].slice.call(
        document.querySelectorAll(
          '.app-sidebar:not(.app-sidebar-end) .menu > .menu-item:not(.menu-profile):not(.menu-header):not(.menu-search)'
        )
      );
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.remove('d-none');
        });
      }

      var elms = [].slice.call(document.querySelectorAll('.app-sidebar:not(.app-sidebar-end) .menu-submenu > .menu-item'));
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.remove('d-none');
        });
      }

      var elms = [].slice.call(document.querySelectorAll('.app-sidebar:not(.app-sidebar-end) .expand'));
      if (elms) {
        elms.map(function (elm: any) {
          elm.classList.remove('expand');
        });
      }
    }
  }

  @HostListener('scroll', ['$event'])
  onScroll(event) {
    this.scrollTop = this.appSettings.appSidebarMinified ? event.srcElement.scrollTop + 40 : 0;
    if (typeof Storage !== 'undefined') {
      localStorage.setItem('sidebarScroll', event.srcElement.scrollTop);
    }
  }

  @HostListener('window:resize', ['$event'])
  onResize(event) {
    if (window.innerWidth <= 767) {
      this.mobileMode = true;
      this.desktopMode = false;
    } else {
      this.mobileMode = false;
      this.desktopMode = true;
    }
  }

  ngAfterViewChecked() {
    if (typeof Storage !== 'undefined' && localStorage.sidebarScroll) {
      if (this.sidebarScrollbar && this.sidebarScrollbar.nativeElement) {
        this.sidebarScrollbar.nativeElement.scrollTop = localStorage.sidebarScroll;
      }
    }
  }

  ngAfterViewInit() {
    var handleSidebarMenuToggle = function (menus, expandTime) {
      menus.map(function (menu) {
        menu.onclick = function (e) {
          e.preventDefault();
          var target = this.nextElementSibling;
          if (target) {
            menus.map(function (m) {
              var otherTarget = m.nextElementSibling;
              if (otherTarget && otherTarget !== target) {
                slideUp(otherTarget, expandTime);
                otherTarget.closest('.menu-item').classList.remove('expand');
                otherTarget.closest('.menu-item').classList.add('closed');
              }
            });

            var targetItemElm = target.closest('.menu-item');

            if (targetItemElm.classList.contains('expand') || (targetItemElm.classList.contains('active') && !target.style.display)) {
              targetItemElm.classList.remove('expand');
              targetItemElm.classList.add('closed');
              // slideToggle(target, expandTime);
            } else {
              targetItemElm.classList.add('expand');
              targetItemElm.classList.remove('closed');
              // slideToggle(target, expandTime);
            }
          }
        };
      });
    };

    var targetSidebar = document.querySelector('.app-sidebar:not(.app-sidebar-end)');
    var expandTime = targetSidebar && targetSidebar.getAttribute('data-disable-slide-animation') ? 0 : 300;
    var disableAutoCollapse = targetSidebar && targetSidebar.getAttribute('data-disable-auto-collapse') ? 1 : 0;

    var menuBaseSelector = '.app-sidebar .menu > .menu-item.has-sub';
    var submenuBaseSelector = ' > .menu-submenu > .menu-item.has-sub';

    // menu
    var menuLinkSelector = menuBaseSelector + ' > .menu-link';
    var menus = [].slice.call(document.querySelectorAll(menuLinkSelector));
    handleSidebarMenuToggle(menus, expandTime);

    // submenu lvl 1
    var submenuLvl1Selector = menuBaseSelector + submenuBaseSelector;
    var submenusLvl1 = [].slice.call(document.querySelectorAll(submenuLvl1Selector + ' > .menu-link'));
    handleSidebarMenuToggle(submenusLvl1, expandTime);

    // submenu lvl 2
    var submenuLvl2Selector = menuBaseSelector + submenuBaseSelector + submenuBaseSelector;
    var submenusLvl2 = [].slice.call(document.querySelectorAll(submenuLvl2Selector + ' > .menu-link'));
    handleSidebarMenuToggle(submenusLvl2, expandTime);
  }

  expandCollapseSubmenu(e: any, currentMenu: any, allMenu: any) {
    var targetItem = e.target.closest('.menu-item');
    var target = <HTMLElement>targetItem.querySelector('.menu-submenu');
    slideToggle(target);
  }

  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
      this.latestUrl = path;
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
      this.latestUrl = pathOffline;
    }
  }

  toggleAppSidebarNone() {
    this.appSidebarNone.emit(false);
    this.sidebarOption.isShow = !this.sidebarOption.isShow;
  }

  protected readonly HOME = HOME;
  protected readonly BILL = BILL;
  protected readonly AREA = AREA;
  protected readonly BILL_OFFLINE = BILL_OFFLINE;
  protected readonly PRODUCT = PRODUCT;
  protected readonly PRODUCT_UNIT = PRODUCT_UNIT;
  protected readonly CUSTOMER = CUSTOMER;
  protected readonly PRODUCT_GROUP = PRODUCT_GROUP;
  protected readonly AREA_UNIT = AREA_UNIT;

  protected readonly RECEIPT_PAYMENT = RECEIPT_PAYMENT;
  protected readonly EMPLOYEE = EMPLOYEE;
  protected readonly INVOICE_CONFIG = INVOICE_CONFIG;
  protected readonly INVOICE = INVOICE;
  protected readonly COMPANY = COMPANY;
  protected readonly RS_INOUT_WARD = RS_INOUT_WARD;
  protected readonly BILL_ORDER = BILL_ORDER;
  protected readonly BILL_ORDER_OFFLINE = BILL_ORDER_OFFLINE;
  protected readonly CHANGE_PASSWORD = CHANGE_PASSWORD;
  protected readonly PRODUCT_TOPPING = PRODUCT_TOPPING;
  protected readonly VOUCHER = VOUCHER;
  protected readonly DESIGN_CONFIGURATION = DESIGN_CONFIGURATION;
  protected readonly PROCESSING = PROCESSING;

  protected readonly PROCESSING_AREA_LIST = PROCESSING_AREA_LIST;
  protected readonly PROMOTIONS = PROMOTIONS;

  protected readonly REPORT_PRODUCT_SALES = REPORT_PRODUCT_SALES;

  protected readonly REPORT_ACTIVITY_HISTORY = REPORT_ACTIVITY_HISTORY;
  protected readonly REPORT_REVENUE_COMMON = REPORT_REVENUE_COMMON;
  protected readonly REPORT_PRODUCT_HOT_SALES = REPORT_PRODUCT_HOT_SALES;
  protected readonly REPORT_PRODUCT_PROFIT = REPORT_PRODUCT_PROFIT;
  protected readonly REPORT_INVENTORY_STATS = REPORT_INVENTORY_STATS;
  logout() {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  openPasswordChange() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    // this.modalRef = this.modalService.open(CreateStaffComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef = this.modalService.open(PasswordComponent);
    this.modalRef.result.then(
      result => {
        this.getLink(HOME);
      },
      reason => {}
    );
  }

  openPreviewPopup() {
    this.modalRef = this.modalService.open(PreviewInventoryStatsComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    this.service.resetAllParam();
  }

  protected readonly CARD = CARD;
  protected readonly CARD_POLICY = CARD_POLICY;

  scrollToBottom(id: string): void {
    setTimeout(() => {
      this.scrollToTargets(id);
    }, 0);
  }

  scrollToTargets(id: string): void {
    const targetElement = document.querySelector(`#${id}`);
    targetElement && targetElement.scrollIntoView({ behavior: 'smooth', block: 'center' });
  }

  onSearchInitProductSales(event: any) {
    this.modalRef = this.modalService.open(ModalSearchInitComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate && res.toDate) {
        event.stopPropagation();
        this.route.navigate([`/${REPORT_PRODUCT_SALES}`], {
          relativeTo: this.activatedRoute,
          queryParams: {
            period: res.period,
            year: res.fromDate.year(),
            fromDate: res.fromDate,
            toDate: res.toDate,
            pattern: res.pattern,
            status: res.status,
            taxCheckStatus: res.taxCheckStatus,
          },
        });
      }
    });
  }

  onSearchInitRevenueCommon(event: any) {
    this.modalRef = this.modalService.open(SearchInitRevenueCommonComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate && res.toDate) {
        event.stopPropagation();
        this.route.navigate([`/${REPORT_REVENUE_COMMON}`], {
          relativeTo: this.activatedRoute,
          queryParams: {
            period: res.period,
            fromDate: res.fromDate,
            toDate: res.toDate,
            type: res.type,
            isHours: res.checkHours,
            fromHour: res.fromHour,
            toHour: res.toHour,
          },
        });
      }
    });
  }

  protected readonly SECURITY_POLICY = SECURITY_POLICY;
  protected readonly INVOICE_EI = INVOICE_EI;

  onSearchInitProductProfit(event: any) {
    this.modalRef = this.modalService.open(SearchInitProductProfitComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate && res.toDate) {
        event.stopPropagation();
        this.route.navigate([`/${REPORT_PRODUCT_PROFIT}`], {
          relativeTo: this.activatedRoute,
          queryParams: {
            period: res.period,
            fromDate: res.fromDate,
            toDate: res.toDate,
            paramCheckAll: res.paramCheckAll,
            paramCheckAllPage: res.paramCheckAllPage,
            listSelected: JSON.stringify(res.listSelected),
            keywordName: res.keywordName,
            keywordUnit: res.keywordUnit,
            sortType: res.sortType ? res.sortType : null,
          },
        });
      }
    });
  }

  checkConditionCompanyConfig(configs: any[]) {
    let displayPrint = configs.find(config => config.code == 'CI/CD');
    let displayOrder = configs.find(config => config.code == 'BH/CD');
    let displayInvoice = configs.find(config => config.code == 'HD/CD');
    if (displayPrint?.active || displayOrder?.active || displayInvoice?.active) {
      return true;
    } else {
      return false;
    }
  }

  hasAuthority(authorities: string[] | string): boolean {
    if (!Array.isArray(authorities)) {
      authorities = [authorities];
    }
    // const permission = this.covertArr(this.lastUser.permissions);
    for (const item of authorities) {
      if (this.mapAuthority.has(item)) {
        return true;
      }
    }
    return false;
    // const check = permission.some((authority: string) => authorities.includes(authority.trim()));
    // return check;
  }

  covertArr(permissions: any) {
    const cleanedPermissions = permissions.slice(1, -1);
    const permissionsArray = cleanedPermissions.split(',');
    return permissionsArray;
  }

  covertStringArrToMap(permissions: any) {
    const dataArr = permissions
      .slice(1, -1)
      .split(', ')
      .map(item => item.trim());

    // Create a Map
    const resultMap = new Map<string, any>();

    // Populate the map
    for (const item of dataArr) {
      resultMap.set(item, null); // You can set values if needed
    }
    return resultMap;
  }

  protected readonly LOGO = LOGO;
  protected readonly EXTEND = EXTEND;
  protected readonly ICON_HOME = ICON_HOME;
  protected readonly ICON_BILL_ORDER = ICON_BILL_ORDER;
  protected readonly ICON_GROUP_PRODUCT = ICON_GROUP_PRODUCT;
  protected readonly ICON_PRODUCT_GROUP = ICON_PRODUCT_GROUP;
  protected readonly ICON_PRODUCT = ICON_PRODUCT;
  protected readonly ICON_PRODUCT_TOPPING = ICON_PRODUCT_TOPPING;
  protected readonly ICON_PRODUCT_UNIT = ICON_PRODUCT_UNIT;
  protected readonly ICON_RS_INOUT_WARD = ICON_RS_INOUT_WARD;
  protected readonly ICON_BILL = ICON_BILL;
  protected readonly ICON_INVOICE = ICON_INVOICE;
  protected readonly ICON_COMPANY = ICON_COMPANY;
  protected readonly ICON_VOUCHER = ICON_VOUCHER;
  protected readonly ICON_AREA = ICON_AREA;
  protected readonly ICON_AREA_UNIT = ICON_AREA_UNIT;
  protected readonly ICON_TABLE_AREA = ICON_TABLE_AREA;
  protected readonly ICON_PROCESSING_AREA = ICON_PROCESSING_AREA;
  protected readonly ICON_RECEIPT_PAYMENT = ICON_RECEIPT_PAYMENT;
  protected readonly ICON_PROCESSING = ICON_PROCESSING;
  protected readonly ICON_GROUP_CUSTOMER = ICON_GROUP_CUSTOMER;
  protected readonly ICON_CUSTOMER = ICON_CUSTOMER;
  protected readonly ICON_EMPLOYEE = ICON_EMPLOYEE;
  protected readonly ICON_GROUP_CART = ICON_GROUP_CART;
  protected readonly ICON_LIST_CARD = ICON_LIST_CARD;
  protected readonly ICON_CARD_POLICY = ICON_CARD_POLICY;
  protected readonly ICON_GRPOUP_INVOICE_CONFIG = ICON_GRPOUP_INVOICE_CONFIG;
  protected readonly ICON_INVOICE_CONFIG = ICON_INVOICE_CONFIG;
  protected readonly ICON_DESIGN_CONFIG = ICON_DESIGN_CONFIG;
  protected readonly ICON_GROUP_REPORT = ICON_GROUP_REPORT;
  protected readonly ICON_REPORT_PRODUCT_SALES = ICON_REPORT_PRODUCT_SALES;
  protected readonly ICON_REPORT_PRODUCT_PROFIT = ICON_REPORT_PRODUCT_PROFIT;
  protected readonly ICON_REPORT_HOT_SALE = ICON_REPORT_HOT_SALE;
  protected readonly ICON_REPORT_ACTIVITY_HISTORY = ICON_REPORT_ACTIVITY_HISTORY;
  protected readonly ICON_INVENTORY_STATS = ICON_INVENTORY_STATS;
  protected readonly ICON_ACCOUNT = ICON_ACCOUNT;
  protected readonly ICON_CHANGE_PASSWORD = ICON_CHANGE_PASSWORD;
  protected readonly ICON_SECURITY_POLICY = ICON_SECURITY_POLICY;
  protected readonly ICON_LOGOUT = ICON_LOGOUT;
  protected readonly ICON_REPORT_REVENUE = ICON_REPORT_REVENUE;
}
