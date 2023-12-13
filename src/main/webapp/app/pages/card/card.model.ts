export class CardItem {
  id: number;
  name: string;
  isDefault: boolean;
  rank: number;
  count: number;
  check?: any;
  customers: CustomerItem[];
}

export class CustomerItem {
  id: any;
  comId: any;
  name: any;
  code: any;
  code2: any;
  type: any;
  phoneNumber: any;
  address: any;
  city: any;
  district: any;
  email: any;
  taxCode: any;
  idNumber: any;
  description: any;
  createTime: any;
  updateTime: any;
}
