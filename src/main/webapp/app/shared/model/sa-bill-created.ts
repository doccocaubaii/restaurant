import { Moment } from 'moment';

export interface ISaBillCreated {
  id?: string;

  companyID?: string;

  invoiceTemplate?: string;

  invoiceSeries?: string;

  invoiceNo?: string;

  invoiceDate?: Moment | any;

  accountingObjectName?: string;

  totalSabill?: number;

  checkStatus?: boolean;
}

export class SaBillCreated implements ISaBillCreated {
  constructor(
    public id?: string,
    public companyID?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public accountingObjectName?: string,
    public totalSabill?: number,
    public checkStatus?: boolean
  ) {
    this.checkStatus = this.checkStatus || false;
  }
}
