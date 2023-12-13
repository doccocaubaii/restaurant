import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';
import { PSTimeSheetDetails } from 'app/shared/model/ps-time-sheet-details.model';

export interface IPSTimeSheetSummaryDetails {
  id?: string;
  pSTimeSheetSummaryID?: string;
  employeeID?: string;
  accountingObjectName?: string;
  accountingObjectTitle?: string;
  departmentID?: string;
  workAllDay?: number;
  workHalfAday?: number;
  totalOverTime?: number;
  total?: number;
  orderPriority?: number;
  listEmployeeID?: IAccountingObject[];
  stt?: number;
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
}

export class PSTimeSheetSummaryDetails implements IPSTimeSheetSummaryDetails {
  constructor(
    public id?: string,
    public pSTimeSheetSummaryID?: string,
    public employeeID?: string,
    public accountingObjectName?: string,
    public accountingObjectTitle?: string,
    public departmentID?: string,
    public workAllDay?: number,
    public workHalfAday?: number,
    public totalOverTime?: number,
    public total?: number,
    public orderPriority?: number,
    public listEmployeeID?: IAccountingObject[],
    public stt?: number,
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
    public day31?: string
  ) {}
}

export class TongHopChamCongExportExcel {
  private stt: number;
  private department: string;
  private employeeCode: string;
  private employeeName: string;
  private workAllDay: number;
  private workHalfAday: number;
  private totalOverTime: number;
  private total: number;

  constructor() {}

  public setAllFieldsFromPSTimeSummarySheetDetails(
    psTimeSummarySheetDetails: PSTimeSheetSummaryDetails,
    organizationUnits: IOrganizationUnit[]
  ): void {
    let department = '';
    const departmentId: string = psTimeSummarySheetDetails.departmentID ? psTimeSummarySheetDetails.departmentID : '';
    for (let i = 0; i < organizationUnits.length; i++) {
      if (departmentId !== null && departmentId === organizationUnits[i].id) {
        department = organizationUnits[i].organizationUnitCode;
        break;
      }
    }
    let employeeCode = '';
    const employeeID: string = psTimeSummarySheetDetails.employeeID ? psTimeSummarySheetDetails.employeeID : '';
    for (let i = 0; i < psTimeSummarySheetDetails.listEmployeeID.length; i++) {
      if (employeeID !== null && employeeID === psTimeSummarySheetDetails.listEmployeeID[i].id) {
        employeeCode = psTimeSummarySheetDetails.listEmployeeID[i].accountingObjectCode;
        break;
      }
    }
    this.stt = psTimeSummarySheetDetails.stt + 1;
    this.employeeCode = employeeCode;
    this.employeeName = psTimeSummarySheetDetails.accountingObjectName ? psTimeSummarySheetDetails.accountingObjectName : '';
    this.department = department;
    this.workHalfAday = psTimeSummarySheetDetails.workHalfAday ? psTimeSummarySheetDetails.workHalfAday : 0;
    this.workAllDay = psTimeSummarySheetDetails.workAllDay ? psTimeSummarySheetDetails.workAllDay : 0;
    this.totalOverTime = psTimeSummarySheetDetails.totalOverTime ? psTimeSummarySheetDetails.totalOverTime : 0;
    this.total = psTimeSummarySheetDetails.total ? psTimeSummarySheetDetails.total : 0;
  }
}
