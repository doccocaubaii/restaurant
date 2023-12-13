import { IExpenseItem } from 'app/shared/model/expense-item.model';

export interface IFAInitDetails {
  id?: string;
  faInitID?: string;
  objectID?: string;
  objectType?: number;
  rate?: any;
  costAccount?: string;
  expenseItemID?: string;
  statisticsCodeID?: string;
  orderPriority?: number;
  prepaidExpenseCodeItem?: any;
  expenseItem?: IExpenseItem;
  expenseItemCode?: string;
}

export class FAInitDetails implements IFAInitDetails {
  constructor(
    public id?: string,
    public faInitID?: string,
    public objectID?: string,
    public objectType?: number,
    public rate?: any,
    public expenseItem?: IExpenseItem,
    public costAccount?: string,
    public expenseItemID?: string,
    public statisticsCodeID?: string,
    public orderPriority?: number,
    public prepaidExpenseCodeItem?: any,
    public expenseItemCode?: string
  ) {}
}
