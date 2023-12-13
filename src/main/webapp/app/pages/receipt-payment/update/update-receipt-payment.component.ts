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
import { DATE_FORMAT_DMY, DATE_TIME_FORMAT } from 'app/config/input.constants';
import { UtilsService } from 'app/utils/Utils.service';
import { CreateBusinessTypeComponent } from '../create-business-type/create-business-type.component';
import moment from 'moment';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './update-receipt-payment.component.html',
  styleUrls: ['./update-receipt-payment.component.scss'],
})
export class UpdateReceiptPaymentComponent extends BaseComponent implements OnInit, OnDestroy {
  idSelect: number;
  tractionSelect: any;
  transactionUpdate: any;
  filterCustomer: FilterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', totalPage: 0 };
  filterBusiness: FilterBusiness = { comId: 0, type: 0, keyword: ' ' };
  keywordCustomer$ = new Subject<string>();
  keywordBusiness$ = new Subject<string>();
  listCustomer: any;
  lstBusinessTypeRes: any;
  type: number;
  lastCompany: any;
  date: any = dayjs();
  now = dayjs();
  disableButton = false;
  canEdit = false;
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
    this.filterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', totalPage: 0 };
    this.listCustomer = [];
    this.lstBusinessTypeRes = [];
    this.canEdit = false;
    this.transactionUpdate = new UpdateTransactionReq();
    this.transactionUpdate.type = this.type;
    this.transactionUpdate.date = this.date;
    this.lastCompany = await this.getCompany();
    if (this.idSelect) {
      await this.getDetailTransaction();
    }
    this.getListCustomer();
    this.getAllBussinessType();
  }

  async getDetailTransaction() {
    this.receiptPaymentService
      .getDetailById({
        comId: this.lastCompany.id,
        type: this.type,
        id: this.idSelect,
      })
      .subscribe(async res => {
        this.tractionSelect = await res.body.data;
        this.transactionUpdate.customerId = this.tractionSelect.customerId;
        this.transactionUpdate.id = this.tractionSelect.id;
        this.transactionUpdate.comId = this.tractionSelect.comId;
        this.transactionUpdate.businessType = this.tractionSelect.businessType;
        this.transactionUpdate.typeDesc = this.tractionSelect.typeDesc;
        this.transactionUpdate.amount = this.tractionSelect.amount;
        this.transactionUpdate.note = this.tractionSelect.note;
        this.transactionUpdate.no = this.tractionSelect.no;
        this.transactionUpdate.type = this.tractionSelect.type;
        this.transactionUpdate.billId = this.tractionSelect.billId;
        this.transactionUpdate.rsInOutWardId = this.tractionSelect.rsInOutWardId;
        let item = dayjs();
        item.add(this.tractionSelect.date);
        this.date = dayjs(this.tractionSelect.date);
        if (this.tractionSelect.billId != null || this.tractionSelect.rsInOutWardId != null) {
          this.canEdit = true;
        }
      });
  }

  closeModal() {
    this.activeModal.close();
  }

  changeMoney(event: Event) {
    const currentInput = event.target as HTMLInputElement;
    if (currentInput.value.indexOf('.') != -1) {
      event.preventDefault();
      currentInput.value = currentInput.value.substring(0, currentInput.value.length - 1);
      return;
    }
  }

  loadMoreCustomer() {
    this.filterCustomer.page++;
    this.getListCustomer();
  }

  getAllBussinessType() {
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

  dismiss(value: any) {
    this.activeModal.close(value);
  }
  enableButton() {
    this.disableButton = false;
    this.disableButton = JSON.parse(JSON.stringify(this.disableButton));
  }

  getListCustomer() {
    this.customerService.query(this.filterCustomer).subscribe(res => {
      this.listCustomer = this.listCustomer.concat(res.body);
      if (!res.length) {
        this.filterCustomer.page -= 1;
      }
    });
  }

  searchCustomer() {
    this.keywordCustomer$
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.filterCustomer.keyword = keyword;
            this.filterCustomer.page = Page.PAGE_NUMBER;
          } else {
            this.filterCustomer.keyword = '';
          }
        }),
        switchMap(() => this.customerService.query(this.filterCustomer))
      )
      .subscribe(res => {
        this.listCustomer = res.body;
      });
  }

  searchBusinessType() {
    this.keywordBusiness$
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.filterBusiness.keyword = keyword;
          } else {
            this.filterCustomer.keyword = '';
          }
          this.filterBusiness.comId = this.lastCompany.id;
          this.filterBusiness.type = this.type;
        }),
        switchMap(() => this.receiptPaymentService.getAllBussinessType(this.filterBusiness))
      )
      .subscribe(res => {
        this.lstBusinessTypeRes = res.body.data;
      });
  }

  validateNumber(event: KeyboardEvent) {
    const charCode = event.which ? event.which : event.keyCode;

    if (charCode !== 46 && (charCode < 48 || charCode > 57)) {
      event.preventDefault();
    }

    if (charCode === 46 && this.transactionUpdate.amount.indexOf('.') !== -1) {
      event.preventDefault();
    }
  }

  changeCustomer() {
    this.listCustomer.forEach(customer => {
      if (customer.id === this.transactionUpdate.customerId) {
        this.transactionUpdate.customerName = customer.name;
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

  updateTransaction() {
    if (!this.date) {
      this.date = dayjs();
    }
    if (!this.transactionUpdate.amount) {
      this.toast.error('Số tiền không được để trống');
    } else {
      let dateObj;
      if (typeof this.date === 'string') {
        dateObj = moment(this.date, 'DD-MM-YYYY', true);
      } else {
        dateObj = moment(this.date.format(DATE_FORMAT_DMY), 'DD-MM-YYYY', true);
      }
      if (!dateObj.isValid()) {
        this.toast.error('Ngày không hợp lệ');
        return;
      }
      if (dateObj.isAfter(moment())) {
        this.toast.error('Ngày không hợp lệ');
        return;
      }
      try {
        if (typeof this.date === 'string') {
          this.transactionUpdate.date = this.date;
        } else {
          this.transactionUpdate.date = this.date.format(DATE_TIME_FORMAT);
        }
      } catch (e) {
        this.transactionUpdate.date = dayjs(this.date, 'DD-MM-YYYY').format(DATE_TIME_FORMAT);
      }
      const index = this.transactionUpdate.date.indexOf(' 00:00:00');
      if (index != -1) {
        let dateAfter = this.transactionUpdate.date.substring(0, index);
        dateAfter += ' ' + dayjs().format('HH:mm:ss');
        this.transactionUpdate.date = dateAfter;
      }
      this.transactionUpdate.comId = this.lastCompany.id;
      this.transactionUpdate.type = this.type;
      if (!this.transactionUpdate.id) {
        // console.log(this.transactionUpdate);

        this.receiptPaymentService.createReceiptPayment(this.transactionUpdate).subscribe(req => {
          if (req.status) {
            this.toast.success(req.message[0].message);
            this.activeModal.close('Saved');
          }
        });
      } else {
        this.receiptPaymentService.updateReceiptPayment(this.transactionUpdate).subscribe(req => {
          if (req.status) {
            this.toast.success(req.message[0].message);
            this.activeModal.close('Saved');
          }
        });
      }
    }
  }

    protected readonly ICON_SAVE = ICON_SAVE;
    protected readonly ICON_CANCEL = ICON_CANCEL;
}
