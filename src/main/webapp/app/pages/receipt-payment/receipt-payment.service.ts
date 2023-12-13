import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption } from '../../core/request/request-util';
import {
  CREATE_BUSINESS_TYPE,
  CREATE_RECEIPT_PAYMENT,
  DELETE_LIST_RECEIPT_PAYMENT,
  DELETE_RECEIPT_PAYMENT,
  GET_LIST_BUSSINESS_TYPE,
  GET_LIST_RECEIPT_PAYMENT,
  GET_RECEIPT_PAYMENT_BY_ID,
  UPDATE_RECEIPT_PAYMENT,
} from '../../constants/api.constants';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { AccountService } from '../../core/auth/account.service';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class ReceiptPaymentService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(
    private http: HttpClient,
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private applicationConfigService: ApplicationConfigService
  ) {}

  getAllTransactions(req?: any): Observable<any> {
    const options = this.createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}` + GET_LIST_RECEIPT_PAYMENT, {
      params: options,
      observe: 'response',
    });
  }

  getAllBussinessType(req?: any): Observable<any> {
    const options = this.createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}` + GET_LIST_BUSSINESS_TYPE, {
      params: options,
      observe: 'response',
    });
  }

  getDetailById(req: any): Observable<any> {
    const options = this.createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}` + GET_RECEIPT_PAYMENT_BY_ID, {
      params: options,
      observe: 'response',
    });
  }

  createReceiptPayment(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_RECEIPT_PAYMENT, req);
  }

  createBusinessType(req: any): Observable<any> {
    const options = this.createRequestOptionNoKeyword(req);
    return this.http.post<any>(
      `${this.resourceUrl}` + CREATE_BUSINESS_TYPE + '?comId=' + req.comId + '&type=' + req.type + '&name=' + req.name,
      null
    );
  }

  updateReceiptPayment(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_RECEIPT_PAYMENT, req);
  }

  deleteReceiptPayment(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_RECEIPT_PAYMENT, req);
  }

  deleteListReceiptPayment(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_LIST_RECEIPT_PAYMENT, req);
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    return res.clone({
      body: res.body,
    });
  }

  createRequestOption = (req?: any): HttpParams => {
    let options: HttpParams = new HttpParams();

    if (req) {
      Object.keys(req).forEach(key => {
        if (key !== 'sort' && (req[key] || req[key] === 0)) {
          for (const value of [].concat(req[key])) {
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

    if (!options.has('keyword')) {
      options = options.append('keyword', '');
    }

    return options;
  };

  createRequestOptionNoKeyword = (req?: any): HttpParams => {
    let options: HttpParams = new HttpParams();

    if (req) {
      Object.keys(req).forEach(key => {
        if (key !== 'sort' && (req[key] || req[key] === 0)) {
          for (const value of [].concat(req[key])) {
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
