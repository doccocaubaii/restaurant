import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { IResultDTO } from '../../core/response/result-dto.model';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { ToastrService } from 'ngx-toastr';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'Application/json' }),
};

export type EntityResponseType = HttpResponse<IResultDTO>;

@Injectable({
  providedIn: 'root',
})
export class ExportService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private toast: ToastrService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  exportErrorData(data: any, fileName: string, comId: number, urlApi: string) {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(this.resourceUrl + urlApi, data, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
