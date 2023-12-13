export interface IFAAuditDetail {
  id?: string;
  faAuditID?: string;
  fixedAssetID?: string;
  fixedAssetCode?: string;
  fixedAssetName?: string;
  departmentID?: string;
  departmentCode?: string;
  originalPrice?: number;
  depreciationAmount?: number;
  acDepreciationAmount?: number;
  remainingAmount?: number;
  existInStock?: number;
  recommendation?: number;
  note?: string;
  orderPriority?: number;
  fixedAssetItem?: any;
  remainingAccount?: number;
  expenditureAccount?: any;
  originalPriceAccount?: any;
  depreciationAccount?: any;
  acDepreciationAmountGiamGia?: number;
}

export class FAAuditDetail implements IFAAuditDetail {
  constructor(
    public id?: string,
    public faAuditID?: string,
    public fixedAssetID?: string,
    public fixedAssetCode?: string,
    public fixedAssetName?: string,
    public departmentID?: string,
    public departmentCode?: string,
    public originalPrice?: number,
    public depreciationAmount?: number,
    public acDepreciationAmount?: number,
    public remainingAmount?: number,
    public existInStock?: number,
    public recommendation?: number,
    public note?: string,
    public orderPriority?: number,
    public fixedAssetItem?: any,
    public remainingAccount?: number,
    public expenditureAccount?: any,
    public originalPriceAccount?: any,
    public depreciationAccount?: any,
    public acDepreciationAmountGiamGia?: number
  ) {}
}
