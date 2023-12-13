import { IOrganizationUnit } from 'app/shared/model//organization-unit.model';

export interface IMCPaymentDetailSalary {
  id?: string;
  mCPaymentID?: string;
  employeeID?: string;
  departmentID?: string;
  description?: string;
  accumAmount?: number;
  accumAmountOriginal?: number;
  currentMonthAmount?: number;
  currentMonthAmountOriginal?: number;
  payAmount?: number;
  payAmountOld?: number;
  payAmountOriginal?: number;
  orderPriority?: number;
}

export class MCPaymentDetailSalary implements IMCPaymentDetailSalary {
  constructor(
    public id?: string,
    public mCPaymentID?: string,
    public employeeID?: string,
    public departmentID?: string,
    public description?: string,
    public accumAmount?: number,
    public accumAmountOriginal?: number,
    public currentMonthAmount?: number,
    public currentMonthAmountOriginal?: number,
    public payAmount?: number,
    public payAmountOld?: number,
    public payAmountOriginal?: number,
    public orderPriority?: number
  ) {}
}
