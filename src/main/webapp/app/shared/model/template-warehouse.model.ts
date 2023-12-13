export interface ITemplateWarehouse {
  id?: number;
  templateType?: number;
  description?: string;
  isRotatePaper?: boolean;
  addColumn?: string;
  removeColumn?: string;
  isDefault?: boolean;
  logo?: string;
  templateName?: string;
}

export class TemplateWarehouse implements ITemplateWarehouse {
  constructor(
    public id?: number,
    public templateType?: number,
    public description?: string,
    public isRotatePaper?: boolean,
    public addColumn?: string,
    public removeColumn?: string,
    public isDefault?: boolean,
    public logo?: string,
    public templateName?: string
  ) {
    this.isRotatePaper = this.isRotatePaper || false;
    this.isDefault = this.isDefault || false;
  }
}
