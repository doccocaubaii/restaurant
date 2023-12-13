import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { BILL_ORDER } from 'app/constants/app.routing.constants';
import { IBillPayment } from 'app/pages/order/model/bill-payment.model';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-area-order',
  templateUrl: './area-order.component.html',
  styleUrls: ['./area-order.component.scss'],
})
export class AreaOrderComponent implements OnInit {
  @Input() unitDetail: any;
  @Input() listOrder: any;
  constructor(public activeModal: NgbActiveModal, private router: Router) {}

  ngOnInit(): void {
    this.unitDetail.usingBills.forEach(orderItem => {
      orderItem.active = false;
      this.listOrder.forEach(order => {
        if (order.id == orderItem.id) {
          orderItem.active = true;
        }
      });
    });
  }

  closeModal() {
    this.activeModal.dismiss();
  }

  createNewOrder() {
    this.activeModal.close();
  }

  getOrder(orderId) {
    this.activeModal.close(orderId);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
