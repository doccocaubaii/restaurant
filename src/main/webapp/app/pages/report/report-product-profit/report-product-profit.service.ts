import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { EXPORT_EXCEL_PRODUCT_PROFIT, EXPORT_PDF_PRODUCT_PROFIT, REPORT_PRODUCT_PROFIT } from '../../../constants/api.constants';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ReportProductProfitService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getProductProfitStats(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + REPORT_PRODUCT_PROFIT, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }

  exportExcel(req: any): Observable<any> {
    let reqBody = req;
    reqBody.isOnlyGetData = true;
    reqBody.isChecAll = true;
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + EXPORT_EXCEL_PRODUCT_PROFIT, reqBody, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  exportPdf(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf'); // Thay đổi kiểu MIME
    return this.http.post(`${this.resourceUrl}` + EXPORT_PDF_PRODUCT_PROFIT, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
