import { ITM05TNCNDetails } from 'app/shared/model/tm-05-tncn-details.model';
import { Moment } from 'moment';

export interface ITM05TNCN {
  ct15?: boolean;
  isTT80?: boolean;
  id?: string;
  companyID?: number;
  branchID?: number;
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
  tm05TNCNDetails?: ITM05TNCNDetails[];
}

export class TM05TNCN implements ITM05TNCN {
  constructor(
    public id?: string,
    public companyID?: number,
    public branchID?: number,
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
    public tm05TNCNDetails?: ITM05TNCNDetails[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
