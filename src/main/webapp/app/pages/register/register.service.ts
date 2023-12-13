import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { REGISTER, REGISTER_CHECK_OTP, REGISTER_SEND_OTP } from '../../constants/api.constants';
import { IResultDTO } from '../../core/response/result-dto.model';

export type EntityResponseType = HttpResponse<IResultDTO>;
@Injectable({ providedIn: 'root' })
export class RegisterService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  sendOtp(request: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + REGISTER_SEND_OTP, request, { observe: 'response' });
  }

  checkOtp(request: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + REGISTER_CHECK_OTP, request, { observe: 'response' });
  }

  register(request: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + REGISTER, request, { observe: 'response' });
  }
}
