import { Moment } from 'moment';
import { ITIIncrementDetails } from 'app/shared/model/ti-increment-details.model';

export interface ITIIncrement {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  reason?: string;
  totalAmount?: number;
  templateID?: string;
  recorded?: boolean;
  tiIncrementDetails?: ITIIncrementDetails[];
  tiIncrementDetailRefVouchers?: any;
  refVoucherDetailDTOs?: any;
  tiIncrementDetailRefVoucherConvertDTOS?: any[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class TIIncrement implements ITIIncrement {
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
    public tiIncrementDetails?: ITIIncrementDetails[],
    public tiIncrementDetailRefVouchers?: any,
    public refVoucherDetailDTOs?: any,
    public tiIncrementDetailRefVoucherConvertDTOS?: any[],
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {
    this.recorded = this.recorded || false;
  }
}
