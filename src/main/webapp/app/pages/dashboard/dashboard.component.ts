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
import { ChartConfiguration, ChartOptions } from 'chart.js';

@Component({
  selector: 'dashboard-v3',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.scss']
})
export class DashboardPage extends BaseComponent implements OnInit {
  global = global;
  infoObject : any = {
    fromDate : '',
    toDate :''
  }

  public lineChartOptions: ChartOptions<'line'> = {
    responsive: false
  };

  public lineChartData: ChartConfiguration<'line'>['data'];

  // Pie
  public pieChartOptions: ChartOptions<'pie'> = {
    responsive: false,
  };
  public pieChartLabels = [];
  public pieChartDatasets = [ {
    data: []
  } ];


  printConfigs = printConfigs;

  constructor(private utilsService: UtilsService, private reportInvoiceService: ReportInvoiceService) {
    super();
  }

  ngOnInit() {
    setTimeout(() => {
      window.dispatchEvent(new Event('resize'));
    }, 500);
    this.infoObject.fromDate = dayjs().subtract(30, 'day');
    this.infoObject.toDate = dayjs();
    this.getRevenueCommonStatus();
  }

  getRevenueCommonStatus() {
    let obj = {
      fromDate : this.infoObject.fromDate.format(DATE_FORMAT),
      toDate : this.infoObject.toDate.format(DATE_FORMAT),
    }
    this.reportInvoiceService.getRevenueCommonStatus(obj)
      .subscribe(res => {
      let data = res.body;
      let labelsLineChart = data.revenue.map(item => item.time);
      let datasetsLineChart = data.revenue.map(item => item.money);
      let pieLabel = data.pieChart.map(item => item.time);
      let pieData = data.pieChart.map(item => item.money);
      console.log(labelsLineChart);
      console.log(datasetsLineChart);
      console.log(pieLabel);
      console.log(pieData);

      this.lineChartData = {
        labels:labelsLineChart,
        datasets: [
          {
            data: datasetsLineChart,
            label: 'Series A',
            fill: true,
            tension: 0.5,
            borderColor: 'black',
            backgroundColor: 'rgba(130,239,160, 0.3)' // rgba(255,0,0,0.3)
          }
        ]
      };
      this.pieChartLabels = pieLabel;
      this.pieChartDatasets = [ {
        data: pieData
      } ];
    });
  }
}
