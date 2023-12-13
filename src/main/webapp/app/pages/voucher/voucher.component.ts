import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../shared/base/base.component';
import { Customer } from '../../entities/customer/customer';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ToastrService } from 'ngx-toastr';
import { CustomerService } from '../customer/customer.service';
import { VoucherService } from './voucher.service';
import { UtilsService } from '../../utils/Utils.service';
import dayjs from 'dayjs/esm';
import { CreateStaffComponent } from '../staff/create-staff/create-staff.component';
import { SaveVoucherComponent } from './save-voucher/save-voucher.component';
import { CreateAreaComponent } from '../area/create-area/create-area.component';
import { ApplyVoucherComponent } from './apply-voucher/apply-voucher.component';
import { Dayjs } from 'dayjs';
import { Authority } from '../../config/authority.constants';
import { ICON_APPLY } from '../../shared/other/icon';
import { VoucherConstants } from '../../constants/voucher.constants';
@Component({
  selector: 'jhi-voucher',
  templateUrl: './voucher.component.html',
  styleUrls: ['./voucher.component.scss'],
})
export class VoucherComponent extends BaseComponent implements OnInit {
  startTime: dayjs.Dayjs | any;
  endTime: dayjs.Dayjs | any;
  company: any;
  companyId: number;
  totalSize = 0;
  page: number = 1;
  pageSize: number = 20;
  keyword: string = '';
  voucherArray: any;
  idDel: number = -1;
  idEditing: number = -1;
  paramCheckAllPage: any;
  paramCheckAll: boolean = false;
  listSelected: any = [];
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
  private modalRef: NgbModalRef | undefined;
  authorADD = Authority.VOUCHER_ADD;
  authorVIEW = Authority.VOUCHER_VIEW;
  authorUPDATE = Authority.VOUCHER_UPDATE;
  authorDELETE = Authority.VOUCHER_DELETE;
  authorAPPLY = Authority.VOUCHER_APPLY;

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
    this.getVouchers();
  }

  getVouchers() {
    const tmpStartTime = this.utilsService.convertDate(this.startTime) ?? '';
    const tmpEndTime = this.utilsService.convertDate(this.endTime) ?? '';
    this.voucherService
      .getVouchers(this.companyId, this.page, this.pageSize, this.keyword, tmpStartTime, tmpEndTime, VoucherConstants.TYPE_REQUEST_VOUCHER)
      .subscribe(respond => {
        this.voucherArray = respond.data;
        this.totalSize = respond.count;
        for (let element of this.voucherArray) {
          let tmpDate = dayjs(element.endTime);
          tmpDate = tmpDate.add(-1, 'day');
          element.endTime = this.utilsService.convertDate(tmpDate);
          element.endTime = this.utilsService.changeFomatDateOfString(element.endTime, 'YYYY-MM-DD', 'DD/MM/YYYY');
          element.startTime = this.utilsService.changeFomatDateOfString(element.startTime, 'YYYY-MM-DD', 'DD/MM/YYYY');
        }
      });
  }

  openCreate(voucher?: any) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(SaveVoucherComponent, { size: 'xl', backdrop: 'static' });
    if (voucher) {
      if (voucher.startTime) this.modalRef.componentInstance.fromDate = dayjs(voucher.startTime);
      if (voucher.endTime) this.modalRef.componentInstance.toDate = dayjs(voucher.endTime);
      for (let key in voucher) {
        if (voucher[key] !== null) {
          this.modalRef.componentInstance.voucher[key] = voucher[key];
        }
      }
    }
    this.modalRef.result.then(
      result => {
        this.getVouchers();
      },
      reason => {}
    );
  }

  openDelete() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_VOUCHER,
      ModalHeader.DELETE_VOUCHER,
      ModalHeader.DELETE_VOUCHER,
      'trash-can',
      'btn-delete'
    );
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.deleteVoucher();
        if (this.modalRef) this.modalRef.close();
      }
    });
    this.modalRef.result.then(
      result => {},
      reason => {}
    );
  }
  deleteVoucher() {
    this.idEditing = -1;
    //gọi api
    this.voucherService.delVoucher(this.idDel).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getVouchers();
        this.modalRef?.close();
      }
    });
  }
  filterOrderList() {
    this.paramCheckAll = false;
    this.paramCheckAllPage = false;
    this.listSelected = [];
    this.getVouchers();
  }

  onDeleteKeywordSearch() {
    if (!this.keyword) {
      this.getVouchers();
    }
  }

  openApply(voucher: any) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(ApplyVoucherComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.voucher.id = voucher.id;
    this.modalRef.componentInstance.voucher.isDefaul = !voucher.applyType;

    this.modalRef.result.then(result => {
      this.getVouchers();
    });
  }

    protected readonly ICON_APPLY = ICON_APPLY;
}
