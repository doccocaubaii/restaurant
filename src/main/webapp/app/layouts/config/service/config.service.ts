import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountService } from 'app/core/auth/account.service';
import { AuthServerProvider } from 'app/core/auth/auth-jwt.service';
import {
  GET_ALL_PRINT_CONFig,
  GET_DEVICE_CODE,
  LOGIN_INVOICE,
  REGISTER_DEVICE,
  UPDATE_LIST_PRINT_CONFIG,
  UPDATE_PRINT_CONFIG,
} from '../../../constants/api.constants';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { HttpClient } from '@angular/common/http';
import { LoginInvoiceModel } from '../../../core/dto/login-invoice.model';
import { createRequestOption } from '../../../core/request/request-util';

@Injectable({ providedIn: 'root' })
export class ConfigService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(
    private http: HttpClient,
    private accountService: AccountService,
    private authServerProvider: AuthServerProvider,
    private applicationConfigService: ApplicationConfigService
  ) {}

  createDevice(req: any): Observable<any> {
    return this.http.post<any>(this.resourceUrl + REGISTER_DEVICE, req);
  }
  updatePrintConfig(req: any): Observable<any> {
    return this.http.put<any>(this.resourceUrl + UPDATE_PRINT_CONFIG, req);
  }
  updateListPrintConfig(req: any): Observable<any> {
    return this.http.put<any>(this.resourceUrl + UPDATE_LIST_PRINT_CONFIG, req);
  }
  getDeviceInfo(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<any>(this.resourceUrl + GET_DEVICE_CODE, {
      params: options,
      observe: 'response',
    });
  }
  getAllPrintConfig(id: any): Observable<any> {
    return this.http.get<any>(this.resourceUrl + GET_ALL_PRINT_CONFig + id);
  }
}
