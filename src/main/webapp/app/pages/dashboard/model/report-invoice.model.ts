import dayjs from 'dayjs/esm';

export class ReportInvoiceInput {
  comId: number;
  fromDate: dayjs.Dayjs;
  toDate: dayjs.Dayjs;
  type: number;
}

export class ProductSaleStatusInput {
  comId: number;
  fromDate: string;
  toDate: string;
  pattern?: string;
  status: number;
  taxCheckStatus: number;
}

// export interface ReportInvoiceInput {
//   comId: number,
//   keyword?: string,
//   groupId?: number
// }
