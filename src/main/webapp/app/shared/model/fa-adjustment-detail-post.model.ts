export interface IFAAdjustmentDetailPost {
  id?: string;
  faAdjustmentID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  costSetID?: string;
  accountingObjectID?: string;
  statisticsCodeID?: string;
  contractID?: string;
  departmentID?: string;
  expenseItemID?: string;
  budgetItemID?: string;
  orderPriority?: number;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  isUnreasonableCost?: boolean;
}

export class FAAdjustmentDetailPost implements IFAAdjustmentDetailPost {
  constructor(
    public id?: string,
    public faAdjustmentID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public costSetID?: string,
    public accountingObjectID?: string,
    public statisticsCodeID?: string,
    public contractID?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public budgetItemID?: string,
    public orderPriority?: number,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public isUnreasonableCost?: boolean
  ) {}
}
