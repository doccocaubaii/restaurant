import { Moment } from 'moment';
import { ITM02TAINDetails } from 'app/shared/model/tm-02-tain-details.model';

export interface ITM02TAIN {
  id?: string;
  companyID?: string;
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
  tM02TAINDetails?: ITM02TAINDetails[];
}

export class TM02TAIN implements ITM02TAIN {
  constructor(
    public id?: string,
    public companyID?: string,
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
    public tM02TAINDetails?: ITM02TAINDetails[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
