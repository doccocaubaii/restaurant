import { Component, ElementRef, ViewChild, OnDestroy, OnInit } from '@angular/core';
import appSettings from 'app/config/app-settings';
import { ProductService } from '../product.service';
import { debounceTime, distinctUntilChanged, Observable, Subject, switchMap, tap } from 'rxjs';
import { CreateCategotyReq, FilterProduct, Page, ProductObjs } from '../product';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateProductComponent } from '../modal-create-product/modal-create-product.component';
import { ImportProductComponent } from '../../import/excel/product/import-product.component';
import { UtilsService } from '../../../utils/Utils.service';
import { ModalDetailDeleteMultiComponent } from '../modal-detail-delete-multi/modal-detail-delete-multi.component';
import { ModalConfirmDeleteComponent } from '../modal-confirm-delete/modal-confirm-delete.component';
import { EXPORT_EXCEL_PRODUCT_NAME } from '../../../constants/file-name.constants';
import { ExportCommon } from '../../export/export.common';
import { CustomerSaveComponent } from '../../customer/customer-save/customer-save.component';
import { ModalCreateCategoryComponent } from '../modal-create-category/modal-create-category.component';
import { ImportProductGroupComponent } from '../../import/excel/productGroup/import-product-group.component';
import { Authority } from '../../../config/authority.constants';
import { ICON_BAR_CODE, ICON_BARCODE, ICON_CANCEL, ICON_CANCEL_WHITE, ICON_IMPORT_EXCEL } from '../../../shared/other/icon';
import { ModalPreviewBarcodePrintComponent } from '../preview-barcode-print/modal-preview-barcode-print.component';
import html2canvas from 'html2canvas';
import html2pdf from 'html2pdf.js';
import { jsPDF } from 'jspdf';
import jsbarcode from 'jsbarcode';

declare var window: any;

