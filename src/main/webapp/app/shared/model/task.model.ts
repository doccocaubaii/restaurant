import { Moment } from 'moment';
import { ITaskLogDetail } from './taskLogDetail.model';

export interface ITask {
  id?: string;
  name?: string;
}

export class Task implements ITask {
  constructor(public id?: string, public name?: string) {}
}
