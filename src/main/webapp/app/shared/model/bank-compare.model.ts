import { Moment } from 'moment';

export interface IBankCompare {
  refID?: string;
  detailID?: string;
  postedDate?: string;
  date?: string;
  accountingObjectID?: string;
  reason?: string;
  noFBook?: string;
  noMBook?: string;
  isMatch?: boolean;
  matchDate?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  typeVoucher?: number;
  checked?: boolean;
  typeID?: number;
  typeGroupID?: number;
  bankAccountDetailID?: string;
  bankName?: string;
}

export class BankCompare implements IBankCompare {
  constructor(
    public refID?: string,
    public detailID?: string,
    public postedDate?: string,
    public date?: string,
    public accountingObjectID?: string,
    public reason?: string,
    public noFBook?: string,
    public noMBook?: string,
    public isMatch?: boolean,
    public matchDate?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public typeVoucher?: number,
    public checked?: boolean,
    public typeID?: number,
    public typeGroupID?: number,
    public bankAccountDetailID?: string,
    public bankName?: string
  ) {}
}
