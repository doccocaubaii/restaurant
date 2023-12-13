import dayjs from 'dayjs/esm';
import { ITopping } from './product.model';
export class IBillPayment {
  id!: number;
  comId!: number;
  areaId!: number;
  areaName?: string;
  code: string;
  code2!: string;
  areaUnitId!: number;
  areaUnitName?: string;
  reservationId!: number;
  customerId!: number;
  customerName!: string;
  taxAuthorityCode!: string;
  billDate!: dayjs.Dayjs;
  deliveryType!: number;
  quantity!: number;
  amount!: number;
  discountAmount!: number;
  totalPreTax!: number;
  vatRate!: number;
  vatAmount!: number;
  totalAmount!: number;
  status!: number;
  typeInv!: number;
  productDiscountAmount!: number;
  productTaxAmount!: number;
  payment!: Payment;
  products: ProductBill[] = [];
  typeDiscount?: string;
  discountPercent?: number;
  countProduct: number;
  description: string;
  vatRateName: string;
  checkboxVatRateDiscountProduct: boolean;
  discountVatRate?: number | null;
  discountVatAmount?: number | null;
  vatRateDiscountProductName: string;
  haveDiscountVat?: boolean;
  buyerName: string;
  statusInvoice!: number;
  pointBalanceCustomer: number;
  moneyBalanceCustomer: number;
  cardCustomerInfo: any;
  statusPaymentBycard: boolean;
  voucherAmount: number;
  vouchers: Voucher[];
  totalAmountSPDV: number;
  checkSPDV: boolean;
  extraConfig: any;
  listVoucher: any[];
  voucherReturns: any[];
}

export class ProductBill {
  id: number;
  productId: number;
  imageUrl!: string;
  productProductUnitId!: number;
  productName!: string;
  productCode!: string;
  quantity!: number;
  unit!: string;
  unitId!: number;
  unitPrice!: number;
  amount!: number;
  discountAmount = 0;
  typeDiscount?: string;
  discountPercent?: number;
  totalPreTax!: number;
  vatRate!: number;
  vatRateName!: string;
  vatAmount!: number;
  totalAmount!: number;
  quantityProduct: number;
  inventoryTracking: boolean;
  feature!: number | undefined;
  position: number | undefined;
  toppings: ProductBill[];
  displayVatAmount: number;
  displayAmount: number;
  displayTotalAmount: number;
  displayQuantity: number;
  totalAmountTopping: number;
  haveTopping: boolean;
  productUniqueId: string;
  productToppingName: string;
  discountVatRate: number;
  totalDiscount: number;
  promoValid: boolean;
  productVoucher: any;
  promoPercent: number;
  promoValue: number;
  parentProductId: number;
  isTopping: boolean;
  voucherProducts: any[];
  notifiedQuantity: number;
  processingQuantity: number;
  processedQuantity: number;
  canceledQuantity: number;
  quantityInitial: number;
  deliveredQuantity: number;
  returnQuantity: number;
  productNameCustom: string;
  inputWidth: number;
}

export class Voucher {
  id: number;
  code: string;
  voucherValue: number;
  voucherPercent: number;
  desc: string;
  type: number;
  minValue: number;
  maxValue: number;
  buyProductGroupId?: number[];
  buyProductProductUnitId?: number[];
  isFixedQuantity?: boolean;
  conditions?: any;
}

export class Payment {
  paymentMethod!: string;
  amount!: number;
  refund!: number | undefined;
  debtType!: number;
  debt!: number | undefined;
  cardAmount: number;
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
  debt?: number;
  cardAmount?: number;
}

export class SavePointInput {
  comId: number;
  customerIds: number[];
  type: number;
  amount: number;
  point: number;
}

export class InvoiceConfiguration {
  comId: number;
  combineVoucherApply: number;
  discountVat: number;
  invDynamicDiscountName: number;
  invoiceMethod: number;
  invoicePattern: string;
  invoiceType: number;
  isBuyer: number;
  overStock: number;
  pushVoucherDiscountInvoice: number;
  taxCode: string;
  taxReductionType: number;
  typeDiscount: number;
  voucherApply: number;
  businessType: number;
  serviceChargeConfig: any[];
  totalAmount: any[];
  displayConfig: any;
}

export class LastCompany {
  address: string;
  createTime: string;
  deviceCode: string;
  deviceName: string;
  discountType: number;
  easyinvoiceAccount: string;
  easyinvoicePass: string;
  easyinvoiceUrl: string;
  id: number;
  invoiceMethod: number;
  invoicePattern: string;
  invoiceType: number;
  name: string;
  ownerId: number;
  ownerName: string;
  phoneNo: string;
  roundScaleAmount: number;
  roundScaleQuantity: number;
  roundScaleUnitPrice: number;
  service: string;
  taxAuthCodePrefix: string;
  taxcode: string;
  updateTime: string;
}
