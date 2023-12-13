import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ProductSaleStatusInput, ReportInventoryInput, ReportInvoiceInput } from '../model/report-invoice.model';
import {
  ACTIVITY_HISTORY_STATS,
  BILL_COMMON_STATUS,
  GENERAL_INPUT_OUTPUT_INVENTORY,
  GET_ACTIVITY_HISTORY,
  INVENTORY_COMMON_STATUS,
  INVENTORY_COMMON_STATUS_V2,
  INVOICE_COMMON_STATUS,
  PRODUCT_HOT_SALES_STATS,
  PRODUCT_PROFIT_STATUS,
  PRODUCT_SALE_STATUS,
  RECENT_ACTIVITES,
  REVENUE_COMMON_STATUS,
  REVENUE_COST_PRICE_BY_PRODUCT,
} from 'app/constants/api.constants';
import { createRequestOption } from 'app/core/request/request-util';
import { map, Observable } from 'rxjs';
import { DATE_FORMAT } from 'app/config/input.constants';

@Injectable({
  providedIn: 'root',
})
export class ReportInvoiceService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('/api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getRevenueCommonStatus(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    // options = this.convertDateFromClient(options);
    return this.http
      .get(`${this.resourceUrl}${REVENUE_COMMON_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getBillCommonStatus(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${BILL_COMMON_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getInvoiceCommonStatus(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${INVOICE_COMMON_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getRevenueCostPriceByProduct(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${REVENUE_COST_PRICE_BY_PRODUCT}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getGeneralInputOutputInventory(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${GENERAL_INPUT_OUTPUT_INVENTORY}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getInventoryCommonstatus(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${INVENTORY_COMMON_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getInventoryCommonstatusV2(reportInventoryInput: ReportInventoryInput) {
    const param = createRequestOption(reportInventoryInput);
    return this.http
      .get(`${this.resourceUrl}${INVENTORY_COMMON_STATUS_V2}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getRecentActivites(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${RECENT_ACTIVITES}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductProfitStatus(reportInvoiceInput: ReportInvoiceInput) {
    const options = this.convertDateFromClient(reportInvoiceInput);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${PRODUCT_PROFIT_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductSaleStatus(productSaleStatus: ProductSaleStatusInput) {
    const param = createRequestOption(productSaleStatus);
    return this.http
      .get(`${this.resourceUrl}${PRODUCT_SALE_STATUS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductHotSaleStatus(productSaleStatus: ReportInvoiceInput) {
    const options = this.convertDateFromClient(productSaleStatus);
    const param = createRequestOption(options);
    return this.http
      .get(`${this.resourceUrl}${PRODUCT_HOT_SALES_STATS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getactivityHistoryStats(productSaleStatus: ReportInvoiceInput) {
    const param = createRequestOption(productSaleStatus);
    return this.http
      .get(`${this.resourceUrl}${ACTIVITY_HISTORY_STATS}`, { params: param, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  protected convertDateFromClient(reportInvoiceInput: ReportInvoiceInput) {
    return {
      ...reportInvoiceInput,
      fromDate: reportInvoiceInput.fromDate.format(DATE_FORMAT),
      toDate: reportInvoiceInput.toDate.format(DATE_FORMAT),
    };
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>) {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body ? res.body.data : null,
    });
  }
  getActivityHistory(searchReq: any): Observable<any> {
    const options = createRequestOption(searchReq);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_ACTIVITY_HISTORY, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
}
