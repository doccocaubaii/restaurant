import { Moment } from 'moment';

export interface IReceiveBill {
  id?: string;
  typeID?: number;
  listIDPP?: string[];
  listIDPPDetail?: string[];
  date?: string;
  postedDate?: string;
  no?: string;
  noFBook?: string;
  noMBook?: string;
  reason?: string;
  amount?: number;
  vATRate?: number;
  vATAccount?: string;
  invoiceTemplate?: string;
  invoiceSeries?: string;
  invoiceNo?: string;
  invoiceDate?: Moment | any;
  goodsServicePurchaseID?: string;
  accountingObjectID?: string;
  isSelected?: boolean;
  allCheck?: boolean;
  searchVoucher?: string;
  refID2?: string;
  typeGroupID?: number;
}

export class ReceiveBill implements IReceiveBill {
  constructor(
    public id?: string,
    public typeID?: number,
    public listIDPP?: string[],
    public listIDPPDetail?: string[],
    public date?: string,
    public postedDate?: string,
    public no?: string,
    public noFBook?: string,
    public noMBook?: string,
    public reason?: string,
    public totalAmount?: number,
    public vATRate?: number,
    public vATAccount?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public goodsServicePurchaseID?: string,
    public accountingObjectID?: string,
    public isSelected?: boolean,
    public allCheck?: boolean,
    public searchVoucher?: string,
    public refID2?: string,
    public typeGroupID?: number
  ) {}
}
