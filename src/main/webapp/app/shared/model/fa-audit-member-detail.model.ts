import { Moment } from 'moment';
import { ITIAuditDetails } from 'app/shared/model/ti-audit-details.model';

export interface IFAAuditMemberDetail {
  id?: string;
  faAuditID?: string;
  accountObjectName?: string;
  accountingObjectTitle?: string;
  role?: string;
  departmentCode?: string;
  accountingObjectID?: string;
  orderPriority?: number;
}

export class FAAuditMemberDetail implements IFAAuditMemberDetail {
  constructor(
    public id?: string,
    public faAuditID?: string,
    public accountObjectName?: string,
    public accountingObjectTitle?: string,
    public role?: string,
    public departmentCode?: string,
    public accountingObjectID?: string,
    public orderPriority?: number
  ) {}
}
