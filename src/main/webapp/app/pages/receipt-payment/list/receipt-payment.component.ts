import { Component, OnDestroy, OnInit } from '@angular/core';
import { DATE_FORMAT, DATE_FORMAT_DMY, DATE_TIME_FORMAT } from '../../../config/input.constants';
import { ReceiptPaymentService } from '../receipt-payment.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ActivatedRoute, Router } from '@angular/router';
import { SearchTransactionReq, UpdateTransactionReq } from '../receipt-payment';
import { Dayjs, isDayjs } from 'dayjs';

import dayjs from 'dayjs/esm';
import { isString } from '@ng-bootstrap/ng-bootstrap/util/util';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from '../../../utils/Utils.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UpdateReceiptPaymentComponent } from '../update/update-receipt-payment.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from 'app/pages/order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { Subject, filter, switchMap, tap } from 'rxjs';
import { LIST_STATISTICS } from 'app/pages/const/customer-order.const';
import { DeleteReceiptPaymentComponent } from '../delete/delete-receipt-payment.component';
import { CreateBusinessTypeComponent } from '../create-business-type/create-business-type.component';
import { TranslateService } from '@ngx-translate/core';
import { Authority } from '../../../config/authority.constants';
import { ICON_ARROW_DOWN_SOLID, ICON_ARROW_UP_SOLID, ICON_CANCEL, ICON_CANCEL_WHITE } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-receipt-payment',
  templateUrl: './receipt-payment.component.html',
  styleUrls: ['./receipt-payment.component.scss'],
})
export class ReceiptPaymentComponent extends BaseComponent implements OnDestroy, OnInit {
  idEditing: any;
  lastCompany: any;
  receiptAmount: any;
  paymentAmount: any;
  surplus: any;
  previousPage: any;
  searchTransactionReq: SearchTransactionReq;
  lstTransactionRes: any;
  lstBusinessTypeRes: any;
  lstAllBusinessTypeRes: any;
  transactionDetailRes: any;
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  type?: number = undefined;
  typeSelect?: number = undefined;
  totalItems: number = 0;
  checkModalRef: NgbModalRef;
  tractionSelect: any;
  page = 0;
  size = 20;
  sizes = [10, 20, 30];
  indexItem: number;
  listStatistics = LIST_STATISTICS;
  suggestValue: number = 3;
  paramCheckAllPage: any;
  paramCheckAll: boolean = false;
  selectedItem: any = {};
  keywordBusiness$ = new Subject<string>();
  date: any;
  now = dayjs();
  private modalRef: NgbModalRef | undefined;
  ngbDate: any;
  listSelected: any = [];
  authorRevenueAdd = Authority.REVENUE_ADD;
  authorExpenseAdd = Authority.EXPENSE_ADD;
  authorRECEIPT_PAYMENT_UPDATE: string[] = [Authority.REVENUE_UPDATE, Authority.EXPENSE_UPDATE];
  authorRECEIPT_PAYMENT_DELETE: string[] = [Authority.REVENUE_DELETE, Authority.EXPENSE_DELETE];

  constructor(
    private receiptPaymentService: ReceiptPaymentService,
    private router: Router,
    private toastr: ToastrService,
    protected utilsService: UtilsService,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    private activatedRoute: ActivatedRoute,
    private toast: ToastrService
  ) {
    super();
  }

  async ngOnInit() {
    this.lstTransactionRes = [];
    this.lstBusinessTypeRes = [];
    this.lstAllBusinessTypeRes = [];
    this.transactionDetailRes = {};
    this.searchTransactionReq = new SearchTransactionReq();
    this.lastCompany = await this.getCompany();
    await this.getBusinessType0();
    this.searchTransactionReq.type = undefined;
    // this.searchTransactionReq.fromDate = this.fromDate.format(DATE_FORMAT);
    // this.searchTransactionReq.toDate = this.toDate.format(DATE_FORMAT);
    this.searchTransactionReq.comId = this.lastCompany.id;
    this.searchTransactionReq.isCountAll = true;
    this.searchTransactionReq.page = this.page;
    this.searchTransactionReq.size = this.size;
    await this.getListTransaction();
    // await this.getBusinessType1();
  }

  searchByType(number?: number) {
    this.type = number;
    this.typeSelect = number;
    this.page = 0;
    this.searchTransactionReq.type = number;
    this.searchTransactionReq.page = 0;
    this.paramCheckAllPage = false;
    this.paramCheckAll = false;
    this.listSelected = [];
    this.getAllBussinessType();
    this.getListTransaction();
  }

  getDetailTransaction(id: number) {
    const transaction = this.lstTransactionRes.filter(b => b.id == id)[0];
    if (transaction) {
      this.type = transaction.type;
    }
    this.receiptPaymentService
      .getDetailById({
        comId: this.lastCompany.id,
        type: this.type,
        id: id,
      })
      .subscribe(async res => {
        this.tractionSelect = await res.body.data;
        let item = dayjs();
        item.add(this.tractionSelect.date);
        this.date = dayjs(this.tractionSelect.date);

        if (this.tractionSelect.billId != null || this.tractionSelect.rsInOutWardId != null) {
          this.tractionSelect.canEdit = true;
        }
        // this.date = dayjs();
        this.idEditing = this.idEditing == id ? -1 : id;
      });
  }

