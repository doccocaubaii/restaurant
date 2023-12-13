export interface ITIAllocationDetails {
  id?: number;
  tiAllocationID?: string;
  toolsID?: string;
  toolsName?: string;
  toolsCode?: any;
  description?: string;
  totalAllocationAmount?: number;
  totalAllocationAmountSave?: number;
  allocationAmount?: number;
  allocationAmountSave?: number;
  remainingAmount?: number;
  orderPriority?: number;
  tiAllocationAllocateds?: any;
  tiAllocationPosts?: any;
  toolsDetailsConvertDTOS?: any;
  allocationAwaitAccount?: any;
  customField1?: any;
  customField2?: any;
  customField3?: any;
  customField4?: any;
  customField5?: any;
}

export class TIAllocationDetails implements ITIAllocationDetails {
  constructor(
    public id?: number,
    public tiAllocationID?: string,
    public toolsID?: string,
    public toolsCode?: any,
    public toolsName?: string,
    public description?: string,
    public totalAllocationAmount?: number,
    public totalAllocationAmountSave?: number,
    public allocationAmount?: number,
    public allocationAmountSave?: number,
    public remainingAmount?: number,
    public orderPriority?: number,
    public tiAllocationAllocateds?: any,
    public tiAllocationPosts?: any,
    public toolsDetailsConvertDTOS?: any,
    public allocationAwaitAccount?: any,
    public customField1?: any,
    public customField2?: any,
    public customField3?: any,
    public customField4?: any,
    public customField5?: any
  ) {}
}
