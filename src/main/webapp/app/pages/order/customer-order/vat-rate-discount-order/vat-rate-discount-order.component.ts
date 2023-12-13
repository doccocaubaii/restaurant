import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { Location } from '@angular/common';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { IBillPayment, ProductBill } from '../../model/bill-payment.model';
import { InvoiceType, LIST_VAT, LIST_VAT_RATE_DISCOUNT_PRODUCT, TYPE_DISCOUNT, TypeDiscount } from 'app/pages/const/customer-order.const';
import {
  checkInput,
  convertNumberToString,
  getPositiveNumber,
  getProductNameSPGC,
  numberOnly,
  take_decimal_number,
} from 'app/pages/const/function';
import { UtilsService } from 'app/utils/Utils.service';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../../shared/other/icon";

@Component({
  selector: 'jhi-vat-rate-discount-order',
  templateUrl: './vat-rate-discount-order.component.html',
  styleUrls: ['./vat-rate-discount-order.component.scss'],
})
export class VatRateDiscountOrderComponent implements OnInit {
  @Input() orderSelected: any;
  @Input() invoiceConfiguration;
  lastCompany;
  title;
  listVat = JSON.parse(JSON.stringify(LIST_VAT_RATE_DISCOUNT_PRODUCT));
  orderSelectedLocal: any;

  statusVatRateInput = false;

  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private location: Location,
    protected utilsService: UtilsService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    if (this.orderSelected) {
      this.orderSelectedLocal = JSON.parse(JSON.stringify(this.orderSelected));
      for (let i = 0; i < this.listVat.length; i++) {
        if (this.orderSelectedLocal.discountVatRate) {
          if (this.listVat[i].value === this.orderSelectedLocal.discountVatRate) {
            break;
          } else {
            if (i === this.listVat.length - 1) {
              this.listVat[i].value = this.orderSelectedLocal.discountVatRate;
              this.statusVatRateInput = true;
            }
          }
        }
      }
      if (this.orderSelectedLocal.discountVatAmount) {
        this.orderSelectedLocal.checkboxVatRateDiscountProduct = true;
      }
    }
  }

  closeModal() {
    this.activeModal.dismiss();
  }

  changeVatRateDiscountProductOrderLocal(event) {
    if (event.target.checked) {
      this.orderSelectedLocal.discountVatRate = 1;
      this.orderSelectedLocal.discountVatAmount = take_decimal_number(
        ((this.orderSelectedLocal.totalPreTax * getPositiveNumber(this.orderSelectedLocal.discountVatRate)) / 100) * 0.2,
        this.lastCompany.roundScaleAmount
      );
    } else {
      this.orderSelectedLocal.discountVatRate = 0;
      this.orderSelectedLocal.discountVatAmount = 0;
    }
  }

  changeVatOrder(vatRate) {
    const findIndex = this.listVat.findIndex(item => item.value === vatRate && item.id === this.listVat.length);
    if (findIndex < 0) {
      this.orderSelectedLocal.discountVatRate = vatRate;
      this.statusVatRateInput = false;
      this.orderSelectedLocal.discountVatAmount = take_decimal_number(
        ((this.orderSelectedLocal.totalPreTax * getPositiveNumber(vatRate)) / 100) * 0.2,
        this.lastCompany.roundScaleAmount
      );
      // (this.orderSelectedLocal.discountAmount * this.orderSelectedLocal.vatRate) / 100 || 0;
    } else {
      this.statusVatRateInput = true;
      this.orderSelectedLocal.discountVatRate = 0;
      this.orderSelectedLocal.discountVatAmount = undefined;
    }
  }

  changeVatRateOrderSelectedLocal(vatRate, event?: any) {
    this.listVat[this.listVat.length - 1].value = vatRate || -3;
    this.listVat = [...this.listVat];
    this.orderSelectedLocal.vatRate = vatRate || -3;
    this.orderSelectedLocal.discountVatAmount = take_decimal_number(
      ((this.orderSelectedLocal.totalPreTax * getPositiveNumber(vatRate)) / 100) * 0.2 || 0,
      this.lastCompany.roundScaleAmount
    );
  }

  saveVatRateDiscountProductOrder() {
    this.activeModal.close({
      ...this.orderSelectedLocal,
      vatRateDiscountProductName: getProductNameSPGC(this.orderSelectedLocal.discountVatAmount),
    });
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
