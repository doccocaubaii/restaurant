import { Moment } from 'moment';
import { AccountingObject } from 'app/shared/model//accounting-object.model';
import { IInvoiceType } from 'app/shared/model/invoice-type.model';
import { ISaBillDetails } from 'app/shared/model//sa-bill-details.model';

export interface ISaBill {
  id?: string;
  currencyID?: string;
  exchangeRate?: number;
  typeLedger?: number;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  companyTaxCode?: string;
  accountingObjectBankAccount?: string;
  accountingObjectBankName?: string;
  contactName?: string;
  reason?: string;
  isAttachList?: boolean;
  listNo?: string;
  listDate?: Moment | any;
  listCommonNameInventory?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  invoiceTemplate?: string;
  invoiceSeries?: string;
  invoiceNo?: string;
  invoiceDate?: Moment | any;
  refDateTime?: Moment | any;
  paymentMethod?: string;
  accountingObject?: AccountingObject;
  invoiceTypeID?: string;
  templateID?: string;
  saBillDetails?: ISaBillDetails[];
  viewVouchers?: any[];
  recorded?: boolean;
  documentNo?: string;
  documentDate?: Moment | any;
  documentNote?: string;
  totalDiscountAmount?: number;
  totalDiscountAmountOriginal?: number;
  total?: number;
  totalOriginal?: number;
  checkStatus?: boolean;
  statusInvoice?: number;
  invoiceForm?: any;
  typeID?: number;
  dateSendMail?: Moment | any;
  type?: number;
  iKeyReplace?: string;
  iKeyAdjust?: string;
  iDReplaceInv?: string;
  iDAdjustInv?: string;
  invoiceTypeName?: string;
  invoiceTypeCode?: string;
  accountingObjectID?: string;
  attachList?: boolean;
  companyID?: any;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  cyberCode?: string;
  tCTCheckStatus?: string;
  tCTErrorMessage?: string;
  taxAuthorityCode?: string;
  exported?: any;
  ikey?: string;
  fkey?: string;
}

export class SaBill implements ISaBill {
  constructor(
    public id?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public typeLedger?: number,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public accountingObjectBankAccount?: string,
    public accountingObjectBankName?: string,
    public contactName?: string,
    public reason?: string,
    public isAttachList?: boolean,
    public listNo?: string,
    public listDate?: Moment | any,
    public listCommonNameInventory?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public refDateTime?: Moment | any,
    public paymentMethod?: string,
    public accountingObject?: AccountingObject,
    public invoiceTypeID?: string,
    public templateID?: string,
    public saBillDetails?: ISaBillDetails[],
    public viewVouchers?: any[],
    public recorded?: boolean,
    public documentNo?: string,
    public documentDate?: Moment | any,
    public documentNote?: string,
    public totalDiscountAmount?: number,
    public totalDiscountAmountOriginal?: number,
    public total?: number,
    public totalOriginal?: number,
    public checkStatus?: boolean,
    public statusInvoice?: number,
    public invoiceForm?: any,
    public typeID?: number,
    public dateSendMail?: Moment | any,
    public type?: number,
    public iKeyReplace?: string,
    public accountingObjectID?: string,
    public iKeyAdjust?: string,
    public attachList?: boolean,
    public companyID?: any,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public exported?: any,
    public tCTCheckStatus?: string,
    public tCTErrorMessage?: string,
    public taxAuthorityCode?: string,
    public cyberCode?: string,
    public ikey?: string,
    public fkey?: string
  ) {
    this.isAttachList = this.isAttachList || false;
  }
}
