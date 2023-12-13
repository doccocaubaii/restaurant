export interface ITIAllocationAllocated {
  id?: number;
  tiAllocationID?: string;
  toolsID?: string;
  toolsItem?: any;
  toolsCode?: string;
  toolsName?: string;
  objectID?: string;
  objectType?: number;
  totalAllocationAmount?: number;
  rate?: number;
  allocationAmount?: number;
  costAccount?: string;
  expenseItemID?: string;
  costSetID?: string;
  orderPriority?: number;
  checked?: boolean;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
  allocationAmountSave?: number;
  totalAllocationAmountSave?: number;
}

export class TIAllocationAllocated implements ITIAllocationAllocated {
  constructor(
    public id?: number,
    public tiAllocationID?: string,
    public toolsID?: string,
    public toolsItem?: any,
    public toolsCode?: string,
    public toolsName?: string,
    public objectID?: string,
    public objectType?: number,
    public totalAllocationAmount?: number,
    public rate?: number,
    public allocationAmount?: number,
    public costAccount?: string,
    public expenseItemID?: string,
    public costSetID?: string,
    public orderPriority?: number,
    public checked?: boolean,
    public customField1?: any,
    public customField2?: any,
    public customField3?: any,
    public customField4?: any,
    public customField5?: any,
    public allocationAmountSave?: number,
    public totalAllocationAmountSave?: number
  ) {}
}
