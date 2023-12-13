export interface IUnit {
  id?: string;
  companyID?: string;
  unitName?: string;
  unitDescription?: string;
  isActive?: boolean;
  isVthh?: boolean;
  convertRate?: number;
  formula?: string;
  materialGoodsID?: string;
  checked?: boolean;
  grade?: number;
}

export class Unit implements IUnit {
  constructor(
    public id?: string,
    public companyID?: string,
    public unitName?: string,
    public unitDescription?: string,
    public isActive?: boolean,
    public convertRate?: number,
    public formula?: string,
    public materialGoodsID?: string,
    public checked?: boolean,
    public grade?: number
  ) {
    this.isActive = this.isActive || false;
  }
}
