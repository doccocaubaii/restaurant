import { Moment } from 'moment';
import { IFaTransferDetails } from 'app/shared/model/fa-transfer-detail.modal';

export interface IFaTransfer {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  transferor?: string;
  receiver?: string;
  reason?: string;
  recorded?: boolean;
  templateID?: string;
  noBook?: string;
  faTransferDetails?: IFaTransferDetails[];
  viewVouchers?: any[];
  checkUnRecordConvertDTO?: any;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FaTransfer implements IFaTransfer {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public transferor?: string,
    public receiver?: string,
    public reason?: string,
    public recorded?: boolean,
    public templateID?: string,
    public noBook?: string,
    public faTransferDetails?: IFaTransferDetails[],
    public viewVouchers?: any[],
    public checkUnRecordConvertDTO?: any
  ) {
    this.recorded = this.recorded || false;
  }
}
