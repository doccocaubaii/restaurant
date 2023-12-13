import { Moment } from 'moment';
import { IMBInternalTransfer } from 'app/shared/model//mb-internal-transfer.model';

export interface IMBInternalTransferDetails {
  id?: string;
  debitAccount?: string;
  creditAccount?: string;
  fromBankAccountDetailID?: string;
  toBankAccountDetailID?: string;
  fromBranchID?: string;
  toBranchID?: string;
  currencyID?: string;
  description?: string;
  exchangeRate?: number;
  amount?: number;
  amountOriginal?: number;
  employeeID?: string;
  budgetItemID?: string;
  costSetID?: string;
  contractID?: string;
  statisticsCodeID?: string;
  departmentID?: string;
  expenseItemID?: string;
  accountingObjectID?: string;
  cashOutExchangeRateFB?: number;
  cashOutAmountFB?: number;
  cashOutDifferAmountFB?: number;
  cashOutDifferAccountFB?: number;
  cashOutExchangeRateMB?: number;
  cashOutAmountMB?: number;
  cashOutDifferAmountMB?: number;
  cashOutDifferAccountMB?: string;
  isMatch?: boolean;
  matchDate?: Moment | any;
  orderPriority?: number;
  mBInternalTransfer?: IMBInternalTransfer;
  year?: number;
  activity?: any;
  business?: any;
  isUnreasonableCost?: boolean;
}

export class MBInternalTransferDetails implements IMBInternalTransferDetails {
  constructor(
    public id?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public fromBankAccountDetailID?: string,
    public toBankAccountDetailID?: string,
    public fromBranchID?: string,
    public toBranchID?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public description?: string,
    public amount?: number,
    public amountOriginal?: number,
    public employeeID?: string,
    public budgetItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public statisticsCodeID?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public accountingObjectID?: string,
    public cashOutExchangeRateFB?: number,
    public cashOutAmountFB?: number,
    public cashOutDifferAmountFB?: number,
    public cashOutDifferAccountFB?: number,
    public cashOutExchangeRateMB?: number,
    public cashOutAmountMB?: number,
    public cashOutDifferAmountMB?: number,
    public cashOutDifferAccountMB?: string,
    public isMatch?: boolean,
    public matchDate?: Moment | any,
    public orderPriority?: number,
    public mBInternalTransfer?: IMBInternalTransfer,
    public year?: number,
    public activity?: any,
    public business?: any,
    public isUnreasonableCost?: boolean
  ) {
    this.isMatch = this.isMatch || false;
  }
}
