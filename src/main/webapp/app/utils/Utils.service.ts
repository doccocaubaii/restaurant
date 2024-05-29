import { Injectable, OnInit } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { HttpClient } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { createRequestOption } from '../core/request/request-util';
import { parse } from 'date-fns';
import { BaseComponent } from '../shared/base/base.component';
import dayjs from 'dayjs/esm';

@Injectable({
  providedIn: 'root'
})
export class UtilsService extends BaseComponent implements OnInit {
  options: any = new BehaviorSubject(true);
  checkOnline = new BehaviorSubject(true);

  constructor(
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private http: HttpClient,
    protected activatedRoute: ActivatedRoute,
    public router: Router) {
    super();
  }

  async ngOnInit() {
    const x = await this.getCompany();
    this.options = {
      align: 'left',
      allowNegative: true,
      allowZero: true,
      decimal: '.',
      precision: x.roundScaleUnitPrice,
      prefix: '',
      suffix: '',
      thousands: ',',
      nullable: true,
      min: null,
      max: null,
      inputMode: 1
    };
  }

  optionCurrentCy() {
    return {
      align: 'left',
      allowNegative: true,
      allowZero: true,
      decimal: ',',
      precision: 2,
      prefix: '',
      suffix: '',
      thousands: '.'
    };
  }

  setStatusOnline(data) {
    this.checkOnline = data;
  }

  setOption(data) {
    this.options = data;
  }

  isOnline() {
    return this.checkOnline;
  }

  getOption() {
    return this.options;
  }

  getDataOfflinePrintConfigs(id?: any): Observable<any> {
    return this.http.get<any>(`${SERVER_API_URL}/api/client/page/config/get-print-config/${id}`, { observe: 'response' });
  }

  getDataOfflineProductGroups(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/client/page/product-group/get-all-for-offline', {
      observe: 'response'
    });
  }

  getDataOfflineProducts(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/client/page/product/get-all-for-offline', {
      observe: 'response'
    });
  }

  getDataOfflineCustomers(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/client/page/customer/get-all-for-offline', {
      params: options,
      observe: 'response'
    });
  }

  getDataOfflineSyncBill(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/client/page/offline-bill/sync', {
      params: options,
      observe: 'response'
    });
  }

  getDataOfflineAreas(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/client/page/area/get-all-for-offline', {
      observe: 'response'
    });
  }

  /**
   * Chuyển thời gian từ string sang date.
   *
   * @param s string thời gian có dạng "yyyy-mm-dd HH24:mm:ss"  VD:"2023-05-09 10:53:41"
   * @returns Thời gian dưới dạng date.
   */
  convertStringToDate(str: string): Date {
    var date = parse(str, 'yyyy-MM-dd HH:mm:ss', new Date());
    return date;
  }

  /**
   * Tính thời gian từ date1 dến hiện tại.
   *
   * @param date1 dạng date.
   * @returns Số milisecond từ date1 đến hiện tại.
   */
  subDate(date1: Date): number {
    return new Date().getTime() - date1.getTime();
  }

  getFromToMoment(date?: dayjs.Dayjs, isMaxDate?: boolean): any {
    const _date = date && date.isValid() ? date : isMaxDate ? null : dayjs();
    return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
  }

  getCurrentDate() {
    const _date = dayjs();
    return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
  }
}
