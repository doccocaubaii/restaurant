export interface ITM05QTTTNCNDetail {
  id?: string;
  tM05QTTTNCNID?: string;
  code?: string;
  name?: string;
  unit?: string;
  data?: number;
  orderPriority?: number;
}

export class TM05QTTTNCNDetail implements ITM05QTTTNCNDetail {
  constructor(
    public id?: string,
    public tM05QTTTNCNID?: string,
    public code?: string,
    public name?: string,
    public unit?: string,
    public data?: number,
    public orderPriority?: number
  ) {}
}
