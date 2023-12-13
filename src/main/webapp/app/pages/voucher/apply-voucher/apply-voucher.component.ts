import { Component, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingOption } from '../../../utils/loadingOption';
import { ToastrService } from 'ngx-toastr';
import { VoucherService } from '../voucher.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { UtilsService } from '../../../utils/Utils.service';
import { CardService } from '../../card/card.service';
import { VoucherConstants } from 'app/constants/voucher.constants';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-apply-voucher',
  templateUrl: './apply-voucher.component.html',
  styleUrls: ['./apply-voucher.component.scss'],
})
export class ApplyVoucherComponent extends BaseComponent implements OnInit {
  voucher: any = {
    applyType: 3,
  };
  lstCard: any;
  isLoading = false;
  checkAll: boolean = false;
  unCheckAll: boolean = false;
  company: any;
  companyId: number;
  totalSize: number = 0;
  page: number = 1;
  pageSize: number = 20;
  voucherArray: any;
  cusArray: any = [];
  paramCheckAllPage: any;
  selectedItem: any = {};
  keyword: string;
  displayPanelAll: any = false;
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
  map: any = [];
  private modalRef: NgbModalRef | undefined;
  dem: any = 0;
  constructor(
    private modalService: NgbModal,
    private cardService: CardService,
    protected utilsService: UtilsService,
    public activeModal: NgbActiveModal,
    public loading: LoadingOption,
    private toastr: ToastrService,
    private voucherService: VoucherService
  ) {
    super();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.map = Array.from({ length: 3 }, () => new Map());
    this.voucher.applyType = 1;
    this.getApplyVoucher(this.voucher.applyType);
  }
  getApplyVoucher(type: number) {
    this.voucherService
      .getApplyVoucher(this.companyId, this.voucher.id, type, this.voucher.isDefaul, this.page, this.pageSize, this.keyword)
      .subscribe(res => {
        this.dem = 100;
        this.totalSize = res.count;
        if (type == VoucherConstants.THE) {
          this.lstCard = res.data;
          this.lstCard.forEach(card => {
            // gắn giá trị cho check
            card.check = !!card.id;
            if (this.checkAll) card.check = true;
            if (this.unCheckAll) card.check = false;
            // nếu nằm trong map thì gắn lại giá trị
            if (this.map[VoucherConstants.THE].has(card.cardId)) card.check = this.map[VoucherConstants.THE].get(card.cardId).newValue;
          });
        } else if (type == VoucherConstants.KHACH_HANG) {
          this.cusArray = res.data;
          this.cusArray.forEach(cus => {
            cus.check = !!cus.id;
            if (this.checkAll) cus.check = true;
            if (this.unCheckAll) cus.check = false;
            if (this.map[VoucherConstants.KHACH_HANG].has(cus.customerId))
              cus.check = this.map[VoucherConstants.KHACH_HANG].get(cus.customerId).newValue;
          });
        }
      });
  }
  dismiss() {
    this.activeModal.dismiss();
  }
  loadMore() {
    if (this.paramCheckAllPage) {
      this.paramCheckAllPage = false;
    }
    this.getApplyVoucher(this.voucher.applyType);
  }
  getMap(applyType: number) {
    return this.map[applyType == VoucherConstants.KHACH_HANG ? VoucherConstants.KHACH_HANG : VoucherConstants.THE];
  }
  apply(applyType: number, close?: boolean) {
    let applyItem: any = [];
    for (let [key, value] of this.getMap(applyType)) {
      let tmp = applyType == VoucherConstants.KHACH_HANG ? value.customerId : value.cardId;
      if (this.checkAll) {
        if (!value.check) applyItem.push({ id: value.id, applyId: tmp });
        continue;
      }
      if (this.unCheckAll) {
        if (value.check) applyItem.push({ id: value.id, applyId: tmp });
        continue;
      }
      if ((value.id && !value.newValue) || (!value.id && value.newValue)) {
        applyItem.push({ id: value.id, applyId: tmp });
      }
    }
    let checkAllType: any = null;
    if (this.checkAll) checkAllType = 1;
    if (this.unCheckAll) checkAllType = 2;
    this.voucherService.applyVoucher(this.companyId, this.voucher.id, this.voucher.applyType, applyItem, checkAllType).subscribe(
      response => {
        if (response.status) {
          this.toastr.success(response.message[0].message);
          if (close) this.activeModal.close();
        }
      },
      e => {
        this.isLoading = false;
      }
    );
  }
  close() {
    this.isLoading = true;
    this.apply(this.voucher.applyType, true);
  }
  checkAllPageForMap(value: any) {
    let list = this.voucher.applyType === VoucherConstants.KHACH_HANG ? this.cusArray : this.lstCard;
    let myMap = this.getMap(this.voucher.applyType);

    list.forEach(item => {
      let uniqueId = this.voucher.applyType === VoucherConstants.KHACH_HANG ? item.customerId : item.cardId;
      let obj: any = {
        id: item.id,
        newValue: value,
      };
      if (this.voucher.applyType === VoucherConstants.THE) {
        obj.cardId = item.cardId;
      } else {
        obj.customerId = item.customerId;
      }
      myMap.set(uniqueId, obj);
      item.check = value;
    });
  }

  checkBoxForMap(item: any) {
    let ListItem = this.map[this.voucher.applyType];
    {
      let isCard = this.voucher.applyType === VoucherConstants.THE;
      let item3 = ListItem[isCard ? item.cardId : item.customerId];
      if (!item3) {
        item3 = item;
      }
      item3.newValue = !item.check;
      this.map[this.voucher.applyType].set(isCard ? item.cardId : item.customerId, item3);
    }
  }

  checkOrUncheckAll(value: boolean) {
    let myMap = this.getMap(this.voucher.applyType);
    myMap.clear();
    if (value) {
      this.checkAll = true;
      this.unCheckAll = false;
    } else {
      this.checkAll = false;
      this.unCheckAll = true;
    }
    let arr = this.voucher.applyType == VoucherConstants.THE ? this.lstCard : this.cusArray;
    for (let i of arr) {
      i.check = value;
    }
  }

  handleApplyTypeChange(event: Event, value: any) {
    if (this.map[3 - value].size == 0 && !this.checkAll && !this.unCheckAll) {
      this.totalSize = 0;
      this.paramCheckAllPage = false;
      this.getApplyVoucher(this.voucher.applyType);
      return;
    }
    this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.CHANGE_TYPE_VOUCHER,
      ModalContent.CHANGE_TYPE_VOUCHER,
      ModalBtn.CHANGE_TYPE_VOUCHER,
      'check',
      'btn-save'
    );
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.totalSize = 0;
        this.paramCheckAllPage = false;
        this.checkAll = false;
        this.unCheckAll = false;
        this.getApplyVoucher(this.voucher.applyType);
        this.map[3 - value].clear();
        if (this.modalRef) this.modalRef.close();
      } else {
        this.voucher.applyType = 3 - value;
      }
    });
  }
  onDeleteKeywordSearch() {
    if (!this.keyword) {
      this.getApplyVoucher(this.voucher.applyType);
    }
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
