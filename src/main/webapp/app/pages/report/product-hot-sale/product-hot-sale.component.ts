import { Component, OnDestroy, OnInit } from '@angular/core';
import { SearchProductHotSaleReq } from './product-hot-sale';
import dayjs from 'dayjs/esm';
import { BaseComponent } from '../../../shared/base/base.component';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from '../../../utils/Utils.service';
import { ProductSalesStatsService } from '../product-sales-stats/product-sales-stats.service';
import { ExportCommon } from '../../export/export.common';
import { ProductHotSaleService } from './product-hot-sale.service';
import { ICON_EXCEL, ICON_PDF, ICON_SELECT_FILE } from '../../../shared/other/icon';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-product-hot-sale',
  templateUrl: './product-hot-sale.component.html',
  styleUrls: ['./product-hot-sale.component.scss'],
})
export class ProductHotSaleComponent extends BaseComponent implements OnDestroy, OnInit {
  searchReq: SearchProductHotSaleReq = new SearchProductHotSaleReq();
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  lastCompany: any = {};
  listData: any = [];
  authorExcel = Authority.REPORT_EXPORT_EXCEL;
  authorPdf = Authority.REPORT_EXPORT_PDF;
  data: any = {};
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    protected utilsService: UtilsService,
    private service: ProductHotSaleService,
    public exportComponent: ExportCommon
  ) {
    super();
  }

  async ngOnInit() {
    this.fromDate = dayjs().startOf('month');
    this.toDate = dayjs();
    this.lastCompany = await this.getCompany();
    this.searchReq.type = 0;
    this.getData();
  }

  searchByType(type: any) {
    this.searchReq.type = type;
    this.getData();
  }

  getData() {
    this.searchReq.fromDate = this.formatDate(this.fromDate);
    this.searchReq.toDate = this.formatDate(this.toDate);
    this.searchReq.comId = this.lastCompany.id;
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
      this.service.getReportProductHotSale(this.searchReq).subscribe(value => {
        this.data = value.body.data;
        this.listData = value.body.data.detail;
        this.listData = JSON.parse(JSON.stringify(this.listData));
      });
    }
  }

  onExportPdf() {
    if (this.listData?.length > 0) {
      this.service.exportPdf(this.searchReq).subscribe(
        value => {
          this.openPdfInNewTab(value.body);
        },
        error => {}
      );
    }
  }

  openPdfInNewTab(byteArray: any): void {
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL, '_blank');
  }

  onExportExcel() {
    if (this.listData?.length > 0) {
      this.service.exportExcel(this.searchReq).subscribe(
        value => {
          this.exportComponent.saveExcelFromByteArray(value.body, 'Thong_Ke_San_Pham_Ban_Chay');
        },
        error => {}
      );
    }
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

  protected readonly ICON_SELECT_FILE = ICON_SELECT_FILE;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_EXCEL = ICON_EXCEL;
}
