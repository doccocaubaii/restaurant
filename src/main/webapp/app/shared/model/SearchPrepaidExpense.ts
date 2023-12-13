import { Moment } from 'moment';

export interface ISearchPrepaidExpense {
  typeExpense?: any /*Loại chứng từ*/;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  textSearch?: string;
}

export class SearchPrepaidExpense implements ISearchPrepaidExpense {
  constructor(public typeExpense?: any, public fromDate?: Moment | any, public toDate?: Moment | any, public textSearch?: string) {}
}
