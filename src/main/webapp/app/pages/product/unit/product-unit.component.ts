import { Component, ElementRef, ViewChild, OnDestroy, OnInit } from '@angular/core';
import appSettings from 'app/config/app-settings';
import { ProductService } from '../product.service';
import { CreateCategotyReq, FilterProduct, Page, ProductObjs } from '../product';
import { DomSanitizer } from '@angular/platform-browser';
import { AccountService } from '../../../core/auth/account.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import { Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { ModalCreateUnitComponent } from '../modal-create-unit/modal-create-unit.component';
import { ModalConfirmDeleteComponent } from '../modal-confirm-delete/modal-confirm-delete.component';
import { UtilsService } from '../../../utils/Utils.service';
import { ModalDetailDeleteMultiComponent } from '../modal-detail-delete-multi/modal-detail-delete-multi.component';
import {ICON_CANCEL, ICON_CANCEL_WHITE} from "../../../shared/other/icon";

declare var window: any;

@Component({
  selector: 'jhi-product-unit',
  templateUrl: './product-unit.component.html',
  styleUrls: ['./product-unit.component.scss'],
})
export class ProductUnitComponent extends BaseComponent implements OnDestroy, OnInit {
  appSettings = appSettings;
  lstUnit: any[] = [];
  lastCompany: any = {};
  totalItems: any = 100;
  filterUnit: FilterProduct = { page: 1, size: 20 };
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  listSelected: any = [];
  selectedItem: any = {};
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

  constructor(
    private service: ProductService,
    private sanitizer: DomSanitizer,
    private accountService: AccountService,
    private toastr: ToastrService,
    private route: Router,
    private translateService: TranslateService,
    protected modalService: NgbModal,
    protected utilsService: UtilsService
  ) {
    super();
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.onSearch();
  }
  getLstUnit() {
    const req = Object.assign({}, this.filterUnit);
    req.page = this.filterUnit.page - 1;
    this.service.getUnitPaging(req).subscribe(value => {
      this.lstUnit = value.body.data;
      this.totalItems = value.body.count;
      if (this.totalItems === 0 || this.listSelected.length === 0) {
        this.paramCheckAll = false;
        this.paramCheckAllPage = false;
        this.listSelected = [];
      } else {
        if (this.paramCheckAll) {
          this.lstUnit.forEach(n => {
            n.check = !this.listSelected.some(m => m === n.id);
          });
        } else {
          if (this.listSelected?.length > 0) {
            this.lstUnit.forEach(n => {
              n.check = this.listSelected.includes(n.id);
            });
          }
        }
      }
    });
  }
  ngAfterViewInit() {}

  loadMore($event: any) {
    this.filterUnit.page = $event;
    this.getLstUnit();
  }

  onSearch() {
    this.filterUnit.page = 1;
    this.getLstUnit();
  }

  onConfirmDeleteUnit(id: any) {
    this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
      size: 'dialog-centered',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.confirmDeleteUnit');
    this.modalRef.closed.subscribe((res?: any) => {
      if (res === 1) {
        this.service.deleteUnit(id).subscribe(value => {
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          const index = this.listSelected.indexOf(id);
          if (index > -1) {
            this.listSelected.splice(index, 1);
          }
          this.getLstUnit();
        });
      }
    });
  }

  onDeleteKeywordSearch() {
    if (!this.filterUnit.keyword) {
      this.onSearch();
    }
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  onCreateUnit() {
    this.modalRef = this.modalService.open(ModalCreateUnitComponent, {
      size: '',
      backdrop: 'static',
    });
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
        this.getLstUnit();
      }
    });
  }
  onUpdateUnit(unit: any) {
    this.modalRef = this.modalService.open(ModalCreateUnitComponent, {
      size: '',
      backdrop: 'static',
    });
    this.modalRef.componentInstance.unit = JSON.parse(JSON.stringify(unit));
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.message) {
        this.getLstUnit();
      }
    });
  }
  deleteMultiUnit() {
    if (this.paramCheckAll || (this.listSelected?.length > 0 && this.totalItems > 0)) {
      this.modalRef = this.modalService.open(ModalConfirmDeleteComponent, {
        size: 'dialog-centered',
        backdrop: 'static',
      });
      this.modalRef.componentInstance.title = this.translateService.instant('easyPos.product.info.confirmDeleteMultiUnit');
      if (this.paramCheckAll) {
        let quantity = this.totalItems - this.listSelected.length;
        if (quantity >= 10) {
          this.modalRef.componentInstance.quantity = quantity;
          this.modalRef.componentInstance.checkDeleteMulti = true;
          this.modalRef.componentInstance.type = 'đơn vị tính';
        }
      } else {
        if (this.listSelected.length >= 10) {
          this.modalRef.componentInstance.checkDeleteMulti = true;
          this.modalRef.componentInstance.quantity = this.listSelected.length;
          this.modalRef.componentInstance.type = 'đơn vị tính';
        }
      }
      this.modalRef.closed.subscribe((res?: any) => {
        if (res === 1) {
          this.service
            .deleteMultiUnit({
              comId: this.lastCompany.id,
              paramCheckAll: this.paramCheckAll,
              ids: this.listSelected,
              keyword: this.filterUnit.keyword,
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
                this.getLstUnit();
              });
            });
        }
      });
    }
  }
  getItem(product: any) {
    this.selectedItem = product;
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
    protected readonly ICON_CANCEL_WHITE = ICON_CANCEL_WHITE;
}
