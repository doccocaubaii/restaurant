import dayjs from 'dayjs/esm';

export class ReportInvoiceInput {
  comId: number;
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  type: number;
  suggestValue: number;
}

export class ProductSaleStatusInput {
  comId: number;
  fromDate: string;
  toDate: string;
  pattern?: string;
  status: number;
  taxCheckStatus: number;
}

export interface ReportInventoryInput {
  comId: number;
  keyword?: string;
  groupId?: number;
}
