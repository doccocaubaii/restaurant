import { Moment } from 'moment';
import { IPPDiscountReturnDetails } from 'app/shared/model/pp-discount-return-details.model';
import { IMaterialGoods } from 'app/shared/model/material-goods.model';
import { Unit } from 'app/shared/model/unit.model';
import { IAccountingObject } from 'app/shared/model/accounting-object.model';
import { IAccountingObjectBankAccount } from 'app/shared/model/accounting-object-bank-account.model';

export interface IPPDiscountReturn {
  id?: any;
  companyID?: string;
  branchID?: string;
  typeID?: number;
  date?: Moment | any;
  postedDate?: Moment | any;
  typeLedger?: number;
  noFBook?: string;
  noMBook?: string;
  rsInwardOutwardID?: string;
  accountingObjectID?: string;
  accountingObjectName?: string;
  accountingObjectAddress?: string;
  accountingObjectBankAccount?: any;
  accountingObjectBankName?: string;
  accountingObjectType?: number;
  accountingObject?: IAccountingObject;
  companyTaxCode?: string;
  reason?: string;
  invoiceTypeID?: string;
  invoiceTemplate?: string;
  paymentMethod?: string;
  invoiceDate?: Moment | any;
  invoiceNo?: string;
  invoiceSeries?: string;
  currencyID?: string;
  exchangeRate?: number;
  transportMethodID?: string;
  dueDate?: Moment | any;
  paymentClauseID?: string;
  employeeID?: string;
  isAttachList?: boolean;
  listNo?: string;
  listDate?: Moment | any;
  listCommonNameInventory?: string;
  totalAmount?: number;
  totalAmountOriginal?: number;
  totalVATAmount?: number;
  totalVATAmountOriginal?: number;
  totalDiscountAmount?: number;
  totalDiscountAmounOriginal?: number;
  totalDiscountAmountReturn?: number;
  totalDiscountAmountOriginalReturn?: number;
  statusInvoice?: number;
  statusSendMail?: number;
  statusConverted?: number;
  dateSendMail?: Moment | any;
  email?: string;
  ikey?: string;
  iKeyAdjust?: string;
  iKeyReplace?: string;
  isBill?: boolean;
  idMIV?: string;
  bill?: boolean;
  templateID?: string;
  recorded?: boolean;
  contactName?: string;
  ppDiscountReturnDetails?: IPPDiscountReturnDetails[];
  numberAttach?: string;
  isDeliveryVoucher?: boolean;
  isExportInvoice?: any;
  invoiceForm?: any;
  noBook?: any;
  saBillInvoiceNo?: string;
  totalSumAmount?: any;
  materialGoodCode?: any;
  repositoryCode?: any;
  typeGroupID?: number;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  exported?: any;
  bankAccountDetailID?: string;
  deliveryVoucher?: boolean;
  paymentVoucherId?: string;
}

export class PPDiscountReturn implements IPPDiscountReturn {
  constructor(
    public id?: any,
    public companyID?: string,
    public branchID?: string,
    public typeID?: number,
    public date?: any,
    public postedDate?: any,
    public typeLedger?: number,
    public noFBook?: string,
    public noMBook?: string,
    public rsInwardOutwardID?: string,
    public accountingObjectID?: string,
    public accountingObjectName?: string,
    public accountingObjectAddress?: string,
    public accountingObjectBankAccount?: any,
    public accountingObjectBankName?: string,
    public companyTaxCode?: string,
    public reason?: string,
    public invoiceTypeID?: string,
    public invoiceTemplate?: string,
    public paymentMethod?: string,
    public invoiceDate?: Moment | any,
    public invoiceNo?: string,
    public invoiceSeries?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public transportMethodID?: string,
    public dueDate?: Moment | any,
    public paymentClauseID?: string,
    public employeeID?: string,
    public isAttachList?: boolean,
    public listNo?: string,
    public listDate?: Moment | any,
    public listCommonNameInventory?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public totalDiscountAmount?: number,
    public totalDiscountAmounOriginal?: number,
    public totalDiscountAmountReturn?: number,
    public totalDiscountAmountOriginalReturn?: number,
    public statusInvoice?: number,
    public statusSendMail?: number,
    public statusConverted?: number,
    public dateSendMail?: Moment | any,
    public email?: string,
    public idAdjustInv?: string,
    public idReplaceInv?: string,
    public isBill?: boolean,
    public idMIV?: string,
    public templateID?: string,
    public recorded?: boolean,
    public contactName?: string,
    public ppDiscountReturnDetails?: IPPDiscountReturnDetails[],
    public numberAttach?: any,
    public isDeliveryVoucher?: boolean,
    public isExportInvoice?: any,
    public invoiceForm?: any,
    public noBook?: any,
    public saBillInvoiceNo?: string,
    public totalSumAmount?: any,
    public attachList?: any,
    public materialGoodCode?: any,
    public repositoryCode?: any,
    public typeGroupID?: number,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public exported?: any
  ) {
    this.isAttachList = this.isAttachList || false;
    this.isBill = this.isBill || false;
    this.recorded = this.recorded || false;
  }
}
