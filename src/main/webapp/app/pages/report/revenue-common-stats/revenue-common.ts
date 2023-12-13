export class SearchRevenueCommonReq {
  comId?: number;
  fromDate?: string;
  toDate?: string;
  type!: number;
  page: any;
  size: any;
  isPaging?: boolean;
  fromHour?: any;
  toHour?: any;
  isChart?: boolean;
}
export class RevenueCommonRes {
  comId!: number;
  fromDate?: string;
  toDate?: string;
  revenue!: number;
  profit!: number;
  createTime?: string;
  detail: Detail[] = [];
}

export class Detail {
  fromDate?: string;
  toDate?: string;
  revenue!: number;
  profit!: number;
}

export class Parent {
  name?: string;
  series: Child[] = [];
}

export class Child {
  name?: string;
  value: number;
}
