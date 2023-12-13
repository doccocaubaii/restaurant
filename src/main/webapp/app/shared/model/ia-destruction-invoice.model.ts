import { Moment } from 'moment';
import { IIADestructionInvoiceDetails } from 'app/shared/model/ia-destruction-invoice-details.model';

export interface IIADestructionInvoice {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  no?: number;
  destructionMethod?: string;
  reason?: string;
  getDetectionObject?: number;
  representationInLaw?: string;
  attachFileName?: string;
  attachFileContent?: any;
  destructionDate?: Moment | any;
  status?: number;
  iADestructionInvoiceDetails?: IIADestructionInvoiceDetails[];
}

export class IADestructionInvoice implements IIADestructionInvoice {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public no?: number,
    public destructionMethod?: string,
    public reason?: string,
    public getDetectionObject?: number,
    public representationInLaw?: string,
    public destructionDate?: Moment | any,
    public attachFileName?: string,
    public attachFileContent?: any,
    public status?: number,
    public iADestructionInvoiceDetails?: IIADestructionInvoiceDetails[]
  ) {}
}
