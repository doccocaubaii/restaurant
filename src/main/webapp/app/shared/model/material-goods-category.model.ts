import { IBudgetItem } from 'app/shared/model/budget-item.model';

export interface IMaterialGoodsCategory {
  id?: string;
  companyID?: string;
  branchID?: string;
  materialGoodsCategoryCode?: string;
  materialGoodsCategoryName?: string;
  parentID?: string;
  isParentNode?: boolean;
  orderFixCode?: string;
  grade?: number;
  isActive?: boolean;
  isSecurity?: boolean;
  parent?: IMaterialGoodsCategory;
  checked?: boolean;
}

export class MaterialGoodsCategory implements IMaterialGoodsCategory {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public materialGoodsCategoryCode?: string,
    public materialGoodsCategoryName?: string,
    public parentID?: string,
    public isParentNode?: boolean,
    public orderFixCode?: string,
    public grade?: number,
    public isActive?: boolean,
    public isSecurity?: boolean,
    public checked?: boolean
  ) {
    this.isParentNode = this.isParentNode || false;
    this.isActive = this.isActive || false;
    this.isSecurity = this.isSecurity || false;
    this.checked = this.checked || false;
  }
}
