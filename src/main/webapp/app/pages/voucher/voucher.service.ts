import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import {
  APPLY_ALL_VOUCHER,
  APPLY_VOUCHER,
  CREATE_VOUCHER,
  DELETE_VOUCHER,
  GET_APPLY_VOUCHER,
  GET_LIST_VOUCHER,
  GET_VOUCHER_USAGE_DETAIL,
  UPDATE_VOUCHER,
} from '../../constants/api.constants';
import { VoucherConstants } from '../../constants/voucher.constants';

@Injectable({
  providedIn: 'root',
})
export class VoucherService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}
  urlGetTax: string = 'http://utilsrv.easyinvoice.com.vn/api/company/info';
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getVouchers(
    comId: number,
    page: number,
    pageSize: number,
    keyword?: string,
    fromDate?: string,
    toDate?: string,
    type?: number
  ): Observable<any> {
    let params = new HttpParams().set('page', String(page - 1)).set('size', String(pageSize));
    params = params.set('comId', comId);
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (fromDate) {
      params = params.set('fromDate', fromDate);
    }
    if (toDate) {
      params = params.set('toDate', toDate);
    }
    params = params.set('type', type ? type : VoucherConstants.TYPE_REQUEST_VOUCHER);
    return this.http.get<any>(`${this.resourceUrl}` + GET_LIST_VOUCHER, { params });
  }

  getApplyVoucher(comId: number, voucherId: number, type: number, getDefault: boolean, page: number, size: number, keyword?: string) {
    let params = new HttpParams().set('page', String(page - 1)).set('size', String(size));
    params = params.set('comId', comId).set('type', type).set('getDefault', getDefault);
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (voucherId) params = params.set('voucherId', voucherId);
    return this.http.get<any>(`${this.resourceUrl}` + GET_APPLY_VOUCHER, { params });
  }

  postVoucher(voucher: any): Observable<any> {
    let params = new HttpParams();
    Object.keys(voucher).forEach(key => {
      params = params.set(key, voucher[key]?.toString() || '');
    });
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_VOUCHER, voucher);
  }

  putVoucher(voucher: any): Observable<any> {
    // console.log('body = ' + JSON.stringify(voucher));
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_VOUCHER, voucher);
  }
  delVoucher(id: Number): Observable<any> {
    // console.log('delete id = ' + JSON.stringify(id));
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_VOUCHER + `/${id}`, null);
  }

  applyVoucher(comId: number, voucherId: number, applyType: number, applyItem: [], checkAllType: any): Observable<any> {
    if (checkAllType) {
      const requestBody = {
        comId: comId,
        voucherId: voucherId,
        applyType: applyType,
        applyItem: applyItem,
        checkAllType: checkAllType,
      };
      return this.http.post<any>(`${this.resourceUrl}${APPLY_ALL_VOUCHER}`, requestBody);
    }
    const requestBody = {
      comId: comId,
      voucherId: voucherId,
      applyType: applyType,
      applyItem: applyItem,
    };

    return this.http.post<any>(`${this.resourceUrl}${APPLY_VOUCHER}`, requestBody);
  }

  getVoucherUsageDetail(req: any): Observable<any> {
    let params = new HttpParams().set('page', String(req.page - 1)).set('size', String(req.pageSize));
    params = params.set('comId', req.comId);
    params = params.set('voucherId', req.voucherId);
    if (req.keyword) {
      params = params.set('keyword', req.keyword);
    }
    if (req.fromDate) {
      params = params.set('fromDate', req.fromDate);
    }
    if (req.toDate) {
      params = params.set('toDate', req.toDate);
    }
    return this.http.get<any>(`${this.resourceUrl}` + GET_VOUCHER_USAGE_DETAIL, { params });
  }
}
