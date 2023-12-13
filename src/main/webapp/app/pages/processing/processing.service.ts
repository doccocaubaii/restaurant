import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { createRequestOption } from '../../core/request/request-util';
import { CHANGE_STATUS_DISH, DELETE_DISH, GET_FOR_PROCESSING, GET_PRODUCT_PAGING } from '../../constants/api.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { FormBuilder } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class ProcessingService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  getForProcessing(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_FOR_PROCESSING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  changeProcessStatus(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CHANGE_STATUS_DISH, req, { observe: 'response' });
  }

  deleteDish(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_DISH, req, { observe: 'response' });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }
}
