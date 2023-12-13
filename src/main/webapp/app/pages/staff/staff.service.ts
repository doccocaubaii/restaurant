import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable, Subject } from 'rxjs';
import {
  CREATE_STAFF,
  DELETE_STAFF,
  FILTER_COMPANY,
  FIND_STAFF,
  GET_ALL_ROLES,
  GET_STAFF_WITH_PAGING,
  UPDATE_STAFF,
} from '../../constants/api.constants';
import { IStaff } from 'app/entities/staff/staff.model';

@Injectable({
  providedIn: 'root',
})
export class StaffService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getStaff(page: number, pageSize: number, sort?: string, keyword?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', String(page - 1))
      .set('size', String(pageSize))
      .set('isCountAll', 'true');

    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (sort) {
      params = params.set('sort', sort);
    }
    return this.http.get<any>(`${this.resourceUrl}` + GET_STAFF_WITH_PAGING, { params });
  }

  getAllRole(filterRole: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_ROLES, { params: filterRole, observe: 'response' });
  }

  postStaff(staff: IStaff): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_STAFF, staff);
  }

  putStaff(staff: IStaff): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_STAFF, staff);
  }
  delStaff(id: Number): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_STAFF + `/${id}`, null);
  }

  getCompany(filterCompany: any) {
    return this.http.get<any>(`${this.resourceUrl}` + FILTER_COMPANY, { params: filterCompany, observe: 'response' });
  }

  findStaff(id: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + FIND_STAFF + `${id}`, { observe: 'response' });
  }

  private searchSubject = new Subject<void>();

  searchObservable$ = this.searchSubject.asObservable();

  triggerSearch(): void {
    this.searchSubject.next();
  }
}
