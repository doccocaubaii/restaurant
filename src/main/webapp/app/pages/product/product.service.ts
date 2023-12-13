import { map } from 'rxjs/operators';
import { HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import {
  CREATE_CATEGORY,
  CREATE_PRODUCT,
  CREATE_TOPPING_GROUP,
  CREATE_UNIT,
  DELETE_CATEGORY,
  DELETE_MULTI_CATEGORY,
  DELETE_MULTI_PRODUCT,
  DELETE_MULTI_UNIT,
  DELETE_PRODUCT_BY_ID,
  DELETE_UNIT,
  DELETE_PRODUCT_CONVERSION_UNIT,
  DELETE_TOPPING_GROUP,
  EXPORT_PRODUCT,
  GET_ALL_CATEGORY_OFFLINE,
  GET_ALL_PRODUCT,
  GET_ALL_PRODUCT_OFFLINE,
  GET_CATEGORY_PAGING,
  GET_COMPANY_CONFIGS,
  GET_PROCESSING_AREA,
  GET_PRODUCT_BY_ID,
  GET_PRODUCT_PAGING,
  GET_PRODUCT_TOPPING,
  GET_PRODUCT_UNIT,
  GET_TOPPING_GROUP_BY_ID,
  GET_TOPPING_PAGING,
  GET_UNIT_PAGING,
  UPDATE_CATEGORY,
  UPDATE_PRODUCT,
  UPDATE_TOPPING_GROUP,
  UPDATE_UNIT,
  GET_WITH_PAGING_BARCODE,
  INVENTORY_STATS_EXCEL,
  BARCODE_EXCEL,
} from '../../constants/api.constants';
import { CreateUnit, ProductObjs } from './product';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { createRequestOption } from '../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  // private form: FormGroup;
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private fb: FormBuilder) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getProductCategory(filterCategory: any): Observable<any> {
    const options = createRequestOption(filterCategory);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_CATEGORY_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  getAllCategory(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_CATEGORY_OFFLINE);
  }
  getAllProduct(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_ALL_PRODUCT);
  }
  getProductWithPaging(filterProduct: any): Observable<any> {
    const options = createRequestOption(filterProduct);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PRODUCT_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  getProductUnit(): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_PRODUCT_UNIT);
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
    formData.append('salePrice', req.salePrice);
    if (req.purchasePrice !== null && req.purchasePrice !== undefined) {
      formData.append('purchasePrice', req.purchasePrice);
    } else {
      formData.append('purchasePrice', '');
    }
    formData.append('code2', req.code2 ? req.code2 : '');
    formData.append('barcode', req.barcode ? req.barcode : '');
    formData.append('inventoryTracking', req.inventoryTracking ? req.inventoryTracking : false);
    formData.append('isTopping', req.isTopping ? req.isTopping : false);
    formData.append('inventoryCount', req.inventoryCount ? req.inventoryCount : 0);
    formData.append('vatRate', req.vatRate !== null && req.vatRate !== undefined ? req.vatRate : '');
    formData.append('discountVatRate', req.discountVatRate !== null && req.discountVatRate !== undefined ? req.discountVatRate : '');
    formData.append('description', req.description ? req.description : '');
    formData.append('groups', req.groups ? req.groups : []);
    formData.append('conversionUnits', req.conversionUnits ? JSON.stringify(req.conversionUnits) : '');
    formData.append('toppings', req.toppings ? JSON.stringify(req.toppings) : '');
    if (req.processingArea !== null && req.processingArea !== undefined) {
      formData.append('processingArea', req.processingArea);
    }
    return formData;
  }
  createUnit(comId: number, unitName: string, description: string): Observable<any> {
    let req: any = {};
    req.comId = comId;
    req.unitName = unitName;
    req.unitDescription = description;
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_UNIT, req);
  }

  createCategory(req: any): Observable<any> {
    let reqBody: any = {};
    reqBody.comId = req.comId;
    reqBody.name = req.name;
    reqBody.description = req.description;
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_CATEGORY, reqBody);
  }

  updateCategory(req: any): Observable<any> {
    let reqBody: any = {};
    reqBody.id = req.id;
    reqBody.comId = req.comId;
    reqBody.name = req.name;
    reqBody.description = req.description;
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_CATEGORY, reqBody);
  }
  deleteCategory(id: any, comId: any): Observable<any> {
    let req: any = {};
    req.id = id;
    req.comId = comId;
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_CATEGORY, req);
  }

  deleteProductConversionUnit(id: any, comId: any): Observable<any> {
    let reqBody: any = {};
    reqBody.id = id;
    reqBody.comId = comId;
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_PRODUCT_CONVERSION_UNIT, reqBody);
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
  deleteMultiProduct(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_MULTI_PRODUCT, req);
  }
  getProductTopping(filterTopping: any): Observable<any> {
    const options = createRequestOption(filterTopping);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_TOPPING_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  getToppingGroupById(id: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_TOPPING_GROUP_BY_ID + `${id}`);
  }
  getListProductTopping(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PRODUCT_TOPPING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  updateToppingGroup(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_TOPPING_GROUP, req);
  }
  createToppingGroup(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_TOPPING_GROUP, req);
  }
  deleteToppingGroupById(id: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_TOPPING_GROUP + `${id}`, null);
  }
  getTaxReductionType(comId: any): Observable<any> {
    return this.http.get<any>(`${this.resourceUrl}` + GET_COMPANY_CONFIGS + `${comId}`);
  }
  deleteMultiCategory(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + DELETE_MULTI_CATEGORY, req);
  }
  getUnitPaging(filterUnit: any): Observable<any> {
    const options = createRequestOption(filterUnit);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_UNIT_PAGING, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }
  updateUnit(req: any): Observable<any> {
    let reqBody = {
      comId: req.comId,
      id: req.id,
      unitName: req.name,
      unitDescription: req.description,
    };
    return this.http.post<any>(`${this.resourceUrl}` + UPDATE_UNIT, reqBody);
  }
  deleteUnit(id: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_UNIT + `${id}`, null);
  }

  deleteMultiUnit(req: any): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + DELETE_MULTI_UNIT, req);
  }

  exportExcelProduct(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + EXPORT_PRODUCT, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }

  exportExcelCustomer(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + EXPORT_PRODUCT, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
  getProcessingArea(req: any): Observable<any> {
    const options = createRequestOption(req);
    return this.http
      .get<any>(`${this.resourceUrl}` + GET_PROCESSING_AREA, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getWithPagingBarcode(req: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + GET_WITH_PAGING_BARCODE, req, { observe: 'response' });
  }

  exportBarcodeExcel(req: any): Observable<any> {
    let headers = new HttpHeaders();
    headers = headers.set('Accept', 'application/x-excel');
    return this.http.post(`${this.resourceUrl}` + BARCODE_EXCEL, req, {
      observe: 'response',
      headers,
      responseType: 'blob',
    });
  }
}
