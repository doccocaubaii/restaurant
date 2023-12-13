import { Component, OnDestroy, OnInit } from '@angular/core';
import { FilterProduct, FilterTopping, Page } from '../product/product';
import { ProductService } from '../product/product.service';
import { ToastrService } from 'ngx-toastr';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { BaseComponent } from '../../shared/base/base.component';
import { ModalCreateCategoryComponent } from '../product/modal-create-category/modal-create-category.component';
import { ModalCreateToppingGroupComponent } from './modal-create-topping-group/modal-create-topping-group.component';
import { ModalConfirmDeleteComponent } from '../product/modal-confirm-delete/modal-confirm-delete.component';
import { Authority } from '../../config/authority.constants';

@Component({
  selector: 'jhi-product-topping',
  templateUrl: './product-topping.component.html',
  styleUrls: ['./product-topping.component.scss'],
})
export class ProductToppingComponent extends BaseComponent implements OnDestroy, OnInit {
  filterTopping: FilterTopping = { page: 0, size: Page.PAGE_SIZE, isCountAll: true };
  lstTopping: any = [];
  lastCompany: any = {};
  totalItems: any = 0;
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
  authorADD = Authority.TOPPING_ADD;
  authorVIEW = Authority.TOPPING_VIEW;
  authorUPDATE = Authority.TOPPING_UPDATE;
  authorDELETE = Authority.TOPPING_DELETE;
  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    protected modalService: NgbModal,
    private translateService: TranslateService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.onSearch();
  }
  onSearch() {
    this.filterTopping.page = 1;
    this.getToppings();
  }

  getToppings() {
    this.filterTopping.comId = this.lastCompany.id;
    const req = Object.assign({}, this.filterTopping);
    req.page = this.filterTopping.page - 1;
    this.service.getProductTopping(req).subscribe(value => {
      this.lstTopping = value.body.data;
      this.totalItems = value.body.count;
    });
  }

  onDeleteKeywordSearch() {
    if (!this.filterTopping.keyword) {
      this.onSearch();
    }
  }
  onCreateToppingGroup() {
    this.openModalCreateToppingGroup({});
  }

  onUpdateToppingGroup(topping: any) {
    this.openModalCreateToppingGroup(topping);
  }
  onConfirmDeleteGroupTopping(groupTopping) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.topping.error.titleDeleteGroup');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deleteToppingGroupById(groupTopping.id).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.getToppings();
        });
      }
    });
  }

  loadMore($event) {
    if (this.filterTopping.comId) {
      this.filterTopping.page = $event;
      this.getToppings();
    }
  }
  openModalCreateToppingGroup(groupSelected: any) {
    this.modalRef = this.modalService.open(ModalCreateToppingGroupComponent, { size: 'lg', backdrop: 'static' });
    this.modalRef.componentInstance.id = groupSelected.id;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.getToppings();
      }
    });
  }
}
