import { Component, ElementRef, EventEmitter, HostListener, Input, OnInit, Output, Renderer2, ViewChild } from '@angular/core';

import appSettings from '../../config/app-settings';
import { Router } from '@angular/router';
import { UtilsService } from '../../utils/Utils.service';
import { LoginService } from '../../pages/login/login.service';
import { SidebarOption } from '../../utils/SidebarOption';
import { BaseComponent } from '../../shared/base/base.component';
import { last_company, last_config_device, last_owner_device, last_user } from '../../object-stores.constants';
import { LANGUAGES } from 'app/config/language.constants';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CompanySessionItem } from '../../entities/companySession/CompanySessionItem.model';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';
import { map } from 'rxjs/operators';
import { InvoiceService } from '../../pages/invoice/service/invoice.service';
import { CommonLogin } from '../../utils/common-login';
import { HOME } from 'app/constants/app.routing.constants';
import { PasswordComponent } from '../../account/password/password.component';
import { VersionUpdateService } from '../../shared/service/version_update.service';
import { NotifyComponent } from '../modal/notify/notify.component';
import { HttpResponse } from '@angular/common/http';
import { DeviceComponent } from '../modal/device/device.component';
import { DashboardPage } from '../../pages/dashboard/dashboard.component';
import { SECURITY_POLICY } from '../../constants/app.routing.constants';
import { PERMISSIONS, EMPLOYEE } from '../../constants/app.routing.constants';
import { Authority } from '../../config/authority.constants';
import {
  ICON_CANCEL,
  ICON_CHANGE_PASSWORD,
  ICON_CHANGE_WORKING_SESSION,
  ICON_EMPLOYEE,
  ICON_LOGOUT,
  ICON_NOTIFICATION,
  ICON_ORDER,
  ICON_ROLE,
  ICON_SAVE,
  ICON_SECURITY_POLICY,
  ICON_SHOW_PROFILE,
  ICON_SHOW_SUPPORT,
  ICON_SUPPORT,
  ICON_USER,
} from '../../shared/other/icon';
import { NotificationService } from '../../pages/processing/notification.service';
import { SECURITY_POLICY_USERNAMES } from '../../constants/common.constants';

@Component({
  selector: 'jhi-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.scss'],
})
export class NavbarComponent extends BaseComponent implements OnInit {
  @Input() appSidebarTwo;
  @Output() appSidebarEndToggled = new EventEmitter<boolean>();
  @Output() appSidebarMobileToggled = new EventEmitter<boolean>();
  @Output() appSidebarEndMobileToggled = new EventEmitter<boolean>();
  @Output() appSidebarNone = new EventEmitter<boolean>();
  appSettings = appSettings;
  lastUser: any;
  lastCompany: any;
  languages = LANGUAGES;
  showSupport = false;
  showProfile = false;
  showNotification = false;
  actionDiv: any;
  actionIcon: any;
  notificationIcon: any;
  notificationDiv: any;
  notificationCount: any = '';
  notify: any;
  isShowModalDevice: boolean = false;
  ownerDevice: any;
  @ViewChild(DashboardPage) dashboardPage: DashboardPage;
  showRoles = false;
  authorRoleView = Authority.ROLE_VIEW;
  authorEmployView = Authority.EMPLOYEE_VIEW;
  authorRoleEmploy: string[] = [Authority.ROLE_VIEW, Authority.EMPLOYEE_VIEW];
  authorCOMPANY_CHANGE_SESSION = Authority.COMPANY_CHANGE_SESSION;
  authorBillView = Authority.BILL_VIEW;
  authorBillAdd = Authority.BILL_ADD;

  constructor(
    private renderer: Renderer2,
    private elementRef: ElementRef,
    protected utilsService: UtilsService,
    public sidebarOption: SidebarOption,
    private router: Router,
    private modalService: NgbModal,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private loginService: LoginService,
    protected toast: ToastrService,
    protected ngbModal: NgbModal,
    private authServerProvider: AuthServerProvider,
    private invoiceService: InvoiceService,
    private notificationService: NotificationService,
    private commonLogin: CommonLogin,
    private versionUpdateService: VersionUpdateService
  ) {
    super();
    this.lastUser = {};
    this.changeLanguage('vi');

    this.renderer.listen('document', 'click', event => {
      this.actionDiv = this.elementRef.nativeElement.querySelector('.overlay-container');
      this.actionIcon = this.elementRef.nativeElement.querySelector('#support-icon');
      this.notificationIcon = this.elementRef.nativeElement.querySelector('#notification-icon');
      this.notificationDiv = this.elementRef.nativeElement.querySelector('.overlay-notification');
      if (
        this.actionDiv &&
        this.actionIcon &&
        !this.actionDiv.contains(event.target) &&
        !this.actionIcon.contains(event.target) &&
        this.showSupport
      ) {
        this.showSupport = false;
      }
      if (
        this.notificationDiv &&
        this.notificationIcon &&
        !this.notificationDiv.contains(event.target) &&
        !this.notificationIcon.contains(event.target) &&
        this.showNotification
      ) {
        this.showNotification = false;
      }
    });
    this.utilsService.refreshLastUser$.subscribe(data => {
      this.ngOnInit();
    });
  }

  toggleAppSidebarMobile() {
    this.appSidebarMobileToggled.emit(true);
  }

  toggleAppSidebarEnd() {
    this.appSidebarEndToggled.emit(true);
  }

