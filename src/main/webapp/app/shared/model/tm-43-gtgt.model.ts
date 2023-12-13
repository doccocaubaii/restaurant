export interface ITM43GTGT {
  materialGoodsName?: string;
  amount?: number;
  vatAmount?: number;
  totalAmount?: number;
  totalVatAmount?: number;
}

export class TM43GTGT implements ITM43GTGT {
  constructor(
    public materialGoodsName?: string,
    public amount?: number,
    public vatAmount?: number,
    public totalAmount?: number,
    public totalVatAmount?: number
  ) {}
}
