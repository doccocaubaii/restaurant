export interface ITM04GTGTDetails {
  id?: number;
  tM04GTGTID?: number;
  code?: string;
  name?: string;
  data?: string;
  orderPriority?: number;
}

export class TM04GTGTDetails implements ITM04GTGTDetails {
  constructor(
    public id?: number,
    public tM04GTGTID?: number,
    public code?: string,
    public name?: string,
    public data?: string,
    public orderPriority?: number
  ) {}
}
