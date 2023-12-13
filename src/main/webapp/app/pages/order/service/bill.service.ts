import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { CancelOrder, CompleteOrder, IBillPayment } from '../model/bill-payment.model';
import { OrderResponse } from '../model/orderResponse.model';
import {
  BILL_CANCEL,
  CHECKOUT_BILL_BY_ID,
  CREATE_BILL,
  DELETE_PRODUCT_BY_ID,
  GET_BILL_BY_ID,
  GET_LIST_BILL,
  PAY_OFF_DEBT_BILL,
  UPDATE_BILL,
  SYNC_BILL,
  GET_DATA_PRINT_TEMPALTE,
  RETURN_BILL,
} from 'app/constants/api.constants';
import { CANCEL_BILL_BY_ID } from 'app/constants/api.constants';

export type PartialUpdateBillPayment = Partial<IBillPayment> & Pick<IBillPayment, 'id'>;

type RestOf<T extends IBillPayment | any> = Omit<T, 'createTime' | 'updateTime' | 'paymentTime'> & {
  billDate?: string | null;
};

export type RestBillPayment = RestOf<IBillPayment>;

export type NewRestBillPayment = RestOf<any>;

export type EntityResponseType = HttpResponse<OrderResponse>;
export type EntityArrayResponseType = HttpResponse<OrderResponse[]>;

@Injectable({
  providedIn: 'root',
})
export class BillService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(billPayment: IBillPayment) {
    const copy = this.convertDateFromClient(billPayment);
    return this.http.post(`${this.resourceUrl}${CREATE_BILL}`, copy, { observe: 'response' });
  }

  syncOrder(billPayment: any) {
    return this.http.post(`${this.resourceUrl}${SYNC_BILL}`, billPayment, { observe: 'response' });
  }

  syncAllOrder(billPayment: any[]) {
    const copy = billPayment;
    return this.http.post(`${this.resourceUrl}${SYNC_BILL}`, copy, { observe: 'response' });
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
    return this.http.put(`${this.resourceUrl}${CHECKOUT_BILL_BY_ID}`, { ...completeOrder, paymentTime }, { observe: 'response' });
  }

  payOffDebt(payOffDebtOrder) {
    return this.http.post(`${this.resourceUrl}${PAY_OFF_DEBT_BILL}`, payOffDebtOrder, { observe: 'response' });
  }

  find(id: number) {
    return this.http.get(`${this.resourceUrl}${GET_BILL_BY_ID}${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<OrderResponse[]>(`${this.resourceUrl}${GET_LIST_BILL}`, { params: options, observe: 'response' });
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
      billDate: billPayment.billDate.format(DATE_TIME_FORMAT),
    };
  }

  protected convertDateFromServer(restBillPayment: OrderResponse): OrderResponse {
    return {
      ...restBillPayment,
      // createTime: restBillPayment.createTime ? (dayjs(restBillPayment.createTime))
    };
  }

  protected convertResponseFromServer(res: HttpResponse<any>) {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<OrderResponse[]>): HttpResponse<OrderResponse[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  billCancel(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + BILL_CANCEL, req);
  }

  getDataPrint(billId: any, comId: any) {
    let req = {
      billId: billId,
      comId: comId,
    };
    const options = createRequestOption(req);
    return this.http.get<any>(`${this.resourceUrl}${GET_DATA_PRINT_TEMPALTE}`, { params: options, observe: 'response' });
  }

  returnBill(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + RETURN_BILL, req);
  }
}
