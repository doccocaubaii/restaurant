import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'modal-dialog',
  templateUrl: './modal-dialog.component.html',
})
export class ModalDialogComponent implements OnInit {
  constructor(public activeModal: NgbActiveModal) {}

  ngOnInit() {}

  closeModal() {
    this.activeModal.dismiss();
  }
}
