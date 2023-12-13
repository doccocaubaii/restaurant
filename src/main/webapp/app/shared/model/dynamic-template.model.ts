export interface IDynamicTemplate {
  id?: number;
  companyID?: string;
  typeID?: number;
  templateName?: string;
}

export class DynamicTemplate implements IDynamicTemplate {
  constructor(public id?: number, public companyID?: string, public typeID?: number, public templateName?: string) {}
}
