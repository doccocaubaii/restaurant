import { Moment } from 'moment';
import { IFaDecrementDetailsPost } from 'app/shared/model/fa-decrement-details-post.model';

export interface IFaDecrement {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment;
  postedDate?: Moment;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  reason?: string;
  totalAmount?: number;
  recorded?: boolean;
  templateID?: string;
  faDecrementDetails?: any[];
  viewVouchers?: any[];
  checkUnRecordConvertDTO?: any;
  faDecrementDetailPosts?: IFaDecrementDetailsPost[];
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FaDecrement implements IFaDecrement {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public reason?: string,
    public totalAmount?: number,
    public recorded?: boolean,
    public templateID?: string,
    public faDecrementDetails?: any[],
    public viewVouchers?: any[],
    public checkUnRecordConvertDTO?: any,
    faDecrementDetailPosts?: IFaDecrementDetailsPost[]
  ) {
    this.recorded = this.recorded || false;
  }
}
