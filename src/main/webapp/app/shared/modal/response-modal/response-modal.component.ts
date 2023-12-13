import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDialogModal } from '../../../pages/order/model/dialogModal.model';
import { LoadingOption } from 'app/utils/loadingOption';
import {ICON_CANCEL} from "../../other/icon";

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './response-modal.component.html',
  styleUrls: ['./response-modal.component.scss'],
})
export class ResponseModalComponent implements OnInit {
  @Input() data: any = [];
  @Output() formSubmit: EventEmitter<boolean> = new EventEmitter<boolean>();
  constructor(private activeModal: NgbActiveModal, public loading: LoadingOption) {}

  ngOnInit() {}

  public decline() {
    this.activeModal.close();
  }

  public accept(event: any) {
    this.formSubmit.emit(true);
  }

  public dismiss() {
    this.activeModal.dismiss();
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
}
