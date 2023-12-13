export interface IInsurancePayment {
  check?: boolean;
  debitAccount?: string;
  description?: number;
  amountPayable?: number;
  amountPayableThisTime?: number;
}

export class InsurancePayment implements IInsurancePayment {
  constructor(
    public check?: boolean,
    public debitAccount?: string,
    public description?: number,
    public amountPayable?: number,
    public amountPayableThisTime?: number
  ) {}
}
