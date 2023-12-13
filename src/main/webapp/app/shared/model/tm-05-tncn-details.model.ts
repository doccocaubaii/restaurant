export interface ITM05TNCNDetails {
  id?: number;
  tM05TNCNID?: number;
  code?: string;
  name?: string;
  unit?: string;
  data?: string;
  orderPriority?: number;
  _code?: string;
  rowspan?: number;
  inspan?: boolean;
  noEdit?: boolean;
  isBold?: boolean;
  maxlength?: number;
  stt?: string;
  modelChange?: () => void;
}

export class TM05TNCNDetails implements ITM05TNCNDetails {
  constructor(
    public id?: number,
    public tM05TNCNID?: number,
    public code?: string,
    public name?: string,
    public unit?: string,
    public data?: string,
    public orderPriority?: number
  ) {}
}
