import { Moment } from 'moment';

export interface IGOtherVoucherDetailTax {
  index?: number;
  id?: string;
  description?: string;
  vATAccount?: string;
  vATRate?: number;
  vATAmount?: number;
  vATAmountOriginal?: number;
  pretaxAmount?: number;
  pretaxAmountOriginal?: number;
  invoiceTemplate?: string;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  accountingObjectID?: string;
  accountingObjectName?: string;
  taxCode?: string;
  goodsServicePurchaseID?: string;
  orderPriority?: number;
}

export class GOtherVoucherDetailTax implements IGOtherVoucherDetailTax {
  constructor(
    public index?: number,
    public id?: string,
    public description?: string,
    public vATAccount?: string,
    public vATRate?: number,
    public vATAmount?: number,
    public vATAmountOriginal?: number,
    public pretaxAmount?: number,
    public pretaxAmountOriginal?: number,
    public invoiceTemplate?: string,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public accountingObjectID?: string,
    public accountingObjectName?: string,
    public taxCode?: string,
    public goodsServicePurchaseID?: string,
    public orderPriority?: number
  ) {}
}
