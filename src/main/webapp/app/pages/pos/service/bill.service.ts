import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { CancelOrder, CompleteOrder, IBillPayment } from '../model/bill-payment.model';
import { OrderResponse } from '../model/orderResponse.model';
import {
  CANCEL_BILL_BY_ID,
  CHECKOUT_BILL_BY_ID, CREATE3,
  CREATE_BILL,
  GET_BILL_BY_ID,
  GET_BILL_TEMP,
  GET_LIST_BILL,
  TEMP_CREATE_BILL,
  UPDATE_BILL
} from 'app/constants/api.constants';

export type PartialUpdateBillPayment = Partial<IBillPayment> & Pick<IBillPayment, 'id'>;

type RestOf<T extends IBillPayment | any> = Omit<T, 'createTime' | 'updateTime' | 'paymentTime'> & {
  billDate?: string | null;
};

export type RestBillPayment = RestOf<IBillPayment>;

export type NewRestBillPayment = RestOf<any>;

export type EntityResponseType = HttpResponse<OrderResponse>;
export type EntityArrayResponseType = HttpResponse<OrderResponse[]>;

@Injectable({
  providedIn: 'root'
})
export class BillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
  }

  create(billPayment: IBillPayment) {
    const copy = this.convertDateFromClient(billPayment);
    return this.http.post(`${this.resourceUrl}${CREATE_BILL}`, copy, { observe: 'response' });
  }

  create3(billPayment: IBillPayment): Observable<any> {
    const copy = this.convertDateFromClient(billPayment);
    return this.http.post(`${this.resourceUrl}${CREATE3}`, copy, { observe: 'response' });
  }

  tempCreate(id: any, type: any, comId): Observable<any> {
    const copy = {
      id : id,
      type : type,
      comId : comId
    };
    return this.http.post(`${this.resourceUrl}${TEMP_CREATE_BILL}`, copy, { observe: 'response' });
  }

  update(billPayment: IBillPayment) {
    const copy = this.convertDateFromClient(billPayment);
    return this.http.post<RestBillPayment>(`${this.resourceUrl}${UPDATE_BILL}`, copy, { observe: 'response' });
  }

  cancel(cancelData: CancelOrder) {
    return this.http.put(`${this.resourceUrl}${CANCEL_BILL_BY_ID}`, cancelData, { observe: 'response' });
    // .pipe(map(res => this.convertResponseFromServer(res)));
  }

  complete(completeOrder: CompleteOrder) {
    const paymentTime = completeOrder.paymentTime.format(DATE_TIME_FORMAT);
    return this.http.post(`${this.resourceUrl}${CHECKOUT_BILL_BY_ID}`, {
      ...completeOrder,
      paymentTime
    }, { observe: 'response' });
  }

  findByTableAndComId(obj: any): Observable<any> {
    let params = new HttpParams()
      .set('tableId', obj.tableId)
      .set('comId', obj.comId);
    return this.http.get(`${this.resourceUrl}${GET_BILL_TEMP}`, { observe: 'response', params: params });
  }

  find(id: number) {
    return this.http.get(`${this.resourceUrl}${GET_BILL_BY_ID}${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<OrderResponse[]>(`${this.resourceUrl}${GET_LIST_BILL}`, {
      params: options,
      observe: 'response'
    });
    // .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getBillPaymentIdentifier(billPayment: Pick<IBillPayment, 'id'>): number {
    return billPayment.id;
  }

  compareBillPayment(o1: Pick<IBillPayment, 'id'> | null, o2: Pick<IBillPayment, 'id'> | null): boolean {
    return o1 && o2 ? this.getBillPaymentIdentifier(o1) === this.getBillPaymentIdentifier(o2) : o1 === o2;
  }

  addBillPaymentToCollectionIfMissing<Type extends Pick<IBillPayment, 'id'>>(
    billPaymentCollection: Type[],
    ...billPaymentsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const billPayments: Type[] = billPaymentsToCheck.filter(isPresent);
    if (billPayments.length > 0) {
      const billPaymentCollectionIdentifiers = billPaymentCollection.map(
        billPaymentItem => this.getBillPaymentIdentifier(billPaymentItem)!
      );
      const billPaymentsToAdd = billPayments.filter(billPaymentItem => {
        const billPaymentIdentifier = this.getBillPaymentIdentifier(billPaymentItem);
        if (billPaymentCollectionIdentifiers.includes(billPaymentIdentifier)) {
          return false;
        }
        billPaymentCollectionIdentifiers.push(billPaymentIdentifier);
        return true;
      });
      return [...billPaymentsToAdd, ...billPaymentCollection];
    }
    return billPaymentCollection;
  }

  protected convertDateFromClient<T extends IBillPayment>(billPayment: T): RestOf<T> {
    return {
      ...billPayment,
      billDate: billPayment.billDate.format(DATE_TIME_FORMAT)
    };
  }

  protected convertDateFromServer(restBillPayment: OrderResponse): OrderResponse {
    return {
      ...restBillPayment
      // createTime: restBillPayment.createTime ? (dayjs(restBillPayment.createTime))
    };
  }

  protected convertResponseFromServer(res: HttpResponse<any>) {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<OrderResponse[]>): HttpResponse<OrderResponse[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null
    });
  }
}
