export interface ISalaryPayment {
  check?: boolean;
  employeeID?: string;
  departmentID?: string;
  accountingObjectBankAccounts?: any[];
  accountingObjectBankAccountID?: string;
  bankName?: string;
  accumAmount?: number;
  payAmount?: number;
  incomeTaxAmount?: number;
  incomeTaxAmountOld?: number;
  received?: number;
}

export class SalaryPayment implements ISalaryPayment {
  constructor(
    public check?: boolean,
    public employeeID?: string,
    public departmentID?: string,
    public accountingObjectBankAccounts?: any[],
    public accountingObjectBankAccountID?: string,
    public bankName?: string,
    public accumAmount?: number,
    public payAmount?: number,
    public incomeTaxAmount?: number,
    public incomeTaxAmountOld?: number,
    public received?: number
  ) {}
}
