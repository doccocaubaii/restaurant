import { Moment } from 'moment';

export interface IRsInwardOutwardSearch {
  typeVoucher?: number;
  object?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  status?: string;
  keyWords?: string;
}
export class RsInwardSearchData implements IRsInwardOutwardSearch {
  constructor(
    public typeVoucher?: number,
    public object?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public status?: string,
    public keyWords?: string
  ) {}
}

export class RsOutwardSearchData implements IRsInwardOutwardSearch {
  constructor(
    public typeVoucher?: number,
    public object?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public status?: string,
    public keyWords?: string
  ) {}
}
