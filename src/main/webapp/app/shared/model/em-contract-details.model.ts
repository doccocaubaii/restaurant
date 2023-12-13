import { IMaterialGoods, IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';
import { IUnit, Unit } from 'app/shared/model/unit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { SAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { IPporder } from 'app/shared/model/pporder.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';

export interface IEMContractDetails {
  id?: number;
  contractID?: string;
  materialGoodsID?: string;
  materialGoodsCode?: string;
  description?: string;
  unitID?: string;
  unitName?: string;
  quantity?: number;
  quantityDelivery?: number;
  unitPrice?: number;
  unitPriceOriginal?: number;
  mainUnitID?: string;
  mainQuantity?: number;
  formula?: string;
  amount?: number;
  amountOriginal?: number;
  amountReceipt?: number;
  amountReceiptOriginal?: number;
  vatAmount?: number;
  vatAmountOriginal?: number;
  vatRate?: number;
  discountRate?: number;
  discountAmount?: number;
  discountAmountOriginal?: number;
  totalAmount?: number;
  totalAmountOriginal?: number;
  orderPriority?: number;
  mainQuantityDelivery?: number;
  mainUnitPrices?: number;
  mainConvertRates?: number;
  accountingObject?: IAccountingObject;
  saOrderDetail?: SAOrderDetails;
  saOrder?: SAOrder;
  noBookSaOrder?: string;
  bookPPOrder?: string;
  sAOrderID?: string;
  sAOrderDetailID?: string;
  pPOrderID?: string;
  ppOrderDetailId?: string;
  saleDiscountPolicys?: ISaleDiscountPolicy[];
  unitPriceOriginals?: any[];
  materialGoodsSpecificationsLedgers?: any[];
  no?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class EMContractDetails implements IEMContractDetails {
  constructor(
    public id?: number,
    public contractID?: string,
    public materialGoodsID?: string,
    public materialGoodsCode?: string,
    public description?: string,
    public unitID?: string,
    public unit?: Unit,
    public unitName?: string,
    public convertRates?: any,
    public mainUnit?: Unit,
    public quantity?: number,
    public quantityDelivery?: number,
    public unitPrice?: number,
    public unitPrices?: any[],
    public unitPriceOriginal?: number,
    public mainUnitID?: string,
    public mainQuantity?: number,
    public formula?: string,
    public amount?: number,
    public amountOriginal?: number,
    public materialGood?: IMaterialGoods,
    public vatAmount?: number,
    public vatAmountOriginal?: number,
    public vatRate?: number,
    public discountRate?: number,
    public discountAmount?: number,
    public discountAmountOriginal?: number,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public orderPriority?: number,
    public mainQuantityDelivery?: number,
    public mainUnitPrice?: number,
    public mainConvertRate?: number,
    public saOrderDetail?: SAOrderDetails,
    public saOrder?: SAOrder,
    public noBookSaOrder?: string,
    public sAOrderID?: string,
    public sAOrderDetailID?: string,
    public ppOrderDetailId?: string,
    public pPOrderID?: string,
    public bookPPOrder?: string,
    public saleDiscountPolicys?: ISaleDiscountPolicy[],
    public units?: IUnit[],
    public unitPriceOriginals?: any[],
    public materialGoodsSpecificationsLedgers?: any[],
    public no?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
