import { Moment } from 'moment';

export interface ISearchVoucherSalePriceGroups {
  unitID?: string;
  keySearch?: string;
  salePiceroupCode?: String;
  salePiceroupName?: String;
}

export class SearchVoucherSalePriceGroups implements ISearchVoucherSalePriceGroups {
  constructor(public unitID?: string, public keySearch?: string, public salePiceroupCode?: string, public salePiceroupName?: string) {}
}
