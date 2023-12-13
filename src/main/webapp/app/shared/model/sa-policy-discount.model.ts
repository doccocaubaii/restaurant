export interface ISAPolicyDiscount {
  id?: number;
  sAPolicyPriceSettingID?: number;
  salePriceGroupID?: number;
  discountType?: number;
  amountAdjust?: number;
}

export class SAPolicyDiscount implements ISAPolicyDiscount {
  constructor(
    public id?: number,
    public sAPolicyPriceSettingID?: number,
    public salePriceGroupID?: number,
    public discountType?: number,
    public amountAdjust?: number
  ) {}
}
