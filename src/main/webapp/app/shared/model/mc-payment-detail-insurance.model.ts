import { IBudgetItem } from 'app/shared/model//budget-item.model';
import { ICostSet } from 'app/shared/model//cost-set.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IExpenseItem } from 'app/shared/model//expense-item.model';
import { IStatisticsCode } from 'app/shared/model//statistics-code.model';

export interface IMCPaymentDetailInsurance {
  id?: string;
  mCPaymentID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  accumAmount?: number;
  payAmount?: number;
  remainningAmount?: number;
  contractID?: string;
  orderPriority?: number;
  budgetItemID?: string;
  costSetID?: string;
  accountingObjectID?: string;
  departmentID?: string;
  expenseItemID?: string;
  statisticsCodeID?: string;
  isUnreasonableCost?: boolean;
}

export class MCPaymentDetailInsurance implements IMCPaymentDetailInsurance {
  constructor(
    public id?: string,
    public mCPaymentID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public accumAmount?: number,
    public payAmount?: number,
    public remainningAmount?: number,
    public contractID?: string,
    public orderPriority?: number,
    public budgetItemID?: string,
    public costSetID?: string,
    public accountingObjectID?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public statisticsCodeID?: string,
    public isUnreasonableCost?: boolean
  ) {}
}
