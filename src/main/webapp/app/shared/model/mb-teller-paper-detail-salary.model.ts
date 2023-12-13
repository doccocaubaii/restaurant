export interface IMBTellerPaperDetailSalary {
  id?: string;
  mBTellerPaperID?: string;
  employeeID?: string;
  description?: string;
  departmentID?: string;
  accumAmount?: number;
  accumAmountOriginal?: number;
  currentMonthAmount?: number;
  currentMonthAmountOriginal?: number;
  payAmount?: number;
  payAmountOld?: number;
  payAmountOriginal?: number;
  bankAccountDetailID?: string;
  orderPriority?: number;
}

export class MBTellerPaperDetailSalary implements IMBTellerPaperDetailSalary {
  constructor(
    public id?: string,
    public mBTellerPaperID?: string,
    public employeeID?: string,
    public description?: string,
    public departmentID?: string,
    public accumAmount?: number,
    public accumAmountOriginal?: number,
    public currentMonthAmount?: number,
    public currentMonthAmountOriginal?: number,
    public payAmount?: number,
    public payAmountOld?: number,
    public payAmountOriginal?: number,
    public bankAccountDetailID?: string,
    public orderPriority?: number
  ) {}
}
