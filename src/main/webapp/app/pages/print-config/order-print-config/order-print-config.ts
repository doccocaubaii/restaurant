export class OrderPrintConfig {
  company: string;
  hotline: string;
  local: string;
  process: string;
  no: string;
  dateTime: string;
  taxCode: string;
  customer: string;
  provisonal: string;
  discount: string;
  totalAmount: string;
  totalTax: string;
  totalPreTax: string;
  customerMustPay: string;
  customerPayment: string;
  extraMoney: string;
  pamentMethod: string;
  searchCode: string;
  url: string;
  description: string;
  listProduct: ListProduct[] = [];
}

export class ListProduct {
  productName: string;
  productSale: string;
  quantity: string;
  amount: string;
}
