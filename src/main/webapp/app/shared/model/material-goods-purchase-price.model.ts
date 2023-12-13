import { IMaterialGoods } from 'app/shared/model//material-goods.model';

export interface IMaterialGoodsPurchasePrice {
  id?: string;
  unitPrice?: number;
  materialGoods?: IMaterialGoods;
  currencyID?: string;
  unitID?: string;
  index?: number;
}

export class MaterialGoodsPurchasePrice implements IMaterialGoodsPurchasePrice {
  constructor(
    public id?: string,
    public unitPrice?: number,
    public materialGoods?: IMaterialGoods,
    public currencyID?: string,
    public unitID?: string,
    public index?: number
  ) {}
}
