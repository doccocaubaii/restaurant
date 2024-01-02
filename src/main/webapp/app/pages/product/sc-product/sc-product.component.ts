import { Component, ElementRef, ViewChild, OnDestroy, OnInit } from '@angular/core';
import appSettings from 'app/config/app-settings';
import { ProductService } from '../product.service';
import { debounceTime, distinctUntilChanged, Observable, Subject, switchMap, tap } from 'rxjs';
import { CreateCategotyReq, FilterProduct, Page, ProductObjs } from '../product';
import { DomSanitizer } from '@angular/platform-browser';
import { AccountService } from '../../../core/auth/account.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateProductComponent } from '../modal-create-product/modal-create-product.component';
import { ModalConfirmDeleteProductComponent } from '../modal-confirm-delete-product/modal-confirm-delete-product.component';

declare var window: any;

@Component({
  selector: 'jhi-sc-product',
  templateUrl: './sc-product.component.html',
  styleUrls: ['./sc-product.component.scss'],
})
export class ScProductComponent extends BaseComponent implements OnDestroy, OnInit {
  appSettings = appSettings;
  posMobileSidebarToggled = false;
  selectedProduct: any = {};
  lstProduct: ProductObjs[] = [];
  allCategories: any = [];
  lastCompany: any = {};
  totalItems: any = 100;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE };
  filterCategory: FilterProduct = { page: 0, size: Page.PAGE_SIZE };

  createCategoryReq: CreateCategotyReq;
  private modalRef: NgbModalRef | undefined;
  keywordCategory$ = new Subject<string>();
  all = {
    name: 'Tất cả',
  };
  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  constructor(
    private service: ProductService,
    private sanitizer: DomSanitizer,
    private accountService: AccountService,
    private toastr: ToastrService,
    private route: Router,
    private translateService: TranslateService,
    protected modalService: NgbModal
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
  }

  ngAfterViewInit() {}

  loadMore($event: any) {
    this.lstProduct = [];
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
    });
  }
  onConfirmDeleteProduct(id: any) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteProductComponent, { size: 'dialog-centered', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      this.filterProduct.page = 1;
      this.getLstProduct();
    });
  }

  onProductDetail(id: any) {
    this.openModalCreateProduct(id);
  }

  onCreateProduct() {
    this.openModalCreateProduct(0);
  }
  openModalCreateProduct(id: any) {
    this.modalRef = this.modalService.open(ModalCreateProductComponent, { size: 'fullscreen', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res) {
        if (!this.selectedProduct.id) {
          console.log(res);
          this.onSearch();
          // this.allCategories = [this.all].concat(this.categories);
        } else {
          this.onSearch();
        }
      }
    });
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
}
