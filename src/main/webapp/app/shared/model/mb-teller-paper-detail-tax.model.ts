import { Moment } from 'moment';
import { IMBTellerPaper } from 'app/shared/model//mb-teller-paper.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';

export interface IMBTellerPaperDetailTax {
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
  goodsServicePurchaseId?: string;
  accountingObjectID?: string;
  accountingObjectName?: string;
  taxCode?: string;
  orderPriority?: number;
  mBTellerPaperId?: string;
  year?: number;
}

export class MBTellerPaperDetailTax implements IMBTellerPaperDetailTax {
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
    public goodsServicePurchaseId?: string,
    public accountingObjectID?: string,
    public accountingObjectName?: string,
    public taxCode?: string,
    public orderPriority?: number,
    public mBTellerPaperId?: string,
    public year?: number
  ) {}
}
