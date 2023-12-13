import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { map } from 'rxjs';
import { CREATE_AREA, GET_ALL_LIST_AREA, GET_AREA_BY_ID, UPDATE_AREA } from 'app/constants/api.constants';
import { Area } from '../model/area.model';
import { FilterArea } from '../model/filterArea.model';

@Injectable({
  providedIn: 'root',
})
export class AreaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(area: Area) {
    return this.http.post(`${this.resourceUrl}${CREATE_AREA}`, area, { observe: 'response' });
  }

  getAllProductUnit(req?: any) {
    const options = createRequestOption(req);
    return this.http.get(`${this.resourceUrl}${GET_ALL_LIST_AREA}`, { params: options, observe: 'response' });
  }

  update(area: Area) {
    return this.http.put(`${this.resourceUrl}${UPDATE_AREA}`, area, { observe: 'response' });
  }

  find(id: number) {
    return this.http.get(`${this.resourceUrl}${GET_AREA_BY_ID}${id}`, { observe: 'response' });
  }

  query(req?: FilterArea) {
    const options = createRequestOption(req);
    return this.http
      .get(`${this.resourceUrl}${GET_ALL_LIST_AREA}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number) {
    return this.http.put(`${this.resourceUrl}/delete/${id}`, { observe: 'response' });
  }

  protected convertResponseArrayFromServer(res) {
    return res.clone({
      body: res.body ? res.body.data : null,
    });
  }
}
