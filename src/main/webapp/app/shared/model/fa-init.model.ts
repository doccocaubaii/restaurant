import { FAInitDetails } from 'app/shared/model/fa-init-details.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { IBudgetItem } from 'app/shared/model/budget-item.model';
import { ICostSet } from 'app/shared/model/cost-set.model';
import { EMContract } from 'app/shared/model/em-contract.model';
import { IStatisticsCode } from 'app/shared/model/statistics-code.model';
import { VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';

export interface IFAInit {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: any;
  postedDate?: any;
  typeLedger?: any;
  fixedAssetID?: any;
  departmentID?: any;
  incrementDate?: any;
  fixedAssetCategoryID?: string;
  depreciationDate?: any;
  usedTime?: any;
  usedTimeRemain?: any;
  monthUsedTime?: any;
  monthUsedTimeRemain?: any;
  originalPrice?: any;
  purchasePrice?: any;
  acDepreciationAmount?: any;
  remainingAmount?: any;
  monthPeriodDepreciationAmount?: any;
  originalPriceAccount?: string;
  expenditureAccount?: string;
  depreciationAccount?: string;
  budgetItemID?: string;
  costSetID?: string;
  fixedAssetName?: string;
  fixedAssetCode?: string;
  statisticsCodeID?: string;
  active?: boolean;
  faInitDetails?: FAInitDetails[];
  voucherRefCatalogDTOS?: VoucherRefCatalogDTO[];
  checked?: boolean;
  stopDepreciation?: boolean;
  depreciationRate?: any;
  monthDepreciationRate?: any;
}

export class FAInit implements IFAInit {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: any,
    public postedDate?: any,
    public typeLedger?: any,
    public fixedAssetID?: any,
    public department?: IOrganizationUnit,
    public expenseItem?: IExpenseItem,
    public budgetItem?: IBudgetItem,
    public costSet?: ICostSet,
    public departmentID?: string,
    public expenseItemID?: string,
    public contract?: EMContract,
    public statisticsCode?: IStatisticsCode,
    public incrementDate?: any,
    public fixedAssetCategoryID?: string,
    public depreciationDate?: any,
    public usedTime?: any,
    public usedTimeRemain?: any,
    public monthUsedTime?: any,
    public monthUsedTimeRemain?: any,
    public originalPrice?: any,
    public purchasePrice?: any,
    public acDepreciationAmount?: any,
    public remainingAmount?: any,
    public monthPeriodDepreciationAmount?: any,
    public originalPriceAccount?: string,
    public expenditureAccount?: string,
    public depreciationAccount?: string,
    public budgetItemID?: string,
    public costSetID?: string,
    public fixedAssetName?: string,
    public fixedAssetCode?: string,
    public statisticsCodeID?: string,
    public active?: boolean,
    public checked?: boolean,
    public stopDepreciation?: boolean,
    public faInitDetails?: FAInitDetails[],
    public voucherRefCatalogDTOS?: VoucherRefCatalogDTO[],
    public depreciationRate?: any,
    public monthDepreciationRate?: any
  ) {
    this.active = this.active || false;
    this.stopDepreciation = this.stopDepreciation || false;
    this.checked = this.checked || false;
  }
}
