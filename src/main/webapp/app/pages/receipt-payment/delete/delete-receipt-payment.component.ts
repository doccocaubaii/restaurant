import { Component, OnDestroy, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { debounceTime, distinctUntilChanged, Subject, switchMap, tap } from 'rxjs';
import { Page } from '../../const/customer-order.const';
import { FilterCustomer } from '../../order/model/filterCustomer.model';
import { CustomerService } from '../../order/service/customer.service';
import dayjs, { Dayjs } from 'dayjs/esm';
import { LoadingOption } from '../../../utils/loadingOption';
import { ReceiptPaymentService } from '../receipt-payment.service';
import { FilterBusiness, UpdateTransactionReq } from '../receipt-payment';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { UtilsService } from 'app/utils/Utils.service';
import { DeleteMultiDetailComponent } from '../delete-multi-detail/delete-multi-detail.component';
import { CreateBusinessTypeComponent } from '../create-business-type/create-business-type.component';
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './delete-receipt-payment.component.html',
  styleUrls: ['./delete-receipt-payment.component.scss'],
})
export class DeleteReceiptPaymentComponent extends BaseComponent implements OnInit, OnDestroy {
  id: number;
  no: string;
  fromDate: any;
  toDate: any;
  keyword: any;
  noEnter: number;
  numberDelete: number;
  listSelected: any = [];
  paramCheckAll: boolean;
  deleteMutiple: boolean = false;
  totalItems: number;
  type: number;
  lastCompany: any;
  date: Dayjs;
  now = dayjs();
  disableButton = false;
  private modalRef: NgbModalRef | undefined;

  ngOnDestroy(): void {}

  constructor(
    public activeModal: NgbActiveModal,
    private customerService: CustomerService,
    private loading: LoadingOption,
    private toast: ToastrService,
    protected utilsService: UtilsService,
    protected modalService: NgbModal,
    private receiptPaymentService: ReceiptPaymentService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    if (this.paramCheckAll) {
      if (this.totalItems - this.listSelected.length > 10) {
        this.deleteMutiple = true;
        this.numberDelete = this.totalItems - this.listSelected.length;
      }
    } else {
      if (this.listSelected.length > 10) {
        this.deleteMutiple = true;
        this.numberDelete = this.listSelected.length;
      }
    }
  }

  closeModal() {
    this.activeModal.close();
  }

  dismiss(value: any) {
    this.activeModal.close(value);
  }
  enableButton() {
    this.disableButton = false;
    this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
  }

  deleteTransaction() {
    if (this.numberDelete > 10 && !this.noEnter) {
      this.toast.error('Số lượng bản ghi không được để trống');
      return;
    }
    if (this.numberDelete > 10 && this.noEnter != this.numberDelete) {
      this.toast.error('Số lượng bản ghi không đúng');
      return;
    }

    if (this.id) {
      const req = {
        comId: this.lastCompany.id,
        id: this.id,
        type: this.type,
        no: this.no,
      };
      this.receiptPaymentService.deleteReceiptPayment(req).subscribe(res => {
        if (res.status) {
          this.toast.success(res.message[0].message);
          this.activeModal.close('Deleted');
        }
      });
    }

    if (!this.id) {
      const req = {
        comId: this.lastCompany.id,
        fromDate: this.fromDate,
        toDate: this.toDate,
        type: this.type,
        keyword: this.keyword,
        paramCheckAll: this.paramCheckAll,
        ids: this.listSelected,
      };
      this.receiptPaymentService.deleteListReceiptPayment(req).subscribe(res => {
        if (res.status) {
          this.toast.success(res.message[0].message);
          this.activeModal.close('Deleted');
          this.modalRef = this.modalService.open(DeleteMultiDetailComponent, { size: '', backdrop: 'static' });
          this.modalRef.componentInstance.countAll = res.data.countAll;
          this.modalRef.componentInstance.countSuccess = res.data.countSuccess;
          this.modalRef.componentInstance.countFalse = res.data.countFalse;
          this.modalRef.componentInstance.data = res.data.dataFalse;
          this.modalRef.closed.subscribe((res?: any) => {
            if (res == 'Saved') {
            }
          });
        }
      });
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
}
