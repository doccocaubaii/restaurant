import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IPSSalarySheetDetails {
  id?: string;
  pSSalarySheetID?: string;
  employeeID?: string;
  accountingObjectName?: string;
  accountingObjectTitle?: string;
  departmentID?: string;
  salaryCoefficient?: number;
  basicWage?: number;
  workingDayUnitPrice?: number;
  workingHourUnitPrice?: number;
  agreementSalary?: number;
  numberOfPaidWorkingDayTimeSheet?: number;
  paidWorkingDayAmount?: number;
  numberOfNonWorkingDayTimeSheet?: number;
  nonWorkingDayAmount?: number;
  productUnitPrice?: number;
  totalProduct?: number;
  productAmount?: number;
  productAmountOriginal?: number;
  totalOverTime?: number;
  overTimeAmount?: number;
  payrollFundAllowance?: number;
  otherAllowance?: number;
  notIncomeTaxAllowance?: number;
  totalAmount?: number;
  temporaryAmount?: number;
  employeeSocityInsuranceAmount?: number;
  employeeAccidentInsuranceAmount?: number;
  employeeMedicalInsuranceAmount?: number;
  employeeUnEmployeeInsuranceAmount?: number;
  employeeTradeUnionInsuranceAmount?: number;
  incomeTaxAmount?: number;
  sumOfDeductionAmount?: number;
  totalPersonalTaxIncomeAmount?: number;
  reduceSelfTaxAmount?: number;
  reduceDependTaxAmount?: number;
  incomeForTaxCalculation?: number;
  signName?: number;
  insuranceSalary?: number;
  netAmount?: number;
  companySocityInsuranceAmount?: number;
  companyAccidentInsuranceAmount?: number;
  companyMedicalInsuranceAmount?: number;
  companyUnEmployeeInsuranceAmount?: number;
  companyTradeUnionInsuranceAmount?: number;
  orderPriority?: number;
  listEmployeeID?: IAccountingObject[];
  numberOfDependent?: number;
  isUnOfficialStaff?: boolean;
  stt?: number;
  workingDay?: number;
  weekendDay?: number;
  holiday?: number;
  workingDayNight?: number;
  weekendDayNight?: number;
  holidayNight?: number;
  otherDays100NotUnitPrice?: number;
}

export class PSSalarySheetDetails implements IPSSalarySheetDetails {
  constructor(
    public id?: string,
    public pSSalarySheetID?: string,
    public employeeID?: string,
    public accountingObjectName?: string,
    public accountingObjectTitle?: string,
    public departmentID?: string,
    public salaryCoefficient?: number,
    public basicWage?: number,
    public workingDayUnitPrice?: number,
    public workingHourUnitPrice?: number,
    public agreementSalary?: number,
    public numberOfPaidWorkingDayTimeSheet?: number,
    public paidWorkingDayAmount?: number,
    public numberOfNonWorkingDayTimeSheet?: number,
    public nonWorkingDayAmount?: number,
    public productUnitPrice?: number,
    public totalProduct?: number,
    public productAmount?: number,
    public productAmountOriginal?: number,
    public totalOverTime?: number,
    public overTimeAmount?: number,
    public payrollFundAllowance?: number,
    public otherAllowance?: number,
    public notIncomeTaxAllowance?: number,
    public totalAmount?: number,
    public temporaryAmount?: number,
    public employeeSocityInsuranceAmount?: number,
    public employeeAccidentInsuranceAmount?: number,
    public employeeMedicalInsuranceAmount?: number,
    public employeeUnEmployeeInsuranceAmount?: number,
    public employeeTradeUnionInsuranceAmount?: number,
    public incomeTaxAmount?: number,
    public sumOfDeductionAmount?: number,
    public totalPersonalTaxIncomeAmount?: number,
    public reduceSelfTaxAmount?: number,
    public reduceDependTaxAmount?: number,
    public incomeForTaxCalculation?: number,
    public signName?: number,
    public insuranceSalary?: number,
    public netAmount?: number,
    public companySocityInsuranceAmount?: number,
    public companyAccidentInsuranceAmount?: number,
    public companyMedicalInsuranceAmount?: number,
    public companyUnEmployeeInsuranceAmount?: number,
    public companyTradeUnionInsuranceAmount?: number,
    public orderPriority?: number,
    public listEmployeeID?: IAccountingObject[],
    public numberOfDependent?: number,
    public isUnOfficialStaff?: boolean,
    public stt?: number,
    public workingDay?: number,
    public weekendDay?: number,
    public holiday?: number,
    public workingDayNight?: number,
    public weekendDayNight?: number,
    public holidayNight?: number,
    public otherDays100NotUnitPrice?: number
  ) {
    this.workingDay = this.workingDay || 0;
    this.weekendDay = this.weekendDay || 0;
    this.holiday = this.holiday || 0;
    this.workingDayNight = this.workingDayNight || 0;
    this.weekendDayNight = this.weekendDayNight || 0;
    this.holidayNight = this.holidayNight || 0;
  }
}

