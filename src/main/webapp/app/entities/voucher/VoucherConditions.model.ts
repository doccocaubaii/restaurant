export interface VoucherConditionsModel {
  discountType: number | 0;
  discountValue: any;
  discountPercent: any;
  minValue: any;
  maxValue: any;
  getQuantity: any;
  getProductProductUnitId: any[];
  getProductProductUnitIdView: any[];
  getProductGroupId: any[];
  getProductGroupIdView: any[];
  conditionGetContent: any;
  conditionGetType: number;
  buyQuantity: any;
  buyProductProductUnitId: any[];
  buyProductProductUnitIdView: any[];
  buyProductGroupId: any[];
  buyProductGroupIdView: any[];
  conditionBuyContent: any;
  conditionBuyType: number;
  desc: string;
}
