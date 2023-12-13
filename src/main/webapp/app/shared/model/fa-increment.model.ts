import { Moment } from 'moment';
import { FAIncrementDetails, IFAIncrementDetails } from 'app/shared/model/fa-increment-details.model';

export interface IFAIncrement {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  reason?: string;
  totalAmount?: number;
  templateID?: string;
  recorded?: boolean;
  faIncrementDetails?: IFAIncrementDetails[];
  faIncrementDetailRefVouchers?: any[];
  faIncrementDetailRefVoucherConvertDTOS?: any[];
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FAIncrement implements IFAIncrement {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public reason?: string,
    public totalAmount?: number,
    public templateID?: string,
    public recorded?: boolean,
    public faIncrementDetails?: IFAIncrementDetails[],
    public faIncrementDetailRefVouchers?: any[],
    public faIncrementDetailRefVoucherConvertDTOS?: any[]
  ) {
    this.recorded = this.recorded || false;
  }
}
