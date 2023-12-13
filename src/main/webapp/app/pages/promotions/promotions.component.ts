import { Component, OnInit } from '@angular/core';
import { VoucherService } from '../voucher/voucher.service';
import { VoucherModel } from '../../entities/voucher/Voucher.model';
import { UtilsService } from '../../utils/Utils.service';
import { BaseComponent } from '../../shared/base/base.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import dayjs from 'dayjs/esm';
import { VoucherConstants } from '../../constants/voucher.constants';
import { SaveVoucherComponent } from '../voucher/save-voucher/save-voucher.component';
import { PromotionSaveComponent } from './save/promotion-save.component';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { ApplyVoucherComponent } from '../voucher/apply-voucher/apply-voucher.component';

@Component({
  selector: 'jhi-promotions',
  templateUrl: './promotions.component.html',
  styleUrls: ['./promotions.component.scss'],
})
export class PromotionsComponent extends BaseComponent implements OnInit {
  startTime: dayjs.Dayjs | any;
  endTime: dayjs.Dayjs | any;
  company: any;
  companyId: number;
  keyword: any;
  totalSize: number = 0;
  page: number = 1;
  pageSize: number = 20;
  sizes = [
    {
      id: 10,
      name: '10',
    },
    {
      id: 20,
      name: '20',
    },
    {
      id: 30,
      name: '30',
    },
  ];
  promotions: VoucherModel[] = [];
  idEditing: number = -1;

  constructor(
    private modalService: NgbModal,
    protected utilsService: UtilsService,
    private voucherService: VoucherService,
    private toastr: ToastrService
  ) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.getAll();
  }

  onSearch() {}

  async getAll() {
    // this.modalRef = this.modalService.open(PromotionSaveComponent, { size: 'xl', backdrop: 'static' });
    let tmpStartTime = this.utilsService.convertDate(this.startTime) ?? '';
    let tmpEndTime = this.utilsService.convertDate(this.endTime) ?? '';
    await this.voucherService
      .getVouchers(
        this.companyId,
        this.page,
        this.pageSize,
        this.keyword,
        tmpStartTime,
        tmpEndTime,
        VoucherConstants.TYPE_REQUEST_PROMOTIONS
      )
      .subscribe(respond => {
        this.promotions = respond.data;
        this.totalSize = respond.count;
      });
  }

  private modalRef: NgbModalRef | undefined;
  onCreate(promotion: any) {
    if (promotion) {
      promotion.conditions.forEach(item => {
        if (item.discountValue > 0) {
          item.discountType = 1;
        } else {
          item.discountType = 0;
        }
      });
      promotion.comId = this.companyId;
    }
    this.modalRef = this.modalService.open(PromotionSaveComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.promotionInput = promotion;
    this.modalRef.result.then(
      result => {
        this.getAll();
      },
      reason => {}
    );
  }

  openDelete(id: number) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_PROMOTIONS,
      ModalContent.DELETE_PROMOTIONS,
      ModalBtn.AGREE,
      'trash-can',
      'btn-delete'
    );
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.deleteVoucher(id);
        if (this.modalRef) this.modalRef.close();
      }
    });
    this.modalRef.result.then(
      result => {},
      reason => {}
    );
  }

  deleteVoucher(id: number) {
    this.idEditing = -1;
    //gọi api
    this.voucherService.delVoucher(id).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getAll();
        this.modalRef?.close();
      }
    });
  }

  openApply(voucher: any) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(ApplyVoucherComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.voucher.id = voucher.id;
    this.modalRef.componentInstance.voucher.isDefaul = !voucher.applyType;

    this.modalRef.result.then(result => {
      this.getAll();
    });
  }
}
