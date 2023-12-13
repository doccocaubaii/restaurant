import { Moment } from 'moment';

export interface ITM011GTGT {
  id?: number;
  tM01GTGTID?: number;
  voucherID?: number;
  voucherTypeID?: number;
  voucherDetailID?: number;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  invoiceTemplate?: string;
  accountingObjectID?: number;
  accountingObjectName?: string;
  taxCode?: string;
  pretaxAmount?: number;
  vATRate?: number;
  taxAmount?: number;
  note?: string;
  orderPriority?: number;
}

export class TM011GTGT implements ITM011GTGT {
  constructor(
    public id?: number,
    public tM01GTGTID?: number,
    public voucherID?: number,
    public voucherTypeID?: number,
    public voucherDetailID?: number,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public invoiceTemplate?: string,
    public accountingObjectID?: number,
    public accountingObjectName?: string,
    public taxCode?: string,
    public pretaxAmount?: number,
    public vATRate?: number,
    public taxAmount?: number,
    public note?: string,
    public orderPriority?: number
  ) {}
}
