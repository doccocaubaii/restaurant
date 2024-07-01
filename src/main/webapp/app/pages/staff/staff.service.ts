import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../core/request/request-util';
import { OrderResponse } from '../pos/model/orderResponse.model';
import { CREATE_STAFF, GET_LIST_STAFF } from '../../constants/api.constants';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';

@Injectable({
  providedIn: 'root'
})
export class StaffService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(private http: HttpClient, protected applicationConfigService: ApplicationConfigService) {
  }

  page(req?: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http.get<OrderResponse[]>(`${this.resourceUrl}${GET_LIST_STAFF}`, {
      params: options,
      observe: 'response'
    });
  }

  updateStaff(selectedItem: any): Observable<any> {
     return this.http.post(`${this.resourceUrl}${CREATE_STAFF}`, selectedItem);
  }

  deleteStaffById(id: any): Observable<any>  {
    return this.http.delete(`${this.resourceUrl}${CREATE_STAFF}/${id}`);
  }
}
