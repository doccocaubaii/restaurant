import { Component, OnDestroy, OnInit } from '@angular/core';
import { BaseComponent } from '../../shared/base/base.component';
import { FilterProduct, Page } from '../product/product';
import { ProductService } from '../product/product.service';
import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateCategoryComponent } from '../product/modal-create-category/modal-create-category.component';
import { ModalConfirmDeleteComponent } from '../product/modal-confirm-delete/modal-confirm-delete.component';
import { TranslateService } from '@ngx-translate/core';
import { ImportProductGroupComponent } from '../import/excel/productGroup/import-product-group.component';
import { UtilsService } from '../../utils/Utils.service';
import { ModalDetailDeleteMultiComponent } from '../product/modal-detail-delete-multi/modal-detail-delete-multi.component';
import { Authority } from '../../config/authority.constants';
import {ICON_CANCEL, ICON_CANCEL_WHITE, ICON_IMPORT_EXCEL} from "../../shared/other/icon";

@Component({
  selector: 'jhi-product-category',
  templateUrl: './product-category.component.html',
  styleUrls: ['./product-category.component.scss'],
})
export class ProductCategoryComponent extends BaseComponent implements OnDestroy, OnInit {
  posMobileSidebarToggled = false;
  categories: any = [];
  categorySelected: any = {};
  lastCompany: any = {};
  totalItems: any = 100;
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  listSelected: any = [];
  selectedItem: any = {};
  filterCategory: FilterProduct = { page: 0, size: Page.PAGE_SIZE, isCountAll: true };
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
  authorGroupAdd = Authority.GROUP_ADD;
  authorGroupVIEW = Authority.GROUP_VIEW;
  authorGroupUPDATE = Authority.GROUP_UPDATE;
  authorGroupDELETE = Authority.GROUP_DELETE;
  authorGroupImportExportExcel = Authority.GROUP_IMPORT_EXPORT_EXCEL;

  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    protected modalService: NgbModal,
    private translateService: TranslateService,
    protected utilsService: UtilsService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.onSearch();
  }

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  onSearch() {
    this.filterCategory.page = 1;
    this.getCategory();
  }

  getCategory() {
    const req = Object.assign({}, this.filterCategory);
    req.page = this.filterCategory.page - 1;
    this.service.getProductCategory(req).subscribe(value => {
      this.categories = JSON.parse(JSON.stringify(value.body.data));
      this.totalItems = value.body.count;
      if (this.categories.length === 0 || this.listSelected.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
        this.listSelected = [];
      } else {
        if (this.paramCheckAll) {
          this.categories.forEach(n => {
            n.check = !this.listSelected.some(m => m === n.id);
          });
        } else {
          if (this.listSelected?.length > 0) {
            this.categories.forEach(n => {
              n.check = this.listSelected.includes(n.id);
            });
          }
        }
      }
    });
  }

  onDeleteKeywordSearch() {
    if (!this.filterCategory.keyword) {
      this.getCategory();
    }
  }

  loadMore($event) {
    this.filterCategory.page = $event;
    this.getCategory();
  }

  onConfirmDeleteCategory(category: any) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.category.infor.deleteMessage');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deleteCategory(category.id, category.comId).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          const index = this.listSelected.indexOf(category.id);
          if (index > -1) {
            this.listSelected.splice(index, 1);
          }
          this.getCategory();
        });
      }
    });
  }

  openModalCreateCategory(categorySelected: any) {
    this.modalRef = this.modalService.open(ModalCreateCategoryComponent, { size: '', backdrop: 'static' });
    this.modalRef.componentInstance.categorySelected = categorySelected;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
        this.getCategory();
      }
    });
  }

  onCategoryUpdate(category) {
    this.categorySelected = {};
    this.categorySelected = JSON.parse(JSON.stringify(category));
    this.openModalCreateCategory(this.categorySelected);
  }

  onCreateCategory() {
    this.categorySelected = {};
    this.openModalCreateCategory(this.categorySelected);
  }

  getItem(category: any) {
    this.selectedItem = category;
  }

  deleteMultiCategory() {
    if (this.paramCheckAll || (this.listSelected?.length > 0 && this.categories?.length)) {
      this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
        size: 'dialog-centered',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.title = this.translateService.instant('easyPos.category.infor.deleteMultiCategoryMessage');
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
          this.modalRef.componentInstance.type = 'nhóm sản phẩm';
        }
      }
      this.modalRef.closed.subscribe((res?: any) => {
        if (res === 1) {
          this.service
            .deleteMultiCategory({
              comId: this.lastCompany.id,
              paramCheckAll: this.paramCheckAll,
              ids: this.listSelected,
              keyword: this.filterCategory.keyword,
              groupId: this.filterCategory.groupId,
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
              this.modalRef.componentInstance.isCategory = true;
              this.modalRef.closed.subscribe((res?: any) => {
                this.paramCheckAll = false;
                this.paramCheckAllPage = false;
                this.listSelected = [];
                this.getCategory();
              });
            });
        }
      });
    }
  }

  onImportExcel() {
    this.modalRef = this.modalService.open(ImportProductGroupComponent, { size: 'xl', backdrop: 'static' });
  }

  protected readonly ICON_IMPORT_EXCEL = ICON_IMPORT_EXCEL;
    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
}
