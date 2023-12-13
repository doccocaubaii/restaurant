import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { createRequestOption, createRequestOption2 } from '../../core/request/request-util';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { AccountService } from '../../core/auth/account.service';
import { AuthServerProvider } from '../../core/auth/auth-jwt.service';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import {
  CREATE_INOUTWARD,
  DELETE_INOUTWARD,
  ENABLE_INVENTORY_TRACKING,
  GET_ALL_CUSTOMER,
  GET_ALL_CUSTOMER_PAGING,
  GET_ALL_TRANSACTIONS,
  GET_PRODUCT,
  GET_TRANSACTION_DETAIL,
  UPDATE_INOUTWARD,
} from '../../constants/api.constants';
import { IResultDTO } from '../../core/response/result-dto.model';

@Injectable({
  providedIn: 'root',
})
export class WarehouseService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(
    private http: HttpClient,
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private applicationConfigService: ApplicationConfigService
  ) {}

  getTransactions(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_TRANSACTIONS, {
      params: options,
      observe: 'response',
    });
  }
  transactionDetail(id: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_TRANSACTION_DETAIL + `${id}`);
  }
  getProductWithPaging(filterProduct: any): Observable<any> {
    const options = createRequestOption2(filterProduct);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PRODUCT, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  getAllCustomerPaging(filterCustomer): Observable<any> {
    const options = createRequestOption(filterCustomer);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_ALL_CUSTOMER_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    return res.clone({
      body: res.body,
    });
  }
  getAllCustomer(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_CUSTOMER);
  }
  createInoutward(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_INOUTWARD, req);
  }

  updateInoutward(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_INOUTWARD, req);
  }

  deleteInoutward(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_INOUTWARD, req);
  }

  enableInvetoryTracking(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + ENABLE_INVENTORY_TRACKING, req);
  }

  getDetail(id: any): Observable<HttpResponse<IResultDTO>> {
    return this.http.get<IResultDTO>(`${this.resourceUrl}` + GET_TRANSACTION_DETAIL + `${id}`, { observe: 'response' });
  }
}
