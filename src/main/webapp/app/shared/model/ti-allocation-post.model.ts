export interface ITIAllocationPost {
  id?: number;
  tiAllocationID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  amountSave?: number;
  departmentID?: string;
  costSetID?: string;
  expenseItemID?: string;
  budgetItemID?: string;
  statisticsCodeID?: string;
  orderPriority?: number;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
  isUnreasonableCost?: boolean;
}

export class TIAllocationPost implements ITIAllocationPost {
  constructor(
    public id?: number,
    public tiAllocationID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public amountSave?: number,
    public departmentID?: string,
    public costSetID?: string,
    public expenseItemID?: string,
    public budgetItemID?: string,
    public statisticsCodeID?: string,
    public orderPriority?: number,
    public customField1?: any,
    public customField2?: any,
    public customField3?: any,
    public customField4?: any,
    public customField5?: any,
    public isUnreasonableCost?: boolean
  ) {}
}
