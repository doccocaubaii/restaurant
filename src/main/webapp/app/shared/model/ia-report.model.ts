import { InvoiceType } from 'app/shared/model/invoice-type.model';

export interface IIAReport {
  id?: string;
  companyID?: string;
  branchID?: string;
  reportName?: string;
  invoiceForm?: number;
  invoiceType?: InvoiceType;
  invoiceTypeID?: string;
  invoiceTemplate?: string;
  invoiceCodeVietTel78?: string;
  invoiceSeries?: string;
  copyNumber?: number;
  tempSortOrder?: number;
  purpose1?: string;
  codeColor1?: string;
  purpose2?: string;
  codeColor2?: string;
  purpose3?: string;
  codeColor3?: string;
  purpose4?: string;
  codeColor4?: string;
  purpose5?: string;
  codeColor5?: string;
  purpose6?: string;
  codeColor6?: string;
  purpose7?: string;
  codeColor7?: string;
  purpose8?: string;
  codeColor8?: string;
  purpose9?: string;
  codeColor9?: string;
  circulars?: number;
  verificationCodes?: boolean;
  active?: boolean;
  invoiceYear?: number;
  specificNotation?: string;
  freehandNotation?: string;
  orderPriority?: number;
  invoiceTypeName?: any;
  countInvoice?: any;
  CRMServiceId?: any;
  InvoiceTypeId?: any;
  nameInvoiceType?: any;
  SumNumber?: any;
  NumberNow?: any;
  NumberRest?: any;
  NumberFrom?: any;
  NumberTo?: any;
  Status?: any;
  Active?: any;
  Datetime?: any;
}

export class IAReport implements IIAReport {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public reportName?: string,
    public invoiceForm?: number,
    public invoiceType?: InvoiceType,
    public invoiceTypeID?: string,
    public invoiceTemplate?: string,
    public invoiceCodeVietTel78?: string,
    public invoiceSeries?: string,
    public copyNumber?: number,
    public tempSortOrder?: number,
    public purpose1?: string,
    public codeColor1?: string,
    public purpose2?: string,
    public codeColor2?: string,
    public purpose3?: string,
    public codeColor3?: string,
    public purpose4?: string,
    public codeColor4?: string,
    public purpose5?: string,
    public codeColor5?: string,
    public purpose6?: string,
    public codeColor6?: string,
    public purpose7?: string,
    public codeColor7?: string,
    public purpose8?: string,
    public codeColor8?: string,
    public purpose9?: string,
    public codeColor9?: string,
    public circulars?: number,
    public verificationCodes?: boolean,
    public active?: boolean,
    public invoiceYear?: number,
    public specificNotation?: string,
    public freehandNotation?: string,
    public orderPriority?: number,
    public invoiceTypeName?: any,
    public countInvoice?: any,
    public CRMServiceId?: any,
    public InvoiceTypeId?: any,
    public nameInvoiceType?: any,
    public SumNumber?: any,
    public NumberNow?: any,
    public NumberRest?: any,
    public NumberFrom?: any,
    public NumberTo?: any,
    public Status?: any,
    public Active?: any,
    public Datetime?: any
  ) {}
}
