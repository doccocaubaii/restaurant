import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { ITypeGroup } from './type-group.model';
import { createRequestOption } from '../../../core/request/request-util';

type EntityResponseType = HttpResponse<ITypeGroup>;
type EntityArrayResponseType = HttpResponse<ITypeGroup[]>;

@Injectable({ providedIn: 'root' })
export class TypeGroupService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api/type-groups');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeGroup: ITypeGroup): Observable<EntityResponseType> {
    return this.http.post<ITypeGroup>(this.resourceUrl, typeGroup, { observe: 'response' });
  }

  update(typeGroup: ITypeGroup): Observable<EntityResponseType> {
    return this.http.put<ITypeGroup>(this.resourceUrl, typeGroup, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeGroup[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  queryForPopup(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeGroup[]>(`${this.resourceUrl}/popup`, { params: options, observe: 'response' });
  }

  queryForPrint(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeGroup[]>(`${this.resourceUrl}/popup-print`, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<any>> {
    return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  findTypeGroupByType(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeGroup[]>(this.resourceUrl + `/find-type-group`, { params: options, observe: 'response' });
  }

  findAllByTypeGroup(): Observable<EntityArrayResponseType> {
    return this.http.get<ITypeGroup[]>(`${this.resourceUrl}/find-all`, { observe: 'response' });
  }

  filterForTemplateConfig() {
    return this.http.get<ITypeGroup[]>(`${this.resourceUrl}/filter-by-template-config`, { observe: 'response' });
  }
  queryForImport(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeGroup[]>(`${this.resourceUrl}/popup-import`, { params: options, observe: 'response' });
  }
}
