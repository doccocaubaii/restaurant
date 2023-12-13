import { Moment } from 'moment';

export interface ITITransfer {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  nofBook?: string;
  noMBook?: string;
  transferor?: string;
  receiver?: string;
  reason?: string;
  totalQuantity?: number;
  recorded?: boolean;
  templateID?: string;
  tiTransferDetails?: any[];
  viewVouchers?: any[];
  total?: any;
  status?: any;
}

export class TITransfer implements ITITransfer {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public nofBook?: string,
    public noMBook?: string,
    public transferor?: string,
    public receiver?: string,
    public reason?: string,
    public totalQuantity?: number,
    public recorded?: boolean,
    public templateID?: string,
    public tiTransferDetails?: any[],
    public viewVouchers?: any[],
    public total?: any,
    public status?: any
  ) {
    this.recorded = this.recorded || false;
  }
}
