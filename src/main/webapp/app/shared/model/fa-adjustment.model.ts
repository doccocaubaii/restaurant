import { Moment } from 'moment';
import { IFAAdjustmentDetails } from 'app/shared/model/fa-adjustment-details.model';

export interface IFAAdjustment {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  postedDate?: Moment;
  date?: Moment;
  typeLedger?: number;
  noFBook?: string;
  nofBook?: string;
  noMBook?: string;
  reason?: string;
  templateID?: string;
  recorded?: boolean;
  reportNo?: string;
  reportDate?: Moment | any;
  faAdjustmentDetails?: IFAAdjustmentDetails[];
  faAdjustmentDetailPosts?: any[];
  faAdjustmentMemberDetails?: any[];
  viewVouchers?: any[];
  checkUnRecordConvertDTO?: any;
}

export class FAAdjustment implements IFAAdjustment {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public postedDate?: Moment | any,
    public date?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public nofBook?: string,
    public noMBook?: string,
    public reason?: string,
    public templateID?: string,
    public recorded?: boolean,
    public reportNo?: string,
    public reportDate?: Moment | any,
    public faAdjustmentDetails?: IFAAdjustmentDetails[],
    public faAdjustmentDetailPosts?: any[],
    public checkUnRecordConvertDTO?: any,
    public viewVouchers?: any[]
  ) {
    this.recorded = this.recorded || false;
  }
}
