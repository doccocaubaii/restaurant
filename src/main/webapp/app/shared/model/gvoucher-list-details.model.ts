import { Moment } from 'moment';

export interface IGvoucherListDetails {
  id?: number;
  gvoucherListId?: string;
  voucherId?: string;
  voucherDetailId?: string;
  voucherTypeId?: number;
  voucherDate?: Moment | any;
  voucherPostedDate?: Moment | any;
  voucherDescription?: string;
  voucherDebitAccount?: string;
  voucherCreditAccount?: string;
  voucherAmount?: number;
  reason?: string;
  generalLedgerId?: string;
  orderPriority?: number;
  noGL?: string;
  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class GvoucherListDetails implements IGvoucherListDetails {
  constructor(
    public id?: number,
    public gvoucherListId?: string,
    public voucherId?: string,
    public voucherDetailId?: string,
    public voucherTypeId?: number,
    public voucherDate?: Moment | any,
    public voucherPostedDate?: Moment | any,
    public voucherDescription?: string,
    public voucherDebitAccount?: string,
    public voucherCreditAccount?: string,
    public voucherAmount?: number,
    public reason?: string,
    public generalLedgerId?: string,
    public orderPriority?: number,
    public noGL?: string,
    public customField1?: string,
    public customField2?: string,
    public customField3?: string,
    public customField4?: string,
    public customField5?: string
  ) {}
}
