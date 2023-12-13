export interface IContextMenu {
  data?: any;
  selectedData?: any;
  x?: any;
  y?: any;
  isShow?: any;
  isNew?: boolean;
  isDelete?: boolean;
  isCopy?: boolean;
  event?: any;
  checkTag?: number;
  select?: number;
  isCopyAnnotation?: boolean;
  isCopyAnnotationHorizontal?: boolean;
  isMaterialGoodsSpecification?: boolean;
  isMaterialGoodsSpecificationSelect?: boolean;
  isCheckInventory?: boolean;
  inventoryInfo?: any;
  index?: any;
  isMultiDelete?: boolean;
  isMultiCopy?: boolean;
  checkReserveFund?: boolean;
  dataCheckReserveFund?: any;
  selectedField?: any;
}

export class ContextMenu implements IContextMenu {
  constructor(
    public data?: any,
    public selectedData?: any,
    public x?: any,
    public y?: any,
    public isShow?: any,
    public isNew?: boolean,
    public isDelete?: boolean,
    public isCopy?: boolean,
    public event?: any,
    public customActionAddNewRow?: any,
    public customActionDeleteRow?: any,
    public checkTag?: number,
    public select?: number,
    public isCopyAnnotation?: boolean,
    public isCopyAnnotationHorizontal?: boolean,
    public isMaterialGoodsSpecification?: boolean,
    public isMaterialGoodsSpecificationSelect?: boolean,
    public isCheckInventory?: boolean,
    public inventoryInfo?: any,
    public index?: any,
    public isMultiDelete?: boolean,
    public isMultiCopy?: boolean,
    public checkReserveFund?: boolean,
    public dataCheckReserveFund?: any,
    public selectedField?: any
  ) {
    this.x = 0;
    this.y = 0;
    this.isShow = false;
    this.isNew = true;
    this.isDelete = true;
  }
}
