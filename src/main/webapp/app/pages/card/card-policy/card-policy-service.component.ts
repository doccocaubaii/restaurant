import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders, HttpParams, HttpResponse } from '@angular/common/http';
import { CARD_POLICY_CREATE, CARD_POLICY_GET_ALL, CARD_POLICY_UPDATE } from '../../../constants/api.constants';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { CardPolicy } from '../../../entities/cardPolicy/card-policy';
import { CardPolicyApplyItem } from '../../../entities/cardPolicy/card-policy-apply-item';
import { UtilsService } from '../../../utils/Utils.service';
import { IResultDTO } from '../../../core/response/result-dto.model';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'Application/json' }),
};
export type EntityResponseType = HttpResponse<IResultDTO>;

@Injectable({
  providedIn: 'root',
})
export class CardPolicyService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService, private utilsService: UtilsService) {}
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getAllCardPolicy(comId: number): Observable<any> {
    let params = new HttpParams().set('comId', comId);
    return this.http.get<any>(`${this.resourceUrl}` + CARD_POLICY_GET_ALL, { params });
  }

  saveCardPolicy(
    request: CardPolicy,
    fromDate: any,
    conditionAll: CardPolicyApplyItem[],
    conditionPage: CardPolicyApplyItem[]
  ): Observable<EntityResponseType> {
    let requestLocal = JSON.parse(JSON.stringify(request));
    // console.log(requestLocal.conditions.filter(item => item.checked))
    let finalCondition: CardPolicyApplyItem[] = [];
    let conditionAllLocal = JSON.parse(JSON.stringify(conditionAll));
    let conditionLocal = JSON.parse(JSON.stringify(conditionPage));
    const mapConditionPage = new Map<number, CardPolicyApplyItem>();
    for (const item of conditionLocal) {
      mapConditionPage.set(item.cardId, item);
    }
    conditionAllLocal.forEach(item => {
      if (mapConditionPage.has(item.cardId)) {
        finalCondition.push(<CardPolicyApplyItem>mapConditionPage.get(item.cardId));
      } else {
        finalCondition.push(item);
      }
    });
    requestLocal.conditions = finalCondition;
    requestLocal.fromDate = this.utilsService.convertDate(fromDate).toString().slice(0, 19).replace('T', ' ');
    if (requestLocal.id) {
      return this.http.put<any>(this.resourceUrl + CARD_POLICY_UPDATE, requestLocal, { observe: 'response' });
    }
    return this.http.post<any>(this.resourceUrl + CARD_POLICY_CREATE, requestLocal);
  }
}
