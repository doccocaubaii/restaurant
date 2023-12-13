export interface IStaff {
  id: number;
  comId: number;
  name: string;
  companies: any[];
  username: string;
  email: string;
  phoneNumber: string;
  createTime: string;
  updateTime: string;
  check: boolean;
}

export class Staff implements IStaff {
  constructor(
    public id: number,
    public comId: number,
    public name: string,
    public companies: any[],
    public username: string,
    public email: string,
    public phoneNumber: string,
    public createTime: string,
    public updateTime: string,
    public check: boolean
  ) {}

  static createNewStaff(): Staff {
    return new Staff(-1, 0, '', [], '', '', '', '', '', false);
  }
}
