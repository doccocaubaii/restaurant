import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders, HttpResponse} from "@angular/common/http";
import {ApplicationConfigService} from "../../../core/config/application-config.service";
import {Observable} from "rxjs";
import {createRequestOption} from "../../../core/request/request-util";
import {
  EXPORT_EXCEL,
  EXPORT_PDF, GET_EXCEL_REVENUE_COMMON, GET_PDF_REVENUE_COMMON,
  GET_REPORT_PRODUCT_SALES,
  GET_REPORT_REVENUE_COMMON
} from "../../../constants/api.constants";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class RevenueCommonStatsService {

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) { }

  getReportRevenueCommon(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_REPORT_REVENUE_COMMON, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }

  exportExcel(req: any): Observable<any>  {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + GET_EXCEL_REVENUE_COMMON, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  exportPdf(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf'); // Thay đổi kiểu MIME
    return this.http.post(`${this.resourceUrl}` + GET_PDF_REVENUE_COMMON, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
