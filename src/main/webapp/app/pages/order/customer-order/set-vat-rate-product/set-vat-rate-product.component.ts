import { Location } from '@angular/common';
import { Component, Input, OnInit, ViewChild, ElementRef } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { checkInput, numberOnly } from 'app/pages/const/function';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../../shared/other/icon";
@Component({
  selector: 'jhi-set-vat-rate-product',
  templateUrl: './set-vat-rate-product.component.html',
  styleUrls: ['./set-vat-rate-product.component.scss'],
})
export class SetVatRateProductComponent implements OnInit {
  @Input() productName!: string;
  @Input() vatRateInput!: number;
  vatRate!: number;
  @ViewChild('focus') focus: ElementRef;
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    if (this.vatRateInput && this.vatRateInput > 0) {
      this.vatRate = this.vatRateInput;
    }
  }

  ngAfterViewInit() {
    this.focus.nativeElement.focus();
  }

  closeModal() {
    this.activeModal.close();
  }

  saveVatRateProduct() {
    this.activeModal.close(this.vatRate);
  }

  checkVatRate(event: any) {
    const status: any = checkInput(event);
    if (status == 100) {
      this.vatRate = 100;
      return false;
    } else {
      if (status) return true;
      return false;
    }
  }

  preventIncrement(e) {
    if (e.keyCode === 38 || e.keyCode === 40) {
      e.preventDefault();
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
