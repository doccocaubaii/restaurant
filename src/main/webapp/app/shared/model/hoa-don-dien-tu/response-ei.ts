export interface IResponseEI {
  status?: number;
  message?: string;
  data?: any;
}

export class ResponseEI implements IResponseEI {
  constructor(public status?: number, public message?: string, public data?: any) {}
}
