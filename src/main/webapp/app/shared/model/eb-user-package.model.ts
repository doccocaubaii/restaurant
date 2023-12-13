import { Moment } from 'moment';

export interface IEbUserPackage {
  id?: string;
  userID?: string;
  packageID?: string;
  companyID?: string;
  status?: number;
  isTotalPackage?: boolean;
  activedDate?: Moment | any;
  expriredDate?: Moment | any;
  packCount?: number;
  packCountUpgrade?: number;
  createdDate?: Moment | any;
  modifiedDate?: Moment | any;
}

export class EbUserPackage implements IEbUserPackage {
  constructor(
    public id?: string,
    public userID?: string,
    public packageID?: string,
    public companyID?: string,
    public status?: number,
    public isTotalPackage?: boolean,
    public activedDate?: Moment | any,
    public expriredDate?: Moment | any,
    public packCount?: number,
    public packCountUpgrade?: number,
    public createdDate?: Moment | any,
    public modifiedDate?: Moment
  ) {
    this.isTotalPackage = this.isTotalPackage || false;
  }
}
