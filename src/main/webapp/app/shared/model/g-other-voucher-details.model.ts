import * as moment from 'moment';
import { Moment } from 'moment';

export interface IGOtherVoucherDetails {
  index?: number;
  id?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amountOriginal?: number;
  amount?: number;
  debitAccountingObjectID?: string;
  creditAccountingObjectID?: string;
  employeeID?: string;
  bankAccountDetailID?: string;
  expenseItemID?: string;
  costSetID?: string;
  contractID?: string;
  budgetItemID?: string;
  departmentID?: string;
  statisticCodeID?: string;
  cashOutAmountVoucherOriginal?: number;
  cashOutAmountVoucher?: number;
  cashOutExchangeRateFB?: number;
  cashOutAmountFB?: number;
  cashOutDifferAmountFB?: number;
  cashOutDifferAccountFB?: string;
  cashOutExchangeRateMB?: number;
  cashOutAmountMB?: number;
  cashOutDifferAmountMB?: number;
  cashOutDifferAccountMB?: string;
  isMatch?: boolean;
  matchDate?: Moment | any;
  orderPriority?: number;
  lastOrganizationUnitCode?: string;
  stt?: number;
  activity?: any;
  business?: any;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  pPBillID?: string;
  pPBillDetailID?: string;
  isUnreasonableCost?: boolean;
}

export class GOtherVoucherDetails implements IGOtherVoucherDetails {
  constructor(
    public index?: number,
    public id?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amountOriginal?: number,
    public amount?: number,
    public debitAccountingObjectID?: string,
    public creditAccountingObjectID?: string,
    public employeeID?: string,
    public bankAccountDetailID?: string,
    public expenseItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public budgetItemID?: string,
    public departmentID?: string,
    public statisticCodeID?: string,
    public cashOutAmountVoucherOriginal?: number,
    public cashOutAmountVoucher?: number,
    public cashOutExchangeRateFB?: number,
    public cashOutAmountFB?: number,
    public cashOutDifferAmountFB?: number,
    public cashOutDifferAccountFB?: string,
    public cashOutExchangeRateMB?: number,
    public cashOutAmountMB?: number,
    public cashOutDifferAmountMB?: number,
    public cashOutDifferAccountMB?: string,
    public isMatch?: boolean,
    public matchDate?: Moment | any,
    public orderPriority?: number,
    public lastOrganizationUnitCode?: string,
    public stt?: number,
    public activity?: any,
    public business?: any,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public pPBillID?: string,
    public pPBillDetailID?: string,
    public isUnreasonableCost?: boolean
  ) {
    this.isMatch = this.isMatch || false;
  }
}
