import { Component, Input, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { CompleteOrder, IBillPayment } from '../../model/bill-payment.model';
import { BillService } from '../../service/bill.service';
import dayjs from 'dayjs/esm';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { PaymentMethod } from 'app/pages/const/customer-order.const';

@Component({
  selector: 'jhi-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrls: ['./confirm-checkout.component.scss'],
})
export class ConfirmCheckoutComponent implements OnInit {
  @Input() orderSelected!: IBillPayment;
  @Output() itemSubmited: EventEmitter<[boolean, string]> = new EventEmitter();
  togglePaymentMethod = false;
  paymentMethod = PaymentMethod;

  @ViewChild('focus') focus: ElementRef;
  constructor(protected orderService: BillService, public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    if (
      this.orderSelected.payment.paymentMethod !== PaymentMethod.transfer &&
      this.orderSelected.payment.paymentMethod !== PaymentMethod.cash
    ) {
      this.togglePaymentMethod = true;
    }
  }

  ngAfterViewInit() {
    this.focus.nativeElement.focus();
  }

  customerPriceChange() {
    const priceRefund = this.orderSelected.payment.amount - this.orderSelected.totalAmount;
  }

  showReasonInput() {
    if (!this.togglePaymentMethod) {
      this.togglePaymentMethod = true;
      this.orderSelected.payment.paymentMethod = '';
    }
  }

  hideReasonInput() {
    this.togglePaymentMethod = false;
  }

  checkout() {
    this.orderSelected.status = 1;

    if (!this.orderSelected.id) {
      this.orderService.create({ ...this.orderSelected, billDate: dayjs() }).subscribe(res => {
        this.activeModal.close([true, '']);
      });
    } else {
      const completeOrderForm: CompleteOrder = {
        billId: this.orderSelected.id,
        billCode: this.orderSelected.code,
        paymentMethod: this.orderSelected.payment.paymentMethod,
        paymentTime: dayjs(),
        amount: this.orderSelected.totalAmount,
      };
      this.orderService.complete(completeOrderForm).subscribe(res => {
        this.activeModal.close([true, '']);
      });
    }
  }

  onCheckboxChange(event) {
    this.orderSelected.payment.amount = 0;
  }

  closeModal() {
    this.orderSelected.payment.amount = 0;
    this.activeModal.dismiss();
  }
}
