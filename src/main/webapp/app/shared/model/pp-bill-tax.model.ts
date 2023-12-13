import { Moment } from 'moment';

export interface IPPBillTax {
  id?: string;
  companyID?: string;
  checkDetail?: any;
  typeID?: number;
  result?: number;
  statusInvoice?: number;
  refLink?: string;
  accountingObjectID?: number;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  companyTaxCode?: string;
  contactName?: string;
  reason?: string;
  no?: string;
  invoiceTemplate?: string;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  idTCT?: string;
  totalAmount?: number;
  createVoucherOption?: number;
  check?: boolean;
  totalDiscountAmount?: number;
  totalVATAmount?: number;
  pPBillTaxDetails?: PPBillTaxDetails[];
  checked?: boolean;
  totalPaymentAmount?: number;
  companyTaxCodeBuy?: string;
  paymentMethod?: string;
}

export class PPBillTax implements IPPBillTax {
  constructor(
    public id?: string,
    public companyID?: string,
    public checkDetail?: any,
    public typeID?: number,
    public result?: number,
    public statusInvoice?: number,
    public refLink?: string,
    public accountingObjectID?: number,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public contactName?: string,
    public reason?: string,
    public no?: string,
    public invoiceTemplate?: string,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public idTCT?: string,
    public totalAmount?: number,
    public createVoucherOption?: number,
    public check?: boolean,
    public totalDiscountAmount?: number,
    public pPBillTaxDetails?: PPBillTaxDetails[],
    public totalVATAmount?: number,
    public checked?: boolean,
    public totalPaymentAmount?: number,
    public companyTaxCodeBuy?: string,
    public paymentMethod?: string
  ) {}
}

export function getPPBillIdentifier(pPBillTax: IPPBillTax): string | undefined {
  return pPBillTax.id;
}

export interface IPPBillTaxDetails {
  id?: string;
  pPBillTaxID?: string;
  materialGoodsID?: string;
  description?: string;
  unitID?: string;
  unitName?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  discountRate?: number;
  discountAmount?: number;
  vATRate?: number;
  vATAmount?: number;
  orderPriority?: number;
  materialGoodsType?: number;
}

export class PPBillTaxDetails implements IPPBillTaxDetails {
  constructor(
    public id?: string,
    public pPBillID?: string,
    public pPBillTaxID?: string,
    public materialGoodsID?: string,
    public description?: string,
    public unitID?: string,
    public unitName?: string,
    public quantity?: number,
    public unitPrice?: number,
    public amount?: number,
    public discountRate?: number,
    public discountAmount?: number,
    public vATRate?: number,
    public vATAmount?: number,
    public orderPriority?: number,
    public materialGoodsType?: number
  ) {}
}
