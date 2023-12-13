import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { map, mergeMap } from 'rxjs/operators';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import {
  ACCOUNT,
  DELETE_INVOICE,
  FIND_INVOICE_BY_ID,
  GET_COMPANY_CONFIG,
  GET_INVOICE_WITH_PAGING,
  GET_OWNER_INFO,
  LOGIN,
  LOGIN_INVOICE,
  MODIFY_INVOICE,
  PUBLISH_INVOICE,
  PUBLISH_INVOICE_LIST,
  SEND_MAIL_INVOICE,
  SYNC_INVOICE,
  SYNC_INVOICE_SAVE,
  UPDATE_INFO_INVOICE_CONFIG,
  UPDATE_INVOICE_CONFIG,
  UPDATE_SALE_INVOICE_CONFIG,
  VIEW_INVOICE_PDF,
} from '../../../constants/api.constants';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Account } from '../../../core/auth/account.model';
import { HttpClient } from '@angular/common/http';
import { LoginInvoiceModel } from '../../../core/dto/login-invoice.model';
import { computeMsgId } from '@angular/compiler';
import { createRequestOption } from '../../../core/request/request-util';

@Injectable({ providedIn: 'root' })
export class InvoiceService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(
    private http: HttpClient,
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private applicationConfigService: ApplicationConfigService
  ) {}

  updateInfoConfig(account: LoginInvoiceModel): Observable<any> {
    return this.http.put<any>(this.resourceUrl + UPDATE_INFO_INVOICE_CONFIG, account);
  }

  publishInvoice(req?: any): Observable<any> {
    return this.http.post<any>(this.resourceUrl + PUBLISH_INVOICE, req);
  }

  deleteInvoice(id: any): Observable<any> {
    return this.http.put<any>(this.resourceUrl + DELETE_INVOICE + id, {});
  }
  modifyInvoice(req: any): Observable<any> {
    return this.http.put<any>(this.resourceUrl + MODIFY_INVOICE + req.id, req);
  }

  updateInvoiceConfig(invoiceConfig: any): Observable<any> {
    return this.http.put<any>(this.resourceUrl + UPDATE_SALE_INVOICE_CONFIG, invoiceConfig);
  }

  getOwnerInfo(comId: number): Observable<any> {
    return this.http.get<any>(this.resourceUrl + GET_OWNER_INFO + comId);
  }

  viewInvoicePdf(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceUrl + VIEW_INVOICE_PDF, {
      params: options,
      observe: 'response',
    });
  }
  sendMailInvoice(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.post<any>(this.resourceUrl + SEND_MAIL_INVOICE, req);
  }

  getCompanyConfig(comId: number): Observable<any> {
    return this.http.get<any>(this.resourceUrl + GET_COMPANY_CONFIG + comId);
  }

  getInvoiceWithPaging(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.applicationConfigService.getEndpointFor('api' + GET_INVOICE_WITH_PAGING), {
      params: req,
      observe: 'response',
    });
  }

  publishInvoiceList(req?: any): Observable<any> {
    return this.http.post<any>(this.applicationConfigService.getEndpointFor('api' + PUBLISH_INVOICE_LIST), req);
  }

  getDeviceCode(device: any): Observable<any> {
    const options = createRequestOption(device);
    return this.http.get<any>(this.resourceUrl + GET_COMPANY_CONFIG, {
      params: options,
      observe: 'response',
    });
  }

  find(id: number): Observable<any> {
    return this.http.get<any>(this.resourceUrl + FIND_INVOICE_BY_ID + id);
  }

  findEI(id: number, viewType: any): Observable<any> {
    const req = {
      viewType: viewType,
    };
    return this.http.get<any>(this.resourceUrl + FIND_INVOICE_BY_ID + id, { params: req });
  }

  syncInvoiceEI(req?: any): Observable<any> {
    return this.http.post<any>(this.applicationConfigService.getEndpointFor('api' + SYNC_INVOICE), req);
  }

  saveSyncInvoiceEI(req?: any): Observable<any> {
    return this.http.post<any>(this.applicationConfigService.getEndpointFor('api' + SYNC_INVOICE_SAVE), req);
  }

  getDigestData(req: any) {
    return this.http.post<any>(this.resourceUrl + '/client/page/invoice/get-digest-data', req, { observe: 'response' });
  }

  publishInvoiceWithCert(req: any) {
    return this.http.post<any>(this.resourceUrl + '/client/page/invoice/sign-with-digest-data', req, { observe: 'response' });
  }
}
