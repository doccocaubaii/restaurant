export interface ITM032ATNDN {
  id?: number;
  tM03TNDNID?: number;
  year?: number;
  lostAmount?: number;
  lostAmountTranferPreviousPeriods?: number;
  lostAmountTranferThisPeriod?: number;
  lostAmountRemain?: number;
  orderPriority?: number;
}

export class TM032ATNDN implements ITM032ATNDN {
  constructor(
    public id?: number,
    public tM03TNDNID?: number,
    public year?: number,
    public lostAmount?: number,
    public lostAmountTranferPreviousPeriods?: number,
    public lostAmountTranferThisPeriod?: number,
    public lostAmountRemain?: number,
    public orderPriority?: number
  ) {}
}
