import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { IRepository } from 'app/shared/model/repository.model';
import { IAccountList } from 'app/shared/model/account-list.model';

export interface IPPInvoiceDetails {
  id?: string;
  ppInvoiceId?: string;
  materialGoodsId?: string;
  materialGood?: any;
  repositoryId?: string;
  repository?: any;
  description?: string;
  descriptionHDDV?: string;
  debitAccount?: string;
  creditAccount?: string;
  unitId?: string;
  unitName?: string;
  quantity?: number;
  unitPrice?: number;
  unitPriceOriginal?: number;
  amount?: number;
  amountOriginal?: number;
  discountRate?: number;
  discountAmount?: number;
  discountAmountOriginal?: number;
  inwardAmount?: number;
  inwardAmountOriginal?: number;
  freightAmount?: number;
  freightAmountOriginal?: number;
  importTaxExpenseAmount?: number;
  importTaxExpenseAmountOriginal?: number;
  expiryDate?: Moment | any;
  lotNo?: string;
  customUnitPrice?: number;
  vatRate?: number;
  vatAmount?: number;
  vatAmountOriginal?: number;
  vatAccount?: string;
  deductionDebitAccount?: string;
  mainUnitId?: string;
  mainQuantity?: number;
  mainUnitPrice?: number;
  mainConvertRate?: number;
  formula?: string;
  importTaxRate?: number;
  importTaxAmount?: number;
  importTaxAmountOriginal?: number;
  importTaxAccount?: string;
  specialConsumeTaxRate?: number;
  specialConsumeTaxAmount?: number;
  specialConsumeTaxAmountOriginal?: number;
  specialConsumeTaxAccount?: string;
  invoiceType?: string;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  goodsServicePurchaseId?: string;
  accountingObjectId?: string;
  budgetItemId?: string;
  costSetId?: string;
  contractId?: string;
  statisticsCodeId?: string;
  departmentId?: string;
  expenseItemId?: string;
  ppOrderId?: string;
  ppOrderDetailId?: string;
  cashOutExchangeRateFB?: number;
  cashOutAmountFB?: number;
  cashOutDifferAmountFB?: number;
  cashOutDifferAccountFB?: string;
  cashOutExchangeRateMB?: number;
  cashOutAmountMB?: number;
  cashOutDifferAmountMB?: number;
  cashOutDifferAccountMB?: string;
  cashOutVATAmountFB?: number;
  cashOutDifferVATAmountFB?: number;
  cashOutVATAmountMB?: number;
  cashOutDifferVATAmountMB?: number;
  orderPriority?: number;
  isSelected?: boolean;
  unit?: any;
  goodsServicePurchase?: any;
  invoiceTemplate?: string;
  vatDescription?: string;
  expenseItem?: any;
  costSetItem?: any;
  emContractItem?: any;
  budgetItem?: any;
  organizationUnitItem?: any;
  statisticsCodeItem?: any;
  units?: any;
  mainUnitName?: string;
  unitPrices?: any[];
  ppOrderNo?: any;
  materialGoodsCode?: string;
  quantityFromDB?: number;
  newPriority?: number;
  materialGoodsSpecificationsLedgers?: any[];
  emContract?: any;
  contractID?: string;
  isFeightService?: boolean;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  isUnreasonableCost?: boolean;
  isCheckHHDV?: boolean;
  stt?: number;
  materialGoodsType?: number;
  fillDataEdit?: any;
  unitNameEdit?: string;
}

export class PPInvoiceDetails implements IPPInvoiceDetails {
  constructor(
    public id?: string,
    public ppInvoiceId?: string,
    public materialGoodsId?: string,
    public materialGood?: any,
    public repositoryId?: string,
    public repository?: any,
    public description?: string,
    public descriptionHDDV?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public unitId?: string,
    public unitName?: string,
    public quantity?: number,
    public unitPrice?: number,
    public unitPriceOriginal?: number,
    public amount?: number,
    public amountOriginal?: number,
    public discountRate?: number,
    public discountAmount?: number,
    public discountAmountOriginal?: number,
    public inwardAmount?: number,
    public inwardAmountOriginal?: number,
    public freightAmount?: number,
    public freightAmountOriginal?: number,
    public importTaxExpenseAmount?: number,
    public importTaxExpenseAmountOriginal?: number,
    public expiryDate?: Moment | any,
    public lotNo?: string,
    public customUnitPrice?: number,
    public vatRate?: number,
    public vatAmount?: number,
    public vatAmountOriginal?: number,
    public vatAccount?: string,
    public deductionDebitAccount?: string,
    public mainUnitId?: string,
    public mainQuantity?: number,
    public mainUnitPrice?: number,
    public mainConvertRate?: number,
    public formula?: string,
    public importTaxRate?: number,
    public importTaxAmount?: number,
    public importTaxAmountOriginal?: number,
    public importTaxAccount?: string,
    public specialConsumeTaxRate?: number,
    public specialConsumeTaxAmount?: number,
    public specialConsumeTaxAmountOriginal?: number,
    public specialConsumeTaxAccount?: string,
    public invoiceType?: string,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public goodsServicePurchaseId?: string,
    public accountingObjectId?: string,
    public budgetItemId?: string,
    public costSetId?: string,
    public contractId?: string,
    public statisticsCodeId?: string,
    public departmentId?: string,
    public expenseItemId?: string,
    public ppOrderId?: string,
    public ppOrderDetailId?: string,
    public cashOutExchangeRateFB?: number,
    public cashOutAmountFB?: number,
    public cashOutDifferAmountFB?: number,
    public cashOutDifferAccountFB?: string,
    public cashOutExchangeRateMB?: number,
    public cashOutAmountMB?: number,
    public cashOutDifferAmountMB?: number,
    public cashOutDifferAccountMB?: string,
    public cashOutVATAmountFB?: number,
    public cashOutDifferVATAmountFB?: number,
    public cashOutVATAmountMB?: number,
    public cashOutDifferVATAmountMB?: number,
    public orderPriority?: number,
    public isSelected?: boolean,
    public unit?: any,
    public goodsServicePurchase?: any,
    public invoiceTemplate?: string,
    public vatDescription?: string,
    public expenseItem?: any,
    public costSetItem?: any,
    public emContractItem?: any,
    public budgetItem?: any,
    public organizationUnitItem?: any,
    public statisticsCodeItem?: any,
    public units?: any,
    public mainUnitName?: string,
    public unitPrices?: any[],
    public ppOrderNo?: any,
    public materialGoodsCode?: string,
    public quantityFromDB?: number,
    public newPriority?: number,
    public materialGoodsSpecificationsLedgers?: any[],
    public emContract?: any,
    public contractID?: string,
    public isFeightService?: boolean,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public isUnreasonableCost?: boolean,
    public isCheckHHDV?: boolean,
    public stt?: number,
    public materialGoodsType?: number,
    public fillDataEdit?: any,
    public unitNameEdit?: string
  ) {
    this.isSelected = this.isSelected || false;
    this.isCheckHHDV = this.isCheckHHDV || false;
  }
}
