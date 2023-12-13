import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-signatures-connect-popup',
  templateUrl: './signatures-popup.component.html',
})
export class SignaturesPopupComponent {
  PATH = 'content/images/signatures/guide.pdf';

  constructor(public activeModal: NgbActiveModal) {}

  close() {
    this.activeModal.dismiss('cancel');
  }
}
