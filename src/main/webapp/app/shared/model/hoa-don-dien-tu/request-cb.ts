export interface IRequestCB {
  id?: string;
  maphiengiaodich?: string;
  base64xml?: string;
  certStr?: string;
  hashID?: string;
  hashData?: string;
  signData?: string;
}

export class RequestCB implements IRequestCB {
  constructor(
    public id?: string,
    public maphiengiaodich?: string,
    public base64xml?: string,
    public certStr?: string,
    public hashID?: string,
    public hashData?: string,
    public signData?: string
  ) {}
}
