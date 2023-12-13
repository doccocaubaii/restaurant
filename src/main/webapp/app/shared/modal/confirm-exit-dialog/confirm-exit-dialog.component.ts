import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDialogModal } from '../../../pages/order/model/dialogModal.model';
import { LoadingOption } from 'app/utils/loadingOption';
import { StatusExit } from 'app/pages/const/customer-order.const';
import {ICON_CANCEL, ICON_CONFIRM} from "../../other/icon";

@Component({
  selector: 'app-confirm-exit-dialog',
  templateUrl: './confirm-exit-dialog.component.html',
  styleUrls: ['./confirm-exit-dialog.component.scss'],
})
export class ConfirmExitDialogComponent implements OnInit {
  @Input() value: IDialogModal = {};
  @Output() formSubmit: EventEmitter<boolean> = new EventEmitter<boolean>();

  statusExit = StatusExit;
  constructor(private activeModal: NgbActiveModal, public loading: LoadingOption) {}

  ngOnInit() {}

  public decline() {
    this.activeModal.close();
  }

  public accept(statusExit: any) {
    this.formSubmit.emit(statusExit);
  }

  public dismiss() {
    this.activeModal.dismiss();
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
