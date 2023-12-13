import { BaseComponent } from '../../shared/base/base.component';
import { Component, Directive, Injectable, OnInit } from '@angular/core';
import { ExportService } from './export.service';
import { ConvertResponse } from '../../config/convert-response';
import { ToastrService } from 'ngx-toastr';

@Injectable({
  providedIn: 'root',
})
export class ExportCommon extends BaseComponent implements OnInit {
  constructor(private exportService: ExportService, private toast: ToastrService) {
    super();
  }

  ngOnInit(): void {}

  exportFileExcel(data: any, fileName: string, comId: number, urlApi: string) {
    this.exportService.exportErrorData(data, fileName, comId, urlApi).subscribe(
      response => {
        if (response) {
          this.saveExcelFromByteArray(response.body, fileName);
        } else {
          this.toast.error(ConvertResponse.getDataFromServer(response, true));
        }
      },
      error => {
        this.toast.error(ConvertResponse.getDataFromServer(error, true));
      }
    );
  }

  saveExcelFromByteArray(byteArray: any, fileName: string): void {
    const blob = new Blob([byteArray], { type: 'application/vnd.ms-excel' });
    const fileURL = URL.createObjectURL(blob);
    const link = document.createElement('a');
    document.body.appendChild(link);
    link.download = fileURL;
    link.setAttribute('style', 'display: none');
    link.setAttribute('download', fileName + '.xls');
    link.href = fileURL;
    link.click();
  }
}
