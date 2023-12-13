export interface ITMAppendixList {
  id?: string;
  typeID?: number;
  appendixCode?: string;
  appendixName?: string;
  checked?: boolean;
  isLoad?: boolean;
}

export class TMAppendixList implements ITMAppendixList {
  constructor(
    public id?: string,
    public typeID?: number,
    public appendixCode?: string,
    public appendixName?: string,
    public checked?: boolean,
    public isload?: boolean
  ) {}
}
