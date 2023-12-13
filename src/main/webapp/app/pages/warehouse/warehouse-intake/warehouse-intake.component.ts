import { Component, OnDestroy, OnInit } from '@angular/core';
import { DetailIntake, FilterCustomer, FilterProduct, Page, WarehouseIntakeReq } from '../warehouse';
import { WarehouseService } from '../warehouse.service';
import { BaseComponent } from '../../../shared/base/base.component';
import { ToastrService } from 'ngx-toastr';
import dayjs from 'dayjs';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { CustomerService } from '../../customer/customer.service';
import { ActivatedRoute, Router } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { ModalCreateProductComponent } from '../../product/modal-create-product/modal-create-product.component';
import { CustomerSaveComponent } from '../../customer/customer-save/customer-save.component';
import { ContentOption } from '../../../utils/contentOption';
import { RS_INOUT_WARD } from '../../../constants/app.routing.constants';
import { debounceTime, distinctUntilChanged, lastValueFrom, Subject, switchMap, tap } from 'rxjs';
import { take_decimal_number } from '../../const/function';
import { ModalConfirmExitComponent } from '../modal-confirm-exit/modal-confirm-exit.component';
import { ModalUpdateInventoyTrackingComponent } from '../modal-update-inventoy-tracking/modal-update-inventoy-tracking.component';
import { debounce } from 'lodash';
import { ICON_CANCEL, ICON_DELETE_SM, ICON_SAVE, ICON_SEARCH_LG } from '../../../shared/other/icon';
import { ConvertResponse } from '../../../config/convert-response';
import { SPDVProduct } from '../../const/customer-order.const';
import { LoadingBarService } from '@ngx-loading-bar/core';
import { ConfirmDialogComponent } from '../../../shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../order/model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from '../../../constants/modal.const';
import {InvoiceService} from "../../invoice/service/invoice.service";

