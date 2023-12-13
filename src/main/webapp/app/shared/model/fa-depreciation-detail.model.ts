import { Moment } from 'moment';

export interface IFADepreciationDetail {
  id?: string;
  faDepreciationID?: string;
  fixAssetID?: string;
  fixedAssetCode?: string;
  fixedAssetName?: string;
  amount?: number;
  depreciationAmount?: number;
  orderPriority?: number;
  fixedAssetItem?: any;

  customField1?: string;
  customField2?: string;
  customField3?: string;
  customField4?: string;
  customField5?: string;
}

export class FADepreciationDetail implements IFADepreciationDetail {
  public customField1?: string;
  public customField2?: string;
  public customField3?: string;
  public customField4?: string;
  public customField5?: string;
  constructor(
    public id?: string,
    public faDepreciationID?: string,
    public fixAssetID?: string,
    public fixedAssetCode?: string,
    public fixedAssetName?: string,
    public amount?: number,
    public depreciationAmount?: number,
    public orderPriority?: number,
    public fixedAssetItem?: any
  ) {}
}
