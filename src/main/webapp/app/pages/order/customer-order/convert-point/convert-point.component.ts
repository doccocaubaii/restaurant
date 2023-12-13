import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from 'app/utils/Utils.service';
import { Location } from '@angular/common';
import { IBillPayment, SavePointInput } from '../../model/bill-payment.model';
import { TypeConvertPoint } from 'app/pages/const/customer-order.const';
import { CustomerService } from '../../service/customer.service';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import {ICON_CANCEL, ICON_CONFIRM} from "../../../../shared/other/icon";

@Component({
  selector: 'jhi-convert-point',
  templateUrl: './convert-point.component.html',
  styleUrls: ['./convert-point.component.scss'],
})
export class ConvertPointComponent implements OnInit {
  orderSelected: IBillPayment;
  savePointInput: SavePointInput;
  lastCompany;

  constructor(
    public activeModal: NgbActiveModal,
    public customerService: CustomerService,
    protected modalService: NgbModal,
    private location: Location,
    protected utilsService: UtilsService,
    private toast: ToastrService
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit() {
    this.savePointInput = {
      comId: this.orderSelected.comId,
      customerIds: [this.orderSelected.customerId],
      type: TypeConvertPoint.converPoint,
      amount: 0,
      point: 0,
    };
  }

  convertPointCustomer() {
    this.savePointInput.amount = this.savePointInput.point * this.orderSelected.cardCustomerInfo.redeemValue;
  }

  savePointCustomer() {
    this.customerService.savePoint(this.savePointInput).subscribe(res => {
      this.activeModal.close();
    });
  }

  closeModal() {
    this.activeModal.dismiss();
  }

    protected readonly ICON_CONFIRM = ICON_CONFIRM;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
