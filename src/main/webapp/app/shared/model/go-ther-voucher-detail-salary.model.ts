import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface IGOtherVoucherDetailSalary {
  id?: string;
  gOtherVoucherID?: string;
  employeeID?: string;
  departmentID?: string;
  description?: string;
  accumAmount?: number;
  accumAmountOriginal?: number;
  currentMonthAmount?: number;
  currentMonthAmountOriginal?: number;
  payAmount?: number;
  payAmountOriginal?: number;
  orderPriority?: number;
}

export class GOtherVoucherDetailSalary implements IGOtherVoucherDetailSalary {
  constructor(
    public id?: string,
    public gOtherVoucherID?: string,
    public employeeID?: string,
    public departmentID?: string,
    public description?: string,
    public accumAmount?: number,
    public accumAmountOriginal?: number,
    public currentMonthAmount?: number,
    public currentMonthAmountOriginal?: number,
    public payAmount?: number,
    public payAmountOriginal?: number,
    public orderPriority?: number
  ) {}
}
