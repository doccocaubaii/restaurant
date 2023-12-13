import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceConfiguration, LastCompany, ProductBill } from '../../model/bill-payment.model';
import {
  InvoiceDiscount,
  InvoiceType,
  LIST_VAT,
  LIST_VAT_RATE_DISCOUNT_PRODUCT_DETAIL,
  SPDVProduct,
  TYPE_DISCOUNT,
  TypeDiscount,
} from 'app/pages/const/customer-order.const';
import { changeProductSelected, checkInput, getPositiveNumber, numberOnly, take_decimal_number } from 'app/pages/const/function';
import { DiscountProductComponent } from './discount-product/discount-product.component';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/utils/Utils.service';
import { LIST_PRODUCT } from './../../../const/customer-order.const';
import { ICON_CANCEL, ICON_CONFIRM } from '../../../../shared/other/icon';
@Component({
  selector: 'jhi-discount-tax-product',
  templateUrl: './discount-tax-product.component.html',
  styleUrls: ['./discount-tax-product.component.scss'],
})
export class DiscountTaxProductComponent implements OnInit {
  @Input() productSelected!: ProductBill;
  @Input() invoiceConfiguration: InvoiceConfiguration;
  listVat = JSON.parse(JSON.stringify(LIST_VAT));
  listVatRateDiscountOrder = LIST_VAT_RATE_DISCOUNT_PRODUCT_DETAIL;
  invoiceType = InvoiceType;
  invoiceDiscount = InvoiceDiscount;
  spdvProduct = SPDVProduct;
  productSelectedLocal!: ProductBill;
  typeDiscountProduct = TYPE_DISCOUNT;
  typeDiscount = TypeDiscount;
  statusVatRateInput = false;
  lastCompany: LastCompany;
  returnOrder = false;

  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private location: Location,
    private toast: ToastrService,
    protected utilsService: UtilsService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    this.productSelectedLocal = JSON.parse(JSON.stringify(this.productSelected));
    console.log(this.productSelectedLocal);
    if (!this.productSelectedLocal.typeDiscount) {
      this.productSelectedLocal.typeDiscount = TypeDiscount.VALUE;
    }
    for (let i = 0; i < this.listVat.length; i++) {
      if (this.productSelectedLocal.vatRate === -4) {
        this.productSelectedLocal.vatRate = this.listVat[0].value;
        this.statusVatRateInput = false;
        break;
      } else {
        if (this.listVat[i].value === this.productSelectedLocal.vatRate) {
          break;
        } else {
          if (i === this.listVat.length - 1) {
            this.listVat[i].value = this.productSelectedLocal.vatRate;
            this.statusVatRateInput = true;
          }
        }
      }
    }
  }

  closeModal() {
    this.activeModal.close();
  }

  saveDiscountTaxAmount() {
    this.activeModal.close(this.productSelectedLocal);
  }

  changeVat(vatRate) {
    const findIndex = this.listVat.findIndex(item => item.value === vatRate && item.id === this.listVat.length);
    if (findIndex < 0) {
      this.statusVatRateInput = false;
      this.productSelectedLocal.vatRate = vatRate;
      this.productSelectedLocal.toppings?.forEach(topping => {
        topping.vatRate = vatRate;
      });
      this.productSelectedLocal.vatRateName = this.listVat.find(item => item.value === this.productSelectedLocal.vatRate)?.name;
      this.changeProductSelected(this.productSelectedLocal);
    } else {
      this.statusVatRateInput = true;
      this.productSelectedLocal.vatAmount = 0;
      this.productSelectedLocal.vatRateName = 'Khác';
    }
  }

  changeVatDiscountProduct(vatRate) {
    this.productSelectedLocal.vatRate = vatRate;
    this.productSelectedLocal.vatRateName = this.listVat.find(item => item.value === this.productSelectedLocal.vatRate)?.name;
    this.changeProductSelected(this.productSelectedLocal);
  }

  changeVatRateProduct(vatRate: number, event?: any) {
    if (!event) {
      this.listVat[this.listVat.length - 1].value = vatRate || -3;
      this.listVat = [...this.listVat];
      this.productSelectedLocal.vatRate = vatRate || -3;
      this.productSelectedLocal.toppings?.forEach(topping => {
        topping.vatRate = this.productSelectedLocal.vatRate;
      });
      this.changeProductSelected(this.productSelectedLocal);
    } else {
      if (vatRate && (vatRate > 100 || vatRate < 0)) {
        this.productSelectedLocal.vatRate = vatRate > 100 ? 100 : 0;
        this.productSelectedLocal.toppings?.forEach(topping => {
          topping.vatRate = this.productSelectedLocal.vatRate;
        });
        event.target.value = this.productSelectedLocal.vatRate;
        this.listVat[this.listVat.length - 1].value = 100;
        this.listVat = [...this.listVat];
        this.changeProductSelected(this.productSelectedLocal);
      }
    }
  }

  changeDiscountProduct() {
    const dialogRef = this.modalService.open(DiscountProductComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.product = this.productSelectedLocal;
    dialogRef.closed.subscribe(res => {
      if (res) {
        this.productSelectedLocal.typeDiscount = res.typeDiscount;
        if (res.typeDiscount === TypeDiscount.PERCENT) {
          this.productSelectedLocal.discountPercent = res.discountPercent;
          this.productSelectedLocal.discountAmount = take_decimal_number(
            res.quantity * res.unitPrice * (res.discountPercent / 100),
            this.lastCompany.roundScaleAmount
          );
        } else {
          this.productSelectedLocal.discountAmount = res.discountAmount;
        }
        this.changeProductSelected(this.productSelectedLocal);
      }
    });
  }

  changeProductQuantity() {
    if (this.productSelectedLocal.typeDiscount === TypeDiscount.PERCENT) {
      this.productSelectedLocal.discountAmount = take_decimal_number(
        this.productSelectedLocal.quantity * this.productSelectedLocal.unitPrice * ((this.productSelectedLocal.discountPercent ?? 0) / 100),
        this.lastCompany.roundScaleAmount
      );
    }
    this.changeProductSelected(this.productSelectedLocal);
  }

  changeTypeDiscountProduct() {
    this.productSelectedLocal.discountPercent = 0;
    this.productSelectedLocal.discountAmount = 0;
    this.changeProductSelected(this.productSelectedLocal);
  }

  changeProductAmount() {
    this.productSelectedLocal.totalPreTax = this.productSelectedLocal.displayAmount = this.productSelectedLocal.amount;
    this.productSelectedLocal.displayVatAmount = this.productSelectedLocal.vatAmount = take_decimal_number(
      (getPositiveNumber(this.productSelectedLocal.vatRate) * this.productSelectedLocal.totalPreTax) / 100,
      this.lastCompany.roundScaleAmount
    );
    this.productSelectedLocal.displayTotalAmount = this.productSelectedLocal.totalAmount =
      this.productSelectedLocal.totalPreTax + this.productSelectedLocal.vatAmount;
  }

  getDiscountAmountProduct(discountPercent?: number, event?: any) {
    if (!event) {
      // this.productSelectedLocal.discountPercent = discountPercent;
      this.productSelectedLocal.discountAmount = take_decimal_number(
        (this.productSelectedLocal.amount * (this.productSelectedLocal.discountPercent ?? 0)) / 100,
        this.lastCompany.roundScaleAmount
      );
      this.changeProductSelected(this.productSelectedLocal);
    } else {
      if (discountPercent && (discountPercent > 100 || discountPercent < 0)) {
        this.productSelectedLocal.discountPercent = discountPercent > 100 ? 100 : 0;
        event.target.value = this.productSelectedLocal.discountPercent;
        this.productSelectedLocal.discountAmount = take_decimal_number(
          (this.productSelectedLocal.amount * (this.productSelectedLocal.discountPercent ?? 0)) / 100,
          this.lastCompany.roundScaleAmount
        );
        this.changeProductSelected(this.productSelectedLocal);
      }
    }
  }

  checkDiscountTaxProductPercent(event, vatDiscountPercent) {
    const status: any = checkInput(event);
    if (status == 100) {
      this.productSelectedLocal[vatDiscountPercent] = 100;
      if (vatDiscountPercent === 'vatRate') {
        this.listVat[this.listVat.length - 1].value = 100;
      }
      this.getDiscountAmountProduct();
      return false;
    } else {
      if (status) return true;
      return false;
    }
  }

  checkDiscountAmountProduct(event, product) {
    if (parseFloat(event?.target.value.replace(/,/g, '')) > product.amount) {
      this.toast.error('Tiền giảm giá không được lớn hơn thành tiền');
      product.discountAmount = product.amount;
    }
    if (this.productSelectedLocal.typeDiscount === this.typeDiscount.PERCENT) {
      product.discountPercent = (product.discountAmount / product.amount) * 100;
    }
    this.changeProductSelected(product);
  }

  changeProductSelected(productSelected: ProductBill) {
    changeProductSelected(productSelected, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
  }

  convertStringToNumber(value) {
    return parseFloat(value.replace(/,/g, ''));
  }

  preventIncrement(e) {
    if (e.keyCode === 38 || e.keyCode === 40) {
      e.preventDefault();
    }
  }
  checkQuantity(productSelectedLocal) {
    if (productSelectedLocal.quantity === null || productSelectedLocal.quantity === undefined) {
      productSelectedLocal.quantity = 0;
    }
  }

  numberOnly(event: any): boolean {
    return numberOnly(event);
  }

  protected readonly ICON_CONFIRM = ICON_CONFIRM;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
