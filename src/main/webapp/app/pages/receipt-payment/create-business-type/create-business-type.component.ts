import { Component, OnDestroy, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
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
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-create-business-type',
  templateUrl: './create-business-type.component.html',
  styleUrls: ['./create-business-type.component.scss'],
})
export class CreateBusinessTypeComponent extends BaseComponent implements OnInit, OnDestroy {
  id: number;
  name: string;
  type: number;
  lastCompany: any;
  disableButton = false;

  ngOnDestroy(): void {}

  constructor(
    public activeModal: NgbActiveModal,
    private customerService: CustomerService,
    private loading: LoadingOption,
    private toast: ToastrService,
    protected utilsService: UtilsService,
    private receiptPaymentService: ReceiptPaymentService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
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

  createBusinessType() {
    if (!this.name || !this.name.trim()) {
      this.toast.error('Tên phân loại nghiệp vụ không được bỏ trống');
    } else {
      const req = {
        comId: this.lastCompany.id,
        type: this.type,
        name: this.name,
      };
      this.receiptPaymentService.createBusinessType(req).subscribe(res => {
        if (res.status) {
          this.toast.success(res.message[0].message);
          this.activeModal.close('Saved');
        }
      });
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