@Component({
  selector: 'jhi-sc-product',
  templateUrl: './sc-product.component.html',
  styleUrls: ['./sc-product.component.scss'],
})
export class ScProductComponent extends BaseComponent implements OnDestroy, OnInit {
  appSettings = appSettings;
  selectedProduct: any = {};
  lstProduct: ProductObjs[] = [];
  listSelected: any = [];
  selectedItem: any = {};
  categories: any = [];
  allCategories: any = [];
  lastCompany: any = {};
  totalItems: any = 0;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE };
  filterCategory: FilterProduct = { page: 0, size: Page.PAGE_SIZE };
  createCategoryReq: CreateCategotyReq;
  private modalRef: NgbModalRef | undefined;
  keywordCategory$ = new Subject<string>();
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  all = {
    name: 'Tất cả',
  };

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
  authorADD = Authority.PRODUCT_ADD;
  authorVIEW = Authority.PRODUCT_VIEW;
  authorUPDATE = Authority.PRODUCT_UPDATE;
  authorDELETE = Authority.PRODUCT_DELETE;
  authorImportExport = Authority.PRODUCT_IMPORT_EXPORT_EXCEL;
  authorPrintBarCode = Authority.PRODUCT_PRINT_BAR_CODE;

  constructor(
    private service: ProductService,
    protected utilsService: UtilsService,
    protected modalService: NgbModal,
    private translateService: TranslateService,
    private toastr: ToastrService,
    public exportComponent: ExportCommon
  ) {
    super();
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  async ngOnInit() {
    this.selectedProduct = {};
    this.createCategoryReq = new CreateCategotyReq();
    this.filterProduct.isCountAll = true;
    this.lastCompany = await this.getCompany();
    this.onSearch();
    this.getCategory();
    this.searchCategoryByKey();
  }

  ngAfterViewInit() {}

  loadMore($event: any) {
    this.filterProduct.page = $event;
    this.getLstProduct();
  }

  onSearch() {
    this.filterProduct.page = 1;
    this.getLstProduct();
  }

  onSearchById($event) {
    if (!$event) {
      this.filterProduct.groupId = undefined;
      this.onSearch();
    } else {
      this.filterProduct.groupId = $event.id;
      this.onSearch();
    }
  }

  getLstProduct() {
    const req = Object.assign({}, this.filterProduct);
    req.page = this.filterProduct.page - 1;
    this.service.getProductWithPaging(req).subscribe(value => {
      this.lstProduct = value.body.data.filter(item => item.code !== 'SP1');
      this.totalItems = value.body.count;
      if (this.lstProduct.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
        this.listSelected = [];
      } else if (this.listSelected.length === 0) {
        this.paramCheckAllPage = false;
        this.listSelected = [];
      } else {
        if (this.paramCheckAll) {
          this.lstProduct.forEach(n => {
            n.check = !this.listSelected.some(m => m === n.id);
          });
        } else {
          if (this.listSelected?.length > 0) {
            this.lstProduct.forEach(n => {
              n.check = this.listSelected.includes(n.id);
            });
          }
        }
      }
    });
  }

  onConfirmDeleteProduct(id: any) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.deleteMessage');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deleteProductById(id).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          const index = this.listSelected.indexOf(id);
          if (index > -1) {
            this.listSelected.splice(index, 1);
          }
          this.getLstProduct();
        });
      }
    });
  }

  getCategory() {
    this.service.getProductCategory(this.filterCategory).subscribe(value => {
      value.body.data.forEach(obj => {
        this.categories.push(obj);
      });
      this.categories = JSON.parse(JSON.stringify(this.categories));
      this.allCategories = [this.all].concat(this.categories);
    });
  }

  async onProductDetail(id: any) {
    this.openModalCreateProduct(id);
  }

  async onCreateProduct() {
    this.openModalCreateProduct(0);
  }

  openModalCreateProduct(id: any) {
    this.modalRef = this.modalService.open(ModalCreateProductComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
        this.categories = [];
        this.getLstProduct();
        this.getCategory();
      }
    });
  }

  onResetCheck(): Promise<void> {
    return new Promise(resolve => {
      this.paramCheckAll = false;
      this.paramCheckAllPage = false;
      this.listSelected = [];
      resolve();
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
        switchMap(() => this.service.getProductCategory(this.filterCategory))
      )
      .subscribe(res => {
        this.categories = res.body.data;
        this.allCategories = [this.all].concat(this.categories);
      });
  }

  loadMoreCategory() {
    this.filterCategory.page += 1;
    this.getCategory();
  }

  onDeleteKeywordSearch() {
    if (!this.filterProduct.keyword) {
      this.onSearch();
    }
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  getItem(product: any) {
    this.selectedItem = product;
  }

  deleteMultiProduct() {
    if (this.paramCheckAll || (this.listSelected?.length > 0 && this.lstProduct?.length > 0)) {
      this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
        size: 'dialog-centered',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.deleteMultiProduct');
      if (this.paramCheckAll) {
        let quantity = this.totalItems - this.listSelected.length;
        if (quantity >= 10) {
          this.modalRef.componentInstance.quantity = quantity;
          this.modalRef.componentInstance.checkDeleteMulti = true;
          this.modalRef.componentInstance.type = 'sản phẩm';
        }
      } else {
        if (this.listSelected.length >= 10) {
          this.modalRef.componentInstance.checkDeleteMulti = true;
          this.modalRef.componentInstance.quantity = this.listSelected.length;
          this.modalRef.componentInstance.type = 'sản phẩm';
        }
      }
      this.modalRef.closed.subscribe((res?: any) => {
        if (res === 1) {
          this.service
            .deleteMultiProduct({
              comId: this.lastCompany.id,
              paramCheckAll: this.paramCheckAll,
              ids: this.listSelected,
              keyword: this.filterProduct.keyword,
              groupId: this.filterProduct.groupId,
            })
            .subscribe(value => {
              this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
              this.modalRef = this.modalService.open(ModalDetailDeleteMultiComponent, {
                size: 'lg',
                backdrop: 'static',
              });
              this.modalRef.componentInstance.countAll = value.data.countAll;
              this.modalRef.componentInstance.countSuccess = value.data.countSuccess;
              this.modalRef.componentInstance.countFalse = value.data.countFalse;
              this.modalRef.componentInstance.data = value.data.dataFalse;
              this.modalRef.closed.subscribe((res?: any) => {
                this.paramCheckAll = false;
                this.paramCheckAllPage = false;
                this.listSelected = [];
                this.getLstProduct();
              });
            });
        }
      });
    }
  }

  onImportExcel() {
    this.modalRef = this.modalService.open(ImportProductComponent, { size: 'xl', backdrop: 'static' });
  }
  onExportExcel() {
    this.service
      .exportExcelProduct({
        comId: this.lastCompany.id,
        paramCheckAll: this.paramCheckAll,
        ids: this.listSelected,
        keyword: this.filterProduct.keyword,
        productGroupId: this.filterProduct.groupId,
      })
      .subscribe(
        response => {
          this.exportComponent.saveExcelFromByteArray(response.body, EXPORT_EXCEL_PRODUCT_NAME);
          this.toastr.success('Yêu cầu xuất file Excel thành công');
        },
        e => {
          this.toastr.error('Có lỗi xảy ra lúc yêu cầu xuất excel sản phẩm!!');
        }
      );
    this.paramCheckAllPage = false;
    this.paramCheckAll = false;
    this.listSelected = [];
    this.getLstProduct();
  }

  onOpenPreviewBarCode() {
    if (this.paramCheckAll || (this.listSelected?.length > 0 && this.totalItems > 0)) {
      this.modalRef = this.modalService.open(ModalPreviewBarcodePrintComponent, {
        size: 'xl',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
      this.modalRef.componentInstance.listSelected = this.listSelected;
    }
  }

  protected readonly ICON_IMPORT_EXCEL = ICON_IMPORT_EXCEL;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
  protected readonly ICON_BARCODE = ICON_BARCODE;
}
