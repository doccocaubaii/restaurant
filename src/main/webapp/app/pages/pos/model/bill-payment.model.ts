import dayjs from 'dayjs/esm';

export class IBillPayment {
  id!: number;
  comId!: number;
  code: string;
  customerName!: string;
  billDate!: dayjs.Dayjs;
  deliveryType!: number;
  quantity!: number;
  amount!: number;
  totalAmount!: number;
  status!: number;
  payment!: Payment;
  products!: ProductBill[];
  tableId!: number;
}

export class ProductBill {
  productId!: number;
  imageUrl!: string;
  productProductUnitId!: number;
  productName!: string;
  quantity!: number;
  unit!: string;
  unitPrice!: number;
  amount!: number;
  totalAmount!: number;
  position: number | undefined;
  productCode!: string;
}

export class Payment {
  paymentMethod!: string;
  amount!: number;
}

export class CancelOrder {
  billId: number;
  billCode: string;
}

export class CompleteOrder {
  billId: number;
  billCode: string;
  paymentMethod: string;
  paymentTime: dayjs.Dayjs;
  amount: number;
  refund?: number;
}
