import { Moment } from 'moment';
import { IFixedAssetCategory } from 'app/shared/model//fixed-asset-category.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IAccountList } from 'app/shared/model//account-list.model';
import { IWarranty } from 'app/shared/model/warranty.model';
import { IFixedAssetAllocation } from 'app/shared/model/fixed-asset-allocation.model';
import { IFixedAssetAccessories } from 'app/shared/model/fixed-asset-accessories.model';
import { IFixedAssetDetails } from 'app/shared/model/fixed-asset-details.model';

export interface IFixedAsset {
  id?: string;
  fixedAssetCode?: string;
  fixedAssetName?: string;
  description?: string;
  productionYear?: number;
  madeIn?: string;
  serialNumber?: string;
  accountingObjectName?: string;
  warranty?: string;
  guaranteeCodition?: string;
  isSecondHand?: boolean;
  currentState?: number;
  deliveryRecordNo?: string;
  deliveryRecordDate?: Moment | any;
  purchasedDate?: Moment | any;
  incrementDate?: Moment | any;
  depreciationDate?: Moment | any;
  usedDate?: Moment | any;
  purchasePrice?: number;
  originalPrice?: number;
  depreciationMethod?: number;
  usedTime?: number;
  displayMonthYear?: boolean;
  periodDepreciationAmount?: number;
  depreciationRate?: number;
  monthDepreciationRate?: number;
  monthPeriodDepreciationAmount?: number;
  isActive?: boolean;
  isStopDepreciation?: boolean;
  fixedAssetAllocation?: IFixedAssetAllocation[];
  fixedAssetAccessories?: IFixedAssetAccessories[];
  fixedAssetDetails?: IFixedAssetDetails[];
  fixedAssetCategoryID?: string;
  branchID?: IOrganizationUnit;
  organizationUnitID?: string;
  accountingObjectID?: string;
  depreciationAccount?: string;
  originalPriceAccount?: string;
  expenditureAccount?: string;
  fixedAssetCategoryName?: string;
  organizationUnitCode?: string;
  voucherRefCatalogDTOS?: VoucherRefCatalogDTO[];
  departmentID?: any;
  acDepreciationAmount?: number;
  checked?: any;
  acDepreciationAmountGiamGia?: number;
}

export class FixedAsset implements IFixedAsset {
  constructor(
    public id?: string,
    public fixedAssetCode?: string,
    public fixedAssetName?: string,
    public description?: string,
    public productionYear?: number,
    public madeIn?: string,
    public serialNumber?: string,
    public accountingObjectName?: string,
    public warranty?: string,
    public guaranteeCodition?: string,
    public isSecondHand?: boolean,
    public currentState?: number,
    public deliveryRecordNo?: string,
    public deliveryRecordDate?: Moment | any,
    public purchasedDate?: Moment | any,
    public incrementDate?: Moment | any,
    public depreciationDate?: Moment | any,
    public usedDate?: Moment | any,
    public purchasePrice?: number,
    public originalPrice?: number,
    public depreciationMethod?: number,
    public usedTime?: number,
    public displayMonthYear?: boolean,
    public periodDepreciationAmount?: number,
    public depreciationRate?: number,
    public monthDepreciationRate?: number,
    public monthPeriodDepreciationAmount?: number,
    public isActive?: boolean,
    isStopDepreciation?: boolean,
    public fixedAssetAllocation?: IFixedAssetAllocation[],
    public fixedAssetAccessories?: IFixedAssetAccessories[],
    public fixedAssetDetails?: IFixedAssetDetails[],
    public fixedAssetCategoryID?: string,
    public branchID?: IOrganizationUnit,
    public organizationUnitID?: string,
    public accountingObjectID?: string,
    public depreciationAccount?: string,
    public originalPriceAccount?: string,
    public expenditureAccount?: string,
    public departmentID?: any,
    public acDepreciationAmount?: number,
    public checked?: any,
    public acDepreciationAmountGiamGia?: number
  ) {
    this.isSecondHand = this.isSecondHand || false;
    this.displayMonthYear = this.displayMonthYear || false;
    // this.isActive = this.isActive || false;
  }
}

export class FixedAssetDTO {
  constructor(
    public id?: string,
    public fixedAssetCode?: string,
    public fixedAssetName?: string,
    public fixedAssetCategoryID?: IFixedAssetCategory,
    public originalPrice?: number,
    public organizationUnit?: IOrganizationUnit,
    public depreciationAccount?: string,
    public usedDate?: Moment | any,
    public depreciationDate?: Moment
  ) {}
}

export class VoucherRefCatalogDTO {
  constructor(
    public id?: string,
    public typeID?: number,
    public typeGroupID?: number,
    public typeName?: string,
    public date?: Moment | any,
    public noFBook?: string,
    public noMBook?: string,
    public reason?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number
  ) {}
}

export class PrepaidExpenseCodeDTO {
  constructor(
    public id?: string,
    public species?: string,
    public code?: string,
    public name?: string,
    public type?: number,
    public isActive?: boolean
  ) {}
}
