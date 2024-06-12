import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { CANCEL_ORDER_NOTIFICATION, CUSTOMER_ORDER } from 'app/pages/const/customer-order.const';
import { ConfirmCheckoutComponent } from 'app/pages/pos/customer-order/confirm-checkout/confirm-checkout.component';
import { BillService } from 'app/pages/pos/service/bill.service';
import { ToastrService } from 'ngx-toastr';
import { ShowListActionOrderComponent } from './show-list-action-order/show-list-action-order.component';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { PosInvoiceComponent } from 'app/pages/pos/customer-order/pos-invoice/pos-invoice.component';

@Component({
  selector: 'jhi-view-detail-order',
  templateUrl: './view-detail-order.component.html',
  styleUrls: ['./view-detail-order.component.scss']
})
export class ViewDetailOrderComponent implements OnInit {
  @Input() idOrderSelected!: number;
  @Output() dataOnChange = new EventEmitter<boolean>();
  orderDetail!: any;

  constructor(
    private confirmDialog: ConfirmDialogService,
    public orderService: BillService,
    private route: Router,
    private toast: ToastrService,
    protected modalService: NgbModal
  ) {
  }

  ngOnInit(): void {
    this.getOrderDetailById(this.idOrderSelected);
  }

  getOrderDetailById(id) {
    this.orderService.find(id).subscribe((res: any) => {
      this.orderDetail = res.body.data;
    });
  }

  checkoutOrderCustomer() {
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.orderSelected = this.orderDetail;
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.toast.success('Thanh toán đơn hàng thành công !');
        this.dataOnChange.emit(true);
      }
    });
  }

  showListAction() {
    const dialogRef = this.modalService.open(ShowListActionOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.orderDetail = this.orderDetail;
    dialogRef.closed.subscribe(res => {
      // if(res){this.activeModal.close()}
    });
  }

  modifyOrder() {
    this.route.navigate([`${CUSTOMER_ORDER}${this.orderDetail.id}`]);
  }

  addNewOrder() {
    this.route.navigate([`${CUSTOMER_ORDER}`]);
  }

  printOrder() {
    const dialogRef = this.modalService.open(PosInvoiceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.orderSelected = this.orderDetail;
    dialogRef.componentInstance.printConfigs = null;
  }

  cancelOrder() {
    this.confirmDialog.confirm(CANCEL_ORDER_NOTIFICATION.message, CANCEL_ORDER_NOTIFICATION.title).then(result => {
      if (result) {
        this.orderService.cancel({ billId: this.orderDetail.id, billCode: this.orderDetail.code }).subscribe(
          (res: any) => {
            this.toast.success(res.body.reason);
            this.dataOnChange.emit(true);
          },
          (error: any) => {
            this.toast.warning(error.error.reason);
          }
        );
      }
    });
  }
}
