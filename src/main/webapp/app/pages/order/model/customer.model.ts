export interface Customer {
  id: number;
  comId: number;
  name: string;
  code2: string;
  type: number;
  address: string;
  city: string;
  district: string;
  phoneNumber: string;
  email: string;
  taxcode: string;
  idNumber: string;
  description: string;
  disabled: boolean;
  pointBalance?: number;
  moneyBalance?: number;
  cardInformation?: any;
}
