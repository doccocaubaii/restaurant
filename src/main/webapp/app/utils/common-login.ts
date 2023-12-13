import { Injectable, OnInit } from '@angular/core';
import { AppDB } from '../db';
import { catchError } from 'rxjs/operators';
import { forkJoin, lastValueFrom, of } from 'rxjs';
import {
  area,
  customer,
  last_company,
  last_config_device,
  last_owner_device,
  last_print_config,
  last_user,
  product,
  product_group,
} from '../object-stores.constants';
import { HOME } from '../constants/app.routing.constants';
import { DeviceComponent } from '../layouts/modal/device/device.component';
import { HttpResponse } from '@angular/common/http';
import { BaseComponent } from '../shared/base/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { AuthServerProvider } from '../core/auth/auth-jwt.service';
import { InvoiceService } from '../pages/invoice/service/invoice.service';
import { CompanySessionItem } from '../entities/companySession/CompanySessionItem.model';
import { Router } from '@angular/router';
import { UtilsService } from './Utils.service';
import { ToastrService } from 'ngx-toastr';
import { VersionUpdateService } from '../shared/service/version_update.service';
import { NotifyComponent } from '../layouts/modal/notify/notify.component';
import { DataEncrypt } from '../entities/indexDatabase/data-encrypt.model';

@Injectable({
  providedIn: 'root',
})
export class CommonLogin extends BaseComponent implements OnInit {
  companiesUser: CompanySessionItem[] = [];
  companies: CompanySessionItem[] = [];
  ownerDevice: any;
  notify: any;
  isShowModalDevice: boolean = false;
  private modalRef: NgbModalRef | undefined;
  newestNewFeature: any;

  constructor(
    protected ngbModal: NgbModal,
    private authServerProvider: AuthServerProvider,
    private invoiceService: InvoiceService,
    private utilsService: UtilsService,
    private versionUpdateService: VersionUpdateService,
    private router: Router,
    private toast: ToastrService
  ) {
    super();
  }

  ngOnInit(): void {}

  async insertDataLoginSuccess(req: any, companySessions: CompanySessionItem[], reloadHome: boolean) {
    this.companiesUser = companySessions;
    const comIdReq = req.data.companyId;
    // There were no routing during login (eg from navigationToStoredUrl)
    const db = new AppDB();
    // localStorage.setItem('comId', res.data.companyId);
    const api1$ = this.invoiceService.getOwnerInfo(comIdReq).pipe(
      catchError(error => {
        //console.log('Error in API 1:', error);
        return of(null); // Trả về giá trị mặc định nếu có lỗi
      })
    );
    const api2$ = this.invoiceService.getCompanyConfig(comIdReq).pipe(
      catchError(error => {
        //console.log('Error in API 2:', error);
        return of(null); // Trả về giá trị mặc định nếu có lỗi
      })
    );
    db.printConfigs.bulkAdd([]);

    let [ownerInfo, config] = await lastValueFrom(forkJoin(api1$, api2$));
    const data_last_company = {
      id: req && req.data ? comIdReq : null,
      name: req && req.data ? req.data.companyName : null,
      isParent: req.data.isParent,
      taxcode: req && req.data ? req.data.taxCode : null,
      ownerName: req && req.data ? req.data.fullName : null,
      ownerId: req && req.data ? req.data.ownerId : null,
      phoneNo: req && req.data ? req.data.phoneNo : null,
      address: req && req.data ? req.data.companies.find(x => x.id === comIdReq).address : null,
      taxAuthCodePrefix: ownerInfo ? ownerInfo.data.taxRegisterCode : null,
      deviceName: req && req.data ? req.data.deviceName : null,
      deviceCode: req && req.data ? req.data.deviceCode : null,
      easyinvoiceUrl: ownerInfo && ownerInfo.data ? ownerInfo.data.easyInvoiceUrl : null,
      easyinvoiceAccount: ownerInfo && ownerInfo.data ? ownerInfo.data.easyInvoiceAccount : null,
      easyinvoicePass: ownerInfo && ownerInfo.data ? ownerInfo.data.easyInvoicePass : null,
      invoicePattern: config && config.data ? config.data.invoicePattern : null,
      invoiceType: config && config.data ? config.data.invoiceType : null,
      invoiceMethod: config && config.data ? config.data.invoiceMethod : null,
      discountType: config && config.data ? config.data.typeDiscount : null,
      roundScaleAmount: ownerInfo && ownerInfo.data ? ownerInfo.data.roundScaleAmount : null,
      roundScaleUnitPrice: ownerInfo && ownerInfo.data ? ownerInfo.data.roundScaleUnitPrice : null,
      roundScaleQuantity: ownerInfo && ownerInfo.data ? ownerInfo.data.roundScaleQuantity : null,
      createTime: req && req.data ? req.data.createTime : null,
      updateTime: req && req.data ? req.data.updateTime : null,
      service: req && req.data ? req.data.service : null,
    };
    const data_last_user = {
      id: req.data.id,
      comId: comIdReq,
      isParent: req.data.isParent,
      fullname: req.data.fullName,
      username: req.data.userName,
      jwt: req.data.id_token,
      role: req.data.role,
      permissions: req.data.permissions,
      companies: this.companiesUser,
      comName: req.data.companyName,
    };
    // Thêm mới data lastCompany
    await this.deleteAll(last_company);
    await this.deleteAll(last_user);
    await this.addItem(last_company, this.genDataEncrypt(data_last_company));
    await this.addItem(last_user, this.genDataEncrypt(data_last_user));
    if (reloadHome) {
      const returnUrl = localStorage.getItem('returnUrl') || HOME; // Lấy đường dẫn trước đó hoặc trang mặc định
      let indexOf = returnUrl.indexOf('?');
      if (indexOf && indexOf >= 0) {
        this.router.navigate([returnUrl.substring(0, indexOf)]);
      } else {
        this.router.navigate([returnUrl]);
      }
    } else {
      // đổi phiên làm việc
      this.utilsService.statusConnectingWebSocket.next(true);
      const currentUrl = localStorage.getItem('currentUrl') || HOME;
      this.router.navigateByUrl('/', { skipLocationChange: true }).then(() => {
        this.router.navigateByUrl(currentUrl); // Chuyển hướng đến URL hiện tại
      });
    }
    this.notify = await this.getNotify();
    if (this.notify && this.notify.length > 0) {
      this.openNotifyModal();
    }
    this.newestNewFeature = await this.getNewestNewFeature();
    this.versionUpdateService.newestNewFeature = this.newestNewFeature;
    this.ownerDevice = await this.findByID(last_owner_device, req.data.id);
    if (!this.ownerDevice) {
      this.isShowModalDevice = true;
      if (!this.notify || this.notify.length <= 0) {
        this.openDeviceModal();
      }
    } else {
      const dataCompanyUpdate = {
        ...data_last_company,
        deviceCode: this.ownerDevice.deviceCode,
        deviceName: this.ownerDevice.name,
      };
      await this.updateById(last_company, req.data.companyId, this.genDataEncrypt(dataCompanyUpdate));
    }
    const id = await this.findByID(last_config_device, comIdReq);
    if (!id) {
      const data_config_device = {
        id: comIdReq,
        inventoryTracking: true,
      };
      await this.addItem(last_config_device, this.genDataEncrypt(data_config_device));
    }

    this.deleteAll(last_print_config);
    this.deleteAll(product_group);
    this.deleteAll(customer);
    this.deleteAll(area);
    this.deleteAll(product);
    const dataPrintConfigs: any = await this.getDataOfflinePrintConfigs(comIdReq).catch(err => []);
    // const dataProductGroups: any = await this.getDataOfflineProductGroups().catch(err => []);
    // const dataProducts: any = await this.getDataOfflineProducts().catch(err => []);
    // const dataCustomers: any = await this.getDataOfflineCustomers().catch(err => []);
    // const dataAreas: any = await this.getDataOfflineAreas().catch(err => []);
    await db.last_print_config.bulkPut(JSON.parse(JSON.stringify(this.genDataEncrypt(dataPrintConfigs))));
    // await db.product_group.bulkPut(dataProductGroups);
    // await db.customer.bulkPut(dataCustomers);
    // await db.area.bulkPut(dataAreas);
    // await db.product.bulkPut(dataProducts);
  }

