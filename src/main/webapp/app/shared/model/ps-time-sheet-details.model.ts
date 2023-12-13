import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface IPSTimeSheetDetails {
  id?: string;
  pSTimeSheetID?: string;
  employeeID?: string;
  accountingObjectName?: string;
  accountingObjectTitle?: string;
  departmentID?: string;
  fromHour?: number;
  toHour?: number;
  day1?: string;
  day2?: string;
  day3?: string;
  day4?: string;
  day5?: string;
  day6?: string;
  day7?: string;
  day8?: string;
  day9?: string;
  day10?: string;
  day11?: string;
  day12?: string;
  day13?: string;
  day14?: string;
  day15?: string;
  day16?: string;
  day17?: string;
  day18?: string;
  day19?: string;
  day20?: string;
  day21?: string;
  day22?: string;
  day23?: string;
  day24?: string;
  day25?: string;
  day26?: string;
  day27?: string;
  day28?: string;
  day29?: string;
  day30?: string;
  day31?: string;
  paidWorkingDay?: number;
  paidNonWorkingDay?: number;
  nonPaidNonWorkingDay?: number;
  paidPaidWorkingDay?: number;
  otherDays100NotUnitPrice?: number;
  insuranceReplaceSalaryWorkingDay?: number;
  insuranceReplaceSalaryTotalRate?: number;
  workingDay?: number;
  weekendDay?: number;
  holiday?: number;
  workingDayNight?: number;
  weekendDayNight?: number;
  holidayNight?: number;
  totalOverTime?: number;
  orderPriority?: number;
  listEmployeeID?: IAccountingObject[];
  stt?: number;
}

export class PSTimeSheetDetails implements IPSTimeSheetDetails {
  constructor(
    public id?: string,
    public pSTimeSheetID?: string,
    public employeeID?: string,
    public accountingObjectName?: string,
    public accountingObjectTitle?: string,
    public departmentID?: string,
    public fromHour?: number,
    public toHour?: number,
    public day1?: string,
    public day2?: string,
    public day3?: string,
    public day4?: string,
    public day5?: string,
    public day6?: string,
    public day7?: string,
    public day8?: string,
    public day9?: string,
    public day10?: string,
    public day11?: string,
    public day12?: string,
    public day13?: string,
    public day14?: string,
    public day15?: string,
    public day16?: string,
    public day17?: string,
    public day18?: string,
    public day19?: string,
    public day20?: string,
    public day21?: string,
    public day22?: string,
    public day23?: string,
    public day24?: string,
    public day25?: string,
    public day26?: string,
    public day27?: string,
    public day28?: string,
    public day29?: string,
    public day30?: string,
    public day31?: string,
    public paidWorkingDay?: number,
    public paidNonWorkingDay?: number,
    public nonPaidNonWorkingDay?: number,
    public paidPaidWorkingDay?: number,
    public otherDays100NotUnitPrice?: number,
    public insuranceReplaceSalaryWorkingDay?: number,
    public insuranceReplaceSalaryTotalRate?: number,
    public workingDay?: number,
    public weekendDay?: number,
    public holiday?: number,
    public workingDayNight?: number,
    public weekendDayNight?: number,
    public holidayNight?: number,
    public totalOverTime?: number,
    public orderPriority?: number,
    public listEmployeeID?: IAccountingObject[],
    public stt?: number
  ) {}
}

export class ChamCongExportExcelDTO {
  private stt: number;
  private department: string;
  private employeeCode: string;
  private employeeName: string;
  private day1: string;
  private day2: string;
  private day3: string;
  private day4: string;
  private day5: string;
  private day6: string;
  private day7: string;
  private day8: string;
  private day9: string;
  private day10: string;
  private day11: string;
  private day12: string;
  private day13: string;
  private day14: string;
  private day15: string;
  private day16: string;
  private day17: string;
  private day18: string;
  private day19: string;
  private day20: string;
  private day21: string;
  private day22: string;
  private day23: string;
  private day24: string;
  private day25: string;
  private day26: string;
  private day27: string;
  private day28: string;
  private day29: string;
  private day30: string;
  private day31: string;
  private paidWorkingDay: number;
  private unPaidWorkingDay: number;
  private working: number;
  private weekendDay: number;
  private holiday: number;
  private nightShiftWeekEnd: number;
  private nightShift: number;
  private nightShiftHoliday: number;
  private totalOverTime: number;

  constructor() {}

