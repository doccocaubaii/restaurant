import { Component, OnDestroy, OnInit } from '@angular/core';
import { SearchProductSaleReq } from './inventory-stats';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { UtilsService } from '../../../utils/Utils.service';
import { BaseComponent } from '../../../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { InventoryStatsService } from './inventory-stats.service';
import { ExportCommon } from '../../export/export.common';
import { ProductService } from '../../product/product.service';
import { FilterProduct, Page } from '../../product/product';
import { debounceTime, distinctUntilChanged, Subject, switchMap, tap } from 'rxjs';
import appSettings from '../../../config/app-settings';
import { ContentOption } from '../../../utils/contentOption';
import { PreviewInventoryStatsComponent } from './preview/preview-inventory-stats.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { DATE_FORMAT_DMY, DATE_FORMAT_DMY2 } from '../../../config/input.constants';
import { ICON_CANCEL, ICON_EXCEL, ICON_PDF, ICON_RESET, ICON_SELECT_PARAM } from '../../../shared/other/icon';
@Component({
  selector: 'jhi-product-sales-stats',
  templateUrl: './inventory-stats.component.html',
  styleUrls: ['./inventory-stats.component.scss'],
})
export class InventoryStatsComponent extends BaseComponent implements OnDestroy, OnInit {
  sizes = [10, 20, 30];
  totalItems: any;
  size = 20;
  page = 1;
  groupId = null;
  keyword = '';
  selectedItem: any = {};
  allCategories: any = [];
  isSavingPdf = false;
  isSavingExcel = false;
  filterCategory: FilterProduct = { page: 0, size: Page.PAGE_SIZE };
  lastCompany: any = {};
  listData: any = [];
  fromDate: any;
  toDate: any;
  totalData: any;
  totalValue: any;
  totalOnHand: any;
  keywordCategory$ = new Subject<string>();
  statusSearch = false;
  searchReq = {
    comId: null,
    keyword: '',
    page: 0,
    size: 20,
    paramCheckAll: false,
    ids: [],
    fromDate: '2023-07-01',
    toDate: '2023-07-31',
  };

  appSettings = appSettings;
  private modalRef: NgbModalRef;
  constructor(
    private router: Router,
    private toastr: ToastrService,
    private translateService: TranslateService,
    protected utilsService: UtilsService,
    protected service: InventoryStatsService,
    private productService: ProductService,
    public exportComponent: ExportCommon,
    private contentOption: ContentOption,
    private modalService: NgbModal
  ) {
    super();
    // this.contentOption.isHiddenOrder = true;
  }

  getCategory() {
    this.productService.getProductCategory(this.filterCategory).subscribe(value => {
      this.allCategories = value.body.data;
    });
  }

  searchCategoryByKey() {
    this.keywordCategory$
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.filterCategory.keyword = keyword;
            this.filterCategory.page = 0;
          } else {
            this.filterCategory.keyword = '';
          }
        }),
        switchMap(() => this.productService.getProductCategory(this.filterCategory))
      )
      .subscribe(res => {
        this.allCategories = res.body.data;
      });
  }

  loadMoreCategory() {
    this.filterCategory.page += 1;
    this.getCategory();
  }

  onDeleteKeywordSearch() {
    if (!this.searchReq.keyword) {
      this.getData();
    }
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.searchReq.comId = this.lastCompany.id;
    if (!this.service.fromDate) {
      this.onChooseAgain();
    }
    this.getData();
    this.getCategory();
    this.searchCategoryByKey();
  }

  changeStatusSearch() {
    this.statusSearch = !this.statusSearch;
  }

  getData() {
    this.searchReq.page = this.page - 1;
    this.searchReq.size = this.size;
    this.searchReq.fromDate = this.utilsService.convertDate(this.service.fromDate) ?? '';
    this.searchReq.toDate = this.utilsService.convertDate(this.service.toDate) ?? '';
    this.searchReq.paramCheckAll = this.service.paramCheckAll;
    // @ts-ignore
    this.searchReq.ids = this.service.ids;
    if (this.searchReq.fromDate && this.searchReq.toDate) {
      this.service.getInventoryStats(this.searchReq).subscribe(value => {
        this.listData = value.body.data.detail;
        this.totalData = value.body.data;
        this.totalItems = value.body.count;
        this.totalOnHand = value.body.data.totalOnHand;
        this.totalValue = value.body.data.totalValue;
        this.fromDate = this.service.fromDate.format(DATE_FORMAT_DMY2);
        this.toDate = this.service.toDate.format(DATE_FORMAT_DMY2);
      });
    }
  }

  onExportPdf() {
    if (this.isSavingPdf) {
      this.isSavingPdf = false;
      return;
    }
    if (this.listData?.length > 0) {
      this.isSavingPdf = true;
      this.service.exportPdf(this.searchReq).subscribe(
        value => {
          // this.savePdfFromByteArray(value.body, 'Thong_Ke_Ton_Kho');
          this.openPdfInNewTab(value.body);
          this.isSavingPdf = false;
        },
        error => {
          this.isSavingPdf = false;
        }
      );
    }
  }

  resetData() {
    this.page = 0;
    this.size = 20;
    this.searchReq.keyword = '';
    this.getData();
  }

  onChooseAgain() {
    this.modalRef = this.modalService.open(PreviewInventoryStatsComponent, {
      size: 'lg',
      backdrop: 'static',
    });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 'Preview') {
        this.getData();
      }
    });
  }

  onExportExcel() {
    if (this.isSavingExcel) {
      this.isSavingExcel = false;
      return;
    }
    if (this.listData?.length > 0) {
      this.isSavingExcel = true;
      this.service.exportExcel(this.searchReq).subscribe(
        value => {
          this.exportComponent.saveExcelFromByteArray(value.body, 'Thong_Ke_Ton_Kho');
          this.isSavingExcel = false;
        },
        error => {
          this.isSavingExcel = false;
        }
      );
    }
  }

  savePdfFromByteArray(byteArray: any, fileName: string): void {
    const blob = new Blob([byteArray], { type: 'application/pdf' }); // Thay đổi thành application/pdf
    const fileURL = URL.createObjectURL(blob);
    const link = document.createElement('a');
    document.body.appendChild(link);
    link.download = fileURL;
    link.setAttribute('style', 'display: none');
    link.setAttribute('download', fileName + '.pdf'); // Thay đổi đuôi file sang .pdf
    link.href = fileURL;
    link.click();
  }

  openPdfInNewTab(byteArray: any): void {
    const blob = new Blob([byteArray], { type: 'application/pdf' });
    const fileURL = URL.createObjectURL(blob);
    window.open(fileURL, '_blank');
  }

  pageChange($event: any) {
    this.page = $event;
    this.getData();
  }

  protected readonly ICON_EXCEL = ICON_EXCEL;
  protected readonly ICON_PDF = ICON_PDF;
  protected readonly ICON_RESET = ICON_RESET;
  protected readonly ICON_SELECT_PARAM = ICON_SELECT_PARAM;
}
