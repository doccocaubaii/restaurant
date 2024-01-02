import { Injectable } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ConfirmDialogComponent } from '../modal/confirm-dialog/confirm-dialog.component';

@Injectable({
  providedIn: 'root',
})
export class ConfirmDialogService {
  constructor(private modalService: NgbModal) {}

  public async confirm(
    message: string,
    title?: string,
    btnText?: string,
    icon?: string,
    classBtn?: string,
    dialogSize: 'sm' | 'md' | 'lg' = 'lg'
  ): Promise<Boolean> {
    const modalRef = this.modalService.open(ConfirmDialogComponent, { size: dialogSize });
    title && (modalRef.componentInstance.title = title);
    modalRef.componentInstance.message = message;
    modalRef.componentInstance.btnText = btnText;
    modalRef.componentInstance.icon = icon;
    modalRef.componentInstance.classBtn = classBtn;
    let isAccept = false;
    await modalRef.componentInstance.formSubmit.subscribe(res => {
      return true;
    });
    return isAccept ? modalRef.componentInstance.formSubmit : modalRef.result;
  }
}
