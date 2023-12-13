import { Moment } from 'moment';

export interface ISearchGvoucherList {
  fromDate?: Moment | any;
  toDate?: Moment | any;
  searchValue?: string;
}

export class SearchGvoucherList implements ISearchGvoucherList {
  constructor(public fromDate?: Moment | any, public toDate?: Moment | any, public searchValue?: string) {}
}
