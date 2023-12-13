export interface ISAPolicyPriceTable {
  id?: string;
  sAPolicyPriceSettingID?: string;
  salePriceGroupID?: string;
  materialGoodsID?: string;
  price?: number;
  lastPrice?: number;
}

export class SAPolicyPriceTable implements ISAPolicyPriceTable {
  constructor(
    public id?: string,
    public sAPolicyPriceSettingID?: string,
    public salePriceGroupID?: string,
    public materialGoodsID?: string,
    public price?: number,
    public lastPrice?: number
  ) {}
}
