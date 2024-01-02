export class IProduct {
  id!: number;
  productProductUnitId?: number;
  images!: string;
  comId!: number;
  code!: string;
  name!: string;
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
