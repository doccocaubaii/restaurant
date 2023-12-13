export interface ISAPolicyPriceMaterialGoods {
  id?: string;
  sAPolicyPriceSettingID?: string;
  materialGoodsID?: string;
}

export class SAPolicyPriceMaterialGoods implements ISAPolicyPriceMaterialGoods {
  constructor(public id?: string, public sAPolicyPriceSettingID?: string, public materialGoodsID?: string) {}
}
