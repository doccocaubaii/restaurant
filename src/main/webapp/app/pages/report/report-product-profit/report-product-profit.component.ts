import { Component, OnDestroy, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { ReportProductProfitRes, SearchReportProductProfitReq } from './reportProductProfit';
import { SearchTransactionReq } from '../../warehouse/warehouse';
import { Page } from '../../product/product';
import { ReportProductProfitService } from './report-product-profit.service';
import { startOfMonth } from 'date-fns';
import { UtilsService } from '../../../utils/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { SearchInitRevenueCommonComponent } from '../revenue-common-stats/modal-search-init/modal-search-init.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchInitProductProfitComponent } from './search-init-product-profit/search-init-product-profit.component';
import { ICON_EXCEL, ICON_PDF, ICON_RESET, ICON_SELECT_PARAM } from '../../../shared/other/icon';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-report-product-profit',
  templateUrl: './report-product-profit.component.html',
  styleUrls: ['./report-product-profit.component.scss'],
})
export class ReportProductProfitComponent extends BaseComponent implements OnDestroy, OnInit {
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  lastCompany: any;
  totalItems: any = 0;
  period: any;
  paramCheckAll: any = false;
  listSelected: any = [];
  paramCheckAllPage: any = false;
  keyword: any;
  private modalRef: NgbModalRef | undefined;
  searchReq: SearchReportProductProfitReq = {
    fromDate: '',
    toDate: '',
    page: 1,
    size: 20,
    isPaging: true,
    productProductUnitIds: '',
    sortType: null,
    getAll: false,
    keywordName: '',
    keywordUnit: '',
  };
  reportProductProfitRes: ReportProductProfitRes;
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
  isSavingPdf = false;
  isSavingExcel = false;
  isResetData = false;
  params: any;
  authorExcel = Authority.REPORT_EXPORT_EXCEL;
  authorPdf = Authority.REPORT_EXPORT_PDF;

  constructor(
    private router: Router,
    private toastr: ToastrService,
    private service: ReportProductProfitService,
    private utilService: UtilsService,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  async ngOnInit() {
    this.reportProductProfitRes = new ReportProductProfitRes();
    this.lastCompany = await this.getCompany();
    try {
      this.params = await new Promise(resolve => {
        this.activatedRoute.queryParamMap.subscribe((params: any) => {
          resolve(params);
        });
      });
      this.getDataInit();
    } catch (error) {}
  }

  onOpenModalFilter() {
    this.modalRef = this.modalService.open(SearchInitProductProfitComponent, { size: 'lg', backdrop: 'static' });
    if (this.fromDate) {
      this.modalRef.componentInstance.checkUpdate = true;
      this.modalRef.componentInstance.fromDate = this.fromDate;
    }
    if (this.toDate) {
      this.modalRef.componentInstance.toDate = this.toDate;
    }
    if (this.period != null && this.period != undefined) {
      this.modalRef.componentInstance.period = this.period;
    }
    if (this.listSelected) {
      this.modalRef.componentInstance.listSelected = this.listSelected;
    }
    this.modalRef.componentInstance.sortType = this.searchReq.sortType ? this.searchReq.sortType : 0;
    this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
    this.modalRef.componentInstance.paramCheckAllPage = this.paramCheckAllPage;
    this.modalRef.componentInstance.filterProduct.keywordName = this.searchReq.keywordName;
    this.modalRef.componentInstance.filterProduct.keywordUnit = this.searchReq.keywordUnit;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate) {
        this.fromDate = res.fromDate;
        this.toDate = res.toDate;
        this.period = res.period;
        this.paramCheckAll = res.paramCheckAll;
        this.paramCheckAllPage = res.paramCheckAllPage;
        this.listSelected = res.listSelected;
        this.searchReq.keywordName = res.keywordName;
        this.searchReq.keywordUnit = res.keywordUnit;
        this.searchReq.sortType = res.sortType ? res.sortType : null;
        this.onSearch();
      }
    });
  }

  onSearch() {
    this.searchReq.page = 1;
    this.getData();
  }

  loadMore($event) {
    this.searchReq.page = $event;
    this.getData();
  }

  getData() {
    this.searchReq.fromDate = this.formatDate(this.fromDate);
    this.searchReq.toDate = this.formatDate(this.toDate);
    this.searchReq.comId = this.lastCompany.id;
    this.searchReq.getAll = false;
    this.searchReq.productProductUnitIds = JSON.stringify(this.listSelected);
    const req = Object.assign({}, this.searchReq);
    req.page = this.searchReq.page - 1;
    if (!this.searchReq.fromDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductProfit.error.dateError'),
        this.translateService.instant('easyPos.reportProductProfit.error.message')
      );
    } else if (!this.searchReq.toDate) {
      this.toastr.error(
        this.translateService.instant('easyPos.reportProductProfit.error.dateError'),
        this.translateService.instant('easyPos.reportProductProfit.error.message')
      );
    } else {
      this.isResetData = true;
      this.service.getProductProfitStats(req).subscribe(
        value => {
          this.reportProductProfitRes = value.body.data;
          this.totalItems = value.body.count;
          this.isResetData = false;
        },
        error => {
          this.isResetData = false;
        }
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

  async onExportPdf() {
    if (this.totalItems > 0) {
      this.isSavingPdf = true;
      this.searchReq.getAll = true;
      this.searchReq.page = 0;
      try {
        const value = await this.service.exportPdf(this.searchReq).toPromise();
        this.isSavingPdf = false;
        this.openPdfInNewTab(value.body);
      } catch (error) {
        this.isSavingPdf = false;
        this.toastr.error('Lỗi kết xuất file');
      }
    }
  }

  openPdfInNewTab(byteArray: any): void {
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL, '_blank');
  }

  async onExportExcel() {
    if (this.totalItems > 0) {
      this.isSavingExcel = true;
      this.searchReq.getAll = true;
      this.searchReq.page = 0;
      try {
        const value = await this.service.exportExcel(this.searchReq).toPromise();
        this.saveExcelFromByteArray(value.body, 'Thong_Ke_Loi_Nhuan_San_Pham');
        this.isSavingExcel = false;
      } catch (error) {
        this.isSavingExcel = false;
        this.toastr.error('Lỗi kết xuất file');
      }
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
    this.period = Number(this.params.get('period'));
    this.searchReq.sortType = Number(this.params.get('sortType'));
    this.paramCheckAll = this.params.get('paramCheckAll') === 'true' ? true : false;
    this.paramCheckAllPage = this.params.get('paramCheckAllPage') === 'true' ? true : false;
    this.listSelected = Array.from(JSON.parse(this.params.get('listSelected')));
    this.searchReq.keywordName = this.params.get('keywordName');
    this.searchReq.keywordUnit = this.params.get('keywordUnit');
    this.onSearch();
  }

  protected readonly ICON_RESET = ICON_RESET;
  protected readonly ICON_SELECT_PARAM = ICON_SELECT_PARAM;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_EXCEL = ICON_EXCEL;
}
