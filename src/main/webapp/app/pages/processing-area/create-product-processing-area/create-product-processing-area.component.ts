import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../../shared/base/base.component';
import { UtilsService } from '../../../utils/Utils.service';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

import { FilterProduct, FilterProductProductUnitId, Page } from '../processing-area';
import { ProcessingAreaService } from '../processing-area.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ModalCreateProcessingAreaComponent } from '../modal-create-processing-area/modal-create-processing-area.component';
import { ConfirmChangeComponent } from '../confirm-change/confirm-change.component';
import { ICON_CANCEL, ICON_CANCEL_WHITE, ICON_SAVE } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-create-product-processing-area',
  templateUrl: './create-product-processing-area.component.html',
  styleUrls: ['./create-product-processing-area.component.scss'],
})
export class CreateProductProcessingAreaComponent extends BaseComponent implements OnInit {
  id: any = 0;
  idProcessingArea: any = 0;
  lastCompany: any = {};
  products: any = [];
  totalItems = 0;
  filterProduct: FilterProduct = { page: 0, size: Page.PAGE_SIZE, isCountAll: true };
  filterProductProductUnitId: FilterProductProductUnitId = {};
  listSelected: any = [];
  listSelectedDismiss: any = [];
  selectedItem: any = {};
  paramCheckAll: boolean = false;
  paramCheckAllPage: boolean = false;
  originalTotalItems = 0;
  originalListSelected: any[] = [];
  originalParamCheckAll: boolean = false;
  showMore = false;
  paramCheckAllShowMore = false;
  idsShowMore: string;
  private modalRef: NgbModalRef | undefined;
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
  processingAreaDetail: any = {};
  processingAreaProduct: any = [];
  listProductProductUnitId: any = [];
  checkChange: boolean = false;
  originCheckChange: boolean = false;

  constructor(
    private service: ProcessingAreaService,
    protected utilsService: UtilsService,
    private toastr: ToastrService,
    private location: Location,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal,
    protected activatedRoute: ActivatedRoute,
    private route: Router
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.originalParamCheckAll = JSON.parse(JSON.stringify(this.paramCheckAll));
    this.originalListSelected = JSON.parse(JSON.stringify(this.listSelected));

    this.lastCompany = await this.getCompany();
    this.onSearch();
    // this.getListProductProductUnitId();
  }

