import { Moment } from 'moment';

export interface ISearchVoucherTools {
  toDate?: Moment | any;
  fromDate?: Moment | any;
  keySearch?: string;
  checkCategory?: boolean;
}

export class SearchVoucherTools implements ISearchVoucherTools {
  constructor(public toDate?: Moment | any, public fromDate?: Moment | any, public keySearch?: string, public checkCategory?: boolean) {}
}
