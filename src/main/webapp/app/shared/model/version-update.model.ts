import { Moment } from 'moment';

export interface IVersionUpdate {
  id?: string;
  version?: string;
  date?: Moment | any;
  description?: string;
  link?: string;
  active?: boolean;
  startDate?: Moment | any;
  endDate?: Moment | any;
  updateLink?: IUpdateLink[];
  image?: string;
  taxCodes?: string;
  orgCodes?: string;
  type?: number;
}

export class VersionUpdate implements IVersionUpdate {
  constructor(
    public id?: string,
    public version?: string,
    public date?: Moment | any,
    public description?: string,
    public link?: string,
    public active?: boolean,
    public startDate?: Moment | any,
    public endDate?: Moment | any,
    public updateLink?: IUpdateLink[]
  ) {}
}

export interface IUpdateLink {
  name?: string;
  link?: string;
}
