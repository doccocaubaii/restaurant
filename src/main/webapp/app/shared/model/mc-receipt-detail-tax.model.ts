import { Moment } from 'moment';
import { IMCReceipt } from 'app/shared/model//mc-receipt.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';

export interface IMCReceiptDetailTax {
  id?: string;
  description?: string;
  vATAmount?: number;
  vATAmountOriginal?: number;
  vATRate?: number;
  vATAccount?: string;
  pretaxAmount?: number;
  pretaxAmountOriginal?: number;
  invoiceTemplate?: string;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  orderPriority?: number;
  mcreceipt?: IMCReceipt;
  mCReceiptID?: any;
  accountingObject?: IAccountingObject;
  accountingObjectID?: string;
}

export class MCReceiptDetailTax implements IMCReceiptDetailTax {
  constructor(
    public id?: string,
    public description?: string,
    public vATAmount?: number,
    public vATAmountOriginal?: number,
    public vATRate?: number,
    public vATAccount?: string,
    public pretaxAmount?: number,
    public pretaxAmountOriginal?: number,
    public invoiceTemplate?: string,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public orderPriority?: number,
    public mcreceipt?: IMCReceipt,
    public mCReceiptID?: any,
    public accountingObject?: IAccountingObject
  ) {}
}
