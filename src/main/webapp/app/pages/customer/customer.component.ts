import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { Customer } from '../../entities/customer/customer';
import { CustomerService } from './customer.service';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CustomerSaveComponent } from './customer-save/customer-save.component';
import { ToastrService } from 'ngx-toastr';
import { BaseComponent } from '../../shared/base/base.component';
import { LoadingOption } from 'app/utils/loadingOption';
import { ModalBtn, ModalContent, ModalHeader } from '../../constants/modal.const';
import { ConfirmDialogComponent } from '../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../order/model/dialogModal.model';
import { ImportCustomerComponent } from '../import/excel/customer/import-customer.component';
import { CustomerCardModalComponent } from './customer-card/modal/card-save/customer-card-modal.component';
import { UtilsService } from '../../utils/Utils.service';
import { ExportCommon } from '../export/export.common';
import { EXPORT_EXCEL_CUSTOMER_NAME } from '../../constants/file-name.constants';
import { CustomerBillHistory, CustomerCardHistory, ReceivableBillHistory } from '../../entities/customer/customer-history';
import { ConvertResponse } from '../../config/convert-response';
import { CustomerCardHistoryComponent } from './customer-card/modal/card-history/customer-card-history.component';
import { ModalDetailDeleteMultiComponent } from '../product/modal-detail-delete-multi/modal-detail-delete-multi.component';
import { formatDate } from '@angular/common';
import {
  ICON_CANCEL,
  ICON_CANCEL_WHITE,
  ICON_CONVERT_POINT,
  ICON_IMPORT_EXCEL,
  ICON_MINUS_POINT,
  ICON_PLUS_POINT,
  ICON_RECHARGE,
} from '../../shared/other/icon';
import { STATUS_MAP } from '../../constants/invoice.constants';

const FILTER_PAG_REGEX = /\D/g;

@Component({
  selector: 'jhi-customer',
  templateUrl: './customer.component.html',
  styleUrls: ['./customer.component.scss'],
})
export class CustomerComponent extends BaseComponent implements OnInit {
  @ViewChild('delCus') delCus: ElementRef;

  typeCustomer: number = 0;
  keyword: string = '';
  cusArray: Customer[] = [];
  page: number = 1;
  pageSize: number = 20;
  tmp: any;
  idEditing: number = -1;
  company: any;
  companyId: number;
  totalSize: number = 0;
  idDel: number = -1;
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
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  listSelected: any = [];
  selectedItem: any = {};

  constructor(
    private customerService: CustomerService,
    protected utilsService: UtilsService,
    private modalService: NgbModal,
    private toastr: ToastrService,
    private elementRef: ElementRef,
    public exportComponent: ExportCommon,
    public loading: LoadingOption
  ) {
    super();
  }

  getCustomers(): void {
    this.cusArray = [];
    this.customerService.getCustomers(this.typeCustomer, this.page, this.pageSize, this.keyword).subscribe(cus => {
      this.cusArray = cus.data;
      this.totalSize = cus.count;
      if (this.cusArray.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
        this.listSelected = [];
      } else if (this.paramCheckAll) {
        this.cusArray.forEach(n => {
          n.check = !this.listSelected.some(m => m === n.id);
        });
      } else {
        // this.listSelected = [];
        this.cusArray.forEach(n => {
          n.check = this.listSelected.includes(n.id);
        });
      }
    });
    // this.cusArray = customerMockup;
  }

  openCreate(cus?: Customer) {
    if (this.modalRef) {
      this.modalRef.close();
    }

    this.modalRef = this.modalService.open(CustomerSaveComponent, { size: 'lg', backdrop: 'static' });
    if (cus) {
      this.idEditing = -1;
      for (let key in cus) {
        if (cus[key] !== null) {
          this.modalRef.componentInstance.cus[key] = cus[key];
        }
      }
    }

    this.modalRef.result.then(
      result => {
        if (result) this.getCustomers();
      },
      reason => {
        // on dismiss
      }
    );
  }

  openDeleteCustomer() {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.value = new DialogModal(
      ModalHeader.DELETE_CUSTOMER,
      ModalContent.DELETE_CUSTOMER,
      ModalBtn.DELETE_CUSTOMER,
      'trash-can',
      'btn-delete'
    );
    this.modalRef.componentInstance.formSubmit.subscribe(res => {
      if (res) {
        this.deleteCustomer();
        if (this.modalRef) this.modalRef.close();
      }
    });
    this.modalRef.result.then(
      result => {},
      reason => {}
    );
  }

  deleteCustomer() {
    this.idEditing = -1;
    //gọi api
    this.customerService.delCustomer(this.idDel).subscribe(response => {
      if (response.status) {
        this.toastr.success(response.message[0].message);
        this.getCustomers();
        this.modalRef?.close();
      }
    });
  }

