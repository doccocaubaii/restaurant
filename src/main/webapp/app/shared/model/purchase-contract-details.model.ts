import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { SAOrderDetails } from 'app/shared/model/sa-order-details.model';
import { SAOrder } from 'app/shared/model/sa-order.model';
import { Unit } from 'app/shared/model/unit.model';
import { IMaterialGoodsInStock } from 'app/shared/model/material-goods.model';

export interface IPurchaseContractDetails {
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
  sAOrderDetailID?: string;
}

export class PurchaseContractDetails implements IPurchaseContractDetails {
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
    public materialGood?: IMaterialGoodsInStock,
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
    public sAOrderDetailID?: string
  ) {}
}
