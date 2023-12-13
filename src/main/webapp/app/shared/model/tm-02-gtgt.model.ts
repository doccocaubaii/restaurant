import { Moment } from 'moment';
import { ITM02GTGTDetails } from 'app/shared/model/tm-02-gtgt-details.model';
import { ITM02GTGTAdjust } from 'app/shared/model/tm-02-gtgt-adjust.model';

export interface ITM02GTGT {
  id?: string;
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
  tm02GTGTDetails?: ITM02GTGTDetails[];
  tm02GTGTAdjusts?: ITM02GTGTAdjust[];
}

export class TM02GTGT implements ITM02GTGT {
  constructor(
    public id?: string,
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
    public tm02GTGTDetails?: ITM02GTGTDetails[],
    public tm02GTGTAdjusts?: ITM02GTGTAdjust[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
