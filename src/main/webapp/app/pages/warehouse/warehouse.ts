export class FilterProduct {
  page: number;
  size: number | undefined;
  sort?: string;
  groupId?: number;
  keyword?: string;
}
export enum Page {
  PAGE_SIZE = 20,
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
export class WarehouseIntakeReq {
  id: number;
  comId?: number;
  billId?: number;
  type?: number;
  businessType?: any;
  typeDesc?: string;
  date?: string;
  customerName?: string;
  customerId: any;
  quantity?: number;
  amount!: number;
  discountAmount!: number;
  costAmount!: number;
  totalAmount?: number;
  description?: string;
  paymentMethod?: String;
  detail?: DetailIntake[];
}

export class DetailIntake {
  id?: number;
  productId?: number;
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
  getWithPaging?: boolean;
  page: number;
  size: number;
}
