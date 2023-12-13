import { Moment } from 'moment';
import { IPaymentClause } from 'app/shared/model//payment-clause.model';
import { IAccountingObjectGroup } from 'app/shared/model//accounting-object-group.model';
import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';
import { IAccountingObjectDependent } from 'app/shared/model/accounting-object-dependent.model';

export interface IAccountingObject {
  isForeignCurrency?: boolean;
  id?: string;
  branchId?: string;
  companyId?: string;
  typeID?: number;
  date?: Moment | any;
  noFBook?: string;
  noMBook?: string;
  totalAmount?: number;
  accountingObjectCode?: string;
  accountingObjectName?: string;
  employeeBirthday?: Moment | any;
  accountingObjectAddress?: string;
  accountObjectGroupID?: string;
  tel?: string;
  fax?: string;
  email?: string;
  website?: string;
  bankName?: string;
  taxCode?: string;
  description?: string;
  contactName?: string;
  contactTitle?: string;
  contactSex?: number;
  contactMobile?: string;
  contactEmail?: string;
  contactHomeTel?: string;
  contactOfficeTel?: string;
  contactAddress?: string;
  scaleType?: number;
  objectType?: number;
  isEmployee?: boolean;
  identificationNo?: string;
  issueDate?: Moment | any;
  issueBy?: string;
  numberOfDependent?: number;
  agreementSalary?: number;
  insuranceSalary?: number;
  salarycoEfficient?: number;
  isUnOfficialStaff?: boolean;
  maximizaDebtAmount?: number;
  dueTime?: number;
  isActive?: boolean;
  paymentClause?: IPaymentClause;
  accountingObjectGroup?: IAccountingObjectGroup;
  departmentId?: string;
  accountingObjectBankAccounts?: IAccountingObjectBankAccount[];
  accountingObjectDependents?: IAccountingObjectDependent[];
  accountingObjectDependentsPro?: IAccountingObjectDependent[];
  voucherRefCatalogDTOS?: VoucherRefCatalogDTO[];
  checked?: boolean;
  contractType?: number;
  organizationUnitCode?: string;
  accountingObjectGroupName?: string;
  statusTaxCode?: number;
  timeTaxCode?: Date;
}

export class AccountingObject implements IAccountingObject {
  constructor(
    public id?: string,
    public branchId?: string,
    public companyId?: string,
    public accountingObjectCode?: string,
    public accountingObjectName?: string,
    public employeeBirthday?: Moment | any,
    public accountingObjectAddress?: string,
    public accountObjectGroupID?: string,
    public tel?: string,
    public fax?: string,
    public email?: string,
    public website?: string,
    public bankName?: string,
    public taxCode?: string,
    public description?: string,
    public contactName?: string,
    public contactTitle?: string,
    public contactSex?: number,
    public contactMobile?: string,
    public contactEmail?: string,
    public contactHomeTel?: string,
    public contactOfficeTel?: string,
    public contactAddress?: string,
    public scaleType?: number,
    public objectType?: number,
    public isEmployee?: boolean,
    public identificationNo?: string,
    public issueDate?: Moment | any,
    public issueBy?: string,
    public numberOfDependent?: number,
    public agreementSalary?: number,
    public insuranceSalary?: number,
    public salarycoEfficient?: number,
    public isUnofficialStaff?: boolean,
    public maximizaDebtAmount?: number,
    public dueTime?: number,
    public isActive?: boolean,
    public paymentClause?: IPaymentClause,
    public accountingObjectGroup?: IAccountingObjectGroup,
    public departmentId?: string,
    public accountingObjectBankAccounts?: IAccountingObjectBankAccount[],
    public checked?: boolean,
    public contractType?: number
  ) {
    this.isEmployee = this.isEmployee || false;
    this.isUnofficialStaff = this.isUnofficialStaff || false;
    this.isActive = this.isActive || false;
    this.checked = this.checked || false;
  }
}

export class AccountingObjectDTO {
  constructor(
    public id?: string,
    public accountingObjectCode?: string,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public taxCode?: string,
    public contactName?: string
  ) {}
}

export class AccountingObjectSaveDTO {
  constructor(public accountingObject?: IAccountingObject, public position?: any, public status?: any) {}
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
    public totalAmountOriginal?: number,
    public ppOrderId?: number
  ) {}
}
