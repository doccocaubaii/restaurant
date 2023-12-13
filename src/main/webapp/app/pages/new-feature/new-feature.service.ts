import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { GET_ALL_NEW_FEATURE } from '../../constants/api.constants';
import { FormBuilder } from '@angular/forms';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class NewFeatureService {
  // private form: FormGroup;
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }

  checkEvent = new EventEmitter();

  checked(): void {
    this.checkEvent.emit();
  }

  getAllNewFeature(filterNewFeature: any): Observable<any> {
    const options = createRequestOption(filterNewFeature);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_ALL_NEW_FEATURE, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  // findRole(id: any): Observable<any> {
  //   return this.http.get<any>(`${this.resourceUrl}` + FIND_ROLE + `${id}`, { observe: 'response' });
  // }
}
