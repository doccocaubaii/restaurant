import { Moment } from 'moment';

export interface IFaTransferDetails {
  id?: string;
  faTransferlID?: string;
  fixedAssetID?: string;
  description?: string;
  fromDepartmentID?: string;
  toDepartmentID?: string;
  costAccount?: string;
  employeeID?: string;
  statisticCodeID?: string;
  costSetID?: string;
  expenseItemID?: string;
  budgetItemID?: string;
  orderPriority?: number;
  fixedAssetItem?: any;
  fixedAssetName?: string;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FaTransferDetail implements IFaTransferDetails {
  constructor(
    public id?: string,
    public faTransferlID?: string,
    public fixedAssetID?: string,
    public description?: string,
    public fromDepartmentID?: string,
    public toDepartmentID?: string,
    public costAccount?: string,
    public employeeID?: string,
    public statisticCodeID?: string,
    public costSetID?: string,
    public expenseItemID?: string,
    public budgetItemID?: string,
    public orderPriority?: number,
    public fixedAssetItem?: any,
    public fixedAssetName?: any
  ) {}
}
