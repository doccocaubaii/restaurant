export class IProduct {
  id!: number;
  productId: number;
  productProductUnitId?: number;
  images!: string;
  productCode: string;
  productName: string;
  name: string;
  comId!: number;
  // code: string;
  code2!: string;
  // name: string;
  unit!: string;
  unitId!: number;
  barcode!: string;
  purchasePrice!: number;
  salePrice!: number;
  vatRate?: number;
  inventoryTracking!: boolean;
  inventoryCount!: number;
  description!: string;
  groups?: Array<number>;
  conversionUnits!: Array<IConversionUnits>;
  imageUrl!: string;
  quantity!: number;
  totalPrice!: number;
  isPrimary: boolean;
  discountVatRate: number;
  isTopping: boolean;
  toppings: ITopping[];
  haveTopping: boolean;
}

export interface IProductResponse {
  barcode: string;
  code: string;
  code2: string;
  comId: number;
  conversionUnits: Array<IConversionUnits>;
  createTime: string;
  description: string;
  groups: Array<number>;
  id: number;
  imageUrl: string;
  inventoryCount: number;
  inventoryTracking: boolean;
  name: string;
  purchasePrice: number;
  salePrice: number;
  unit: string;
  unitId: number;
  updateTime: string;
  vatRate: number;
}

export interface ITopping {
  position: number;
  unitId: number;
  unit: string;
  productProductUnitId: number;
  productCode: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  amount: number;
  discountAmount: number;
  totalPreTax: number;
  vatRate: number;
  vatAmount: number;
  totalAmount: number;
  isTopping: boolean;
  parentProductId: number;
  feature: number;
}

export interface IGroups {
  id: number;
}

export interface IConversionUnits {
  id: number;
  convertRate: number | null;
  formula: number | null;
  purchasePrice?: number | null;
  salePrice: number | null;
  directSale: boolean | null;
  primary?: boolean | null;
}

export type NewIProduct = Omit<IProduct, 'id'> & { id: null };
