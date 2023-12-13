export interface IType {
  id?: number;
  typeName?: string;
  typeGroupID?: number;
  typeNameModule?: string;
}

export class Type implements IType {
  constructor(public id?: number, public typeName?: string, public typeGroupID?: number, public typeNameModule?: string) {}
}
