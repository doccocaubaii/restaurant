import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
  CREATE_CATEGORY,
  EXPORT_EXCEL,
  EXPORT_PDF,
  GET_INVENTORY_STATS,
  GET_INVOICE_PATTERNS,
  GET_PREVIEW_INVENTORY_STATS,
  GET_PRODUCT_BY_ID,
  GET_PRODUCT_PAGING,
  GET_REPORT_PRODUCT_SALES,
  INVENTORY_STATS_EXCEL,
  INVENTORY_STATS_PDF,
} from '../../../constants/api.constants';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { map } from 'rxjs/operators';
import { ExportCommon } from '../../export/export.common';
import dayjs from 'dayjs/esm';

@Injectable({
  providedIn: 'root',
})
export class InventoryStatsService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  paramCheckAll: boolean = false;
  ids: any[] = [];
  paramCheckAllPage: any;

  resetAllParam() {
    this.fromDate = dayjs();
    this.toDate = dayjs();
    this.paramCheckAll = false;
    this.paramCheckAllPage = false;
    this.ids = [];
  }

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, public exportComponent: ExportCommon) {}
  getInvoicePatterns(comId: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_INVOICE_PATTERNS + `${comId}`);
  }

  getInventoryStats(searchReq: any): Observable<any> {
    const options = this.createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_INVENTORY_STATS, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getPreviewInventoryStats(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PREVIEW_INVENTORY_STATS, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }
  exportExcel(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    let reqBody = {
      comId: req.comId,
      isOnlyGetData: true,
      keyword: req.keyword,
      groupId: req.groupId,
      fromDateStr: req.fromDate,
      toDateStr: req.toDate,
      paramCheckAll: req.paramCheckAll,
      ids: req.ids,
    };
    return this.http.post(`${this.resourceUrl}` + INVENTORY_STATS_EXCEL, reqBody, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  exportPdf(req: any): Observable<any> {
    let reqBody = {
      comId: req.comId,
      isOnlyGetData: true,
      keyword: req.keyword,
      groupId: req.groupId,
      fromDateStr: req.fromDate,
      toDateStr: req.toDate,
      paramCheckAll: req.paramCheckAll,
      ids: req.ids,
    };
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf');
    return this.http.post(`${this.resourceUrl}` + INVENTORY_STATS_PDF, reqBody, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }

  createRequestOption = (req?: any): HttpParams => {
    let options: HttpParams = new HttpParams();

    if (req) {
      Object.keys(req).forEach(key => {
        if (key !== 'sort' && (req[key] || req[key] === 0 || req[key] === false)) {
          for (const value of [].concat(req[key]).filter(v => v !== '')) {
            options = options.append(key, value);
          }
        }
      });

      if (req.sort) {
        req.sort.forEach((val: string) => {
          options = options.append('sort', val);
        });
      }
    }

    return options;
  };
}
