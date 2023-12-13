import { Moment } from 'moment';
import { IaPublishInvoiceDetails } from 'app/shared/model/ia-publish-invoice-details.model';
import { InvoiceType } from 'app/shared/model/invoice-type.model';
import * as moment from 'moment';

export interface IIADeletedInvoice {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  no?: string;
  reason?: string;
  attachFileName?: string;
  attachFileContent?: any;
  saInvoiceID?: string;
  invoiceType?: InvoiceType;
  invoiceTemplate?: string;
  invoiceSeries?: string;
  invoiceNo?: string;
  invoiceDate?: Moment | any;
  accountingObjectName?: any;
  noBook?: string;
  voucherID?: string;
}

export class IADeletedInvoice implements IIADeletedInvoice {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public no?: string,
    public reason?: string,
    public attachFileName?: string,
    public attachFileContent?: any,
    public saInvoiceID?: string,
    public invoiceType?: InvoiceType,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public accountingObjectName?: string,
    public noBook?: string,
    public voucherID?: string
  ) {}
}
