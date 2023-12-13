export interface IRSAssemblyDismantlementDetails {
  id?: string;
  rsAssemblyDismantlementID?: string;
  materialGoodsID?: string;
  description?: string;
  repositoryID?: string;
  unit?: string;
  unitID?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  orderPriority?: number;
  convertRates?: any;
  materialGoodsCode?: string;
  repositoryCode?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class RSAssemblyDismantlementDetails implements IRSAssemblyDismantlementDetails {
  constructor(
    public id?: string,
    public rsAssemblyDismantlementID?: string,
    public materialGoodsID?: string,
    public materialGoodsCode?: string,
    public description?: string,
    public repositoryID?: string,
    public repositoryCode?: string,
    public unit?: string,
    public unitID?: string,
    public quantity?: number,
    public unitPrice?: number,
    public convertRates?: any,
    public amount?: number,
    public orderPriority?: number,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
