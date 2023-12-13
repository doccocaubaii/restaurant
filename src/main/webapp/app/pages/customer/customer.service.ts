import { Injectable } from '@angular/core';
import { Customer } from '../../entities/customer/customer';
import { Observable, of } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import {
  DELETE_CUSTOMER,
  GET_LIST_CUSTOMER,
  UPDATE_CUSTOMER,
  CREATE_CUSTOMER,
  GET_TAX_INFO,
  DELETE_MULTI_CUSTOMER,
  EXPORT_CUSTOMER,
  EXPORT_EXCEL_CUSTOMER_ALL,
  CUSTOMER_CARD_SAVE_POINT,
  CUSTOMER_CARD_HISTORY_GET_ALL,
  CUSTOMER_CARD_GET_DEFAULT,
  CUSTOMER_BILL_HISTORY_RECEIVABLE_GET_ALL,
} from '../../constants/api.constants';
import { createRequestOption } from '../../core/request/request-util';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'Application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}
  urlGetTax: string = 'http://utilsrv.easyinvoice.com.vn/api/company/info';
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getCustomers(type: number, page: number, pageSize: number, keyword?: string): Observable<any> {
    let params = new HttpParams().set('page', String(page - 1)).set('size', String(pageSize));
    params = params.set('isHiddenDefault', true);
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (type > 0) {
      params = params.set('type', type);
    }
    return this.http.get<any>(`${this.resourceUrl}` + GET_LIST_CUSTOMER, { params });
  }

  postCustomer(cus: Customer): Observable<any> {
    // console.log('body = ' + JSON.stringify(cus));
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_CUSTOMER, cus);
  }

  putCustomer(cus: Customer): Observable<any> {
    // console.log('body = ' + JSON.stringify(cus));
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_CUSTOMER, cus);
  }
  delCustomer(id: Number): Observable<any> {
    // console.log('delete id = ' + JSON.stringify(id));
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_CUSTOMER + `/${id}`, null);
  }
  getInfoCustomerFromTax(taxCode: string): Observable<any> {
    // let headers = new HttpHeaders().set('Authorization', 'easyinvoice@sds@123').set('Access-Control-Allow-Origin', 'http://localhost:9000');

    let params = new HttpParams().set('taxCode', taxCode);
    return this.http.get<any>(`${this.resourceUrl}` + GET_TAX_INFO, { params });
  }

  customerCardSavePoint(data: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CUSTOMER_CARD_SAVE_POINT, data);
  }

  getAllCustomerCardHistory(request: any): Observable<any> {
    const options = createRequestOption(request);
    return this.http.get<any>(`${this.resourceUrl}` + CUSTOMER_CARD_HISTORY_GET_ALL, {
      params: options,
      observe: 'response',
    });
  }

  getCardDefaultInCompany(comId: number): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + CUSTOMER_CARD_GET_DEFAULT + `/${comId}`, {
      observe: 'response',
    });
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      // TODO: send the error to remote logging infrastructure
      // console.error(operation + ' Lỗi khi khi gọi api : ' + error); // log to console instead
      // alert(operation + ' không thành công!');
      // Let the app keep running by returning an empty result.
      return of(result as T);
    };
  }

  delMultiStaff(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_MULTI_CUSTOMER, req);
  }

  exportExcelCustomer(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + EXPORT_EXCEL_CUSTOMER_ALL, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }

  getAllCustomerBillHistory(request: any): Observable<any> {
    const options = createRequestOption(request);
    return this.http.get<any>(`${this.resourceUrl}` + CUSTOMER_BILL_HISTORY_RECEIVABLE_GET_ALL, {
      params: options,
      observe: 'response',
    });
  }
}
