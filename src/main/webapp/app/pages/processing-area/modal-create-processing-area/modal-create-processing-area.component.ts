import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { Location } from '@angular/common';
import { TranslateService } from '@ngx-translate/core';
import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { BaseComponent } from '../../../shared/base/base.component';
import { FilterProduct } from '../../product/product';
import { Page, ProcessingAreaDetail } from '../processing-area';
import { ProcessingAreaService } from '../processing-area.service';
import { UtilsService } from '../../../utils/Utils.service';
import { CreateProductProcessingAreaComponent } from '../create-product-processing-area/create-product-processing-area.component';
import { ModalConfirmDeleteComponent } from '../../product/modal-confirm-delete/modal-confirm-delete.component';
import { ConfirmChangeComponent } from '../confirm-change/confirm-change.component';
import { ICON_CANCEL, ICON_SAVE } from '../../../shared/other/icon';

@Component({
  selector: 'jhi-modal-create-processing-area',
  templateUrl: './modal-create-processing-area.component.html',
  styleUrls: ['./modal-create-processing-area.component.scss'],
})
export class ModalCreateProcessingAreaComponent extends BaseComponent implements OnInit {
  id: any = 0;
  lastCompany: any = {};
  products: any = [];
  createProduct: any = [];
  totalItems = 0;
  filterProduct: FilterProduct = { page: 0, size: Page.FULL_SIZE, isCountAll: true };
  listSelected: any = [];
  processingAreaDetail: any = {};
  selectedItem: any = {};
  paramCheckAll: boolean = false;
  private modalRef: NgbModalRef | undefined;
  indexProduct = -1;
  processingAreaProduct: any = [];
  idProcessingArea: any = 0;
  listSelectedDismiss: any = [];
  checkChange: boolean = false;
  isBegin = true;
  page = 1;
  size = 20;
  originalProducts: any[] = [];
  isLoading = false;
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

  constructor(
    protected utilsService: UtilsService,
    private service: ProcessingAreaService,
    private toastr: ToastrService,
    private location: Location,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    public activeModal: NgbActiveModal
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    if (this.id) {
      this.onProductDetail(this.id);
    }
    if (!this.processingAreaDetail.setting) {
      this.processingAreaDetail.setting = 0;
    }
    if (!this.processingAreaDetail.active) {
      this.processingAreaDetail.active = 1;
    }
    this.onSearch();
    // if (this.createProduct.length > 0) {
    //   this.products = [];
    //   for (let i = 0; i < this.createProduct.length; i++) {
    //     this.totalItems = this.totalItems + 1;
    //     this.products.push(this.createProduct[i]);
    //     this.listSelected.push(this.createProduct[i].productProductUnitId);
    //   }
    // }
    if (this.idProcessingArea != 0) {
      this.service.findProcessingArea(this.idProcessingArea).subscribe(value => {
        if (value.body.data.processingAreaProductItemResponses) {
          for (const item of value.body.data.processingAreaProductItemResponses) {
            this.listSelectedDismiss.push(item);
          }
        }
      });
    }
  }

  onSearch() {
    this.getProduct();
  }

