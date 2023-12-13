export class ProcessingArea {
  id?: number;
  comId?: number;
  name?: string;
  normalizedName?: string;
  setting?: number;
  printer?: string;
  printType?: string;
  active?: number;
}

export class FilterProcessingArea {
  comId?: number;
  name?: string;
  setting?: number;
  active?: number;
  fromDate?: string;
  toDate?: string;
  size?: any;
  page?: any;
}

export enum Page {
  PAGE_SIZE = 20,
  PAGE_NUMBER = 1,
  TOTAL_ITEM = 0,
  FULL_SIZE = 2000,
}

export class FilterProduct {
  productId?: number;
  comId?: number;
  processingAreaId?: number;
  active?: number;
  productProductUnitId?: number;
  code2?: string;
  productName?: string;
  unit?: string;
  group?: string;
  page: number;
  size: number;
  sort?: string;
  groupId?: number;
  keyword?: string;
  isCountAll?: boolean;
  ids?: any;
  paramCheckAll?: boolean;
}

export class ProcessingAreaDetail {
  id?: number;
  comId?: number;
  name?: string;
  setting?: number;
  active?: number;
  listProduct: any = [];
}

export class FilterProductProductUnitId {
  processingAreaId?: number;
  comId?: number;
}
export class FilterProductProcessingAreas {
  processingAreaId?: number;
  page?: number;
  size?: number;
}
