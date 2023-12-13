import { IMaterialGoodsInStock, MaterialGoods } from 'app/shared/model/material-goods.model';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';

export interface IRSProductionOrderQuantumn {
  id?: number;
  rsProductionOrderDetailID?: string;
  materialGoodsID?: string;
  materialGoodsCode?: string;
  description?: string;
  unitID?: string;
  quantityOnUnit?: number;
  quantity?: number;
  costSetID?: string;
  expenseItemID?: string;
  orderPriority?: number;
  materialGood?: IMaterialGoodsInStock;
  convertRates?: any;
  unit?: Unit;
  units?: IUnit[];
  costSet?: ICostSet;
  costSets?: ICostSet[];
  expenseItem?: IExpenseItem;
  expenseItems?: IExpenseItem[];
  amount?: number;
  owAmount?: number;
  materialGoods?: MaterialGoods;
  oWPrice?: number;
  mainConvertRate?: number;
  formula?: string;
  unitPrice?: number;
  unitPrices?: any[];
  mainQuantity?: number;
  unitPriceOriginal?: number;
  amountOriginal?: number;
  mainUnit?: Unit;
  mainUnitId?: string;
  unitName?: string;
  checkCoincide?: boolean;
  expenseItemCode?: string;
  costSetCode?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class RSProductionOrderQuantumn implements IRSProductionOrderQuantumn {
  constructor(
    public id?: number,
    public rsProductionOrderDetailID?: string,
    public materialGoodsID?: string,
    public description?: string,
    public unitID?: string,
    public quantityOnUnit?: number,
    public quantity?: number,
    public costSetID?: string,
    public expenseItemID?: string,
    public orderPriority?: number,
    public materialGood?: IMaterialGoodsInStock,
    public convertRates?: any,
    public unit?: Unit,
    public units?: IUnit[],
    public costSet?: ICostSet,
    public costSets?: ICostSet[],
    public expenseItem?: IExpenseItem,
    public expenseItems?: IExpenseItem[],
    public amount?: number,
    public owAmount?: number,
    public materialGoods?: MaterialGoods,
    public oWPrice?: number,
    public mainConvertRate?: number,
    public formula?: string,
    public unitPrice?: number,
    public unitPrices?: any[],
    public mainQuantity?: number,
    public unitPriceOriginal?: number,
    public amountOriginal?: number,
    public mainUnit?: Unit,
    public mainUnitId?: string,
    public unitName?: string,
    public checkCoincide?: boolean,
    public expenseItemCode?: string,
    public costSetCode?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
