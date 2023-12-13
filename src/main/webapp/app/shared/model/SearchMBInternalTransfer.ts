import { Moment } from 'moment';

export interface ISearchMBInternalTransfer {
  fromBankAccountDetailID?: string;
  toBankAccountDetailID?: string;
  currencyID?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  statusRecord?: boolean;
  searchValue?: string;
}

export class SearchMBInternalTransfer implements ISearchMBInternalTransfer {
  constructor(
    public fromBankAccountDetailID?: string,
    public toBankAccountDetailID?: string,
    public currencyID?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public statusRecord?: boolean,
    public searchValue?: string
  ) {}
}
