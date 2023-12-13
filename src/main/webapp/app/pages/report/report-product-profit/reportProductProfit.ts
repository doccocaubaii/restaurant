export class SearchReportProductProfitReq {
  comId?: number;
  fromDate?: string;
  toDate?: string;
  type?: number;
  isPaging: boolean;
  page: number;
  size: number;
  productProductUnitIds?: string;
  sortType: any;
  getAll: boolean;
  keywordUnit: string;
  keywordName: string;
}

export class ReportProductProfitRes {
  comId?: number;
  fromDate?: string;
  toDate?: string;
  totalQuantity?: number;
  totalRevenue?: number;
  totalProfit?: number;
  detail: Detail[] = [];
}

export class Detail {
  id?: number;
  name?: string;
  quantity?: number;
  revenue?: number;
  profit?: number;
  unitName?: any;
}
