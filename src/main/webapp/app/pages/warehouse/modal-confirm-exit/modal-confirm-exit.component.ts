import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-confirm-exit',
  templateUrl: './modal-confirm-exit.component.html',
  styleUrls: ['./modal-confirm-exit.component.scss'],
})
export class ModalConfirmExitComponent implements OnInit {
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}
  onSave() {
    this.dismiss(1);
  }
  dismiss(check: any) {
    this.activeModal.close(check);
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
