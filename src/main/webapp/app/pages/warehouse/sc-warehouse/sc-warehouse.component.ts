import { Component, OnDestroy, OnInit } from '@angular/core';
import { DATE_FORMAT } from '../../../config/input.constants';
import { WarehouseService } from '../warehouse.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchTransactionReq } from '../warehouse';
import dayjs, { isDayjs } from 'dayjs';
import { isString } from '@ng-bootstrap/ng-bootstrap/util/util';
import { ToastrService } from 'ngx-toastr';
import { INOUT_WARD, RS_INOUT_WARD } from '../../../constants/app.routing.constants';
import { TranslateService } from '@ngx-translate/core';
import moment from 'moment';
import { UtilsService } from '../../../utils/Utils.service';
import { Authority } from '../../../config/authority.constants';
import { ICON_CIRCLE_ARROW_DOWN, ICON_CIRCLE_ARROW_UP } from '../../../shared/other/icon';
import { BUSINESS_TYPE_IN_WARD, BUSINESS_TYPE_OUT_WARD } from '../../../constants/common.constants';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { LoadingBarService } from '@ngx-loading-bar/core';

@Component({
  selector: 'jhi-sc-warehouse',
  templateUrl: './sc-warehouse.component.html',
  styleUrls: ['./sc-warehouse.component.scss'],
})
export class ScWarehouseComponent extends BaseComponent implements OnDestroy, OnInit {
  idEditing: any = 0;
  lastCompany: any;
  inWardAmount: any;
  outWardAmount: any;
  searchTransactionReq: SearchTransactionReq;
  lstTransactionRes: any;
  transactionDetailRes: any;
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;

  totalItems: any = 0;
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
  RS_INWARD_ADD = Authority.RS_INWARD_ADD;
  RS_INWARD_VIEW = Authority.RS_INWARD_VIEW;
  RS_INWARD_UPDATE = Authority.RS_INWARD_UPDATE;
  RS_INWARD_DELETE = Authority.RS_INWARD_DELETE;

  constructor(
    private warehouseService: WarehouseService,
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    public utilService: UtilsService,
    protected activatedRoute: ActivatedRoute,
    private modalService: NgbModal,
    private loadingService: LoadingBarService
  ) {
    super();
  }

  async ngOnInit() {
    this.lstTransactionRes = [];
    this.transactionDetailRes = {};
    this.fromDate = {};
    this.toDate = {};
    this.searchTransactionReq = new SearchTransactionReq();
    this.searchTransactionReq.getWithPaging = true;
    this.searchTransactionReq.page = 1;
    this.searchTransactionReq.size = 20;
    this.lastCompany = await this.getCompany();
    this.searchTransactionReq.comId = this.lastCompany.id;
    this.getListTransaction();
  }

  searchByType(number?: number) {
    if (number === 3) {
      this.searchTransactionReq.type = undefined;
    } else {
      this.searchTransactionReq.type = number;
    }
    this.getListTransaction();
  }

  getListTransaction() {
    try {
      if (this.fromDate) {
        if (Object.keys(this.fromDate).length !== 0) {
          this.searchTransactionReq.fromDate = this.fromDate
            ? new Date(this.utilService.convertDate(this.fromDate)).toISOString().slice(0, 19).replace('T', ' ')
            : '';
        }
      } else {
        this.searchTransactionReq.fromDate = undefined;
      }
      if (this.toDate) {
        if (Object.keys(this.toDate).length !== 0) {
          this.searchTransactionReq.toDate = this.toDate
            ? new Date(this.utilService.convertDate(this.toDate)).toISOString().slice(0, 19).replace('T', ' ')
            : '';
        }
      } else {
        this.searchTransactionReq.toDate = undefined;
      }
      const req = Object.assign({}, this.searchTransactionReq);
      req.page = this.searchTransactionReq.page - 1;
      this.warehouseService.getTransactions(req).subscribe(res => {
        this.lstTransactionRes = [];
        this.outWardAmount = res.body.data.outWardAmount;
        this.inWardAmount = res.body.data.inWardAmount;
        this.totalItems = res.body.count;
        this.lstTransactionRes = res.body.data.inOutWardList;
      });
    } catch (e) {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.error.formatDate'),
        this.translateService.instant('easyPos.warehouse.error.message')
      );
    }
  }

  viewTransaction(transaction: any) {
    this.idEditing = this.idEditing === transaction.id ? -1 : transaction.id;
    this.warehouseService.transactionDetail(transaction.id).subscribe(res => {
      this.transactionDetailRes = res.data;
    });
  }

  getColspan() {
    return window.innerWidth >= 961 ? 8 : 8;
  }

  onWarehouseIntake(type: any) {
    this.router.navigate([`/` + RS_INOUT_WARD + `/` + INOUT_WARD],{
      relativeTo: this.activatedRoute,
        queryParams: {
          type: type,
      },
    });
  }

  loadMore($event: any) {
    this.searchTransactionReq.page = $event;
    this.getListTransaction();
  }
  onChangeKeyword() {
    if (!this.searchTransactionReq.keyword) {
      this.onSearch();
    }
  }
  onSearch() {
    this.lstTransactionRes = [];
    this.searchTransactionReq.page = 1;
    this.getListTransaction();
  }

  updateRsInward(transaction: any) {
    this.router.navigate([`/${RS_INOUT_WARD + '/' + INOUT_WARD}`], {
      relativeTo: this.activatedRoute,
      queryParams: {
        id: transaction.id,
        type: transaction.type,
      },
    });
  }

  private modalRef: NgbModalRef | undefined;
  deleteRsInWard(id: number) {
    this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_INWARD,
      ModalContent.DELETE_INWARD,
      ModalBtn.AGREE,
      'trash-can',
      'btn-delete'
    );
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        const request = {
          comId: this.lastCompany.id,
          rsInoutwardId: id,
        };
        this.loadingService.start();
        this.warehouseService.deleteInoutward(request).subscribe(
          value => {
            this.loadingService.complete();
            this.toastr.success(value.message[0].message);
            this.modalService.dismissAll();
            this.getListTransaction();
          },
          error => {
            this.loadingService.complete();
          }
        );
      }
    });
  }

  protected readonly BUSINESS_TYPE_IN_WARD = BUSINESS_TYPE_IN_WARD;
  protected readonly BUSINESS_TYPE_OUT_WARD = BUSINESS_TYPE_OUT_WARD;

  protected readonly ICON_CIRCLE_ARROW_DOWN = ICON_CIRCLE_ARROW_DOWN;
  protected readonly ICON_CIRCLE_ARROW_UP = ICON_CIRCLE_ARROW_UP;
}
