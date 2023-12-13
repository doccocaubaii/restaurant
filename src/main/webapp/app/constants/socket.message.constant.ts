import * as CryptoJS from 'crypto-js';
import { Injectable } from '@angular/core';

export class SocketMessage {
  action?: any;
  reason?: any;
  data?: any;
}

@Injectable({
  providedIn: 'root',
})
export class Md5Service {
  public convertToMd5(text: string) {
    return CryptoJS.MD5(text).toString();
  }
}
