import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ApplicationConfigService } from 'app/core/config/application-config.service';

@Injectable({ providedIn: 'root' })
export class PasswordResetInitService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  saveData: any;

  save(mail: string): Observable<any> {
    let req = {
      username: mail,
    };
    return this.http.post<any>(this.applicationConfigService.getEndpointFor('api/client/common/forgot-password'), req);
  }

  confirmOtp(req?: any): Observable<any> {
    return this.http.post<any>(this.applicationConfigService.getEndpointFor('api/p/client/page/otp/check-otp'), req);
  }
}
