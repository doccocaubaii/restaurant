import { Moment } from 'moment';
import { IFADepreciationAllocation } from 'app/shared/model/fa-depreciation-allocation.model';
import { IFADepreciationDetail } from 'app/shared/model/fa-depreciation-detail.model';

export interface IFADepreciation {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  postedDate?: Moment | any;
  date?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  noBook?: string;
  month?: number;
  year?: number;
  reason?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  templateID?: string;
  recorded?: boolean;
  faDepreciationAllocations?: IFADepreciationAllocation[];
  faDepreciationDetails?: IFADepreciationDetail[];
  faDepreciationPosts?: any[];
  viewVouchers?: any[];
  total?: number[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class FADepreciation implements IFADepreciation {
  public customField1?: string;
  public customField2?: string;
  public customField3?: string;
  public customField4?: string;
  public customField5?: string;
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public postedDate?: Moment | any,
    public date?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public noBook?: string,
    public month?: number,
    public year?: number,
    public reason?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public templateID?: string,
    public recorded?: boolean,
    public faDepreciationAllocations?: IFADepreciationAllocation[],
    public faDepreciationDetails?: IFADepreciationDetail[],
    public faDepreciationPosts?: any[],
    public viewVouchers?: any[],
    public total?: number[]
  ) {
    this.recorded = this.recorded || false;
  }
}
