import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { Customer } from '../model/customer.model';
import { map } from 'rxjs';
import {
  CHECK_VOUCHER_VALID,
  CREATE_CUSTOMER,
  DELETE_CUSTOMER,
  GET_CUSTOMER_BY_ID,
  GET_LIST_CUSTOMER,
  SAVE_POINT,
  UPDATE_CUSTOMER,
  GET_LIST_PRODUCT_BY_PRODUCT_IDS,
  GET_PRODUCT_GROUP_BY_ID,
  GET_LIST_PRODUCT_BY_PRODUCT_GROUP_ID,
  GET_VOUCHER_FOR_CUSTOMER_WEB,
  GET_VOUCHER_FOR_CUSTOMER,
} from 'app/constants/api.constants';
import { SavePointInput } from '../model/bill-payment.model';
import { ListProductToppingReq } from '../../product/product';
@Injectable({
  providedIn: 'root',
})
export class CustomerService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(customer: Customer) {
    return this.http.post(`${this.resourceUrl}${CREATE_CUSTOMER}`, customer, { observe: 'response' });
  }

  update(customer: Customer) {
    return this.http.put(`${this.resourceUrl}${UPDATE_CUSTOMER}`, customer, { observe: 'response' });
  }

  find(id: number) {
    return this.http.get(`${this.resourceUrl}${GET_CUSTOMER_BY_ID}${id}`, { observe: 'response' });
  }

  savePoint(savePointInput: SavePointInput) {
    return this.http.post(`${this.resourceUrl}${SAVE_POINT}`, savePointInput, { observe: 'response' });
  }

  query(req?: any) {
    // const options = createRequestOption(req);
    return this.http
      .get(`${this.resourceUrl}${GET_LIST_CUSTOMER}`, { params: req, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getVoucher(res) {
    const options = createRequestOption(res);
    return this.http.get(`${this.resourceUrl}${GET_VOUCHER_FOR_CUSTOMER}`, { params: options, observe: 'response' });
  }

  getProductFromProductId(listProductId) {
    return this.http.post(`${this.resourceUrl}${GET_LIST_PRODUCT_BY_PRODUCT_IDS}`, listProductId, { observe: 'response' });
  }

  getProductGroupFromProductGroupId(listProductGroupId) {
    return this.http.post(`${this.resourceUrl}${GET_LIST_PRODUCT_BY_PRODUCT_GROUP_ID}`, listProductGroupId, { observe: 'response' });
  }

  checkVoucher(listVoucherId: number[], customerId: number) {
    return this.http.post(`${this.resourceUrl}${CHECK_VOUCHER_VALID}`, { ids: listVoucherId, customerId }, { observe: 'response' });
  }

  delete(id: number) {
    return this.http.put(`${DELETE_CUSTOMER}${id}`, { observe: 'response' });
  }

  protected convertResponseArrayFromServer(res) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body ? res.body.data.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  protected convertDateFromServer(RestProduct) {
    return {
      ...RestProduct,
    };
  }
}
