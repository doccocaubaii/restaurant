import { IOrganizationUnit } from 'app/shared/model/organization-unit.model';

export interface ITreeOrganizationUnit {
  parent?: IOrganizationUnit;
  index?: number;
  select?: boolean;
  check?: boolean;
  children?: ITreeOrganizationUnit[];
  organizationUnitCode?: string;
  organizationUnitName?: string;
  grade?: number;
  id?: any;
  parentID?: any;
}

export class TreeOrganizationUnit implements ITreeOrganizationUnit {
  constructor(
    public parent?: IOrganizationUnit,
    public index?: number,
    public select?: boolean,
    public check?: boolean,
    public children?: ITreeOrganizationUnit[],
    public organizationUnitCode?: string,
    public organizationUnitName?: string,
    public grade?: number,
    public id?: any,
    public parentID?: any
  ) {
    this.select = this.select || false;
    this.check = this.check || false;
  }
}
