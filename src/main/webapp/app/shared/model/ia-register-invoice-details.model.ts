import { IIAReport } from './ia-report.model';

export interface IIARegisterInvoiceDetails {
  id?: number;
  iaRegisterInvoiceID?: string;
  iaReportID?: string;
  purpose?: string;
  iaReport?: IIAReport;
}

export class IARegisterInvoiceDetails implements IIARegisterInvoiceDetails {
  constructor(
    public id?: number,
    public iaRegisterInvoiceID?: string,
    public iaReportID?: string,
    public purpose?: string,
    public iaReport?: IIAReport
  ) {}
}
