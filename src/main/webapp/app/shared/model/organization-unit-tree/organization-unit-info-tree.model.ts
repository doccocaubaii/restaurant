export class OrganizationUnitInfoTree {
  id: string;
  name: string;
  unitType: number;
  parentID: string;
  userId: number;
  voucherTotal: number;
  usingVoucherTotal: number;
  remainVoucherTotal: number;
  children: OrganizationUnitInfoTree[];
}
