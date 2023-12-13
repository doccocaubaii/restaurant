export interface Irecord {
  id?: string;
  typeID?: number;
  success?: boolean;
  msg?: string;
  repositoryLedgerID?: string;
  answer?: boolean;
  companyId?: string;
  companyID?: any;
  postedDate?: any;
  date?: any;
  noFBook?: any;
  noMBook?: any;
}

export class Record implements Irecord {
  constructor(
    public id?: string,
    public typeID?: number,
    public success?: boolean,
    public msg?: string,
    public repositoryLedgerID?: string,
    public answer?: boolean,
    public companyId?: string,
    public companyID?: any,
    public date?: any,
    public postedDate?: any,
    public noFBook?: any,
    public noMBook?: any
  ) {
    this.success = this.success || false;
  }
}
