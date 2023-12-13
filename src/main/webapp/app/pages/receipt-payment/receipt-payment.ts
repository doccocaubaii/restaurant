import dayjs from 'dayjs';
import { Dayjs } from 'dayjs/esm';

export class FilterReceiptPayment {
  page: number;
  size: number | undefined;
  sort?: string;
  groupId?: number;
  keyword?: string;
}
export enum Page {
  PAGE_SIZE = 10,
  PAGE_NUMBER = 0,
  TOTAL_ITEM = 0,
}

export class FilterCustomer {
  page!: number;
  size?: number;
  sort?: string;
  type?: number;
  keyword?: string;
}

export class FilterBusiness {
  comId?: number;
  type?: number;
  keyword?: string;
}

export class UpdateTransactionReq {
  id?: number;
  comId?: number;
  type?: number;
  businessType?: number;
  typeDesc?: string;
  customerName?: string;
  customerId?: number;
  amount!: number;
  note?: string;
  date?: string;
}

export class DetailIntake {
  position?: number;
  productName?: string;
  productCode?: string;
  productProductUnitId?: number;
  unitId?: number;
  unitName?: string;
  quantity?: number;
  unitPrice?: number;
  amount?: number;
  discountAmount?: number;
  totalAmount?: number;
  lotNo?: string;
}
export class SearchTransactionReq {
  comId?: number;
  fromDate?: string;
  toDate?: string;
  type?: number;
  keyword?: string;
  isCountAll?: boolean;
  page?: number;
  size?: number;
}