  async getBusinessType0() {
    this.receiptPaymentService
      .getAllBussinessType({
        comId: this.lastCompany.id,
        type: 0,
        keyword: '',
      })
      .subscribe(async res => {
        this.lstBusinessTypeRes = await res.body.data;
        this.lstAllBusinessTypeRes = await res.body.data;
        this.receiptPaymentService
          .getAllBussinessType({
            comId: this.lastCompany.id,
            type: 1,
            keyword: '',
          })
          .subscribe(async res => {
            this.lstBusinessTypeRes = await this.lstBusinessTypeRes.concat(res.body.data);
            this.lstAllBusinessTypeRes = await this.lstAllBusinessTypeRes.concat(res.body.data);
          });
      });
  }

  async getBusinessType1() {
    this.receiptPaymentService
      .getAllBussinessType({
        comId: this.lastCompany.id,
        type: 1,
        keyword: '',
      })
      .subscribe(async res => {
        this.lstBusinessTypeRes = await this.lstBusinessTypeRes.concat(res.body.data);
        this.lstAllBusinessTypeRes = await this.lstAllBusinessTypeRes.concat(res.body.data);
      });
  }

  async getAllBussinessType() {
    if (!this.type) {
      await this.getBusinessType0();
    } else {
      this.receiptPaymentService
        .getAllBussinessType({
          comId: this.lastCompany.id,
          type: this.type,
          keyword: '',
        })
        .subscribe(res => {
          this.lstBusinessTypeRes = res.body.data;
        });
    }
  }

  formatDate(date: string) {
    const data = dayjs(date);
    return data.format('DD/MM/YYYY');
  }

  getBusinessTypeName(id: number) {
    let business = this.lstAllBusinessTypeRes.filter(res => res.id === id)[0];
    if (business) {
      return business.businessTypeName;
    }
    return null;
  }

  async getListTransaction() {
    try {
      this.searchTransactionReq.fromDate = this.utilsService.convertDate(this.fromDate) ?? '';
      this.searchTransactionReq.toDate = this.utilsService.convertDate(this.toDate) ?? '';
    } catch (e: any) {
      // this.toastr.error('Định dạng ngày không hợp lệ!', 'ERROR');
    }
    this.receiptPaymentService.getAllTransactions(this.searchTransactionReq).subscribe(res => {
      this.receiptAmount = res.body.data.receiptAmount;
      this.paymentAmount = res.body.data.paymentAmount;
      this.surplus = this.receiptAmount - this.paymentAmount;
      this.lstTransactionRes = res.body.data.receiptPaymentList;
      this.totalItems = res.body.count;
      // this.lstTransactionResPaging = this.lstTransactionRes.slice(0, this.size);
      this.indexItem = this.size * this.page;
      if (this.paramCheckAll) {
        this.lstTransactionRes.forEach(n => {
          n.check = !this.listSelected.some(m => m === n.id);
        });
      } else {
        if (this.listSelected?.length > 0) {
          this.lstTransactionRes.forEach(n => {
            n.check = this.listSelected.includes(n.id);
          });
        }
      }
    });
  }

  onSearch() {
    this.page = 1;
    this.searchTransactionReq.size = this.size;
    this.searchTransactionReq.page = this.page;
    this.handleNavigation(this.page);
  }

  changeDate() {
    console.log(this.date);
  }

  navigateToPage(page: number) {
    if (this.paramCheckAllPage && !this.paramCheckAll) {
      this.paramCheckAllPage = false;
      this.listSelected = [];
    }
    this.handleNavigation(page);
  }

  protected handleNavigation(page, predicate?: string, ascending?: boolean): void {
    this.page = page;
    const queryParamsObj = {
      page,
      size: this.size,
      type: this.type,
    };
    // if (page !== this.previousPage) {
    //   this.previousPage = page;
    //   this.router.navigate(['./'], {
    //     relativeTo: this.activatedRoute,
    //     queryParams: queryParamsObj,
    //   });
    // }
    this.searchTransactionReq.page = page - 1;
    this.getListTransaction();
  }

  viewTransaction(transaction: any) {
    this.idEditing = this.idEditing == transaction.id ? -1 : transaction.id;
    this.receiptPaymentService.getDetailById(transaction.id).subscribe(res => {
      this.transactionDetailRes = res.data;
    });
  }
  getColspan() {
    return window.innerWidth >= 961 ? 12 : 7;
  }

