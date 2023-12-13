import { Moment } from 'moment';
import { IBankAccountDetails } from 'app/shared/model//bank-account-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMCPaymentDetails {
  id?: string;
  mCPaymentID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  amountOriginal?: number;
  contractID?: string;
  cashOutExchangeRateFB?: number;
  cashOutAmountFB?: number;
  cashOutDifferAmountFB?: number;
  cashOutDifferAccountFB?: number;
  cashOutExchangeRateMB?: number;
  cashOutAmountMB?: number;
  cashOutDifferAmountMB?: number;
  cashOutDifferAccountMB?: number;
  isMatch?: boolean;
  matchDate?: Moment | any;
  orderPriority?: number;
  bankAccountDetailID?: string;
  accountingObject?: IAccountingObject;
  budgetItem?: IBudgetItem;
  costSet?: ICostSet;
  statisticsCode?: IStatisticsCode;
  organizationUnit?: IOrganizationUnit;
  expenseItem?: IExpenseItem;
  year?: number;
  activity?: any;
  business?: any;
  pPBillID?: string;
  pPBillDetailID?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  isUnreasonableCost?: boolean;
}

export class MCPaymentDetails implements IMCPaymentDetails {
  constructor(
    public id?: string,
    public mCPaymentID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public amountOriginal?: number,
    public contractID?: string,
    public cashOutExchangeRateFB?: number,
    public cashOutAmountFB?: number,
    public cashOutDifferAmountFB?: number,
    public cashOutDifferAccountFB?: number,
    public cashOutExchangeRateMB?: number,
    public cashOutAmountMB?: number,
    public cashOutDifferAmountMB?: number,
    public cashOutDifferAccountMB?: number,
    public isMatch?: boolean,
    public matchDate?: Moment | any,
    public orderPriority?: number,
    public bankAccountDetailID?: string,
    public accountingObject?: IAccountingObject,
    public budgetItem?: IBudgetItem,
    public costSet?: ICostSet,
    public statisticsCode?: IStatisticsCode,
    public organizationUnit?: IOrganizationUnit,
    public expenseItem?: IExpenseItem,
    public year?: number,
    public activity?: any,
    public business?: any,
    public pPBillID?: string,
    public pPBillDetailID?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public isUnreasonableCost?: boolean
  ) {
    this.isMatch = this.isMatch || false;
  }
}
