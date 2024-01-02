import { Component, OnInit } from '@angular/core';
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
import { LIST_STATISTICS } from './../const/customer-order.const';

@Component({
  selector: 'dashboard-v3',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss'],
})
export class DashboardPage extends BaseComponent implements OnInit {
  global = global;
  dataTest: any;

  printConfigs = printConfigs;

  revenueCommonStatus: any;
  revenueCommonStatusBarVertical: any = [];
  lineChartColor: any = { domain: [global.color.blue, global.color.success, global.color.purple, global.color.componentColor] };

  reportInvoiceInput: ReportInvoiceInput = new ReportInvoiceInput();
  listStatistics = LIST_STATISTICS;

  invoiceCommonStatus;

  constructor(private utilsService: UtilsService, private reportInvoiceService: ReportInvoiceService) {
    super();
  }

  async ngOnInit() {
    setTimeout(() => {
      window.dispatchEvent(new Event('resize'));
    }, 500);
    // await this.populate();

    this.resetRevenueCommonStatus();
    this.findByIDTest();
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
        name = (new Date(dataDetail.fromDate).getMonth() + 1).toString();
        break;
      case STATISTICS.date:
        name = new Date(dataDetail.fromDate).getDate() + '-' + (new Date(dataDetail.fromDate).getMonth() + 1);
        break;
      case STATISTICS.hour:
        name = new Date(dataDetail.fromDate).getHours() + 'h-' + new Date(dataDetail.fromDate).getDate();
        break;
      default:
    }
    return name;
  }

  getRevenueCommonStatus() {
    this.reportInvoiceService.getRevenueCommonStatus(this.reportInvoiceInput).subscribe(res => {
      this.revenueCommonStatus = [...this.resetRevenueCommonStatus()];
      this.revenueCommonStatus.forEach((item, index) => {
        res.body.detail.forEach(dataDetail => {
          let name = this.getName(dataDetail);
          item.series.push({ value: index ? dataDetail.profit : dataDetail.revenue, name });
        });
      });

      this.revenueCommonStatusBarVertical = [];
      res.body.detail.forEach(dataDetail => {
        let revenueCommonBarVerticalStatusLocal = this.addNewRevenueCommonBarVerticalStatus();
        let name = this.getName(dataDetail);
        revenueCommonBarVerticalStatusLocal.name = name;
        revenueCommonBarVerticalStatusLocal.series.forEach((serie, index) => {
          serie.value = index ? dataDetail.profit : dataDetail.revenue;
        });
        this.revenueCommonStatusBarVertical.push(revenueCommonBarVerticalStatusLocal);
      });
      this.revenueCommonStatusBarVertical = [...this.revenueCommonStatusBarVertical];
    });
  }

  async findByIDTest() {
    this.dataTest = await this.getCompany();
    this.reportInvoiceInput.comId = this.dataTest.id;
    this.reportInvoiceInput.fromDate = dayjs().subtract(30, 'day');
    this.reportInvoiceInput.toDate = dayjs();
    this.reportInvoiceInput.type = STATISTICS.date;

    this.getRevenueCommonStatus();
  }
}
