import { Moment } from 'moment';
import { IMBDepositDetails } from 'app/shared/model/mb-deposit-details.model';
import { IViewVoucher } from 'app/shared/model/view-voucher.model';
import { IGOtherVoucherDetails } from 'app/shared/model/g-other-voucher-details.model';
import { IGOtherVoucherDetailTax } from 'app/shared/model/g-other-voucher-detail-tax.model';
import { IRefVoucher } from 'app/shared/model/ref-voucher.model';
import { IGOtherVoucherDetailExpenseAllocation } from 'app/shared/model/g-other-voucher-detail-expense-allocation.model';
import { IGOtherVoucherDetailExpense } from 'app/shared/model/g-other-voucher-detail-expense.model';
import { IGOtherVoucherDetailSalary } from './go-ther-voucher-detail-salary.model';

export interface IGOtherVoucher {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  currencyID?: string;
  exchangeRate?: number;
  typeLedger?: number;
  reason?: string;
  noMBook?: string;
  noFBook?: string;
  date?: Moment | any;
  postedDate?: Moment | any;
  totalAmount?: number;
  totalAmountOriginal?: number;
  recorded?: boolean;
  gOtherVoucherDetails?: IGOtherVoucherDetails[];
  gOtherVoucherDetailTax?: IGOtherVoucherDetailTax[];
  gOtherVoucherDetailExpenses?: IGOtherVoucherDetailExpense[];
  gOtherVoucherDetailSalaries?: IGOtherVoucherDetailSalary[];
  gOtherVoucherDetailExpenseAllocations?: IGOtherVoucherDetailExpenseAllocation[];
  gOtherVoucherDetailDebtPayments?: any[];
  gOtherVoucherDetailDebtPaymentsDTO?: any[];
  gOtherVoucherDetailForeignCurrencies?: any[];
  gOtherVoucherDetailForeignCurrenciesDTO?: any[];
  gOtherVoucherDetailExcepts?: any[];
  gOtherVoucherDetailExceptsDTO?: any[];
  viewVouchers?: IViewVoucher[];
  templateID?: string;
  no?: string;
  typeGroup?: number;
  refVouchers?: IRefVoucher[];
  currentBook?: string;
  noBook?: any;
  total?: number;
  sumAmount?: any;
  cPPeriodID?: any;
  sumTotalAmount?: number;
  pSSalarySheetID?: string;
  exceptVouchers?: any;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  billReceived?: boolean;
  isFeightService?: boolean;
  ppBillTaxID?: string;
}

export class GOtherVoucher implements IGOtherVoucher {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public currencyID?: string,
    public exchangeRate?: number,
    public typeLedger?: number,
    public reason?: string,
    public noMBook?: string,
    public noFBook?: string,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public recorded?: boolean,
    public gOtherVoucherDetails?: IGOtherVoucherDetails[],
    public gOtherVoucherDetailTax?: IGOtherVoucherDetailTax[],
    public gOtherVoucherDetailExpenses?: IGOtherVoucherDetailExpense[],
    public gOtherVoucherDetailSalaries?: IGOtherVoucherDetailSalary[],
    public gOtherVoucherDetailExpenseAllocations?: IGOtherVoucherDetailExpenseAllocation[],
    public gOtherVoucherDetailDebtPayments?: any[],
    public gOtherVoucherDetailDebtPaymentsDTO?: any[],
    public gOtherVoucherDetailForeignCurrencies?: any[],
    public gOtherVoucherDetailForeignCurrenciesDTO?: any[],
    public gOtherVoucherDetailExcepts?: any[],
    public gOtherVoucherDetailExceptsDTO?: any[],
    public viewVouchers?: IViewVoucher[],
    public templateID?: string,
    public no?: string,
    public typeGroup?: number,
    public refVouchers?: IRefVoucher[],
    public noBook?: any,
    public total?: number,
    public sumAmount?: any,
    public cPPeriodID?: any,
    public sumTotalAmount?: number,
    public pSSalarySheetID?: string,
    public exceptVouchers?: any,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public billReceived?: boolean,
    public isFeightService?: boolean,
    public ppBillTaxID?: string
  ) {
    this.recorded = this.recorded || false;
    this.isFeightService = this.isFeightService || false;
  }
}
