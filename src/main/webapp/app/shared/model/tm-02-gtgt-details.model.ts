export interface ITM02GTGTDetails {
  id?: number;
  tM02GTGTID?: number;
  code?: string;
  name?: string;
  data?: string;
  orderPriority?: number;
}

export class TM02GTGTDetails implements ITM02GTGTDetails {
  constructor(
    public id?: number,
    public tM02GTGTID?: number,
    public code?: string,
    public name?: string,
    public data?: string,
    public orderPriority?: number
  ) {}
}
