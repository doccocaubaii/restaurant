import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBillPayment } from 'app/pages/order/model/bill-payment.model';
import { BillService } from 'app/pages/order/service/bill.service';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { ToastrService } from 'ngx-toastr';
import { BILL } from '../../../../../constants/app.routing.constants';

@Component({
  selector: 'jhi-show-list-action-order',
  templateUrl: './show-list-action-order.component.html',
  styleUrls: ['./show-list-action-order.component.scss'],
})
export class ShowListActionOrderComponent implements OnInit {
  @Input() orderDetail!: IBillPayment;

  constructor(
    private confirmDialog: ConfirmDialogService,
    protected orderService: BillService,
    public activeModal: NgbActiveModal,
    private route: Router,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {}

  closeModal() {
    this.activeModal.dismiss();
  }

  modifyOrder() {
    this.activeModal.close(true);
    this.route.navigate([BILL + `/${this.orderDetail.id}`]);
  }

  addNewOrder() {
    this.activeModal.close(true);
    this.route.navigate([BILL]);
  }

  cancelOrder() {
    this.confirmDialog.confirm('Bạn có chắc chắn muốn hủy đơn hàng?').then(result => {
      if (result) {
        this.orderService.cancel({ billId: this.orderDetail.id, billCode: this.orderDetail.code }).subscribe(
          (res: any) => {
            this.toast.success(res.body.reason);
            this.activeModal.close(true);
          },
          (error: any) => {
            this.toast.warning(error.error.reason);
          }
        );
      }
    });
  }
}
