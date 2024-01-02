import { Component, ElementRef, OnInit } from '@angular/core';
import { FilterOrder } from '../pos/model/filterOrder.model';
import { BillService } from '../pos/service/bill.service';
import { Page, STATUS_ORDER, StatusOrder } from '../const/customer-order.const';
import { OrderResponse } from '../pos/model/orderResponse.model';
import { HttpHeaders } from '@angular/common/http';
import { TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import dayjs from 'dayjs/esm';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ViewDetailOrderComponent } from './view-detail-order/view-detail-order.component';
@Component({
  selector: 'order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderPage implements OnInit {
  model1: dayjs.Dayjs | any;
  model2: dayjs.Dayjs | any;
  statusOrder = STATUS_ORDER;
  indexItem: number;
  idEditing: number;

  listOrder: OrderResponse[] | null = [];
  filterOrder: FilterOrder = {
    page: Page.PAGE_NUMBER,
    size: Page.PAGE_SIZE,
  };
  constructor(protected orderService: BillService, protected modalService: NgbModal, private elementRef: ElementRef) {}

  ngOnInit() {
    this.getListOrder();
  }

  getListOrder() {
    this.orderService.query(this.filterOrder).subscribe({
      next: (res: any) => {
        this.listOrder = res.body.data;
        this.filterOrder.totalItem = res.body.count;
        this.indexItem = this.filterOrder.size * this.filterOrder.page;
        this.listOrder?.forEach(order => {
          order.statusName = STATUS_ORDER.find(status => status.id === order.status)?.name;
        });
      },
    });
  }

  scrollToTargets(): void {
    console.log('scroll');
    const targetElements = this.elementRef.nativeElement.querySelectorAll('.scroll-target');

    targetElements.forEach(el => {
      el.scrollIntoView({ behavior: 'smooth' });
    });
  }

  filterListOrder(statusId?: number) {
    this.filterOrder.status = statusId;
    this.getListOrder();
  }

  viewOrder(order) {
    this.idEditing = this.idEditing === order.id ? -1 : order.id;
  }

  onChangedPage(event: any): void {
    this.filterOrder.page = event - 1;
    this.getListOrder();
  }

  filterOrderList() {
    this.filterOrder.fromDate = this.model1?.format(DATE_TIME_FORMAT) ?? '';
    this.filterOrder.toDate = this.model2?.format(DATE_TIME_FORMAT) ?? '';
  }

  openViewDetailOrder(order: OrderResponse) {
    const dialogRef = this.modalService.open(ViewDetailOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.idOrderSelected = order.id;
    dialogRef.closed.subscribe(() => {
      this.getListOrder();
    });
  }

  handleEvent(event) {
    if (event) {
      this.idEditing = 0;
      this.ngOnInit();
    }
  }
}
