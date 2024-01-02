import { IUsingBill } from './using-bill.model';

export interface IAreaUnit {
  id: number;
  comId: number;
  areaId: number;
  name: string;
  createTime: string;
  updateTime: string;
  stt?: number;
  usingBills?: IUsingBill[];
  totalMoney?: number;
  timeTillNow?: string;
}

export interface IAreaUnitDto {
  comId: number;
  areaId: number;
  name: string;
}

export class DUnit implements IAreaUnitDto {
  constructor(public comId: number, public areaId: number, public name: string) {}
}

export interface InfoArea {
  strValue: string;
  intValue: number;
}
