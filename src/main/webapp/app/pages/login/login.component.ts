import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import appSettings from '../../config/app-settings';
import { LoginService } from './login.service';
import { HttpResponse } from '@angular/common/http';
import Dexie from 'dexie';
import { UtilsService } from '../../utils/Utils.service';
import { BaseComponent } from '../../shared/base/base.component';
import { InvoiceService } from '../invoice/service/invoice.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DeviceComponent } from '../../layouts/modal/device/device.component';
import { REGISTER } from '../../constants/app.routing.constants';
import { ContentOption } from '../../utils/contentOption';
import { CompanySessionItem } from '../../entities/companySession/CompanySessionItem.model';
import { CommonLogin } from '../../utils/common-login';
import { PasswordResetInitComponent } from 'app/account/password-reset/init/password-reset-init.component';
import { VersionUpdateService } from '../../shared/service/version_update.service';
import { StaticMapService } from '../../shared/other/StaticMapService';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import {
  ICON_ACCOUNT_INFO, ICON_EMAIL, ICON_LOCAL,
  ICON_PASSWORD_INFO,
  ICON_PASSWORD_INVISIBLE,
  ICON_PASSWORD_VISIBLE, ICON_PHONE, ICON_WEBSITE
} from "../../shared/other/icon";
@Component({
  selector: 'login',
  templateUrl: './login.component.html',
  styleUrls: ['login.component.scss'],
})
export class LoginPage extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;

  authenticationError = false;

  loginForm: any;
  notify: any;
  companies: any;
  companyId: any;
  errorMessage: any;
  ownerDevice: any;
  checkModalRef: NgbModalRef;
  isLoading: boolean;

  constructor(
    private router: Router,
    private renderer: Renderer2,
    private loginService: LoginService,
    private utilsService: UtilsService,
    private invoiceService: InvoiceService,
    private versionUpdateService: VersionUpdateService,
    private modalService: NgbModal,
    private contentOption: ContentOption,
    private commonLogin: CommonLogin,
    private staticMapService: StaticMapService,
    private toast: ToastrService,
    private translateService: TranslateService
  ) {
    super();
    this.appSettings.appEmpty = true;
    this.contentOption.isHiddenOrder = true;
    this.renderer.addClass(document.body, 'bg-white');
    this.loginService.getCompanies().subscribe(updatedCompanies => {
      this.companies = updatedCompanies;
      this.errorMessage = null;
    });
  }
  ngOnInit() {
    this.loginForm = {};
  }
  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.contentOption.isHiddenOrder = false;
    this.renderer.removeClass(document.body, 'bg-white');
  }

  companiesUser: CompanySessionItem[] = [];

  login() {
    this.isLoading = true;
    localStorage.setItem('comId', this.loginForm.companyId);
    this.loginService.login(this.loginForm).subscribe({
      next: async res => {
        this.authenticationError = false;
        if (!res.status) {
          if (res.message && res.message[0].code && res.message[0].code !== 'LOGIN_ERROR') {
            // this.errorMessage = res.message[0].code;
            if (res.message[0].code === 'USER_NOT_FOUND') {
              this.toast.error(this.translateService.instant('login.error.USER_NOT_FOUND'));
            } else {
              this.toast.error(res.message[0].message);
            }
          }
          if (res.message[0].code === 'LOGIN_ERROR') {
            this.companies = res.data && res.data.companies ? res.data.companies : null;
            if (this.companies) {
              this.loginForm.companyId = this.companies[0].id;
            }
            this.companies.forEach(c => {
              this.companiesUser?.push(new CompanySessionItem(c.id, c.name));
            });
          }
        } else if (res.status) {
          this.staticMapService.reload();
          if (!this.router.getCurrentNavigation()) {
            await this.commonLogin.insertDataLoginSuccess(res, this.companiesUser, true);
          }
        }
        this.isLoading = false;
      },
      error: error => {
        //console.log(error);
        this.isLoading = false;
        this.authenticationError = true;
      },
    });
  }

  forgot() {
    if (this.utilsService.isOnline()) {
      this.modalService.open(PasswordResetInitComponent, {
        size: 'dialog-centered',
        backdrop: 'static',
      });
      // this.router.navigate([FORGOT_PASSWORD]);
    }
  }

  openDeviceModal() {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(DeviceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
  }

  getDataOfflinePrintConfigs(comId: number) {
    return new Promise(resolve => {
      this.utilsService.getDataOfflinePrintConfigs(comId).subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineProductGroups() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflineProductGroups().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineProducts() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflineProducts().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineCustomers() {
    return new Promise(resolve => {
      this.utilsService
        .getDataOfflineCustomers({
          type: 1,
        })
        .subscribe((res: HttpResponse<any> | any) => {
          return resolve(res.body.data);
        });
    });
  }

  getDataOfflineAreas() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflineAreas().subscribe(
        (res: HttpResponse<any> | any) => {
          return resolve(res.body.data);
        },
        error => {
          //console.log(error);
        }
      );
    });
  }

  getDataOfflineSyncBill() {
    return new Promise(resolve => {
      this.utilsService
        .getDataOfflineSyncBill({
          comId: 16,
        })
        .subscribe((res: HttpResponse<any> | any) => {
          return resolve(res.body.data);
        });
    });
  }

  async populate() {
    Dexie.exists('EZPosIndexedDB')
      .then(async exists => {
        if (exists) {
          if (Dexie.version < 1.03) {
            //console.log('Dexie v1.3.0 or higher is required');
          } else {
            //console.log('Database exists');
          }
        } else {
          // const dataPrintConfigs: any = await this.getDataOfflinePrintConfigs();
          // const dataProductGroups: any = await this.getDataOfflineProductGroups();
          // const dataProducts: any = await this.getDataOfflineProducts();
          // const dataCustomers: any = await this.getDataOfflineCustomers();
          // // const dataSyncBill: any = await this.getDataOfflineSyncBill();
          // const dataAreas: any = await this.getDataOfflineAreas();
          // await db.printConfigs.bulkAdd(dataPrintConfigs);
          // await db.product_group.bulkAdd(dataProductGroups);
          // await db.product.bulkAdd(dataProducts);
          // await db.customer.bulkAdd(dataCustomers);
          // // await db.offline_bill.bulkAdd(dataSyncBill);
          // await db.area.bulkAdd(dataAreas);
        }
      })
      .catch(function (error) {
        // console.error('Oops, an error  occurred when trying to check database existance');
      });
  }

  passwordVisible = false;
  showPassword() {
    this.passwordVisible ? (this.passwordVisible = false) : (this.passwordVisible = true);
  }

  onEnter() {
    this.login();
  }

  getLink(path: string, pathOffline?: string) {
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
    }
  }

  protected readonly REGISTER = REGISTER;
  protected readonly ICON_ACCOUNT_INFO = ICON_ACCOUNT_INFO;
  protected readonly ICON_PASSWORD_INFO = ICON_PASSWORD_INFO;
  protected readonly ICON_PASSWORD_VISIBLE = ICON_PASSWORD_VISIBLE;
  protected readonly ICON_PASSWORD_INVISIBLE = ICON_PASSWORD_INVISIBLE;
  protected readonly ICON_LOCAL = ICON_LOCAL;
  protected readonly ICON_PHONE = ICON_PHONE;
  protected readonly ICON_EMAIL = ICON_EMAIL;
  protected readonly ICON_WEBSITE = ICON_WEBSITE;
}