  openModalUpdateTransaction(id: any) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.checkModalRef = this.modalService.open(UpdateReceiptPaymentComponent, {
      size: '',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    const transaction = this.lstTransactionRes.filter(b => b.id == id)[0];
    if (transaction) {
      this.checkModalRef.componentInstance.type = transaction.type;
    }
    this.checkModalRef.componentInstance.idSelect = id;
    this.checkModalRef.result.then(() => {
      this.getListTransaction();
    });
  }

  createTransaction() {
    this.checkModalRef = this.modalService.open(UpdateReceiptPaymentComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    this.checkModalRef.componentInstance.type = this.type;
  }

  deleteMultiTransaction() {
    if (!this.paramCheckAll && this.listSelected.length == 0) {
      return;
    }
    this.modalRef = this.modalService.open(DeleteReceiptPaymentComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.listSelected = this.listSelected;
    this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
    this.modalRef.componentInstance.totalItems = this.totalItems;
    this.modalRef.componentInstance.fromDate = this.searchTransactionReq.fromDate;
    this.modalRef.componentInstance.toDate = this.searchTransactionReq.toDate;
    this.modalRef.componentInstance.keyword = this.searchTransactionReq.keyword;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res == 'Deleted') {
        this.getListTransaction();
        this.idEditing = -1;
      }
    });
  }

  deleteTransaction() {
    this.receiptPaymentService.deleteReceiptPayment(this.tractionSelect.id).subscribe(res => {
      if (res.status) {
        this.toast.success(res.reason);
        this.getListTransaction();
      }
    });
  }

  onChangedPage(event: any): void {
    this.page = event - 1;
    this.searchTransactionReq.page = this.page;
    if (this.paramCheckAllPage && !this.paramCheckAll) {
      this.paramCheckAllPage = false;
      this.lstTransactionRes = [];
    }
    this.getListTransaction();
  }

  checkAll(isCheck: boolean) {
    this.paramCheckAll = isCheck;
    this.paramCheckAllPage = isCheck;
    this.utilsService.checkAll(this.lstTransactionRes, this.lstTransactionRes, this.paramCheckAll, false);
  }

  cancelUpdate() {
    this.idEditing = -1;
  }

  update() {
    this.tractionSelect.date = this.date.format(DATE_TIME_FORMAT);
    this.receiptPaymentService.updateReceiptPayment(this.tractionSelect).subscribe((res: any) => {
      if (res.status) {
        this.toast.success(res.message[0].message);
        this.getListTransaction();
        this.idEditing = -1;
      }
    });
  }

  getPagedItems() {
    const startIndex = this.page * this.size;
    const endIndex = startIndex + this.size;
    this.lstTransactionRes = this.lstTransactionRes.slice(startIndex, endIndex);
    this.indexItem = this.size * this.page;
  }

  openModalCreateTransaction(type: number) {
    this.modalRef = this.modalService.open(UpdateReceiptPaymentComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.type = type;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res == 'Saved') {
        this.getListTransaction();
        this.idEditing = -1;
      }
    });
  }

  openModalCreateBusinessType(type: number) {
    this.modalRef = this.modalService.open(CreateBusinessTypeComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.type = type;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res == 'Saved') {
        this.getAllBussinessType();
      }
    });
  }

  openModalDeleteTransaction(tractionSelect: any) {
    this.modalRef = this.modalService.open(DeleteReceiptPaymentComponent, { size: '', backdrop: 'static' });
    const transaction = this.lstTransactionRes.filter(b => b.id == tractionSelect.id)[0];
    if (transaction) {
      this.modalRef.componentInstance.type = transaction.type;
    }
    this.modalRef.componentInstance.id = tractionSelect.id;
    this.modalRef.componentInstance.no = tractionSelect.no;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res == 'Deleted') {
        this.getListTransaction();
        this.listSelected = [];
        this.idEditing = -1;
      }
    });
  }

  async onDeleteKeywordSearch() {
    if (!this.searchTransactionReq.keyword) {
      await this.getListTransaction();
    }
  }

  changeStatistic(event) {
    switch (event.id) {
      case 1:
        this.searchTransactionReq.fromDate = dayjs().format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      case 2:
        this.searchTransactionReq.fromDate = dayjs().startOf('week').add(1, 'day').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      case 3:
        this.searchTransactionReq.fromDate = dayjs().startOf('month').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      case 4:
        this.searchTransactionReq.fromDate = dayjs().startOf('year').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      case 5:
        this.searchTransactionReq.fromDate = dayjs().subtract(1, 'day').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().subtract(1, 'day').format(DATE_FORMAT);
        break;
      case 6:
        this.searchTransactionReq.fromDate = dayjs().subtract(6, 'day').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      case 7:
        this.searchTransactionReq.fromDate = dayjs().subtract(30, 'day').format(DATE_FORMAT);
        this.searchTransactionReq.toDate = dayjs().format(DATE_FORMAT);
        break;
      default:
    }
    this.getListTransaction();
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
  protected readonly ICON_ARROW_DOWN_SOLID = ICON_ARROW_DOWN_SOLID;
  protected readonly ICON_ARROW_UP_SOLID = ICON_ARROW_UP_SOLID;
}
