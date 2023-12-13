export interface ITMIndustryType {
  id?: number;
  industryTypeCode?: string;
  industryTypeName?: string;
}

export class TMIndustryType implements ITMIndustryType {
  constructor(public id?: number, public industryTypeCode?: string, public industryTypeName?: string) {}
}
