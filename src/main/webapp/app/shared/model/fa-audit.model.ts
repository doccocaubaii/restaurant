import { Moment } from 'moment';
import { IFAAuditDetail } from 'app/shared/model/fa-audit-detail.model';
import { IFAAuditMemberDetail } from 'app/shared/model/fa-audit-member-detail.model';

export interface IFAAudit {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  noBook?: string;
  date?: any;
  description?: string;
  inventoryDate?: Moment | any;
  summary?: string;
  templateID?: string;
  faAuditDetails?: IFAAuditDetail[];
  faAuditMemberDetails?: IFAAuditMemberDetail[];
  viewVouchers?: any[];
  fixedAssetIDs?: any[];
}

export class FAAudit implements IFAAudit {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public noBook?: string,
    public date?: any,
    public description?: string,
    public inventoryDate?: Moment | any,
    public summary?: string,
    public templateID?: string,
    public faAuditDetails?: IFAAuditDetail[],
    public viewVouchers?: any[],
    public faAuditMemberDetails?: IFAAuditMemberDetail[],
    public fixedAssetIDs?: any[]
  ) {}
}
