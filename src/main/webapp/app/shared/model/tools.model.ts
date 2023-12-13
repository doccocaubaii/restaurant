import { Moment } from 'moment';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IUnit } from 'app/shared/model//unit.model';
import { VoucherRefCatalogDTO } from 'app/shared/model/accounting-object.model';
import { IMCReceiptDetails } from 'app/shared/model/mc-receipt-details.model';
import { IToolsDetails } from 'app/shared/model/tools-details.model';

export interface ITools {
  active?: any;
  checkCategory?: boolean;
  id?: string;
  declareType?: number;
  postedDate?: Moment | any;
  branchID?: string;
  toolCode?: string;
  toolName?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  allocationTimes?: number;
  remainAllocationTimes?: number;
  allocatedAmount?: number;
  allocationAmount?: number;
  remainAmount?: number;
  allocationAwaitAccount?: string;
  isActive?: boolean;
  typeLedger?: number;
  organizationUnit?: IOrganizationUnit;
  voucherRefCatalogDTOS?: VoucherRefCatalogDTO[];
  toolsDetails?: IToolsDetails[];
  unit?: IUnit;
  checked?: boolean;
  unitID?: any;
  isStopAllocating?: boolean;
  companyID?: any;
}

export class Tools implements ITools {
  constructor(
    public id?: string,
    public declareType?: number,
    public postedDate?: Moment | any,
    public branchID?: string,
    public toolCode?: string,
    public toolName?: string,
    public quantity?: number,
    public unitPrice?: number,
    public amount?: number,
    public allocationTimes?: number,
    public remainAllocationTimes?: number,
    public allocatedAmount?: number,
    public allocationAmount?: number,
    public remainAmount?: number,
    public allocationAwaitAccount?: string,
    public isActive?: boolean,
    public typeLedger?: number,
    public organizationUnit?: IOrganizationUnit,
    public voucherRefCatalogDTOS?: VoucherRefCatalogDTO[],
    public toolsDetails?: IToolsDetails[],
    public unit?: IUnit,
    public checkCategory?: boolean,
    public checked?: boolean,
    public unitID?: any,
    public isStopAllocating?: boolean,
    public active?: any,
    public companyID?: any
  ) {
    this.isActive = this.isActive || false;
    this.checked = this.checked || false;
  }
}
