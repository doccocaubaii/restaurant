import { IMaterialGoodsInStock, MaterialGoods } from 'app/shared/model/material-goods.model';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { ISAOrder } from 'app/shared/model/sa-order.model';
import { ISAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { ISAInvoice } from 'app/shared/model/sa-invoice.model';
import { ISAInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';
import { RSProductionOrderQuantumn } from 'app/shared/model/rs-production-order-quantumn-details.model';

export interface IRSProductionOrderDetails {
  id?: string;
  rsProductionOrderID?: string;
  materialGoodsID?: string;
  description?: string;
  unitID?: string;
  quantity?: number;
  sAOrderID?: string;
  costSetID?: string;
  contractID?: string;
  orderPriority?: number;
  creditAccount?: string;
  convertRates?: any;
  unit?: Unit;
  units?: IUnit[];
  mainUnitId?: string;
  materialGood?: IMaterialGoodsInStock;
  oWPrice?: number;
  amountOriginal?: number;
  amount?: number;
  owAmount?: number;
  materialGoods?: MaterialGoods;
  materialGoodsCode?: string;
  materialGoodsName?: string;
  unitName?: string;
  sAOrderDetailID?: string;
  saOrder?: ISAOrder;
  saOrderDetail?: ISAOrderDetails;
  noBookSaOrder?: string;
  mainUnitID?: string;
  costSet?: ICostSet;
  costSets?: ICostSet[];
  saInvoice?: ISAInvoice;
  saInvoiceDetail?: ISAInvoiceDetails;
  contractDetailID?: string;
  noBookEmContract?: string;
  costSetName?: string;
  costSetCode?: string;
  viewEMContract?: any;
  checkCoincide?: boolean;
  rsProductionOrderQuantumns?: RSProductionOrderQuantumn[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  // checkSAOrder?: boolean;
}

export class RSProductionOrderDetails implements IRSProductionOrderDetails {
  constructor(
    public id?: string,
    public rsProductionOrderID?: string,
    public materialGoodsID?: string,
    public description?: string,
    public unitID?: string,
    public quantity?: number,
    public sAOrderID?: string,
    public costSetID?: string,
    public contractID?: string,
    public orderPriority?: number,
    public creditAccount?: string,
    public convertRates?: any,
    public unit?: Unit,
    public units?: IUnit[],
    public mainUnitId?: string,
    public materialGood?: IMaterialGoodsInStock,
    public oWPrice?: number,
    public amountOriginal?: number,
    public amount?: number,
    public owAmount?: number,
    public materialGoods?: MaterialGoods,
    public materialGoodsCode?: string,
    public materialGoodsName?: string,
    public unitName?: string,
    public sAOrderDetailID?: string,
    public saOrder?: ISAOrder,
    public saOrderDetail?: ISAOrderDetails,
    public noBookSaOrder?: string,
    public noBookEmContract?: string,
    public costSetName?: string,
    public costSetCode?: string,
    public mainUnitID?: string,
    public costSet?: ICostSet,
    public costSets?: ICostSet[],
    public saInvoice?: ISAInvoice,
    public saInvoiceDetail?: ISAInvoiceDetails,
    public contractDetailID?: string,
    public viewEMContract?: any,
    public checkCoincide?: boolean,
    public rsProductionOrderQuantumns?: RSProductionOrderQuantumn[], // public checkSAOrder?: boolean,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
