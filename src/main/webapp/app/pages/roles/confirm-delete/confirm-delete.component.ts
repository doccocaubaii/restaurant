import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { IDialogModal } from '../../order/model/dialogModal.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EventManager, EventWithContent } from '../../../core/util/event-manager.service';

@Component({
  selector: 'jhi-confirm-delete',
  templateUrl: './confirm-delete.component.html',
  styleUrls: ['./confirm-delete.component.scss'],
})
export class ConfirmDeleteComponent implements OnInit {
  @Input() value: IDialogModal = {};
  isLoading = false;
  @Output() formSubmit: EventEmitter<any> = new EventEmitter<any>();
  constructor(private activeModal: NgbActiveModal, private eventManager: EventManager) {}
  name: any;

  ngOnInit() {}

  public decline() {
    this.formSubmit.emit(false);
    this.activeModal.close();
    this.formSubmit.emit(false);
  }

  public accept(event: any) {
    this.isLoading = true;
    this.formSubmit.emit(true);
    // this.sendKitchenBroadcast();
  }

  sendKitchenBroadcast() {
    const content = { status: 200, message: 'Kitchen Notification' };
    this.eventManager.broadcast(new EventWithContent('easyPosFrontEndApp.httpError', content));
    this.eventManager.broadcast({ name: 'eposApp.kitchenNotification', content: content });
    this.eventManager.broadcast2('EVENT');
  }

  public dismiss() {
    this.formSubmit.emit(123);
    // this.activeModal.dismiss();
  }
}
