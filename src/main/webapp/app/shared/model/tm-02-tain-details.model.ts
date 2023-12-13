export interface ITM02TAINDetails {
  id?: string;
  tM02TAINID?: string;
  type?: number;
  materialGoodsResourceTaxGroupID?: string;
  materialGoodsResourceTaxGroupName?: string;
  unitID?: string;
  quantity?: number;
  unitPrice?: number;
  taxRate?: number;
  resourceTaxTaxAmountUnitID?: number;
  resourceTaxAmountIncurration?: number;
  resourceTaxAmountDeduction?: number;
  resourceTaxAmount?: number;
  resourceTaxAmountDeclaration?: number;
  differAmount?: number;
  orderPriority?: number;
}

export class TM02TAINDetails implements ITM02TAINDetails {
  constructor(
    public id?: string,
    public tM02TAINID?: string,
    public type?: number,
    public materialGoodsResourceTaxGroupID?: string,
    public materialGoodsResourceTaxGroupName?: string,
    public unitID?: string,
    public quantity?: number,
    public unitPrice?: number,
    public taxRate?: number,
    public resourceTaxTaxAmountUnitID?: number,
    public resourceTaxAmountIncurration?: number,
    public resourceTaxAmountDeduction?: number,
    public resourceTaxAmount?: number,
    public resourceTaxAmountDeclaration?: number,
    public differAmount?: number,
    public orderPriority?: number
  ) {}
}
