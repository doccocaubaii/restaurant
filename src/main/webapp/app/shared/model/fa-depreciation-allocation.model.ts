import { Moment } from 'moment';

export interface IFADepreciationAllocation {
  amount?: number;
  checked?: boolean;
  id?: string;
  faDepreciationID?: string;
  fixAssetID?: string;
  fixedAssetCode?: string;
  fixedAssetName?: string;
  description?: string;
  objectID?: string;
  objectType?: number;
  rate?: number;
  allocationAmount?: number;
  costAccount?: string;
  costSetID?: string;
  costSetCode?: string;
  expenseItemID?: string;
  expenseItemCode?: string;
  orderPriority?: number;
  fixedAssetItem?: any;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class FADepreciationAllocation implements IFADepreciationAllocation {
  public customField1?: string;
  public customField2?: string;
  public customField3?: string;
  public customField4?: string;
  public customField5?: string;
  constructor(
    public id?: string,
    public faDepreciationID?: string,
    public fixAssetID?: string,
    public fixedAssetCode?: string,
    public fixedAssetName?: string,
    public description?: string,
    public objectID?: string,
    public objectType?: number,
    public rate?: number,
    public allocationAmount?: number,
    public costAccount?: string,
    public costSetID?: string,
    public costSetCode?: string,
    public expenseItemID?: string,
    public expenseItemCode?: string,
    public orderPriority?: number,
    public checked?: boolean,
    public amount?: number,
    public fixedAssetItem?: any
  ) {}
}
