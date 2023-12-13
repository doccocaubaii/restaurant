export interface ISalePriceGroup {
  id?: any;
  salePiceGroupCode?: string;
  salePriceGroupName?: string;
  description?: string;
  isActive?: boolean;
}

export class SalePriceGroup implements ISalePriceGroup {
  constructor(
    public id?: any,
    public salePiceGroupCode?: string,
    public salePriceGroupName?: string,
    public description?: string,
    public isActive?: boolean
  ) {
    this.isActive = this.isActive || false;
  }
}
