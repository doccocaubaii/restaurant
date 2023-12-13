import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct, NewIProduct } from '../model/product.model';
import {
  CREATE_PRODUCT,
  DELETE_PRODUCT,
  GET_LIST_PRODUCT,
  GET_LIST_PRODUCT2,
  GET_LIST_PRODUCT_UNIT,
  GET_PRODUCT_BY_ID,
  GET_PRODUCT_NAME_BY_IDS,
  GET_PRODUCT_TOPPING,
  GET_PRODUCT_TOPPING_BY_ID,
  UPDATE_PRODUCT,
} from 'app/constants/api.constants';
import { DELETE_PRODUCT_GROUP } from '../../../constants/api.constants';

export type PartialUpdateProduct = Partial<IProduct> & Pick<IProduct, 'id'>;

type RestOf<T extends IProduct | NewIProduct> = Omit<T, 'createTime' | 'updateTime' | 'paymentTime'> & {
  createTime?: string | null;
  updateTime?: string | null;
  paymentTime?: string | null;
};

export type RestProduct = RestOf<IProduct>;

export type NewRestProduct = RestOf<NewIProduct>;

// export type PartialUpdateRestProduct = RestOf<IProduct>;

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(product: NewIProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .post<RestProduct>(`${this.resourceUrl}${CREATE_PRODUCT}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getAllProductUnit(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProduct[]>(`${this.resourceUrl}${GET_LIST_PRODUCT_UNIT}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  getProductTopping(idProduct: number): Observable<EntityArrayResponseType> {
    return this.http
      .get<RestProduct[]>(`${this.resourceUrl}${GET_PRODUCT_TOPPING_BY_ID}${idProduct}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  update(product: IProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(product);
    return this.http
      .put<RestProduct>(`${this.resourceUrl}${UPDATE_PRODUCT}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProduct>(`${this.resourceUrl}${GET_PRODUCT_BY_ID}${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    // const options = createRequestOption(req);
    return this.http
      .post<RestProduct[]>(`${this.resourceUrl}${GET_LIST_PRODUCT2}`, req, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<EntityResponseType> {
    return this.http.put<HttpResponse<RestProduct>>(`${DELETE_PRODUCT}/${id}`, { observe: 'response' });
  }

  getProductIdentifier(product: Pick<IProduct, 'id'>): number {
    return product.id;
  }

  compareProduct(o1: Pick<IProduct, 'id'> | null, o2: Pick<IProduct, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductIdentifier(o1) === this.getProductIdentifier(o2) : o1 === o2;
  }

  addProductToCollectionIfMissing<Type extends Pick<IProduct, 'id'>>(
    productCollection: Type[],
    ...productsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const products: Type[] = productsToCheck.filter(isPresent);
    if (products.length > 0) {
      const productCollectionIdentifiers = productCollection.map(productItem => this.getProductIdentifier(productItem)!);
      const productsToAdd = products.filter(productItem => {
        const productIdentifier = this.getProductIdentifier(productItem);
        if (productCollectionIdentifiers.includes(productIdentifier)) {
          return false;
        }
        productCollectionIdentifiers.push(productIdentifier);
        return true;
      });
      return [...productsToAdd, ...productCollection];
    }
    return productCollection;
  }

  protected convertDateFromClient<T extends IProduct | NewIProduct | PartialUpdateProduct>(product: T): RestOf<any> {
    return {
      ...product,
    };
  }

  protected convertDateFromServer(RestProduct: RestProduct): IProduct {
    return {
      ...RestProduct,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestProduct>): HttpResponse<IProduct> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<IProduct[]> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body ? res.body.data.map(item => this.convertDateFromServer(item)) : null,
    });
  }

  getProductFromProductId(request: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}${GET_PRODUCT_NAME_BY_IDS}`, request, { observe: 'response' });
  }
}
