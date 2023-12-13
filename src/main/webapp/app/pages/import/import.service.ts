import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { HttpClient, HttpHeaders, HttpResponse } from '@angular/common/http';
import { IMPORT_EXCEL_SAVE_PRODUCT, IMPORT_EXCEL_VALIDATE_PRODUCT } from '../../constants/api.constants';
import { IResultDTO } from '../../core/response/result-dto.model';
import { LoadingOption } from '../../utils/loadingOption';
import { ImportProductResponseData } from './excel/product/import-product-response-data';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'Application/json' }),
};

export type EntityResponseType = HttpResponse<IResultDTO>;

@Injectable({
  providedIn: 'root',
})
export class ImportService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  validateImport(file: any, sheetIndex: number, comId: number, urlApi: string): Observable<EntityResponseType> {
    const formData = new FormData();
    if (file) {
      formData.append('file', file, file.name);
    }
    formData.append('comId', JSON.stringify(comId));
    formData.append('indexSheet', JSON.stringify(sheetIndex));
    return this.http.post<IResultDTO>(`${this.resourceUrl}` + urlApi, formData, { observe: 'response' });
  }

  saveDataImport(data: any, urlApi: string): Observable<EntityResponseType> {
    return this.http.post<IResultDTO>(`${this.resourceUrl}` + urlApi, data, { observe: 'response' });
  }
}
