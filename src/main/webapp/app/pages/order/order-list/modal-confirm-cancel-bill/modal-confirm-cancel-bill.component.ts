import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-confirm-cancel-bill',
  templateUrl: './modal-confirm-cancel-bill.component.html',
  styleUrls: ['./modal-confirm-cancel-bill.component.scss'],
})
export class ModalConfirmCancelBillComponent implements OnInit {
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}
  onConfirm() {
    this.close(1);
  }
  close(check: any) {
    this.activeModal.close(check);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
