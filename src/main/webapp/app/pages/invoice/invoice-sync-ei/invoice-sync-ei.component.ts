import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { UtilsService } from '../../../utils/Utils.service';
import dayjs from 'dayjs/esm';
import { InvoiceService } from '../service/invoice.service';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceSyncRequest, InvoiceSyncResponse, InvoiceSyncResponseItem, InvoiceSyncSaveDataResponse } from './model/InvoiceSyncModel';
import { ToastrService } from 'ngx-toastr';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { ALL_STATUS, ALL_TYPE, STATUS_MAP, TCT_CHECK_STATUS_MAP } from '../../../constants/invoice.constants';

@Component({
  selector: 'jhi-invoice-sync-ei',
  templateUrl: './invoice-sync-ei.component.html',
  styleUrls: ['./invoice-sync-ei.component.scss'],
})
export class InvoiceSyncEIComponent extends BaseComponent implements OnInit {
  minDate: dayjs.Dayjs | any;
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  maxToDate: dayjs.Dayjs | any;
  filterInvoice = {
    pattern: '',
    results: [
      {
        id: 0,
        name: 'Tất cả',
      },
      {
        id: 1,
        name: 'Hợp lệ',
      },
      {
        id: -2,
        name: 'Không hợp lệ',
      },
      {
        id: -1,
        name: 'Đang kiểm tra',
      },
    ],
    statusList: [
      {
        id: -1,
        name: 'Tất cả',
      },
      {
        id: 1,
        name: 'Hóa đơn có chữ ký số',
      },
      {
        id: 3,
        name: 'Hóa đơn bị thay thế',
      },
      {
        id: 4,
        name: 'Hóa đơn bị điều chỉnh',
      },
      {
        id: 5,
        name: 'Hóa đơn bị hủy',
      },
    ],
    result: 0,
    status: -1,
  };
  patterns: any[] = [];
  listSelected: any[] = [];
  listUnSelected: number[] = [];
  selectedItem: any = {};
  lastCompany: any = {};
  totalItems: any = 0;
  filterData = { page: 0, size: 50 };
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  invoices: InvoiceSyncResponse;
  isDisableSearch = false;
  syncSaveDataResponse: InvoiceSyncSaveDataResponse = {
    dataErrors: [],
    countFail: 0,
    countSuccess: 0,
    countTotal: 0,
  };
  viewType = 0;
  dataView: InvoiceSyncResponseItem[] = [];
  isAction = false;
  dataValid: InvoiceSyncResponseItem[] = [];

  constructor(
    protected utilsService: UtilsService,
    private invoiceService: InvoiceService,
    private toast: ToastrService,
    private loadingBar: LoadingBarService,
    private activeModal: NgbActiveModal
  ) {
    super();
  }

  async ngOnInit() {
    this.minDate = dayjs().subtract(4, 'years');
    this.lastCompany = await this.getCompany();
    this.onReset();
  }

  dismiss($event: MouseEvent) {
    this.activeModal.close(true);
  }

  async onReset() {
    this.fromDate = dayjs().subtract(30, 'day');
    this.toDate = dayjs();
    this.maxToDate = dayjs();
    await this.invoiceService.getOwnerInfo(this.lastCompany.id).subscribe(res => {
      this.patterns = res.data.patterns;
      if (this.patterns && this.patterns.length > 0) {
        this.filterInvoice.pattern = this.patterns[0].pattern;
      } else {
        this.filterInvoice.pattern = '';
      }
    });
    this.filterInvoice.status = -1;
    this.filterInvoice.result = 0;
  }

  onSearch() {
    if (!this.filterInvoice.pattern) {
      this.toast.error('Không để trống mẫu số hoá đơn');
      return;
    }

    this.listSelected = [];
    this.selectedItem = {};
    this.loadingBar.start();
    this.isDisableSearch = true;

    const request: InvoiceSyncRequest = this.buildInvoiceSyncRequest();

    this.invoiceService.syncInvoiceEI(request).subscribe(
      res => {
        this.handleSyncSuccess(res);
        this.isAction = true;
      },
      error => {
        this.isAction = true;
        this.handleSyncError(error);
      }
    );
  }

