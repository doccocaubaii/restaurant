export class SearchProductSaleReq {
  comId?: number;
  fromDate?: string;
  toDate?: string;
  pattern?: number;
  status!: any;
  taxCheckStatus!: any;
  page: any;
  size: any;
  isPaging?: boolean;
}
