import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { LocalStorageService, SessionStorageService } from 'ngx-webstorage';

import { ApplicationConfigService } from '../config/application-config.service';
import { CHANGE_SESSION, LOGIN } from '../../constants/api.constants';
import jwt_decode from 'jwt-decode';
type JwtToken = {
  id_token: string;
};

@Injectable({ providedIn: 'root' })
export class AuthServerProvider {
  constructor(
    private http: HttpClient,
    private localStorageService: LocalStorageService,
    private sessionStorageService: SessionStorageService,
    private applicationConfigService: ApplicationConfigService
  ) {}

  getToken(): string {
    const tokenInLocalStorage: string | null = this.localStorageService.retrieve('authenticationToken');
    const tokenInSessionStorage: string | null = this.sessionStorageService.retrieve('authenticationToken');
    return tokenInLocalStorage ?? tokenInSessionStorage ?? '';
  }

  login(credentials: any): Observable<void> {
    return this.http
      .post<JwtToken>(this.applicationConfigService.getEndpointFor('api' + LOGIN), credentials)
      .pipe(map(response => this.authenticateSuccess(response, credentials.rememberMe)));
  }

  changeSession(comId: number): Observable<void> {
    return this.http.post<JwtToken>('api' + CHANGE_SESSION, comId).pipe(map(response => this.authenticateSuccess(response, false)));
  }

  logout(): Observable<void> {
    return new Observable(observer => {
      this.localStorageService.clear('authenticationToken');
      this.sessionStorageService.clear('authenticationToken');
      observer.complete();
    });
  }

  private authenticateSuccess(response: any, rememberMe: boolean): void {
    let jwt = null;
    if (!response.status) {
      return response;
    } else {
      jwt = response.data.id_token;
    }
    // if (rememberMe) {
    //   this.localStorageService.store('authenticationToken', jwt);
    //   // this.sessionStorageService.clear('authenticationToken');
    // } else {
    this.sessionStorageService.store('authenticationToken', jwt);
    this.localStorageService.store('authenticationToken', jwt);
    // }
    return response;
  }

  isJwtExpired(): boolean {
    const tokenITem = this.localStorageService.retrieve('authenticationToken');
    if (tokenITem) {
      const decodedToken = jwt_decode(tokenITem) as { exp: number };
      if (!decodedToken || !decodedToken.exp) {
        return true; // Không thể giải mã JWT hoặc không tìm thấy thời gian hết hạn
      }
      const expirationTime = decodedToken.exp;
      const currentTime = Math.floor(Date.now() / 1000);
      return currentTime + 600 > expirationTime;
    }
    return true;
  }
}
