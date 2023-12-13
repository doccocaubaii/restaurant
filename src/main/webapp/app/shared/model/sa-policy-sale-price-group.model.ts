export interface ISAPolicySalePriceGroup {
  id?: string;
  sAPolicyPriceSettingID?: string;
  salePriceGroupID?: string;
  basedOn?: number;
  method?: number;
  amountAdjust?: number;
}

export class SAPolicySalePriceGroup implements ISAPolicySalePriceGroup {
  constructor(
    public id?: string,
    public sAPolicyPriceSettingID?: string,
    public salePriceGroupID?: string,
    public basedOn?: number,
    public method?: number,
    public amountAdjust?: number
  ) {}
}
