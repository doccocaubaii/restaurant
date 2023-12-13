import { Moment } from 'moment';
import { ITM04GTGTDetails } from 'app/shared/model/tm-04-gtgt-details.model';
import { ITM04GTGTAdjust } from 'app/shared/model/tm-04-gtgt-adjust.model';

export interface ITM04GTGT {
  id?: string;
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
  itm04GTGTDetails?: ITM04GTGTDetails[];
  tm04GTGTAdjusts?: ITM04GTGTAdjust[];
}

export class TM04GTGT implements ITM04GTGT {
  constructor(
    public id?: string,
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
    public itm04GTGTDetails?: ITM04GTGTDetails[],
    public tm04GTGTAdjusts?: ITM04GTGTAdjust[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
