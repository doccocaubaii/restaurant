import { BILL, BILL_ORDER, BILL_ORDER_OFFLINE, PRODUCT } from '../../constants/app.routing.constants';

export enum MenuType {
  ProductGroup = 0,
  ProductUnit = 1,
}

export enum Page {
  PAGE_SIZE = 20,
  PAGE_NUMBER = 0,
  TOTAL_ITEM = 0,
}

export enum StatusOrder {
  NOT_COMPLETE = 0,
  COMPLETE = 1,
  CANCELED = 2,
  RETURNED = 3,
  RETURN = 4,
}

export enum DeliveryType {
  AT_TABLE = 1,
  GET_AWAY = 2,
  SHIPPING = 3,
}

export enum StatusExit {
  CLOSE = 0,
  NOT_SAVE = 1,
  SAVE = 2,
}

export const LIST_VAT = [
  {
    id: 1,
    name: 'Thuế suất 0%',
    shortName: '0%',
    value: 0,
  },
  {
    id: 2,
    name: 'Thuế suất 5%',
    shortName: '5%',
    value: 5,
  },
  {
    id: 3,
    name: 'Thuế suất 8%',
    value: 8,
  },
  {
    id: 4,
    name: 'Thuế suất 10%',
    shortName: '10%',
    value: 10,
  },
  {
    id: 5,
    name: 'KCT',
    shortName: 'KCT',
    value: -1,
  },
  {
    id: 6,
    name: 'KTT',
    shortName: 'KTT',
    value: -2,
  },
  {
    id: 7,
    name: 'Khác',
    shortName: 'Khác',
    value: -3,
  },
];

export const LIST_VAT_RATE_DISCOUNT_PRODUCT_DETAIL = [
  {
    id: 1,
    name: 'Thuế suất 1%',
    value: 1,
  },
  {
    id: 2,
    name: 'Thuế suất 2%',
    value: 2,
  },
  {
    id: 3,
    name: 'Thuế suất 3%',
    value: 3,
  },
  {
    id: 4,
    name: 'Thuế suất 5%',
    value: 5,
  },
];

export const LIST_VAT_RATE_DISCOUNT_PRODUCT = [
  {
    id: 1,
    name: 'Thuế suất 1%',
    value: 1,
  },
  {
    id: 2,
    name: 'Thuế suất 2%',
    value: 2,
  },
  {
    id: 3,
    name: 'Thuế suất 3%',
    value: 3,
  },
  {
    id: 4,
    name: 'Thuế suất 5%',
    value: 5,
  },
  {
    id: 5,
    name: 'Khác',
    value: 0,
  },
];

export const STATUS_ORDER = [
  {
    id: 0,
    name: 'Chưa hoàn thành',
    code: 'DC/DH',
  },
  {
    id: 1,
    name: 'Đã hoàn thành',
    code: 'HT/DH',
  },
  {
    id: 2,
    name: 'Đã hủy',
    code: 'DH/DH',
  },
  {
    id: 4,
    name: 'Trả hàng',
    code: 'TH/DH',
  },
];

export const PAGE_SIZE = [
  {
    id: 10,
    name: '10',
  },
  {
    id: 20,
    name: '20',
  },
  {
    id: 30,
    name: '30',
  },
];

export const TYPE_DISCOUNT = [
  {
    id: 0,
    name: 'Giảm giá trị',
  },
  {
    id: 1,
    name: 'Giảm theo phần trăm',
  },
];

export enum TypeDiscount {
  VALUE = 'Giảm giá trị',
  PERCENT = 'Giảm theo phần trăm',
}

export class PaymentMethod {
  public static cash = 'Tiền mặt';
  public static transfer = 'Chuyển khoản';
  public static card = 'Thẻ';
  public static other = 'Khác';
}

export const NoTaxProduct = {
  id: -4,
  name: 'Không áp dụng thuế',
};