  changeType(type) {
    this.typeCustomer = type;
    this.page = 1;
    // this.totalSize thay đổi lại số lượng tổng sản phẩm
    this.getCustomers();
  }

  search() {
    this.getCustomers();
  }

  async ngOnInit() {
    this.company = await this.getCompany();
    this.companyId = this.company.id;
    this.getCustomers();
  }
  getColspan(): number {
    return window.innerWidth >= 961 ? 7 : 6;
  }

  scrollToTargets(): void {
    this.indexCustomerInfo = 0;
    setTimeout(() => {
      if (this.idEditing == -1) return;
      const element = document.getElementsByClassName('editing-head')[0];
      const elementBody = document.getElementsByClassName('editing-body')[0];
      const rect = element.getBoundingClientRect();
      const rect2 = elementBody.getBoundingClientRect();
      if (rect2.bottom > window.innerHeight || rect.top < 100)
        window.scrollBy({
          top: rect.top - 100,
          behavior: 'smooth',
        });
    }, 100);
  }

  onDeleteKeywordSearch() {
    if (!this.keyword) {
      this.getCustomers();
    }
  }

  onImportExcel() {
    this.modalRef = this.modalService.open(ImportCustomerComponent, { size: 'xl', backdrop: 'static' });
  }

  onExportExcel() {
    // if (this.modalRef)
    //   this.modalRef.close();
    // this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'xl', backdrop: 'static' });
    // this.modalRef.componentInstance.value = new DialogModal(
    //   ModalHeader.EXPORT_CUSTOMER,
    //   ModalContent.EXPORT_CUSTOMER,
    //   ModalBtn.EXPORT,
    //   '',
    //   'btn-success'
    // );
    this.customerService
      .exportExcelCustomer({
        comId: this.companyId,
        paramCheckAll: this.paramCheckAll,
        ids: this.listSelected,
        type: this.typeCustomer,
        keyword: this.keyword,
      })
      .subscribe(
        response => {
          this.exportComponent.saveExcelFromByteArray(response.body, EXPORT_EXCEL_CUSTOMER_NAME);
          this.toastr.success('Yêu cầu xuất file Excel thành công');
          this.paramCheckAllPage = false;
          this.paramCheckAll = false;
          this.listSelected = [];
          this.getCustomers();
        },
        e => {
          this.toastr.error('Có lỗi xảy ra lúc yêu cầu xuất excel khách hàng/nhà cung cấp!!');
        }
      );
  }

  deleteMultiStaff() {
    if (this.paramCheckAll || this.listSelected?.length > 0) {
      this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
      this.modalRef.componentInstance.value = new DialogModal(
        ModalHeader.DELETE_CUSTOMER,
        ModalContent.DELETE_CUSTOMER,
        ModalBtn.DELETE_CUSTOMER,
        'trash-can',
        'btn-delete'
      );
      this.modalRef.componentInstance.formSubmit.subscribe(res => {
        if (res) {
          // Sau khi bấm nút đồng ý thì thực hiện công việc và đóng model
          this.customerService
            .delMultiStaff({
              comId: this.companyId,
              paramCheckAll: this.paramCheckAll,
              ids: this.listSelected,
            })
            .subscribe(response => {
              if (response.status) {
                this.toastr.success(response.message[0].message);
                this.modalRef = this.modalService.open(ModalDetailDeleteMultiComponent, {
                  size: 'lg',
                  backdrop: 'static',
                });
                this.modalRef.componentInstance.countAll = response.data.countAll;
                this.modalRef.componentInstance.countSuccess = response.data.countSuccess;
                this.modalRef.componentInstance.countFalse = response.data.countFalse;
                this.modalRef.componentInstance.data = response.data.dataFalse;
                this.modalRef.closed.subscribe((res?: any) => {
                  this.paramCheckAllPage = false;
                  this.paramCheckAll = false;
                  this.listSelected = [];
                  this.getCustomers();
                });
              }
            });
          if (this.modalRef) this.modalRef.close();
        }
      });
    }
  }
  getItem(staff: any) {
    this.selectedItem = staff;
  }

  loadMore() {
    if (this.paramCheckAllPage && !this.paramCheckAll) {
      this.paramCheckAllPage = false;
      // this.listSelected = [];
    }
    this.getCustomers();
  }

  typeName = '';
  customerIds: number[] = [];
  onUpdateCustomerCard(type: number, id: number, redeemValue: any, accumValue: number, cardName: string) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    this.customerIds = [];
    this.customerIds.push(id);

