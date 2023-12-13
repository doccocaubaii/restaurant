import { Moment } from 'moment';

export interface ISearchVoucher {
  id?: any;
  typeID?: number /*Loại chứng từ*/;
  statusRecorded?: boolean;
  currencyID?: string;
  accountingObjectID?: string;
  employeeID?: string;
  fromDate?: Moment | any;
  toDate?: Moment | any;
  textSearch?: string;
  invoiceTemplate?: string;
  invoiceSeries?: string;
  status?: number;
  statusSendMail?: number;
  isShowSearch?: boolean;
  // vị trí của chứng từ
  rowNum?: number;
  totalItem?: any;
}

export class SearchVoucher implements ISearchVoucher {
  constructor(
    public id?: any,
    public typeID?: number,
    public statusRecorded?: boolean,
    public currencyID?: string,
    public accountingObjectID?: string,
    public employeeID?: string,
    public fromDate?: Moment | any,
    public toDate?: Moment | any,
    public textSearch?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public status?: number,
    public statusSendMail?: number,
    public rowNum?: number,
    public isShowSearch?: boolean,
    public totalItem?: any
  ) {}
}
