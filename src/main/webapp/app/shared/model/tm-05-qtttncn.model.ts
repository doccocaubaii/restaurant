import { Moment } from 'moment';
import { ITM05QTTTNCNDetail } from 'app/shared/model/tm-05-qtttncn-details.model';
import { ITM051BKQTTTNCN } from 'app/shared/model/tm-051bk-qtttncn.model';
import { ITM052BKQTTTNCN } from 'app/shared/model/tm-052bk-qtttncn.model';
import { ITM053BKQTTTNCN } from 'app/shared/model/tm-053bk-qtttncn.model';

export interface ITM05QTTTNCN {
  id?: string;
  companyID?: string;
  typeID?: number;
  reason?: string;
  declarationName?: string;
  declarationTerm?: string;
  fromDate?: Moment;
  toDate?: Moment;
  isFirstDeclaration?: boolean;
  additionTime?: number;
  additionDate?: Moment;
  companyName?: string;
  companyTaxCode?: string;
  taxAgencyTaxCode?: string;
  taxAgencyName?: string;
  taxAgencyEmployeeName?: string;
  certificationNo?: string;
  signName?: string;
  signDate?: Moment;
  isGroup?: boolean;
  tm05TNCNDetails?: ITM05QTTTNCNDetail[];
  tM051BKQTTTNCN?: ITM051BKQTTTNCN[];
  tM052BKQTTTNCN?: ITM052BKQTTTNCN[];
  tM053BKQTTTNCN?: ITM053BKQTTTNCN[];
}

export class TM05QTTTNCN implements ITM05QTTTNCN {
  constructor(
    public id?: string,
    public companyID?: string,
    public typeID?: number,
    public reason?: string,
    public declarationName?: string,
    public declarationTerm?: string,
    public fromDate?: Moment,
    public toDate?: Moment,
    public isFirstDeclaration?: boolean,
    public additionTime?: number,
    public additionDate?: Moment,
    public companyName?: string,
    public companyTaxCode?: string,
    public taxAgencyTaxCode?: string,
    public taxAgencyName?: string,
    public taxAgencyEmployeeName?: string,
    public certificationNo?: string,
    public signName?: string,
    public signDate?: Moment,
    public isGrouped?: boolean,
    public tm05TNCNDetails?: ITM05QTTTNCNDetail[],
    public tM051BKQTTTNCN?: ITM051BKQTTTNCN[],
    public tM052BKQTTTNCN?: ITM052BKQTTTNCN[],
    public tM053BKQTTTNCN?: ITM053BKQTTTNCN[]
  ) {
    this.isFirstDeclaration = this.isFirstDeclaration || false;
  }
}
