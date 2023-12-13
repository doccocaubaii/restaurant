export interface ITypeGroup {
  id?: number;
  typeGroupName?: string;
  debitAccount?: string;
  creditAccount?: string;
  isCheck?: boolean;
  checked?: boolean;
}

export class TypeGroup implements ITypeGroup {
  constructor(
    public id?: number,
    public typeGroupName?: string,
    public debitAccount?: string,
    public creditAccount?: string,
    public isCheck?: boolean,
    public checked?: boolean
  ) {}
}