  public setAllFieldsFromPSTimeSheetDetails(psTimeSheetDetails: PSTimeSheetDetails, organizationUnits: IOrganizationUnit[]): void {
    let department = '';
    const departmentId: string = psTimeSheetDetails.departmentID ? psTimeSheetDetails.departmentID : '';
    for (let i = 0; i < organizationUnits.length; i++) {
      if (departmentId !== null && departmentId === organizationUnits[i].id) {
        department = organizationUnits[i].organizationUnitCode;
        break;
      }
    }
    let employeeCode = '';
    const employeeID: string = psTimeSheetDetails.employeeID ? psTimeSheetDetails.employeeID : '';
    for (let i = 0; i < psTimeSheetDetails.listEmployeeID.length; i++) {
      if (employeeID !== null && employeeID === psTimeSheetDetails.listEmployeeID[i].id) {
        employeeCode = psTimeSheetDetails.listEmployeeID[i].accountingObjectCode;
        break;
      }
    }
    this.stt = psTimeSheetDetails.stt ? psTimeSheetDetails.stt : null;
    this.employeeCode = employeeCode;
    this.employeeName = psTimeSheetDetails.accountingObjectName ? psTimeSheetDetails.accountingObjectName : '';
    this.department = department;
    this.day1 = psTimeSheetDetails.day1 ? psTimeSheetDetails.day1 : '';
    this.day2 = psTimeSheetDetails.day2 ? psTimeSheetDetails.day2 : '';
    this.day3 = psTimeSheetDetails.day3 ? psTimeSheetDetails.day3 : '';
    this.day4 = psTimeSheetDetails.day4 ? psTimeSheetDetails.day4 : '';
    this.day5 = psTimeSheetDetails.day5 ? psTimeSheetDetails.day5 : '';
    this.day6 = psTimeSheetDetails.day6 ? psTimeSheetDetails.day6 : '';
    this.day7 = psTimeSheetDetails.day7 ? psTimeSheetDetails.day7 : '';
    this.day8 = psTimeSheetDetails.day8 ? psTimeSheetDetails.day8 : '';
    this.day9 = psTimeSheetDetails.day9 ? psTimeSheetDetails.day9 : '';
    this.day10 = psTimeSheetDetails.day10 ? psTimeSheetDetails.day10 : '';
    this.day11 = psTimeSheetDetails.day11 ? psTimeSheetDetails.day11 : '';
    this.day12 = psTimeSheetDetails.day12 ? psTimeSheetDetails.day12 : '';
    this.day13 = psTimeSheetDetails.day13 ? psTimeSheetDetails.day13 : '';
    this.day14 = psTimeSheetDetails.day14 ? psTimeSheetDetails.day14 : '';
    this.day15 = psTimeSheetDetails.day15 ? psTimeSheetDetails.day15 : '';
    this.day16 = psTimeSheetDetails.day16 ? psTimeSheetDetails.day16 : '';
    this.day17 = psTimeSheetDetails.day17 ? psTimeSheetDetails.day17 : '';
    this.day18 = psTimeSheetDetails.day18 ? psTimeSheetDetails.day18 : '';
    this.day19 = psTimeSheetDetails.day19 ? psTimeSheetDetails.day19 : '';
    this.day20 = psTimeSheetDetails.day20 ? psTimeSheetDetails.day20 : '';
    this.day21 = psTimeSheetDetails.day21 ? psTimeSheetDetails.day21 : '';
    this.day22 = psTimeSheetDetails.day22 ? psTimeSheetDetails.day22 : '';
    this.day23 = psTimeSheetDetails.day23 ? psTimeSheetDetails.day23 : '';
    this.day24 = psTimeSheetDetails.day24 ? psTimeSheetDetails.day24 : '';
    this.day25 = psTimeSheetDetails.day25 ? psTimeSheetDetails.day25 : '';
    this.day26 = psTimeSheetDetails.day26 ? psTimeSheetDetails.day26 : '';
    this.day27 = psTimeSheetDetails.day27 ? psTimeSheetDetails.day27 : '';
    this.day28 = psTimeSheetDetails.day28 ? psTimeSheetDetails.day28 : '';
    this.day29 = psTimeSheetDetails.day29 ? psTimeSheetDetails.day29 : '';
    this.day30 = psTimeSheetDetails.day30 ? psTimeSheetDetails.day30 : '';
    this.day31 = psTimeSheetDetails.day31 ? psTimeSheetDetails.day31 : '';
    this.paidWorkingDay = psTimeSheetDetails.paidWorkingDay ? psTimeSheetDetails.paidWorkingDay : null;
    this.unPaidWorkingDay = psTimeSheetDetails.paidNonWorkingDay ? psTimeSheetDetails.paidNonWorkingDay : null;
    this.working = psTimeSheetDetails.workingDay ? psTimeSheetDetails.workingDay : null;
    this.weekendDay = psTimeSheetDetails.weekendDay ? psTimeSheetDetails.weekendDay : null;
    this.holiday = psTimeSheetDetails.holiday ? psTimeSheetDetails.holiday : null;
    this.nightShift = psTimeSheetDetails.workingDayNight ? psTimeSheetDetails.workingDayNight : null;
    this.nightShiftWeekEnd = psTimeSheetDetails.weekendDayNight ? psTimeSheetDetails.weekendDayNight : null;
    this.nightShiftHoliday = psTimeSheetDetails.holidayNight ? psTimeSheetDetails.holidayNight : null;
    this.totalOverTime = psTimeSheetDetails.totalOverTime ? psTimeSheetDetails.totalOverTime : null;
  }
}

export class BangChamCongExportExcelDTO {
  public refID: any;
  public chamCongExportExcelDTOs: ChamCongExportExcelDTO[];
  public soNgayCuaThang: number;
  public chuNhatDauTien: number;

  constructor() {}
}
