import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { FormBuilder } from '@angular/forms';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import {
  EXPORT_EXCEL,
  EXPORT_EXCEL_ACTIVITY_HISTORY,
  EXPORT_PDF,
  EXPORT_PDF_ACTIVITY_HISTORY,
  GET_ACTIVITY_HISTORY,
  GET_RECENT_ACTIVITY,
} from '../../../constants/api.constants';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class ActivityHistoryService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getActivityHistory(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_ACTIVITY_HISTORY, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getRecentActivites(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_RECENT_ACTIVITY, { params: options, observe: 'response' })
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
    return this.http.post(`${this.resourceUrl}` + EXPORT_EXCEL_ACTIVITY_HISTORY, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  exportPdf(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/pdf'); // Thay đổi kiểu MIME
    return this.http.post(`${this.resourceUrl}` + EXPORT_PDF_ACTIVITY_HISTORY, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
