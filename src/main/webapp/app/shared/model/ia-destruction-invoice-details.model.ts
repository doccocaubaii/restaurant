import { IInvoiceType } from 'app/shared/model/invoice-type.model';
import { VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';

export interface IIADestructionInvoiceDetails {
  id?: number;
  iaDestructionInvoiceID?: string;
  invoiceTemplate?: string;
  iaReportID?: string;
  invoiceTypeID?: string;
  invoiceSeries?: string;
  fromNo?: string;
  toNo?: string;
  quantity?: number;
  copyPart?: string;
  description?: string;
  orderPriority?: number;
  invoiceType?: IInvoiceType;
  isCheck?: boolean;
  invoiceTypeName?: string;
}

export class IADestructionInvoiceDetails implements IIADestructionInvoiceDetails {
  constructor(
    public id?: number,
    public iaDestructionInvoiceID?: string,
    public invoiceTemplate?: string,
    public iaReportID?: string,
    public invoiceTypeID?: string,
    public invoiceSeries?: string,
    public fromNo?: string,
    public invoiceType?: IInvoiceType,
    public toNo?: string,
    public quantity?: number,
    public copyPart?: string,
    public description?: string,
    public orderPriority?: number,
    public isCheck?: boolean,
    public invoiceTypeName?: string
  ) {}
}