@Component({
  selector: 'jhi-warehouse-intake',
  templateUrl: './warehouse-intake.component.html',
  styleUrls: ['./warehouse-intake.component.scss'],
})
export class WarehouseIntakeComponent extends BaseComponent implements OnDestroy, OnInit {
  posMobileSidebarToggled = false;
  posSidebarFooterStatus = true;
  lstProduct: any;
  lstCustomer: any;
  lastCompany: any;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE };
  filterCustomer: FilterCustomer;
  intakeReq: WarehouseIntakeReq;
  formProductIntake: any;
  formDiscountAmount: any;
  formCostAmount: any;
  productIntake: any;
  lstProductIntake: any;
  checkPercentOrValue: any;
  percentInput = 0;
  discountInput: number = 0;
  costInput = 0;
  formPaymentMethod: any;
  disableButton = false;
  paymentMethodInput = '';
  keywordCustomer$ = new Subject<string>();
  private modalRef: NgbModalRef | undefined;
  checkExit = false;
  activeRemoveProduct = 0;
  discountType = [
    {
      value: 0,
      name: 'Chiết khấu theo giá trị',
    },
    {
      value: 1,
      name: 'Chiết khấu theo phần trăm',
    },
  ];
  paymentMethod = [
    {
      id: 1,
      name: 'Tiền mặt',
      value: 'Tiền mặt',
    },
    {
      id: 2,
      name: 'Chuyển khoản',
      value: 'Chuyển khoản',
    },
    {
      id: 3,
      name: 'Khác',
      value: '',
    },
  ];
  overStock: any;
  constructor(
    private warehouseService: WarehouseService,
    private toastr: ToastrService,
    private modalService: NgbModal,
    private customerService: CustomerService,
    private router: Router,
    private translateService: TranslateService,
    public contentOption: ContentOption,
    private activatedRoute: ActivatedRoute,
    private loadingService: LoadingBarService,
    private invoiceService: InvoiceService
  ) {
    super();
    this.contentOption.isHiddenOrder = true;
  }

  async ngOnInit() {
    // @ts-ignore
    this.formProductIntake = new window.bootstrap.Modal(document.getElementById('modalIntakeProduct'));
    // @ts-ignore
    this.formDiscountAmount = new window.bootstrap.Modal(document.getElementById('modalDiscountAmount'));
    // @ts-ignore
    this.formCostAmount = new window.bootstrap.Modal(document.getElementById('modalCostAmount'));
    // @ts-ignore
    this.formPaymentMethod = new window.bootstrap.Modal(document.getElementById('modalPaymentMethod'));
    this.productIntake = {};
    this.lstProduct = [];
    this.lstCustomer = [];
    this.lstProductIntake = [];
    this.checkPercentOrValue = 0;
    this.intakeReq = new WarehouseIntakeReq();
    this.filterCustomer = new FilterCustomer();
    this.filterCustomer.page = 0;
    this.filterCustomer.size = 10;
    this.lastCompany = await this.getCompany();
    this.invoiceService.getCompanyConfig(this.lastCompany.id).subscribe(value => {
      if (value.data) {
        this.overStock = value.data.overStock;
      }
    });
    this.resetIntakeReq();
    this.onSearch();
    await this.getLstCustomer(false);
    this.searchCustomer();

    this.intakeReq.paymentMethod = 'Tiền mặt';
    try {
      this.params = await new Promise(resolve => {
        this.activatedRoute.queryParamMap.subscribe((params: any) => {
          resolve(params);
        });
      });
      if (!this.params.get('id')) {
        this.intakeReq.customerId = this.lstCustomer[0].id;
        this.intakeReq.customerName = this.lstCustomer[0].name;
        if (this.params.get('type') && Number(this.params.get('type')) == 2) {
          this.intakeReq.type = 2;
          this.intakeReq.typeDesc = 'Xuất kho';
        } else {
          this.intakeReq.type = 1;
          this.intakeReq.typeDesc = 'Nhập kho';
        }
      } else {
        await this.getDataInit();
      }
    } catch (error) {}
  }

  loadMore = debounce(($event: any) => {
    const target = $event.target;
    if (target.scrollTop === 0) {
      return;
    }
    if (target.offsetHeight + target.scrollTop >= target.scrollHeight - 50) {
      this.filterProduct.page += 1;
      this.getLstProduct();
    }
  }, 100);

  onSearch() {
    this.lstProduct = [];
    this.filterProduct.page = 0;
    this.getLstProduct();
  }

  onDeleteKeywordSearch() {
    if (!this.filterProduct.keyword) {
      this.onSearch();
    }
  }

  deleteKeywordSearch() {
    this.filterProduct.keyword = undefined;
    this.onSearch();
  }

  onSelectProductIntake(product: any) {
    if (!product.inventoryTracking) {
      if (product.isPrimary) {
        this.modalRef = this.modalService.open(ModalUpdateInventoyTrackingComponent, {
          size: 'lg',
          backdrop: 'static',
        });
        this.modalRef.componentInstance.productInfo = JSON.parse(JSON.stringify(product));
        this.modalRef.closed.subscribe((res?: any) => {
          if (res.inventoryTracking) {
            this.onSearch();
          }
        });
      } else {
        this.toastr.error(
          this.translateService.instant('easyPos.warehouse.info.isPrimaryError'),
          this.translateService.instant('easyPos.warehouse.info.message')
        );
      }
    } else {
      const objectToSelect = this.lstProductIntake.find(arrayItem => arrayItem.productProductUnitId === product.productProductUnitId);
      if (objectToSelect) {
        if (this.intakeReq.type === 2 && this.overStock === 0) {
          if (product.inventoryCount-1 <= objectToSelect.numberIntake && objectToSelect.numberIntake < product.inventoryCount) {
            objectToSelect.numberIntake = product.inventoryCount
          } else if (objectToSelect.numberIntake <= product.inventoryCount-1) {
            objectToSelect.numberIntake += 1;
            objectToSelect.amountIntake = take_decimal_number(objectToSelect.purchasePrice * objectToSelect.numberIntake, 0);
          } else {
            this.toastr.error(
                'Sản phẩm ' + product.productName + ' vượt quá số lượng tồn kho' + ' (Tổng tồn kho: ' + product.inventoryCount + ' ' + (product.unit ? product.unit : '') + ')',
                'Thông báo'
            );
          }
        } else {
          objectToSelect.numberIntake += 1;
          objectToSelect.amountIntake = take_decimal_number(objectToSelect.purchasePrice * objectToSelect.numberIntake, 0);
        }
      } else {
        product.checked = true;
        this.productIntake = product;
        this.productIntake.productId = product.productId;
        this.productIntake.numberIntake = 1;
        this.productIntake.amountIntake = take_decimal_number(this.productIntake.purchasePrice * this.productIntake.numberIntake, 0);
        this.lstProductIntake.push(this.productIntake);
        this.lstProductIntake = JSON.parse(JSON.stringify(this.lstProductIntake));
      }
      this.getAmount();
    }
  }

  closeIntakeProductModal() {
    this.formProductIntake.hide();
  }

  closeDiscountModal() {
    this.formDiscountAmount.hide();
  }

  getLstProduct() {
    let req = Object.assign({}, this.filterProduct);
    this.warehouseService.getProductWithPaging(req).subscribe(value => {
      value.body.data.forEach(obj => {
        if (!['SP1', 'SPGC', 'SPKM', 'SPDV', 'SPCK'].includes(obj.productCode)) {
          this.lstProduct.push(obj);
        }
      });
    });
  }

  async getLstCustomer(checkScroll: any) {
    this.filterCustomer.type = 3;
    const value: any = await lastValueFrom(this.warehouseService.getAllCustomerPaging(this.filterCustomer));
    if (!value.body.data.length) {
      this.filterCustomer.page -= 1;
    } else if (checkScroll) {
      this.lstCustomer = this.lstCustomer.concat(value.body.data);
    } else {
      this.lstCustomer = value.body.data;
    }
  }

  onSaveInoutward() {
    this.intakeReq.comId = this.lastCompany.id;
    this.intakeReq.date = dayjs().format('YYYY-MM-DD HH:mm:ss').toString();
    if (!this.intakeReq.paymentMethod) {
      if (this.paymentMethodInput) {
        this.intakeReq.paymentMethod = this.paymentMethodInput;
      } else {
        this.toastr.error(
          this.translateService.instant('easyPos.warehouse.info.payMethodError1'),
          this.translateService.instant('easyPos.warehouse.info.message')
        );
        return;
      }
    }
    this.disableButton = true;
    if (!this.isUpdate) {
      this.loadingService.start();
      this.warehouseService.createInoutward(this.intakeReq).subscribe(
        value => {
          this.loadingService.complete();
          this.resetIntakeReq();
          this.lstProductIntake = [];
          this.discountInput = 0;
          this.onSearch();
          this.toastr.success(value.message[0].message, this.translateService.instant('easyPos.product.info.message'));
          this.disableButton = false;
          if (this.checkExit) {
            this.router.navigate([RS_INOUT_WARD]);
          }
          this.intakeReq = new WarehouseIntakeReq();
          this.intakeReq.discountAmount = 0;
          this.intakeReq.costAmount = 0;
          this.intakeReq.paymentMethod = 'Tiền mặt';
          this.intakeReq.amount = 0;
          this.intakeReq.totalAmount = 0;
          this.intakeReq.customerId = this.lstCustomer[0].id;
          this.intakeReq.customerName = this.lstCustomer[0].name;
        },
        error => {
          this.loadingService.complete();
          this.checkExit = false;
          this.disableButton = false;
        }
      );
    } else {
      // if (this.intakeReq.quantity && this.totalQuantityOld > this.intakeReq.quantity) {
      //   this.modalRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static' });
      //   this.modalRef.componentInstance.value = new DialogModal(
      //     ModalHeader.SAVE_UPDATE_INWARD,
      //     ModalContent.SAVE_UPDATE_INWARD,
      //     ModalBtn.AGREE,
      //     'check',
      //     'btn-save'
      //   );
      //   this.modalRef.componentInstance.formSubmit.subscribe(res => {
      //     if (res) {
      //       this.updateInWard();
      //     }
      //   });
      // } else {
        this.updateInWard();
      // }
    }

    this.formPaymentMethod.hide();
    this.disableButton = false;
  }

  private updateInWard() {
    this.loadingService.start();
    this.warehouseService.updateInoutward(this.intakeReq).subscribe(
      value => {
        this.loadingService.complete();
        this.resetIntakeReq();
        this.toastr.success(value.message[0].message);
        this.disableButton = false;
        this.router.navigate([RS_INOUT_WARD]);
      },
      error => {
        this.loadingService.complete();
        this.disableButton = false;
      }
    );
    if (this.modalRef) this.modalRef.close();
    this.loadingService.complete();
  }

  onChangeCustomer($event) {
    this.intakeReq.customerId = $event.id;
    this.intakeReq.customerName = $event.name;
  }

  minus() {
    if (this.productIntake.numberIntake) {
      if (this.productIntake.numberIntake > 0) {
        this.productIntake.numberIntake = Number(this.productIntake.numberIntake) - 1;
        this.productIntake.amountIntake = take_decimal_number(this.productIntake.purchasePrice * this.productIntake.numberIntake, 0);
        if (this.productIntake.numberIntake < 0) {
          this.productIntake.numberIntake = 0;
        }
      }
    }
  }

  onProductDetailIntake() {
    if (this.productIntake.numberIntake == null || this.productIntake.numberIntake == undefined) {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.quantityError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
    } else if (this.productIntake.purchasePrice == null || this.productIntake.purchasePrice == undefined) {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.purchasePriceError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
    } else {
      const objectToReplace = this.lstProductIntake.find(
        arrayItem => arrayItem.productProductUnitId === this.productIntake.productProductUnitId
      );
      if (objectToReplace) {
        Object.assign(objectToReplace, this.productIntake);
        this.formProductIntake.hide();
        this.getAmount();
      } else {
        this.lstProductIntake.push(this.productIntake);
        this.formProductIntake.hide();
      }
    }
  }

  onChangeQuantity(product: any) {
    if (product.numberIntake) {
      product.amountIntake = take_decimal_number(product.purchasePrice * product.numberIntake, 0);
    } else {
      product.amountIntake = 0;
    }
    this.getAmount();
  }

  onChangeNumberIntake() {
    if (this.productIntake.numberIntake) {
      this.productIntake.amountIntake = take_decimal_number(this.productIntake.purchasePrice * this.productIntake.numberIntake, 0);
    } else {
      this.productIntake.amountIntake = 0;
    }
  }

  onChangePurchasePrise() {
    if (this.productIntake.purchasePrice) {
      this.productIntake.amountIntake = take_decimal_number(this.productIntake.purchasePrice * this.productIntake.numberIntake, 0);
    } else {
      this.productIntake.amountIntake = 0;
    }
  }

  onUpdateProductIntake(product) {
    this.productIntake = JSON.parse(JSON.stringify(product));
    this.formProductIntake.show();
  }

  onDiscountAmount() {
    if (this.lstProductIntake.length > 0) {
      this.discountInput = this.intakeReq.discountAmount;
      // this.percentInput = (Number(this.discountInput) / this.intakeReq.amount) * 100;
      this.formDiscountAmount.show();
    } else {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.lstIntakeError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
    }
  }

  onCostAmount() {
    if (this.lstProductIntake.length > 0) {
      this.costInput = this.intakeReq.costAmount;
      this.formCostAmount.show();
    } else {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.lstIntakeError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
    }
  }

  closeCostModal() {
    this.formCostAmount.hide();
  }

  getAmount() {
    this.intakeReq.amount = 0;
    this.intakeReq.detail = [];
    this.intakeReq.quantity = 0;
    let position = 0;
    this.lstProductIntake.forEach(value => {
      this.intakeReq.amount += value.amountIntake;
      this.intakeReq.quantity += value.numberIntake;
      let obj: DetailIntake = {
        id: value.id,
        productId: value.productId,
        position: position,
        productName: value.productName,
        productCode: value.productCode,
        productProductUnitId: value.productProductUnitId,
        unitId: value.unitId,
        unitName: value.unit,
        quantity: value.numberIntake,
        unitPrice: value.purchasePrice,
        amount: value.amountIntake,
        discountAmount: 0,
        totalAmount: value.amountIntake,
      };
      this.intakeReq.detail?.push(obj);
      position++;
    });
    if (this.checkPercentOrValue) {
      this.intakeReq.discountAmount = take_decimal_number(Number(this.intakeReq.amount) * (this.percentInput / 100), 0);
    }
    this.intakeReq.totalAmount = Number(this.intakeReq.amount) - Number(this.intakeReq.discountAmount) + Number(this.intakeReq.costAmount);
  }

  resetIntakeReq() {
    this.intakeReq = new WarehouseIntakeReq();
    this.intakeReq.quantity = 0;
    this.intakeReq.amount = 0;
    this.intakeReq.discountAmount = 0;
    this.intakeReq.costAmount = 0;
    this.intakeReq.totalAmount = 0;
  }

  loadMoreCustomer() {
    this.filterCustomer.page++;
    this.getLstCustomer(true);
  }

  onChangeDiscount() {
    if (!this.checkPercentOrValue) {
      if (this.discountInput > this.intakeReq.amount) {
        this.toastr.error(
          this.translateService.instant('easyPos.warehouse.info.discountError1'),
          this.translateService.instant('easyPos.warehouse.info.message')
        );
      }
    } else {
      if (!this.percentInput) {
        this.discountInput = 0;
      }
      if (this.percentInput > 100) {
        this.toastr.error(
          this.translateService.instant('easyPos.warehouse.info.discountError2'),
          this.translateService.instant('easyPos.warehouse.info.message')
        );
      } else {
        this.discountInput = take_decimal_number((Number(this.percentInput) / 100) * this.intakeReq.amount, 0);
      }
    }
  }

  onSaveDiscount() {
    this.intakeReq.discountAmount = this.discountInput ? this.discountInput : 0;
    this.intakeReq.totalAmount = this.intakeReq.amount - Number(this.intakeReq.discountAmount) + Number(this.intakeReq.costAmount);
    this.formDiscountAmount.hide();
  }

  onSaveCostAmount() {
    this.intakeReq.costAmount = this.costInput ? this.costInput : 0;
    this.intakeReq.totalAmount = this.intakeReq.amount - Number(this.intakeReq.discountAmount) + Number(this.intakeReq.costAmount);
    this.formCostAmount.hide();
  }

  onDismisProductIntake(event, product: any) {
    event.stopPropagation();
    const index: number = this.lstProductIntake.indexOf(product);
    if (index !== -1) {
      this.lstProductIntake.splice(index, 1);
      this.activeRemoveProduct = 0;
    }
    let productDismis = this.lstProduct.find(item => item.productProductUnitId == product.productProductUnitId);
    if (productDismis) {
      productDismis.checked = false;
    }
    this.getAmount();
  }

  onCreateCustomer() {
    this.modalRef = this.modalService.open(CustomerSaveComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.isSuplier = true;
    this.modalRef.result.then(result => {
      this.lstCustomer = [result].concat(this.lstCustomer);
      this.intakeReq.customerId = result.id;
      this.intakeReq.customerName = result.name;
    });
  }

  onBack() {
    if (this.intakeReq.customerId || (this.intakeReq.detail && this.intakeReq.detail.length > 0)) {
      this.modalRef = this.modalService.open(ModalConfirmExitComponent, { size: 'lg', backdrop: 'static' });
      this.modalRef.closed.subscribe((res?: any) => {
        if (res === 1) {
          this.checkExit = true;
          this.formPaymentMethod.show();
        }
        if (res === 3) {
          this.router.navigate([`/` + RS_INOUT_WARD]);
        }
      });
    } else {
      this.router.navigate([`/` + RS_INOUT_WARD]);
    }
  }

  onCreateProduct() {
    this.openModalCreateProduct(0);
  }

  openModalCreateProduct(id: any) {
    this.modalRef = this.modalService.open(ModalCreateProductComponent, { size: 'xl', backdrop: 'static' });
    this.modalRef.componentInstance.id = id;
    this.modalRef.closed.subscribe((res?: any) => {
      if (res.data) {
        this.onSearch();
      }
    });
  }

  dismissCostAmount() {
    this.formCostAmount.hide();
  }

  dismissIntakeProduct() {
    this.formProductIntake.hide();
  }

  dismissDiscountAmount() {
    this.formDiscountAmount.hide();
  }

  togglePosSidebarFooter() {
    this.posSidebarFooterStatus = !this.posSidebarFooterStatus;
  }

  onMinusIntake(event, product: any) {
    event.stopPropagation();
    if (product.numberIntake <= 1) {
      this.activeRemoveProduct = product.productProductUnitId;
    }
    if (product.numberIntake > 1) {
      product.numberIntake = take_decimal_number(Number(product.numberIntake) - 1, 6);
      product.amountIntake = take_decimal_number(product.purchasePrice * product.numberIntake, 0);
    }
    this.getAmount();
  }

  onClickQuantity(event: any) {
    event.stopPropagation();
    this.getAmount();
  }

  onPlusIntake(event, product: any) {
    event.stopPropagation();
    if (this.intakeReq.type === 2 && this.overStock === 0) {
      if (product.inventoryCount - 1 <= product.numberIntake && product.numberIntake < product.inventoryCount) {
        product.numberIntake = product.inventoryCount
      } else if (product.numberIntake <= product.inventoryCount - 1) {
        product.numberIntake = Number(product.numberIntake) + 1;
      } else {
        this.toastr.error(
            'Sản phẩm ' + product.productName + ' vượt quá số lượng tồn kho' + ' (Tổng tồn kho: ' + product.inventoryCount + ' ' + (product.unit ? product.unit : '') + ')',
            'Thông báo'
        );
      }
    } else {
      product.numberIntake = Number(product.numberIntake) + 1;
    }
    product.amountIntake = take_decimal_number(Number(product.purchasePrice) * Number(product.numberIntake), 0);
    this.getAmount();
  }

  onChangeCost() {}

  onChoosePayMethod() {
    if (!this.intakeReq.customerId) {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.customerError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
      this.formPaymentMethod.hide();
    } else if (!this.intakeReq.detail) {
      this.toastr.error(
        this.translateService.instant('easyPos.warehouse.info.lstIntakeError'),
        this.translateService.instant('easyPos.warehouse.info.message')
      );
      this.formPaymentMethod.hide();
    } else {
      this.formPaymentMethod.show();
    }
  }

  closeMethodModal() {
    this.formPaymentMethod.hide();
  }

  searchCustomer() {
    this.keywordCustomer$
      .pipe(
        debounceTime(500),
        distinctUntilChanged(),
        tap(keyword => {
          if (keyword) {
            this.filterCustomer.keyword = keyword;
            this.filterCustomer.page = Page.PAGE_NUMBER;
          } else {
            this.filterCustomer.keyword = '';
          }
        }),
        switchMap(() => this.warehouseService.getAllCustomerPaging(this.filterCustomer))
      )
      .subscribe(res => {
        this.lstCustomer = res.body.data;
      });
  }

  cancel(event) {
    event.stopPropagation();
    this.activeRemoveProduct = 0;
  }

  onClickFormDelete(event) {
    event.stopPropagation();
  }

  changeTypeDiscount() {
    if (this.checkPercentOrValue) {
      this.discountInput = take_decimal_number(Number(this.intakeReq.amount) * (this.percentInput / 100), 0);
    }
  }

  onDeleteProduct(product: any) {
    if (product.numberIntake === 0 || !product.numberIntake) {
      const index: number = this.lstProductIntake.indexOf(product);
      if (index !== -1) {
        this.lstProductIntake.splice(index, 1);
        this.activeRemoveProduct = 0;
      }
      let productDismis = this.lstProduct.find(item => item.productProductUnitId == product.productProductUnitId);
      if (productDismis) {
        productDismis.checked = false;
      }
      this.getAmount();
    }
  }
  ngOnDestroy() {
    // super.ngOnDestroy();
    this.contentOption.isHiddenOrder = false;
  }

  /*
    update rs_inward
   */
  isUpdate: boolean = false;
  params: any;
  dataUpdate: any;
  dataDetailUpdate: any[] = [];
  totalAmountOld: number = 0;
  totalQuantityOld: number = 0;
  formConfirmUpdate: any;

  async getDataInit() {
    const id = Number(this.params.get('id'));
    await this.warehouseService.getDetail(id).subscribe(res => {
      if (res.ok) {
        this.isUpdate = true;
        const responseBody = ConvertResponse.getDataFromServer(res, false);
        this.dataUpdate = responseBody.data;

        let paymentOld = this.dataUpdate.paymentMethod;
        if (!this.paymentMethod.find(item => item.value === paymentOld)) {
          paymentOld = this.paymentMethod[this.paymentMethod.length - 1].value;
          this.paymentMethodInput = this.dataUpdate.paymentMethod;
        }
        this.totalAmountOld = this.dataUpdate.totalAmount;
        this.totalQuantityOld = this.dataUpdate.quantity;
        this.intakeReq = JSON.parse(JSON.stringify(this.intakeReq));
        this.intakeReq = {
          id: id,
          comId: this.lastCompany.id,
          billId: this.dataUpdate.billId,
          type: this.dataUpdate.type,
          businessType: null,
          typeDesc: this.dataUpdate.typeDesc,
          date: this.dataUpdate.date,
          customerName: this.dataUpdate.type === 2 ? this.dataUpdate.customerName : this.dataUpdate.supplierName,
          customerId: this.dataUpdate.type === 2 ? this.dataUpdate.customerId : this.dataUpdate.supplierId,
          quantity: this.dataUpdate.quantity,
          amount: this.dataUpdate.amount,
          discountAmount: this.dataUpdate.discountAmount,
          costAmount: this.dataUpdate.costAmount,
          totalAmount: this.dataUpdate.totalAmount,
          paymentMethod: paymentOld,
          description: this.dataUpdate.description,
          detail: [],
        };
        this.dataDetailUpdate = this.dataUpdate.detail;
        for (let item of this.dataDetailUpdate) {
          this.productIntake = item;
          this.productIntake.productId = item.productId;
          this.productIntake.id = item.id;
          this.productIntake.unit = item.unitName;
          this.productIntake.purchasePrice = item.unitPrice;
          this.productIntake.numberIntake = item.quantity;
          this.productIntake.amountIntake = take_decimal_number(this.productIntake.purchasePrice * item.quantity, 0);
          this.lstProductIntake.push(this.productIntake);
        }
        this.lstProductIntake = JSON.parse(JSON.stringify(this.lstProductIntake));
        this.intakeReq.detail = this.lstProductIntake;
        this.intakeReq = JSON.parse(JSON.stringify(this.intakeReq));
      } else {
        this.intakeReq.customerId = this.lstCustomer[0].id;
        this.intakeReq.customerName = this.lstCustomer[0].name;
        this.toastr.error(ConvertResponse.getDataFromServer(res, true));
      }
    });
  }

  protected readonly SPDVProduct = SPDVProduct;

  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SEARCH_LG = ICON_SEARCH_LG;
  protected readonly ICON_DELETE_SM = ICON_DELETE_SM;
}
