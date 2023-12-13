import { Moment } from 'moment';

export interface ISysLog {
  id?: number;
  companyID?: string;
  userID?: number;
  userName?: string;
  computerName?: string;
  computerMAC?: string;
  computerIP?: string;
  time?: Moment | any;
  workName?: string;
  typeID?: number;
  action?: string;
  reference?: string;
  refID?: string;
  description?: string;
  typeLedger?: number;
  oldData?: string;
  noFBook?: string;
  noMBook?: string;
  refID2?: string;
  typeGroupID?: string;
  descriptionProcessed?: string;
  currentBook?: number;
  url?: string;
  lastCurrentBook?: number;
  description2?: string;
}

export class SysLog implements ISysLog {
  constructor(
    public id?: number,
    public companyID?: string,
    public userID?: number,
    public userName?: string,
    public computerName?: string,
    public computerMAC?: string,
    public computerIP?: string,
    public time?: Moment | any,
    public workName?: string,
    public typeID?: number,
    public action?: string,
    public reference?: string,
    public refID?: string,
    public description?: string,
    public typeLedger?: number,
    public oldData?: string,
    public noFBook?: string,
    public noMBook?: string,
    public refID2?: string,
    public typeGroupID?: string,
    public descriptionProcessed?: string,
    public currentBook?: number,
    public url?: string,
    public lastCurrentBook?: number,
    public description2?: string
  ) {}
}
