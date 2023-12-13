import { RSAssemblyDismantlementDetails } from 'app/shared/model/rs-assembly-dismantlement-details.model';

export interface IRSAssemblyDismantlement {
  id?: string;
  companyID?: string;
  branchID?: string;
  typeID?: any;
  date?: any;
  no?: string;
  noMBook?: string;
  reason?: string;
  materialGoodsID?: string;
  materialGoodsName?: string;
  unit?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  templateID?: string;
  typeName?: string;
  convertRates?: any;
  totalAmount?: number;
  sumTotalAmount?: number;
  repositoryID?: string;
  rsAssemblyDismantlementDetails?: RSAssemblyDismantlementDetails[];
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  typeLedger?: number;
}

export class RSAssemblyDismantlement implements IRSAssemblyDismantlement {
  constructor(
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public typeID?: any,
    public date?: any,
    public convertRates?: any,
    public no?: string,
    public noMBook?: string,
    public reason?: string,
    public materialGoodsID?: string,
    public repositoryID?: string,
    public materialGoodsName?: string,
    public unit?: string,
    public unitID?: string,
    public quantity?: number,
    public unitPrice?: number,
    public amount?: number,
    public templateID?: string,
    public typeName?: string,
    public totalAmount?: number,
    public sumTotalAmount?: number,
    public rsAssemblyDismantlementDetails?: RSAssemblyDismantlementDetails[],
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public typeLedger?: number
  ) {}
}
