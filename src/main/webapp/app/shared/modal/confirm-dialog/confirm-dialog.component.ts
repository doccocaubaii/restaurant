import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { IDialogModal } from '../../../pages/order/model/dialogModal.model';
import { LoadingOption } from 'app/utils/loadingOption';
import { EventManager, EventWithContent } from '../../../core/util/event-manager.service';
import {ICON_CANCEL, ICON_CONFIRM} from "../../other/icon";

@Component({
  selector: 'app-confirm-dialog',
  templateUrl: './confirm-dialog.component.html',
  styleUrls: ['./confirm-dialog.component.scss'],
})
export class ConfirmDialogComponent implements OnInit {
  @Input() value: IDialogModal = {};
  @Output() formSubmit: EventEmitter<any> = new EventEmitter<any>();
  constructor(private activeModal: NgbActiveModal, public loading: LoadingOption, private eventManager: EventManager) {}

  ngOnInit() {}

  public decline() {
    this.formSubmit.emit(false);
    this.activeModal.close();
    this.formSubmit.emit(false);
  }

  public accept(event: any) {
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

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CONFIRM = ICON_CONFIRM;
}
