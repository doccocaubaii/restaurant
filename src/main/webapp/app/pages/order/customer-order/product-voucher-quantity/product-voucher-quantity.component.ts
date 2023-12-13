import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBillPayment, LastCompany, ProductBill } from 'app/pages/order/model/bill-payment.model';
import { ICON_SAVE, ICON_CANCEL } from 'app/shared/other/icon';

@Component({
  selector: 'jhi-product-voucher-quantity',
  templateUrl: './product-voucher-quantity.component.html',
  styleUrls: ['./product-voucher-quantity.component.scss'],
})
export class ProductVoucherQuantityComponent implements OnInit {
  @Input() productSelected: ProductBill;
  @Input() lastCompany: LastCompany;
  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    console.log(this.productSelected);
  }

  public decline() {
    this.activeModal.dismiss();
  }

  public accept() {
    this.activeModal.close(this.productSelected);
  }
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
