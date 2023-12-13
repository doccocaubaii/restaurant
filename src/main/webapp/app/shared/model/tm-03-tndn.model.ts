import { Moment } from 'moment';
import { ITM03TNDNDetails } from 'app/shared/model/tm-03-tndn-details.model';
import { ITM03TNDNAdjust } from 'app/shared/model/tm-03-tndn-adjust.model';
import { ITM031ATNDN } from 'app/shared/model/tm-031-atndn.model';
import { ITM032ATNDN } from 'app/shared/model/tm-032-atndn.model';
import { TM03TNDNDocument } from 'app/shared/model/tm-03-tndn-document.model';

export interface ITM03TNDN {
  id?: any;
  typeID?: number;
  declarationName?: string;
  declarationTerm?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  isFirstDeclaration?: boolean;
  additionTime?: number;
  additionDate?: Moment | any;
  career?: number;
  isAppendix031ATNDN?: boolean;
  isAppendix032ATNDN?: boolean;
  isAppendixPL92?: boolean;
  isSMEEnterprise?: boolean;
  isDependentEnterprise?: boolean;
  isRelatedTransactionEnterprise?: boolean;
  tMIndustryTypeID?: number;
  companyID?: number;
  companyName?: string;
  rate?: number;
  companyTaxCode?: string;
  taxAgencyTaxCode?: string;
  taxAgencyName?: string;
  taxAgencyEmployeeName?: string;
  certificationNo?: string;
  signName?: string;
  signDate?: Moment | any;
  isExtend?: boolean;
  extensionCase?: number;
  extendTime?: Moment | any;
  extendTaxAmount?: number;
  notExtendTaxAmount?: number;
  lateDays?: number;
  fromDateLate?: Moment | any;
  toDateLate?: Moment | any;
  lateAmount?: number;
  tm03TNDNDetails?: ITM03TNDNDetails[];
  tm03TNDNAdjust?: ITM03TNDNAdjust[];
  tm031ATNDNS?: ITM031ATNDN[];
  tm032ATNDNS?: ITM032ATNDN[];
  tm03TNDNDocument?: TM03TNDNDocument[];
  plnd92s?: ITM031ATNDN[];
}

export class TM03TNDN implements ITM03TNDN {
  constructor(
    public id?: any,
    public typeID?: number,
    public declarationName?: string,
    public declarationTerm?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public isFirstDeclaration?: boolean,
    public additionTime?: number,
    public additionDate?: Moment | any,
    public career?: number,
    public isAppendix031ATNDN?: boolean,
    public isAppendix032ATNDN?: boolean,
    public isAppendixPL92?: boolean,
    public isSMEEnterprise?: boolean,
    public isDependentEnterprise?: boolean,
    public isRelatedTransactionEnterprise?: boolean,
    public tMIndustryTypeID?: number,
    public companyID?: number,
    public companyName?: string,
    public rate?: number,
    public companyTaxCode?: string,
    public taxAgencyTaxCode?: string,
    public taxAgencyName?: string,
    public taxAgencyEmployeeName?: string,
    public certificationNo?: string,
    public signName?: string,
    public signDate?: Moment | any,
    public isExtend?: boolean,
    public extensionCase?: number,
    public extendTime?: Moment | any,
    public extendTaxAmount?: number,
    public notExtendTaxAmount?: number,
    public lateDays?: number,
    public fromDateLate?: Moment | any,
    public toDateLate?: Moment | any,
    public lateAmount?: number,
    public tm03TNDNDetails?: ITM03TNDNDetails[],
    public tm03TNDNAdjust?: ITM03TNDNAdjust[],
    public tm031ATNDN?: ITM031ATNDN[],
    public tm032ATNDN?: ITM032ATNDN[],
    public tm03TNDNDocument?: TM03TNDNDocument[],
    public plnd92s?: ITM031ATNDN[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
    this.isAppendix031ATNDN = this.isAppendix031ATNDN || false;
    this.isAppendix032ATNDN = this.isAppendix032ATNDN || false;
    this.isAppendixPL92 = this.isAppendixPL92 || false;
    this.isSMEEnterprise = this.isSMEEnterprise || false;
    this.isDependentEnterprise = this.isDependentEnterprise || false;
    this.isRelatedTransactionEnterprise = this.isRelatedTransactionEnterprise || false;
    this.isExtend = this.isExtend || false;
  }
}
