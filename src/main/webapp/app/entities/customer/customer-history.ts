import dayjs from 'dayjs/esm';

export interface CustomerCardHistory {
  id: any;
  cardName: any;
  usageDate: dayjs.Dayjs;
  typeName: any;
  billCode: any;
  amount: any;
  point: any;
  description: string;
}

export interface CustomerBillHistory {
  id: number;
  code: string;
  billDate: string;
  totalAmount: any;
  status: number;
  paymentMethod: string;
  updater: string;
}

export interface ReceivableBillHistory {
  id: number;
  code: string;
  type: number;
  description: string;
  date: dayjs.Dayjs;
  amount: any;
  receivable: number;
  creator: string;
}
