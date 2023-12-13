import { Moment } from 'moment';
import { IInvoiceType } from 'app/shared/model/invoice-type.model';

export interface IIaLostInvoiceDetails {
  id?: string;
  iaLostInvoiceID?: string;
  iaPublishInvoiceID?: string;
  iaReportID?: string;
  invoiceTemplate?: string;
  invoiceType?: IInvoiceType;
  invoiceSeries?: string;
  fromNo?: string;
  toNo?: string;
  quantity?: number;
  copyPart?: string;
  description?: string;
  orderPriority?: number;
  isCheck?: boolean;
  invoiceTypeID?: string;
}

export class IaLostInvoiceDetails implements IIaLostInvoiceDetails {
  constructor(
    public id?: string,
    public iaLostInvoiceID?: string,
    public iaPublishInvoiceID?: string,
    public iaReportID?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public fromNo?: string,
    public toNo?: string,
    public quantity?: number,
    public copyPart?: string,
    public description?: string,
    public orderPriority?: number,
    public invoiceType?: IInvoiceType,
    public invoiceTypeID?: string,
    public isCheck?: boolean
  ) {}
}
