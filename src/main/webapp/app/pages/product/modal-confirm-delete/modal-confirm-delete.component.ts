import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-confirm-delete-convert-unit',
  templateUrl: './modal-confirm-delete.component.html',
  styleUrls: ['./modal-confirm-delete.component.scss'],
})
export class ModalConfirmDeleteComponent implements OnInit {
  title = '';
  title2 = '';
  checkDeleteMulti = false;
  type = '';
  quantity: any = 0;
  quantityInput: any = null;
  isBlueButton: any = false;
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
  onConfirm() {
    this.dismiss(1);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
