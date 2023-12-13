import { Moment } from 'moment';

export class InfoPackage {
  isWarningTime?: boolean;
  isWarningQuantity?: boolean;
  countVoucher?: number;
  totalVoucher?: number;
  remainTime?: number;
  expireDate?: Moment | any;
  isShowWarning?: boolean;
}
