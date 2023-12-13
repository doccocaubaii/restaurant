import { Directive, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { AppDB, db } from '../../db';
import { last_company, last_owner_device } from '../../object-stores.constants';
import Dexie from 'dexie';
import * as CryptoJS from 'crypto-js';
import { SECURITY_INITIALIZATION_VECTOR, SECURITY_MAIN_KEY } from '../../constants/common.constants';
import { DataEncrypt } from '../../entities/indexDatabase/data-encrypt.model';
import { LoginService } from '../../pages/login/login.service';
import { Router } from '@angular/router';
import { IndexDBError } from '../../entities/error/IndexDBError';
import { INDEX_DB_ERROR } from '../../constants/error.constants';

@Directive()
export abstract class BaseComponent implements OnDestroy {
  private db: IDBDatabase;
  eventSubscribers: Subscription[] = [];
  obs: Subscription | any;
  id: any;

  private loginServiceCtrl: LoginService;
  private routerCtrl: Router;

  async clearDB() {
    await Dexie.delete('EZPosIndexedDB')
      .then(() => {
        // console.log('Database deleted');
        new AppDB();
      })
      .catch(error => {
        // console.error('Error:', error);
      });
  }
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
        // console.log('Delete successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        // console.error(err.stack);
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
        // console.log('Delete successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        // console.error(err.stack);
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
        // console.log('Modify successful!');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        // console.error(err.stack);
      });
  }

  // edit by id
  async updateById(objectStore?: any, searchValue?: any, valueChange?: any) {
    return db
      .transaction('rw', objectStore, async () => {
        let result = db[objectStore].update(searchValue, valueChange);
        return result;
      })
      .then(result => {
        // console.log('Đã cập nhật bản ghi thành công');
        return result;
      })
      .catch(error => {
        // console.error('Lỗi khi cập nhật bản ghi', error);
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
        // console.log('Modify successful!');
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
        // console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  async getObjectStoreWithPaging(objectStore: string, searchInfo: any) {
    return db
      .transaction('r', objectStore, async () => {
        let result = db[objectStore];
        let dataReturn;

        if (searchInfo.keyword) {
          dataReturn = await result.filter(product => {
            let keyword = this.removeAccents(searchInfo.keyword).toLowerCase();
            let productName = this.removeAccents(product.productName).toLowerCase();
            if (productName.search(keyword) == -1) {
              return false;
            } else {
              return true;
            }
            // return regex.test(product.productName) || regex.test(product.barcode);
          });
        }

        // console.log(searchInfo)
        // if(searchInfo.status){
        //   console.log(searchInfo.status)
        //   dataReturn = await result.filter(product => {
        //     return product.status === searchInfo.status;
        //   })
        // }

        if (searchInfo.page !== null && searchInfo.size) {
          const start = searchInfo.page * searchInfo.size;
          dataReturn = await result.offset(start).limit(searchInfo.size);
        }

        return await dataReturn;
      })
      .then(result => {
        // console.log('Transaction committed');
        return result.toArray();
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  async getOrderListWithPaging(objectStore: string, searchInfo: any) {
    let totalItem = 0;
    let dataReturn = await db
      .transaction('r', objectStore, async () => {
        let result = db[objectStore];
        console.log(searchInfo);

        if (searchInfo.keyword) {
          result = await result.filter(order => {
            let keyword = this.removeAccents(searchInfo.keyword).toLowerCase();
            let customerName = this.removeAccents(order.customerName).toLowerCase();
            if (customerName.search(keyword) == -1) {
              return false;
            } else {
              return true;
            }
            // return regex.test(product.productName) || regex.test(product.barcode);
          });
        }

        if (searchInfo.status != undefined) {
          console.log(searchInfo.status);
          result = await result.filter(order => {
            return order.status === searchInfo.status;
          });
        }

        if (searchInfo.fromDate) {
          const fromDateInput = new Date(searchInfo.fromDate);
          result = await result.filter(order => {
            return fromDateInput <= new Date(order.billDate);
          });
        }

        if (searchInfo.toDate) {
          const toDateInput = new Date(searchInfo.toDate);
          result = await result.filter(order => {
            return toDateInput >= new Date(order.billDate);
          });
        }

        if (searchInfo.page !== null && searchInfo.size) {
          totalItem = await result.count();
          console.log(totalItem);
          const start = searchInfo.page * searchInfo.size;
          result = await result.offset(start).limit(searchInfo.size);
        }

        return await result;
      })
      .then(result => {
        // console.log('Transaction committed');
        return result.toArray();
      })
      .catch(err => {
        console.error(err.stack);
      });

    return {
      listOrder: dataReturn,
      totalItem: totalItem,
    };
  }

  async getProductListtWithPaging(objectStore: string, searchInfo: any) {
    let totalItem = 0;
    return db
      .transaction('r', objectStore, async () => {
        let result = db[objectStore];
        console.log(searchInfo);

        if (searchInfo.keyword) {
          result = await result.filter(product => {
            let keyword = this.removeAccents(searchInfo.keyword).toLowerCase();
            let productName = this.removeAccents(product.productName).toLowerCase();
            if (productName.search(keyword) == -1) {
              return false;
            } else {
              return true;
            }
            // return regex.test(product.productName) || regex.test(product.barcode);
          });
        }

        if (searchInfo.groupIds.length) {
          result = await result.filter(product => {
            product.groups?.forEach(groupProduct => {
              searchInfo.groupIds.forEach(groupId => {
                if (groupProduct.id === groupId) {
                }
                return product.status === searchInfo.status;
              });
            });
          });
        }

        if (searchInfo.page !== null && searchInfo.size) {
          totalItem = await result.count();
          const start = searchInfo.page * searchInfo.size;
          result = await result.offset(start).limit(searchInfo.size);
        }

        return await result;
      })
      .then(result => {
        // console.log('Transaction committed');
        return result.toArray();
      })
      .catch(err => {
        console.error(err.stack);
      });
  }

  async getCompany() {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const dataEncrypt: DataEncrypt[] = await this.getAllByObjectStore(last_company);
    if (dataEncrypt) {
      return this.convertToJsonValue(dataEncrypt);
    }
    throw new IndexDBError(INDEX_DB_ERROR);
  }

  convertToJsonValue(dataEncrypt: DataEncrypt[]) {
    if (dataEncrypt && dataEncrypt.length > 0) {
      return this.decryptFromValue(dataEncrypt[0].data);
    }
    throw new IndexDBError(INDEX_DB_ERROR);
  }

  async getFirstItemIndexDB(objectStore?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const dataEncrypt: DataEncrypt[] = await this.getAllByObjectStore(objectStore);
    if (dataEncrypt) {
      return this.convertToJsonValue(dataEncrypt);
    }
    throw new IndexDBError(INDEX_DB_ERROR);
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
        return this.decryptFromValue(result.data);
      })
      .catch(err => {
        // console.error(err.stack);
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
        // console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        if (err.name === '') console.error(err.stack);
      });
  }

  removeAccents(str: string) {
    return str
      .normalize('NFD')
      .replace(/[\u0300-\u036f]/g, '')
      .replace(/đ/g, 'd')
      .replace(/Đ/g, 'D');
  }

  // tìm kiếm theo key bất kì
  async findByKeySearch2(objectStore?: any, keySearch?: any, valueSearch?: any) {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    // keysearch bắt buộc phải thuộc nhóm key path
    const regex = new RegExp(valueSearch);
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore]
          .filter(product => {
            let keyword = this.removeAccents(valueSearch).toLowerCase();
            let productName = this.removeAccents(product.productName).toLowerCase();
            if (productName.search(keyword) == -1) {
              return false;
            } else {
              return true;
            }
            // return regex.test(product.productName) || regex.test(product.barcode);
          })
          .toArray();
      })
      .then(result => {
        // console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        if (err.name === '') console.error(err.stack);
      });
  }

  // tìm kiếm trong khoảng theo key bất kì
  async findByKeySearchBetween(objectStore?: any, keySearch?: any, from?: any, to?: any) {
    return db
      .transaction('r', objectStore, async () => {
        return await db[objectStore].where(keySearch).between(from, to).toArray();
      })
      .then(result => {
        // console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        // console.error(err.stack);
      });
  }

  // Thêm mới 1 dòng vào bảng
  async addItem(objectStore?: any, item?: any) {
    return db
      .transaction('rw', objectStore, async () => {
        //
        // Transaction Scope
        //
        let result = await db[objectStore].put(item);
        return result;
      })
      .then(result => {
        //
        // Transaction Complete
        //
        // console.log('Transaction committed');
        return result;
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        // console.error(err.stack);
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
        // console.log('Transaction committed');
      })
      .catch(err => {
        //
        // Transaction Failed
        //
        // console.error(err.stack);
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

  isObjectStoreExist(dbName, objectStoreName) {
    return new Promise((resolve, reject) => {
      const request = indexedDB.open(dbName);

      request.onsuccess = (event: any) => {
        const db = event.target.result;
        const objectStoreNames = db.objectStoreNames;

        if (objectStoreNames.contains(objectStoreName)) {
          resolve(true);
        } else {
          resolve(false);
        }
      };

      request.onerror = (event: any) => {
        reject(event.target.error);
      };
    });
  }

  encryptFromData(data: any): string {
    const keyAndIv = this.convertKeyAndIvToUtf8();
    const ciphertext = CryptoJS.AES.encrypt(JSON.stringify(data), keyAndIv[0], {
      iv: keyAndIv[1],
      mode: CryptoJS.mode.CBC,
      padding: CryptoJS.pad.Pkcs7,
    });
    return ciphertext.toString();
  }

  convertKeyAndIvToUtf8() {
    const key = CryptoJS.enc.Utf8.parse(SECURITY_MAIN_KEY);
    const iv = CryptoJS.enc.Utf8.parse(SECURITY_INITIALIZATION_VECTOR);
    return [key, iv];
  }

  decryptFromValue(value: string) {
    const keyAndIv = this.convertKeyAndIvToUtf8();
    try {
      const decryptedData = CryptoJS.AES.decrypt(value, keyAndIv[0], {
        iv: keyAndIv[1],
        mode: CryptoJS.mode.CBC,
        padding: CryptoJS.pad.Pkcs7,
      });
      return JSON.parse(CryptoJS.enc.Utf8.stringify(decryptedData));
    } catch (e) {
      console.error('Lỗi giải mã hoá dữ liệu: ' + e);
      throw new IndexDBError(INDEX_DB_ERROR);
    }
  }
}
