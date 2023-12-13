import { Component, Input, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TYPE_DISCOUNT, TypeDiscount } from 'app/pages/const/customer-order.const';
import { ProductBill } from 'app/pages/order/model/bill-payment.model';

@Component({
  selector: 'jhi-discount-product',
  templateUrl: './discount-product.component.html',
  styleUrls: ['./discount-product.component.scss'],
})
export class DiscountProductComponent implements OnInit {
  @Input() product!: ProductBill;
  @Input() productSelectedLocal!: ProductBill;
  typeDiscountProduct = [...TYPE_DISCOUNT];
  typeDiscount = TypeDiscount;
  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    // this.productSelectedLocal = JSON.parse(JSON.stringify(this.product));
    if (!this.productSelectedLocal.typeDiscount) {
      this.productSelectedLocal.typeDiscount = TypeDiscount.VALUE;
    }
  }

  changeTypeDiscountProduct() {
    this.productSelectedLocal.discountPercent = 0;
    this.productSelectedLocal.discountAmount = 0;
  }

  getDiscountAmountProduct() {
    if (this.productSelectedLocal.discountPercent) {
      this.productSelectedLocal.discountAmount = (this.productSelectedLocal.amount * this.productSelectedLocal.discountPercent) / 100;
    }
  }

  checkDiscountProductPercent(event: any) {
    const charCode = event.which ? event.which : event.keyCode;
    if ((charCode > 31 && (charCode < 48 || charCode > 57)) || event.target.value > 10) {
      return false;
    }
    return true;
  }
}
