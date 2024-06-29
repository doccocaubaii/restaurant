import { Component, ElementRef, OnDestroy, OnInit } from '@angular/core';
import dayjs from 'dayjs/esm';
import { Page, STATUS_ORDER } from '../const/customer-order.const';
import { OrderResponse } from '../pos/model/orderResponse.model';
import { FilterOrder } from '../pos/model/filterOrder.model';
import { BillService } from '../pos/service/bill.service';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { DATE_TIME_FORMAT } from '../../config/input.constants';
import { ViewDetailOrderComponent } from '../don-hang/view-detail-order/view-detail-order.component';
import { BaseComponent } from '../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { RxStompService } from '../../rxStomp/rx-stomp.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'jhi-kitchen',
  templateUrl: './kitchen.component.html',
  styleUrls: ['./kitchen.component.scss']
})
export class KitchenComponent extends BaseComponent  implements OnInit, OnDestroy {
  model1: dayjs.Dayjs | any;
  model2: dayjs.Dayjs | any;
  statusOrder = STATUS_ORDER;
  indexItem: number;
  idEditing: number;
  private lastCompany: any;
  private topicSubscription: Subscription | undefined;


  listOrder: OrderResponse[] | null = [];
  filterOrder: FilterOrder = {
    page: Page.PAGE_NUMBER,
    size: Page.PAGE_SIZE,
    status : 3
  };
  constructor(protected orderService: BillService, protected modalService: NgbModal, private elementRef: ElementRef,    private rxStompService: RxStompService,

              private toast: ToastrService
  ) {
    super();}

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.topicSubscription = this.rxStompService.watch(`/topic/reload/${this.lastCompany.id}`).subscribe((res: any) => {
      console.log(
        res.body
      );
      this.toast.success("Bạn có yêu cầu mới từ bàn " + res.body);
      this.getListOrder();
    });
    this.getListOrder();
  }
  ngOnDestroy() {
    if (this.topicSubscription) this.topicSubscription.unsubscribe();
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

  tempCreate(order: number, b: number) {
    this.orderService.tempCreate(order, b, this.lastCompany.id).subscribe(
      (res) => {
        if (res.body.status ) {
          this.toast.success(res.body.message.message);
          this.getListOrder();
          console.log(res);
        }
      }
    )
  }
}
