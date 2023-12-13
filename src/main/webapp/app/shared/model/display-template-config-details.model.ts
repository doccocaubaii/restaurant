export interface IDisplayTemplateConfigDetails {
  id?: number;
  displayTemplateConfigID?: string;
  order?: number;
  title?: string;
  displayTitle?: string;
  name?: string;
  isDisplay?: boolean;
}

export class DisplayTemplateConfigDetails implements IDisplayTemplateConfigDetails {
  constructor(
    public id?: number,
    public displayTemplateConfigID?: string,
    public order?: number,
    public title?: string,
    public displayTitle?: string,
    public name?: string,
    public isDisplay?: boolean
  ) {
    this.isDisplay = this.isDisplay || false;
  }
}
