import { Moment } from 'moment';
import * as moment from 'moment';
import { IOrganizationUnitOptionReport } from 'app/shared/model/organization-unit-option-report.model';

export interface IOrganizationUnit {
  id?: string;
  branchID?: string;
  companyID?: string;
  accountingType?: number;
  organizationUnitCode?: string;
  organizationUnitName?: string;
  organizationUnitEName?: string;
  unitType?: number;
  getUnitName?: string;
  address?: string;
  taxCode?: string;
  phoneNumber?: string;
  businessRegistrationNumber?: string;
  issueDate?: Moment | any;
  issueBy?: string;
  accType?: number;
  isPrivateVAT?: boolean;
  financialYear?: number;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  startDate?: Moment | any;
  currencyID?: string;
  taxCalculationMethod?: number;
  goodsServicePurchaseID?: string;
  careerGroupID?: string;
  costAccount?: string;
  orderFixCode?: string;
  parentID?: string;
  getParentName?: string;
  isParentNode?: boolean;
  grade?: number;
  isActive?: boolean;
  organizationUnitOptionReport?: IOrganizationUnitOptionReport;
  userID?: number;
  packageID?: string;
  status?: number;
  isHaveOrg?: boolean;
  checked?: boolean;
  quantityRest?: number;
  quantity?: number;
}

export class OrganizationUnit implements IOrganizationUnit {
  constructor(
    public id?: string,
    public branchID?: string,
    public companyID?: string,
    public accountingType?: number,
    public organizationUnitCode?: string,
    public organizationUnitName?: string,
    public organizationUnitEName?: string,
    public unitType?: number,
    public getUnitName?: string,
    public address?: string,
    public taxCode?: string,
    public phoneNumber?: string,
    public businessRegistrationNumber?: string,
    public issueDate?: Moment | any,
    public issueBy?: string,
    public accType?: number,
    public isPrivateVAT?: boolean,
    public financialYear?: number,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public startDate?: Moment | any,
    public currencyID?: string,
    public taxCalculationMethod?: number,
    public goodsServicePurchaseID?: string,
    public careerGroupID?: string,
    public costAccount?: string,
    public orderFixCode?: string,
    public parentID?: string,
    public getParentName?: string,
    public isParentNode?: boolean,
    public grade?: number,
    public isActive?: boolean,
    public organizationUnitOptionReport?: IOrganizationUnitOptionReport,
    public userID?: number,
    public packageID?: string,
    public status?: number,
    public isHaveOrg?: boolean,
    public checked?: boolean,
    public quantityRest?: number,
    public quantity?: number
  ) {
    this.isPrivateVAT = this.isPrivateVAT || false;
    this.isParentNode = this.isParentNode || false;
    this.isActive = this.isActive || false;
    isHaveOrg = this.isHaveOrg || false;
    checked = this.checked || false;
  }
}
