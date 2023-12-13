export interface ITimeSheetSymbols {
  id?: string;
  timeSheetSymbolsCode?: string;
  timeSheetSymbolsName?: string;
  salaryRate?: number;
  isDefault?: boolean;
  isHalfDayDefault?: boolean;
  isHalfDay?: boolean;
  overTimeSymbol?: number;
  isOverTime?: boolean;
  isActive?: boolean;
  isSecurity?: boolean;
  timeOverTime?: number;
  checked?: boolean;
  isReadOnly?: boolean;
}

export class TimeSheetSymbols implements ITimeSheetSymbols {
  constructor(
    public id?: string,
    public timeSheetSymbolsCode?: string,
    public timeSheetSymbolsName?: string,
    public salaryRate?: number,
    public isDefault?: boolean,
    public isHalfDayDefault?: boolean,
    public isHalfDay?: boolean,
    public overTimeSymbol?: number,
    public isOverTime?: boolean,
    public isActive?: boolean,
    public isSecurity?: boolean,
    public timeOverTime?: number,
    public checked?: boolean,
    public isReadOnly?: boolean
  ) {
    this.isDefault = this.isDefault || false;
    this.isHalfDayDefault = this.isHalfDayDefault || false;
    this.isHalfDay = this.isHalfDay || false;
    this.isOverTime = this.isOverTime || false;
    this.isActive = this.isActive || false;
    this.isSecurity = this.isSecurity || false;
    this.isReadOnly = this.isReadOnly || false;
  }
}
