import { IUser } from '../user/user.model';

export class ISocketConfigModel {
  type: number;
  message: string;
  refId: string;
  reason: string;
  comId: number;
  billProductId: number;
}

export class SocketConfigModel implements ISocketConfigModel {
  constructor(
    public type: number,
    public message: string,
    public refId: string,
    public reason: string,
    public comId: number,
    public billProductId: number
  ) {}
}
