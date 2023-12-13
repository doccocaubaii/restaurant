import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProductGroup, NewIProductGroup } from '../model/product-group.model';
import { GET_ALL_PRODUCT_GROUP_OFFLINE, GET_LIST_PRODUCT_GROUP, GET_PRODUCT_BY_PRODUCT_GROUP_ID } from 'app/constants/api.constants';

export type PartialUpdateProductGroup = Partial<IProductGroup> & Pick<IProductGroup, 'id'>;

type RestOf<T extends IProductGroup | NewIProductGroup> = Omit<T, 'createTime' | 'updateTime' | 'paymentTime'> & {
  createTime?: string | null;
  updateTime?: string | null;
  paymentTime?: string | null;
};

export type RestProductGroup = RestOf<IProductGroup>;

export type NewRestProductGroup = RestOf<NewIProductGroup>;

// export type PartialUpdateRestProductGroup = RestOf<IProductGroup>;

export type EntityResponseType = HttpResponse<IProductGroup>;
export type EntityArrayResponseType = HttpResponse<IProductGroup[]>;

@Injectable({
  providedIn: 'root',
})
export class ProductGroupService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(productGroup: NewIProductGroup): Observable<EntityResponseType> {
    return this.http
      .post<RestProductGroup>(`${this.resourceUrl}/create`, productGroup, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(productGroup: IProductGroup): Observable<EntityResponseType> {
    return this.http
      .put<RestProductGroup>(`${this.resourceUrl}/update`, productGroup, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(productGroup: PartialUpdateProductGroup): Observable<EntityResponseType> {
    return this.http
      .patch<RestProductGroup>(`${this.resourceUrl}/${this.getProductGroupIdentifier(productGroup)}`, productGroup, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestProductGroup>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  getAllProductGroup() {
    return this.http
      .get(`${this.resourceUrl}${GET_ALL_PRODUCT_GROUP_OFFLINE}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestProductGroup[]>(`${this.resourceUrl}${GET_LIST_PRODUCT_GROUP}`, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number, comId: number): Observable<HttpResponse<{}>> {
    return this.http.post(`${this.resourceUrl}/delete`, { id, comId }, { observe: 'response' });
  }

  getProductGroupIdentifier(productGroup: Pick<IProductGroup, 'id'>): number {
    return productGroup.id;
  }

  compareProductGroup(o1: Pick<IProductGroup, 'id'> | null, o2: Pick<IProductGroup, 'id'> | null): boolean {
    return o1 && o2 ? this.getProductGroupIdentifier(o1) === this.getProductGroupIdentifier(o2) : o1 === o2;
  }

  addProductGroupToCollectionIfMissing<Type extends Pick<IProductGroup, 'id'>>(
    productGroupCollection: Type[],
    ...productGroupsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const productGroups: Type[] = productGroupsToCheck.filter(isPresent);
    if (productGroups.length > 0) {
      const productGroupCollectionIdentifiers = productGroupCollection.map(
        productGroupItem => this.getProductGroupIdentifier(productGroupItem)!
      );
      const productGroupsToAdd = productGroups.filter(productGroupItem => {
        const productGroupIdentifier = this.getProductGroupIdentifier(productGroupItem);
        if (productGroupCollectionIdentifiers.includes(productGroupIdentifier)) {
          return false;
        }
        productGroupCollectionIdentifiers.push(productGroupIdentifier);
        return true;
      });
      return [...productGroupsToAdd, ...productGroupCollection];
    }
    return productGroupCollection;
  }

  protected convertResponseFromServer(res: HttpResponse<RestProductGroup>): HttpResponse<IProductGroup> {
    return res.clone({
      body: res.body ? res.body : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<any>): HttpResponse<IProductGroup[]> {
    // eslint-disable-next-line @typescript-eslint/no-unsafe-return
    return res.clone({
      body: res.body ? res.body.data : null,
    });
  }

  getProductGroupFromProductGroupId(request: any): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}${GET_PRODUCT_BY_PRODUCT_GROUP_ID}`, request, { observe: 'response' });
  }
}
