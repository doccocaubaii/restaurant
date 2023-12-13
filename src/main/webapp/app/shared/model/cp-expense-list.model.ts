import { Moment } from 'moment';

export interface ICPExpenseList {
  id?: string;
  cPPeriodID?: string;
  typeVoucher?: number;
  costSetID?: string;
  contractID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  no?: string;
  description?: string;
  amount?: number;
  accountNumber?: string;
  expenseItemID?: string;
}

export class CPExpenseList implements ICPExpenseList {
  constructor(
    public id?: string,
    public cPPeriodID?: string,
    public typeVoucher?: number,
    public costSetID?: string,
    public contractID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public no?: string,
    public description?: string,
    public amount?: number,
    public accountNumber?: string,
    public expenseItemID?: string
  ) {}
}
