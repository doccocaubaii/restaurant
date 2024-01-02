import { Component, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { Router } from '@angular/router';
import appSettings from '../../config/app-settings';
import { LoginService } from './login.service';
import { db } from '../../db';
import { HttpClient, HttpResponse } from '@angular/common/http';
import Dexie from 'dexie';
import { UtilsService } from '../../utils/Utils.service';
import { BaseComponent } from '../../shared/base/base.component';
import {
  area_offline,
  customer_offline,
  last_company,
  last_print_config,
  last_user,
  printConfigs,
  product_group,
  product_offline,
} from '../../object-stores.constants';
import { AppDB } from '../../db';
// import { InvoiceService } from '../invoice/service/invoice.service';
import { forkJoin, Observable, of } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { GET_COMPANY_CONFIG, GET_OWNER_INFO } from '../../constants/api.constants';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Component({
  selector: 'login',
  templateUrl: './login.component.html',
})
export class LoginPage extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  authenticationError = false;

  loginForm: any;
  companies: any;
  companyId: any;
  errorMessage: any;

  constructor(
    private router: Router,
    private renderer: Renderer2,
    private loginService: LoginService,
    private utilsService: UtilsService,
    private http: HttpClient,
    private applicationConfigService: ApplicationConfigService
  ) {
    super();
    this.appSettings.appEmpty = true;
    this.renderer.addClass(document.body, 'bg-white');
  }

  ngOnInit() {
    this.loginForm = {};
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.renderer.removeClass(document.body, 'bg-white');
  }

  login() {
    this.loginService.login(this.loginForm).subscribe({
      next: async res => {
        this.authenticationError = false;
        if (!res.status) {
          if (res.message && res.message[0].code) {
            this.errorMessage = res.message[0].code;
          }
          this.companies = res.data.companies;
          this.loginForm.companyId = this.companies[0].id;
        } else if (res.status) {
          if (!this.router.getCurrentNavigation()) {
            // There were no routing during login (eg from navigationToStoredUrl)
            this.router.navigate(['tong-quan']);
            const db = new AppDB();
            let ownerInfo = {};
            db.printConfigs.bulkAdd([]);
            // Xử lý kết quả từ các API
            const data_last_company = {
              id: res.data.companyId,
              name: res.data.companyName,
            };
            const data_last_user = {
              id: res.data.id,
              comId: res.data.companyId,
              fullname: res.data.fullName,
              username: res.data.userName,
              jwt: res.data.id_token,
            };
            this.deleteAll(last_company);
            this.deleteAll(last_user);
            // Thêm mới data lastCompany
            this.addItem(last_company, data_last_company);
            this.addItem(last_user, data_last_user);
          }
        }
      },
      error: error => {
        console.log(error);
        this.authenticationError = true;
      },
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
          console.log(error);
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
            console.log('Dexie v1.3.0 or higher is required');
          } else {
            console.log('Database exists');
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
        console.error('Oops, an error  occurred when trying to check database existance');
      });
  }

  getOwnerInfo(comId: number): Observable<any> {
    return this.http.get<any>(this.resourceUrl + GET_OWNER_INFO + comId);
  }

  getCompanyConfig(comId: number): Observable<any> {
    return this.http.get<any>(this.resourceUrl + GET_COMPANY_CONFIG + comId);
  }
}
