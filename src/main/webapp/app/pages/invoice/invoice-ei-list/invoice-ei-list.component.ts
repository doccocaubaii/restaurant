import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from '../../../config/pagination.constants';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { UtilsService } from '../../../utils/Utils.service';
import { TranslateService } from '@ngx-translate/core';
import { ConfirmDialogService } from '../../../shared/service/confirm-dialog.service';
import { InvoiceService } from '../service/invoice.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { LoadingOption } from '../../../utils/loadingOption';
import { InvoiceDetailComponent } from '../invoice-detail/invoice-detail.component';
import { HttpHeaders } from '@angular/common/http';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { UpdateInvoiceComponent } from '../../../layouts/modal/invoice-update/update-invoice.component';
import { ResponseModalComponent } from '../../../shared/modal/response-modal/response-modal.component';
import { InvoiceSyncEIComponent } from '../invoice-sync-ei/invoice-sync-ei.component';
import { ALL_TYPE, STATUS_MAP, TCT_CHECK_STATUS_MAP } from '../../../constants/invoice.constants';

@Component({
  selector: 'jhi-invoice-ei-list',
  templateUrl: './invoice-ei-list.component.html',
  styleUrls: ['./invoice-ei-list.component.scss'],
})
export class InvoiceEiListComponent extends BaseComponent implements OnInit {
  minDate: dayjs.Dayjs | any;
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  @ViewChild('deleteInvoiceModal') deleteInvoiceModal: ElementRef;
  @ViewChild('sendMailModal') sendMailModal: ElementRef;
  @ViewChild('publishInvoiceModal') publishInvoiceModal: ElementRef;
  listInvoice: any = [];
  listSelected: any = [];
  sizes = [10, 20, 30];
  filterInvoice: any = {
    page: 0,
    size: 10,
  };
  totalItems: any = 0;
  itemsPerPage = ITEMS_PER_PAGE;
  page = 1;
  previousPage: any;
  type: any;
  idEditing: any;
  invoiceSelect: any;
  checkModalRef: NgbModalRef;
  email: any;
  lastCompany: any;
  patterns: any;
  paramCheckAllPage: any;
  paramCheckAll: boolean = false;
  selectedItem: any = {};

  constructor(
    private utilService: UtilsService,
    protected modalService: NgbModal,
    protected translateService: TranslateService,
    protected confirmDialog: ConfirmDialogService,
    private invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    protected toast: ToastrService,
    protected utilsService: UtilsService,
    public loading: LoadingOption,
    public router: Router
  ) {
    super();
  }

  async ngOnInit() {
    this.minDate = dayjs().subtract(4, 'years');
    this.invoiceSelect = {};
    this.filterInvoice = {};
    this.fromDate = null;
    this.toDate = null;
    this.lastCompany = await this.getCompany();
    // this.activatedRoute.data.subscribe(data => {
    //   console.log("========================",data)
    //   if (data.pagingParams) {
    //     this.page = data.pagingParams.page;
    //     this.itemsPerPage = data.pagingParams.size;
    //     this.type = data.pagingParams.size;
    //   }
    // });
    this.activatedRoute.queryParamMap.subscribe(params => {
      this.type = params.get('type');
      if (params.get('page')) {
        this.page = +(params.get('page') ?? 1);
      }
      if (params.get('size')) {
        this.itemsPerPage = +(params.get('size') ?? 0);
      }
      // this.getOwnerInfo();
      this.getListInvoice();
    });
  }
  // getOwnerInfo() {
  //   this.invoiceService.getOwnerInfo(this.lastCompany.id).subscribe(res => {
  //     this.patterns = res.data.patterns;
  //   });
  // }
  onSearch() {
    this.page = 1;
    this.handleNavigation(this.page);
    this.getListInvoice();
  }

  getListInvoice() {
    this.invoiceService
      .getInvoiceWithPaging({
        page: this.page - 1,
        size: this.itemsPerPage,
        isCountAll: true,
        fromDate: this.utilService.convertDate(this.fromDate) ?? '',
        toDate: this.utilService.convertDate(this.toDate) ?? '',
        taxCheckStatus: this.type || this.type === 0 ? this.type : '',
        no: this.filterInvoice.no ? this.filterInvoice.no : '',
        customerName: this.filterInvoice.customerName ? this.filterInvoice.customerName : '',
        viewType: 0,
      })
      .subscribe(res => {
        this.listInvoice = res.body.data;
        this.totalItems = res.body.count ? res.body.count : 0;
        if (this.paramCheckAll) {
          this.listInvoice.forEach(n => {
            n.check = !this.listSelected.some(m => m === n.id);
          });
        } else {
          this.listSelected = [];
          this.listInvoice.forEach(n => {
            if (this.paramCheckAllPage) {
              n.check = true;
              this.listSelected.push(n.id);
            } else {
              n.check = false;
            }
          });
        }
      });
  }

  filterOrderList() {
    this.filterInvoice.fromDate = this.fromDate;
    this.filterInvoice.toDate = this.fromDate;
    this.paramCheckAll = false;
    this.paramCheckAllPage = false;
    this.listSelected = [];
    this.getListInvoice();
  }

  openViewDetailOrder(invoice: any) {
    const dialogRef = this.modalService.open(InvoiceDetailComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.id = invoice.id;
    dialogRef.closed.subscribe();
  }

  protected fillComponentAttributesFromResponseHeader(headers: HttpHeaders): void {
    this.filterInvoice.totalItem = Number(headers.get(TOTAL_COUNT_RESPONSE_HEADER));
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
      size: this.itemsPerPage,
      type: this.type,
    };
    if (page !== this.previousPage) {
      this.previousPage = page;
      this.router.navigate(['./'], {
        relativeTo: this.activatedRoute,
        queryParams: queryParamsObj,
      });
    }
  }

