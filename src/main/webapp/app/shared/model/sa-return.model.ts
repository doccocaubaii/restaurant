import { Moment } from 'moment';
import { IAccountingObject } from 'app/shared/model//accounting-object.model';
import { IInvoiceType } from 'app/shared/model//invoice-type.model';
import { ITemplate } from 'app/shared/model//template.model';
// import { IRsInwardOutward } from 'app/shared/model//rs-inward-outward.model';
import { ISaReturnDetails } from 'app/shared/model/sa-return-details.model';

export interface ISaReturn {
  id?: string;
  companyID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  noFBookInventory?: string;
  noMBookInventory?: string;
  autoOWAmountCal?: number;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  companyTaxCode?: string;
  accountingObjectBankAccount?: string;
  accountingObjectBankName?: string;
  contactName?: string;
  reason?: string;
  numberAttach?: string;
  invoiceTemplate?: string;
  invoiceSeries?: string;
  invoiceNo?: string;
  invoiceDate?: Moment | any;
  paymentMethod?: string;
  currencyID?: string;
  exchangeRate?: number;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalDiscountAmount?: number;
  totalDiscountAmountOriginal?: number;
  totalPaymentAmount?: number;
  totalPaymentAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  totalOWAmount?: number;
  recorded?: boolean;
  statusInvoice?: number;
  statusSendMail?: number;
  statusConverted?: number;
  dateSendMail?: Moment | any;
  email?: string;
  totalExportTaxAmount?: number;
  isBill?: boolean;
  ikey?: string;
  iKeyAdjust?: string;
  iKeyReplace?: string;
  accountingObject?: IAccountingObject;
  accountingObjectID?: string;
  employee?: IAccountingObject;
  invoiceTypeID?: string;
  templateID?: string;
  rsInwardOutwardID?: string;
  saReturnDetails?: ISaReturnDetails[];
  dueDate?: Moment | any;
  invoiceForm?: number;
  isDeliveryVoucher?: boolean;
  isExportInvoice?: boolean;
  total?: number;
  attachList?: boolean;
  listDate?: any;
  listNo?: string;
  listCommonNameInventory?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  pPBillID?: string;
  pPBillDetailID?: string;
  exported?: boolean;
  paymentVoucherId?: string;
  bankAccountDetailID?: string;
  isAttachList?: boolean;
  creditCardID?: string;
}

export class SaReturn implements ISaReturn {
  constructor(
    public id?: string,
    public companyID?: string,
    public typeID?: number,
    public date?: Moment | any,
    public postedDate?: Moment | any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public noFBookInventory?: string,
    public noMBookInventory?: string,
    public autoOWAmountCal?: number,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public accountingObjectBankAccount?: string,
    public accountingObjectBankName?: string,
    public contactName?: string,
    public reason?: string,
    public numberAttach?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public paymentMethod?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalDiscountAmount?: number,
    public totalDiscountAmountOriginal?: number,
    public totalPaymentAmount?: number,
    public totalPaymentAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public totalOWAmount?: number,
    public recorded?: boolean,
    public statusInvoice?: number,
    public statusSendMail?: number,
    public statusConverted?: number,
    public dateSendMail?: Moment | any,
    public email?: string,
    public totalExportTaxAmount?: number,
    public isBill?: boolean,
    public IDAdjustInv?: string,
    public ID_MIV?: string,
    public idMIV?: string,
    public IDReplaceInv?: string,
    public accountingObject?: IAccountingObject,
    public accountingObjectID?: string,
    public employee?: IAccountingObject,
    public invoiceTypeID?: string,
    public templateID?: string,
    public rsInwardOutwardID?: string,
    public saReturnDetails?: ISaReturnDetails[],
    public dueDate?: Moment | any,
    public invoiceForm?: number,
    public total?: number,
    public isDeliveryVoucher?: boolean,
    public isExportInvoice?: boolean,
    public isAttachList?: boolean,
    public listDate?: any,
    public listNo?: string,
    public listCommonNameInventory?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public pPBillID?: string,
    public pPBillDetailID?: string,
    public exported?: boolean
  ) {
    this.recorded = this.recorded || false;
    this.isBill = this.isBill || false;
  }
}
