import { IUsingBill } from './using-bill.model';

export interface IAreaUnit {
  id: number;
  comId: number;
  areaId: number;
  name: string;
  createTime: string;
  updateTime: string;
  stt?: number;
  areaName?: string;
  usingBills?: IUsingBill[];
  totalMoney?: number;
  timeTillNow?: string;
}

export interface IAreaUnitDto {
  comId: number;
  areaId: number;
  name: string;
}
export function initUnit(): IAreaUnit {
  const initialIAreaUnit: IAreaUnit = {
    id: 0,
    comId: 0,
    areaId: 0,
    name: '',
    createTime: '',
    updateTime: '',
    stt: 0,
    totalMoney: 0,
    timeTillNow: '',
  };

  return initialIAreaUnit;
}
export class DUnit implements IAreaUnitDto {
  constructor(public comId: number, public areaId: number, public name: string) {}
}

export interface InfoArea {
  strValue: string;
  intValue: number;
}
