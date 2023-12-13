// db.ts
import Dexie, { Table } from 'dexie';
import { last_config_device } from './object-stores.constants';

export class AppDB extends Dexie {
  printConfigs!: Table<any, number>;
  last_sync!: Table<any, number>;
  last_company!: Table<any, number>;
  last_user!: Table<any, number>;
  last_permission!: Table<any, number>;
  last_print_config!: Table<any, number>;
  last_print_config_detail!: Table<any, number>;
  last_owner_device!: Table<any, number>;
  customer!: Table<any, number>;
  product!: Table<any, number>;
  topping!: Table<any, number>;
  product_group!: Table<any, number>;
  area!: Table<any, number>;
  area_unit!: Table<any, number>;
  offline_bill!: Table<any, number>;
  offline_bill_product!: Table<any, number>;
  offline_bill_payment!: Table<any, number>;
  last_config_device!: Table<any, number>;
  constructor() {
    // Khởi tạo IndexedDB
    super('EZPosIndexedDB');
    // tạo ra các Table tương ứng là các object stores trên IndexedDB
    this.createDB();
  }
  createDB() {
    this.version(1).stores({
      printConfigs: '++id, title',
      last_sync: 'id',
      last_company: 'id',
      last_user: 'id',
      last_permission: 'id',
      last_print_config: 'id',
      last_print_config_detail: 'id',
      last_owner_device: 'ownerId',
      customer: 'id',
      product: 'productId',
      topping: 'productId',
      product_group: 'id',
      area: 'id',
      area_unit: 'id',
      offline_bill: '++id',
      offline_bill_product: '++id',
      offline_bill_payment: '++id',
      last_config_device: 'id',
    });
  }
}

export const db: any = new AppDB();
