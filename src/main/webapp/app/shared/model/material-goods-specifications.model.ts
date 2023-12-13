import { IMaterialGoods } from 'app/shared/model//material-goods.model';

export interface IMaterialGoodsSpecifications {
  id?: string;
  isFollow?: boolean;
  materialGoodsSpecificationsCode?: string;
  materialGoodsSpecificationsName?: string;
  description?: string;
  materialGoods?: IMaterialGoods;
  isSame?: boolean;
}

export class MaterialGoodsSpecifications implements IMaterialGoodsSpecifications {
  constructor(
    public id?: string,
    public isFollow?: boolean,
    public materialGoodsSpecificationsCode?: string,
    public materialGoodsSpecificationsName?: string,
    public description?: string,
    public materialGoods?: IMaterialGoods,
    public isSame?: boolean
  ) {
    this.isFollow = this.isFollow || false;
  }
}
