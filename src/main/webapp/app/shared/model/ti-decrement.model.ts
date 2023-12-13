import { Moment } from 'moment';
import { TIDecrementDetails } from 'app/shared/model/ti-decrement-details.model';

export interface ITIDecrement {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  nofBook?: string;
  noMBook?: string;
  reason?: string;
  totalAmount?: number;
  recorded?: boolean;
  templateID?: string;
  tiDecrementDetails?: TIDecrementDetails[];
  viewVouchers?: any[];
  total?: any;
  status?: any;

  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class TIDecrement implements ITIDecrement {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: any,
    public typeLedger?: number,
    public noFBook?: string,
    public nofBook?: string,
    public noMBook?: string,
    public reason?: string,
    public totalAmount?: number,
    public recorded?: boolean,
    public templateID?: string,
    public tiDecrementDetails?: TIDecrementDetails[],
    public viewVouchers?: any[],
    public total?: any,
    public status?: any,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {
    this.recorded = this.recorded || false;
  }
}
