export interface ISAPolicySalePriceCustomer {
  id?: string;
  sAPolicySalePriceGroupID?: string;
  accountingObjectID?: string;
}

export class SAPolicySalePriceCustomer implements ISAPolicySalePriceCustomer {
  constructor(public id?: string, public sAPolicySalePriceGroupID?: string, public accountingObjectID?: string) {}
}
