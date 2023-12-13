import { Component, OnDestroy, OnInit } from '@angular/core';
import { SearchProductSaleReq } from './product-sales-stats';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from '../../../utils/Utils.service';
import { BaseComponent } from '../../../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { ProductSalesStatsService } from './product-sales-stats.service';
import { ExportCommon } from '../../export/export.common';
import { ModalCreateCategoryComponent } from '../../product/modal-create-category/modal-create-category.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalSearchInitComponent } from './modal-search-init/modal-search-init.component';
import { ICON_EXCEL, ICON_PDF, ICON_RESET, ICON_SELECT_PARAM } from '../../../shared/other/icon';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-product-sales-stats',
  templateUrl: './product-sales-stats.component.html',
  styleUrls: ['./product-sales-stats.component.scss'],
})
export class ProductSalesStatsComponent extends BaseComponent implements OnDestroy, OnInit {
  searchReq: SearchProductSaleReq = {
    fromDate: '',
    toDate: '',
    status: null,
    taxCheckStatus: null,
    page: 1,
    size: 20,
    isPaging: true,
  };
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  lastCompany: any;
  listPatterns: any = [];
  listData: any = [];
  paginatedData: any = [];
  private modalRef: NgbModalRef | undefined;
  totalItems: any = 0;
  data: any = {};
  period: any;
  year: any;
  isSavingPdf = false;
  isSavingExcel = false;
  isResetData = false;
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
  params: any;
  authorExcel = Authority.REPORT_EXPORT_EXCEL;
  authorPdf = Authority.REPORT_EXPORT_PDF;
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    protected utilsService: UtilsService,
    private service: ProductSalesStatsService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  async ngOnInit() {
    try {
      this.lastCompany = await this.getCompany();
      this.getPattern();
      this.params = await new Promise(resolve => {
        this.activatedRoute.queryParamMap.subscribe((params: any) => {
          resolve(params);
        });
      });
      this.getDataInit();
    } catch (error) {}
  }

