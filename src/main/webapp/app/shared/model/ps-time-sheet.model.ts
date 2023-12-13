import { IPSTimeSheetDetails } from 'app/shared/model/ps-time-sheet-details.model';

export interface IPSTimeSheet {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  typeLedger?: number;
  typeLedgerStr?: string;
  month?: number;
  year?: number;
  pSTimeSheetName?: string;
  pSTimeSheetDetails?: IPSTimeSheetDetails[];
}

export class PSTimeSheet implements IPSTimeSheet {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public typeLedgerStr?: string,
    public month?: number,
    public year?: number,
    public pSTimeSheetName?: string,
    public pSTimeSheetDetails?: IPSTimeSheetDetails[]
  ) {}
}
