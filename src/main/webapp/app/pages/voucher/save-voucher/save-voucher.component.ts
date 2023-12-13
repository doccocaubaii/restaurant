import { Component, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { UtilsService } from '../../../utils/Utils.service';
import { VoucherService } from '../voucher.service';
import dayjs from 'dayjs/esm';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-save-voucher',
  templateUrl: './save-voucher.component.html',
  styleUrls: ['./save-voucher.component.scss'],
})
export class SaveVoucherComponent extends BaseComponent implements OnInit {
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  company: any;
  companyId: number;
  isLoading = false;
  typeDiscount = [
    {
      id: 0,
      discountValue: 'Theo phần trăm',
    },
    {
      id: 1,
      discountValue: 'Theo giá tiền',
    },
  ];
  voucher: any = {
    code: '',
    name: '',
    id: 0,
    startTime: {},
    endTime: {},
    status: 1,
    type: 300,
    conditions: [
      {
        discountType: 0,
        discountValue: null,
        discountPercent: null,
      },
    ],
  };
  constructor(
    private voucherService: VoucherService,
    public activeModal: NgbActiveModal,
    private toastr: ToastrService,
    protected utilsService: UtilsService
  ) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    if (!this.voucher.id) this.voucher.id = 0;
    const { conditions } = this.voucher;
    if (conditions[0].discountPercent > 0) {
      this.voucher.conditions[0].discountType = 0;
      this.voucher.conditions[0].discountValue = conditions[0].discountPercent;
    } else {
      this.voucher.conditions[0].discountType = 1;
    }
  }

  close() {
    this.isLoading = true;
    this.voucher.startTime = this.utilsService.convertDate(this.fromDate) ?? '';
    this.voucher.endTime = this.utilsService.convertDate(this.toDate) ?? '';
    // this.isLoading = true;
    this.voucher.comId = this.companyId;
    if (!this.voucher.id) {
      this.voucher.id = null;
      //gọi api
      this.voucherService.postVoucher(this.voucher).subscribe(
        response => {
          if (response.status) {
            this.toastr.success(response.message[0].message);
            this.activeModal.close(true);
          }
        },
        e => {
          this.isLoading = false;
          console.log(e);
        }
      );
    } else {
      //gọi api
      this.voucherService.putVoucher(this.voucher).subscribe(
        response => {
          if (response.status) {
            this.toastr.success(response.message[0].message);
            this.activeModal.close(true);
          }
        },
        e => {
          this.isLoading = false;
          console.log(e);
        }
      );
    }
  }
  clearByPercent() {
    if (this.voucher.conditions[0].discountType == 0 && this.voucher.conditions[0].discountValue > 100)
      this.voucher.conditions[0].discountValue = null;
  }
  dismiss() {
    this.activeModal.dismiss();
  }
  getAfterPresent(date: any) {
    let present = dayjs();
    if (present.isAfter(date)) return present;
    return date;
  }
  autoSelectStatus() {
    if (this.voucher.id) return;
    let present = dayjs();
    if (present.isAfter(this.fromDate)) {
      this.voucher.status = 1;
      return;
    }
    this.voucher.status = 0;
  }

    protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
}
