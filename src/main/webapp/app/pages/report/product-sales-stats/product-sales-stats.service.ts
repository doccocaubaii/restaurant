import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import {
  CREATE_CATEGORY,
  EXPORT_EXCEL,
  EXPORT_PDF,
  GET_INVOICE_PATTERNS,
  GET_PRODUCT_BY_ID,
  GET_PRODUCT_PAGING,
  GET_REPORT_PRODUCT_SALES,
} from '../../../constants/api.constants';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { createRequestOption } from '../../../core/request/request-util';
import { map } from 'rxjs/operators';
import { ExportCommon } from '../../export/export.common';

@Injectable({
  providedIn: 'root',
})
export class ProductSalesStatsService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, public exportComponent: ExportCommon) {}
  getInvoicePatterns(comId: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_INVOICE_PATTERNS + `${comId}`);
  }

  getReportProductSales(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_REPORT_PRODUCT_SALES, { params: options, observe: 'response' })
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
    return this.http.post(`${this.resourceUrl}` + EXPORT_EXCEL, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  exportPdf(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf'); // Thay đổi kiểu MIME
    return this.http.post(`${this.resourceUrl}` + EXPORT_PDF, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
