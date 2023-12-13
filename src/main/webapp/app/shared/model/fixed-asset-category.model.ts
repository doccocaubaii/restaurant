export interface IFixedAssetCategory {
  id?: string;
  fixedAssetCategoryCode?: string;
  fixedAssetCategoryName?: string;
  description?: string;
  isParentNode?: boolean;
  orderFixCode?: string;
  grade?: number;
  usedTime?: number;
  depreciationRate?: number;
  originalPriceAccount?: string;
  depreciationAccount?: string;
  expenditureAccount?: string;
  isActive?: boolean;
  parentID?: string;
  parent?: IFixedAssetCategory;
}

export class FixedAssetCategory implements IFixedAssetCategory {
  constructor(
    public id?: string,
    public fixedAssetCategoryCode?: string,
    public fixedAssetCategoryName?: string,
    public description?: string,
    public isParentNode?: boolean,
    public orderFixCode?: string,
    public grade?: number,
    public usedTime?: number,
    public depreciationRate?: number,
    public originalPriceAccount?: string,
    public depreciationAccount?: string,
    public expenditureAccount?: string,
    public isActive?: boolean,
    public parentID?: string,
    public parent?: IFixedAssetCategory
  ) {
    this.isParentNode = this.isParentNode || false;
    this.isActive = this.isActive || false;
  }
}
