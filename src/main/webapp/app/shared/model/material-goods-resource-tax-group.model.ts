import { IUnit } from 'app/shared/model//unit.model';

export interface IMaterialGoodsResourceTaxGroup {
  id?: string;
  materialGoodsResourceTaxGroupCode?: string;
  materialGoodsResourceTaxGroupName?: string;
  unitID?: string;
  taxRate?: number;
  orderFixCode?: string;
  parentID?: string;
  isParentNode?: boolean;
  grade?: number;
  active?: boolean;
  isSecurity?: boolean;
  unitName?: string;
  son?: IUnit;
  parent?: IMaterialGoodsResourceTaxGroup;
}

export class MaterialGoodsResourceTaxGroup implements IMaterialGoodsResourceTaxGroup {
  constructor(
    public id?: string,
    public materialGoodsResourceTaxGroupCode?: string,
    public materialGoodsResourceTaxGroupName?: string,
    public unitID?: string,
    public taxRate?: number,
    public orderFixCode?: string,
    public parentID?: string,
    public isParentNode?: boolean,
    public grade?: number,
    public active?: boolean,
    public isSecurity?: boolean,
    public unitName?: string,
    public son?: IUnit,
    public parent?: IMaterialGoodsResourceTaxGroup
  ) {
    this.isParentNode = this.isParentNode || false;
    this.active = this.active || false;
    this.isSecurity = this.isSecurity || false;
  }
}
