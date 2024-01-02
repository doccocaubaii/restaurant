import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { CHANGE_PASSWORD, CREATE_CUSTOMER } from '../../constants/api.constants';

@Injectable({ providedIn: 'root' })
export class PasswordService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  save(currentPassword: string, newPassword: string, confirmPassword: string, isLogout: boolean): Observable<{}> {
    return this.http.post<any>(`${this.resourceUrl}` + CHANGE_PASSWORD, {
      oldPassword: currentPassword,
      newPassword: newPassword,
      confirmPassword: confirmPassword,
      isLogoutAll: isLogout,
    });
  }
}
