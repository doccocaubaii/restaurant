import { Moment } from 'moment';
import { IMBInternalTransferDetails } from 'app/shared/model/mb-internal-transfer-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';

export interface IMBInternalTransfer {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  noBook?: string;
  reason?: string;
  employeeID?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  amount?: number;
  toBranchID?: string;
  recorded?: boolean;
  currencyID?: string;
  exchangeRate?: number;
  viewVouchers?: IViewVoucher[];
  mbInternalTransferDetails?: IMBInternalTransferDetails[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  bankTransactionID?: any;
}

export class MBInternalTransfer implements IMBInternalTransfer {
  constructor(
    public id?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public noBook?: string,
    public reason?: string,
    public employeeID?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public amount?: number,
    public toBranchID?: string,
    public recorded?: boolean,
    public currencyID?: string,
    public exchangeRate?: number,
    public viewVouchers?: IViewVoucher[],
    public mBInternalTransferDetails?: IMBInternalTransferDetails[],
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public bankTransactionID?: any
  ) {}
}
