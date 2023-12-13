import { Component, OnDestroy, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { debounceTime, distinctUntilChanged, Subject, switchMap, tap } from 'rxjs';
import { UtilsService } from '../../../../utils/Utils.service';
import { ProductService } from '../../../product/product.service';
import { InventoryStatsService } from '../inventory-stats.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import dayjs from 'dayjs/esm';
import { HOME, REPORT_INVENTORY_STATS } from '../../../../constants/app.routing.constants';
import {ICON_CANCEL, ICON_SAVE, ICON_SEARCH_SM} from "../../../../shared/other/icon";
@Component({
  selector: 'jhi-product-sales-stats',
  templateUrl: './preview-inventory-stats.component.html',
  styleUrls: ['./preview-inventory-stats.component.scss'],
})
export class PreviewInventoryStatsComponent implements OnInit {
  sizes = [10, 20, 30];
  totalItems: any;
  size = 20;
  page = 1;
  keyword = '';
  lastCompany: any = {};
  listData: any = [];
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  paramCheckAll: boolean = false;
  paramCheckAllPage: any = false;
  listSelected: any[] = [];
  selectedItem: any;
  latestUrl: string;
  searchReq = {
    code2: '',
    name: '',
    page: 0,
    size: 20,
  };
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    protected utilsService: UtilsService,
    private service: InventoryStatsService,
    public activeModal: NgbActiveModal
  ) {}

  async ngOnInit() {
    this.fromDate = dayjs();
    this.toDate = dayjs();
    if (this.service.ids) {
      this.listSelected = this.service.ids;
    }
    if (this.service.paramCheckAllPage) {
      this.paramCheckAllPage = this.service.paramCheckAllPage;
    }
    if (this.service.paramCheckAll) {
      this.paramCheckAll = this.service.paramCheckAll;
    }
    if (this.service.fromDate) {
      this.fromDate = this.service.fromDate;
    }
    if (this.service.toDate) {
      this.toDate = this.service.toDate;
    }
    await this.getData();
    const pathname = location.pathname;
    this.latestUrl = pathname.slice(1, pathname.length);
  }

  async onSizeChange() {
    this.searchReq.size = this.size;
    await this.getData();
  }

  async getData() {
    this.service.getPreviewInventoryStats(this.searchReq).subscribe(res => {
      if (this.paramCheckAll && this.totalItems < res.body.count) {
        this.listData.forEach(item => {
          this.listSelected.push(item.id);
          this.paramCheckAll = false;
        });
      }
      this.listData = res.body.data;
      this.totalItems = res.body.count;
      if (this.listData.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
      } else {
        if (this.paramCheckAll) {
          this.listData.forEach(n => {
            n.check = !this.listSelected.includes(n.id);
          });
        } else {
          if (this.listSelected?.length > 0) {
            this.paramCheckAllPage = true;
            this.listData.forEach(n => {
              if (!this.listSelected.includes(n.id)) {
                this.paramCheckAllPage = false;
              }
              n.check = this.listSelected.includes(n.id);
            });
          }
        }
      }
    });
  }

  checkAllPage() {
    this.listSelected = [];
  }

  checkItem(data: any) {
    if (!this.paramCheckAllPage) {
      if (data.check) {
        this.listSelected.push(data.id);
      } else if (this.selectedItem && this.selectedItem.length > 0) {
        this.selectedItem = this.selectedItem.filter(r => r !== data.id);
      }
    } else {
      if (!data.check) {
        this.listSelected.push(data.id);
      } else if (this.selectedItem && this.selectedItem.length > 0) {
        this.selectedItem = this.selectedItem.filter(r => r !== data.id);
      }
    }
  }

  dismiss(value: any) {
    this.activeModal.close(value);
    if (!this.service.fromDate) {
      this.router.navigate([HOME]);
    }
  }

  async loadMore($event: any) {
    // if (this.paramCheckAllPage && !this.paramCheckAll) {
    //   this.paramCheckAllPage = false;
    //   this.listSelected = [];
    // }
    this.page = $event;
    this.searchReq.page = this.page - 1;
    await this.getData();
  }

  getLink(path: string, pathOffline?: string) {
    if (!this.paramCheckAllPage && (!this.listSelected || this.listSelected.length == 0)) {
      this.toastr.error('Vui lòng chọn sản phẩm', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    if (!this.fromDate || !this.toDate || !dayjs.isDayjs(this.fromDate) || !dayjs.isDayjs(this.toDate)) {
      this.toastr.error('Ngày không đúng định dạng', this.translateService.instant('easyPos.product.info.message'));
      return;
    }
    this.service.fromDate = this.fromDate;
    this.service.toDate = this.toDate;
    this.service.paramCheckAll = this.paramCheckAll;
    this.service.paramCheckAllPage = this.paramCheckAllPage;
    this.service.ids = this.listSelected;
    this.dismiss('Preview');
    if (this.utilsService.isOnline()) {
      this.router.navigate([path]);
      this.latestUrl = path;
    } else if (pathOffline) {
      this.router.navigate([pathOffline]);
      this.latestUrl = pathOffline;
    }
  }

  changeFromDate(event) {
    const form = 'DD/MM/YYYY';
    if (event.length === form.length) {
      this.fromDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.fromDate)) {
      this.changeDate();
    }
  }

  changeToDate(event) {
    const form = 'DD/MM/YYYY';
    if (event.length === form.length) {
      this.toDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.toDate)) {
      this.changeDate();
    }
  }

  changeDate() {
    if (!this.fromDate || !this.toDate) return;
    const fromMonth = this.fromDate.month() + 1;
    const toMonth = this.toDate.month() + 1;
    const fromQuarter = Math.ceil(fromMonth / 3);
    const toQuarter = Math.ceil(toMonth / 3);
    const fromYear = this.fromDate.year();
    const toYear = this.toDate.year();
    return;
  }
  convertStringToDate(str: string): dayjs.Dayjs {
    const date = dayjs(str, 'DD/MM/YYYY');
    return date;
  }

  protected readonly REPORT_INVENTORY_STATS = REPORT_INVENTORY_STATS;
    protected readonly ICON_SAVE = ICON_SAVE;
    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_SEARCH_SM = ICON_SEARCH_SM;
}
