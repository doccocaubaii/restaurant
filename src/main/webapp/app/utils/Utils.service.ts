import { Component, EventEmitter, Injectable, OnInit } from '@angular/core';
import { BehaviorSubject, Observable, timeout } from 'rxjs';
import { TranslateService } from '@ngx-translate/core';
import { SessionStorageService } from 'ngx-webstorage';
import { HttpClient, HttpParams } from '@angular/common/http';
import { StateStorageService } from '../core/auth/state-storage.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ApplicationConfigService } from '../core/config/application-config.service';
import { PING } from '../constants/api.constants';
import { catchError } from 'rxjs/operators';
import { LoadingBarConfig, LoadingBarService } from '@ngx-loading-bar/core';
import { createRequestOption } from '../core/request/request-util';
import { last_company } from '../object-stores.constants';
import { parse } from 'date-fns';
import { BaseComponent } from '../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { Title } from '@angular/platform-browser';
import { NgbDate, NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { DATE_FORMAT, DATE_FORMAT_DMY } from '../config/input.constants';
import { Dayjs } from 'dayjs';
import { outputAst } from '@angular/compiler';
import moment, { Moment } from 'moment';
import {
  BaoCao,
  BaoCaoDong,
  CentimeterToPixel,
  Max_Row_Count,
  percentLandScape,
  percentPortrait,
  PicaPointToPixel,
  ViTri,
} from '../app.constants';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { IAccountingObject } from '../shared/model/accounting-object.model';
import { ICurrency } from '../shared/model/currency.model';
import { DatePipe } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import * as QRCode from 'qrcode';
import { DOW, DOW_VALUE } from '../constants/times.constants';
import { StaticMapService } from '../shared/other/StaticMapService';

@Injectable({
  providedIn: 'root',
})
export class UtilsService extends BaseComponent implements OnInit {
  options: any = new BehaviorSubject(true);
  currency: ICurrency[];
  statisticsCode: IStatisticsCode[];
  dtBeginTemp: any;
  dtEndTemp: any;
  dtBeginDate: any;
  dtEndDate: any;
  objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
  autoCallApi: any;
  statusConnectingWebSocket = new BehaviorSubject<any>(false);
  statusConnectingWebSocket$ = this.statusConnectingWebSocket.asObservable();

  refreshLastUser = new BehaviorSubject<any>(false);
  refreshLastUser$ = this.refreshLastUser.asObservable();

  constructor(
    private parserFormatter: NgbDateParserFormatter,
    private translateService: TranslateService,
    private sessionStorageService: SessionStorageService,
    private http: HttpClient,
    protected activatedRoute: ActivatedRoute,
    public router: Router,
    public loadingBar: LoadingBarService,
    private titleService: Title,
    private applicationService: ApplicationConfigService,
    private translate: TranslateService,
    private datepipe: DatePipe,
    private toastr: ToastrService,
    private mapService: StaticMapService
  ) {
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
      inputMode: 1,
    };
    this.setStatusOnline(true);
  }

  checkOnline = new BehaviorSubject(true);

  optionCurrentCy() {
    return {
      align: 'left',
      allowNegative: true,
      allowZero: true,
      decimal: ',',
      precision: 2,
      prefix: '',
      suffix: '',
      thousands: '.',
    };
  }

  addCommas(inputValue: number): string {
    // Định dạng số bằng cách thêm dấu ',' vào mỗi 3 chữ số
    return inputValue.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  }
  removeCommas(inputValue: string): number {
    // Loại bỏ dấu ',' khỏi chuỗi và chuyển thành số
    return Number(inputValue.replace(/,/g, ''));
  }

  ping(): Observable<any> {
    const options = {
      params: new HttpParams(),
      timeout: 2000, // set thời gian 10s
    };
    this.loadingBar.stop();
    return this.http.get('api' + PING, options).pipe(
      timeout(2000), // set thời gian 10s
      catchError(error => {
        this.loadingBar.complete();
        return 'NOT_CONNECTED_SERVER';
      })
    );
  }

  scrollToTargets(): void {
    const targetElements = document.querySelectorAll('.scroll-target');
    targetElements.forEach(el => {
      el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    });
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
      observe: 'response',
    });
  }

  getDataOfflineProducts(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/client/page/product/get-all-for-offline', {
      observe: 'response',
    });
  }

  getDataOfflineCustomers(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/client/page/customer/get-all-for-offline', {
      params: options,
      observe: 'response',
    });
  }

  getDataOfflineSyncBill(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get(SERVER_API_URL + 'api/client/page/offline-bill/sync', {
      params: options,
      observe: 'response',
    });
  }

  getDataOfflineAreas(): Observable<any> {
    return this.http.get(SERVER_API_URL + 'api/client/page/area/get-all-for-offline', {
      observe: 'response',
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

  convertStringToDateFormat(str: string, format: string): any {
    const date: Date = parse(str, format, new Date());
    return {
      year: date.getFullYear(),
      month: date.getMonth() + 1,
      day: date.getDate(),
    };
    // return date;
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
    if (date && Object.keys(date).length !== 0) {
      const date1 = dayjs(date);
      return {
        year: date1.year(),
        month: date1.month() + 1,
        day: date1.date(),
      };
    }
    const _date = isMaxDate ? null : dayjs();
    return _date ? { year: _date.year(), month: _date.month() + 1, day: _date.date() } : null;
  }

  getCurrentDate() {
    const _date = dayjs();
    return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
  }

  convertDate(date: any): any {
    if (date && Object.keys(date).length !== 0) {
      if (date.year) {
        const date1 = dayjs(date);
        return date1.format(DATE_FORMAT);
      }
      const value = moment(date, DATE_FORMAT_DMY);
      return value.format(DATE_FORMAT);
    } else {
      return '';
    }
  }

  convertNgbDateToDayjs(ngbDate: NgbDateStruct): any {
    if (ngbDate) {
      return `${padNumber(ngbDate.day)}-${padNumber(ngbDate.month)}-${ngbDate.year || ''}`;
    }
  }

  convertDayjsToNgbDate(dayjs1: dayjs.Dayjs) {
    return {
      year: dayjs1.year(),
      month: dayjs1.month() + 1, // NgbDate month is 1-based
      day: dayjs1.date(),
    };
  }

  checkAll(objectList: any, listSelected: any, paramCheckAll: boolean, paramCheckAllPage?: boolean) {
    if (objectList) {
      objectList.forEach(n => (n.check = paramCheckAll));
      listSelected.splice(0, listSelected.length);
    }
  }

  notCheckAll(objectList: any, listSelected: any, paramCheckAll: boolean) {
    paramCheckAll = false;
    if (objectList) {
      objectList.forEach(n => (n.check = paramCheckAll));
      listSelected.splice(0, listSelected.length);
    }
  }

  checkAllForPage(objectList: any[], listSelected: any, paramCheckAllPage: boolean) {
    if (paramCheckAllPage) {
      objectList.forEach(n => {
        n.check = true;
        if (!listSelected.includes(n.id)) {
          listSelected.push(n.id);
        }
      });
    } else {
      objectList.forEach(n => (n.check = false));
      objectList.forEach(n => {
        if (listSelected.includes(n.id)) {
          listSelected.splice(listSelected.indexOf(n.id), 1);
        }
      });
      // listSelected.splice(0, listSelected.length);
    }
  }

  check(object: any, listSelected: any, paramCheckAll: boolean, selectedItem: any) {
    object.check = !object.check;
    if (object.check) {
      selectedItem = object;
    }
    if (paramCheckAll) {
      // this.isShowRecord = false;
      if (!object.check) {
        listSelected.push(object.id);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] === object.id) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
    } else {
      if (object.check) {
        listSelected.push(object.id);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] === object.id) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
      // this.isShowRecord = listSelected.length <= 1;
    }
  }

  getApiReport(typeReport) {
    let api = this.applicationService.getEndpointFor('');
    switch (typeReport) {
      case BaoCaoDong.TYPE_PREVIEW_PROFIT:
        api += 'api/client/page/dynamic-report/profit-stats';
        break;
      default:
        api += 'api/client/page/dynamic-report/profit-stats';
        break;
    }
    return api;
  }

  isPackageDemo(account) {
    if (account) {
      return account.ebPackage.packageCode.toUpperCase().includes('admin1');
    }
  }

  additionalInfoReport(reportConfigs?, isGetInfoGroupName?, isGetInfoSum?, listGroup?) {
    const obj = Object.assign({});
    obj.firstOrder = reportConfigs.map(n => n.order).reduce((a, b) => Math.min(a, b));
    // Cột đầu tiên có groupName
    if (isGetInfoGroupName) {
      const listOrder = reportConfigs.filter(a => a.groupName).map(n => n.order);
      let firstOrderGroupName = 0;
      if (listOrder && listOrder.length > 0) {
        firstOrderGroupName = listOrder.reduce((a, b) => Math.min(a, b));
        obj.colSpanGroup = reportConfigs.filter(a => a.order !== null && a.order < firstOrderGroupName).length;
      }
      // Cột cuối cùng có groupName
      const listLastOrder = reportConfigs.filter(a => a.groupName).map(n => n.order);
      if (listLastOrder && listLastOrder.length > 0) {
        const lastOrderGroupName = listLastOrder.reduce((a, b) => Math.max(a, b));
        // số cột colspan từ cột đầu tiên đến cột đầu tiên Tính tổng theo group hoặc cột đầu tiên có groupName
        // List cột sau cột cuối cùng có GroupName ( Dùng cho groupBy )
      }
    }
    if (isGetInfoSum) {
      const listSum = [];
      listGroup.forEach(n => {
        const config = reportConfigs.find(a => a.dbColumn === n);
        if (config) {
          // @ts-ignore
          listSum.push(config);
        }
      });
      // @ts-ignore
      const listFirstOrderSum = listSum.map(n => n.order);
      if (listFirstOrderSum && listFirstOrderSum.length > 0) {
        const firstOrderSum = listFirstOrderSum.reduce((a, b) => Math.min(a, b));
        if (firstOrderSum !== null && firstOrderSum !== undefined) {
          obj.fromFirstToFirstColumnSum = reportConfigs.filter(a => a.order !== null && a.order < firstOrderSum).length;
          obj.listAfterLastColumnGroup = reportConfigs
            .filter(a => a.order > firstOrderSum && listGroup.indexOf(a.dbColumn) === -1)
            .map(a => a.dbColumn);
        }
      } else {
        obj.fromFirstToFirstColumnSum = 1;
        obj.listAfterLastColumnGroup = reportConfigs.filter(a => a.order > 1).map(a => a.dbColumn);
      }
      // @ts-ignore
      const listLastOrderSum = listSum.map(n => n.order);
      if (listLastOrderSum && listLastOrderSum.length > 0) {
        const lastOrderSum = listLastOrderSum.reduce((a, b) => Math.max(a, b));
        // @ts-ignore
        const lastDBColumnSum = listSum.find(a => a.order === lastOrderSum).dbColumn;
        if (lastOrderSum !== null && lastOrderSum !== undefined) {
          const lastOrderGroupName = reportConfigs.filter(a => a.dbColumn === lastDBColumnSum).order;
          obj.listAfterLastColumnSum = reportConfigs.filter(a => a.order > lastOrderGroupName).map(a => a.dbColumn);
        }
      }
      // Cột cuối cùng có groupName
    }
    // tên DbColumn đầu tiên trong list config
    obj.firstDbColumn = reportConfigs.find(a => a.order && a.order === obj.firstOrder).dbColumn;
    return obj;
  }

  isAsync(sizeData: number): Boolean {
    return sizeData >= Max_Row_Count;
  }

  getJSONDisplayReportConfig(displayReportConfig) {
    let newPaperSize = '';
    let orientation = '';
    switch (displayReportConfig.paperSize) {
      case 1:
        newPaperSize = 'A4';
        orientation = 'Portrait';
        break;
      case 2:
        newPaperSize = 'A4';
        orientation = 'Landscape';
        break;
      case 3:
        newPaperSize = 'A3';
        orientation = 'Portrait';
        break;
      case 4:
        newPaperSize = 'A3';
        orientation = 'Landscape';
        break;
      case 5:
        newPaperSize = 'A2';
        orientation = 'Portrait';
        break;
      case 6:
        newPaperSize = 'A2';
        orientation = 'Landscape';
        break;
      case 7:
        newPaperSize = 'A1';
        orientation = 'Portrait';
        break;
      case 8:
        newPaperSize = 'A1';
        orientation = 'Landscape';
        break;
      case 9:
        newPaperSize = 'A0';
        orientation = 'Portrait';
        break;
      case 10:
        newPaperSize = 'A0';
        orientation = 'Landscape';
        break;
    }
    const displayReportConfigNew = {
      id: displayReportConfig.id,
      companyID: displayReportConfig.companyID,
      reportType: displayReportConfig.reportType,
      marginTop: displayReportConfig.marginTop,
      marginBottom: displayReportConfig.marginBottom,
      marginLeft: displayReportConfig.marginLeft,
      marginRight: displayReportConfig.marginRight,
      paperSize: newPaperSize,
      orient: orientation,
      orgInfoWidth: displayReportConfig.orgInfoWidth,
    };
    return "\n <div style='display: none'>" + JSON.stringify(displayReportConfigNew) + '</div>';
  }

  concatHtmlCss(html?) {
    let css =
      '.note-title {' +
      '   display: -webkit-box;' +
      '}' +
      '.dynamic-table-print {' +
      '   border: 1px solid black;' +
      '   border-collapse: collapse;' +
      '}' +
      '.dynamic-table-print td {' +
      '    ' +
      '    border: none;' +
      '    border-right: 1px solid black !important;' +
      '    border-top: 1px solid black !important;' +
      '    border-bottom: 1px solid black !important;' +
      '}' +
      '' +
      '.dynamic-table-print th {' +
      '    ' +
      '    border: none;' +
      '    border-right: 1px solid black !important;' +
      '    border-top: 1px solid black !important;' +
      '    border-bottom: 1px solid black !important;' +
      '}' +
      '' +
      '.dynamic-table-print tr:nth-child(even) {' +
      '    background-color: #ffffff;' +
      '}' +
      '' +
      '.dynamic-table-print tr:nth-child(odd) {' +
      '    background-color: #ffffff;' +
      '}' +
      '.dynamic-td, .dynamic-td-non-data, .dynamic-td-no-data {' +
      '    word-break: break-word;' +
      '    padding: 2px 2px 2px 5px !important;' +
      '    min-width: 50px !important;' +
      '    white-space: inherit !important;' +
      '    min-height: 28px !important;' +
      '}' +
      '.dynamic-table,' +
      '.dynamic-table > table {' +
      '    min-width: 100%;' +
      '}' +
      '.dynamic-table {' +
      '   width: 100%;' +
      '   border: 1px solid black;' +
      '   border-collapse: collapse;' +
      '}' +
      'body {' +
      'margin: 0px;' +
      '}' +
      '.header-dynamic-report {' +
      'padding: 5px 0px 20px 0px;' +
      '}' +
      '.header-dynamic-report-print {' +
      'padding: 5px 0px 5px 0px;' +
      '}' +
      '' +
      '.dynamic-th {' +
      '    word-break: break-word;' +
      '    padding: 2px 2px 2px 5px;' +
      '    min-width: 50px !important;' +
      '    text-align: center;' +
      '    background: white !important;' +
      '    color: black;' +
      '    border-right: 1px solid black; !important;' +
      '}' +
      '' +
      '.dynamic-table td {' +
      '    ' +
      '    border: none;' +
      '    border-right: 1px solid black; !important;' +
      '    border-top: 1px solid black; !important;' +
      '    border-bottom: 1px solid black; !important;' +
      '}' +
      '' +
      '.dynamic-table tr:nth-child(even) {' +
      '    background-color: #ffffff;' +
      '}' +
      '' +
      '.dynamic-table tr:nth-child(odd) {' +
      '    background-color: #ffffff;' +
      '}' +
      '' +
      '.text-center {' +
      '    text-align: center;' +
      '}' +
      '' +
      '.text-left {' +
      '    text-align: left;' +
      '}' +
      '' +
      '.text-right {' +
      '    text-align: right;' +
      '}' +
      '' +
      '' +
      '.nameReport {' +
      '    display: block;' +
      '    text-align: center;' +
      '    font-weight: 600;' +
      '    font-size: 22px;' +
      "    font-family: 'Times New Roman', Times, serif;" +
      '}' +
      '' +
      '.period {' +
      '    display: block;' +
      '    text-align: center;' +
      '    font-size: 13px;' +
      '    font-style: italic;' +
      "    font-family: 'Times New Roman', Times, serif;" +
      '}' +
      '' +
      '.headerSetting {' +
      'text-align: left;' +
      'font-size: 12px;' +
      "font-family: 'Times New Roman', Times, serif;" +
      '}' +
      '' +
      '.circulars {' +
      '   text-align: center !important;' +
      '    font-size: 12px;' +
      '    width: 20%;' +
      '}' +
      '' +
      '.header {' +
      '    padding: 5px 10px 20px 10px;' +
      '}' +
      '' +
      '.footer {' +
      '    background: transparent;' +
      '    border: none;' +
      '    font-size: 12px;' +
      "    font-family: 'Times New Roman', Times, serif;" +
      '}' +
      '.footer-dynamic-report {' +
      '    background: transparent;' +
      '    border: none;' +
      '    font-size: 12px;' +
      '    padding-top: 15px;' +
      "    font-family: 'Times New Roman', Times, serif;" +
      '}' +
      '.text-center {' +
      '    text-algin: center;' +
      '}' +
      '' +
      '.bold-line {' +
      '    font-weight: bold;' +
      '}' +
      '' +
      '' +
      '.bg-report {' +
      '    background-color: white;' +
      '}' +
      '' +
      '.flex-wrap {' +
      '    display: flex;' +
      '    flex-wrap: wrap;' +
      '    justify-content: space-between;' +
      '}' +
      '' +
      'thead { display: table-header-group; }' +
      'tfoot { display: table-row-group; }' +
      '.tr-group, .footer-dynamic-report {' +
      '    page-break-inside: avoid !important;' +
      '}' +
      ' ' +
      '.th-bd-bt {' +
      '   border-bottom: 1px solid #adc8e6;' +
      '}' +
      ' ' +
      '' +
      '.th-bd-bt-template {' +
      '    border-bottom: 1px solid #adc8e6;' +
      '}' +
      '' +
      '.label-left {' +
      '   width: 33.33%;' +
      '}' +
      ' ' +
      '.label-center {' +
      '   width: 33.33%;' +
      '   text-align: center;' +
      '}' +
      ' ' +
      '.new-page {' +
      '    page-break-before: always;' +
      '}' +
      '' +
      '.bold-line-header {' +
      ' font-weight: bold;' +
      ' }' +
      '' +
      '.header-dynamic-report-font {' +
      ' background: transparent;' +
      ' border: none;' +
      '}' +
      ' ' +
      '.final-chapter {' +
      '   grid-area: chapter;' +
      '   position: relative;' +
      '   overflow: hidden;' +
      '}' +
      ' ' +
      '.final-chapter::after {' +
      '   position: absolute;' +
      '   padding-left: .25ch;' +
      '   content: " ..........................................................................................................................................."' +
      '            "............................................................................................................................................";' +
      '   text-align: right;' +
      '}' +
      '' +
      '.input-border {' +
      '    ' +
      '    font-weight: 400;' +
      '    min-width: 50px;' +
      '    text-align: center;' +
      '    background: white !important;' +
      '    color: black;' +
      '    border-right: 1px solid black; !important;' +
      '    border-bottom: 1px solid black; !important;' +
      '    border-top: 1px solid black; !important;' +
      '}' +
      ' .dynamic-td-no-data {' +
      '    min-height: 28px !important;' +
      '    height: 28px;' +
      '}' +
      ' .dynamic-td-non-data {' +
      '    min-height: 20px !important;' +
      '    height: 20px;' +
      '} ';
    css = '<html><meta charset="utf-8"><style type="text/css">' + css + '</style>';
    html = html.replace('<!--.*?-->', '');
    return css + html;
  }

  reCalReportConfigs(dRDisplayReportConfig?, reportConfigs?) {
    let widthPaper = this.getWidthPaperSize(dRDisplayReportConfig.paperSize);
    let test2 = 0;
    if (dRDisplayReportConfig.paperSize === 1) {
      test2 = 2;
    } else if (dRDisplayReportConfig.paperSize === 2) {
      // test2 = 46;
    } else if (dRDisplayReportConfig.paperSize === 3) {
      // test2 = 36;
    } else {
      // test2 = 45;
    }
    widthPaper =
      widthPaper -
      parseFloat(dRDisplayReportConfig.marginLeft) * CentimeterToPixel -
      parseFloat(dRDisplayReportConfig.marginRight) * CentimeterToPixel -
      test2;
    let sum = 0;
    const max = reportConfigs
      .filter(a => a.isDisplay)
      .map(q => q.order)
      .reduce((a, b) => Math.max(a, b));
    reportConfigs
      .filter(a => a.isDisplay)
      .forEach(a => {
        sum += parseFloat(a.width);
      });
    const report = reportConfigs.filter(a => a.isDisplay);
    let test = 0;
    if (sum > widthPaper) {
      const widthRedundancy = sum - widthPaper;
      const averageWidth = widthRedundancy / report.length;
      if (averageWidth > 1) {
        for (let i = 0; i < reportConfigs.length; i++) {
          if (reportConfigs[i].isDisplay && reportConfigs[i].width > 50) {
            if (max !== reportConfigs[i].order) {
              reportConfigs[i].width -= Math.ceil(averageWidth);
              test += Math.ceil(averageWidth);
            } else {
              reportConfigs[i].width -= Math.floor(widthRedundancy - test);
            }
          }
        }
      }
    } else {
      const widthRedundancy = widthPaper - sum;
      const averageWidth = widthRedundancy / report.length;
      if (averageWidth > 1) {
        for (let i = 0; i < reportConfigs.length; i++) {
          if (reportConfigs[i].isDisplay) {
            if (max !== reportConfigs[i].order) {
              reportConfigs[i].width += Math.floor(averageWidth);
              test += Math.floor(averageWidth);
            } else {
              reportConfigs[i].width += Math.floor(widthRedundancy - test);
            }
          }
        }
      }
    }
    return reportConfigs;
  }

  checkUseGroupTheSameItem(reportConfigs: any[], requestReport: any) {
    if (reportConfigs && requestReport) {
      if (requestReport.groupTheSameItem) {
        reportConfigs = reportConfigs.filter(a => (a.dbColumn && !a.dbColumn.includes('FieldDetail')) || !a.dbColumn);
      }
    }
    return reportConfigs;
  }

  getWidthPaperSize(paperSize) {
    let totalWidth = 0;
    switch (paperSize) {
      case 1:
      case 12:
        totalWidth = 21 * CentimeterToPixel;
        break;
      case 2:
      case 3:
        totalWidth = 29.7 * CentimeterToPixel;
        break;
      case 4:
      case 5:
        totalWidth = 42 * CentimeterToPixel;
        break;
      case 6:
      case 7:
        totalWidth = 59.4 * CentimeterToPixel;
        break;
      case 8:
      case 9:
        totalWidth = 84.1 * CentimeterToPixel;
        break;
      case 10:
        totalWidth = 118.9 * CentimeterToPixel;
        break;
      case 11:
        totalWidth = 14.8 * CentimeterToPixel;
        break;
    }
    return totalWidth;
  }

  calWidthOrg(displayReportConfig?) {
    let widthPercentage = '';
    switch (displayReportConfig.paperSize) {
      case 1:
      case 12:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 21) * 100 * 100) / 75) * percentPortrait + '%';
        break;
      case 2:
      case 4:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 29.7) * 100 * 100) / 75) * percentLandScape + '%';
        break;
      case 3:
      case 5:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 42) * 100 * 100) / 75) * percentPortrait + '%';
        break;
      case 6:
      case 7:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 59.4) * 100 * 100) / 75) * percentLandScape + '%';
        break;
      case 8:
      case 9:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 84.1) * 100 * 100) / 75) * percentPortrait + '%';
        break;
      case 10:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 118.9) * 100 * 100) / 75) * percentLandScape + '%';
        break;
      case 11:
        widthPercentage = (((parseFloat(displayReportConfig.orgInfoWidth) / 14.8) * 100 * 100) / 75) * percentPortrait + '%';
        break;
    }
    return widthPercentage;
  }

  calWidthReportRoot2(reportConfigsGroup, reportConfigsNonGroup) {
    let width = 0;
    const configs: any = reportConfigsGroup.map(object => ({ ...object }));
    configs.push(...reportConfigsNonGroup);
    // if (reportConfigsGroup) {
    //     reportConfigsGroup.filter(a => a.groupName).forEach(group => {
    //         if (group.width) {
    //             width += group.width;
    //         }
    //     });
    // }
    if (configs) {
      configs.forEach(group => {
        if (group.rootWidth) {
          width += group.rootWidth;
        }
      });
    }
    return width;
  }

  calWidthReport(reportConfigsGroup, reportConfigsNonGroup) {
    let width = 0;
    // if (reportConfigsGroup) {
    //     reportConfigsGroup.filter(a => a.groupName).forEach(group => {
    //         if (group.width) {
    //             width += group.width;
    //         }
    //     });
    // }
    if (reportConfigsNonGroup) {
      reportConfigsNonGroup.forEach(group => {
        if (group.width) {
          width += group.width;
        }
      });
    }
    return width;
  }

  getStyleConfig(reportConfigFont?, viTri?, isCheckSoAm?, data?, mauSoAm?) {
    let style: any;
    if (viTri && reportConfigFont) {
      if (viTri === ViTri.TenCCTC) {
        style = {
          'font-family': reportConfigFont.headerOrgFont,
          'font-size.px': reportConfigFont.headerOrgFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerOrgFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerOrgFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerOrgFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.TenThemCCTC) {
        style = {
          'font-family': reportConfigFont.headerAddInfoOrgFont,
          'font-size.px': reportConfigFont.headerAddInfoOrgFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerAddInfoOrgFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerAddInfoOrgFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerAddInfoOrgFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.TieuDeChinh) {
        style = {
          'font-family': reportConfigFont.titleFont,
          'font-size.px': reportConfigFont.titleFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.titleFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.titleFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.titleFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.TieuDePhu) {
        style = {
          'font-family': reportConfigFont.subTitleFont,
          'font-size.px': reportConfigFont.subTitleFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.subTitleFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.subTitleFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.subTitleFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.TieuDeCot) {
        style = {
          'font-family': reportConfigFont.headerFont,
          'font-size.px': reportConfigFont.headerFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.NoiDungKieuChu) {
        style = {
          'font-family': reportConfigFont.bodyTextFont,
          'font-size.px': reportConfigFont.bodyTextFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodyTextFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodyTextFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodyTextFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.NoiDungKieuSo) {
        style = {
          'font-family': reportConfigFont.bodyNumberFont,
          'font-size.px': reportConfigFont.bodyNumberFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodyNumberFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodyNumberFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodyNumberFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.NoiDungTongCong) {
        style = {
          'font-family': reportConfigFont.bodySumFont,
          'font-size.px': reportConfigFont.bodySumFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodySumFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodySumFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodySumFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.CuoiTrangKieuChu) {
        style = {
          'font-family': reportConfigFont.footerTextFont,
          'font-size.px': reportConfigFont.footerTextFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.footerTextFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.footerTextFontItalic ? 'Italic' : 'normal',
          'line-height': '1.5',
          'text-decoration': reportConfigFont.footerTextFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.CuoiTrangTongCong) {
        style = {
          'font-family': reportConfigFont.footerSumFont,
          'font-size.px': reportConfigFont.footerSumFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.footerSumFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.footerSumFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.footerSumFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.NgayThang) {
        style = {
          'font-family': reportConfigFont.dayMonthFont,
          'font-size.px': reportConfigFont.dayMonthFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.dayMonthFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.dayMonthFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.dayMonthFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.ChucDanh) {
        style = {
          'font-family': reportConfigFont.positionFont,
          'font-size.px': reportConfigFont.positionFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.positionFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.positionFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.positionFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.LoaiKy) {
        style = {
          'font-family': reportConfigFont.typeSignedFont,
          'font-size.px': reportConfigFont.typeSignedFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.typeSignedFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.typeSignedFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.typeSignedFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.ChuKy) {
        style = {
          'font-family': reportConfigFont.signedFont,
          'font-size.px': reportConfigFont.signedFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.signedFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.signedFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.signedFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.SoChungTu) {
        style = {
          'font-family': reportConfigFont.headerNoFont,
          'font-size.px': reportConfigFont.headerNoFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerNoFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerNoFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerNoFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.PhanChungKieuChu) {
        style = {
          'font-family': reportConfigFont.generalTextFont,
          'font-size.px': reportConfigFont.generalTextFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.generalTextFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.generalTextFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.generalTextFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.PhanChungKieuSo) {
        style = {
          'font-family': reportConfigFont.generalNumberFont,
          'font-size.px': reportConfigFont.generalNumberFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.generalNumberFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.generalNumberFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.generalNumberFontUnderline ? 'underline' : 'none',
        };
      } else if (viTri === ViTri.NoiDungTongCongCustom) {
        style = {
          'font-family': reportConfigFont.bodySumFont,
          'font-size.px': reportConfigFont.bodySumFontSize * PicaPointToPixel,
          'font-weight': 'normal',
          'font-style': reportConfigFont.bodySumFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodySumFontUnderline ? 'underline' : 'none',
        };
      }
    }
    if (isCheckSoAm && data && (data.toString().startsWith('-') || data.toString().startsWith('('))) {
      style.color = mauSoAm;
    }
    if (style) {
      style.padding = '0px';
      style['white-space'] = 'inherit';
      style['height'] = 'auto';
    }
    return style;
  }

  getStyleConfig2(reportConfigFont?, viTri?, width?) {
    let style: any;
    if (viTri && reportConfigFont) {
      if (viTri === ViTri.TenCCTC) {
        style = {
          'font-family': reportConfigFont.headerOrgFont,
          'font-size.px': reportConfigFont.headerOrgFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerOrgFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerOrgFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerOrgFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.TenThemCCTC) {
        style = {
          'font-family': reportConfigFont.headerAddInfoOrgFont,
          'font-size.px': reportConfigFont.headerAddInfoOrgFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerAddInfoOrgFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerAddInfoOrgFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerAddInfoOrgFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.TieuDeChinh) {
        style = {
          'font-family': reportConfigFont.titleFont,
          'font-size.px': reportConfigFont.titleFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.titleFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.titleFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.titleFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.TieuDePhu) {
        style = {
          'font-family': reportConfigFont.subTitleFont,
          'font-size.px': reportConfigFont.subTitleFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.subTitleFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.subTitleFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.subTitleFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.TieuDeCot) {
        style = {
          'font-family': reportConfigFont.headerFont,
          'font-size.px': reportConfigFont.headerFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.headerFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.headerFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.headerFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.NoiDungKieuChu) {
        style = {
          'font-family': reportConfigFont.bodyTextFont,
          'font-size.px': reportConfigFont.bodyTextFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodyTextFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodyTextFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodyTextFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.NoiDungKieuSo) {
        style = {
          'font-family': reportConfigFont.bodyNumberFont,
          'font-size.px': reportConfigFont.bodyNumberFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodyNumberFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodyNumberFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodyNumberFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.NoiDungTongCong) {
        style = {
          'font-family': reportConfigFont.bodySumFont,
          'font-size.px': reportConfigFont.bodySumFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.bodySumFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.bodySumFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.bodySumFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.CuoiTrangKieuChu) {
        style = {
          'font-family': reportConfigFont.footerTextFont,
          'font-size.px': reportConfigFont.footerTextFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.footerTextFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.footerTextFontItalic ? 'Italic' : 'normal',
          'line-height': '1.5',
          'text-decoration': reportConfigFont.footerTextFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.CuoiTrangTongCong) {
        style = {
          'font-family': reportConfigFont.footerSumFont,
          'font-size.px': reportConfigFont.footerSumFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.footerSumFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.footerSumFontItalic ? 'Italic' : 'normal',
          'text-decoration': reportConfigFont.footerSumFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.NgayThang) {
        style = {
          'font-family': reportConfigFont.dayMonthFont,
          'font-size.px': reportConfigFont.dayMonthFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.dayMonthFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.dayMonthFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.dayMonthFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.ChucDanh) {
        style = {
          'font-family': reportConfigFont.positionFont,
          'font-size.px': reportConfigFont.positionFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.positionFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.positionFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.positionFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.LoaiKy) {
        style = {
          'font-family': reportConfigFont.typeSignedFont,
          'font-size.px': reportConfigFont.typeSignedFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.typeSignedFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.typeSignedFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.typeSignedFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      } else if (viTri === ViTri.ChuKy) {
        style = {
          'font-family': reportConfigFont.signedFont,
          'font-size.px': reportConfigFont.signedFontSize * PicaPointToPixel,
          'font-weight': reportConfigFont.signedFontBold ? 'Bold' : 'normal',
          'font-style': reportConfigFont.signedFontItalic ? 'Italic' : 'normal',
          'line-height': '1.2',
          'text-decoration': reportConfigFont.signedFontUnderline ? 'underline' : 'none',
          width: width + 'px',
        };
      }
    }
    if (style) {
      style.padding = '0px';
      style['white-space'] = 'inherit';
      style['height'] = 'auto';
    }
    return style;
  }

  getCbbTimeLine() {
    let listTimeLine;
    this.translateService
      .get([
        'ebwebApp.receiveBill.timeLine.today',
        'ebwebApp.receiveBill.timeLine.thisWeek',
        'ebwebApp.receiveBill.timeLine.earlyWeekToNow',
        'ebwebApp.receiveBill.timeLine.thisMonth',
        'ebwebApp.receiveBill.timeLine.earlyMonthToNow',
        'ebwebApp.receiveBill.timeLine.thisQuarter',
        'ebwebApp.receiveBill.timeLine.earlyQuarterToNow',
        'ebwebApp.receiveBill.timeLine.thisYear',
        'ebwebApp.receiveBill.timeLine.earlyYearToNow',
        'ebwebApp.receiveBill.timeLine.january',
        'ebwebApp.receiveBill.timeLine.february',
        'ebwebApp.receiveBill.timeLine.march',
        'ebwebApp.receiveBill.timeLine.april',
        'ebwebApp.receiveBill.timeLine.may',
        'ebwebApp.receiveBill.timeLine.june',
        'ebwebApp.receiveBill.timeLine.july',
        'ebwebApp.receiveBill.timeLine.august',
        'ebwebApp.receiveBill.timeLine.september',
        'ebwebApp.receiveBill.timeLine.october',
        'ebwebApp.receiveBill.timeLine.november',
        'ebwebApp.receiveBill.timeLine.december',
        'ebwebApp.receiveBill.timeLine.quarterOne',
        'ebwebApp.receiveBill.timeLine.quarterTwo',
        'ebwebApp.receiveBill.timeLine.quarterThree',
        'ebwebApp.receiveBill.timeLine.quarterFour',
        'ebwebApp.receiveBill.timeLine.lastWeek',
        'ebwebApp.receiveBill.timeLine.lastMonth',
        'ebwebApp.receiveBill.timeLine.lastQuarter',
        'ebwebApp.receiveBill.timeLine.lastYear',
        'ebwebApp.receiveBill.timeLine.nextWeek',
        'ebwebApp.receiveBill.timeLine.theNextFourWeeks',
        'ebwebApp.receiveBill.timeLine.nextMonth',
        'ebwebApp.receiveBill.timeLine.nextQuarter',
        'ebwebApp.receiveBill.timeLine.nextYear',
        'ebwebApp.receiveBill.timeLine.optional',
      ])
      .subscribe(res => {
        listTimeLine = [
          { value: '0', display: res['ebwebApp.receiveBill.timeLine.today'] },
          { value: '1', display: res['ebwebApp.receiveBill.timeLine.thisWeek'] },
          { value: '2', display: res['ebwebApp.receiveBill.timeLine.earlyWeekToNow'] },
          { value: '3', display: res['ebwebApp.receiveBill.timeLine.thisMonth'] },
          { value: '4', display: res['ebwebApp.receiveBill.timeLine.earlyMonthToNow'] },
          { value: '5', display: res['ebwebApp.receiveBill.timeLine.thisQuarter'] },
          { value: '6', display: res['ebwebApp.receiveBill.timeLine.earlyQuarterToNow'] },
          { value: '7', display: res['ebwebApp.receiveBill.timeLine.thisYear'] },
          { value: '8', display: res['ebwebApp.receiveBill.timeLine.earlyYearToNow'] },
          { value: '9', display: res['ebwebApp.receiveBill.timeLine.january'] },
          { value: '10', display: res['ebwebApp.receiveBill.timeLine.february'] },
          { value: '11', display: res['ebwebApp.receiveBill.timeLine.march'] },
          { value: '12', display: res['ebwebApp.receiveBill.timeLine.april'] },
          { value: '13', display: res['ebwebApp.receiveBill.timeLine.may'] },
          { value: '14', display: res['ebwebApp.receiveBill.timeLine.june'] },
          { value: '15', display: res['ebwebApp.receiveBill.timeLine.july'] },
          { value: '16', display: res['ebwebApp.receiveBill.timeLine.august'] },
          { value: '17', display: res['ebwebApp.receiveBill.timeLine.september'] },
          { value: '18', display: res['ebwebApp.receiveBill.timeLine.october'] },
          { value: '19', display: res['ebwebApp.receiveBill.timeLine.november'] },
          { value: '20', display: res['ebwebApp.receiveBill.timeLine.december'] },
          { value: '21', display: res['ebwebApp.receiveBill.timeLine.quarterOne'] },
          { value: '22', display: res['ebwebApp.receiveBill.timeLine.quarterTwo'] },
          { value: '23', display: res['ebwebApp.receiveBill.timeLine.quarterThree'] },
          { value: '24', display: res['ebwebApp.receiveBill.timeLine.quarterFour'] },
          { value: '25', display: res['ebwebApp.receiveBill.timeLine.lastWeek'] },
          { value: '26', display: res['ebwebApp.receiveBill.timeLine.lastMonth'] },
          { value: '27', display: res['ebwebApp.receiveBill.timeLine.lastQuarter'] },
          { value: '28', display: res['ebwebApp.receiveBill.timeLine.lastYear'] },
          { value: '29', display: res['ebwebApp.receiveBill.timeLine.nextWeek'] },
          { value: '30', display: res['ebwebApp.receiveBill.timeLine.theNextFourWeeks'] },
          { value: '31', display: res['ebwebApp.receiveBill.timeLine.nextMonth'] },
          { value: '32', display: res['ebwebApp.receiveBill.timeLine.nextQuarter'] },
          { value: '33', display: res['ebwebApp.receiveBill.timeLine.nextYear'] },
          { value: '34', display: res['ebwebApp.receiveBill.timeLine.optional'] },
        ];
      });
    return listTimeLine;
  }

  getTimeLine(intTimeLine: String, account?: any) {
    let ngayHachToan;
    this.objTimeLine = {};
    let today = new Date();
    this.dtBeginTemp = new Date();
    this.dtEndTemp = new Date();
    let dayOfWeek = today.getDay();
    let month = today.getMonth();
    let year = today.getFullYear();
    if (ngayHachToan) {
      today = new Date(ngayHachToan.year(), ngayHachToan.month(), ngayHachToan.date());
      dayOfWeek = today.getDay();
      this.dtBeginTemp = new Date(ngayHachToan.year(), ngayHachToan.month(), ngayHachToan.date());
      this.dtEndTemp = new Date(ngayHachToan.year(), ngayHachToan.month(), ngayHachToan.date());
      year = ngayHachToan.year();
      month = ngayHachToan.month();
      this.dtBeginTemp = new Date(year, this.dtBeginTemp.getMonth(), this.dtBeginTemp.getDate());
      this.dtEndTemp = new Date(year, this.dtEndTemp.getMonth(), this.dtEndTemp.getDate());
    }
    // số ngày từ ngày đầu tuần tới ngày hiện tại
    // const alpha = getLocaleFirstDayOfWeek('');
    const alpha = 7 - dayOfWeek;
    switch (intTimeLine.toString()) {
      case '0': // hôm nay
        this.dtBeginDate = this.dtEndDate = this.datepipe.transform(today, 'yyyy-MM-dd');
        break;
      case '1': // tuần này
        this.dtBeginDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() - (dayOfWeek - 1)), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtEndTemp.setDate(this.dtEndTemp.getDate() + alpha), 'yyyy-MM-dd');
        break;
      case '2': // đầu tuần đến hiện tại
        this.dtBeginDate = this.datepipe.transform(today.setDate(today.getDate() - (dayOfWeek - 1)), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        break;
      case '3': // tháng này
        this.dtBeginDate = this.datepipe.transform(new Date(year, month, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, month + 1, 0), 'yyyy-MM-dd');
        break;
      case '4': // đầu tháng tới hiện tại
        this.dtBeginDate = this.datepipe.transform(new Date(year, month, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        break;
      case '5': // quý này
        // quý I
        if (month >= 0 && month <= 2) {
          this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
        } else if (month >= 3 && month <= 5) {
          // quý II
          this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
        } else if (month >= 6 && month <= 8) {
          // quý III
          this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
        } else if (month >= 9 && month <= 11) {
          // quý IV
          this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
        } else {
          this.toastr.error(
            this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
            this.translate.instant('ebwebApp.receiveBill.message')
          );
        }
        break;
      case '6': // đầu quý đến hiện tại
        // quý I
        if (month >= 0 && month <= 2) {
          this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        } else if (month >= 3 && month <= 5) {
          // quý II
          this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        } else if (month >= 6 && month <= 8) {
          // quý III
          this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        } else if (month >= 9 && month <= 11) {
          // quý IV
          this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        } else {
          this.toastr.error(
            this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
            this.translate.instant('ebwebApp.receiveBill.message')
          );
        }
        break;
      case '7': // năm nay
        this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
        break;
      case '8': // đầu năm tới hiện tại
        this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtEndTemp, 'yyyy-MM-dd');
        break;
      case '9': // tháng 1
        this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 1, 0), 'yyyy-MM-dd');
        break;
      case '10': // tháng 2
        this.dtBeginDate = this.datepipe.transform(new Date(year, 1, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 2, 0), 'yyyy-MM-dd');
        break;
      case '11': // tháng 3
        this.dtBeginDate = this.datepipe.transform(new Date(year, 2, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
        break;
      case '12': // tháng 4
        this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 4, 0), 'yyyy-MM-dd');
        break;
      case '13': // tháng 5
        this.dtBeginDate = this.datepipe.transform(new Date(year, 4, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 5, 0), 'yyyy-MM-dd');
        break;
      case '14': // tháng 6
        this.dtBeginDate = this.datepipe.transform(new Date(year, 5, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
        break;
      case '15': // tháng 7
        this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 7, 0), 'yyyy-MM-dd');
        break;
      case '16': // tháng 8
        this.dtBeginDate = this.datepipe.transform(new Date(year, 7, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 8, 0), 'yyyy-MM-dd');
        break;
      case '17': // tháng 9
        this.dtBeginDate = this.datepipe.transform(new Date(year, 8, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
        break;
      case '18': // tháng 10
        this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 10, 0), 'yyyy-MM-dd');
        break;
      case '19': // tháng 11
        this.dtBeginDate = this.datepipe.transform(new Date(year, 10, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 11, 0), 'yyyy-MM-dd');
        break;
      case '20': // tháng 12
        this.dtBeginDate = this.datepipe.transform(new Date(year, 11, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
        break;
      case '21': // quý I
        this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
        break;
      case '22': // quý II
        this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
        break;
      case '23': // quý III
        this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
        break;
      case '24': // quý IV
        this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
        break;
      case '25': // tuần trước
        this.dtBeginDate = this.datepipe.transform(
          this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() - (dayOfWeek - 1) - 7),
          'yyyy-MM-dd'
        );
        this.dtEndDate = this.datepipe.transform(this.dtEndTemp.setDate(this.dtEndTemp.getDate() - (dayOfWeek - 1) - 1), 'yyyy-MM-dd');
        break;
      case '26': // tháng trước
        let lastMonth;
        let lastYear;
        if (month === 0) {
          lastMonth = 12;
          lastYear = year - 1;
        } else {
          lastMonth = month - 1;
          lastYear = year;
        }
        this.dtBeginDate = this.datepipe.transform(new Date(lastYear, lastMonth, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(lastYear, lastMonth + 1, 0), 'yyyy-MM-dd');
        break;
      case '27': // quý trước
        // quý I
        if (month >= 0 && month <= 2) {
          this.dtBeginDate = this.datepipe.transform(new Date(year - 1, 9, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year - 1, 12, 0), 'yyyy-MM-dd');
        } else if (month >= 3 && month <= 5) {
          // quý II
          this.dtBeginDate = this.datepipe.transform(new Date(year, 0, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 3, 0), 'yyyy-MM-dd');
        } else if (month >= 6 && month <= 8) {
          // quý III
          this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
        } else if (month >= 9 && month <= 11) {
          // quý IV
          this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
        } else {
          this.toastr.error(
            this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
            this.translate.instant('ebwebApp.receiveBill.message')
          );
        }
        break;
      case '28': // năm trước
        this.dtBeginDate = this.datepipe.transform(new Date(year - 1, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year - 1, 12, 0), 'yyyy-MM-dd');
        break;
      case '29': // tuần sau
        this.dtBeginDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + (alpha + 1)), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + 6), 'yyyy-MM-dd');
        break;
      case '30': // bốn tuần tới
        this.dtBeginDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + (alpha + 1)), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(this.dtBeginTemp.setDate(this.dtBeginTemp.getDate() + 27), 'yyyy-MM-dd');
        break;
      case '31': // tháng sau
        let afterMonth;
        let afterYear;
        if (month === 11) {
          afterMonth = 0;
          afterYear = year + 1;
        } else {
          afterMonth = month + 1;
          afterYear = year;
        }
        this.dtBeginDate = this.datepipe.transform(new Date(afterYear, afterMonth, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(afterYear, afterMonth + 1, 0), 'yyyy-MM-dd');
        break;
      case '32': // quý sau
        // quý I
        if (month >= 0 && month <= 2) {
          this.dtBeginDate = this.datepipe.transform(new Date(year, 3, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 6, 0), 'yyyy-MM-dd');
        } else if (month >= 3 && month <= 5) {
          // quý II
          this.dtBeginDate = this.datepipe.transform(new Date(year, 6, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 9, 0), 'yyyy-MM-dd');
        } else if (month >= 6 && month <= 8) {
          // quý III
          this.dtBeginDate = this.datepipe.transform(new Date(year, 9, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year, 12, 0), 'yyyy-MM-dd');
        } else if (month >= 9 && month <= 11) {
          // quý IV
          this.dtBeginDate = this.datepipe.transform(new Date(year + 1, 0, 1), 'yyyy-MM-dd');
          this.dtEndDate = this.datepipe.transform(new Date(year + 1, 3, 0), 'yyyy-MM-dd');
        } else {
          this.toastr.error(
            this.translate.instant('ebwebApp.receiveBill.errorWhenSelectTimeLine'),
            this.translate.instant('ebwebApp.receiveBill.message')
          );
        }
        break;
      case '33': // năm sau
        this.dtBeginDate = this.datepipe.transform(new Date(year + 1, 0, 1), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(year + 1, 12, 0), 'yyyy-MM-dd');
        break;
      case '34': // Tùy chọn
        this.dtBeginDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
        this.dtEndDate = this.datepipe.transform(new Date(), 'yyyy-MM-dd');
        break;
    }
    this.objTimeLine.dtBeginDate = this.dtBeginDate;
    this.objTimeLine.dtEndDate = this.dtEndDate;
    return this.objTimeLine;
  }

  getStylePrint(value: String, map: any): any {
    const styles = this.getValue(value, map).isPrint
      ? {
          fontWeight: this.getValue(value, map).isBold ? 'bold' : 'normal',
          fontSize: Number(this.getValue(value, map).fontSize) * 0.8 + 'px',
          textAlign: this.getValue(value, map).alignText == 0 ? 'left' : this.getValue(value, map).alignText == 1 ? 'center' : 'right',
        }
      : {
          display: 'none',
          border: 'none',
        };
    return styles;
  }

  getValue(code: any, maps: any): any {
    let value = maps && maps.get(code) && maps.get(code).value ? maps.get(code).value : '';
    return value;
  }

  genQrCode(value: string) {
    if (value) {
      QRCode.toDataURL(value, (err, url) => {
        if (err) {
          console.error(err);
          return;
        }
        return url;
        // In ra URL của mã QR (có thể sử dụng để hiển thị hoặc tải xuống)
      });
    }
  }

  formatDate(date, type: string, formatYYYYMMDD: boolean): string | undefined {
    if (!date) return undefined;

    if (date instanceof Date) {
      const year = date.getFullYear();
      const month = ('0' + (date.getMonth() + 1)).slice(-2);
      const day = ('0' + date.getDate()).slice(-2);
      return formatYYYYMMDD ? `${year}${type}${month}${type}${day}` : `${day}${type}${month}${type}${year}`;
    } else if (typeof date === 'string') {
      const segments = date.split(/[/,-]/);
      if (segments.length !== 3) return undefined;
      const [day, month, year] = segments;
      return formatYYYYMMDD ? `${year}${type}${month}${type}${day}` : `${day}${type}${month}${type}${year}`;
    } else if (date && date.hasOwnProperty('$d')) {
      // Kiểm tra xem đó có phải là đối tượng Dayjs không
      const dayjsDate = date;
      const year = dayjsDate.$y;
      const month = ('0' + (dayjsDate.$M + 1)).slice(-2);
      const day = ('0' + dayjsDate.$D).slice(-2);
      return formatYYYYMMDD ? `${year}${type}${month}${type}${day}` : `${day}${type}${month}${type}${year}`;
    } else {
      return undefined;
    }
  }

  getWeekdaysInRange(from: string, to: string): any[] {
    const daysOfWeek = DOW_VALUE; // Short abbreviations
    const result: any[] = [];

    const startDate = dayjs(from);
    const endDate = dayjs(to).subtract(1, 'day').startOf('day');

    let weekdaysInRange: string[] = []; // Explicitly declare the type

    let currentDate = startDate.subtract(1, 'day').startOf('day');
    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      const dayOfWeek = currentDate.day();
      if (!weekdaysInRange.includes(daysOfWeek[dayOfWeek])) {
        weekdaysInRange.push(daysOfWeek[dayOfWeek]);
      }
      currentDate = currentDate.add(1, 'day').startOf('day'); // Add 1 day and reset to the beginning of the day
    }

    weekdaysInRange.sort((a, b) => daysOfWeek.indexOf(a) - daysOfWeek.indexOf(b));
    weekdaysInRange.forEach(day => {
      DOW.map((item, index) => {
        if (item.id === day?.toUpperCase()) {
          result.push({
            id: item.id,
            name: item.name,
            value: item.value,
          });
        }
      });
    });
    return result;
  }

  getDaysInRange(from, to): any[] {
    const startDate = dayjs(from);
    const endDate = dayjs(to);

    let daysInRange: number[] = [];
    let result: string[] = [];
    let currentDate = startDate;
    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      const dayOfMonth = currentDate.date();
      if (!daysInRange.includes(dayOfMonth)) {
        daysInRange.push(dayOfMonth);
      }
      currentDate = currentDate.add(1, 'day').startOf('day');
    }
    daysInRange.sort((a, b) => a - b);
    result = daysInRange.map(num => num.toString());
    return result;
  }

  // lấy các ngày nằm trong khoảng ngày và tháng
  getAllDatesInRange(from, to, months) {
    const startDate = dayjs(from);
    const endDate = dayjs(to);

    let allDatesInRange: any[] = [];
    let currentDate = startDate;
    currentDate = currentDate.hour(0);
    currentDate = currentDate.minute(0);
    currentDate = currentDate.second(0);
    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      const currentMonth = currentDate.month() + 1;
      if ((months.length === 0 || months.includes(currentMonth)) && !allDatesInRange.includes(currentDate)) {
        allDatesInRange.push(currentDate);
      }
      currentDate = currentDate.add(1, 'day').startOf('day');
    }

    return allDatesInRange;
  }

  /**
   * nhập vào string có định dang fromFormat
   * trả về string có định dạng toFormat
   */
  changeFomatDateOfString(time: any, fromFormat: string, toFormat: string) {
    if (time) {
      const date1 = dayjs(time, fromFormat);
      return date1.format(toFormat);
    }
    return '';
  }
  /**
   *  lấy position từ json theo code
   */
  getPosition(code: string) {
    let map = this.mapService.getMap();
    const value = map.get(code) || '';
    return value.position;
  }

  // lấy các ngày nằm trong khoảng ngày và không nằm trong tháng
  getDatesInRangeExcludingSpecificMonths(from: string, to: string, months: number[]): dayjs.Dayjs[] {
    const startDate = dayjs(from);
    const endDate = dayjs(to);

    const datesArray: dayjs.Dayjs[] = [];

    let currentDate = startDate.clone();

    while (currentDate.isBefore(endDate) || currentDate.isSame(endDate, 'day')) {
      const currentMonth = currentDate.month() + 1;
      if (!months.includes(currentMonth)) {
        datesArray.push(currentDate);
      }
      currentDate = currentDate.add(1, 'day');
    }

    return datesArray;
  }

  getAllMonthInRange(from, to) {
    const monthsArray: number[] = [];

    let current = from.clone(); // Bắt đầu từ ngày đầu tiên
    while (current.isBefore(to) || current.isSame(to, 'month')) {
      const month = current.month() + 1; // +1 để chuyển từ 0-indexed sang 1-indexed
      if (!monthsArray.includes(month)) {
        monthsArray.push(month);
      }
      current = current.add(1, 'month');
    }
    monthsArray.sort((a, b) => a - b);
    return monthsArray;
  }

  getActive(code: string) {
    let map = this.mapService.getMap();
    const value = map.get(code) || '';
    return value.active;
  }

  getStyleByCode(code: string) {
    let map = this.mapService.getMap();
    let cssString = '';
    const value = map.get(code) || '';
    if (value.active == false) {
      cssString += 'display:none !important; ';
    }
    if (value.position && typeof value.position === 'number') {
      cssString += `order: ${value.position} !important;`;
    }
    return cssString;
  }

  getMaxToDate(fromDate?: dayjs.Dayjs) {
    if (!fromDate) {
      const _date = dayjs();
      return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
    } else {
      const maxDate = { year: fromDate.year(), month: fromDate.month(), day: fromDate.date() + 30 };
      const currentDate = dayjs();

      // So sánh giá trị trả về từ fromDate với ngày hiện tại
      if (
        maxDate.year > currentDate.year() ||
        (maxDate.year === currentDate.year() && maxDate.month > currentDate.month()) ||
        (maxDate.year === currentDate.year() && maxDate.month === currentDate.month() && maxDate.day > currentDate.date())
      ) {
        return maxDate;
      } else {
        return { year: currentDate.year(), month: currentDate.month() + 1, day: currentDate.date() };
      }
    }
  }

  getPositonByCode(code: string, increasePosition: any) {
    let map = this.mapService.getMap();
    let cssString = '';
    const value = map.get(code) || '';
    if (increasePosition) {
      if (value.position && typeof value.position === 'number') {
        cssString += `order: ${value.position + 1} !important;`;
      }
    } else {
      if (value.position && typeof value.position === 'number') {
        cssString += `order: ${value.position} !important;`;
      }
    }
    return cssString;
  }
}
function padNumber(value: any | null) {
  if (!isNaN(value) && value !== null) {
    return `0${value}`.slice(-2);
  } else {
    return '';
  }
}
