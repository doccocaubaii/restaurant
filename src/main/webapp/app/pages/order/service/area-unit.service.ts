import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { CREATE_AREA_UNIT, DELETE_AREA_UNIT, GET_AREA_UNIT_BY_ID, UPDATE_AREA_UNIT } from 'app/constants/api.constants';
import { AreaUnit } from '../model/area-unit.model';

@Injectable({
  providedIn: 'root',
})
export class AreaUnitService {
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(area: AreaUnit) {
    return this.http.post(`${this.resourceUrl}${CREATE_AREA_UNIT}`, area, { observe: 'response' });
  }

  update(area: AreaUnit) {
    return this.http.put(`${this.resourceUrl}${UPDATE_AREA_UNIT}`, area, { observe: 'response' });
  }

  find(id: number) {
    return this.http.get(`${this.resourceUrl}${GET_AREA_UNIT_BY_ID}${id}`, { observe: 'response' });
  }

  delete(id: number) {
    return this.http.put(`${this.resourceUrl}${DELETE_AREA_UNIT}${id}`, { observe: 'response' });
  }
}
