import { IMaterialGoodsAssembly } from 'app/shared/model/material-goods-assembly.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
import { IMaterialGoodsPurchasePrice } from 'app/shared/model/material-goods-purchase-price.model';
import { IMaterialGoodsSpecifications } from 'app/shared/model/material-goods-specifications.model';
import { IMaterialGoodsConvertUnit } from 'app/shared/model/material-goods-convert-unit.model';
import { VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';
import { Currency } from 'app/shared/model/currency.model';

export interface IMaterialGoods {
  id?: string;
  branchID?: string;
  materialGoodsCategoryID?: string;
  materialGoodsCode?: string;
  materialGoodsName?: string;
  materialGoodsType?: number;
  unitID?: string;
  unitName?: string;
  unitPrice?: number;
  minimumStock?: number;
  itemSource?: string;
  warranty?: string;
  repositoryID?: string;
  repositoryName?: string;
  reponsitoryAccount?: string;
  expenseAccount?: string;
  revenueAccount?: string;
  materialGoodsGSTID?: string;
  careerGroupID?: string;
  vatTaxRate?: number;
  importTaxRate?: number;
  exportTaxRate?: number;
  saleDiscountRate?: number;
  purchaseDiscountRate?: number;
  fixedSalePrice?: number;
  salePrice1?: number;
  salePrice2?: number;
  salePrice3?: number;
  isActive?: boolean;
  isSecurity?: boolean;
  isFollow?: boolean;
  lotNos?: any[];
  checked?: boolean;
  materialGoodsAssembly?: IMaterialGoodsAssembly[];
  materialGoodsPurchasePrice?: IMaterialGoodsPurchasePrice[];
  materialGoodsSpecifications?: IMaterialGoodsSpecifications[];
  saleDiscountPolicy?: ISaleDiscountPolicy[];
  materialGoodsConvertUnits?: IMaterialGoodsConvertUnit[];
  voucherRefCatalogDTOS?: VoucherRefCatalogDTO[];
  description?: string;
  currencyID?: string;
  isChange?: boolean;
  mainUnit?: boolean;
  groupForMerge?: string;
  convertRate?: number;
  formula?: string;
}
export interface IMaterialGoodsInStock {
  id?: string;
  materialGoodsCode?: string;
  materialGoodsName?: string;
  materialGoodsInStock?: number;
  repositoryID?: string;
  unitID?: string;
  reponsitoryAccount?: string;
  vatTaxRate?: number;
  purchaseDiscountRate?: number;
  fixedSalePrice?: number;
  salePrice1?: number;
  salePrice2?: number;
  salePrice3?: number;
  description?: string;
  isFollow?: boolean;
  amountMaterialGoodsInStock?: any;
  unitMaterialGoodsInStock?: any;
}

export class MaterialGoodsOfTinhGiaBanDTO {
  constructor(
    public id?: string,
    public fixedSalePrice?: number,
    public salePrice1?: number,
    public salePrice2?: number,
    public salePrice3?: number,
    public orderNumber?: number
  ) {}
}

export class MaterialGoods implements IMaterialGoods {
  constructor(
    public id?: string,
    public branchID?: string,
    public materialGoodsCategoryID?: string,
    public materialGoodsCode?: string,
    public materialGoodsName?: string,
    public materialGoodsType?: number,
    public unitID?: string,
    public unitName?: string,
    public unitPrice?: number,
    public minimumStock?: number,
    public itemSource?: string,
    public warranty?: string,
    public repositoryID?: string,
    public reponsitoryAccount?: string,
    public expenseAccount?: string,
    public revenueAccount?: string,
    public materialGoodsGSTID?: string,
    public careerGroupID?: string,
    public vatTaxRate?: number,
    public importTaxRate?: number,
    public exportTaxRate?: number,
    public saleDiscountRate?: number,
    public purchaseDiscountRate?: number,
    public fixedSalePrice?: number,
    public salePrice1?: number,
    public salePrice2?: number,
    public salePrice3?: number,
    public isActive?: boolean,
    public isSecurity?: boolean,
    public isFollow?: boolean,
    public lotNos?: any[],
    public checked?: boolean,
    public currencyId?: string,
    public exchangeRate?: number,
    public currency?: Currency,
    public materialGoodsAssembly?: IMaterialGoodsAssembly[],
    public materialGoodsPurchasePrice?: IMaterialGoodsPurchasePrice[],
    public materialGoodsSpecifications?: IMaterialGoodsSpecifications[],
    public saleDiscountPolicy?: ISaleDiscountPolicy[],
    public materialGoodsConvertUnits?: IMaterialGoodsConvertUnit[],
    public voucherRefCatalogDTOS?: VoucherRefCatalogDTO[],
    public description?: string,
    public isChange?: boolean,
    public mainUnit?: boolean,
    public convertRate?: number,
    public formula?: string
  ) {
    this.isActive = this.isActive || false;
    this.isSecurity = this.isSecurity || false;
    this.checked = this.checked || false;
    this.isChange = this.isChange || false;
  }
}

export interface IMaterialGoodsReportBCTDSX {
  id?: string;
  isFollow?: boolean;
  description?: string;
  currencyID?: string;
  isChange?: boolean;
  checked?: boolean;
  date?: string;
  no?: string;
}
