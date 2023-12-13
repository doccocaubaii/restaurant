import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model//unit.model';

export interface IMaterialGoodsAssembly {
  id?: string;
  materialAssemblyID?: string;
  repositoryID?: string;
  materialAssemblyDescription?: string;
  materialGoodsName?: string;
  quantity?: number;
  unitPrice?: number;
  totalAmount?: number;
  materialGoods?: IMaterialGoods;
  materialGoodsID?: string;
  unitID?: string;
  unit?: IUnit;
  units?: IUnit[];
  unitPriceOriginals?: any[];
  index?: number;
}

export class MaterialGoodsAssembly implements IMaterialGoodsAssembly {
  constructor(
    public id?: string,
    public materialAssemblyID?: string,
    public materialAssemblyDescription?: string,
    public repositoryID?: string,
    public materialGoodsName?: string,
    public quantity?: number,
    public unitPrice?: number,
    public totalAmount?: number,
    public materialGoodsID?: string,
    public unitID?: string,
    public materialGoods?: IMaterialGoods,
    public unit?: IUnit,
    public units?: IUnit[],
    public unitPriceOriginals?: any[],
    public index?: number
  ) {}
}
