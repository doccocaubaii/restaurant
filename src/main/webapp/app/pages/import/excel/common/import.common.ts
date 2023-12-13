import { Injectable, OnInit } from '@angular/core';
import { BaseComponent } from '../../../../shared/base/base.component';
import { ImportService } from '../../import.service';

@Injectable({
  providedIn: 'root',
})
export class ImportCommon extends BaseComponent implements OnInit {
  dataValidateResponse?: any[];

  constructor(private importService: ImportService) {
    super();
  }

  ngOnInit(): void {}

  checkInput(fileData: any, sheetIndex: number, importTypeValue: any): string {
    let message = '';
    if (!fileData) {
      message = 'Vui lòng chọn tệp nguồn';
    } else if (sheetIndex < 0) {
      message = 'Vui lòng chọn Sheet dữ liệu';
    } else if (!importTypeValue) {
      message = 'Vui lòng chọn hình thức Import';
    }
    return message;
  }

  statusFilter(): any {
    return [
      { id: 0, name: 'Tất cả' },
      { id: 1, name: 'Hợp lệ' },
      { id: 2, name: 'Không hợp lệ' },
    ];
  }
}
