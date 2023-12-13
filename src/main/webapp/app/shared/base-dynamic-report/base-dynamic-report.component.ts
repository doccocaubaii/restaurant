import { HostListener, Injectable, OnDestroy } from '@angular/core';
import { Subscription } from 'rxjs';
import { last_company } from '../../object-stores.constants';
import { db } from '../../db';

@Injectable()
export abstract class BaseDynamicReportComponent implements OnDestroy {
  paddingToolBar: any = 0;
  widthReportRoot: any;
  setting = {
    element: {
      dynamicDownload: null as HTMLElement | null,
    },
  };
  isPrivateTool: any;
  isTool: any;
  eventSubscribers: Subscription[] = [];

  @HostListener('window:scroll', ['$event'])
  doSomething(event) {
    const sideBar = document.getElementById('sidebar');
    sideBar!.style.left = window.pageXOffset + 'px';
    sideBar!.style.position = 'relative';
    this.paddingToolBar = window.pageXOffset > 0 ? window.pageXOffset : 0;
  }

  ngOnDestroy(): void {
    if (this.eventSubscribers) {
      this.eventSubscribers.forEach(ev => {
        ev.unsubscribe();
      });
    }
    const sideBar = document.getElementById('sidebar');
    if (sideBar && sideBar.style) {
      sideBar.style.left = 0 + 'px';
      sideBar.style.position = 'unset';
    }
  }

  dynamicDownloadByHtmlTag(arg: { fileName: string; text: string }) {
    if (!this.setting.element.dynamicDownload) {
      this.setting.element.dynamicDownload = document.createElement('a');
    }
    const textToBLOB = new Blob([arg.text], { type: 'text/plain' });
    const sFileName = arg.fileName + '.html'; // The file to save the data.

    let newLink = document.createElement('a');
    newLink.download = sFileName;
    newLink.href = window.URL.createObjectURL(textToBLOB);
    newLink.style.display = 'none';
    document.body.appendChild(newLink);
    newLink.click();
  }
  async getCompany() {
    // rw: cấp quyền read & write cho 1 transaction, nếu chỉ đọc thì chỉ cần 'r'
    // db.printConfigs: cấp quyền truy cập object stores cho transaction
    const data = await this.getAllByObjectStore(last_company);
    return data ? data[0] : {};
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
}
