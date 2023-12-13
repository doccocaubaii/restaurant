import { IMaterialGoods } from 'app/shared/model//material-goods.model';

export interface IMaterialGoodsConvertUnit {
  id?: string;
  orderNumber?: number;
  convertRate?: number;
  formula?: string;
  description?: string;
  materialGoods?: IMaterialGoods;
  materialGoodsID?: string;
  unitID?: string;
  fixedSalePrice?: number;
  salePrice1?: number;
  salePrice2?: number;
  salePrice3?: number;
  unitName?: string;
}

export class MaterialGoodsConvertUnit implements IMaterialGoodsConvertUnit {
  constructor(
    public id?: string,
    public orderNumber?: number,
    public convertRate?: number,
    public formula?: string,
    public description?: string,
    public materialGoods?: IMaterialGoods,
    public materialGoodsID?: string,
    public unitID?: string,
    public fixedSalePrice?: number,
    public salePrice1?: number,
    public salePrice2?: number,
    public salePrice3?: number,
    public unitName?: string
  ) {}
}
