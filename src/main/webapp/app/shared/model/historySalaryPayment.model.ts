import { Moment } from 'moment';

export interface IHistorySalaryPayment {
  refID2?: string;
  employeeID?: string;
  postedDate?: Moment | any;
  date?: Moment | any;
  no?: string;
  reason?: string;
  accumAmount?: number;
  payAmount?: number;
  incomeTaxAmount?: number;
  remainingAmount?: number;
  typeID?: number;
  typeGroupID?: number;
}

export class HistorySalaryPayment implements IHistorySalaryPayment {
  constructor(
    public refID2?: string,
    public employeeID?: string,
    public postedDate?: Moment | any,
    public date?: Moment | any,
    public no?: string,
    public reason?: string,
    public accumAmount?: number,
    public payAmount?: number,
    public incomeTaxAmount?: number,
    public remainingAmount?: number,
    public typeID?: number,
    public typeGroupID?: number
  ) {}
}
