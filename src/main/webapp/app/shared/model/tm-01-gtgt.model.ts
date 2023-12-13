import { Moment } from 'moment';
import { ITM01GTGTDetails } from 'app/shared/model/tm-01-gtgt-details.model';
import { ITM011GTGT } from 'app/shared/model/tm-011-gtgt.model';
import { ITM012GTGT } from 'app/shared/model/tm-012-gtgt.model';
import { ITM01GTGTAdjust } from 'app/shared/model/tm-01-gtgt-adjust.model';
import { ITM43GTGT } from 'app/shared/model/tm-43-gtgt.model';

export interface ITM01GTGT {
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
  career?: number;
  isExtend?: boolean;
  extensionCase?: number;
  isAppendix011GTGT?: boolean;
  isAppendix012GTGT?: boolean;
  isAppendix43GTGT?: boolean;
  companyName?: string;
  companyTaxCode?: string;
  taxAgencyTaxCode?: string;
  taxAgencyName?: string;
  taxAgencyEmployeeName?: string;
  certificationNo?: string;
  signName?: string;
  signDate?: Moment | any;
  itm01GTGTDetails?: ITM01GTGTDetails[];
  tm011GTGTS?: ITM011GTGT[];
  tm012GTGTS?: ITM012GTGT[];
  tm43GTGTS?: ITM43GTGT[];
  tm01GTGTAdjusts?: ITM01GTGTAdjust[];
  industryTypeName?: string;
  branchName?: string;
  branchTaxCode?: string;
}

export class TM01GTGT implements ITM01GTGT {
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
    public career?: number,
    public isExtend?: boolean,
    public extensionCase?: number,
    public isAppendix011GTGT?: boolean,
    public isAppendix012GTGT?: boolean,
    public isAppendix43GTGT?: boolean,
    public companyName?: string,
    public companyTaxCode?: string,
    public taxAgencyTaxCode?: string,
    public taxAgencyName?: string,
    public taxAgencyEmployeeName?: string,
    public certificationNo?: string,
    public signName?: string,
    public signDate?: Moment | any,
    public itm01GTGTDetails?: ITM01GTGTDetails[],
    public tm011GTGTS?: ITM011GTGT[],
    public tm012GTGTS?: ITM012GTGT[],
    public tm43GTGTS?: ITM43GTGT[],
    public tm01GTGTAdjusts?: ITM01GTGTAdjust[],
    public industryTypeName?: string,
    public branchName?: string,
    public branchTaxCode?: string
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
    this.isExtend = this.isExtend || false;
    this.isAppendix011GTGT = this.isAppendix011GTGT || false;
    this.isAppendix012GTGT = this.isAppendix012GTGT || false;
    this.isAppendix43GTGT = this.isAppendix43GTGT || false;
  }
}
