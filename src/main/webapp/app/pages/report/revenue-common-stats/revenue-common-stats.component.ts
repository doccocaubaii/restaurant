import { Component, OnDestroy, OnInit } from '@angular/core';
import { Detail, Parent, RevenueCommonRes, SearchRevenueCommonReq } from './revenue-common';
import dayjs from 'dayjs/esm';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from '../../../utils/Utils.service';
import { ExportCommon } from '../../export/export.common';
import { RevenueCommonStatsService } from './revenue-common-stats.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { SearchInitRevenueCommonComponent } from './modal-search-init/modal-search-init.component';
import { ActivatedRoute } from '@angular/router';
import { ICON_EXCEL, ICON_PDF, ICON_PROFIT_ORANGE, ICON_RESET, ICON_REVENUE, ICON_SELECT_PARAM } from '../../../shared/other/icon';
import { Chart } from 'chart.js';
import moment from 'moment';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'jhi-revenue-common-stats',
  templateUrl: './revenue-common-stats.component.html',
  styleUrls: ['./revenue-common-stats.component.scss'],
})
export class RevenueCommonStatsComponent extends BaseComponent implements OnDestroy, OnInit {
  reportRevenueCommonRes: RevenueCommonRes = new RevenueCommonRes();
  details: Parent[] = [];
  searchReq: SearchRevenueCommonReq = {
    page: 1,
    size: 20,
    type: 1,
    isPaging: false,
    fromDate: '',
    toDate: '',
    fromHour: null,
    toHour: null,
    isChart: false,
  };
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  totalItems: any = 0;
  lastCompany: any;
  period: any;
  checkHours: any;
  view: [number, number] = [500, 450];
  private modalRef: NgbModalRef | undefined;
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
  isSavingPdf = false;
  isSavingExcel = false;
  isResetData = false;
  public chart: Chart;
  lineChartColor: any = { domain: ['#0074bd', '#eb5624'] };
  authorExcel = Authority.REPORT_EXPORT_EXCEL;
  authorPdf = Authority.REPORT_EXPORT_PDF;
  constructor(
    private toastr: ToastrService,
    private translateService: TranslateService,
    public exportComponent: ExportCommon,
    private service: RevenueCommonStatsService,
    protected utilsService: UtilsService,
    protected modalService: NgbModal,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  async ngOnInit() {
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
  getDataInit() {
    this.fromDate = dayjs(this.params.get('fromDate'));
    this.toDate = dayjs(this.params.get('toDate'));
    this.searchReq.type = Number(this.params.get('type'));
    this.period = Number(this.params.get('period'));
    this.checkHours = Number(this.params.get('isHours'));
    this.searchReq.fromHour = this.params.get('fromHour');
    this.searchReq.toHour = this.params.get('toHour');
    this.onSearch();
  }

  onOpenModalFilter() {
    this.modalRef = this.modalService.open(SearchInitRevenueCommonComponent, { size: 'lg', backdrop: 'static' });
    if (this.fromDate) {
      this.modalRef.componentInstance.checkUpdate = true;
      this.modalRef.componentInstance.fromDate = this.fromDate;
    }
    if (this.toDate) {
      this.modalRef.componentInstance.toDate = this.toDate;
    }
    if (this.searchReq.type != null && this.searchReq.type != undefined) {
      this.modalRef.componentInstance.type = this.searchReq.type;
    }
    if (this.period != null && this.period != undefined) {
      this.modalRef.componentInstance.period = this.period;
    }
    if (this.checkHours) {
      this.modalRef.componentInstance.checkHours = this.checkHours;
    }
    if (this.searchReq.fromHour) {
      this.modalRef.componentInstance.fromHour = this.searchReq.fromHour;
    }
    if (this.searchReq.toHour) {
      this.modalRef.componentInstance.toHour = this.searchReq.toHour;
    }
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.fromDate) {
        this.fromDate = res.fromDate;
        this.toDate = res.toDate;
        this.searchReq.type = res.type;
        this.period = res.period;
        this.searchReq.fromHour = res.fromHour;
        this.searchReq.toHour = res.toHour;
        this.checkHours = res.checkHours;
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
    const req = Object.assign({}, this.searchReq);
    req.page = this.searchReq.page - 1;
    req.isChart = true;
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
      this.isResetData = true;
      this.service.getReportRevenueCommon(req).subscribe(
        value => {
          this.reportRevenueCommonRes = new RevenueCommonRes();
          this.reportRevenueCommonRes = JSON.parse(JSON.stringify(value.body.data));
          this.totalItems = value.body.count;
          this.isResetData = false;
          let label: any = [];
          let revenue: any = [];
          let profit: any = [];
          this.reportRevenueCommonRes.detail.forEach(value => {
            if (value.revenue !== null && value.revenue !== undefined) {
              revenue.push(value.revenue);
            } else {
              revenue.push(0);
            }
            if (value.profit !== null && value.profit !== undefined) {
              profit.push(value.profit);
            } else {
              profit.push(0);
            }
            if (this.searchReq.type === 1) {
              if (value.fromDate && value.toDate) {
                if (this.getMonth(value.fromDate) === this.getMonth(value.toDate)) {
                  let label1 = 'Tháng ' + this.getMonth(value.fromDate);
                  label.push(label1);
                } else {
                  let labelFrom = this.formatDateV2(value.fromDate, 'YYYY-MM-DD', 'DD/MM');
                  let labelTo = this.formatDateV2(value.toDate, 'YYYY-MM-DD', 'DD/MM');
                  let label1 = labelFrom + ' - ' + labelTo;
                  label.push(label1);
                }
              }
            } else if (this.searchReq.type === 2) {
              let label2 = this.formatDateV2(value.fromDate, 'YYYY-MM-DD HH:mm:ss', 'DD/MM');
              label.push(label2);
            } else {
              let label3 = this.formatDateV2(value.fromDate, 'HH:mm:ss', 'HH') + 'h';
              label.push(label3);
            }
          });
          this.chart = new Chart('canvas', {
            type: 'bar',
            data: {
              labels: label,
              datasets: [
                {
                  label: 'Doanh thu',
                  data: revenue,
                  backgroundColor: '#e6f1f8',
                  borderColor: '#0074BD',
                  borderWidth: 1,
                  maxBarThickness: 70,
                },
                {
                  label: 'Lợi nhuận',
                  data: profit,
                  backgroundColor: '#fdeee9',
                  borderColor: '#EB5624',
                  borderWidth: 1,
                  maxBarThickness: 70,
                },
              ],
            },
            options: {
              scales: {
                yAxes: [
                  {
                    ticks: {
                      beginAtZero: true,
                    },
                  },
                ],
                xAxes: [
                  {
                    ticks: {
                      autoSkip: false,
                      maxRotation: this.searchReq.type !== 3 ? this.getRotationAngle(this.reportRevenueCommonRes.detail) : 0,
                      minRotation: this.searchReq.type !== 3 ? this.getRotationAngle(this.reportRevenueCommonRes.detail) : 0,
                    },
                  },
                ],
              },
              responsive: true,
              maintainAspectRatio: false,
            },
          });
        },
        error => {
          this.isResetData = false;
        }
      );
    }
  }

  getMonth(date: any) {
    let dateObj = new Date(date);
    return ('0' + (dateObj.getMonth() + 1)).slice(-2);
  }

  getRotationAngle(data, threshold = 20) {
    // Nếu số lượng cột lớn hơn hoặc bằng ngưỡng, trả về 90 độ
    if (data.length >= threshold) {
      return 45;
    }
    // Nếu không, trả về 0 độ
    else {
      return 0;
    }
  }
  setView(details: any) {
    if (details.length === 1) {
      this.view = [350, 450];
    } else if (details.length === 2) {
      this.view = [400, 450];
    } else if (details.length === 3) {
      this.view = [500, 450];
    } else if (details.length <= 4) {
      this.view = [800, 450];
    } else if (details.length <= 6) {
      this.view = [1200, 450];
    } else if (details.length <= 8) {
      this.view = [1500, 450];
    } else {
      this.view = [1700, 450];
    }
  }

  searchByType(type: any) {
    this.searchReq.type = type;
    this.getData();
  }
  onExportPdf() {
    if (this.reportRevenueCommonRes.detail.length > 0) {
      this.searchReq.isPaging = false;
      this.isSavingPdf = true;
      this.service.exportPdf(this.searchReq).subscribe(
        value => {
          this.isSavingPdf = false;
          this.openPdfInNewTab(value.body);
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
    if (this.reportRevenueCommonRes.detail.length > 0) {
      this.searchReq.isPaging = false;
      this.isSavingExcel = true;
      this.service.exportExcel(this.searchReq).subscribe(
        value => {
          this.isSavingExcel = false;
          this.saveExcelFromByteArray(value.body, 'Thong_ke_loi_nhuan(uoc_tinh)');
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
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(blob);
    const link = document.createElement('a');
    document.body.appendChild(link);
    link.download = fileURL;
    link.setAttribute('style', 'display: none');
    link.setAttribute('download', fileName + '.pdf');
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

  formatDateV2(date: any, fromCovert, toConvert): any {
    return moment(date, fromCovert).format(toConvert);
  }

  protected readonly ICON_SELECT_PARAM = ICON_SELECT_PARAM;
  protected readonly ICON_RESET = ICON_RESET;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_EXCEL = ICON_EXCEL;
  protected readonly ICON_REVENUE = ICON_REVENUE;
  protected readonly ICON_PROFIT_ORANGE = ICON_PROFIT_ORANGE;
}
