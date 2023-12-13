import { RSProductionOrderDetails } from 'app/shared/model/rs-production-order-details.model';

export interface IRSProductionOrder {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: any;
  date?: any;
  no?: string;
  reason?: string;
  templateID?: string;
  postedDate?: any;
  noFBook?: string;
  noMBook?: string;
  typeLedger?: number;
  totalAmount?: number;
  totalOWAmount?: number;
  accountingObject?: any;
  rsProductionOrderDetails?: RSProductionOrderDetails[];
  materialGoodsSpecificationsLedgers?: any[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  checked?: boolean;
}

export class RsProductionOrder implements IRSProductionOrder {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: any,
    public date?: any,
    public no?: string,
    public reason?: string,
    public templateID?: string,
    public sumTotalAmount?: number,
    public postedDate?: any,
    public noFBook?: string,
    public noMBook?: string,
    public typeLedger?: number,
    public totalAmount?: number,
    public totalOWAmount?: number,
    public accountingObject?: any,
    public rsProductionOrderDetails?: RSProductionOrderDetails[],
    public materialGoodsSpecificationsLedgers?: any[],
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public checked?: boolean
  ) {}
}
