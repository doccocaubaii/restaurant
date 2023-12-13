import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { ExportCommon } from '../../export/export.common';
import { Observable } from 'rxjs';
import { GET_EXCEL_PRODUCT_HOT_SALES, GET_PDF_PRODUCT_HOT_SALES, GET_REPORT_PRODUCT_HOT_SALES } from '../../../constants/api.constants';
import { createRequestOption } from '../../../core/request/request-util';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ProductHotSaleService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, public exportComponent: ExportCommon) {}
  getReportProductHotSale(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_REPORT_PRODUCT_HOT_SALES, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    return res.clone({
      body: res.body,
    });
  }

  exportExcel(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + GET_EXCEL_PRODUCT_HOT_SALES, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }

  exportPdf(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf'); // Thay đổi kiểu MIME
    return this.http.post(`${this.resourceUrl}` + GET_PDF_PRODUCT_HOT_SALES, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
