import { Component, OnDestroy, OnInit } from '@angular/core';
import { ProductService } from '../../product/product.service';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Location } from '@angular/common';
import { BaseComponent } from '../../../shared/base/base.component';
import { FilterProduct, ListProductToppingReq } from '../../product/product';
import { map } from 'rxjs/operators';
import { debounceTime, distinctUntilChanged, forkJoin, Subject, switchMap, tap } from 'rxjs';
import { Page } from '../../const/customer-order.const';
import {ICON_CANCEL, ICON_SAVE} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-modal-create-topping-group',
  templateUrl: './modal-create-topping-group.component.html',
  styleUrls: ['./modal-create-topping-group.component.scss'],
})
export class ModalCreateToppingGroupComponent extends BaseComponent implements OnDestroy, OnInit {
  id: any;
  groupSelected: any = {};
  lastCompany: any = {};
  disableButton = false;
  listProductToppingReq: ListProductToppingReq;
  filterProduct: FilterProduct = { page: 0, size: 20, isTopping: true, ids: [] };
  listProductTopping: any = [];
  listProducts: any = [];
  listProductLinks: any = [];
  checkNullData = true;
  keywordToppings$ = new Subject<string>();
  constructor(
    private service: ProductService,
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.searchToppings();
    this.listProductToppingReq = new ListProductToppingReq();
    this.listProductToppingReq.id = this.id;
    this.listProductToppingReq.page = 0;
    this.listProductToppingReq.size = 20;
    this.listProductToppingReq.isSingleList = true;
    if (this.id) {
      await this.onGroupToppingDetail();
    }
    await this.getListProductTopping(0);
  }

  getListProductTopping(checkLoadMore: any) {
    return new Promise((resolve, reject) => {
      this.service.getProductWithPaging(this.filterProduct).subscribe(
        result => {
          if (checkLoadMore === 1) {
            this.listProductTopping.push(...result.body.data);
          } else {
            this.listProductTopping = result.body.data;
          }
          this.listProductTopping = JSON.parse(JSON.stringify(this.listProductTopping));
          resolve(result);
        },
        error => {
          reject(error);
        }
      );
    });
  }

  onGroupToppingDetail() {
    return new Promise((resolve, reject) => {
      this.service.getToppingGroupById(this.id).subscribe(
        value => {
          this.groupSelected = value.data;
          this.groupSelected.products.forEach(value => {
            if (value.id) {
              this.listProducts.push(value.id);
            }
          });
          this.groupSelected.productLinks.forEach(value => {
            if (value.id) {
              this.listProductLinks.push(value.id);
            }
          });
          this.listProducts = JSON.parse(JSON.stringify(this.listProducts));
          this.filterProduct.ids = this.listProducts;
          this.filterProduct = JSON.parse(JSON.stringify(this.filterProduct));
          this.listProductLinks = JSON.parse(JSON.stringify(this.listProductLinks));
          this.groupSelected = JSON.parse(JSON.stringify(this.groupSelected));
          resolve(value);
        },
        error => {
          reject(error);
        }
      );
    });
  }
  onSave() {
    if (!this.groupSelected.name) {
      this.toastr.error(
        this.translateService.instant('easyPos.topping.error.groupNameError'),
        this.translateService.instant('easyPos.product.info.message')
      );
    } else {
      this.groupSelected.comId = this.lastCompany.id;
      if (this.id) {
        this.disableButton = true;
        this.service.updateToppingGroup(this.groupSelected).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(1);
            this.disableButton = true;
          },
          error => {
            this.disableButton = false;
          }
        );
      } else {
        this.service.createToppingGroup(this.groupSelected).subscribe(
          value => {
            this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
            this.dismiss(1);
            this.disableButton = true;
          },
          error => {
            this.disableButton = false;
          }
        );
      }
    }
  }
  dismiss(value: any) {
    this.activeModal.close(value);
  }
  onChangeRequiredOptional() {}
  onBlurGroupName() {
    this.groupSelected.name = this.groupSelected.name.trim();
  }

  onProductToppingSelected($event: any) {
    this.groupSelected.products = $event;
  }

  loadMoreToppings() {
    if (this.checkNullData) {
      this.filterProduct.page++;
      this.getListProductTopping(1);
    }
  }

  searchToppings() {
    this.keywordToppings$
      .pipe(
        debounceTime(600),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.filterProduct.keyword = keyword;
            this.filterProduct.page = 0;
          } else {
            this.filterProduct.keyword = '';
          }
        }),
        switchMap(() => this.service.getProductWithPaging(this.filterProduct))
      )
      .subscribe(res => {
        this.listProductTopping = JSON.parse(JSON.stringify(res.body.data));
      });
  }

    protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
