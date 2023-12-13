import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDialogComponent } from '../modal/confirm-dialog/confirm-dialog.component';
import { LoginInvoiceModel } from '../../core/dto/login-invoice.model';
import { Observable } from 'rxjs';
import { GET_VERSION_UPDATE_TODAY, LOGIN_INVOICE, NEWEST_NEW_FEATURE } from '../../constants/api.constants';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class VersionUpdateService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  newestNewFeature: any;
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  getVersionUpdate(): Observable<any> {
    const listSystem: any[number] = [0, 2];
    const listType: any[string] = ['BAOTRI', 'KHOA'];
    return this.http.get<any>(this.resourceUrl + GET_VERSION_UPDATE_TODAY, {
      params: { listSystem, listType },
      observe: 'response',
    });
  }

  getNewestNewFeature(): Observable<any> {
    const listSystem: any[number] = [0, 2];
    return this.http.get<any>(this.resourceUrl + NEWEST_NEW_FEATURE, { params: { listSystem }, observe: 'response' });
  }
}
