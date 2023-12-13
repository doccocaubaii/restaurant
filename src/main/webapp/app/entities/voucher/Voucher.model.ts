import { VoucherConditionsModel } from './VoucherConditions.model';
import { VoucherExtTimeModel } from './VoucherExtTime.model';
import dayjs from 'dayjs/esm';
import { VoucherDifferentExtConditionsModel } from './VoucherDifferentExtConditions.model';
import { VoucherUsageModel } from './VoucherUsage.model';

export interface VoucherModel {
  id: any;
  comId: number;
  code: string;
  name: string;
  startTime: dayjs.Dayjs | any;
  endTime: dayjs.Dayjs | any;
  applyType: string;
  type: number;
  status: number;
  conditions: VoucherConditionsModel[];
  extTimeConditions: VoucherExtTimeModel[];
  differentExtConditions: VoucherDifferentExtConditionsModel;
  active: boolean;
  historyUsage: VoucherUsageModel[];
}
