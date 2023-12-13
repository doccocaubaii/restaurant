import { Moment } from 'moment';
import { IMCReceipt } from 'app/shared/model//mc-receipt.model';
import { IBankAccountDetails } from 'app/shared/model//bank-account-details.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { IEMContract } from 'app/shared/model/em-contract.model';

export interface IMCReceiptDetails {
  id?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  amountOriginal?: number;
  contractID?: any;
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
  mcreceipt?: IMCReceipt;
  mCReceiptID?: any;
  bankAccountDetailID?: string;
  accountingObject?: IAccountingObject;
  accountingObjectID?: string;
  statisticsCode?: IStatisticsCode;
  statisticsCodeID?: string;
  organizationUnit?: IOrganizationUnit;
  departmentID?: string;
  expenseItem?: IExpenseItem;
  expenseItemID?: string;
  costSet?: ICostSet;
  costSetID?: string;
  budgetItem?: IBudgetItem;
  budgetItemID?: string;
  year?: number;
  activity?: any;
  business?: any;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  isUnreasonableCost?: boolean;
}

export class MCReceiptDetails implements IMCReceiptDetails {
  constructor(
    public id?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public amountOriginal?: number,
    public contractID?: any,
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
    public mcreceipt?: IMCReceipt,
    public mCReceiptID?: any,
    public bankAccountDetailID?: string,
    public accountingObject?: IAccountingObject,
    public accountingObjectID?: string,
    public statisticsCode?: IStatisticsCode,
    public organizationUnit?: IOrganizationUnit,
    public expenseItem?: IExpenseItem,
    public costSet?: ICostSet,
    public budgetItem?: IBudgetItem,
    public year?: number,
    public activity?: any,
    public business?: any,
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
