import { Component, ElementRef, OnInit } from '@angular/core';
import { FilterOrder } from '../model/filterOrder.model';
import { BillService } from '../service/bill.service';
import { CUSTOMER_ORDER, CUSTOMER_ORDER_OFFLINE, Page, STATUS_ORDER, StatusOrder } from '../../const/customer-order.const';
import { OrderResponse } from '../model/orderResponse.model';
import { HttpHeaders } from '@angular/common/http';
import { TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import dayjs from 'dayjs/esm';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ViewDetailOrderComponent } from '../order-list/view-detail-order/view-detail-order.component';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { PosInvoiceComponent } from '../customer-order/pos-invoice/pos-invoice.component';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { BaseComponent } from 'app/shared/base/base.component';
import { IBillPayment } from 'app/pages/order/model/bill-payment.model';
import { offline_bill } from 'app/object-stores.constants';
import { LoadingOption } from 'app/utils/loadingOption';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { DialogModal } from 'app/pages/order/model/dialogModal.model';
import { UtilsService } from 'app/utils/Utils.service';

@Component({
  selector: 'jhi-order-offline-management',
  templateUrl: './order-offline-management.component.html',
  styleUrls: ['./order-offline-management.component.scss'],
})
export class OrderOfflineManagementComponent extends BaseComponent implements OnInit {
  model1: dayjs.Dayjs | any;
  model2: dayjs.Dayjs | any;
  statusOrder = STATUS_ORDER;
  indexItem: number;
  idEditing: number;

  listOrder: any[] | null = [];
  filterOrder: FilterOrder = {
    page: Page.PAGE_NUMBER,
    size: Page.PAGE_SIZE,
    isCountAll: true,
  };
  idOrderSelected = 0;
  constructor(
    protected orderService: BillService,
    protected modalService: NgbModal,
    private elementRef: ElementRef,
    private route: Router,
    private toast: ToastrService,
    private confirmDialog: ConfirmDialogService,
    public loading: LoadingOption,
    public utilsService: UtilsService
  ) {
    super();
  }

  ngOnInit() {
    this.getOrderNotAsync();
  }

  async filterListOrder(statusId?: number) {
    let data = await this.findByKeySearch(offline_bill, 'id', 49);
    console.log(data);
  }

  async getOrderNotAsync() {
    this.listOrder = await this.getAllByObjectStore(offline_bill);
    this.listOrder?.forEach(order => {
      order.statusName = STATUS_ORDER.find(status => status.id === order.status)?.name;
    });
    this.filterOrder.totalItem = this.listOrder?.length;
  }

  viewOrder(order) {
    this.idEditing = this.idEditing === order.id ? -1 : order.id;
  }

  onChangedPage(event: any): void {
    this.filterOrder.page = event - 1;
  }

  filterOrderList() {
    this.filterOrder.fromDate = this.model1?.format(DATE_TIME_FORMAT) ?? '';
    this.filterOrder.toDate = this.model2?.format(DATE_TIME_FORMAT) ?? '';
  }

  openViewDetailOrder(order: OrderResponse) {
    const dialogRef = this.modalService.open(ViewDetailOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.idOrderSelected = order.id;
    dialogRef.closed.subscribe();
  }

  handleEvent(event) {
    if (event) {
      this.idEditing = 0;
      this.ngOnInit();
    }
  }

  modifyOrder(idOrder, event) {
    event.stopPropagation();
    this.route.navigate([`${CUSTOMER_ORDER_OFFLINE}${idOrder}`]);
  }

  addNewOrder(event) {
    event.stopPropagation();
    this.route.navigate([`${CUSTOMER_ORDER}`]);
  }

  printOrder(order, event) {
    event.stopPropagation();
    const dialogRef = this.modalService.open(PosInvoiceComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = order;
  }

  syncOrder(order, event) {
    event.stopPropagation();
    this.idOrderSelected = order.id;
    this.orderService.syncOrder([{ ...order, id: null }]).subscribe((res: any) => {
      console.log(res);
      if (res.body.status) {
        this.deleteByID(offline_bill, order.id);
        this.ngOnInit();
        this.toast.success(res.body.reason);
      }
    });
  }

  syncAllOrder(event) {
    event.stopPropagation();
    this.listOrder?.forEach(order => {
      order.id = null;
    });
    this.orderService.syncAllOrder(this.listOrder as any).subscribe((res: any) => {
      if (res.body.status) {
        this.deleteAll(offline_bill);
        this.ngOnInit();
        this.toast.success(res.body.reason);
      }
    });
  }

  cancelOrder(idOrder, event) {
    event.stopPropagation();

    const dialogRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_ORDER,
      ModalContent.DELETE_ORDER,
      ModalBtn.DELETE_ORDER,
      'check',
      'btn-danger'
    );
    dialogRef.componentInstance.formSubmit.subscribe(async res => {
      if (res) {
        dialogRef.close();
        await this.deleteByID(offline_bill, idOrder);
        this.toast.success('Xóa đơn hàng thành công');
        this.ngOnInit();
      }
    });
  }
}