    this.modalRef = this.modalService.open(CustomerCardModalComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.request = {
      type: type,
      customerIds: this.customerIds,
      redeemValue: redeemValue,
      accumValue: accumValue ? accumValue : 0,
      cardName: cardName,
    };
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        this.getCustomers();
      }
    });
  }

  visibleCardAction = false;
  idCustomerCardAction: number;
  viewCardAction() {
    this.visibleCardAction = !this.visibleCardAction;
  }

  indexCustomerInfo = 0;
  customerCardHistory: CustomerCardHistory[] = [];
  customerBillHistory: CustomerBillHistory[] = [];
  receivableBillHistory: ReceivableBillHistory[] = [];
  countCustomerCardHistory = 0;
  countCustomerBillHistory = 0;
  countReceivableBillHistory = 0;

  onCustomerDetail(index: number, customerId: number) {
    this.indexCustomerInfo = index;
    if (index < 3) {
      this.customerBillHistory = [];
      this.countCustomerBillHistory = 0;
      this.getAllCustomerCardHistory(index, customerId);
    } else {
      this.customerCardHistory = [];
      this.countCustomerCardHistory = 0;
      this.getAllCustomerBillHistory(index, customerId);
    }
  }

  private getAllCustomerBillHistory(index: number, customerId: number) {
    this.customerBillHistory = [];
    this.receivableBillHistory = [];
    this.countCustomerBillHistory = 0;
    this.countReceivableBillHistory = 0;
    const type = index === 3 ? 1 : 2;
    const request = {
      comId: this.companyId,
      customerId: customerId,
      type: type,
      page: 0,
      size: 10,
    };

    this.customerService.getAllCustomerBillHistory(request).subscribe(
      response => {
        if (response.ok) {
          const responseConvert = ConvertResponse.getDataFromServer(response, false);
          if (type === 2) {
            this.customerBillHistory = responseConvert.data;
            this.countCustomerBillHistory = responseConvert.count;
          } else {
            this.receivableBillHistory = responseConvert.data;
            this.countReceivableBillHistory = responseConvert.count;
          }
          this.checkValidData();
        }
      },
      error => {
        this.receivableBillHistory = [];
        this.customerBillHistory = [];
        this.countReceivableBillHistory = 0;
        this.countCustomerBillHistory = 0;
      }
    );
  }

  private getAllCustomerCardHistory(index: number, customerId: number) {
    this.customerCardHistory = [];
    this.countCustomerCardHistory = 0;
    const request = {
      comId: this.companyId,
      customerId: customerId,
      getWithPaging: false,
      type: index,
    };

    this.customerService.getAllCustomerCardHistory(request).subscribe(
      response => {
        if (response.ok) {
          const responseConvert = ConvertResponse.getDataFromServer(response, false);
          this.customerCardHistory = responseConvert.data;
          this.countCustomerCardHistory = responseConvert.count;
          this.checkValidData();
        }
      },
      error => {
        this.customerCardHistory = [];
        this.countCustomerCardHistory = 0;
      }
    );
  }

  viewMoreHistory(customerId: any) {
    const data = {
      comId: this.companyId,
      customerId: customerId,
      type: this.indexCustomerInfo,
    };
    this.modalRef = this.modalService.open(CustomerCardHistoryComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.data = data;
  }

  compactDate(date, pattern) {
    if (date) {
      const myDate = new Date(date);
      return formatDate(myDate, pattern, 'en-US');
    }
    return date;
  }

  haveData = true;
  haveMoreData = false;
  checkValidData() {
    switch (this.indexCustomerInfo) {
      case 0:
      case 1:
      case 2: {
        if (!this.customerCardHistory || this.customerCardHistory.length < 1) {
          this.haveData = false;
        }
        if (this.countCustomerCardHistory > 10) {
          this.haveMoreData = true;
        }
        break;
      }
      case 3: {
        if (!this.receivableBillHistory || this.receivableBillHistory.length < 1) {
          this.haveData = false;
        }
        if (this.countReceivableBillHistory > 10) {
          this.haveMoreData = true;
        }
        break;
      }
      case 4: {
        if (!this.customerBillHistory || this.customerBillHistory.length == 0) {
          this.haveData = false;
        }
        if (this.countCustomerBillHistory > 10) {
          this.haveMoreData = true;
        }
        break;
      }
    }
  }

  protected readonly ICON_IMPORT_EXCEL = ICON_IMPORT_EXCEL;
  protected readonly ICON_RECHARGE = ICON_RECHARGE;
  protected readonly ICON_PLUS_POINT = ICON_PLUS_POINT;
  protected readonly ICON_MINUS_POINT = ICON_MINUS_POINT;
  protected readonly ICON_CONVERT_POINT = ICON_CONVERT_POINT;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
  protected readonly STATUS_MAP = STATUS_MAP;
}
