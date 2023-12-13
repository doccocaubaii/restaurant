import { map } from 'rxjs/operators';
import { HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, of, Subject } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import {
  CHECK_PRODUCT_PROCESSING_AREA_PRODUCT,
  CREATE_PROCESSING_AREA,
  CREATE_PRODUCT,
  DELETE_CATEGORY,
  DELETE_PROCESSING_AREA,
  DELETE_PROCESSING_AREA_PRODUCT,
  FILTER_PROCESSING_AREA,
  FIND_LIST_PRODUCT_PRODUCT_UNIT_ID,
  FIND_PROCESSING_AREA,
  FIND_PRODUCT_PROCESSING_AREA,
  PRODUCT_PROCESSING_AREA,
  UPDATE_PROCESSING_AREA,
} from '../../constants/api.constants';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class ProcessingAreaService {
  // private form: FormGroup;
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}

  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }

  getProcessingArea(filterProcessingArea: any): Observable<any> {
    const options = createRequestOption(filterProcessingArea);
    return this.http
      .get<any>(`${this.resourceUrl}` + FILTER_PROCESSING_AREA, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  create(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_PROCESSING_AREA, req);
  }

  deleteProcessingArea(id: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_PROCESSING_AREA + `${id}`, { observe: 'response' });
  }

  findProcessingArea(id: any, req?: any): Observable<any> {
    if (req) {
      const options = this.createRequestOption(req);
      return this.http.get<any>(`${this.resourceUrl}` + FIND_PROCESSING_AREA + `${id}`, { params: options, observe: 'response' });
    }
    return this.http.get<any>(`${this.resourceUrl}` + FIND_PROCESSING_AREA + `${id}`, { observe: 'response' });
  }

  findProduct(filterProduct: any): Observable<any> {
    const options = this.createRequestOption(filterProduct);
    return this.http
      .get<any>(`${this.resourceUrl}` + FIND_PRODUCT_PROCESSING_AREA, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  deleteProcessingAreaProduct(id: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_PROCESSING_AREA_PRODUCT + `${id}`, { observe: 'response' });
  }

  private searchSubject = new Subject<void>();

  searchObservable$ = this.searchSubject.asObservable();

  triggerSearch(): void {
    this.searchSubject.next();
  }

  update(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + UPDATE_PROCESSING_AREA, req);
  }

  findListProductProductUnitId(findListPPUId: any): Observable<any> {
    const options = this.createRequestOption(findListPPUId);
    return this.http
      .get<any>(`${this.resourceUrl}` + FIND_LIST_PRODUCT_PRODUCT_UNIT_ID, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductByProcessingAreaId(processingAreaId: any): Observable<any> {
    const options = this.createRequestOption(processingAreaId);
    return this.http
      .get<any>(`${this.resourceUrl}` + PRODUCT_PROCESSING_AREA, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  checkProduct(productProductUnitId: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + CHECK_PRODUCT_PROCESSING_AREA_PRODUCT + `${productProductUnitId}`, {
      observe: 'response',
    });
  }

  createRequestOption = (req?: any): HttpParams => {
    let options: HttpParams = new HttpParams();

    if (req) {
      Object.keys(req).forEach(key => {
        if (key !== 'sort' && (req[key] || req[key] === 0 || req[key] === false)) {
          for (const value of [].concat(req[key]).filter(v => v !== '')) {
            options = options.append(key, value);
          }
        }
      });

      if (req.sort) {
        req.sort.forEach((val: string) => {
          options = options.append('sort', val);
        });
      }
    }

    return options;
  };
}
