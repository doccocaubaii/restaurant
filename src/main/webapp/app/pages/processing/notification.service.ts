import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import { createRequestOption } from '../../core/request/request-util';
import {
  CHANGE_STATUS_DISH,
  COUNT_NOTIFICATION,
  DELETE_DISH,
  GET_FOR_PROCESSING,
  GET_LIST_NOTIFICATION,
  GET_PRODUCT_PAGING,
  UPDATE_NOTIFICATION,
} from '../../constants/api.constants';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { FormBuilder } from '@angular/forms';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  private unreadNotifications: number = 0;

  getUnreadNotifications() {
    return this.unreadNotifications;
  }

  setUnreadNotifications(number: any) {
    this.unreadNotifications = number;
  }

  removeNotification() {
    if (this.unreadNotifications > 0) {
      this.unreadNotifications--;
    }
  }

  getUnReadNotification(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_LIST_NOTIFICATION, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  updateNotification(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_NOTIFICATION, req, { observe: 'response' });
  }

  countUnreadNotification(): Observable<any> {
    let req = {
      isToday: false,
    };
    const options = createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}` + COUNT_NOTIFICATION, { params: options, observe: 'response' });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }
}
