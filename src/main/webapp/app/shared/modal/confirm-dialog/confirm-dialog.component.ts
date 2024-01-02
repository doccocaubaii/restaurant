import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDialogModal } from '../../../pages/pos/model/dialogModal.model';
import { LoadingOption } from '../../../utils/loadingOption';

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  @Input() value: IDialogModal = {};
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
}
