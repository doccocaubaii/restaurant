import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IPaymentClause } from 'app/shared/model//payment-clause.model';
import { ISAQuoteDetails } from 'app/shared/model/sa-quote-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface ISAQuote {
  id?: string;
  companyId?: string;
  branchId?: string;
  typeId?: number;
  typeLedger?: number;
  date?: Moment | any;
  no?: string;
  noMBook?: string;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  companyTaxCode?: string;
  contactName?: string;
  contactMobile?: string;
  contactEmail?: string;
  deliveryTime?: string;
  guaranteeDuration?: string;
  description?: string;
  reason?: string;
  currencyID?: string;
  exchangeRate?: number;
  finalDate?: Moment | any;
  employeeID?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  totalDiscountAmount?: number;
  totalDiscountAmountOriginal?: number;
  templateID?: string;
  accountingObject?: IAccountingObject;
  paymentClause?: IPaymentClause;
  sAQuoteDetails?: ISAQuoteDetails[];
  viewVouchers?: IViewVoucher[];
  total?: number;
  totalOriginal?: number;
  totalMoney?: number;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class SAQuote implements ISAQuote {
  constructor(
    public id?: string,
    public companyId?: string,
    public branchId?: string,
    public typeId?: number,
    public typeLedger?: number,
    public date?: Moment | any,
    public no?: string,
    public noMBook?: string,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public contactName?: string,
    public contactMobile?: string,
    public contactEmail?: string,
    public deliveryTime?: string,
    public guaranteeDuration?: string,
    public description?: string,
    public reason?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public finalDate?: Moment | any,
    public employeeID?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public totalDiscountAmount?: number,
    public totalDiscountAmountOriginal?: number,
    public templateID?: string,
    public accountingObject?: IAccountingObject,
    public paymentClause?: IPaymentClause,
    public sAQuoteDetails?: ISAQuoteDetails[],
    public viewVouchers?: IViewVoucher[],
    public total?: number,
    public totalMoney?: number,
    public totalOriginal?: number,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
