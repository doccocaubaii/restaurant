import { Component, ElementRef, OnInit } from '@angular/core';
import { FilterOrder } from '../model/filterOrder.model';
import { BillService } from '../service/bill.service';
import { CustomerService } from '../service/customer.service';
import { CUSTOMER_ORDER, Page, STATUS_ORDER, StatusNotify, StatusOrder } from '../../const/customer-order.const';
import { OrderResponse } from '../model/orderResponse.model';
import { HttpHeaders } from '@angular/common/http';
import { TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import dayjs from 'dayjs/esm';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ViewDetailOrderComponent } from './view-detail-order/view-detail-order.component';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { ConfirmCheckoutComponent } from '../customer-order/confirm-checkout/confirm-checkout.component';
import { PosInvoiceComponent } from '../customer-order/pos-invoice/pos-invoice.component';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { lastValueFrom } from 'rxjs';
import { BaseComponent } from 'app/shared/base/base.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { UtilsService } from 'app/utils/Utils.service';
import { BILL_OFFLINE, BILL_ORDER } from 'app/constants/app.routing.constants';
import { formatDate } from '@angular/common';
import { ModalConfirmCancelBillComponent } from './modal-confirm-cancel-bill/modal-confirm-cancel-bill.component';
import { ISocketConfigModel } from 'app/entities/socket/socket-config.model';
import { WebsocketService } from 'app/config/service/websocket-service.service';
import { MqttService } from 'ngx-mqtt';
import { Md5Service, SocketMessage } from '../../../constants/socket.message.constant';
import { Authority } from '../../../config/authority.constants';