export class BangLuongExportExcel {
  private stt: number;
  private department: string;
  private employeeCode: string;
  private employeeName: string;
  private position: string;
  private salaryAgreement: number;
  private salaryCoefficient: number;
  private basicWage: number;
  private workingDayUnitPrice: number;
  private workingHourUnitPrice: number;
  private numberOfPaidWorkingDayTimeSheet: number;
  private paidWorkingDayAmount: number;
  private numberOfNonWorkingDayTimeSheet: number;
  private nonWorkingDayAmount: number;
  private totalOverTime: number;
  private overTimeAmount: number;
  private payrollFundAllowance: number;
  private otherAllowance: number;
  private notIncomeTaxAllowance: number;
  private totalAmount: number;
  private temporaryAmount: number;
  private insuranceSalary: number;
  private employeeSocietyInsuranceAmount: number;
  private employeeAccidentInsuranceAmount: number;
  private employeeMedicalInsuranceAmount: number;
  private employeeUnEmployeeInsuranceAmount: number;
  private employeeTradeUnionInsuranceAmount: number;
  private incomeTaxAmount: number;
  private sumOfDeductionAmount: number;
  private totalPersonalTaxIncomeAmount: number;
  private reduceSelfTaxAmount: number;
  private reduceDependTaxAmount: number;
  private incomeForTaxCalculation: number;
  private netAmount: number;
  private companySocietyInsuranceAmount: number;
  private companyAccidentInsuranceAmount: number;
  private companyMedicalInsuranceAmount: number;
  private companyUnEmployeeInsuranceAmount: number;
  private companyTradeUnionInsuranceAmount: number;

  constructor() {}

