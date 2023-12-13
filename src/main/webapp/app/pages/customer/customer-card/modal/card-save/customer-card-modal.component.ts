import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from '../../../../../shared/base/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CustomerService } from '../../../customer.service';
import { ToastrService } from 'ngx-toastr';
import { ConfirmDialogComponent } from '../../../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal, IDialogModal } from '../../../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../../../constants/modal.const';
import { faSave } from '@fortawesome/free-solid-svg-icons';
import {ICON_CANCEL, ICON_UPDATE_CONFIG} from "../../../../../shared/other/icon";

@Component({
  selector: '',
  templateUrl: './customer-card-modal.component.html',
  styleUrls: ['./customer-card-modal.component.scss'],
})
export class CustomerCardModalComponent extends BaseComponent implements OnInit {
  @Input() request: any;
  title: string;
  label: string;
  labelExchange: string;
  inputHolder: string;
  inputHolderExchange: string;
  inputValue: number;
  inputExchange: any;
  description: any;
  company: any;
  companyId: number;
  private modalRef: NgbModalRef | undefined;

  constructor(
    private activeModal: NgbActiveModal,
    private customerService: CustomerService,
    private toast: ToastrService,
    private modalService: NgbModal
  ) {
    super();
  }

  async ngOnInit() {
    this.innitData();
    this.company = await this.getCompany();
    this.companyId = this.company.id;
  }

  innitData() {
    if (this.request.type === 0) {
      this.title = 'Nạp tiền';
      this.label = 'Mức tiền nạp';
      this.inputHolder = 'Số tiền nạp';
    } else if (this.request.type === 1) {
      this.title = 'Điểm cộng';
      this.label = 'Điểm cộng';
      this.inputHolder = 'Số điểm cộng';
    } else if (this.request.type === 2) {
      this.title = 'Điểm trừ';
      this.label = 'Điểm trừ';
      this.inputHolder = 'Số điểm trừ';
    } else if (this.request.type === 3) {
      this.title = 'Quy đổi điểm';
      this.label = 'Số điểm';
      this.inputHolder = 'Số điểm';
      this.labelExchange = 'Mức tiền';
      this.inputHolderExchange = 'Mức tiền';
    }
  }

  dismiss($event: MouseEvent) {
    this.modalService.dismissAll();
  }

  confirm: DialogModal;
  @ViewChild('confirmSave') confirmSave: ElementRef | undefined;
  preSave() {
    const { type, redeemValue, cardName } = this.request;
    if (type === 3 && !redeemValue) {
      this.confirm = {
        title: 'Lưu ý chính sách quy đổi',
        btnText: 'Đồng ý',
        message: 'Thẻ ' + cardName + ' không tồn tại chính sách. Bạn có chắc chắn muốn quy đổi điểm không?',
      };
      this.modalRef = this.modalService.open(this.confirmSave, { size: 'lg' });
    } else {
      this.onSave();
    }
  }

  onSave() {
    const { type, accumValue } = this.request;
    let amount, point;
    if (type === 0) {
      amount = this.inputValue;
    } else if (type === 1 || type === 2) {
      point = this.inputValue;
    } else if (type === 3) {
      if (!this.inputExchange || this.inputExchange === 0) {
        this.toast.error('Mức tiền quy đổi phải lớn hơn 0');
        return;
      }
      if (this.inputValue > accumValue) {
        this.toast.error('Giá trị quy đổi điểm không vượt quá số điểm tích luỹ');
        return;
      }
      amount = this.inputExchange;
      point = this.inputValue;
    }
    const request = {
      comId: this.companyId,
      customerIds: this.request.customerIds,
      type: type,
      amount: amount,
      point: point,
      description: this.description,
    };
    this.customerService.customerCardSavePoint(request).subscribe(response => {
      if (response.status) {
        this.toast.success(response.message[0].message);
        this.modalService.dismissAll();
      }
    });
  }

  exchangeValue() {
    const { type, redeemValue } = this.request;
    if (type === 3 && redeemValue) {
      if (!this.inputValue) {
        this.inputExchange = null;
      } else {
        this.inputExchange = this.inputValue * redeemValue;
      }
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_UPDATE_CONFIG = ICON_UPDATE_CONFIG;
}