@Component({
  selector: 'order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderPage extends BaseComponent implements OnInit {
  model1: dayjs.Dayjs | any;
  model2: dayjs.Dayjs | any;
  listStatusOrder = STATUS_ORDER;
  statusOrder = StatusOrder;
  indexItem: number;
  idEditing: number;

  listOrder: OrderResponse[] | null = [];
  filterOrder: FilterOrder = {
    page: 1,
    size: Page.PAGE_SIZE,
    isCountAll: true,
  };
  statusNotification = true;
  totalOrderOffline?: number = 0;
  lastCompany;

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
  socketMessage: SocketMessage = {
    action: '',
    reason: '',
    data: null,
  };

  authorAdd = Authority.BILL_ADD;
  authorBillView = Authority.BILL_VIEW;
  authorBillUpdate = Authority.BILL_UPDATE;
  authorBillCancel = Authority.BILL_CANCEL;
  authorBillPrintShare = Authority.BILL_PRINT_SHARE;
  authorBillDone = Authority.BILL_DONE;
  authorBillReturn = Authority.BILL_RETURN;
  constructor(
    private mqttService: MqttService,
    private md5Service: Md5Service,
    private websocketService: WebsocketService,
    protected orderService: BillService,
    protected customerService: CustomerService,
    protected modalService: NgbModal,
    protected utilsService: UtilsService,
    private elementRef: ElementRef,
    private route: Router,
    private toast: ToastrService,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  async ngOnInit() {
    this.websocketService.connect();
    this.lastCompany = await this.getCompany();
    this.activatedRoute.queryParamMap.subscribe(params => {
      if (params.get('status')) {
        this.filterOrder.status = +(params.get('status') ?? 0);
      }
      this.getListOrder();
    });
    this.getOrderNotAsync();
  }

  navigateToOrderListOffline() {
    this.route.navigate([`/${BILL_OFFLINE}`]);
  }

  onSearch() {
    this.filterOrder.page = 1;
    this.getListOrder();
  }
  getListOrder() {
    this.orderService.query({ ...this.filterOrder, page: this.filterOrder.page - 1 }).subscribe({
      next: (res: any) => {
        this.listOrder = res.body.data;
        this.filterOrder.totalItem = res.body.count;
        this.indexItem = this.filterOrder.size * (this.filterOrder.page - 1);
        this.listOrder?.forEach(order => {
          order.statusName = STATUS_ORDER.find(status => status.id === order.status)?.name;
          if (order.status == 1 && order.debt) {
            order.statusName = 'Thanh toán ghi nợ';
          }
        });
      },
    });
  }

  onDeleteKeyWord() {
    if (!this.filterOrder.keyword) {
      this.onSearch();
    }
  }
  toggleNotification() {
    this.statusNotification = !this.statusNotification;
  }

  filterListOrder(statusId?: number) {
    this.filterOrder.status = statusId;
    this.filterOrder.page = 1;
    this.handleNavigation(this.filterOrder.page, this.filterOrder.size, this.filterOrder.status);
  }

  async getOrderNotAsync() {
    let listOrder = await this.getAllByObjectStore('offline_bill');
    this.totalOrderOffline = listOrder?.length;
  }

  viewOrder(order) {
    this.idEditing = this.idEditing === order.id ? -1 : order.id;
    console.log(order.id == this.idEditing);
  }

  onChangedPage(event: any): void {
    this.filterOrder.page = event;
    this.handleNavigation(this.filterOrder.page, this.filterOrder.size, this.filterOrder.status);
  }

  filterOrderList() {
    this.filterOrder.fromDate = this.utilsService.convertDate(this.model1) ?? '';
    this.filterOrder.toDate = this.utilsService.convertDate(this.model2) ?? '';
    this.onSearch();
  }

  openViewDetailOrder(order: OrderResponse) {
    const dialogRef = this.modalService.open(ViewDetailOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.idOrderSelected = order.id;
    dialogRef.closed.subscribe(() => {
      this.getListOrder();
    });
  }

  handleEvent(event) {
    if (event) {
      this.idEditing = 0;
      this.onSearch();
    }
  }

  returnOrderCustomer(idOrder, event) {
    event.stopPropagation();
    this.route.navigate([`/${BILL_ORDER}`], {
      relativeTo: this.activatedRoute,
      queryParams: {
        id_order: idOrder,
        status_order: this.statusOrder.RETURNED,
        display: 1,
      },
    });
  }

  async checkoutOrderCustomer(idOrder, customerId, event) {
    event.stopPropagation();
    let orderDetail: any = await lastValueFrom(this.orderService.find(idOrder));
    let customerDetail: any = await lastValueFrom(this.customerService.find(customerId));
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = {
      ...orderDetail.body.data,
      pointBalanceCustomer: customerDetail.body.data.pointBalance,
      moneyBalanceCustomer: customerDetail.body.data.moneyBalance,
      cardCustomerInfo: customerDetail.body.data.cardInformation,
      payment: { ...orderDetail.body.data.payment, cardAmount: 0 },
    };
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.toast.success('Thanh toán đơn hàng thành công !');
        this.orderService.find(idOrder).subscribe((result: any) => {
          const posInvoiceRef = this.modalService.open(PosInvoiceComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
          posInvoiceRef.componentInstance.orderSelected = result.body.data;
          posInvoiceRef.componentInstance.type = 1;
          posInvoiceRef.closed.subscribe(() => {
            this.onSearch();
          });
        });
      }
    });
  }

  modifyOrder(idOrder, event) {
    event.stopPropagation();
    this.route.navigate([`/${BILL_ORDER}`], {
      relativeTo: this.activatedRoute,
      queryParams: {
        id_order: idOrder,
        display: 1,
      },
    });
  }

  compactDate(date) {
    const myDate = new Date(date);
    const formattedDate = formatDate(myDate, 'dd-MM-yyyy', 'en-US');
    return formattedDate;
  }

  addNewOrder(event) {
    event.stopPropagation();
    this.route.navigate([`/${BILL_ORDER}`], {
      relativeTo: this.activatedRoute,
      queryParams: {
        display: 1,
      },
    });
  }

  async printOrder(idOrder, event) {
    event.stopPropagation();
    let orderDetail: any = await lastValueFrom(this.orderService.find(idOrder));
    orderDetail.body.data.products.forEach(product => [(product.vatRateName = 'Thuế suất ' + product.vatRate + '%')]);
    const dialogRef = this.modalService.open(PosInvoiceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.orderSelected = orderDetail.body.data;
    dialogRef.componentInstance.type = orderDetail.body.data.status === 0 ? 0 : 1;
  }

  async cancelOrder(idOrder, event, order) {
    event.stopPropagation();
    if (order.status === 1) {
      let orderDetail: any = await lastValueFrom(this.orderService.find(idOrder));
      const dialogRef = this.modalService.open(ConfirmCheckoutComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.orderSelected = orderDetail.body.data;
      dialogRef.componentInstance.lastCompany = this.lastCompany;
      dialogRef.componentInstance.isCancelOrder = true;
      dialogRef.closed.subscribe(res => {
        if (res === 1) {
          const dialogRef = this.modalService.open(ModalConfirmCancelBillComponent, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'margin-5',
          });
          dialogRef.closed.subscribe(async res => {
            if (res === 1) {
              this.orderService
                .billCancel({
                  comId: orderDetail.body.data.comId,
                  billId: orderDetail.body.data.id,
                  paymentMethod: orderDetail.body.data.payment.paymentMethod,
                })
                .subscribe(value => {
                  this.toast.success(value.message[0].message);
                  this.onSearch();
                });
            }
          });
        }
        if (res === 2) {
          this.onSearch();
        }
      });
    } else {
      let orderResponse: any = await lastValueFrom(this.orderService.find(idOrder));

      const dialogRef = this.modalService.open(ConfirmDialogComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.value = new DialogModal(
        ModalHeader.DELETE_ORDER,
        ModalContent.DELETE_ORDER,
        ModalBtn.DELETE_ORDER,
        'check',
        'btn-delete'
      );
      dialogRef.componentInstance.formSubmit.subscribe(res => {
        if (res) {
          let orderDetail = orderResponse.body.data;
          this.orderService.cancel({ billId: idOrder, billCode: orderDetail.code }).subscribe((res: any) => {
            dialogRef.close();
            this.toast.success(res.body.reason);
            let itemSendSocket: ISocketConfigModel = new ISocketConfigModel();
            itemSendSocket.message = 'Thông báo bếp' + orderDetail.code;
            itemSendSocket.type = StatusNotify.cancel;
            itemSendSocket.refId = orderDetail.id.toString();
            itemSendSocket.reason = 'Gửi tool in in hộ';
            itemSendSocket.comId = this.lastCompany.id;
            this.websocketService.sendMessage(itemSendSocket);
            this.onSearch();
          });
        }
      });
    }
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.filterOrder.totalItem = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
  }

  protected handleNavigation(page: number, size: number, status?: number): void {
    const queryParamsObj = {
      page,
      size,
      status,
    };
    this.route.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }
  // returnOrder(idOrder, event) {
  //   event.stopPropagation();
  //   this.route.navigate([`/${BILL_ORDER}`], {
  //     relativeTo: this.activatedRoute,
  //     queryParams: {
  //       id_order: idOrder,
  //       return: true,
  //     },
  //   });
  // }
}
