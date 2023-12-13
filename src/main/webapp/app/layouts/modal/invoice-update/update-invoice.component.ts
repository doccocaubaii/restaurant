import { Component, OnDestroy, AfterViewInit, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../../shared/base/base.component';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../../../pages/invoice/service/invoice.service';
import { last_company } from '../../../object-stores.constants';
import { ConfigService } from '../../config/service/config.service';
import { debounceTime, distinctUntilChanged, Subject, switchMap, tap } from 'rxjs';
import { Page } from '../../../pages/const/customer-order.const';
import { FilterCustomer } from '../../../pages/order/model/filterCustomer.model';
import { CustomerService } from '../../../pages/order/service/customer.service';
import dayjs from 'dayjs/esm';
import { LoadingOption } from '../../../utils/loadingOption';
import { UtilsService } from '../../../utils/Utils.service';
import { DATE_FORMAT } from '../../../config/input.constants';
import {ICON_CANCEL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-configuration',
  templateUrl: './update-invoice.component.html',
  styleUrls: ['./update-invoice.component.scss'],
})
export class UpdateInvoiceComponent extends BaseComponent implements OnInit, OnDestroy {
  invoiceSelect: any;
  invoiceUpdate: any;
  filterCustomer: FilterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', totalPage: 0 };
  keywordCustomer$ = new Subject<string>();
  listCustomer: any;
  paymentMethod: any = '';
  paymentMethodString: any = '';

  ngOnDestroy(): void {}

  constructor(
    private modalService: NgbModal,
    public activeModal: NgbActiveModal,
    private configService: ConfigService,
    private customerService: CustomerService,
    private invoiceService: InvoiceService,
    private loading: LoadingOption,
    private utilsService: UtilsService,
    private toast: ToastrService
  ) {
    super();
  }

  async ngOnInit() {
    this.filterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', totalPage: 0 };
    this.listCustomer = [];
    this.invoiceUpdate = {};
    this.getListCustomer();
    let item = dayjs(this.invoiceSelect.arisingDate, 'dd/mm/yyyy');
    // item.add(this.invoiceSelect.arisingDate);
    this.invoiceUpdate.arisingDate = item;
    // console.log(this.invoiceUpdate.arisingDate);
    this.invoiceUpdate.customerId = this.invoiceSelect.customerId;
    this.invoiceUpdate.vatAmount = this.invoiceSelect.vatAmount;
    this.invoiceUpdate.totalPreTax = this.invoiceSelect.totalPreTax;
    this.invoiceUpdate.totalAmount = this.invoiceSelect.totalAmount;
    this.invoiceUpdate.id = this.invoiceSelect.id;
    this.paymentMethod = this.invoiceSelect.paymentMethod;
    // console.log(this.invoiceUpdate);
  }

  closeModal() {
    this.activeModal.close();
  }

  loadMoreCustomer() {
    this.filterCustomer.page++;
    this.getListCustomer();
  }

  getListCustomer() {
    this.customerService.query(this.filterCustomer).subscribe(res => {
      this.listCustomer = this.listCustomer.concat(res.body);
      if (!res.length) {
        this.filterCustomer.page -= 1;
      }
      if (!this.invoiceSelect.customerId) {
        this.invoiceSelect.customerId = res.body[0].id;
        this.invoiceSelect.customerName = res.body[0].name;
      }
    });
  }

  addCustomer() {
    const dialogRef = this.modalService.open('CustomerComponent', {
      size: 'xl',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.closed.subscribe();
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

  changeCustomer() {
    this.listCustomer.forEach(customer => {
      if (customer.id === this.invoiceSelect.customerId) {
        this.invoiceSelect.customerName = customer.name;
      }
    });
  }

  updateInvoice() {
    let invoice = Object.assign({}, this.invoiceUpdate);
    this.convertPaymentMethodToString(invoice.paymentMethod);
    // console.log(this.loading.isLoading);
    invoice.paymentMethod = this.paymentMethodString;
    invoice.arisingDate = invoice.arisingDate.format('DD/MM/YYYY');
    this.invoiceService.modifyInvoice(invoice).subscribe(req => {
      if (req.status) {
        // console.log(this.loading.isLoading);
        this.toast.success(req.message[0].message);
        this.activeModal.close();
      }
    });
  }

  convertPaymentMethodToString(paymentMethod: number) {
    if (this.paymentMethod !== 'Khác') {
      this.paymentMethodString = this.paymentMethod;
    }
  }

  changeDate() {
    // console.log(this.invoiceUpdate);
  }

  changeTotalPreTax() {
    this.invoiceUpdate.totalAmount = (this.invoiceUpdate.totalPreTax ?? 0) + (this.invoiceUpdate.vatAmount ?? 0);
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
}
