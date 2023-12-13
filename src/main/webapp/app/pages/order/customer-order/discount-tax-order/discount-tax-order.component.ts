import { Component, Input, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { Location } from '@angular/common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IBillPayment, InvoiceConfiguration, LastCompany, ProductBill } from '../../model/bill-payment.model';
import { InvoiceType, LIST_VAT, TYPE_DISCOUNT, TypeDiscount } from 'app/pages/const/customer-order.const';
import { checkInput, getPositiveNumber, numberOnly, take_decimal_number } from 'app/pages/const/function';
import { UtilsService } from 'app/utils/Utils.service';
import { ToastrService } from 'ngx-toastr';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../../shared/other/icon";

@Component({
  selector: 'jhi-discount-order',
  templateUrl: './discount-tax-order.component.html',
  styleUrls: ['./discount-tax-order.component.scss'],
})
export class DiscountTaxOrderComponent implements OnInit {
  @Input() productDiscountTaxOrder: ProductBill | null = null;
  @Input() orderSelected: IBillPayment;
  @Input() title!: string;
  @Input() invoiceConfiguration: InvoiceConfiguration;
  lastCompany: LastCompany;
  productNameCustomerInput: string = '';

  listVat = JSON.parse(JSON.stringify(LIST_VAT));
  invoiceType = InvoiceType;
  typeDiscount = TypeDiscount;
  @ViewChild('firstInput') firstInput: ElementRef;

  typeDiscountOrder = [...TYPE_DISCOUNT];
  productDiscountTaxOrderLocal: any;
  orderSelectedLocal: IBillPayment;
  statusVatRateInput = false;

  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private location: Location,
    protected utilsService: UtilsService,
    private toast: ToastrService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    if (this.productDiscountTaxOrder) {
      this.productDiscountTaxOrderLocal = JSON.parse(JSON.stringify(this.productDiscountTaxOrder));
      for (let i = 0; i < this.listVat.length; i++) {
        if (this.productDiscountTaxOrderLocal.vatRate) {
          if (this.listVat[i].value === this.productDiscountTaxOrderLocal.vatRate) {
            break;
          } else {
            if (i === this.listVat.length - 1) {
              this.listVat[i].value = this.productDiscountTaxOrderLocal.vatRate;
              this.statusVatRateInput = true;
            }
          }
        }
      }
    }
    if (this.orderSelected) {
      this.orderSelectedLocal = JSON.parse(JSON.stringify(this.orderSelected));
      for (let i = 0; i < this.listVat.length; i++) {
        if (this.orderSelectedLocal.vatRate) {
          if (this.listVat[i].value === this.orderSelectedLocal.vatRate) {
            break;
          } else {
            if (i === this.listVat.length - 1) {
              this.listVat[i].value = this.orderSelectedLocal.vatRate;
              this.statusVatRateInput = true;
            }
          }
        }
      }
    }
  }

  closeModal() {
    this.activeModal.dismiss();
  }

  changeTypeDiscountOrder() {
    if (this.productDiscountTaxOrderLocal.typeDiscount === TypeDiscount.VALUE) {
      this.productDiscountTaxOrderLocal.discountPercent = 0;
    } else {
      this.productDiscountTaxOrderLocal.totalPreTax = 0;
    }
  }

  changeVat(vatRate) {
    const findIndex = this.listVat.findIndex(item => item.value === vatRate && item.id === this.listVat.length);
    if (findIndex < 0) {
      this.productDiscountTaxOrderLocal.vatRate = vatRate;
      this.statusVatRateInput = false;
      this.productDiscountTaxOrderLocal.vatAmount = take_decimal_number(
        (this.productDiscountTaxOrderLocal.totalPreTax * getPositiveNumber(vatRate)) / 100 || 0,
        this.lastCompany.roundScaleAmount
      );
    } else {
      this.statusVatRateInput = true;
      this.productDiscountTaxOrderLocal.vatRate = 0;
    }
  }

  changeVatOrder(vatRate) {
    const findIndex = this.listVat.findIndex(item => item.value === vatRate && item.id === this.listVat.length);
    if (findIndex < 0) {
      this.orderSelectedLocal.vatRate = vatRate;
      this.statusVatRateInput = false;
      this.orderSelectedLocal.vatAmount = take_decimal_number(
        (this.orderSelectedLocal.totalPreTax * getPositiveNumber(vatRate)) / 100,
        this.lastCompany.roundScaleAmount
      );
      // (this.orderSelectedLocal.discountAmount * this.orderSelectedLocal.vatRate) / 100 || 0;
    } else {
      this.statusVatRateInput = true;
      this.orderSelectedLocal.vatRate = 0;
    }
  }

  changeVatRateOrderSelectedLocal(vatRate, event?: any) {
    this.listVat[this.listVat.length - 1].value = vatRate || -3;
    this.listVat = [...this.listVat];
    this.orderSelectedLocal.vatRate = vatRate || -3;
    this.orderSelectedLocal.vatAmount = take_decimal_number(
      (this.orderSelectedLocal.totalPreTax * getPositiveNumber(vatRate)) / 100 || 0,
      this.lastCompany.roundScaleAmount
    );
  }

  changeVatRateOrder(vatRate, event?: any) {
    if (!event) {
      this.listVat[this.listVat.length - 1].value = vatRate || -3;
      this.listVat = [...this.listVat];
      this.productDiscountTaxOrderLocal.vatRate = vatRate || -3;
      this.productDiscountTaxOrderLocal.vatAmount = take_decimal_number(
        (this.productDiscountTaxOrderLocal.totalPreTax * getPositiveNumber(vatRate)) / 100 || 0,
        this.lastCompany.roundScaleAmount
      );
    } else {
      if (vatRate && (vatRate > 100 || vatRate < 0)) {
        this.productDiscountTaxOrderLocal.vatRate = vatRate > 100 ? 100 : 0;
        event.target.value = this.productDiscountTaxOrderLocal.vatRate;
        this.listVat[this.listVat.length - 1].value = 100;
        this.listVat = [...this.listVat];
        this.productDiscountTaxOrderLocal.vatAmount = take_decimal_number(
          (this.productDiscountTaxOrderLocal.totalPreTax * getPositiveNumber(vatRate)) / 100 || 0,
          this.lastCompany.roundScaleAmount
        );
      }
    }
  }

  saveDiscountTaxAmount() {
    if (this.title === 'discountAmount') {
      if (this.productDiscountTaxOrderLocal.vatRate >= 0) {
        this.productDiscountTaxOrderLocal.vatRateName = this.productDiscountTaxOrderLocal.vatRate + '%';
      } else {
        this.productDiscountTaxOrderLocal.vatRateName = this.listVat.find(
          vat => vat.value == this.productDiscountTaxOrderLocal.vatRate
        ).name;
      }
      this.activeModal.close({
        ...this.productDiscountTaxOrderLocal,
        productName: this.productNameCustomerInput || this.productDiscountTaxOrderLocal.productName,
        amount: this.productDiscountTaxOrderLocal.totalPreTax,
        totalAmount: this.productDiscountTaxOrderLocal.totalPreTax + this.productDiscountTaxOrderLocal.vatAmount,
      });
    } else {
      if (this.title === 'vatRate') {
        this.activeModal.close(this.orderSelectedLocal);
      } else {
        this.activeModal.close(this.orderSelectedLocal.vatAmount);
      }
    }
  }

  changeDiscountPercent(discountPercent, event?: any) {
    if (!event) {
      this.productDiscountTaxOrderLocal.totalPreTax = take_decimal_number(
        (this.orderSelectedLocal.amount * discountPercent) / 100,
        this.lastCompany.roundScaleAmount
      );
      this.changeDiscountAmount(null, this.productDiscountTaxOrderLocal);
    } else {
      if (discountPercent && (discountPercent > 100 || discountPercent < 0)) {
        this.productDiscountTaxOrderLocal.discountPercent = discountPercent > 100 ? 100 : 0;
        event.target.value = this.productDiscountTaxOrderLocal.discountPercent;
        this.productDiscountTaxOrderLocal.totalPreTax = take_decimal_number(
          (this.orderSelectedLocal.amount * discountPercent) / 100,
          this.lastCompany.roundScaleAmount
        );
        this.changeDiscountAmount(null, this.productDiscountTaxOrderLocal);
      }
    }
  }

  changeDiscountAmount(event, productDiscountTaxOrderLocal) {
    if (parseFloat(event?.target.value.replace(/,/g, '')) > this.orderSelectedLocal.amount) {
      productDiscountTaxOrderLocal.totalPreTax = this.orderSelectedLocal.amount;
      event.target.value = this.orderSelectedLocal.amount;
    }
    productDiscountTaxOrderLocal.vatAmount = take_decimal_number(
      (productDiscountTaxOrderLocal.totalPreTax * getPositiveNumber(productDiscountTaxOrderLocal.vatRate)) / 100 || 0,
      this.lastCompany.roundScaleAmount
    );
  }

  checkDiscountTaxOrderPercent(event, vatDiscountPercent) {
    const status: any = checkInput(event);
    if (status == 100) {
      this.productDiscountTaxOrderLocal[vatDiscountPercent] = 100;
      if (vatDiscountPercent === 'vatRate') {
        this.listVat[this.listVat.length - 1].value = 100;
        // this.productDiscountTaxOrderLocal.vatAmount =
        // (this.productDiscountTaxOrderLocal.totalPreTax * this.productDiscountTaxOrderLocal.vatRate) / 100 || 0;
      }
      this.changeDiscountPercent(this.productDiscountTaxOrderLocal[vatDiscountPercent]);
      return false;
    } else {
      if (status) return true;
      return false;
    }
  }

  ngAfterViewInit() {
    if (this.firstInput) {
      this.firstInput.nativeElement.focus();
    }
  }

  convertStringToNumber(value) {
    return parseFloat(value.replace(/,/g, ''));
  }

  preventIncrement(e) {
    if (e.keyCode === 38 || e.keyCode === 40) {
      e.preventDefault();
    }
  }

  numberOnly(event: any): boolean {
    return numberOnly(event);
  }

    protected readonly ICON_CONFIRM = ICON_CONFIRM;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
