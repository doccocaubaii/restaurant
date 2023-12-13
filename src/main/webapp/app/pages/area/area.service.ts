import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable, of } from 'rxjs';
import { CREATE_AREA, DELETE_AREA, GET_ALL_LIST_AREA, UPDATE_AREA } from '../../constants/api.constants';
import { areaMockup, IArea } from '../../entities/area/area.model';

@Injectable({
  providedIn: 'root',
})
export class AreaService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getAreas(
    areaSize: number,
    areaUnitSize: number,
    reservationId?: any,
    areaKeyword?: any,
    areaUnitKeyword?: any,
    areaId?: any
  ): Observable<any> {
    let params = new HttpParams().set('areaSize', areaSize).set('areaUnitSize', areaUnitSize);
    if (areaKeyword) params = params.set('areaKeyword', areaKeyword);
    if (areaUnitKeyword) params = params.set('areaUnitKeyword', areaUnitKeyword);
    if (areaId) params = params.set('areaId', areaId);
    if (reservationId) {
      params = params.set('reservationId', reservationId);
    }
    // let myObject: { data: IArea[] } = {
    //   data: areaMockup,
    // };
    // return of(myObject);
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_LIST_AREA, { params });
  }

  postArea(area: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_AREA, area);
  }

  putArea(id: number, comId: number, name: string): Observable<any> {
    let dto = { id: id, comId: comId, name: name };
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_AREA, dto);
  }
  deleteArea(id: number, comId: number) {
    let dto = { id: id, comId: comId };
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_AREA, dto);
  }
}
