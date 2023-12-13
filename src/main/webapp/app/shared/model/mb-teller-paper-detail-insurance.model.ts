export interface IMBTellerPaperDetailInsurance {
  id?: string;
  mBTellerPaperID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  accumAmount?: number;
  payAmount?: number;
  remainningAmount?: number;
  budgetItemID?: string;
  costSetID?: string;
  contractID?: string;
  accountingObjectID?: string;
  statisticsCodeID?: string;
  departmentID?: string;
  expenseItemID?: string;
  orderPriority?: number;
}

export class MBTellerPaperDetailInsurance implements IMBTellerPaperDetailInsurance {
  constructor(
    public id?: string,
    public mBTellerPaperID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public accumAmount?: number,
    public payAmount?: number,
    public remainningAmount?: number,
    public budgetItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public accountingObjectID?: string,
    public statisticsCodeID?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public orderPriority?: number
  ) {}
}
