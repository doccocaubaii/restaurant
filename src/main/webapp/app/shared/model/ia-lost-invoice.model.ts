import { Moment } from 'moment';
import { IaLostInvoiceDetails } from 'app/shared/model/ia-lost-invoice-details.model';

export interface IIaLostInvoice {
  id?: string;
  companyId?: string;
  date?: Moment | any;
  dateLost?: Moment | any;
  no?: string;
  typeId?: number;
  auditDate?: Moment | any;
  getDetectionObject?: string;
  respresentationInLaw?: string;
  status?: number;
  reason?: string;
  attachFileName?: string;
  attachFileContent?: any;
  iaLostInvoiceDetails?: IaLostInvoiceDetails[];
  exchangeRate?: number;
  currencyID?: string;
  description?: string;
  fromNo?: string;
  toNo?: string;
  quantity?: number;
}

export class IaLostInvoice implements IIaLostInvoice {
  constructor(
    public id?: string,
    public companyId?: string,
    public date?: Moment | any,
    public dateLost?: Moment | any,
    public auditDate?: Moment | any,
    public no?: string,
    public typeId?: number,
    public getDetectionObject?: string,
    public respresentationInLaw?: string,
    public status?: number,
    public reason?: string,
    public attachFileName?: string,
    public attachFileContent?: any,
    public iaLostInvoiceDetails?: IaLostInvoiceDetails[],
    public exchangeRate?: number,
    public currencyID?: string,
    public description?: string,
    public fromNo?: string,
    public toNo?: string,
    public quantity?: number
  ) {}
}
