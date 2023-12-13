import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-confirm-change',
  templateUrl: './confirm-change.component.html',
  styleUrls: ['./confirm-change.component.scss'],
})
export class ConfirmChangeComponent implements OnInit {
  title = '';
  title2 = '';
  checkDeleteMulti = false;
  type = '';
  quantity: any = 0;
  quantityInput: any = null;
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }
  ngOnInit(): void {}

  onEnterQuantity() {
    if (this.checkDeleteMulti && this.quantityInput === this.quantity) {
      this.dismiss(1);
    }
  }
  onDeteleConvertUnit() {
    this.dismiss(1);
  }
  dismiss(check: any) {
    this.activeModal.close(check);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
