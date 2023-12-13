import { Moment } from 'moment';

export interface ICPExpenseTranferDetails {
  id?: string;
  cPExpenseTranferID?: string;
  description?: string;
  debitAccount?: string;
  creditAccount?: string;
  amount?: number;
  amountOriginal?: number;
  costSetID?: string;
  costSetName?: string;
  contractID?: string;
  signDate?: Moment | any;
  statisticsCodeID?: string;
  expenseItemID?: string;
  orderPriority?: number;
  no?: string;
  isUnreasonableCost?: boolean;
}

export class CPExpenseTranferDetails implements ICPExpenseTranferDetails {
  constructor(
    public id?: string,
    public cPExpenseTranferID?: string,
    public description?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public amount?: number,
    public amountOriginal?: number,
    public costSetID?: string,
    public costSetName?: string,
    public contractID?: string,
    public signDate?: Moment | any,
    public statisticsCodeID?: string,
    public expenseItemID?: string,
    public orderPriority?: number,
    public no?: string,
    public isUnreasonableCost?: boolean
  ) {}
}
