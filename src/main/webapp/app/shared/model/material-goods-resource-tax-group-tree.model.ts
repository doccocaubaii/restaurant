import { IMaterialGoodsResourceTaxGroup } from 'app/shared/model/material-goods-resource-tax-group.model';

export interface ITreeMaterialGoodsResourceTaxGroup {
  parent?: IMaterialGoodsResourceTaxGroup;
  index?: number;
  select?: boolean;
  check?: boolean;
  children?: ITreeMaterialGoodsResourceTaxGroup[];
}

export class TreeMaterialGoodsResourceTaxGroup implements ITreeMaterialGoodsResourceTaxGroup {
  constructor(
    public parent?: IMaterialGoodsResourceTaxGroup,
    public index?: number,
    public select?: boolean,
    public check?: boolean,
    public children?: ITreeMaterialGoodsResourceTaxGroup[]
  ) {
    this.select = this.select || false;
    this.check = this.check || false;
  }
}
