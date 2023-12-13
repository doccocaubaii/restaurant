import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { IProduct } from '../../model/product.model';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { IBillPayment, InvoiceConfiguration, LastCompany, ProductBill } from '../../model/bill-payment.model';
import { ProductToppingListComponent } from '../product-topping-list/product-topping-list.component';
import { changeProductSelected, checkConditionNotify, updateOrder } from 'app/pages/const/function';
import { InvoiceType } from 'app/pages/const/customer-order.const';

@Component({
  selector: 'jhi-product-topping',
  templateUrl: './product-topping.component.html',
  styleUrls: ['./product-topping.component.scss'],
})
export class ProductOrderToppingComponent implements OnInit {
  @Input() product: IProduct;
  @Input() listProductOrder: ProductBill[];
  @Input() invoiceConfiguration: InvoiceConfiguration;
  @Input() lastCompany: LastCompany;
  @Input() orderSelected: IBillPayment;
  invoiceType = InvoiceType;

  constructor(
    protected productService: ProductService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal,
    public modalService: NgbModal,
    private location: Location
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    if (!this.listProductOrder.length) {
      this.openProductToppingListComponent(this.product);
    }
  }

  openProductToppingListComponent(product?: IProduct, productOrder?: ProductBill, statusUpdateOrder?: boolean) {
    const dialogRef = this.modalService.open(ProductToppingListComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.product = product;
    dialogRef.componentInstance.productOrder = productOrder;
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.closed.subscribe(res => {
      this.activeModal.close({
        productOrder: res,
        statusUpdateOrder: statusUpdateOrder || false,
      });
    });
    dialogRef.dismissed.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  checkConditionNotify() {
    return checkConditionNotify(this.invoiceConfiguration);
  }

  modifyProductOrder(productOrder) {
    this.openProductToppingListComponent(this.product, productOrder, true);
  }

  increaseProductQuantity(productOrder) {
    productOrder.quantity++;
    changeProductSelected(productOrder, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
    updateOrder(this.orderSelected, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  decreaseProductQuantity(productOrder) {
    productOrder.quantity--;
    changeProductSelected(productOrder, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
    updateOrder(this.orderSelected, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  changeProductQuantity(productOrder) {
    changeProductSelected(productOrder, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
    updateOrder(this.orderSelected, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  createNewProductOrder() {
    this.openProductToppingListComponent(this.product);
  }

  closeModal() {
    this.activeModal.dismiss();
  }
}