export const CANCEL_ORDER_NOTIFICATION = {
  title: 'Hủy đơn hàng',
  message: 'Bạn có chắc chắn muốn hủy đơn hàng',
};

export const CREATE_ORDER_NOTIFICATION = {
  title: 'Tạo đơn hàng',
  message: 'Bạn có chắc chắn muốn tạo đơn hàng',
};

export const UPDATE_ORDER_NOTIFICATION = {
  title: 'Cập nhật đơn hàng',
  message: 'Bạn có chắc chắn muốn cập nhật đơn hàng',
};

export const STATISTICS = {
  month: 1,
  date: 2,
  hour: 3,
};

export class TypeConvertPoint {
  public static putMoney = 0;
  public static increasePoint = 1;
  public static decreasePoint = 2;
  public static converPoint = 3;
}

export const LIST_STATISTICS = [
  {
    id: 1,
    name: 'Hôm nay',
  },
  {
    id: 2,
    name: 'Tuần này',
  },
  {
    id: 3,
    name: 'Tháng này',
  },
  {
    id: 4,
    name: 'Năm nay',
  },
  {
    id: 5,
    name: 'Hôm qua',
  },
  {
    id: 6,
    name: '7 ngày trước',
  },
  {
    id: 7,
    name: '30 ngày trước',
  },
];

export const LIST_STATISTICS_PRODUCT = [
  {
    id: 0,
    name: 'Theo doanh thu giảm dần',
  },
  {
    id: 1,
    name: 'Theo số lượng giảm dần',
  },
];

export class InvoiceType {
  public static invoiceSale = 0;
  public static invoiceOne = 1;
  public static invoiceMultiple = 2;
}

export const INVOICE_TYPE = [
  {
    id: 0,
    name: 'Hóa đơn bán hàng',
  },
  {
    id: 1,
    name: 'Hóa đơn GTGT một thuế',
  },
  {
    id: 2,
    name: 'Hóa đơn GTGT nhiều thuế',
  },
];

export class InvoiceDiscount {
  public static noDiscount = 3;
  public static productValue = 1;
  public static orderValue = 0;
  public static orderProductValue = 2;
}

export class SPGCProduct {
  public static code = 'SPGC';
  public static feature = 4;
}

export class DiscountVat {
  public static noDiscount = 0;
  public static discount = 1;
}

export class VoucherApply {
  public static noVoucher = 0;
  public static useVoucher = 1;
}

export class ProductNormal {
  public static feature = 1;
}

export class TaxReductionType {
  public static order = 0;
  public static product = 1;
}

export class SPCKProduct {
  public static code = 'SPCK';
  public static feature = 3;
  // public static productNameKM = 'Lấy nội dung của chương trình KM';
}

export class SPVoucherProduct {
  public static code = 'SP1';
  public static feature = 3;
  // public static productNameKM = 'Lấy nội dung của chương trình KM';
}

export class SPKMProduct {
  public static code = 'SPKM';
  public static feature = 2;
}

export class VoucherType {
  public static type300 = 300;
  public static type200 = 200;
  public static type102 = 102;
  public static type100 = 100;
}

export class StatusNotify {
  public static notify = 0;
  public static processing = 2;
  public static cancel = 3;
}

export class SPDVProduct {
  public static code = 'SPDV';
  public static feature = 1;
}

export const INVOICE_DISCOUNT = [
  {
    id: 0,
    name: 'Giảm giá theo đơn hàng',
  },
  {
    id: 1,
    name: 'Giảm giá theo sản phẩm',
  },
  {
    id: 2,
    name: 'Giảm giá theo đơn hàng và sản phẩm',
  },
  {
    id: 3,
    name: 'Không giảm giá',
  },
];

export const CUSTOMER_ORDER = BILL + '/';
export const CUSTOMER_ORDER_OFFLINE = BILL_ORDER_OFFLINE + '/';
export const LIST_PRODUCT = BILL + '/';
