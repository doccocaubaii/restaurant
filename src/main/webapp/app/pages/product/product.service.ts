import { map } from 'rxjs/operators';
import { HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import {
  CREATE_PRODUCT,
  DELETE_PRODUCT_BY_ID,
  GET_ALL_PRODUCT,
  GET_PRODUCT_BY_ID,
  GET_PRODUCT_PAGING,
  UPDATE_PRODUCT,
} from '../../constants/api.constants';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // private form: FormGroup;
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getAllProduct(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_PRODUCT);
  }
  getProductWithPaging(filterProduct: any): Observable<any> {
    const options = createRequestOption(filterProduct);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PRODUCT_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductById(id: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_PRODUCT_BY_ID + `${id}`);
  }
  updateProduct(req: any): Observable<any> {
    let formData = this.buildFormData(req);
    formData.append('id', req.id ? req.id : '');
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_PRODUCT, formData);
  }
  createProduct(req: any): Observable<any> {
    let formData = this.buildFormData(req);
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_PRODUCT, formData);
  }
  buildFormData(req: any) {
    let formData = new FormData();
    if (req.images) {
      formData.append('images', req.images, req.images.name);
    }
    formData.append('comId', req.comId ? req.comId : '');
    formData.append('name', req.name ? req.name : '');
    formData.append('unit', req.unit ? req.unit : '');
    formData.append('unitId', req.unitId ? req.unitId : '');
    formData.append('salePrice', req.salePrice ? req.salePrice : 0);
    formData.append('purchasePrice', req.purchasePrice ? req.purchasePrice : 0);
    formData.append('code2', req.code2 ? req.code2 : '');
    formData.append('barcode', req.barcode ? req.barcode : '');
    formData.append('inventoryTracking', req.inventoryTracking ? req.inventoryTracking : false);
    formData.append('inventoryCount', req.inventoryCount ? req.inventoryCount : 0);
    formData.append('vatRate', req.vatRate !== null && req.vatRate !== undefined ? req.vatRate : '');
    formData.append('description', req.description ? req.description : '');
    formData.append('groups', req.groups ? req.groups : []);
    formData.append('conversionUnits', req.conversionUnits ? JSON.stringify(req.conversionUnits) : '');
    return formData;
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<any> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body,
    });
  }
  deleteProductById(id: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_PRODUCT_BY_ID + `${id}`, null);
  }
}
