import { Moment } from 'moment';

export interface ISearchVoucherFAInit {
  toDate?: Moment | any;
  fromDate?: Moment | any;
  keySearch?: string;
}

export class SearchVoucherFAInit implements ISearchVoucherFAInit {
  constructor(public toDate?: Moment | any, public fromDate?: Moment | any, public keySearch?: string) {}
}

export interface ISearchTM05QT {
  toDate?: string | any;
  fromDate?: string | any;
  keySearch?: string;
}

export class ISearchTM05QT implements ISearchVoucherFAInit {
  constructor(public toDate?: string | any, public fromDate?: string | any, public keySearch?: string) {}
}
