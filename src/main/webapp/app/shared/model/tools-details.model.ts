import { IExpenseItem } from 'app/shared/model/expense-item.model';
import { ICostSetDepartment } from 'app/shared/model/cost-set-department.model';

export interface IToolsDetails {
  id?: number;
  toolsID?: string;
  objectID?: string;
  objectType?: number;
  quantity?: number;
  rate?: number;
  costAccount?: string;
  // expenseItemID?: string;
  statisticsCodeID?: string;
  orderPriority?: number;
  toolsId?: number;
  expenseItem?: IExpenseItem;
  prepaidExpenseCodeItem?: any;
  checkDuplicate?: boolean;
}

export class ToolsDetails implements IToolsDetails {
  constructor(
    public id?: number,
    public toolsID?: string,
    public objectID?: string,
    public objectType?: number,
    public quantity?: number,
    public rate?: number,
    public costAccount?: string,
    // public expenseItemID?: string,
    public statisticsCodeID?: string,
    public orderPriority?: number,
    // public toolsId?: number,
    public expenseItem?: IExpenseItem,
    public prepaidExpenseCodeItem?: any,
    public checkDuplicate?: boolean
  ) {}
}
