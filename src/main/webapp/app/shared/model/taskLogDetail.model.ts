import { Moment } from 'moment';
import { IAccountingObjectBankAccount } from './accounting-object-bank-account.model';

export interface ITaskLogDetail {
  id?: string;
  createTime?: string;
  taskLogID?: string;
  status?: number;
  startTime?: Moment | any;
  endTime?: Moment | any;
  pathFile?: string;
  exception?: string;
}

export class TaskLogDetail implements ITaskLogDetail {
  constructor(
    public id?: string,
    public createTime?: string,
    public taskLogID?: string,
    public status?: number,
    public startTime?: Moment | any,
    public endTime?: Moment | any,
    public pathFile?: string,
    public exception?: string
  ) {}
}
