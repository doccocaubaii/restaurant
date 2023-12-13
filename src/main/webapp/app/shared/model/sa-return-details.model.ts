import { Moment } from 'moment';
import { IMaterialGoods } from 'app/shared/model//material-goods.model';
import { IUnit } from 'app/shared/model//unit.model';
import { IRepository } from 'app/shared/model//repository.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { EMContract, IEMContract } from 'app/shared/model/em-contract.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IOrganizationUnit, OrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { ISaleDiscountPolicy } from 'app/shared/model/sale-discount-policy.model';
// import { ISaInvoice } from 'app/shared/model/sa-invoice.model';
// import { ISaInvoiceDetails } from 'app/shared/model/sa-invoice-details.model';

export interface ISaReturnDetails {
  id?: string;
  saReturnID?: string;
  isPromotion?: boolean;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  quantity?: number;
  unitPrice?: number;
  unitPriceTax?: number;
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
  vatAccount?: string;
  deductionDebitAccount?: string;
  owPrice?: number;
  owAmount?: number;
  costAccount?: string;
  repositoryAccount?: string;
  expiryDate?: Moment | any;
  lotNo?: string;
  departmentID?: string;
  expenseItemID?: string;
  budgetItemID?: string;
  costSetID?: string;
  contractID?: string;
  statisticsCodeID?: string;
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
  vatDescription?: string;
  materialGoods?: IMaterialGoods;
  materialGoodsID?: string;
  unit?: IUnit;
  unitID?: string;
  repository?: IRepository;
  repositoryID?: string;
  mainUnit?: IUnit;
  mainUnitID?: string;
  mainUnitName?: string;
  accountingObjectID?: string;
  units?: any;
  organizationUnitID?: string;
  saBillDetailID?: string;
  saBillID?: string;
  saInvoiceID?: string;
  date?: any;
  saInvoiceDetailID?: string;
  totalQuantity?: number;
  isLotNoReadOnly?: boolean;
  lotNoObject?: any;
  saleDiscountPolicys?: ISaleDiscountPolicy[];
  careerGroupID?: string;
  noFBook?: string;
  preOrder?: number;
  materialGoodsSpecificationsLedgers?: any[];
  materialGoodsCode?: string;
  repositoryCode?: string;
  accountingObjectCode?: string;
  unitName?: string;
  expenseItemCode?: string;
  costSetCode?: string;
  contractCode?: string;
  budgetItemCode?: string;
  organizationUnitCode?: string;
  statisticsCode?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  ppBillID?: string;
  ppBillDetailID?: string;
  maxQuantity?: any;
  notInVATDeclaration?: any;
  isUnreasonableCost?: boolean;
}

export class SaReturnDetails implements ISaReturnDetails {
  constructor(
    public id?: string,
    public saReturnID?: string,
    public isPromotion?: boolean,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public quantity?: number,
    public unitPrice?: number,
    public unitPriceTax?: number,
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
    public owPrice?: number,
    public owAmount?: number,
    public costAccount?: string,
    public repositoryAccount?: string,
    public expiryDate?: Moment | any,
    public lotNo?: string,
    public departmentID?: string,
    public expenseItemID?: string,
    public budgetItemID?: string,
    public costSetID?: string,
    public contractID?: string,
    public statisticsCodeID?: string,
    public organizationUnitID?: string,
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
    public vatDescription?: string,
    public materialGoods?: IMaterialGoods,
    public materialGoodsID?: string,
    public unit?: IUnit,
    public unitID?: string,
    public repository?: IRepository,
    public repositoryID?: string,
    public mainUnit?: IUnit,
    public mainUnitID?: string,
    public mainUnitName?: string,
    public accountingObjectID?: string,
    public units?: any,
    public vatAccount?: string,
    public saBillDetailID?: string,
    public saBillID?: string,
    public saInvoiceDetailID?: string,
    public saInvoiceID?: string,
    public date?: any,
    public totalQuantity?: number,
    public lotNoObject?: any,
    public isLotNoReadOnly?: boolean,
    public deductionDebitAccount?: string,
    public saleDiscountPolicys?: ISaleDiscountPolicy[],
    public careerGroupID?: string,
    public noFBook?: string,
    public preOrder?: number,
    public materialGoodsSpecificationsLedgers?: any[],
    public materialGoodsCode?: string,
    public repositoryCode?: string,
    public accountingObjectCode?: string,
    public unitName?: string,
    public expenseItemCode?: string,
    public costSetCode?: string,
    public contractCode?: string,
    public budgetItemCode?: string,
    public organizationUnitCode?: string,
    public statisticsCode?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public ppBillID?: string,
    public ppBillDetailID?: string,
    public maxQuantity?: any,
    public notInVATDeclaration?: any,
    public isUnreasonableCost?: boolean
  ) {
    this.isPromotion = this.isPromotion || false;
  }
}
