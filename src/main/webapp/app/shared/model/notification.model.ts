import { Moment } from 'moment';

export interface INotification {
  id?: string;
  userID?: number;
  companyID?: string;
  isRead?: boolean;
  image?: string;
  description?: string;
  startDate?: Moment | any;
  endDate?: Moment | any;
  isActive?: boolean;
  timeNoti?: string;
}

export class Notification implements INotification {
  constructor(
    public id?: string,
    public userID?: number,
    public companyID?: string,
    public isRead?: boolean,
    public image?: string,
    public description?: string,
    public startDate?: Moment | any,
    public endDate?: Moment | any,
    public isActive?: boolean,
    public timeNoti?: string
  ) {
    this.isRead = this.isRead || false;
    this.isActive = this.isActive || false;
  }
}
