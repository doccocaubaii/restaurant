import { IPSSalarySheetDetails } from 'app/shared/model/ps-salary-sheet-details.model';

export interface IPSSalarySheet {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  typeLedger?: number;
  month?: number;
  year?: number;
  totalNetIncomeAmount?: number;
  pSSalarySheetName?: string;
  gOtherVoucherID?: string;
  dayWorks?: number;
  pSSalarySheetDetails?: IPSSalarySheetDetails[];
  total?: number;
  pSTimeSheetID?: any;
}

export class PSSalarySheet implements IPSSalarySheet {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public month?: number,
    public year?: number,
    public reasonFail?: string, // dùng cho đưa ra kết quả không thành công
    public totalNetIncomeAmount?: number,
    public pSSalarySheetName?: string,
    public gOtherVoucherID?: string,
    public dayWorks?: number,
    public pSSalarySheetDetails?: IPSSalarySheetDetails[],
    public total?: number,
    public pSTimeSheetID?: any
  ) {}
}
