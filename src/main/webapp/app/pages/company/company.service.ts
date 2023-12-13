import { Injectable } from '@angular/core';
import { Company } from '../../entities/company/company';
import { Observable, of } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { CREATE_COMPANY, GET_COMPANY_OWNER, GET_COMPANY_WITH_PAGING, UPDATE_COMPANY } from '../../constants/api.constants';

const httpOptions = {
  headers: new HttpHeaders({ 'Content-Type': 'Application/json' }),
};

@Injectable({
  providedIn: 'root',
})
export class CompanyService {
  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}
  urlGetTax: string = 'http://utilsrv.easyinvoice.com.vn/api/company/info';
  private resourceUrl = this.applicationConfigService.getEndpointFor('api');

  getCompany(page: number, pageSize: number, sort?: string, keyword?: string): Observable<any> {
    let params = new HttpParams()
      .set('page', String(page - 1))
      .set('size', String(pageSize))
      .set('isCountAll', 'true');

    if (keyword) {
      params = params.set('keyword', keyword);
    }
    if (sort) {
      params = params.set('sort', sort);
    }
    return this.http.get<any>(`${this.resourceUrl}` + GET_COMPANY_WITH_PAGING, { params });
  }

  createCompany(com: Company): Observable<any> {
    return this.http.post<any>(`${this.resourceUrl}` + CREATE_COMPANY, this.setRequest(com));
  }

  private setRequest(com: Company) {
    com.phone = com.phone ? com.phone : null;
    return {
      ...com,
      comId: com.id,
    };
  }

  updateCompany(com: Company): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + UPDATE_COMPANY, this.setRequest(com));
  }

  getCompanyOwner(userId: number): Observable<any> {
    return this.http.put<any>(`${this.resourceUrl}` + GET_COMPANY_OWNER + `/${userId}`, null);
  }

  private handleError<T>(operation = 'operation', result?: T) {
    return (error: any): Observable<T> => {
      return of(result as T);
    };
  }
}