  private buildInvoiceSyncRequest(): InvoiceSyncRequest {
    return {
      page: this.filterData.page,
      pagesize: this.filterData.size,
      fromDate: this.utilsService.formatDate(this.fromDate, '/', false),
      toDate: this.utilsService.formatDate(this.toDate, '/', false),
      pattern: this.filterInvoice.pattern,
      listStatus: this.filterInvoice.status === -1 || !this.filterInvoice.status ? ALL_STATUS : [],
      status: this.filterInvoice.status === -1 ? null : this.filterInvoice.status,
      tctCheckStatus: !this.filterInvoice.result ? null : this.filterInvoice.result,
    };
  }

  private handleSyncSuccess(res: any): void {
    this.loadingBar.complete();
    this.isDisableSearch = false;
    this.invoices = res.data;
    this.invoices.data.forEach(item => (item.id = item.Id));
    this.dataView = this.invoices.data;
    this.totalItems = this.invoices.totalRecords;
  }

  private handleSyncError(error: any): void {
    this.loadingBar.complete();
    this.isDisableSearch = false;
    console.log(error);
  }

  loadMore($event: any) {
    this.filterData.page = $event;
    this.onSearch();
  }

  onFromDateChange() {
    if (this.toDate.diff(this.fromDate, 'day') > 30) {
      this.toDate = this.fromDate.add(30, 'day');
      this.maxToDate = this.toDate;
    }
  }

  onToDateChange() {
    if (this.toDate.diff(this.fromDate, 'day') > 30) {
      this.fromDate = this.toDate.subtract(30, 'day');
    }
  }

  selectItem(item: InvoiceSyncResponseItem): void {
    if (this.viewType === 0 && !item.isExists) {
      if (this.listUnSelected.includes(item.id)) {
        this.listUnSelected.splice(this.listUnSelected.indexOf(item.id), 1);
      } else {
        this.listUnSelected.push(item.id);
      }
      this.selectedItem = item;
      this.utilsService.check(item, this.listSelected, this.paramCheckAll, this.selectedItem);
    }
  }

  protected readonly STATUS_MAP = STATUS_MAP;
  protected readonly TCT_CHECK_STATUS_MAP = TCT_CHECK_STATUS_MAP;

  syncSaveData(item: any) {
    let data: any[] = [];
    if (!item && !this.paramCheckAllPage) {
      this.invoices.data.forEach(item => {
        if (this.listSelected.includes(item.id)) {
          data.push(item);
        }
      });
    } else if (item) {
      data = [item];
    }

    const req = {
      comId: this.lastCompany.id,
      checkAll: this.paramCheckAllPage,
      data: data,
      requestGetData: !this.paramCheckAllPage ? null : this.buildInvoiceSyncRequest(),
      ignoreIds: !this.paramCheckAllPage ? [] : this.listUnSelected,
      totalPage: Math.ceil(this.totalItems / this.filterData.size),
    };
    this.loadingBar.start();
    this.isDisableSearch = true;
    this.invoiceService.saveSyncInvoiceEI(req).subscribe(
      res => {
        if (res) {
          this.syncSaveDataResponse = res.data;
          this.viewType = 1;
          this.dataView = this.syncSaveDataResponse.dataErrors;
          this.loadingBar.complete();
          this.isDisableSearch = false;
        }
        this.loadingBar.complete();
        this.isDisableSearch = false;
      },
      error => {
        this.isDisableSearch = false;
        this.loadingBar.complete();
      }
    );
  }

  getColorTctStatus(taxCheckStatus: any) {
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

  // addAllDataValid(paramCheckAllPage: boolean, data: InvoiceSyncResponseItem[], listSelected: any) {
  //   if (paramCheckAllPage) {
  //     if (data && data.length > 0) {
  //       for (const item of data) {
  //         if(!item.isExists) {
  //           this.dataValid.push(item);
  //         }
  //       }
  //     }
  //   } else {
  //     this.dataValid = [];
  //   }
  //   this.utilsService.checkAllForPage(this.dataValid, listSelected, paramCheckAllPage)
  // }
}
