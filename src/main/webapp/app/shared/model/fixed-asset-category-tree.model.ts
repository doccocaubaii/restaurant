import { IFixedAssetCategory } from 'app/shared/model/fixed-asset-category.model';

export interface ITreeFixedAssetCategory {
  parent?: IFixedAssetCategory;
  index?: number;
  select?: boolean;
  check?: boolean;
  children?: ITreeFixedAssetCategory[];
}

export class TreeFixedAssetCategory implements ITreeFixedAssetCategory {
  constructor(
    public parent?: IFixedAssetCategory,
    public index?: number,
    public select?: boolean,
    public check?: boolean,
    public children?: ITreeFixedAssetCategory[]
  ) {
    this.select = this.select || false;
    this.check = this.check || false;
  }
}
