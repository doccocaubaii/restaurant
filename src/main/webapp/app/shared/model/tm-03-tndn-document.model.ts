export interface ITM03TNDNDocument {
  id?: number;
  tM03TNDNID?: number;
  documentName?: string;
  orderPriority?: number;
}

export class TM03TNDNDocument implements ITM03TNDNDocument {
  constructor(public id?: number, public tM03TNDNID?: number, public documentName?: string, public orderPriority?: number) {}
}
