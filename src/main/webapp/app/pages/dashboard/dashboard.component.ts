import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import global from '../../config/globals';
import { BaseComponent } from '../../shared/base/base.component';
import { HttpResponse } from '@angular/common/http';
import { db } from '../../db';
import { UtilsService } from '../../utils/Utils.service';
import Dexie from 'dexie';
import { last_company, printConfigs } from '../../object-stores.constants';
import { ReportInvoiceService } from './service/report-invoice.service';
import { ProductSaleStatusInput, ReportInvoiceInput } from './model/report-invoice.model';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT } from 'app/config/input.constants';
import { STATISTICS } from '../const/customer-order.const';
import { LIST_STATISTICS, LIST_STATISTICS_PRODUCT } from './../const/customer-order.const';
import { ActivatedRoute, Router } from '@angular/router';
import { INVOICE } from '../../constants/app.routing.constants';
import { ActivityHistoryService } from '../report/activity-history/activity-history.service';
import { ReportActivityHistoryReq } from './model/report-activity-history';
import { debounce } from 'lodash';
import {ICON_PROFIT} from "../../shared/other/icon";

@Component({
  selector: 'dashboard-v3',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardPage extends BaseComponent implements OnInit {
  @ViewChild('activityHistory') activityHistoryRef!: ElementRef;
  global = global;
  company: any;

  printConfigs = printConfigs;

  lineChartData: any = [];
  revenueCommonStatus: any;
  revenueCommonStatusBarVertical: any = [];
  lineChartColor: any = { domain: ['#0074bd', '#eb5624'] };
  lastCompany: any;
  fromDateActvity: dayjs.Dayjs | any;
  toDateActvity: dayjs.Dayjs | any;
  pieChartData: any = [
    { name: 'Germany', value: 8940000 },
    { name: 'USA', value: 5000000 },
    { name: 'France', value: 7200000 },
  ];
  pieChartColor: any = { domain: [global.color.red, global.color.orange, global.color.componentColor] };

  productSaleStatusInput: ProductSaleStatusInput = new ProductSaleStatusInput();
  reportInvoiceInput: ReportInvoiceInput = new ReportInvoiceInput();
  productHotSaleInput: ReportInvoiceInput = new ReportInvoiceInput();
  inventoryInput;
  activityHistoryReq: ReportActivityHistoryReq = { fromDate: '', toDate: '' };
  listStatistics = LIST_STATISTICS;
  listStatisticsProduct = LIST_STATISTICS_PRODUCT;
  statusTab: any = 0;
  listActivity: any = [];

  billCommonStatus;
  invoiceCommonStatus;
  revenueCostPriceByProduct;
  generalInputOutputInventory;
  inventoryCommonStatus;
  recentActivites;
  productProfitStatus;
  productSaleStatus;
  productHotSale;
  revenueCommonStats;
  reloadHome;
  constructor(
    public utilsService: UtilsService,
    private reportInvoiceService: ReportInvoiceService,
    public router: Router,
    protected activatedRoute: ActivatedRoute,
    private activityHistoryService: ActivityHistoryService
  ) {
    super();
    this.productHotSale = [];
    this.utilsService.statusConnectingWebSocket$.subscribe(data => {
      this.ngOnInit();
    });
  }

  async ngOnInit() {
    setTimeout(() => {
      window.dispatchEvent(new Event('resize'));
    }, 500);
    // await this.populate();

    this.resetRevenueCommonStatus();
    this.findByIDTest();
    this.lastCompany = await this.getCompany();
    this.fromDateActvity = dayjs().subtract(7, 'day');
    this.toDateActvity = dayjs();
    this.getActivityHistory(false);
  }

  resetReportInvoiceInput() {
    this.reportInvoiceInput.fromDate = dayjs().startOf('month');
    this.reportInvoiceInput.toDate = dayjs();
    this.reportInvoiceInput.suggestValue = 3;
    this.getRevenueCommonStatus();
  }

  resetProductHotSale() {
    this.productHotSaleInput.fromDate = dayjs().startOf('month');
    this.productHotSaleInput.toDate = dayjs();
    this.productHotSaleInput.type = 0;
  }

  setPadding(input: number): number {
    if (input <= 2) {
      return 260;
    } else if (input === 3) {
      return 120;
    } else if (input === 4) {
      return 80;
    } else if (input >= 8) {
      return 1;
    } else {
      return 80 - (input - 4) * 25;
    }
  }

  changeStatistic(event) {
    switch (event.id) {
      case 1:
        this.reportInvoiceInput.fromDate = dayjs();
        this.reportInvoiceInput.toDate = dayjs();
        break;
      case 2:
        this.reportInvoiceInput.fromDate = dayjs().startOf('week').add(1, 'day');
        this.reportInvoiceInput.toDate = dayjs();
        break;
      case 3:
        this.reportInvoiceInput.fromDate = dayjs().startOf('month');
        this.reportInvoiceInput.toDate = dayjs();
        break;
      case 4:
        this.reportInvoiceInput.fromDate = dayjs().startOf('year');
        this.reportInvoiceInput.toDate = dayjs();
        break;
      case 5:
        this.reportInvoiceInput.fromDate = dayjs().subtract(1, 'day');
        this.reportInvoiceInput.toDate = dayjs().subtract(1, 'day');
        break;
      case 6:
        this.reportInvoiceInput.fromDate = dayjs().subtract(6, 'day');
        this.reportInvoiceInput.toDate = dayjs();
        break;
      case 7:
        this.reportInvoiceInput.fromDate = dayjs().subtract(30, 'day');
        this.reportInvoiceInput.toDate = dayjs();
        break;
      default:
      // code block
    }

    this.getRevenueCommonStatus();
  }

  resetRevenueCommonStatus() {
    const revenueCommonStatusLocal = [
      {
        name: 'Doanh thu',
        series: [],
      },
      {
        name: 'Lợi nhuận',
        series: [],
      },
    ];
    return revenueCommonStatusLocal;
  }

  resetRevenueCommonBarVerticalStatus() {
    return [];
  }

  addNewRevenueCommonBarVerticalStatus() {
    return {
      name: '',
      series: [
        {
          name: 'Doanh thu',
          value: 0,
        },
        {
          name: 'Lợi nhuận',
          value: 0,
        },
      ],
    };
  }

  getName(dataDetail) {
    let name;
    switch (this.reportInvoiceInput.type) {
      case STATISTICS.month:
        name = (new Date(dataDetail.fromDate).getMonth() + 1).toString() + '/' + new Date(dataDetail.fromDate).getFullYear();
        break;
      case STATISTICS.date:
        name = new Date(dataDetail.fromDate).getDate() + '/' + (new Date(dataDetail.fromDate).getMonth() + 1);
        break;
      case STATISTICS.hour:
        name = dataDetail.fromDate.split(':')[0] + 'h';
        break;
      default:
    }
    return name;
  }

  getRevenueCommonStatus() {
    if (this.reportInvoiceInput.fromDate.format('YYYY-MM-DD') == this.reportInvoiceInput.toDate.format('YYYY-MM-DD')) {
      this.reportInvoiceInput.type = STATISTICS.hour;
    } else {
      if (this.reportInvoiceInput.fromDate.format('YYYY-MM') == this.reportInvoiceInput.toDate.format('YYYY-MM')) {
        this.reportInvoiceInput.type = STATISTICS.date;
      } else {
        this.reportInvoiceInput.type = STATISTICS.month;
      }
    }

    this.reportInvoiceService.getRevenueCommonStatus(this.reportInvoiceInput).subscribe(res => {
      this.revenueCommonStats = res.body;
      this.revenueCommonStatus = [...this.resetRevenueCommonStatus()];
      this.revenueCommonStatus.forEach((item, index) => {
        res.body.detail.forEach(dataDetail => {
          const name = this.getName(dataDetail);
          item.series.push({ value: index ? dataDetail.profit : dataDetail.revenue, name });
        });
      });

      this.revenueCommonStatusBarVertical = [];
      res.body.detail.forEach(dataDetail => {
        const revenueCommonBarVerticalStatusLocal = this.addNewRevenueCommonBarVerticalStatus();
        const name = this.getName(dataDetail);
        revenueCommonBarVerticalStatusLocal.name = name;
        revenueCommonBarVerticalStatusLocal.series.forEach((serie, index) => {
          serie.value = index ? dataDetail.profit : dataDetail.revenue;
        });
        this.revenueCommonStatusBarVertical.push(revenueCommonBarVerticalStatusLocal);
      });
      this.revenueCommonStatusBarVertical = [...this.revenueCommonStatusBarVertical];
    });
  }

  getBillCommonStatus() {
    this.reportInvoiceService.getBillCommonStatus(this.reportInvoiceInput).subscribe(res => {
      this.billCommonStatus = res.body;
    });
  }

  getInvoiceCommonStatus() {
    this.reportInvoiceService.getInvoiceCommonStatus(this.reportInvoiceInput).subscribe(res => {
      // this.invoiceCommonStatus = [
      //   {
      //     name: 'Tổng số hóa đơn',
      //     value: res.body.allCount,
      //   },
      //   {
      //     name: 'Hóa đơn chưa phát hành',
      //     value: res.body.newCount,
      //   },
      //   {
      //     name: 'Hóa đơn đang xử lý',
      //     value: res.body.processingCount,
      //   },
      //   {
      //     name: 'Hóa đơn xử lý thành công',
      //     value: res.body.doneCount,
      //   },
      // ];
      this.invoiceCommonStatus = res.body;
    });
  }

  getRevenueCostPriceByProduct() {
    this.reportInvoiceService.getRevenueCostPriceByProduct(this.reportInvoiceInput).subscribe(res => {
      this.revenueCostPriceByProduct = res.body;
    });
  }

  getGeneralInputOutputInventory() {
    this.reportInvoiceService.getGeneralInputOutputInventory(this.reportInvoiceInput).subscribe(res => {
      this.generalInputOutputInventory = res.body;
    });
  }

  getInventoryCommonstatus() {
    this.reportInvoiceService.getInventoryCommonstatusV2(this.inventoryInput).subscribe(res => {
      this.inventoryCommonStatus = res.body;
    });
  }

  getRecentActivites() {
    this.reportInvoiceService.getRecentActivites(this.reportInvoiceInput).subscribe(res => {
      this.recentActivites = res.body;
    });
  }

  getProductProfitStatus() {
    this.reportInvoiceService.getProductProfitStatus(this.reportInvoiceInput).subscribe(res => {
      this.productProfitStatus = res.body;
    });
  }

  getProductSaleStatus() {
    this.reportInvoiceService.getProductSaleStatus(this.productSaleStatus).subscribe(res => {
      this.productSaleStatus = res.body;
    });
  }

  getProductHotSaleStatus() {
    this.reportInvoiceService.getProductHotSaleStatus(this.productHotSaleInput).subscribe(res => {
      this.productHotSale = res.body.detail;
    });
  }

  getDataOfflinePrintConfigs() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflinePrintConfigs(16).subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineProductGroups() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflineProductGroups().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineProducts() {
    return new Promise(resolve => {
      this.utilsService.getDataOfflineProducts().subscribe((res: HttpResponse<any> | any) => {
        return resolve(res.body.data);
      });
    });
  }

  getDataOfflineCustomers() {
    return new Promise(resolve => {
      this.utilsService
        .getDataOfflineCustomers({
          type: 1,
        })
        .subscribe((res: HttpResponse<any> | any) => {
          return resolve(res.body.data);
        });
    });
  }

  searchByType(number?: number) {
    const queryParamsObj = {
      type: number,
    };
    this.router.navigate([INVOICE], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  async findByIDTest() {
    this.company = await this.getCompany();
    this.reportInvoiceInput.comId = this.company.id;
    this.productHotSaleInput.comId = this.company.id;

    this.inventoryInput = { comId: this.company.id };

    this.resetReportInvoiceInput();
    this.resetProductHotSale();

    this.getRevenueCommonStatus();
    this.getProductHotSaleStatus();
    this.getBillCommonStatus();
    this.getInvoiceCommonStatus();

    // this.getRevenueCostPriceByProduct();
    // this.getGeneralInputOutputInventory();
    this.getInventoryCommonstatus();
    // this.getRecentActivites();
    // this.getProductProfitStatus();
    // this.getProductSaleStatus();
  }
  getActivityHistory(checkScroll: any) {
    this.activityHistoryReq.fromDate = this.formatDate(this.fromDateActvity);
    this.activityHistoryReq.toDate = this.formatDate(this.toDateActvity);
    this.activityHistoryReq.comId = this.lastCompany.id;
    this.activityHistoryService.getActivityHistory(this.activityHistoryReq).subscribe(value => {
      if (!checkScroll) {
        this.listActivity = value.body.data.detail[0];
      } else {
        this.listActivity = [...this.listActivity, ...value.body.data.detail[0]];
      }
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

  loadMore = debounce(($event: any) => {
    const target = $event.target;
    if (target.scrollTop === 0) {
      return;
    }
    if (target.offsetHeight + target.scrollTop >= target.scrollHeight - 20) {
      this.activityHistoryReq.page++;
      this.getActivityHistory(true);
    }
  }, 100);
  protected readonly ICON_PROFIT = ICON_PROFIT;
}
