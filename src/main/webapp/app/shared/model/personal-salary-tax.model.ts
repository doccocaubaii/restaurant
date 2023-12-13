export interface IPersonalSalaryTax {
  id?: string;
  personalSalaryTaxName?: string;
  personalSalaryTaxGrade?: number;
  salaryType?: number;
  taxRate?: number;
  fromAmount?: number;
  toAmount?: number;
  taxAmountInPersonalSalary?: number;
  isActive?: boolean;
}

export class PersonalSalaryTax implements IPersonalSalaryTax {
  constructor(
    public id?: string,
    public personalSalaryTaxName?: string,
    public personalSalaryTaxGrade?: number,
    public salaryType?: number,
    public taxRate?: number,
    public fromAmount?: number,
    public toAmount?: number,
    public taxAmountInPersonalSalary?: number,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