  toggleAppSidebarEndMobile() {
    this.appSidebarEndMobileToggled.emit(true);
  }

  toggleAppSidebarNone() {
    this.appSidebarNone.emit(false);
    this.sidebarOption.isShow = !this.sidebarOption.isShow;
  }

  toggleAppTopMenuMobile() {
    this.appSettings.appTopMenuMobileToggled = !this.appSettings.appTopMenuMobileToggled;
  }

  toggleAppHeaderMegaMenuMobile() {
    this.appSettings.appHeaderMegaMenuMobileToggled = !this.appSettings.appHeaderMegaMenuMobileToggled;
  }

  changeShowSupport() {
    this.showSupport = !this.showSupport;
  }

  ngOnDestroy() {
    this.appSettings.appTopMenuMobileToggled = false;
    this.appSettings.appHeaderMegaMenuMobileToggled = false;
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    this.actionDiv = this.elementRef.nativeElement.querySelector('.overlay-container');
    this.actionIcon = this.elementRef.nativeElement.querySelector('#support-icon');
    // this.notificationService.countUnreadNotification().subscribe((res) => {
    //   this.notificationCount = res.body.data;
    //   this.notificationService.setUnreadNotifications(this.notificationCount);
    // });
  }

  logout(): void {
    this.loginService.logout();
    this.router.navigate(['/login']);
  }

  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }

  protected readonly PERMISSIONS = PERMISSIONS;
  protected readonly EMPLOYEE = EMPLOYEE;

  changeLanguage(languageKey: string): void {
    this.sessionStorageService.store('locale', languageKey);
    this.translateService.use(languageKey ? languageKey : 'vi');
  }

  viewChat() {
    this.toast.success('Chat Comming soon');
  }

  viewNotify() {
    this.toast.success('Notification Comming soon');
  }

  private modalRef: NgbModalRef | undefined;
  @ViewChild('modalWorkingSession') modalWorkingSession: ElementRef | undefined;
  companies: CompanySessionItem[] = [];
  currentCompanyId: any;

  dismiss($event: MouseEvent) {
    this.ngbModal.dismissAll();
  }

  async changeWorkingSession() {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    if (this.lastUser.companies.length > 0) {
      this.currentCompanyId = this.lastCompany.id;
      this.modalRef = this.ngbModal.open(this.modalWorkingSession, { size: 'md', backdrop: 'static' });
      this.companies = this.lastUser.companies;
      //console.log(this.lastUser.companies)
    }
  }

  onChangeCompanySession(comId: any) {
    this.currentCompanyId = comId;
  }

  saveChangeSession() {
    if (this.currentCompanyId) {
      this.authServerProvider
        .changeSession(this.currentCompanyId)
        .pipe(
          map((res: any) => {
            if (res && !res.status) {
              return res;
            } else {
              return res;
            }
            // mergeMap(() => this.accountService.identity(true));
          })
        )
        .subscribe(
          async res => {
            this.ngbModal.dismissAll();
            await this.commonLogin.insertDataLoginSuccess(res, this.companies, false);
            this.lastCompany = await this.getCompany();
            this.lastUser = await this.getFirstItemIndexDB(last_user);
          },
          error => {
            this.toast.error('Đổi phiên làm việc thất bại');
            // console.log(error);
          }
        );
    } else {
      this.toast.error('Vui lòng chọn cửa hàng của bạn');
    }
  }

  showSecurityPolicy = false;
  onChangeShowPolicy() {
    if (this.lastUser && this.lastUser.username && this.lastUser.username === 'demo') {
      this.showSecurityPolicy = !this.showSecurityPolicy;
      this.showSupport = false;
      event?.stopPropagation();
    }
  }

  @HostListener('document:click', ['$event'])
  onHidePolicy(event: Event) {
    if (this.showSecurityPolicy) this.showSecurityPolicy = false;
    const divShowRole = document.getElementById('show-role');
    if (divShowRole && !divShowRole.contains(event.target as HTMLElement)) {
      if (this.showRoles) this.showRoles = false;
    }
  }

  protected readonly SECURITY_POLICY = SECURITY_POLICY;

  changeShowRoles() {
    this.showRoles = !this.showRoles;
  }

  ClickOrder() {
    this.router.navigate(['/easy-pos/bill-order']);
  }

  showProfileClick(): void {
    this.showProfile = !this.showProfile;
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

  protected readonly ICON_CANCEL = ICON_CANCEL;

  protected readonly ICON_ORDER = ICON_ORDER;
  protected readonly ICON_SUPPORT = ICON_SUPPORT;
  protected readonly ICON_SHOW_SUPPORT = ICON_SHOW_SUPPORT;
  protected readonly ICON_CHANGE_WORKING_SESSION = ICON_CHANGE_WORKING_SESSION;
  protected readonly ICON_USER = ICON_USER;
  protected readonly ICON_SHOW_PROFILE = ICON_SHOW_PROFILE;
  protected readonly ICON_CHANGE_PASSWORD = ICON_CHANGE_PASSWORD;
  protected readonly ICON_LOGOUT = ICON_LOGOUT;
  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_ROLE = ICON_ROLE;
  protected readonly ICON_EMPLOYEE = ICON_EMPLOYEE;
  protected readonly ICON_NOTIFICATION = ICON_NOTIFICATION;
  protected readonly SECURITY_POLICY_USERNAMES = SECURITY_POLICY_USERNAMES;
  protected readonly ICON_SECURITY_POLICY = ICON_SECURITY_POLICY;
}
