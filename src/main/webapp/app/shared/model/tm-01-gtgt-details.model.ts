export interface ITM01GTGTDetails {
  id?: number;
  tM01GTGTID?: number;
  code?: string;
  name?: string;
  data?: string;
  orderPriority?: number;
}

export class TM01GTGTDetails implements ITM01GTGTDetails {
  constructor(
    public id?: number,
    public tM01GTGTID?: number,
    public code?: string,
    public name?: string,
    public data?: string,
    public orderPriority?: number
  ) {}
}
