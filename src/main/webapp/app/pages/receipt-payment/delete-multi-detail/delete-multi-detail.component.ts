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
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './delete-multi-detail.component.html',
  styleUrls: ['./delete-multi-detail.component.scss'],
})
export class DeleteMultiDetailComponent extends BaseComponent implements OnInit, OnDestroy {
  countAll = 0;
  countSuccess = 0;
  countFalse = 0;
  data: any = [];
  constructor(public activeModal: NgbActiveModal) {
    super();
  }

  ngOnInit(): void {}

  dismiss() {
    this.activeModal.close('Deleted');
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
}
