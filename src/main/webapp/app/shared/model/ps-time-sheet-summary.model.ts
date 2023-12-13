import { IPSTimeSheetSummaryDetails } from 'app/shared/model/ps-time-sheet-summary-details.model';

export interface IPSTimeSheetSummary {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  typeLedger?: number;
  typeLedgerStr?: string;
  month?: number;
  year?: number;
  pSTimeSheetSummaryName?: string;
  pSTimeSheetSummaryDetails?: IPSTimeSheetSummaryDetails[];
}

export class PSTimeSheetSummary implements IPSTimeSheetSummary {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public typeLedgerStr?: string,
    public month?: number,
    public year?: number,
    public pSTimeSheetSummaryName?: string,
    public pSTimeSheetSummaryDetails?: IPSTimeSheetSummaryDetails[]
  ) {}
}