  public setAllFields(psSalarySheetDetails: PSSalarySheetDetails, organizationUnits: IOrganizationUnit[]): void {
    let department = '';
    const departmentId: string = psSalarySheetDetails.departmentID ? psSalarySheetDetails.departmentID : '';
    for (let i = 0; i < organizationUnits.length; i++) {
      if (departmentId !== null && departmentId === organizationUnits[i].id) {
        department = organizationUnits[i].organizationUnitCode;
        break;
      }
    }
    let employeeCode = '';
    const employeeID: string = psSalarySheetDetails.employeeID ? psSalarySheetDetails.employeeID : '';
    for (let i = 0; i < psSalarySheetDetails.listEmployeeID.length; i++) {
      if (employeeID !== null && employeeID === psSalarySheetDetails.listEmployeeID[i].id) {
        employeeCode = psSalarySheetDetails.listEmployeeID[i].accountingObjectCode;
        break;
      }
    }
    this.stt = psSalarySheetDetails.stt;
    this.department = department;
    this.employeeCode = employeeCode;
    this.employeeName = psSalarySheetDetails.accountingObjectName ? psSalarySheetDetails.accountingObjectName : '';
    this.position = psSalarySheetDetails.accountingObjectTitle ? psSalarySheetDetails.accountingObjectTitle : '';
    this.salaryAgreement = psSalarySheetDetails.agreementSalary ? psSalarySheetDetails.agreementSalary : 0;
    this.workingDayUnitPrice = psSalarySheetDetails.workingDayUnitPrice ? psSalarySheetDetails.workingDayUnitPrice : 0;
    this.workingHourUnitPrice = psSalarySheetDetails.workingHourUnitPrice ? psSalarySheetDetails.workingHourUnitPrice : 0;
    this.salaryCoefficient = psSalarySheetDetails.salaryCoefficient ? psSalarySheetDetails.salaryCoefficient : 0;
    this.basicWage = psSalarySheetDetails.basicWage ? psSalarySheetDetails.basicWage : 0;
    this.numberOfPaidWorkingDayTimeSheet = psSalarySheetDetails.numberOfPaidWorkingDayTimeSheet
      ? psSalarySheetDetails.numberOfPaidWorkingDayTimeSheet
      : 0;
    this.paidWorkingDayAmount = psSalarySheetDetails.paidWorkingDayAmount ? psSalarySheetDetails.paidWorkingDayAmount : 0;
    this.numberOfNonWorkingDayTimeSheet = psSalarySheetDetails.numberOfNonWorkingDayTimeSheet
      ? psSalarySheetDetails.numberOfNonWorkingDayTimeSheet
      : 0;
    this.nonWorkingDayAmount = psSalarySheetDetails.nonWorkingDayAmount ? psSalarySheetDetails.nonWorkingDayAmount : 0;
    this.totalOverTime = psSalarySheetDetails.totalOverTime ? psSalarySheetDetails.totalOverTime : 0;
    this.overTimeAmount = psSalarySheetDetails.overTimeAmount ? psSalarySheetDetails.overTimeAmount : 0;
    this.payrollFundAllowance = psSalarySheetDetails.payrollFundAllowance ? psSalarySheetDetails.payrollFundAllowance : 0;
    this.otherAllowance = psSalarySheetDetails.otherAllowance ? psSalarySheetDetails.otherAllowance : 0;
    this.notIncomeTaxAllowance = psSalarySheetDetails.notIncomeTaxAllowance ? psSalarySheetDetails.notIncomeTaxAllowance : 0;
    this.totalAmount = psSalarySheetDetails.totalAmount ? psSalarySheetDetails.totalAmount : 0;
    this.temporaryAmount = psSalarySheetDetails.temporaryAmount ? psSalarySheetDetails.temporaryAmount : 0;
    this.insuranceSalary = psSalarySheetDetails.insuranceSalary ? psSalarySheetDetails.insuranceSalary : 0;
    this.employeeSocietyInsuranceAmount = psSalarySheetDetails.employeeSocityInsuranceAmount
      ? psSalarySheetDetails.employeeSocityInsuranceAmount
      : 0;
    this.employeeAccidentInsuranceAmount = psSalarySheetDetails.employeeAccidentInsuranceAmount
      ? psSalarySheetDetails.employeeAccidentInsuranceAmount
      : 0;
    this.employeeMedicalInsuranceAmount = psSalarySheetDetails.employeeMedicalInsuranceAmount
      ? psSalarySheetDetails.employeeMedicalInsuranceAmount
      : 0;
    this.employeeUnEmployeeInsuranceAmount = psSalarySheetDetails.employeeUnEmployeeInsuranceAmount
      ? psSalarySheetDetails.employeeUnEmployeeInsuranceAmount
      : 0;
    this.employeeTradeUnionInsuranceAmount = psSalarySheetDetails.employeeTradeUnionInsuranceAmount
      ? psSalarySheetDetails.employeeTradeUnionInsuranceAmount
      : 0;
    this.incomeTaxAmount = psSalarySheetDetails.incomeTaxAmount ? psSalarySheetDetails.incomeTaxAmount : 0;
    this.sumOfDeductionAmount = psSalarySheetDetails.sumOfDeductionAmount ? psSalarySheetDetails.sumOfDeductionAmount : 0;
    this.totalPersonalTaxIncomeAmount = psSalarySheetDetails.totalPersonalTaxIncomeAmount
      ? psSalarySheetDetails.totalPersonalTaxIncomeAmount
      : 0;
    this.reduceSelfTaxAmount = psSalarySheetDetails.reduceSelfTaxAmount ? psSalarySheetDetails.reduceSelfTaxAmount : 0;
    this.reduceDependTaxAmount = psSalarySheetDetails.reduceDependTaxAmount ? psSalarySheetDetails.reduceDependTaxAmount : 0;
    this.incomeForTaxCalculation = psSalarySheetDetails.incomeForTaxCalculation ? psSalarySheetDetails.incomeForTaxCalculation : 0;
    this.netAmount = psSalarySheetDetails.netAmount ? psSalarySheetDetails.netAmount : 0;
    this.companySocietyInsuranceAmount = psSalarySheetDetails.companySocityInsuranceAmount
      ? psSalarySheetDetails.companySocityInsuranceAmount
      : 0;
    this.companyAccidentInsuranceAmount = psSalarySheetDetails.companyAccidentInsuranceAmount
      ? psSalarySheetDetails.companyAccidentInsuranceAmount
      : 0;
    this.companyMedicalInsuranceAmount = psSalarySheetDetails.companyMedicalInsuranceAmount
      ? psSalarySheetDetails.companyMedicalInsuranceAmount
      : 0;
    this.companyUnEmployeeInsuranceAmount = psSalarySheetDetails.companyUnEmployeeInsuranceAmount
      ? psSalarySheetDetails.companyUnEmployeeInsuranceAmount
      : 0;
    this.companyTradeUnionInsuranceAmount = psSalarySheetDetails.companyTradeUnionInsuranceAmount
      ? psSalarySheetDetails.companyTradeUnionInsuranceAmount
      : 0;
  }
}
