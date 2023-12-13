import { map } from 'rxjs/operators';
import { HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import {ApplicationConfigService} from "../../../core/config/application-config.service";
import {FormBuilder} from "@angular/forms";
import {CREATE_PRINT_CONFIG, CREATE_PRODUCT, CREATE_UNIT, GET_ALL_PRINT_CONFIG} from "../../../constants/api.constants";

@Injectable({
  providedIn: 'root'
})
export class OrderPrintConfigService {

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) { }

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');
  getAllPrintConfig(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_PRINT_CONFIG);
  }
  createPrintConfig(code: any, name: any, content: any, pageSize: any) {
    let req: any = {};
    req.code = code;
    req.name = name;
    req.content = content;
    req.pageSize = pageSize;
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_PRINT_CONFIG, req);
  }
}