  dismiss(value: any) {
    this.activeModal.close(value);
    if (!this.showMore) {
      this.modalRef = this.modalService.open(ModalCreateProcessingAreaComponent, {
        size: 'xl',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.processingAreaDetail = this.processingAreaDetail;
      this.modalRef.componentInstance.createProduct = this.listSelectedDismiss;
      this.modalRef.componentInstance.idProcessingArea = this.idProcessingArea;
      this.modalRef.componentInstance.processingAreaProduct = this.processingAreaProduct;
      this.modalRef.componentInstance.checkChange = JSON.parse(JSON.stringify(this.originCheckChange));

      this.modalRef.componentInstance.isBegin = false;
      this.modalRef.componentInstance.listSelected = this.originalListSelected;
      this.modalRef.componentInstance.paramCheckAll = this.originalParamCheckAll;
    }
  }

  onSearch() {
    this.getProduct();
  }

  onDeleteKeywordSearch() {
    if (!this.filterProduct.keyword) {
      this.getProduct();
    }
  }

  getProduct() {
    this.filterProduct.comId = this.lastCompany.id;
    if (this.filterProduct.page > 0) {
      this.filterProduct.page = this.filterProduct.page - 1;
    }
    if (this.idProcessingArea > 0) {
      this.filterProduct.processingAreaId = this.idProcessingArea;
    }
    if (this.paramCheckAllShowMore) {
      this.filterProduct.paramCheckAll = this.paramCheckAllShowMore;
    }
    if (this.idsShowMore) {
      this.filterProduct.ids = JSON.parse(this.idsShowMore);
    }
    const req = Object.assign({}, this.filterProduct);
    this.service.findProduct(req).subscribe(value => {
      if (this.paramCheckAll && this.totalItems < value.body.count) {
        this.products.forEach(item => {
          if (!item.check) {
            this.listSelected.push(item.productProductUnitId);
          }
        });
      }
      this.products = JSON.parse(JSON.stringify(value.body.data));
      this.totalItems = value.body.count;
      if (this.products.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
      } else {
        if (this.paramCheckAll) {
          this.paramCheckAllPage = true;
          this.products.forEach(n => {
            n.check = !this.listSelected.includes(n.productProductUnitId);
          });
        } else {
          if (this.listSelected?.length > 0) {
            this.paramCheckAllPage = true;
            this.products.forEach(n => {
              if (!this.listSelected.includes(n.productProductUnitId)) {
                this.paramCheckAllPage = false;
              }
              n.check = this.listSelected.includes(n.productProductUnitId);
            });
          }
        }
      }
    });
  }

  getItem(product: any) {
    this.selectedItem = product;
  }

  onChangedPage(event: any): void {
    this.filterProduct.page = event;
    this.getProduct();
  }

  checkAllForPage(objectList: any[], listSelected: any, paramCheckAllPage: boolean) {
    if (paramCheckAllPage) {
      objectList.forEach(n => {
        n.check = true;
        if (!listSelected.includes(n.productProductUnitId)) {
          listSelected.push(n.productProductUnitId);
        }
      });
    } else {
      objectList.forEach(n => (n.check = false));
      listSelected.splice(0, listSelected.length);
    }
  }

  create() {
    this.modalRef = this.modalService.open(ModalCreateProcessingAreaComponent, {
      size: 'xl',
      backdrop: 'static',
    });
    // this.modalRef.componentInstance.createProduct = this.listSelected;
    this.modalRef.componentInstance.processingAreaDetail = this.processingAreaDetail;
    this.modalRef.componentInstance.idProcessingArea = this.idProcessingArea;
    this.modalRef.componentInstance.processingAreaProduct = this.processingAreaProduct;
    this.modalRef.componentInstance.checkChange = JSON.parse(JSON.stringify(this.checkChange));

    this.modalRef.componentInstance.listSelected = this.listSelected;
    this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
    this.modalRef.componentInstance.isBegin = false;
    this.modalRef.closed.subscribe((res?: any) => {
      this.paramCheckAll = false;
      this.listSelected = [];
    });
    this.activeModal.dismiss();
  }

  checkAll(objectList: any, listSelected: any, paramCheckAll: boolean, paramCheckAllPage?: boolean) {
    this.checkChange = true;
    if (objectList) {
      objectList.forEach(n => (n.check = paramCheckAll));
      listSelected.splice(0, listSelected.length);
      // this.listSelected = this.listSelectedDismiss;
    }
  }

  getListProductProductUnitId() {
    this.filterProductProductUnitId.comId = this.lastCompany.id;
    this.filterProductProductUnitId.processingAreaId = this.idProcessingArea;
    const req = Object.assign({}, this.filterProductProductUnitId);
    this.service.findListProductProductUnitId(req).subscribe(value => {
      this.listProductProductUnitId = value.body.data;
    });
  }

  limit(string: any, limit: any) {
    return string.substring(0, limit) + '...';
  }

  checkListProductUnit(product: any, listSelected: any, paramCheckAll: boolean, selectedItem: any, check: boolean) {
    if (check) {
      product.check = !product.check;
    }
    let checkList = false;
    if (!product.check) {
      this.checked(product, this.listSelected, this.paramCheckAll, this.selectedItem);
    } else {
      for (const item of this.listProductProductUnitId) {
        if (product.productProductUnitId == item.productProductUnitId && product.check) {
          checkList = true;
        }
      }
      if (!checkList) {
        this.checked(product, this.listSelected, this.paramCheckAll, this.selectedItem);
      } else {
        this.modalRef = this.modalService.open(ConfirmChangeComponent, {
          size: 'dialog-centered',
          backdrop: 'static',
        });
        let name = this.listProductProductUnitId.find(
          item => item.productProductUnitId === product.productProductUnitId
        ).processingAreaName;
        if (name.trim().length > 30) {
          name = this.limit(name, 30);
        }
        this.modalRef.componentInstance.title = 'Sản phẩm ' + product.name + ' đang ở khu vực ' + name + ' bạn có chắc muốn đổi khu vực?';
        this.modalRef.closed.subscribe((res?: any) => {
          if (res === 1) {
            this.checked(product, this.listSelected, this.paramCheckAll, this.selectedItem);
          } else {
            product.check = false;
          }
        });
      }
    }
  }

  checked(object: any, listSelected: any, paramCheckAll: boolean, selectedItem: any) {
    this.checkChange = true;
    object.check = !object.check;
    if (object.check) {
      selectedItem = object;
    }
    if (paramCheckAll) {
      if (!object.check) {
        listSelected.push(object.productProductUnitId);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] === object.productProductUnitId) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
    } else {
      if (object.check) {
        listSelected.push(object.productProductUnitId);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] == object.productProductUnitId) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
      if (this.listSelected?.length > 0) {
        this.paramCheckAllPage = true;
        this.products.forEach(n => {
          if (!this.listSelected.includes(n.productProductUnitId)) {
            this.paramCheckAllPage = false;
          }
        });
      }
    }
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
}
