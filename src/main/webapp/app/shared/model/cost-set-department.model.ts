import { ICostSetMaterialGood } from 'app/shared/model/cost-set-material-good.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';

export interface ICostSetDepartment {
  id?: string;
  objectCode?: string;
  objectName?: string;
}

export class CostSet implements ICostSetDepartment {
  constructor(public id?: string, public objectCode?: string, public objectName?: string) {}
}
