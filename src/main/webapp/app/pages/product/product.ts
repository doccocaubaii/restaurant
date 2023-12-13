export class ProductObjs {
  id?: any;
  comId?: number;
  code2?: string;

  code: string;

  name?: string;
  unit?: string;
  unitId?: number;
  barcode?: string;
  purchasePrice?: number;
  salePrice?: number;
  vatRate?: number;
  inventoryTracking?: boolean;
  inventoryCount?: number;
  description?: string;
  groups: any;
  conversionUnits: ConvertUnit[] = [];
  images?: File;
  imageUrl?: string;
  isTopping?: boolean;
  directSale?: boolean;
  productProductUnitId?: number;
  check?: any;
}

export class ConvertUnit {
  id?: number;
  converRate?: number;
  formula?: number;
  productUnitId?: number;
  purchasePrice?: number;
  salePrice?: number;
  directSale?: boolean;
  unitName?: string;
}
export class Group {
  id!: number;
  productId?: number;
  name?: string;
  description?: string;
}

export class CreateUnit {
  comId?: number;
  unitName?: string;
}

export class FilterProduct {
  page: number;
  size: number;
  sort?: string;
  groupId?: number;
  keyword?: string;
  isCountAll?: boolean;
  isTopping?: boolean;
  ids?: any;
  keywordName?: string;
  keywordUnit?: string;
  isDefault?: boolean;
}
export enum Page {
  PAGE_SIZE = 20,
  PAGE_NUMBER = 1,
  TOTAL_ITEM = 0,
}
export class CreateCategotyReq {
  name?: string;
  description?: string;
  comId?: number;
}
export class FilterTopping {
  page: number;
  size: number;
  comId?: number;
  keyword?: string;
  isCountAll?: boolean;
}

export class ListProductToppingReq {
  id?: number;
  keyword?: string;
  isSingleList?: boolean;
  page: number;
  size: number;
}
