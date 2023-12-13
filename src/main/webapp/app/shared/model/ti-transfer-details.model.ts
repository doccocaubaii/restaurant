export interface ITITransferDetails {
  id?: number;
  tiTransferID?: string;
  toolsID?: string;
  toolsName?: string;
  toolsCode?: string;
  description?: string;
  fromDepartmentID?: string;
  toDepartmentID?: string;
  quantity?: number;
  transferQuantity?: number;
  costAccount?: string;
  orderPriority?: number;
  toolsItem?: any;
  fromDepartmentCode?: any;
  toDepartmentCode?: any;
  debitAccount?: any;
  statisticsCode?: any;
  costSetCode?: any;
  expenseItemCode?: any;
  budgetItemCode?: any;
  organizationUnitsFrom?: any;
  contractCode?: any;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class TITransferDetails implements ITITransferDetails {
  constructor(
    public id?: number,
    public tiTransferID?: string,
    public toolsID?: string,
    public toolsName?: string,
    public toolsCode?: string,
    public description?: string,
    public fromDepartmentID?: string,
    public toDepartmentID?: string,
    public quantity?: number,
    public transferQuantity?: number,
    public costAccount?: string,
    public orderPriority?: number,
    public toolsItem?: any,
    public fromDepartmentCode?: any,
    public toDepartmentCode?: any,
    public debitAccount?: any,
    public statisticsCode?: any,
    public costSetCode?: any,
    public expenseItemCode?: any,
    public budgetItemCode?: any,
    public organizationUnitsFrom?: any,
    public contractCode?: any,
    public customField1?: any,
    public customField2?: any,
    public customField3?: any,
    public customField4?: any,
    public customField5?: any
  ) {}
}
