import { Moment } from 'moment';
import { IMaterialGoods, IMaterialGoodsInStock, MaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model/unit.model';
import { IViewLotNo } from 'app/shared/model/view-lotno.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { ISaReturnDetails } from 'app/shared/model/sa-return-details.model';
import { ISaReturn } from 'app/shared/model/sa-return.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IPPDiscountReturn } from 'app/shared/model/pp-discount-return.model';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';

export interface IPpBillDetails {
  id?: string;
  saBillId?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  quantity?: number;
  unitPrice?: number;
  unitPriceOriginal?: number;
  mainQuantity?: number;
  mainUnitPrice?: number;
  mainConvertRate?: number;
  formula?: string;
  amount?: number;
  amountOriginal?: number;
  discountRate?: number;
  discountAmount?: number;
  discountAmountOriginal?: number;
  discountAccount?: string;
  vatRate?: number;
  vatAmount?: number;
  vatAmountOriginal?: number;
  lotNo?: string;
  ppInvoiceDetailID?: string;
  ppReturnDetailID?: string;
  ppDiscountReturnDetailID?: string;
  ppInvoiceID?: string;
  ppReturnID?: string;
  ppDiscountReturnID?: string;
  expiryDate?: Moment | any;
  isPromotion?: boolean;
  orderPriority?: number;
  materialGoodsCode?: string;
  unitPriceOriginals?: any[];
  unitID?: string;
  mainUnitID?: string;
  unit?: IUnit;
  unitName?: string;
  units?: IUnit[];
  mainUnit?: IUnit;
  mainUnitName?: string;
  mainUnits?: IUnit[];
  lotNos?: IViewLotNo[];
  saleDiscountPolicys?: ISaleDiscountPolicy[];
  careerGroupID?: string;
  pPInvoiceID?: string;
  pPInvoiceDetailID?: string;
  ppServiceDetailID?: string;
  vATDescription?: string;
  materialGoodsID?: string;
  goodsServicePurchaseId?: string;
  goodsServicePurchase?: any;
  goodsServicePurchaseCode?: string;
  stt?: number;
  sttCK?: number;
}

export class PpBillDetails implements IPpBillDetails {
  constructor(
    public id?: string,
    public ppBillId?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public quantity?: number,
    public unitPrice?: number,
    public unitPriceOriginal?: number,
    public mainQuantity?: number,
    public mainUnitPrice?: number,
    public mainConvertRate?: number,
    public formula?: string,
    public amount?: number,
    public amountOriginal?: number,
    public discountRate?: number,
    public discountAmount?: number,
    public discountAmountOriginal?: number,
    public discountAccount?: string,
    public vatRate?: number,
    public vatAmount?: number,
    public vatAmountOriginal?: number,
    public lotNo?: string,
    public expiryDate?: Moment | any,
    public isPromotion?: boolean,
    public orderPriority?: number,
    public ppInvoiceDetailID?: string,
    public ppReturnDetailID?: string,
    public ppDiscountReturnDetailID?: string,
    public ppInvoiceID?: string,
    public ppReturnID?: string,
    public ppDiscountReturnID?: string,
    public unitPriceOriginals?: any[],
    public unitID?: string,
    public mainUnitID?: string,
    public unit?: IUnit,
    public mainUnit?: IUnit,
    public units?: IUnit[],
    public mainUnits?: IUnit[],
    public lotNos?: IViewLotNo[],
    public saleDiscountPolicys?: ISaleDiscountPolicy[],
    public careerGroupID?: string,
    public convertRates?: any,
    public unitPrices?: any[],
    public pPInvoiceID?: string,
    public pPInvoiceDetailID?: string,
    public ppServiceID?: string,
    public ppServiceDetailID?: string,
    public vATDescription?: string,
    public materialGoodsID?: string,
    public saReturnID?: string,
    public saReturn?: ISaReturn,
    public saReturnDetails?: ISaReturnDetails,
    public saReturnDetailID?: string,
    public repository?: IRepository,
    public materialGood?: IMaterialGoodsInStock,
    public ppDiscountReturn?: IPPDiscountReturn,
    public ppDiscountReturnDetail?: IPPDiscountReturnDetails,
    public repositoryID?: string,
    public goodsServicePurchaseId?: string,
    public goodsServicePurchase?: any,
    public stt?: number,
    public sttCK?: number
  ) {
    this.isPromotion = this.isPromotion || false;
  }
}

export class PPBillDetailEasyInDTO {
  constructor(
    public id?: string,
    public ppBillID?: string,
    public materialGoodsID?: string,
    public materialGoodsCode?: string,
    public materialGoodsName?: string,
    public materialGoodsNameEB?: string,
    public materialGoodsType?: number,
    public units?: IUnit[],
    public unitID?: string,
    public mainUnitID?: string,
    public unitName?: string,
    public quantity?: number,
    public unitPriceOriginal?: number,
    public unitPrice?: number,
    public amountOriginal?: number,
    public amount?: number,
    public vatRate?: number,
    public vatAmountOriginal?: number,
    public vatAmount?: number,
    public discountAmountOriginal?: number,
    public discountAmount?: number,
    public orderPriority?: number
  ) {}
}
