export interface ITM03TNDNDetails {
  id?: number;
  tM03TNDNID?: number;
  code?: string;
  name?: string;
  amount?: number;
  orderPriority?: number;
}

export class TM03TNDNDetails implements ITM03TNDNDetails {
  constructor(
    public id?: number,
    public tM03TNDNID?: number,
    public code?: string,
    public name?: string,
    public amount?: number,
    public orderPriority?: number
  ) {}
}
