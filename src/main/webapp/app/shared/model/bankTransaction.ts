import { IAccountingObject } from './accounting-object.model';
import { ICurrency } from './currency.model';

export interface IBankTransaction {
  createdDate?: any | null;
  dateForConvert?: any | null;
  bankAccountNumber?: any | null;
  modifiedDate?: Date | null;
  accountingObjectName?: string | null;
  accountingObject?: IAccountingObject | null;
  bankAccountId?: any | null;
  bankName?: string | null;
  companyId?: any | null;
  creditAmount?: any | null;
  currency?: string | null;
  objectCurrency?: ICurrency | null;
  debitAmount?: any | null;
  description?: string | null;
  id?: any;
  reflink?: string | null;
  surplus?: any | null;
  transactionNumber?: string | null;
  noFbook?: string | null;
  noMbook?: string | null;
  showNoBook?: string | null;
}
export class BankTransaction implements IBankTransaction {}
