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
}

export enum DeliveryType {
  AT_TABLE = 1,
  GET_AWAY = 2,
  SHIPPING = 3,
}

export const STATUS_ORDER = [
  {
    id: 0,
    name: 'Chưa hoàn thành',
  },
  {
    id: 1,
    name: 'Đã hoàn thành',
  },
  {
    id: 2,
    name: 'Đã hủy',
  },
];

export class PaymentMethod {
  public static cash = 'Tiền mặt';
  public static transfer = 'Chuyển khoản';
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

export const LIST_STATISTICS = [
  {
    id: 1,
    name: 'Theo tháng',
  },
  {
    id: 2,
    name: 'Theo ngày',
  },
  {
    id: 3,
    name: 'Theo giờ',
  },
];

export const CUSTOMER_ORDER = '/pos/ban-hang/';
export const LIST_PRODUCT = '/pos/san-pham/';
