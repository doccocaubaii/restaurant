import { ITools } from 'app/shared/model/tools.model';
import { IFixedAsset } from 'app/shared/model/fixed-asset.model';

export interface IFAIncrementDetails {
  id?: number;
  faIncrementID?: string;
  fixedAssetID?: string;
  fixedAssetItem?: any;
  description?: string;
  unitID?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  accountingObjectID?: string;
  budgetItemID?: string;
  costSetID?: string;
  contractID?: string;
  statisticsCodeID?: string;
  departmentID?: string;
  expenseItemID?: string;
  orderPriority?: number;
  fixedAssetName?: string;
  fixedAssetCode?: string;
  checkDuplicate?: any;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FAIncrementDetails implements IFAIncrementDetails {
  constructor(
    public id?: number,
    public faIncrementID?: string,
    public fixedAssetID?: string,
    public fixedAssetItem?: IFixedAsset,
    public description?: string,
    public unitID?: string,
    public quantity?: number,
    public unitPrice?: number,
    public amount?: number,
    public accountingObjectID?: string,
    public budgetItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public statisticsCodeID?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public fixedAssetName?: string,
    public fixedAssetCode?: string,
    public orderPriority?: number,
    public checkDuplicate?: any
  ) {}
}
