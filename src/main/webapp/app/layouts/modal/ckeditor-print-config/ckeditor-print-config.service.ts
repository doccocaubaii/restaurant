import { Injectable } from '@angular/core';
import {HttpClient, HttpResponse} from "@angular/common/http";
import {ApplicationConfigService} from "../../../core/config/application-config.service";
import {FormBuilder} from "@angular/forms";
import {Observable} from "rxjs";
import {
  CREATE_PRINT_CONFIG, DELETE_PRINT_CONFIG,
  EDIT_PRINT_CONFIG,
  GET_ALL_PRINT_CONFIG, GET_CATEGORY_PAGING,
  GET_DATA_DEFAULT, GET_PRINT_COFIG_DEFAULT
} from "../../../constants/api.constants";
import {createRequestOption} from "../../../core/request/request-util";
import {map} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class CkeditorPrintConfigService {

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) { }

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getAllPrintConfig(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_PRINT_CONFIG);
  }
  createPrintConfig(name: any, content: any, pageSize: any, contentParams, typeTemplate: any) {
    let req: any = {};
    req.name = name;
    req.content = content;
    req.pageSize = pageSize;
    req.contentParams = contentParams;
    req.typeTemplate = typeTemplate;
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_PRINT_CONFIG, req);
  }
  updatePrintConfig(id: any, name: any, content: any, pageSize: any, contentParams, typeTemplate: any) {
    let req: any = {};
    req.name = name;
    req.content = content;
    req.pageSize = pageSize;
    req.id = id;
    req.contentParams = contentParams;
    req.typeTemplate = typeTemplate;
    return this.http.put<any>(`${this.resourceUrl}` + EDIT_PRINT_CONFIG, req);
  }
  getPrintConfigDefault(type: any): Observable<any> {
    let req = {
      type: type
    };
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PRINT_COFIG_DEFAULT, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  getDataDefault(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_DATA_DEFAULT);
  }
  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }

  deletePrintConfig(comId: any, id: any) {
    let req: any = {};
    req.comId = comId;
    req.id = id;
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_PRINT_CONFIG, req);
  }
}
