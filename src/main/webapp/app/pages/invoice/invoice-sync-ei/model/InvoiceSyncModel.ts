export class InvoiceSyncRequest {
  page: number;
  pagesize: number;
  pattern?: string;
  listStatus: any[] = [];
  status: any;
  fromDate?: string;
  toDate?: string;
  tctCheckStatus: any;
}

export class InvoiceSyncResponse {
  page: number;
  pagesize: number;
  totalRecords: number;
  data: InvoiceSyncResponseItem[];
}

export class InvoiceSyncResponseItem {
  id: number;
  Id: number;
  Pattern?: string;
  pattern?: string;
  InvoiceNo: number;
  invoiceNo: number;
  ArisingDate?: string;
  arisingDate?: string;
  CusName?: string;
  cusName?: string;
  Buyer?: string;
  buyer?: string;
  Amount?: string;
  amount?: string;
  Status: number;
  status: number;
  TCTCheckStatus: number;
  tctcheckStatus: number;
  check: boolean;
  errorMessage: string;
  isExists: boolean;
  Type: number;
}

export class InvoiceSyncSaveDataResponse {
  countSuccess: number;
  countFail: number;
  countTotal: number;
  dataErrors: InvoiceSyncResponseItem[];
}
