export interface IFaDecrementDetailsPost {
  id?: number;
  faDecrementDetailID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  budgetItemID?: string;
  costSetID?: string;
  contractID?: string;
  accountingObjectID?: string;
  employeeID?: string;
  statisticsCodeID?: string;
  expenseItemID?: string;
  orderPriority?: number;
  fixedAssetID?: string;
  fixedAssetCode?: string;
  orderPriorityDetail?: number;
  index?: number;
  isUnreasonableCost?: boolean;
}

export class FaDecrementDetailsPost implements IFaDecrementDetailsPost {
  constructor(
    public id?: number,
    public faDecrementDetailID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public budgetItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public accountingObjectID?: string,
    public employeeID?: string,
    public statisticsCodeID?: string,
    public expenseItemID?: string,
    public orderPriority?: number,
    public fixedAssetID?: string,
    public fixedAssetCode?: string,
    public orderPriorityDetail?: number,
    public index?: number,
    public isUnreasonableCost?: boolean
  ) {}
}