  searchByType(number?: number) {
    this.type = number;
    this.paramCheckAllPage = false;
    this.paramCheckAll = false;
    this.listSelected = [];
    this.onSearch();
  }

  getColspan() {
    return 10;
  }

  viewInvoice(invoice) {
    this.idEditing = this.idEditing == invoice.id ? -1 : invoice.id;
    this.invoiceService.findEI(invoice.id, 0).subscribe(res => {
      this.invoiceSelect = res.data;
    });
  }

  publishInvoice() {
    const request = [
      {
        invoiceId: this.invoiceSelect.id,
        invoiceIKey: this.invoiceSelect.ikey,
      },
    ];
    this.invoiceService.publishInvoice(request).subscribe(res => {
      if (res.status) {
        if (this.checkModalRef) {
          this.checkModalRef.close();
        }
        this.toast.success(res.reason);
        this.getListInvoice();
      }
    });
  }

  sendMail() {
    this.invoiceService
      .sendMailInvoice({
        invoiceId: this.invoiceSelect.id,
        ikey: this.invoiceSelect.ikey,
        emails: this.email,
      })
      .subscribe(res => {
        if (res.status) {
          if (this.checkModalRef) {
            this.checkModalRef.close();
          }
          this.toast.success(res.message[0].message);
        }
      });
  }

  viewInvoicePdf(invoice: any) {
    this.invoiceService
      .viewInvoicePdf({
        invoiceId: invoice.id,
        ikey: invoice.ikey,
        type: 0,
      })
      .subscribe(
        res => {
          // console.log(res);
          if (res.body.status) {
            this.toast.success(res.body.reason, this.translateService.instant('global.info.notify'));
            const imageName = 'name.png';
            const imageBlob = this.dataURItoBlob(res.body.data);
            const file = new File([imageBlob], imageName, { type: 'application/pdf' });
            // const file = new Blob([res.body.data], {type: 'application/pdf'});
            const fileURL = window.URL.createObjectURL(file);
            window.open(fileURL, '_blank');
          }
        },
        error => {
          if (error.error.message[0] && error.error.message[0].code)
            this.toast.error(error.error.message[0].code, this.translateService.instant('global.info.notify'));
        }
      );
  }

  dataURItoBlob(dataURI) {
    const byteString = window.atob(dataURI);
    const arrayBuffer = new ArrayBuffer(byteString.length);
    const int8Array = new Uint8Array(arrayBuffer);
    for (let i = 0; i < byteString.length; i++) {
      int8Array[i] = byteString.charCodeAt(i);
    }
    const blob = new Blob([int8Array], { type: 'image/png' });
    return blob;
  }

  openMailModal(invoice: any) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.invoiceSelect = invoice;
    this.checkModalRef = this.modalService.open(this.sendMailModal, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
  }

  openModalPublish(invoice: any) {
    this.invoiceSelect = invoice;
    // Gọi model
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    // Tạo đối tượng gửi sang model với các value
    // title: tiêu đề model
    // message: Nội dung model
    // btnText: Nội dung nút đồng ý
    // icon: Icon nút đồng ý
    // classBtn: Class nút đồng ý
    this.checkModalRef.componentInstance.value = new DialogModal(
      ModalHeader.PUBLISH_INVOICE,
      ModalContent.PUBLISH_INVOICE,
      ModalBtn.PUBLISH_INVOICE,
      'arrow-up-from-bracket'
    );
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
        this.publishInvoice();
        this.checkModalRef.close();
      }
    });
  }

  openModalEditInvoice(invoice: any) {
    if (this.checkModalRef) {
      this.checkModalRef.close();
    }
    this.invoiceService.find(invoice.id).subscribe(async res => {
      this.invoiceSelect = res.data;
      this.checkModalRef = this.modalService.open(UpdateInvoiceComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      this.checkModalRef.componentInstance.invoiceSelect = this.invoiceSelect;
      this.checkModalRef.closed.subscribe(reason => {
        this.onSearch();
      });
    });
  }

  onSearchPage() {
    this.onSearch();
  }

  getItem(invoice: any) {
    this.selectedItem = invoice;
  }
  getColorTctCheckStatus(taxCheckStatus: any) {
    let color = '';
    switch (taxCheckStatus) {
      case -1: {
        //Hóa đơn đang kiểm tra
        color = 'text-warning';
        break;
      }
      case -2: {
        //Hóa đơn không hợp lệ
        color = 'text-danger';
        break;
      }
      case 1: {
        //Hóa đơn hợp lệ
        color = 'text-primary';
        break;
      }
      default: {
        color = 'text-dark';
      }
    }
    return color;
  }

  private modalRef: NgbModalRef | undefined;
  loadDataFromEasyInvoice() {
    this.modalRef = this.modalService.open(InvoiceSyncEIComponent, {
      backdrop: 'static',
      windowClass: 'invoice-sync',
    });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        this.onSearch();
      }
    });
  }

  getColorStatus(status: any, type: number) {
    let color = '';
    switch (status) {
      case 3:
      case 4: {
        //Hóa đơn đang kiểm tra
        color = 'text-warning';
        break;
      }
      case 5: {
        //Hóa đơn không hợp lệ
        color = 'text-danger';
        break;
      }
      case 1: {
        if (ALL_TYPE.includes(type)) {
          color = 'text-primary';
        } else {
          color = 'text-dark';
        }
        break;
      }
      default: {
        color = 'text-dark';
      }
    }
    return color;
  }

  protected readonly STATUS_MAP = STATUS_MAP;
  protected readonly TCT_CHECK_STATUS_MAP = TCT_CHECK_STATUS_MAP;
}
