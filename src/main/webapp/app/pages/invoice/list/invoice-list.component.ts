import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { ITEMS_PER_PAGE, TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import dayjs from 'dayjs/esm';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../service/invoice.service';
import { ActivatedRoute, Router } from '@angular/router';
import { InvoiceDetailComponent } from '../invoice-detail/invoice-detail.component';
import { ToastrService } from 'ngx-toastr';
import { FilterCustomer } from '../../order/model/filterCustomer.model';
import { Page } from '../../const/customer-order.const';
import { CustomerService } from '../../order/service/customer.service';
import { debounceTime, distinctUntilChanged, lastValueFrom, Subject, switchMap, tap } from 'rxjs';
import { UpdateInvoiceComponent } from '../../../layouts/modal/invoice-update/update-invoice.component';
import { LoadingOption } from '../../../utils/loadingOption';
import { UtilsService } from '../../../utils/Utils.service';
import { ConfirmDialogService } from '../../../shared/service/confirm-dialog.service';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { TranslateService } from '@ngx-translate/core';
import { DialogModal } from '../../order/model/dialogModal.model';
import { BaseComponent } from '../../../shared/base/base.component';
import { ResponseModalComponent } from '../../../shared/modal/response-modal/response-modal.component';
import { InvoiceSyncEIComponent } from '../invoice-sync-ei/invoice-sync-ei.component';
import { ALL_TYPE } from '../../../constants/invoice.constants';
import { Authority } from '../../../config/authority.constants';
import { INVOICE_METHOD, STATUS_MAP, TCT_CHECK_STATUS_MAP } from '../../../constants/invoice.constants';
import { CertificateService } from '../../../config/service/certificate-service.service';
import { SignaturesPopupComponent } from '../../../shared/signatures/signatures-popup.component';
import { ICON_CANCEL, ICON_CANCEL_WHITE } from '../../../shared/other/icon';

@Component({
  selector: 'invoice-list',
  templateUrl: './invoice-list.component.html',
  styleUrls: ['./invoice-list.component.scss'],
})
export class InvoiceListComponent extends BaseComponent implements OnInit {
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
  protected readonly STATUS_MAP = STATUS_MAP;
  protected readonly TCT_CHECK_STATUS_MAP = TCT_CHECK_STATUS_MAP;
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
  invoiceConfiguration: any;
  authorRELEASE = Authority.INVOICE_RELEASE;
  authorVIEW = Authority.INVOICE_VIEW;
  authorUPDATE = Authority.INVOICE_UPDATE;
  authorSendMail = Authority.INVOICE_SEND_MAIL;
  authorPrintShare = Authority.INVOICE_PRINT_SHARE;
  authorGetData = Authority.INVOICE_GET_DATA;
  authorCHANGE = Authority.INVOICE_CHANGE;

  plugin: any;
  option = { lang: 'en' };
  digiDocChrome = 'TokenSigning';
  certificate: any;
  constructor(
    private utilService: UtilsService,
    protected modalService: NgbModal,
    protected translateService: TranslateService,
    protected confirmDialog: ConfirmDialogService,
    private invoiceService: InvoiceService,
    protected activatedRoute: ActivatedRoute,
    protected toast: ToastrService,
    protected utilsService: UtilsService,
    public certificateService: CertificateService,
    public loading: LoadingOption,
    public router: Router
  ) {
    super();
  }

  async ngOnInit() {
    try {
      this.certificateService.tryConnectSigningTool();
    } catch (e) {}
    this.minDate = dayjs().subtract(4, 'years');
    this.invoiceSelect = {};
    this.filterInvoice = {};
    this.fromDate = null;
    this.toDate = null;
    this.lastCompany = await this.getCompany();
    this.invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    if (this.invoiceConfiguration) {
      this.invoiceConfiguration = this.invoiceConfiguration.data;
    }
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

  invoiceMap = new Map<number, any>();
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
        this.listInvoice.forEach(item => this.invoiceMap.set(item.id, item.arisingDate));
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

  openUpdate(invoice) {}

  deleteinvoicetomer(id) {}

  viewInvoice(invoice) {
    this.idEditing = this.idEditing == invoice.id ? -1 : invoice.id;
    this.invoiceService.find(invoice.id).subscribe(res => {
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

  editInvoice() {
    this.invoiceSelect.isEdit = !this.invoiceSelect.isEdit;
  }

  deleteInvoice() {
    this.invoiceService.deleteInvoice(this.invoiceSelect.id).subscribe(res => {
      if (res.status) {
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

  async openModelDeleteInvoice(invoice: any) {
    this.invoiceSelect = invoice;
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    this.checkModalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_INVOICE,
      ModalContent.DELETE_INVOICE,
      ModalBtn.DELETE_INVOICE,
      'trash-can',
      'btn-delete'
    );
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.deleteInvoice();
        this.checkModalRef.close();
      }
    });
  }

  updateInvoice() {}

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

  submitPublishList() {
    if (this.listSelected.length === 1) {
      this.openModalPublish(this.selectedItem);
    } else {
      this.invoiceService
        .publishInvoiceList({
          fromDate: this.utilService.convertDate(this.fromDate) ?? '',
          toDate: this.utilService.convertDate(this.toDate) ?? '',
          taxCheckStatus: this.type || this.type === 0 ? this.type : '',
          pattern: this.filterInvoice.pattern ? this.filterInvoice.pattern : '',
          no: this.filterInvoice.no ? this.filterInvoice.no : '',
          customerName: this.filterInvoice.customerName ? this.filterInvoice.customerName : '',
          listID: this.listSelected,
          paramCheckAll: this.paramCheckAll ? this.paramCheckAll : false,
        })
        .subscribe(res => {
          this.checkModalRef = this.modalService.open(ResponseModalComponent, { size: 'xl', backdrop: 'static' });

          this.checkModalRef.componentInstance.data = res.data;
        });
    }
  }

  openModalArisingDateConfirm(isPublishList: boolean) {
    this.checkModalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
    // Tạo đối tượng gửi sang model với các value
    this.checkModalRef.componentInstance.value = new DialogModal(
      ModalHeader.ARISING_DATE_CONFIRM,
      ModalContent.ARISING_DATE_CONFIRM,
      ModalBtn.AGREE,
      'check',
      'btn-save'
    );
    this.checkModalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.checkModalRef.close();
        isPublishList ? this.submitPublishList() : this.submitPublishItem();
      }
    });
  }

  openModalPublish(invoice: any) {
    this.invoiceSelect = invoice;
    if (this.checkArisingDate(invoice.arisingDate)) {
      this.openModalArisingDateConfirm(false);
    } else {
      this.submitPublishItem();
    }
  }

  submitPublishItem() {
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
        if (this.lastCompany.invoiceMethod === INVOICE_METHOD.TOKEN_MOI_TAO_LAP) {
          let count: number = 0;
          if (this.paramCheckAll) {
            count = this.totalItems - this.listSelected.length;
          } else {
            count = this.listSelected.length;
          }
          if (count > 20) {
            this.toast.error('Vui lòng chọn tối đa 20 hóa đơn để phát hành', 'Thông báo');
            return;
          }
          this.certificateService.loadPluginFor().then(response => {
            if (response) {
              this.certificate = response;
              this.signHashData(this.invoiceSelect.ikey);
            }
          });
        } else {
          // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
          this.publishInvoice();
          this.checkModalRef.close();
        }
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

  updateValue(event: number) {
    this.filterInvoice.keyword = event;
  }

  getMaxDate() {
    return { year: 2023, month: 5, day: 12 };
  }

  async publishListInvoice() {
    // kiểm tra điều kiện đẩy lên
    if (!this.paramCheckAll && (!this.listSelected || !this.listSelected.length || this.listSelected.length === 1)) {
      this.toast.error('Vui lòng chọn nhiều hóa đơn để phát hành', 'Thông báo');
      return;
    }
    if (this.lastCompany.invoiceMethod === INVOICE_METHOD.TOKEN_MOI_TAO_LAP) {
      let count: number = 0;
      if (this.paramCheckAll) {
        count = this.totalItems - this.listSelected.length;
      } else {
        count = this.listSelected.length;
      }
      if (count > 20) {
        this.toast.error('Vui lòng chọn tối đa 20 hóa đơn để phát hành', 'Thông báo');
        return;
      }
      this.certificateService.loadPluginFor().then(response => {
        if (response) {
          this.certificate = response;
          this.signHashData();
        }
      });
    } else {
      if (this.checkArisingDate()) {
        this.openModalArisingDateConfirm(true);
      } else {
        this.submitPublishList();
      }
    }
  }

  private checkArisingDate(arisingDate?: string) {
    const dateNow = dayjs().startOf('day');
    let checkArisingDate: boolean = false;
    if (arisingDate) {
      const arisingDateConvert = dayjs(arisingDate, 'DD/MM/YYYY');
      if (arisingDateConvert.isBefore(dateNow)) {
        checkArisingDate = true;
      }
    } else {
      for (const item of this.listSelected) {
        if (this.invoiceMap.has(item)) {
          const arisingDateConvert = dayjs(this.invoiceMap.get(item), 'DD/MM/YYYY');
          if (arisingDateConvert.isBefore(dateNow)) {
            checkArisingDate = true;
            break;
          }
        }
      }
    }

    return checkArisingDate;
  }

  onSearchPage() {
    this.onSearch();
  }

  getItem(invoice: any) {
    this.selectedItem = invoice;
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
  hasExtensionFor = cls => {
    // @ts-ignore
    if (typeof top.window[cls] === 'function') {
      return true;
    }
    return typeof window[cls] === 'function';
  };
  signHashData(ikey?: string) {
    if (this.certificate) {
      const rq: any = {};
      rq.CertString = this.certificate.base64Cert;
      rq.fromDate = this.utilService.convertDate(this.fromDate) ?? '';
      rq.toDate = this.utilService.convertDate(this.toDate) ?? '';
      rq.taxCheckStatus = this.type || this.type === 0 ? this.type : '';
      rq.pattern = this.filterInvoice.pattern ? this.filterInvoice.pattern : '';
      rq.no = this.filterInvoice.no ? this.filterInvoice.no : '';
      rq.customerName = this.filterInvoice.customerName ? this.filterInvoice.customerName : '';
      rq.listID = this.listSelected;
      rq.ikey = ikey ? ikey : '';
      rq.paramCheckAll = this.paramCheckAll ? this.paramCheckAll : false;
      this.invoiceService.getDigestData(rq).subscribe(res => {
        let data = res.body.data.Data.DigestDataDTO;
        let hashData = res.body.data.Data.DigestDataDTO.map(n => n.hashData);
        this.certificateService
          .signHashData(
            this.certificate.base64Cert,
            {
              type: 'xmlwithcert',
              hex: hashData,
            },
            this.option
          )
          .then(
            response => {
              //   Phast hanh hoa don
              const requestSign = {
                CertString: this.certificate.base64Cert,
                signatureDTO: res.body.data.Data.DigestDataDTO,
              };
              if (data.length > 0) {
                for (let i = 0; i < data.length; i++) {
                  data[i].sigData = (response?.value.signature as string[])[i];
                  if (i === data.length - 1) {
                    const rq: any = {};
                    rq.signatureDTO = data;
                  }
                }
                this.invoiceService.publishInvoiceWithCert(requestSign).subscribe((req: any) => {
                  this.toast.success('Phát hành hóa đơn thành công');
                });
              }
            },
            error => {
              console.log(error);
            }
          );
      });
    } else {
      // console.log(new Error(this.INVALID_ARGUMENT));
    }
  }

  opendPopupGuide() {
    const modalRef = this.modalService.open(SignaturesPopupComponent);
    modalRef.result.then(
      result => {},
      reason => {}
    );
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
}
