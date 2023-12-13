export interface ITM031ATNDN {
  id?: number;
  tM03TNDNID?: number;
  code?: string;
  name?: string;
  data?: string;
  orderPriority?: number;
}

export class TM031ATNDN implements ITM031ATNDN {
  constructor(
    public id?: number,
    public tM03TNDNID?: number,
    public code?: string,
    public name?: string,
    public data?: string,
    public orderPriority?: number
  ) {}
}
