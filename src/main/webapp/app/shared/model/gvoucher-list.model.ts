import { Moment } from 'moment';
import { IGvoucherListDetails } from 'app/shared/model/gvoucher-list-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IGvoucherList {
  id?: string;
  companyId?: string;
  branchId?: string;
  typeId?: number;
  date?: Moment | any;
  typeLedger?: number;
  noBook?: string;
  noFBook?: string;
  noMBook?: string;
  description?: string;
  totalAmount?: number;
  amount?: number;
  templateId?: string;
  gvoucherListDetails?: IGvoucherListDetails[];
  viewVouchers?: IViewVoucher[];
  refID2?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class GvoucherList implements IGvoucherList {
  constructor(
    public id?: string,
    public companyId?: string,
    public branchId?: string,
    public typeId?: number,
    public date?: Moment | any,
    public typeLedger?: number,
    public noBook?: string,
    public noFBook?: string,
    public noMBook?: string,
    public description?: string,
    public totalAmount?: number,
    public amount?: number,
    public templateId?: string,
    public gvoucherListDetails?: IGvoucherListDetails[],
    public viewVouchers?: IViewVoucher[],
    public refID2?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
