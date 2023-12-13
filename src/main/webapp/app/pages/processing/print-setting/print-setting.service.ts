import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { createRequestOption } from '../../../core/request/request-util';
import {
  CHANGE_STATUS_DISH,
  GET_FOR_PROCESSING,
  GET_LIST_PRINT_TEMPLATE,
  GET_LIST_PRINTER,
  GET_LIST_SETTING,
  GET_PRODUCT_PAGING,
  UPDATE_LIST_SETTING,
} from '../../../constants/api.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { FormBuilder } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class PrintSettingService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  getListSetting(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_LIST_SETTING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getListPrinter(): Observable<any> {
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_LIST_PRINTER, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getListPrintTemplate(): Observable<any> {
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_LIST_PRINT_TEMPLATE, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  updateListPrintSetting(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_LIST_SETTING, req, { observe: 'response' });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }
}
