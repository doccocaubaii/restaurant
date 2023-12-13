import { Moment } from 'moment';

export interface IRSTransferSearchData {
  noType?: number;
  repository?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  status?: string;
  accountingObject?: string;
  searchValue?: string;
}
export class RSTransferSearchData implements IRSTransferSearchData {
  constructor(
    public noType?: number,
    public repository?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public status?: string,
    public accountingObject?: string,
    public searchValue?: string
  ) {}
}