  dismiss(value: any) {
    if (!this.processingAreaDetail.id && !this.processingAreaDetail.name && this.totalItems < 1) {
      this.checkChange = false;
    }
    if (this.checkChange) {
      this.modalRef = this.modalService.open(ConfirmChangeComponent, {
        size: 'dialog-centered',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.title = this.translateService.instant('processingArea.message.exit');
      this.modalRef.closed.subscribe((res?: any) => {
        if (res === 1) {
          this.activeModal.close(value);
        }
      });
    } else {
      this.activeModal.dismiss();
    }
  }

  // getProduct() {
  //   this.processingAreaDetail.comId = this.lastCompany.id;
  //   this.filterProduct.ids = this.lastCompany.id;
  //   const req = Object.assign({}, this.filterProduct);
  //   this.service.getProductWithPaging(req).subscribe(value => {
  //     this.products = JSON.parse(JSON.stringify(value.body.data));
  //     this.totalItems = value.body.count;
  //     if (this.products.length === 0 || this.listSelected.length === 0) {
  //       this.paramCheckAll = false;
  //       // this.paramCheckAllPage = false;
  //       this.listSelected = [];
  //     } else {
  //       if (this.paramCheckAll) {
  //         this.products.forEach(n => {
  //           n.check = !this.listSelected.some(m => m === n.productProductUnitId);
  //         });
  //       } else {
  //         if (this.listSelected?.length > 0) {
  //           this.products.forEach(n => {
  //             n.check = this.listSelected.includes(n.productProductUnitId);
  //           });
  //         }
  //       }
  //     }
  //   });
  // }

  getProduct() {
    this.processingAreaDetail.comId = this.lastCompany.id;
    // if (!this.isBegin) {
    let req = {
      comId: this.lastCompany.id,
      page: this.page - 1,
      size: this.size,
      paramCheckAll: this.paramCheckAll,
      ids: !this.listSelected || this.listSelected.length == 0 ? [-1] : this.listSelected,
    };
    this.service.findProduct(req).subscribe(value => {
      this.products = JSON.parse(JSON.stringify(value.body.data));
      this.totalItems = value.body.count;
    });
    // } else {
    //   if (this.processingAreaDetail.id) {
    //     this.products = this.originalProducts.slice(
    //       Math.min(this.originalProducts.length, (this.page - 1) * this.size),
    //       Math.min(this.originalProducts.length, this.page * this.size)
    //     );
    //     this.totalItems = this.originalProducts.length;
    //   }
    // }
  }

  check(object: any, listSelected: any, paramCheckAll: boolean, selectedItem: any) {
    object.check = !object.check;
    if (object.check) {
      selectedItem = object;
    }
    if (paramCheckAll) {
      // this.isShowRecord = false;
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
          if (listSelected[i] === object.productProductUnitId) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
    }
  }

  create() {
    this.isLoading = true;
    this.processingAreaDetail.listProduct = this.listSelected;
    this.processingAreaDetail.paramCheckAll = this.paramCheckAll;
    const req = Object.assign({}, this.processingAreaDetail);
    if (this.processingAreaDetail.id > 0) {
      this.service.update(req).subscribe(value => {
        this.isLoading = false;
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        this.triggerSearchInComponentB();
        this.activeModal.close(value);
      });
    } else {
      this.service.create(req).subscribe(value => {
        this.isLoading = false;
        this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
        this.triggerSearchInComponentB();
        this.activeModal.close(value);
      });
    }
  }

  getItem(product: any) {
    this.selectedItem = product;
  }

  onProductDetail(id: any) {
    this.service.findProcessingArea(id).subscribe(value => {
      this.listSelected = [];
      if (value.body.data.processingArea) {
        this.listSelected = JSON.parse(value.body.data.processingArea.ids);
        this.paramCheckAll = value.body.data.processingArea.paramCheckAll;
        if (this.paramCheckAll == null) {
          this.paramCheckAll = false;
        }
      }
      if (value.body.data.processingAreaProductItemResponses) {
        for (const item of value.body.data.processingAreaProductItemResponses) {
          // this.listSelected.push(item.productProductUnitId);
          this.products.push(item);
          this.originalProducts.push(item);
          this.processingAreaProduct.push(item.id);
        }
        this.totalItems = value.body.count;
      }
      this.processingAreaDetail = {
        id: value.body.data.processingArea.id,
        comId: value.body.data.processingArea.comId,
        name: value.body.data.processingArea.name,
        setting: value.body.data.processingArea.setting,
        active: value.body.data.processingArea.active,
      };
      if (this.products.length > this.size) {
        this.products = this.products.slice(0, this.size);
      }
      if (this.listSelected == null) {
        this.listSelected = [];
        this.products.forEach(n => {
          this.listSelected.push(n.productProductUnitId);
        });
      }
    });
  }

  async onCreateProduct() {
    this.openModalCreateProductProcessingArea(this.id);
    this.activeModal.dismiss();
  }

  openModalCreateProductProcessingArea(id: any) {
    // this.processingAreaDetail.listProduct = this.listSelected;
    this.modalRef = this.modalService.open(CreateProductProcessingAreaComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.componentInstance.processingAreaDetail = this.processingAreaDetail;
    this.modalRef.componentInstance.listSelectedDismiss = JSON.parse(JSON.stringify(this.products));
    // this.modalRef.componentInstance.listSelectedDismiss = this.listSelectedDismiss;
    this.modalRef.componentInstance.paramCheckAll = this.paramCheckAll;
    this.modalRef.componentInstance.listSelected = this.listSelected;
    this.modalRef.componentInstance.idProcessingArea = this.idProcessingArea;
    this.modalRef.componentInstance.processingAreaProduct = this.processingAreaProduct;
    this.modalRef.componentInstance.checkChange = JSON.parse(JSON.stringify(this.checkChange));
    this.modalRef.componentInstance.originCheckChange = JSON.parse(JSON.stringify(this.checkChange));
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
      }
    });
  }

  onConfirmDeleteCategory(product: any) {
    if (this.products.length < 2) {
      this.toastr.error(
        'Sản phẩm ' + product.name + ' là sản phẩm cuối cùng trong khu vực này, không thể xoá.',
        this.translateService.instant('processingArea.title.error')
      );
      return;
    }
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.deleteMessage');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.checkChanged();
        this.toastr.success('Xoá sản phẩm thành công', this.translateService.instant('easyPos.product.info.message'));
        this.totalItems = this.totalItems - 1;
        const index = this.listSelected.indexOf(product.productProductUnitId);
        if (!this.paramCheckAll && index > -1) {
          this.listSelected.splice(index, 1);
        } else {
          this.listSelected.push(product.productProductUnitId);
        }
        for (let i = 0; i < this.products.length; i++) {
          if (product.id == this.products[i].id) {
            this.indexProduct = i;
          }
        }
        if (this.indexProduct > -1) {
          this.products.splice(this.indexProduct, 1);
          // for (const item of this.processingAreaProduct) {
          //   if (item === product.id) {
          //     this.service.deleteProcessingAreaProduct(product.id).subscribe(value => {});
          //   }
          // }
        }
      }
    });
  }

  triggerSearchInComponentB(): void {
    this.service.triggerSearch();
  }

  checkChanged() {
    this.checkChange = true;
  }

  onChangedPage(event: any): void {
    this.page = event;
    this.getProduct();
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
