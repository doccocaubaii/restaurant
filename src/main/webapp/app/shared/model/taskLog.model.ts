import { Moment } from 'moment';
import { IAccountingObjectBankAccount } from './accounting-object-bank-account.model';
import { ITaskLogDetail } from './taskLogDetail.model';
import { IOrganizationUnit, OrganizationUnit } from './organization-unit.model';

export interface ITaskLog {
  id?: string;
  companyID?: string;

  createTime?: string;
  taskID?: string;
  status?: number;
  startTime?: Moment | any;
  endTime?: Moment | any;
  description?: string;
  userID?: string;
  userName?: string;
  pathFile?: string;
  taskLogDetails?: ITaskLogDetail[];
}

export class TaskLog implements ITaskLog {
  constructor(
    public id?: string,
    public companyID?: string,
    public createTime?: string,
    public taskID?: string,
    public status?: number,
    public startTime?: Moment | any,
    public endTime?: Moment | any,
    public description?: string,
    public userID?: string,
    public userName?: string,
    public pathFile?: string,
    public taskLogDetails?: ITaskLogDetail[]
  ) {}
}
