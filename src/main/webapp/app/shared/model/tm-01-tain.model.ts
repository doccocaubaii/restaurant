import { Moment } from 'moment';
import { ITM01TAINDetails } from 'app/shared/model/tm-01-tain-details.model';

export interface ITM01TAIN {
  id?: number;
  companyID?: number;
  typeID?: number;
  declarationName?: string;
  declarationTerm?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  isFirstDeclaration?: boolean;
  additionTime?: number;
  additionDate?: Moment | any;
  companyName?: string;
  companyTaxCode?: string;
  taxAgencyTaxCode?: string;
  taxAgencyName?: string;
  taxAgencyEmployeeName?: string;
  certificationNo?: string;
  signName?: string;
  signDate?: Moment | any;
  tM01TAINDetails?: ITM01TAINDetails[];
}

export class TM01TAIN implements ITM01TAIN {
  constructor(
    public id?: number,
    public companyID?: number,
    public typeID?: number,
    public declarationName?: string,
    public declarationTerm?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public isFirstDeclaration?: boolean,
    public additionTime?: number,
    public additionDate?: Moment | any,
    public companyName?: string,
    public companyTaxCode?: string,
    public taxAgencyTaxCode?: string,
    public taxAgencyName?: string,
    public taxAgencyEmployeeName?: string,
    public certificationNo?: string,
    public signName?: string,
    public signDate?: Moment | any,
    public tM01TAINDetails?: ITM01TAINDetails[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