  openDeviceModal() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.ngbModal.open(DeviceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
  }
  openNotifyModal() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    const modalRef1 = this.ngbModal.open(NotifyComponent, {
      size: 'lg m-650',
      backdrop: 'static',
      windowClass: 'margin-5',
      centered: true,
    });
    modalRef1.componentInstance.notifies = this.notify;
    modalRef1.closed.subscribe(res => {
      if (this.isShowModalDevice) {
        this.openDeviceModal();
      }
    });
  }

  getDataOfflinePrintConfigs(comId: number) {
    return new Promise((resolve, reject) => {
      this.utilsService.getDataOfflinePrintConfigs(comId).subscribe(
        (res: HttpResponse<any> | any) => {
          resolve(res.body.data);
        },
        error => {
          reject(null);
        }
      );
    });
  }
  getNotify() {
    return new Promise(resolve => {
      this.versionUpdateService.getVersionUpdate().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineProductGroups() {
    return new Promise((resolve, reject) => {
      this.utilsService.getDataOfflineProductGroups().subscribe(
        (res: HttpResponse<any> | any) => {
          resolve(res.body.data);
        },
        error => {
          reject(null);
        }
      );
    });
  }

  getDataOfflineProducts() {
    return new Promise((resolve, reject) => {
      this.utilsService.getDataOfflineProducts().subscribe(
        (res: HttpResponse<any> | any) => {
          resolve(res.body.data);
        },
        error => {
          reject(null);
        }
      );
    });
  }

  getDataOfflineCustomers() {
    return new Promise((resolve, reject) => {
      this.utilsService
        .getDataOfflineCustomers({
          type: 1,
        })
        .subscribe(
          (res: HttpResponse<any> | any) => {
            resolve(res.body.data);
          },
          error => {
            reject(null);
          }
        );
    });
  }

  getDataOfflineAreas() {
    return new Promise((resolve, reject) => {
      this.utilsService.getDataOfflineAreas().subscribe(
        (res: HttpResponse<any> | any) => {
          resolve(res.body.data);
        },
        error => {
          reject(null);
        }
      );
    });
  }

  genDataEncrypt(value: any) {
    if (value) {
      if (value instanceof Array && value.length > 0) {
        let result: DataEncrypt[] = [];
        value.forEach(item => result.push(new DataEncrypt(item.id, this.encryptFromData(item))));
        return result;
      } else {
        return new DataEncrypt(value.id, this.encryptFromData(value));
      }
    }
    return null;
  }

  getNewestNewFeature() {
    return new Promise(resolve => {
      this.versionUpdateService.getNewestNewFeature().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }
}
