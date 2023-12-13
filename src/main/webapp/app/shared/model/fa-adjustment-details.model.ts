export interface IFAAdjustmentDetails {
  id?: string;
  faAdjustmentID?: string;
  fixAssetID?: string;
  oldRemainingAmountOriginal?: number;
  newRemainingAmount?: number;
  differRemainingAmount?: number;
  oldUsedTime?: number;
  newUsedTime?: number;
  differUsedTime?: number;
  oldAcDepreciationAmount?: number;
  newAcDepreciationAmount?: number;
  differAcDepreciationAmount?: number;
  oldDepreciationAmount?: number;
  newDepreciationAmount?: number;
  differDepreciationAmount?: number;
  costAccount?: string;
  adjustAccount?: string;
  newMonthlyDepreciationAmount?: number;
  orderPriority?: number;
  fixedAssetItem?: any;
  fixedAssetName?: string;
  departmentID?: string;
  newOrgPrice?: any;
  oldOrgPrice?: any;
}

export class FAAdjustmentDetails implements IFAAdjustmentDetails {
  constructor(
    public id?: string,
    public faAdjustmentID?: string,
    public fixAssetID?: string,
    public oldRemainingAmountOriginal?: number,
    public newRemainingAmount?: number,
    public differRemainingAmount?: number,
    public oldUsedTime?: number,
    public newUsedTime?: number,
    public differUsedTime?: number,
    public oldAcDepreciationAmount?: number,
    public newAcDepreciationAmount?: number,
    public differAcDepreciationAmount?: number,
    public oldDepreciationAmount?: number,
    public newDepreciationAmount?: number,
    public differDepreciationAmount?: number,
    public costAccount?: string,
    public adjustAccount?: string,
    public newMonthlyDepreciationAmount?: number,
    public orderPriority?: number,
    public fixedAssetItem?: any,
    public fixedAssetName?: string,
    public departmentID?: string,
    public newOrgPrice?: any,
    public oldOrgPrice?: any
  ) {}
}
