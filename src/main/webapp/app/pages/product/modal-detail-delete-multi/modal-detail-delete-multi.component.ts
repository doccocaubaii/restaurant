import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-detail-delete-multi',
  templateUrl: './modal-detail-delete-multi.component.html',
  styleUrls: ['./modal-detail-delete-multi.component.scss'],
})
export class ModalDetailDeleteMultiComponent implements OnInit {
  countAll = 0;
  countSuccess = 0;
  countFalse = 0;
  data: any = [];
  isCategory = false;
  constructor(public activeModal: NgbActiveModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}

  dismiss() {
    this.activeModal.close();
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
}