  onOpenModalFilter() {
    this.modalRef = this.modalService.open(ModalSearchInitComponent, { size: 'lg', backdrop: 'static' });
    if (this.fromDate) {
      this.modalRef.componentInstance.checkUpdate = true;
      this.modalRef.componentInstance.fromDate = this.fromDate;
    }
    if (this.toDate) {
      this.modalRef.componentInstance.toDate = this.toDate;
    }
    if (this.searchReq.pattern) {
      this.modalRef.componentInstance.pattern = this.searchReq.pattern;
    }
    if (this.searchReq.status != null && this.searchReq.status != undefined) {
      this.modalRef.componentInstance.status = this.searchReq.status;
    }
    if (this.searchReq.taxCheckStatus != null && this.searchReq.taxCheckStatus != undefined) {
      this.modalRef.componentInstance.taxCheckStatus = this.searchReq.taxCheckStatus;
    }
    if (this.period != null && this.period != undefined) {
      this.modalRef.componentInstance.period = this.period;
    }
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate) {
        this.fromDate = res.fromDate;
        this.toDate = res.toDate;
        this.searchReq.pattern = res.pattern;
        this.searchReq.status = res.status;
        this.searchReq.taxCheckStatus = res.taxCheckStatus;
        this.year = this.fromDate.year();
        this.period = res.period;
        this.onSearch();
      }
    });
  }

  onSearch() {
    this.searchReq.page = 1;
    this.getData();
  }

  loadMore($event: any) {
    this.searchReq.page = $event;
    this.paginateData();
  }

  paginateData() {
    const start = (this.searchReq.page - 1) * this.searchReq.size;
    const end = start + this.searchReq.size;
    this.paginatedData = this.listData.slice(start, end);
    this.paginatedData = JSON.parse(JSON.stringify(this.paginatedData));
  }

  getData() {
    if (!this.fromDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductSales.message.dateError'),
        this.translateService.instant('easyPos.reportProductSales.message.title')
      );
    } else if (!this.toDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductSales.message.dateError'),
        this.translateService.instant('easyPos.reportProductSales.message.title')
      );
    } else {
      this.searchReq.fromDate = this.formatDate(this.fromDate);
      this.searchReq.toDate = this.formatDate(this.toDate);
      this.searchReq.comId = this.lastCompany.id;
      const req = Object.assign({}, this.searchReq);
      if (req.status === 2) {
        req.status = null;
      }
      if (req.taxCheckStatus === 0) {
        req.taxCheckStatus = null;
      }
      req.page = this.searchReq.page - 1;
      this.isResetData = true;
      this.service.getReportProductSales(req).subscribe(
        value => {
          this.data = JSON.parse(JSON.stringify(value.body.data));
          this.listData = [];
          let cursor = 0;
          let listVatRate = [-1, 0, 5, 8, 10, -2];
          let index = 1;
          for (let i = 0; i < value.body.data.detail.length; i++) {
            if (this.listData.length === 0) {
              let title = {
                title: index++ + '. Hàng hoá, dịch vụ không chịu thuế giá trị gia tăng (GTGT)',
                checkTitle: true,
              };
              this.listData.push(title);
            }
            while (
              cursor <= listVatRate.length - 1 &&
              (value.body.data.detail[i].vatRate === listVatRate[cursor] || cursor <= listVatRate.length - 1)
            ) {
              if (value.body.data.detail[i].vatRate !== listVatRate[cursor]) {
                let amount: any;
                if (this.listData[this.listData.length - 1].rowNumber) {
                  amount = {
                    title: 'Cộng nhóm',
                    checkAmount: true,
                    totalTotalPreTax: value.body.data.detail[i - 1].totalTotalPreTax,
                    totalVatAmount: value.body.data.detail[i - 1].totalVatAmount,
                  };
                } else {
                  amount = {
                    title: 'Cộng nhóm',
                    checkAmount: true,
                    totalTotalPreTax: 0,
                    totalVatAmount: 0,
                  };
                }
                this.listData.push(amount);
                cursor++;
                let title: any;
                if (cursor <= listVatRate.length - 1) {
                  if (listVatRate[cursor] === -1) {
                    title = {
                      title: index++ + '. Hàng hoá, dịch vụ không chịu thuế giá trị gia tăng (GTGT)',
                      checkTitle: true,
                    };
                    this.listData.push(title);
                  } else if (listVatRate[cursor] === -2) {
                    title = {
                      title: index++ + '. Hàng hoá, dịch vụ chịu thuế Không kê khai (Không tính thuế)',
                      checkTitle: true,
                    };
                    this.listData.push(title);
                  } else {
                    title = {
                      title: index++ + '. Hàng hoá, dịch vụ chịu thuế suất thuế GTGT ' + listVatRate[cursor] + '%',
                      checkTitle: true,
                    };
                    this.listData.push(title);
                  }
                }
              } else {
                this.listData.push(value.body.data.detail[i]);
                if (i === value.body.data.detail.length - 1) {
                  let amount = {
                    title: 'Cộng nhóm',
                    checkAmount: true,
                    totalTotalPreTax: value.body.data.detail[i].totalTotalPreTax,
                    totalVatAmount: value.body.data.detail[i].totalVatAmount,
                  };
                  this.listData.push(amount);
                  if (cursor <= listVatRate.length - 1) {
                    cursor++;
                  }
                }
                break;
              }
            }
          }
          if (cursor <= listVatRate.length - 1) {
            for (let i = cursor; i < listVatRate.length; i++) {
              let title: any;
              if (listVatRate[i] === -1) {
                title = {
                  title: index++ + '. Hàng hoá, dịch vụ không chịu thuế giá trị gia tăng (GTGT)',
                  checkTitle: true,
                };
                this.listData.push(title);
              } else if (listVatRate[i] === -2) {
                title = {
                  title: index++ + '. Hàng hoá, dịch vụ chịu thuế Không kê khai (Không tính thuế)',
                  checkTitle: true,
                };
                this.listData.push(title);
              } else {
                title = {
                  title: index++ + '. Hàng hoá, dịch vụ chịu thuế suất thuế GTGT ' + listVatRate[i] + '%',
                  checkTitle: true,
                };
                this.listData.push(title);
              }
              let amount: any;
              amount = {
                title: 'Cộng nhóm',
                checkAmount: true,
                totalTotalPreTax: 0,
                totalVatAmount: 0,
              };
              this.listData.push(amount);
            }
          }
          this.totalItems = this.listData.length;
          this.listData = JSON.parse(JSON.stringify(this.listData));
          this.paginateData();
          this.isResetData = false;
        },
        error => {
          this.isResetData = false;
        }
      );
    }
  }

  getContentReport(data: any) {}

  getPattern() {
    this.service.getInvoicePatterns(this.lastCompany.id).subscribe(value => {
      this.listPatterns = value.data;
    });
  }

  formatDate(date) {
    if (!date) return undefined;
    if (date instanceof Date) {
      let year = date.getFullYear();
      let month = ('0' + (date.getMonth() + 1)).slice(-2);
      let day = ('0' + date.getDate()).slice(-2);
      return year + '-' + month + '-' + day;
    } else if (typeof date === 'string') {
      let dateObj = new Date(date);
      let year = dateObj.getFullYear();
      let month = ('0' + (dateObj.getMonth() + 1)).slice(-2);
      let day = ('0' + dateObj.getDate()).slice(-2);
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

  onExportPdf() {
    if (this.listData?.length > 0) {
      this.searchReq.fromDate = this.formatDate(this.fromDate);
      this.searchReq.toDate = this.formatDate(this.toDate);
      this.searchReq.comId = this.lastCompany.id;
      const req = Object.assign({}, this.searchReq);
      if (req.status === 2) {
        req.status = null;
      }
      if (req.taxCheckStatus === 0) {
        req.taxCheckStatus = null;
      }
      this.isSavingPdf = true;
      this.service.exportPdf(req).subscribe(
        value => {
          this.openPdfInNewTab(value.body);
          this.isSavingPdf = false;
        },
        error => {
          this.isSavingPdf = false;
        }
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
      this.searchReq.fromDate = this.formatDate(this.fromDate);
      this.searchReq.toDate = this.formatDate(this.toDate);
      this.searchReq.comId = this.lastCompany.id;
      const req = Object.assign({}, this.searchReq);
      if (req.status === 2) {
        req.status = null;
      }
      if (req.taxCheckStatus === 0) {
        req.taxCheckStatus = null;
      }
      this.isSavingExcel = true;
      this.service.exportExcel(req).subscribe(
        value => {
          this.saveExcelFromByteArray(value.body, 'Thong_Ke_Hang_Hoa_Ban_Ra');
          this.isSavingExcel = false;
        },
        error => {
          this.isSavingExcel = false;
        }
      );
    }
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
  getDataInit() {
    this.fromDate = dayjs(this.params.get('fromDate'));
    this.toDate = dayjs(this.params.get('toDate'));
    this.searchReq.pattern = this.params.get('pattern');
    this.searchReq.status = Number(this.params.get('status'));
    this.searchReq.taxCheckStatus = Number(this.params.get('taxCheckStatus'));
    this.year = Number(this.params.get('year'));
    this.period = Number(this.params.get('period'));
    this.onSearch();
  }

  protected readonly ICON_RESET = ICON_RESET;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_EXCEL = ICON_EXCEL;
  protected readonly ICON_SELECT_PARAM = ICON_SELECT_PARAM;
}
