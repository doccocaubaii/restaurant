export class OwnerDevice {
  id: number | null;
  name?: string | null;
  ownerId: number | null;
  deviceCode?: string | null;
  taxCode?: string | null;

  constructor(name: string | null, ownerId: number | null, deviceCode: string | null) {
    this.name = name;
    this.ownerId = ownerId;
    this.deviceCode = deviceCode;
  }
}
