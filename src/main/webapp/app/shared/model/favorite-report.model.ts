import * as dayjs from 'dayjs';
import { Moment } from 'moment/moment';

export interface IFavoriteReport {
  id?: string;
  companyID?: string;
  reportType?: number;
  reportName?: number;
  postedDate?: Moment | any;
  role?: string;
}

export class FavoriteReport implements IFavoriteReport {
  constructor(
    public id?: string,
    public companyID?: string,
    public reportType?: number,
    public reportName?: number,
    public postedDate?: Moment | any,
    public role?: string
  ) {}
}
