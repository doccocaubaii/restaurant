import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { Observable } from 'rxjs';
import {
  CREATE_CARD,
  DELETE_CARD,
  DELETE_LIST_CARD,
  GET_CARD_BY_ID,
  GET_LIST_CARD,
  SORT_CARD,
  UPDATE_CARD,
} from '../../constants/api.constants';

@Injectable({
  providedIn: 'root',
})
export class CardService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getListCard(keyword: string): Observable<any> {
    let params = new HttpParams();
    if (keyword) {
      params = params.set('keyword', keyword);
    }
    return this.http.get<any>(`${this.resourceUrl}` + GET_LIST_CARD, { params });
  }

  getCardById(id: number) {
    return this.http.get<any>(`${this.resourceUrl}` + GET_CARD_BY_ID + `${id}`);
  }

  createCard(req: any) {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_CARD, req);
  }

  updateCard(req: any) {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_CARD, req);
  }

  deleteCard(id: number) {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_CARD + `/${id}`, null);
  }

  deleteListCard(req: any) {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_LIST_CARD, req);
  }

  sortCard(req: any) {
    return this.http.post<any>(`${this.resourceUrl}` + SORT_CARD, req);
  }
}
