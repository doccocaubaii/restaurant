import { Moment } from 'moment';

export interface IContractSearch {
  currency?: string;
  startedDate?: Moment | any;
  closedDate?: Moment | any;
  signedDate?: Moment | any;
  contractState?: string;
  accountingObject?: string;
  searchValue?: string;
}
export class PurchaseContractSearchData implements IContractSearch {
  constructor(
    public currency?: string,
    public startedDate?: Moment | any,
    public closedDate?: Moment | any,
    public signedDate?: Moment | any,
    public contractState?: string,
    public accountingObject?: string,
    public searchValue?: string
  ) {}
}

export class SaleContractSearchData implements IContractSearch {
  constructor(
    public currency?: string,
    public startedDate?: Moment | any,
    public closedDate?: Moment | any,
    public signedDate?: Moment | any,
    public contractState?: string,
    public accountingObject?: string,
    public searchValue?: string
  ) {}
}
