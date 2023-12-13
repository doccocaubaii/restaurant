import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from '../../../utils/Utils.service';
import { ProductSalesStatsService } from '../product-sales-stats/product-sales-stats.service';
import { ExportCommon } from '../../export/export.common';
import { BaseComponent } from '../../../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { SearchActivityHistoryReq } from './activity-history';
import { ActivityHistoryService } from './activity-history.service';

@Component({
  selector: 'jhi-activity-history',
  templateUrl: './activity-history.component.html',
  styleUrls: ['./activity-history.component.scss'],
})
export class ActivityHistoryComponent extends BaseComponent implements OnDestroy, OnInit {
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  lastCompany: any;
  searchReq: SearchActivityHistoryReq = new SearchActivityHistoryReq();

  listData: any = [];
  totalItems: any = 0;
  sizes = [
    {
      id: 10,
      name: '10',
    },
    {
      id: 20,
      name: '20',
    },
    {
      id: 30,
      name: '30',
    },
  ];
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    protected utilsService: UtilsService,
    private service: ActivityHistoryService
  ) {
    super();
  }

  async ngOnInit() {
    this.fromDate = dayjs().startOf('month');
    this.toDate = dayjs();
    this.lastCompany = await this.getCompany();
    this.searchReq.size = 20;
    this.onSearch();
  }

  onSearch() {
    this.searchReq.page = 1;
    this.getData();
  }
  getData() {
    this.searchReq.fromDate = this.formatDate(this.fromDate);
    this.searchReq.toDate = this.formatDate(this.toDate);
    this.searchReq.comId = this.lastCompany.id;
    const req = Object.assign({}, this.searchReq);
    req.page = this.searchReq.page - 1;
    if (!this.searchReq.fromDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductSales.message.dateError'),
        this.translateService.instant('easyPos.reportProductSales.message.title')
      );
    } else if (!this.searchReq.toDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductSales.message.dateError'),
        this.translateService.instant('easyPos.reportProductSales.message.title')
      );
    } else {
      this.service.getActivityHistory(req).subscribe(value => {
        if (value.body.data.detail) {
          this.listData = value.body.data.detail[0];
          this.totalItems = value.body.count;
        } else {
          this.listData = [];
          this.totalItems = 0;
        }
      });
    }
  }
  onExportPdf() {
    if (this.listData?.length > 0) {
      this.service.exportPdf(this.searchReq).subscribe(
        value => {
          this.savePdfFromByteArray(value.body, 'Thong_Ke_Hoat_Dong_Gan_Day');
        },
        error => {}
      );
    }
  }

  onExportExcel() {
    if (this.listData?.length > 0) {
      this.service.exportExcel(this.searchReq).subscribe(
        value => {
          this.saveExcelFromByteArray(value.body, 'Thong_Ke_Hoat_Dong_Gan_Day');
        },
        error => {}
      );
    }
  }

  savePdfFromByteArray(byteArray: any, fileName: string): void {
    const blob = new Blob([byteArray], { type: 'application/pdf' }); // Thay đổi thành application/pdf
    const fileURL = URL.createObjectURL(blob);
    const link = document.createElement('a');
    document.body.appendChild(link);
    link.download = fileURL;
    link.setAttribute('style', 'display: none');
    link.setAttribute('download', fileName + '.pdf'); // Thay đổi đuôi file sang .pdf
    link.href = fileURL;
    link.click();
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
  formatDate(date) {
    if (!date) return undefined;
    if (date instanceof Date) {
      let year = date.getFullYear();
      let month = ('0' + (date.getMonth() + 1)).slice(-2);
      let day = ('0' + date.getDate()).slice(-2);
      return year + '-' + month + '-' + day;
    } else if (typeof date === 'string') {
      let segments = date.split(/[/,-]/);
      if (segments.length !== 3) return undefined;
      let day = segments[0];
      let month = segments[1];
      let year = segments[2];
      return year + '-' + month + '-' + day;
    } else if (date && date.hasOwnProperty('$d')) {
      // check if it is a dayjs object
      var dayjsDate = date; // cast date to Dayjs
      let year = dayjsDate.$y;
      let month = ('0' + (dayjsDate.$M + 1)).slice(-2);
      let day = ('0' + dayjsDate.$D).slice(-2);
      return year + '-' + month + '-' + day;
    } else {
      return undefined;
    }
  }

  loadMore($event: any) {
    this.searchReq.page = $event;
    this.getData();
  }
}
