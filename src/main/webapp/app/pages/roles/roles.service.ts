import { map } from 'rxjs/operators';
import { HttpResponse } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { EventEmitter, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { CREATE_ROLES, DELETE_ROLES, FILTER_PERMISSION, FILTER_ROLES, FIND_ROLE, UPDATE_ROLES } from '../../constants/api.constants';
import { FormBuilder } from '@angular/forms';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class RolesService {
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

  getRoles(filterRoles: any): Observable<any> {
    const options = createRequestOption(filterRoles);
    return this.http
      .get<any>(`${this.resourceUrl}` + FILTER_ROLES, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getPermissions(): Observable<any> {
    return this.http
      .get<any>(`${this.resourceUrl}` + FILTER_PERMISSION, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  findRole(id: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + FIND_ROLE + `${id}`, { observe: 'response' });
  }

  create(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_ROLES, req);
  }

  update(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_ROLES, req);
  }

  delete(id: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_ROLES + `${id}`, { observe: 'response' });
  }

  private searchSubject = new Subject<void>();

  searchObservable$ = this.searchSubject.asObservable();

  triggerSearch(): void {
    this.searchSubject.next();
  }
}
