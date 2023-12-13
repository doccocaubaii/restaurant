export interface IExceptVoucher {
  id?: number;
}

export class ExceptVoucher implements IExceptVoucher {
  constructor(public id?: number) {}
}
