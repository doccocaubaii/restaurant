export interface ITM01TAINDetails {
  id?: string;
  tM01TAINID?: number;
  type?: number;
  materialGoodsResourceTaxGroupID?: string;
  materialGoodsResourceTaxGroupName?: string;
  unitID?: string;
  unitName?: string;
  quantity?: number;
  unitPrice?: number;
  taxRate?: number;
  resourceTaxTaxAmountUnitID?: number;
  resourceTaxAmountIncurration?: number;
  resourceTaxAmountDeduction?: number;
  resourceTaxAmount?: number;
  orderPriority?: number;
}

export class TM01TAINDetails implements ITM01TAINDetails {
  constructor(
    public id?: string,
    public tM01TAINID?: number,
    public type?: number,
    public materialGoodsResourceTaxGroupID?: string,
    public materialGoodsResourceTaxGroupName?: string,
    public unitID?: string,
    public unitName?: string,
    public quantity?: number,
    public unitPrice?: number,
    public taxRate?: number,
    public resourceTaxTaxAmountUnitID?: number,
    public resourceTaxAmountIncurration?: number,
    public resourceTaxAmountDeduction?: number,
    public resourceTaxAmount?: number,
    public orderPriority?: number
  ) {}
}
