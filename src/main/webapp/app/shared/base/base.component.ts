import { Directive, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { db } from '../../db';
import { last_company } from '../../object-stores.constants';

@Directive()
export abstract class BaseComponent implements OnDestroy {
  eventSubscribers: Subscription[] = [];

  obs: Subscription | any;

  id: any;

  // delete by id
  async deleteByID(objectStore?: any, deleteValue?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    db.transaction('rw', objectStore, async () => {
      db[objectStore].delete(deleteValue);
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Delete successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }
  async deleteAll(objectStore?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    db.transaction('rw', objectStore, async () => {
      db[objectStore].clear();
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Delete successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }

  // edit by id
  async editByID(objectStore?: any, searchValue?: any, keyChange?: any, valueChange?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    db.transaction('rw', objectStore, async () => {
      db[objectStore]
        .where({
          id: searchValue,
        })
        .modify(response => {
          response[keyChange] = valueChange;
        });
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Modify successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }

  // edit by id
  async updateById(objectStore?: any, searchValue?: any, valueChange?: any) {
    db.transaction('rw', objectStore, async () => {
      db[objectStore].update(searchValue, valueChange);
    })
      .then(() => {
        console.log('Đã cập nhật bản ghi thành công');
      })
      .catch(error => {
        console.error('Lỗi khi cập nhật bản ghi', error);
      });
  }
  // edit by id
  async editAllByID(objectStore?: any, searchValue?: any, valueChange?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    db.transaction('rw', objectStore, async () => {
      db[objectStore]
        .where({
          id: searchValue,
        })
        .modify(response => {
          response = valueChange;
        });
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Modify successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }

  // tìm kiếm theo id
  async getAllByObjectStore(objectStore?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore].toArray();
      })
      .then(result => {
        console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  async getCompany() {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const data = await this.getAllByObjectStore(last_company);
    return data[0];
  }

  async getFirstItemIndexDB(objectStore?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const data = await this.getAllByObjectStore(objectStore);
    return data[0];
  }

  // tìm kiếm theo id
  async findByID(objectStore?: any, value?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore].get(value);
      })
      .then(result => {
        console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  // tìm kiếm theo key bất kì
  async findByKeySearch(objectStore?: any, keySearch?: any, valueSearch?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    // keysearch bắt buộc phải thuộc nhóm key path
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore].where(keySearch).equals(valueSearch);
      })
      .then(result => {
        console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  // tìm kiếm trong khoảng theo key bất kì
  async findByKeySearchBetween(objectStore?: any, keySearch?: any, from?: any, to?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    // keysearch bắt buộc phải thuộc nhóm key path
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore].where(keySearch).between(from, to).toArray();
      })
      .then(result => {
        console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  // Thêm mới 1 dòng vào bảng
  async addItem(objectStore?: any, item?: any) {
    db.transaction('rw', objectStore, async () => {
      //
      // Transaction Scope
      //
      await db[objectStore].put(item);
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Transaction committed');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }

  // Thêm mới nhiều dòng vào bảng
  async addArrItems(objectStore?: any, arr?: any[]) {
    db.transaction('rw', objectStore, async () => {
      //
      // Transaction Scope
      //
      await db[objectStore].bulkAdd(arr);
    })
      .then(() => {
        //
        // Transaction Complete
        //
        console.log('Transaction committed');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        console.error(err.stack);
      });
  }

  ngOnDestroy() {
    if (this.eventSubscribers) {
      this.eventSubscribers.forEach(ev => {
        ev.unsubscribe();
      });
    }
    if (this.obs) {
      this.obs.unsubscribe();
    }
  }

  randomString(length) {
    const randomChars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
    let result = '';
    for (var i = 0; i < length; i++) {
      result += randomChars.charAt(Math.floor(Math.random() * randomChars.length));
    }
    return result;
  }

  randomAge() {
    return Math.floor(50 * Math.random());
  }
}
