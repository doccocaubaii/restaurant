export interface IDisplayTemplateConfig {
  id?: number;
  companyID?: string;
  reportType?: number;
  marginTop?: number;
  marginBottom?: number;
  marginLeft?: number;
  marginRight?: number;
  paperSize?: number;
  orgInfoWidth?: number;
}

export class DisplayTemplateConfig implements IDisplayTemplateConfig {
  constructor(
    public id?: number,
    public companyID?: string,
    public reportType?: number,
    public marginTop?: number,
    public marginBottom?: number,
    public marginLeft?: number,
    public marginRight?: number,
    public paperSize?: number,
    public orgInfoWidth?: number
  ) {}
}
