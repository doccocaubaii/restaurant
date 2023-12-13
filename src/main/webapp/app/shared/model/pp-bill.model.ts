import { Moment } from 'moment';
import { AccountingObject } from 'app/shared/model//accounting-object.model';
import { IInvoiceType } from 'app/shared/model/invoice-type.model';
import { ISaBillDetails } from 'app/shared/model//sa-bill-details.model';
import { IPpBillDetails, PPBillDetailEasyInDTO } from 'app/shared/model/pp-bill-details.model';
import { IUnit } from 'app/shared/model/unit.model';

export interface IPPBill {
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
  ppBillDetails?: IPpBillDetails[];
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
  iDReplaceInv?: string;
  iDAdjustInv?: string;
  accountingObjectId?: string;
  companyID?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
  invKey?: string;
  checked?: boolean;
  used?: boolean;
  voucherID?: string;
  voucherType?: number;
  voucherNo?: string;
}

export class PPBill implements IPPBill {
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
    public accountingObjectID?: any,
    public invoiceTypeID?: string,
    public templateID?: string,
    public ppBillDetails?: IPpBillDetails[],
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
    public iDReplaceInv?: string,
    public iDAdjustInv?: string,
    public accountingObjectId?: string,
    public companyID?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string,
    public invKey?: string,
    public checked?: boolean,
    public used?: boolean,
    public voucherID?: string,
    public voucherType?: number,
    public voucherNo?: string
  ) {
    this.isAttachList = this.isAttachList || false;
  }
}

export class PPBillEasyInDTO {
  constructor(
    public id?: string,
    public companyID?: string,
    public typeID?: number,
    public typeLedger?: number,
    public invKey?: string,
    public invoiceForm?: any,
    public invoiceTemplate?: string,
    public invoiceSeries?: string,
    public invoiceNo?: string,
    public invoiceDate?: Moment | any,
    public paymentMethod?: string,
    public currencyID?: string,
    public exchangeRate?: number,
    public accountingObjectID?: string,
    public accountingObjectCode?: string,
    public accountingObjectName?: string,
    public accountingObjectNameEB?: string,
    public accountingObjectAddress?: string,
    public companyTaxCode?: string,
    public reason?: string,
    public totalAmount?: number,
    public totalAmountOriginal?: number,
    public totalVATAmount?: number,
    public totalVATAmountOriginal?: number,
    public totalDiscountAmount?: number,
    public totalDiscountAmountOriginal?: number,
    public checked?: boolean,
    public index?: number,
    public ppBillDetailEasyInDTOS?: PPBillDetailEasyInDTO[]
  ) {}
}

export class SynchronizeDataDTO {
  constructor(
    public idEI?: string,
    public codeEI?: string,
    public nameEI?: string,
    public unitNameEI?: string,
    public id?: string,
    public code?: string,
    public name?: string,
    public nature?: number,
    public units?: IUnit[],
    public unitID?: string,
    public mainUnitID?: string,
    public type?: number,
    public typeU?: number,
    public codeNew?: string,
    public nameNew?: string,
    public natureNew?: number,
    public unitIDNew?: string,
    public unitNameNew?: string,
    public status?: string,
    public statusU?: string,
    public category?: string,
    public index?: number
  ) {}
}

export class PPBillCheckDuplicateDTO {
  constructor(
    public voucherID?: string,
    public taxCode?: string,
    public accountingObjectID?: string,
    public invoiceNo?: string,
    public invoiceDate?: string,
    public invoiceTemplate?: string,
    public invoiceSeries?: string
  ) {}
}
