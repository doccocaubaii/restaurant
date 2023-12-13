import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { CREATE_AREA_UNIT, DELETE_AREA_UNIT, UPDATE_AREA_UNIT } from '../../../constants/api.constants';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class UnitService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  postUnit(unit: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_AREA_UNIT, unit);
  }

  putUnit(id: number, comId: number, areaId: number, name: string): Observable<any> {
    let dto = { id: id, comId: comId, areaId: areaId, name: name };
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_AREA_UNIT, dto);
  }
  deleteUnit(id: number, areaId: number, comId: number) {
    let dto = { id: id, areaId: areaId, comId: comId };
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_AREA_UNIT, dto);
  }
}
