import { Moment } from 'moment';
import { IMCPaymentDetails } from 'app/shared/model//mc-payment-details.model';
import { IMCPaymentDetailInsurance } from 'app/shared/model//mc-payment-detail-insurance.model';
import { IMCPaymentDetailSalary } from 'app/shared/model//mc-payment-detail-salary.model';
import { IMCPaymentDetailTax } from 'app/shared/model//mc-payment-detail-tax.model';
import { IMCPaymentDetailVendor } from 'app/shared/model//mc-payment-detail-vendor.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';

export interface IMCPayment {
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
  accountingObjectAddress?: string;
  receiver?: string;
  reason?: string;
  numberAttach?: string;
  currencyID?: string;
  exchangeRate?: number;
  isImportPurchase?: boolean;
  pPOrderID?: string;
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
  mCAuditID?: string;
  taxCode?: string;
  mCPaymentDetails?: IMCPaymentDetails[];
  mCPaymentDetailSalaries?: IMCPaymentDetailSalary[];
  mCPaymentDetailTaxes?: IMCPaymentDetailTax[];
  mcpaymentDetailVendors?: IMCPaymentDetailVendor[];
  mCPaymentDetailInsurances?: IMCPaymentDetailInsurance[];
  viewVouchers?: IViewVoucher[];
  total?: number;
  ppServiceID?: string;
  ppInvocieID?: string;
  storedInRepository?: boolean;
  billReceived?: boolean;
  isFeightService?: boolean;
  ppBillTaxID?: string;
  bankTransactionID?: any;
}

export class MCPayment implements IMCPayment {
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
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public receiver?: string,
    public reason?: string,
    public numberAttach?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public isImportPurchase?: boolean,
    public pPOrderID?: string,
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
    public mCAuditID?: string,
    public taxCode?: string,
    public mCPaymentDetails?: IMCPaymentDetails[],
    public mCPaymentDetailSalaries?: IMCPaymentDetailSalary[],
    public mCPaymentDetailTaxes?: IMCPaymentDetailTax[],
    public mcpaymentDetailVendors?: IMCPaymentDetailVendor[],
    public mCPaymentDetailInsurances?: IMCPaymentDetailInsurance[],
    public viewVouchers?: IViewVoucher[],
    public total?: number,
    public ppServiceID?: string,
    public ppInvocieID?: string,
    public storedInRepository?: boolean,
    public billReceived?: boolean,
    public isFeightService?: boolean,
    public ppBillTaxID?: string,
    public bankTransactionID?: any
  ) {
    this.isImportPurchase = this.isImportPurchase || false;
    this.recorded = this.recorded || false;
    this.accountingObjectID = accountingObjectID ? accountingObjectID : null;
    this.receiver = receiver ? receiver : '';
    this.isFeightService = this.isFeightService || false;
  }
}
