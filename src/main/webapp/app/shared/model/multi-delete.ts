import { Moment } from 'moment';
import { IMBDeposit } from 'app/shared/model/mb-deposit.model';

export interface IMultiDelete {
  status?: boolean;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeID?: string;
  reason?: string;
}

export class MultiDelete implements IMultiDelete {
  constructor(
    public status?: boolean,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeID?: string,
    public reason?: string
  ) {}
}
