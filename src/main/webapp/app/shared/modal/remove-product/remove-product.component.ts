import { Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IBillPayment, ProductBill } from 'app/pages/order/model/bill-payment.model';
import { LastCompany } from './../../../pages/order/model/bill-payment.model';
import {ICON_CANCEL, ICON_DELETE} from "../../other/icon";

@Component({
  selector: 'jhi-remove-product',
  templateUrl: './remove-product.component.html',
  styleUrls: ['./remove-product.component.scss'],
})
export class RemoveProductComponent implements OnInit {
  @Input() productSelected: ProductBill;
  @Input() lastCompany: LastCompany;
  @Input() orderSelected: IBillPayment;
  productQuantityRemove: number;
  @ViewChild('inputQuantity') inputQuantity: ElementRef;
  cancelOrderSelected = false;

  constructor(private activeModal: NgbActiveModal) {}

  ngOnInit(): void {
    // console.log(this.productSelected)
    this.checkCancelOrderSelected();
  }

  checkCancelOrderSelected() {
    let listProductSelected = this.orderSelected.products.filter(product => product.feature === 1);
    if (listProductSelected?.length == 1 && listProductSelected[0].quantityInitial == this.productQuantityRemove) {
      this.cancelOrderSelected = true;
    } else {
      this.cancelOrderSelected = false;
    }
  }

  public decline() {
    this.activeModal.dismiss();
  }

  public accept() {
    this.activeModal.close(this.productQuantityRemove);
  }

  ngAfterViewInit() {
    setTimeout(() => {
      this.inputQuantity?.nativeElement.focus();
    }, 100);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_DELETE = ICON_DELETE;
}
