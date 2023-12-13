export interface IFaDecrementDetails {
  id?: number;
  faDecrementID?: string;
  fixedAssetID?: string;
  departmentID?: string;
  originalPrice?: number;
  depreciationAmount?: number;
  acDepreciationAmount?: number;
  remainingAmount?: number;
  originalPriceAccount?: string;
  depreciationAccount?: string;
  expenditureAccount?: string;
  remainingAccount?: string;
  orderPriority?: number;
  fixedAssetItem?: any;
  fixedAssetName?: any;
  faDecrementDetailPosts?: any[];
  checkDuplicate?: boolean;
  fixedAssetCode?: boolean;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class FaDecrementDetails implements IFaDecrementDetails {
  constructor(
    public id?: number,
    public faDecrementID?: string,
    public fixedAssetID?: string,
    public departmentID?: string,
    public originalPrice?: number,
    public depreciationAmount?: number,
    public acDepreciationAmount?: number,
    public remainingAmount?: number,
    public originalPriceAccount?: string,
    public depreciationAccount?: string,
    public expenditureAccount?: string,
    public remainingAccount?: string,
    public orderPriority?: number,
    public fixedAssetItem?: any,
    public fixedAssetName?: any,
    public faDecrementDetailPosts?: any[],
    public checkDuplicate?: boolean,
    public fixedAssetCode?: any
  ) {}
}
