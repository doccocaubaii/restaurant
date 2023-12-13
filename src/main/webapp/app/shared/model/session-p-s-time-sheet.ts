export interface ISessionPSTimeSheet {
  month?: number;
  year?: number;
  typeID?: number;
  pSTimeSheetName?: string;
  pSTimeSheetSummaryName?: string;
  pSSalarySheetName?: string;
  typeCreate?: number;
  lastPSTimeSheetID?: string;
  lastPSTimeSheetSummaryID?: string;
  lastPSSalarySheetID?: string;
  autoAddNewEmployee?: boolean;
  getEmployeeNotActive?: boolean;
  listDepartmentID?: any[];
  daysWork?: number;
}

export class SessionPSTimeSheet implements ISessionPSTimeSheet {
  constructor(
    public month?: number,
    public year?: number,
    public typeID?: number,
    public pSTimeSheetName?: string,
    public pSTimeSheetSummaryName?: string,
    public pSSalarySheetName?: string,
    public typeCreate?: number,
    public lastPSTimeSheetID?: string,
    public lastPSTimeSheetSummaryID?: string,
    public lastPSSalarySheetID?: string,
    public autoAddNewEmployee?: boolean,
    public getEmployeeNotActive?: boolean,
    public listDepartmentID?: any[],
    public daysWork?: number
  ) {}
}
