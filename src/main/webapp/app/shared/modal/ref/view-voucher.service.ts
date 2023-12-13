import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import moment from 'moment';
import { map } from 'rxjs/operators';

import { DATE_FORMAT } from '../../../config/input.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { IViewVoucher } from './view-voucher.model';

type EntityResponseType = HttpResponse<IViewVoucher>;
type EntityArrayResponseType = HttpResponse<IViewVoucher[]>;

@Injectable({ providedIn: 'root' })
export class ViewVoucherService {
  private resourceUrl = SERVER_API_URL + 'api/view-vouchers';
  private resourceUrlUtilities = SERVER_API_URL + 'api/utilities';
  private resourceUrlGL = SERVER_API_URL + 'api/general-ledgers';
  private resourceUrlViewVoucherNo = SERVER_API_URL + 'api/view-voucher-no';

  constructor(private http: HttpClient) {}

  create(viewVoucher: IViewVoucher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viewVoucher);
    return this.http
      .post<IViewVoucher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(viewVoucher: IViewVoucher): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(viewVoucher);
    return this.http
      .put<IViewVoucher>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IViewVoucher>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryAll(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl + '/get-all', { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  private convertDateFromClient(viewVoucher: IViewVoucher): IViewVoucher {
    const copy: IViewVoucher = Object.assign({}, viewVoucher, {
      date: viewVoucher.date != null && viewVoucher.date.isValid() ? viewVoucher.date.format(DATE_FORMAT) : null,
      postedDate: viewVoucher.postedDate != null && viewVoucher.postedDate.isValid() ? viewVoucher.postedDate.format(DATE_FORMAT) : null,
    });
    return copy;
  }

  private convertDateFromServer(res: EntityResponseType): EntityResponseType {
    // @ts-ignore
    res.body.date = res.body.date != null ? moment(res.body.date) : null;
    // @ts-ignore
    res.body.postedDate = res.body.postedDate != null ? moment(res.body.postedDate) : null;
    return res;
  }

  getViewVoucherToModal(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrl + '/get-view-voucher-to-modal', { params: options, observe: 'response' });
  }

  checkViaStockPPInvoice(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(SERVER_API_URL + 'api/pp-invoice/check-via-stock', {
      params: options,
      observe: 'response',
    });
  }

  getQuantityExistsTest(req?: any): Observable<HttpResponse<any[]>> {
    // const options = createRequestOption(req);
    return this.http.post<any[]>(`api/material-goods/get-all-by-quantity-exists`, req, { observe: 'response' });
  }
  getEMContract(req?: any): Observable<HttpResponse<any[]>> {
    // const options = createRequestOption(req);
    return this.http.post<any[]>(`api/e-m-contracts/get-details-and-material`, req, { observe: 'response' });
  }
  getMaterialGoodAndRepository(req?: any): Observable<HttpResponse<any>> {
    const options = createRequestOption(req);
    return this.http.post<any>(`api/material-goods/material-good-and-repository`, req, { observe: 'response' });
  }
  getMaterialGoodAndEMContract(req?: any): Observable<HttpResponse<any>> {
    const options = createRequestOption(req);
    return this.http.post<any>(`api/e-m-contracts/material-good-and-em-contract`, req, { observe: 'response' });
  }

  getVoucherByTypeGroup(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any[]>(this.resourceUrlUtilities + '/reset-no/get-vouchers', { params: options, observe: 'response' })
      .pipe(map((res: any) => this.convertDateFromServer(res)));
  }

  searchVoucher(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any[]>(this.resourceUrlUtilities + '/search-voucher', { params: options, observe: 'response' })
      .pipe(map((res: any) => this.convertDateFromServer(res)));
  }

  searchVoucherCustom(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any[]>(this.resourceUrlUtilities + '/search-voucher-custom', { params: options, observe: 'response' })
      .pipe(map((res: any) => this.convertDateFromServer(res)));
  }

  searchVoucherList(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any[]>(this.resourceUrlUtilities + '/search-gvoucher-list', { params: options, observe: 'response' })
      .pipe(map((res: any) => this.convertDateFromServer(res)));
  }

  getViewGVoucherToModal(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrlGL + '/get-view-g-voucher-modal', { params: options, observe: 'response' });
  }

  getListViewGVoucher(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any[]>(this.resourceUrlGL + '/get-list-view-g-voucher', { params: options, observe: 'response' });
  }

  viewVoucherNoDetails(red?: any): Observable<any> {
    const option = createRequestOption(red);
    return this.http.get<any>(`${this.resourceUrlViewVoucherNo}` + '/all-by-account', {
      params: option,
      observe: 'response',
    });
  }

  viewVoucherNoByAccountingObject(red?: any): Observable<any> {
    const option = createRequestOption(red);
    return this.http.get<any>(`${this.resourceUrlViewVoucherNo}` + '/all-by-other-data', {
      params: option,
      observe: 'response',
    });
  }

  changeMultipleData(req?: any) {
    return this.http.put(`${this.resourceUrlViewVoucherNo}/change-multiple-data`, req, {
      observe: 'response',
    });
  }

  changMultipleOtherData(req?: any) {
    return this.http.put(`${this.resourceUrlViewVoucherNo}/change-other-data`, req, {
      observe: 'response',
    });
  }

  changMultipleOtherDataUpdateUnit(req?: any) {
    return this.http.put(`${this.resourceUrlViewVoucherNo}/change-other-data-update-unit`, req, {
      observe: 'response',
    });
  }

  getUnitsByMaterialsId(red?: any) {
    const option = createRequestOption(red);
    return this.http.get(`${this.resourceUrlViewVoucherNo}/units-by-materialsIds`, {
      params: option,
      observe: 'response',
    });
  }
}
