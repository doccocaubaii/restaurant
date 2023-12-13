import { ICurrency } from 'app/shared/model/currency.model';

export class AccountDebitCreditModel {
  constructor(
    public typeId?: any,
    public orderPriority?: number,
    public id?: string,
    public companyID?: string,
    public branchID?: string,
    public accountingObjectCode?: string,
    public accountingObjectName?: string,
    public accountNameGlobal?: string,
    public description?: string,
    public checked?: boolean,
    public duCo?: number,
    public duCoQD?: number,
    public duNo?: number,
    public duNoQD?: number,
    public account?: string,
    public currencyID?: string,
    public currency?: ICurrency,
    public no?: string,
    public accountParent?: string
  ) {}
}
