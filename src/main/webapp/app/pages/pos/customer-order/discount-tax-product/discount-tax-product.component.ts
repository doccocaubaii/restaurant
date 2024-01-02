import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductBill } from '../../model/bill-payment.model';
import { Location } from '@angular/common';

@Component({
  selector: 'jhi-discount-tax-product',
  templateUrl: './discount-tax-product.component.html',
  styleUrls: ['./discount-tax-product.component.scss'],
})
export class DiscountTaxProductComponent implements OnInit {
  @Input() productSelected!: ProductBill;
  productSelectedLocal!: ProductBill;

  constructor(public activeModal: NgbActiveModal, protected modalService: NgbModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    this.productSelectedLocal = JSON.parse(JSON.stringify(this.productSelected));
  }

  closeModal() {
    this.activeModal.close();
  }

  saveDiscountTaxAmount() {
    this.activeModal.close(this.productSelectedLocal);
  }

  changeVatRateProduct(productSelectedLocal: ProductBill) {
    this.changeProductSelected(productSelectedLocal);
  }

  changeTypeDiscountProduct() {
    this.changeProductSelected(this.productSelectedLocal);
  }

  getDiscountAmountProduct() {
    this.changeProductSelected(this.productSelectedLocal);
  }

  checkDiscountTaxProductPercent(event: any) {
    const charCode = event.which ? event.which : event.keyCode;
    if ((charCode > 31 && (charCode < 48 || charCode > 57)) || event.target.value > 10) {
      return false;
    }
    return true;
  }

  changeProductSelected(newProductSelected: ProductBill) {
    newProductSelected.amount = newProductSelected.quantity * newProductSelected.unitPrice;
    newProductSelected.totalAmount = newProductSelected.amount;
  }
}
