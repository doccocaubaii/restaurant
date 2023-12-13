import { Moment } from 'moment';
import { IEMContract } from 'app/shared/model/em-contract.model';
import { SaBill } from 'app/shared/model/sa-bill.model';
import { IAccountList } from 'app/shared/model/account-list.model';

export interface IPPDiscountReturnDetails {
  id?: string;
  ppDiscountReturnID?: string;
  ppInvoiceID?: string;
  ppInvoiceDetailID?: string;
  repositoryID?: string;
  materialGoodsID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  unitID?: string;
  quantity?: number;
  unitPrice?: number;
  unitPriceTax?: number;
  unitPriceOriginal?: number;
  amount?: number;
  amountOriginal?: number;
  mainUnitID?: string;
  mainQuantity?: number;
  mainUnitPrice?: number;
  mainConvertRate?: number;
  formula?: string;
  expiryDate?: any;
  lotNo?: string;
  isPromotion?: boolean;
  goodsServicePurchaseID?: string;
  vatRate?: number;
  vatAmount?: number;
  vatAmountOriginal?: number;
  vatAccount?: string;
  accountingObjectID?: string;
  saBillID?: string;
  saBillDetailID?: string;
  costSetID?: string;
  contractID?: string;
  statisticsCodeID?: string;
  departmentID?: string;
  expenseItemID?: string;
  budgetItemID?: string;
  isMatch?: boolean;
  matchDate?: Moment | any;
  orderPriority?: number;
  materialGoods?: any;
  unit?: any;
  deductionDebitAccount?: string;
  goodsServicePurchase?: any;
  expenseItem?: any;
  costSet?: any;
  emContract?: any;
  budgetItem?: any;
  organizationUnit?: any;
  statisticsCode?: any;
  repository?: any;
  vatAccountItem?: any;
  mainUnitName?: any;
  mainUnit?: any;
  saBill?: SaBill;
  units?: any;
  vatDescription?: any;
  contractNoMBook?: any;
  contractNoFBook?: any;
  ppInvoiceDateVoucher?: Moment | any;
  ppInvoiceNoBook?: string;
  ppInVoiceDate?: any;
  ppInVoiceNoBook?: any;
  ppInvoiceQuantity?: number;
  expenseAccount?: string;
  materialGoodsCode?: string;
  expenseItemCode?: string;
  costSetCode?: any;
  budgetItemCode?: any;
  departmentCode?: any;
  goodsServicePurchaseCode?: any;
  repositoryCode?: any;
  accountingObjectCode?: any;
  unitCode?: any;
  materialGoodsSpecificationsLedgers?: any[];
  unitName?: any;
  contractNo?: any;
  statisticCodeID?: any;
  departmentId?: string;
  ppBillID?: string;
  ppBillDetailID?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  maxQuantity?: any;
  isUnreasonableCost?: boolean;
  fillDataEditDefault?: any;
  fillDataEdit?: any;
  isCheckHHDV?: boolean;
}

export class PPDiscountReturnDetails implements IPPDiscountReturnDetails {
  constructor(
    public id?: string,
    public ppDiscountReturnID?: string,
    public ppInvoiceID?: string,
    public ppInvoiceDetailID?: string,
    public repositoryID?: string,
    public materialGoodsID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public unitID?: string,
    public quantity?: number,
    public unitPrice?: number,
    public unitPriceTax?: number,
    public unitPriceOriginal?: number,
    public amount?: number,
    public amountOriginal?: number,
    public mainUnitID?: string,
    public mainQuantity?: number,
    public mainUnitPrice?: number,
    public mainConvertRate?: number,
    public formula?: string,
    public expiryDate?: any,
    public lotNo?: string,
    public isPromotion?: boolean,
    public goodsServicePurchaseID?: string,
    public vatRate?: number,
    public vatAmount?: number,
    public vatAmountOriginal?: number,
    public vatAccount?: string,
    public accountingObjectID?: string,
    public saBillID?: string,
    public saBillDetailID?: string,
    public costSetID?: string,
    public contractID?: string,
    public statisticsCodeID?: string,
    public departmentId?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public budgetItemID?: string,
    public materialGoodsCode?: string,
    public isMatch?: boolean,
    public matchDate?: Moment | any,
    public orderPriority?: number,
    public materialGoods?: any,
    public unit?: any,
    public deductionDebitAccount?: string,
    public goodsServicePurchase?: any,
    public expenseItem?: any,
    public costSet?: any,
    public emContract?: any,
    public budgetItem?: any,
    public organizationUnit?: any,
    public statisticsCode?: any,
    public vatAccountItem?: any,
    public mainUnitName?: any,
    public mainUnit?: any,
    public repository?: any,
    public saBill?: SaBill,
    public units?: any,
    public vatDescription?: any,
    public contractNoMBook?: any,
    public contractNoFBook?: any,
    public ppInvoiceDateVoucher?: Moment | any,
    public ppInvoiceNoBook?: string,
    public ppInVoiceDate?: any,
    public ppInVoiceNoBook?: any,
    public ppInvoiceQuantity?: number,
    public expenseAccount?: string,
    public expenseItemCode?: string,
    public costSetCode?: any,
    public budgetItemCode?: any,
    public departmentCode?: any,
    public goodsServicePurchaseCode?: any,
    public repositoryCode?: any,
    public accountingObjectCode?: any,
    public unitCode?: any,
    public materialGoodsSpecificationsLedgers?: any[],
    public unitName?: any,
    public contractNo?: any,
    public statisticCodeID?: any,
    public ppBillID?: string,
    public ppBillDetailID?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public maxQuantity?: any,
    public isUnreasonableCost?: boolean,
    public fillDataEditDefault?: any,
    public fillDataEdit?: any,
    public isCheckHHDV?: boolean
  ) {
    this.isPromotion = this.isPromotion || false;
    this.isMatch = this.isMatch || false;
  }
}
