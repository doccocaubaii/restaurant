import { Moment } from 'moment';

export interface ITM012GTGT {
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
  taxAmount?: number;
  note?: string;
  orderPriority?: number;
  type?: number;
  typeID?: number;
  isImportPurchase?: any;
  vatAccount?: any;
  deductionDebitAccount?: any;
  voucherTypeLedger?: number;
}

export class TM012GTGT implements ITM012GTGT {
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
    public taxAmount?: number,
    public note?: string,
    public orderPriority?: number,
    public type?: number,
    public typeID?: number,
    public isImportPurchase?: any,
    public vatAccount?: any,
    public deductionDebitAccount?: any,
    public voucherTypeLedger?: number
  ) {}
}
