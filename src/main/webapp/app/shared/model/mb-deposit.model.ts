import { Moment } from 'moment';
import * as moment from 'moment';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IMBDepositDetailTax } from 'app/shared/model/mb-deposit-detail-tax.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IBankAccountDetails } from 'app/shared/model/bank-account-details.model';
import { IBank } from 'app/shared/model/bank.model';
import { ICurrency } from 'app/shared/model/currency.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IMBDepositDetailCustomer } from 'app/shared/model/mb-deposit-detail-customer.model';

export interface IMBDeposit {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  accountingObjectID?: string;
  accountingObjectName?: string;
  // accountingObjectID?: string;
  accountingObjectAddress?: string;
  bankName?: string;
  reason?: string;
  exchangeRate?: number;
  paymentClauseID?: string;
  transportMethodID?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  employeeID?: string;
  accountingObjectType?: number;
  templateID?: string;
  recorded?: boolean;
  exported?: boolean;
  isMatch?: boolean;
  matchDate?: Moment | any;
  // mBDepositDetailTax?: IMBDepositDetailTax[];
  bankAccountDetailID?: string;
  currencyID?: string;
  mBDepositDetails?: IMBDepositDetails[];
  mBDepositDetailCustomers?: IMBDepositDetailCustomer[];
  viewVouchers?: IViewVoucher[];
  total?: number;
  sAInvoiceID?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  ppBillTaxID?: string;
  bankTransactionID?: any;
}

export class MBDeposit implements IMBDeposit {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public accountingObjectID?: string,
    // public accountingObjectID?: string,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public bankName?: string,
    public reason?: string,
    public exchangeRate?: number,
    public sAQuoteID?: string,
    public sAOrderID?: string,
    public paymentClauseID?: string,
    public transportMethodID?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public employeeID?: string,
    public accountingObjectType?: number,
    public templateID?: string,
    public recorded?: boolean,
    public exported?: boolean,
    public isMatch?: boolean,
    public matchDate?: Moment | any,
    public mBDepositDetails?: IMBDepositDetails[],
    // public mBDepositDetailTax?: IMBDepositDetailTax[],
    public mBDepositDetailCustomer?: IMBDepositDetailCustomer[],
    public bankAccountDetailID?: string,
    public currencyID?: string,
    public viewVouchers?: IViewVoucher[],
    public total?: number,
    public sAInvoiceID?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public ppBillTaxID?: string,
    public bankTransactionID?: any
  ) {
    this.recorded = this.recorded || false;
    this.exported = this.exported || false;
    this.isMatch = this.isMatch || false;
  }
}
