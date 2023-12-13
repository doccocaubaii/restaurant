import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, Renderer2, ViewChild } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import {
  DeliveryType,
  DiscountVat,
  InvoiceDiscount,
  InvoiceType,
  LIST_VAT,
  MenuType,
  NoTaxProduct,
  Page,
  SPCKProduct,
  SPGCProduct,
  SPKMProduct,
  SPVoucherProduct,
  StatusExit,
  StatusOrder,
  TaxReductionType,
  TypeDiscount,
  SPDVProduct,
  StatusNotify,
  VoucherType,
  VoucherApply,
  ProductNormal,
} from 'app/pages/const/customer-order.const';
import { Location } from '@angular/common';
import { ProductService } from '../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductGroupService } from '../service/product-group.service';
import { Subject, debounceTime, distinctUntilChanged, forkJoin, lastValueFrom, of, pluck, switchMap, tap } from 'rxjs';
import { IProduct, ITopping } from '../model/product.model';
import { IProductGroup } from '../model/product-group.model';
import { FormControl } from '@angular/forms';
import {
  changeProductSelected,
  generateRandomString,
  getPositiveNumber,
  numberOnly,
  take_decimal_number,
  updateOrder,
  updatePosition,
  validVariable,
  getProductNameSPGC,
  getQuantityProduct,
  updateVoucher,
  updateProductVoucherOrder,
  updateSPDVOrder,
  checkStatusSPKM,
  updateApplyProductPromoAndListProductPromo,
  convertVoucherUpdateToCreate,
  checkConditionNotify,
  addProductSelectedToList,
  removeProductPromoOrder,
  removeVoucherOrder,
  checkCancelSPKMOrder,
  checkProductVoucherOrder,
  updateProductQuantityReturnOrder,
} from 'app/pages/const/function';
import { CustomerService } from '../service/customer.service';
import { AreaService } from '../service/area.service';
import { AreaUnitService } from '../service/area-unit.service';
import { Customer } from '../model/customer.model';
import { IBillPayment, InvoiceConfiguration, LastCompany, Payment, ProductBill } from '../model/bill-payment.model';
import { BillService } from '../service/bill.service';
import dayjs from 'dayjs/esm';
import { ToastrService } from 'ngx-toastr';
import { ConfirmCheckoutComponent } from './confirm-checkout/confirm-checkout.component';
import { DiscountTaxOrderComponent } from './discount-tax-order/discount-tax-order.component';
import { FilterProduct } from '../model/filterProduct.model';
import { FilterProductGroup } from '../model/filterProductGroup.mode';
import { FilterCustomer } from '../model/filterCustomer.model';
import { FilterArea } from '../model/filterArea.model';
import { DiscountTaxProductComponent } from './discount-tax-product/discount-tax-product.component';
import { SetVatRateProductComponent } from './set-vat-rate-product/set-vat-rate-product.component';
import { BaseComponent } from 'app/shared/base/base.component';
import { IArea } from 'app/entities/area/area.model';
import { CustomerSaveComponent } from 'app/pages/customer/customer-save/customer-save.component';
import { PosInvoiceComponent } from './pos-invoice/pos-invoice.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { LoadingOption } from 'app/utils/loadingOption';
import { CheckDeactivate } from './check-deactivate';
import { ConfirmExitDialogComponent } from 'app/shared/modal/confirm-exit-dialog/confirm-exit-dialog.component';
import { ModalCreateProductComponent } from 'app/pages/product/modal-create-product/modal-create-product.component';
import { BILL, BILL_ORDER } from 'app/constants/app.routing.constants';
import { InvoiceService } from 'app/pages/invoice/service/invoice.service';
import { UtilsService } from 'app/utils/Utils.service';
import { ContentOption } from '../../../utils/contentOption';
import { VatRateDiscountOrderComponent } from './vat-rate-discount-order/vat-rate-discount-order.component';
import { VoucherOrderComponent } from './voucher-order/voucher-order.component';

import { ProductToppingListComponent } from './product-topping-list/product-topping-list.component';
import { ProductOrderToppingComponent } from './product-topping/product-topping.component';
import { ConvertPointComponent } from './convert-point/convert-point.component';
import { RemoveProductComponent } from 'app/shared/modal/remove-product/remove-product.component';
import { WebsocketService } from '../../../config/service/websocket-service.service';
import { ISocketConfigModel } from '../../../entities/socket/socket-config.model';
import { NotificationService } from 'app/pages/processing/notification.service';
import { TranslateService } from '@ngx-translate/core';
import { MqttService } from 'ngx-mqtt';
import { Md5Service, SocketMessage } from '../../../constants/socket.message.constant';
import { Authority } from '../../../config/authority.constants';
import { ICON_BAR_CODE, ICON_DELETE_SM, ICON_TOPPING, ICON_VOUCHER_BLUE } from '../../../shared/other/icon';
import { ProductVoucherQuantityComponent } from './product-voucher-quantity/product-voucher-quantity.component';

@Component({
  selector: 'customer-order',
  templateUrl: './customer-order.component.html',
  styleUrls: ['./customer-order.component.scss'],
})
export class PosCustomerOrderPage extends BaseComponent implements OnInit, OnDestroy, CheckDeactivate {
  appSettings = appSettings;
  posMobileSidebarToggled = true;
  deliveryType = DeliveryType;
  loadingProduct = true;
  indexOrder = 1;
  afterViewInit = false;
  @ViewChild('descriptionOrder') descriptionOrder: ElementRef;
  @ViewChild('searchProduct') scanBarCodeProduct: ElementRef;
  statusNotify = StatusNotify;
  updateAreaUnit = false;

  menuType: number = 1;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: 26, groupIds: [] };
  filterProductGroup: FilterProductGroup = { page: Page.PAGE_NUMBER, size: 40 };
  filterCustomer: FilterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', type: 1, totalPage: 0 };
  filterArea: FilterArea = { areaSize: Page.PAGE_SIZE, areaUnitSize: Page.PAGE_SIZE };
  filterVoucher = { customerId: 0, totalItem: 0 };
  query: FormControl = new FormControl('');
  keywordCustomer$ = new Subject<string>();

  listProduct: IProduct[] = [];
  listProductGroup: IProductGroup[] | null = [];
  listCustomer: Customer[] = [];
  listArea: IArea[] = [];
  listAreaShow: IArea[] = [];
  listVat = LIST_VAT;
  listOrder: IBillPayment[] = [];
  listOrderInitial: string;
  listVoucherInitial: any[];
  listVoucher: any = [];
  orderSelected!: IBillPayment;
  areaSelected?: IArea;
  listProductSelected?: IProduct[] = [];
  statusOrder = StatusOrder;

  SPCKProduct;
  SPVoucherProduct;
  SPGCProduct;
  SPDVProduct;
  productDiscountTaxOrder;
  productVoucherOrder;

  lastCompany: LastCompany;
  invoiceConfiguration: InvoiceConfiguration;
  statusDesciptionOrder = false;
  invoiceType = InvoiceType;
  invoiceDiscount = InvoiceDiscount;
  spgcProduct = SPGCProduct;
  spckProduct = SPCKProduct;
  spdvProduct = SPDVProduct;
  spkmProduct = SPKMProduct;
  spVoucherProduct = SPVoucherProduct;
  posSidebarFooterStatus = true;
  listOrderShowStatus = false;
  idOrderSelected: number;
  idTableSelected: number;
  statusOrderSelected: number;
  activeRemoveProductSelected = '';
  activeTotalProductGroup = false;
  activeBorderArea = 0;
  activeCreateOrder: boolean | null = null;
  runOutOfProduct = false;
  scanBarCode = false;
  listProductId = [];
  listProductGroupId = [];
  dialogRef: any;
  statusNotifyProduct = false;
  statusConnectingWebSocket: boolean;
  statusNotifyOrder = false;
  socketMessage: SocketMessage = {
    action: '',
    reason: '',
    data: null,
  };
  mqttSubscription: any;
  autoCallApi: any;
  authorProductAdd = Authority.PRODUCT_ADD;
  authorCustomerAdd = Authority.CUSTOMER_ADD;
  display: any;
  authorBillDone = Authority.BILL_DONE;
  authorBillAdd = Authority.BILL_ADD;
  authorBillUpdate = Authority.BILL_UPDATE;
  authorBillNotify = [Authority.BILL_ADD, Authority.BILL_UPDATE];
  returnOrder = false;

  constructor(
    private mqttService: MqttService,
    private websocketService: WebsocketService,
    protected notificationService: NotificationService,
    public utilService: UtilsService,
    protected productService: ProductService,
    protected customerService: CustomerService,
    protected areaService: AreaService,
    protected areaUnitService: AreaUnitService,
    protected productGroupService: ProductGroupService,
    protected activatedRoute: ActivatedRoute,
    protected orderService: BillService,
    public router: Router,
    public toast: ToastrService,
    protected modalService: NgbModal,
    public loading: LoadingOption,
    private translateService: TranslateService,
    private invoiceService: InvoiceService,
    public contentOption: ContentOption,
    private md5Service: Md5Service,
    private renderer: Renderer2,
    private location: Location
  ) {
    super();
    this.contentOption.isHiddenOrder = true;
    this.appSettings.appEmpty = true;
    this.appSettings.appContentFullHeight = true;
  }

  async ngOnInit() {
    this.getListNotification();
    this.websocketService.statusConnectingWebSocket$.subscribe(data => {
      this.statusConnectingWebSocket = data;
      if (data) {
        setTimeout(() => {
          this.statusConnectingWebSocket = false;
        }, 2000);
      }
    });

    this.menuType = this.utilService.getPosition('PB/BH') < this.utilService.getPosition('TD/BH') ? 1 : 0;
    this.searchCustomer();
    this.searchProduct();

    this.lastCompany = await this.getCompany();
    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
    this.invoiceConfiguration.displayConfig = JSON.parse(this.invoiceConfiguration.displayConfig);
    if (this.checkConditionNotify()) {
      this.websocketService.connect();
      this.websocketService.statusConnectingWebSocket$.subscribe(data => {
        this.statusConnectingWebSocket = data;
        if (data) {
          setTimeout(() => {
            this.statusConnectingWebSocket = false;
          }, 2000);
        }
      });
      this.mqttService.connect();
      let title = this.md5Service.convertToMd5(this.lastCompany.id + '/order');
      this.mqttService.observe(title).subscribe(message => {
        // Xử lý tin nhắn khi được nhận
        let dataReceive = JSON.parse(String.fromCharCode(...message.payload));
        this.getListNotification();
        if (this.idOrderSelected == dataReceive.data.refId) {
          this.orderService.find(this.idOrderSelected).subscribe((res: any) => {
            // this.orderSelected = res.body.data;
            this.orderSelected.products.forEach(productOrder => {
              res.body.data.products.forEach(productOrderNew => {
                if (productOrder.id == productOrderNew.id) {
                  productOrder.deliveredQuantity = productOrderNew.deliveredQuantity;
                  productOrder.processedQuantity = productOrderNew.processedQuantity;
                  productOrder.processingQuantity = productOrderNew.processingQuantity;
                }
              });
            });
          });
        }
      });
    }

    this.getListProduct();
    this.getListProductGroup();
    this.getListCustomer();
    this.activatedRoute.queryParamMap.subscribe((params: any) => {
      this.idOrderSelected = +params.get('id_order');
      this.idTableSelected = +params.get('table');
      this.statusOrderSelected = +params.get('status_order');
      if (this.statusOrderSelected == this.statusOrder.RETURNED) {
        this.returnOrder = true;
      }
      this.display = Number(params.get('display'));
      this.getListArea();
      if (this.idOrderSelected) {
        this.getOrderDetailById(this.idOrderSelected);
      } else {
        this.createOrder();
      }
    });
  }

  getListNotification() {
    let req = {
      page: Page.PAGE_NUMBER,
      size: Page.PAGE_SIZE,
      isUnRead: true,
      type: 0,
    };
    this.notificationService.getUnReadNotification(req).subscribe(res => {
      const notificationList = res.body.data;
      if (notificationList && notificationList.length > 0) {
        for (const element of notificationList) {
          this.toast.success(element.content);
          this.notificationService
            .updateNotification({
              id: element.id,
              readAll: false,
            })
            .subscribe();
        }
      }
    });
  }

  returnToOrderList() {
    this.router.navigate([`/${BILL}`]);
  }

  getListVoucher() {
    if (this.invoiceConfiguration.voucherApply === VoucherApply.useVoucher) {
      this.filterVoucher.customerId = this.orderSelected.customerId;
      this.customerService.getVoucher(this.filterVoucher).subscribe((res: any) => {
        this.orderSelected.listVoucher = res.body.data;
        this.listVoucher = this.orderSelected.listVoucher;
        this.getListProductId(this.listVoucher);
        if (this.listProductId.length || this.listProductGroupId.length) {
          this.getProductFromProductId(this.listProductId, this.listProductGroupId);
        } else {
          this.updateOrder();
        }
        this.filterVoucher.totalItem = res.body.count;
      });
    }
  }

  checkApplySPKMOrder() {
    this.productVoucherOrder = this.orderSelected.products.find(product => product.productCode == SPVoucherProduct.code);
    let voucherApply;
    let productVoucherApply;
    let voucherAmountApply = 0;
    if (!this.listVoucher) return;
    for (const voucher of this.listVoucher) {
      if (voucher.differentExtConditions?.autoApplyVoucher) {
        for (const productVoucher of voucher.conditions) {
          if (productVoucher.status) {
            switch (voucher.type) {
              case VoucherType.type100:
                if (!voucher.check) {
                  this.productVoucherOrder = this.addVoucherOrder();
                  if (this.invoiceConfiguration.combineVoucherApply) {
                    voucher.check = true;
                    let voucherAmountOrder;
                    if (productVoucher.discountPercent && !productVoucher.discountType) {
                      voucherAmountOrder = (this.orderSelected.amount * productVoucher.discountPercent) / 100;
                    }
                    this.orderSelected.vouchers.push({
                      id: voucher.id,
                      code: voucher.code,
                      voucherValue: voucherAmountOrder || productVoucher.discountValue,
                      voucherPercent: productVoucher.discountPercent || 0,
                      desc: productVoucher.desc,
                      type: voucher.type,
                      minValue: productVoucher.minValue || 0,
                      maxValue: productVoucher.maxValue || 0,
                    });
                  } else {
                    if (!this.orderSelected.vouchers.length) {
                      this.productVoucherOrder = this.addVoucherOrder();
                      let voucherAmountOrder;
                      if (productVoucher.discountPercent) {
                        voucherAmountOrder = (this.orderSelected.amount * productVoucher.discountPercent) / 100;
                      } else {
                        voucherAmountOrder = productVoucher.discountValue;
                      }
                      if (voucherAmountOrder > voucherAmountApply) {
                        voucherApply = voucher;
                        productVoucherApply = productVoucher;
                        voucherAmountApply = voucherAmountOrder;
                      }
                    }
                  }
                }
                break;
              default:
            }
          }
        }
      }
    }
    if (!this.invoiceConfiguration.combineVoucherApply && voucherApply && !this.orderSelected.vouchers.length) {
      this.orderSelected.vouchers.push({
        id: voucherApply.id,
        code: voucherApply.code,
        voucherValue: voucherAmountApply || productVoucherApply.discountValue,
        voucherPercent: productVoucherApply.discountPercent || 0,
        desc: productVoucherApply.desc,
        type: voucherApply.type,
        minValue: productVoucherApply.minValue || 0,
        maxValue: productVoucherApply.maxValue || 0,
      });
      voucherApply.check = true;
    }
    return this.productVoucherOrder;
  }

  getListProductId(listVoucher) {
    this.listProductId = [];
    this.listProductGroupId = [];
    listVoucher?.forEach(voucher => {
      voucher.conditions?.forEach(productVoucher => {
        if (productVoucher.getProductProductUnitId?.length) {
          this.listProductId = this.listProductId.concat(productVoucher.getProductProductUnitId);
        }
        if (productVoucher.getProductGroupId?.length) {
          this.listProductGroupId = this.listProductGroupId.concat(productVoucher.getProductGroupId);
        }
        if (productVoucher.buyProductGroupId?.length) {
          this.listProductGroupId = this.listProductGroupId.concat(productVoucher.buyProductGroupId);
        }
      });
    });
    this.listProductId = [...new Set(this.listProductId)];
    this.listProductGroupId = [...new Set(this.listProductGroupId)];
  }

  getProductFromProductId(listProductId, listProductGroupId) {
    let listObservable: any[] = [];
    if (listProductId.length) {
      listObservable.push(this.customerService.getProductFromProductId({ ids: listProductId, comId: this.lastCompany.id }));
    } else {
      listObservable.push(of(null));
    }
    listProductGroupId.length &&
      listObservable.push(this.customerService.getProductGroupFromProductGroupId({ ids: listProductGroupId, comId: this.lastCompany.id }));

    if (listProductId.length || listProductGroupId.length) {
      forkJoin(listObservable).subscribe((response: any[]) => {
        this.listVoucher?.forEach(voucher => {
          voucher.conditions?.forEach(productVoucher => {
            productVoucher.applyProductVoucher = [];
            productVoucher.newGetQuantity = productVoucher.getQuantity;
            productVoucher.listProductPromo = productVoucher.listProductPromo || [];
            if (response[0]) {
              productVoucher.getProductProductUnitId?.forEach(productPromoId => {
                let productPromoItem = response[0].body.data.find(product => product.productProductUnitId == productPromoId);
                if (productPromoItem) {
                  productPromoItem = JSON.parse(JSON.stringify(productPromoItem));
                  productVoucher.listProductPromo.push(productPromoItem);
                  if (productVoucher.type == VoucherType.type200) {
                    this.getPricePromoProduct(productVoucher, productPromoItem);
                  }
                }
              });
            }
            if (response[1]) {
              productVoucher.getProductGroupId?.forEach(productGroupId => {
                JSON.parse(JSON.stringify(response[1].body.data)).forEach(productGroupRes => {
                  if (productGroupId == productGroupRes.id) {
                    productGroupRes.products.forEach(productItem => {
                      productVoucher.getProductProductUnitId.push(productItem.productProductUnitId);
                      if (productVoucher.type == VoucherType.type200) {
                        this.getPricePromoProduct(productVoucher, productItem);
                      }
                    });
                    // let listProductPromo = JSON.parse(JSON.stringify(productGroupRes.products));
                    productVoucher.listProductPromo = productVoucher.listProductPromo.concat(productGroupRes.products);
                  }
                });
              });

              productVoucher.buyProductGroupId?.forEach(productGroupId => {
                response[1].body.data.forEach(productGroupRes => {
                  if (productGroupId == productGroupRes.id) {
                    productGroupRes.products.forEach(productItem => {
                      productVoucher.buyProductProductUnitId.push(productItem.productProductUnitId);
                    });
                  }
                });
              });
            }
          });
        });
        this.listVoucherInitial = JSON.parse(JSON.stringify(this.listVoucher));
        this.updateOrder();
        updateApplyProductPromoAndListProductPromo(this.orderSelected, this.listVoucher);
        this.listOrderInitial = JSON.stringify(this.listOrder);
        this.resetQuantityProductOrderReturn(this.orderSelected);
      });
    }
  }

  resetQuantityProductOrderReturn(orderSelected) {
    if (this.returnOrder) {
      orderSelected.products.forEach(productSelected => {
        productSelected.quantity = 0;
        productSelected.displayQuantity = 0;
        // productSelected.toppings?.forEach(productTopping => {
        //   productTopping.quantity = 0;
        //   productTopping.displayQuantity = 0;
        //   changeProductSelected(productTopping, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
        // });
        productSelected.voucherProducts?.forEach(productVoucher => {
          productVoucher.quantity = 0;
          productVoucher.displayQuantity = 0;
          this.changeProductSelected(productVoucher);
        });
        this.changeProductSelected(productSelected);
      });
      // this.updateOrder();
    }
  }

  getPricePromoProduct(productVoucher, productItem) {
    if (productVoucher.discountPercent) {
      let salePricePromo = take_decimal_number(
        productItem.salePrice - (productItem.salePrice * productVoucher.discountPercent) / 100,
        this.lastCompany.roundScaleUnitPrice
      );
      productItem.salePricePromo = salePricePromo < 0 ? 0 : salePricePromo;
    }
    if (productVoucher.discountValue) {
      let salePricePromo = productItem.salePrice - productVoucher.discountValue;
      productItem.salePricePromo = salePricePromo < 0 ? 0 : salePricePromo;
    }
  }

  cancelOrder() {
    this.listOrderInitial = JSON.stringify(this.listOrder);
    this.router.navigate([`/${BILL}`]);
  }

  checkDeactivate() {
    if (this.listOrderInitial === JSON.stringify(this.listOrder)) {
      return of(true);
    } else {
      const resultSubject: Subject<boolean> = new Subject<boolean>();
      const dialogRef = this.modalService.open(ConfirmExitDialogComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.value = new DialogModal(
        ModalHeader.CONFIRM_EXIT_ORDER,
        ModalContent.CONFIRM_EXIT_ORDER,
        ModalBtn.CONFIRM_EXIT_ORDER,
        'check',
        'btn-success'
      );
      dialogRef.componentInstance.formSubmit.subscribe(async res => {
        if (res && res === StatusExit.SAVE) {
          if (this.orderSelected.id) {
            const result: any = await lastValueFrom(
              this.orderService.update({
                ...this.orderSelected,
                billDate: dayjs(),
              })
            );
            if (result.status) {
              dialogRef.componentInstance.dismiss();
              resultSubject.next(!!res);
              resultSubject.complete();
              this.toast.success(result.body.reason);
            }
          } else {
            const result: any = await lastValueFrom(
              this.orderService.create({
                ...this.orderSelected,
                billDate: dayjs(),
              })
            );
            if (result.status) {
              dialogRef.componentInstance.dismiss();
              resultSubject.next(!!res);
              resultSubject.complete();
              this.toast.success(result.body.reason);
            }
          }
        } else {
          dialogRef.componentInstance.dismiss();
          resultSubject.next(!!res);
          resultSubject.complete();
        }
      });
      return resultSubject;
    }
  }

  searchProduct() {
    this.query.valueChanges
      .pipe(
        debounceTime(500),
        tap(value => {
          this.filterProduct.keyword = value;
        })
      )
      .subscribe(() => {
        if (!this.scanBarCode) {
          this.listProduct = [];
          this.filterProduct.page = 0;
          this.getListProduct();
        }
      });
  }

  searchProductFunction(event?: any) {
    this.filterProduct.keyword = event.target.value || '';
    this.filterProduct.page = 0;
    this.listProduct = [];
    this.getListProduct();
  }

  toggleScanBarCode() {
    this.scanBarCode = !this.scanBarCode;
    if (this.scanBarCode) {
      this.toast.success('Chế độ tìm kiếm barcode');
    } else {
      this.toast.success('Chế độ tìm kiếm thường');
    }
    this.scanBarCodeProduct.nativeElement.focus();
    if (!this.filterProduct.keyword) {
      this.searchProductFunction();
    }
  }

  getOrderDetailById(id) {
    this.orderService.find(id).subscribe((res: any) => {
      this.orderSelected = res.body.data;
      convertVoucherUpdateToCreate(this.orderSelected);
      this.orderSelected.billDate = dayjs(this.orderSelected.billDate);
      this.orderSelected.typeInv = this.invoiceConfiguration.invoiceType;
      if (!this.orderSelected.voucherReturns) {
        this.orderSelected.voucherReturns = [];
      }
      this.orderSelected.countProduct = 0;
      let productVoucherOrder = this.orderSelected.products.find(productOrder => productOrder.productCode == SPVoucherProduct.code);
      if (productVoucherOrder) {
        this.productVoucherOrder = productVoucherOrder;
        if (productVoucherOrder.productName.indexOf(this.orderSelected.vouchers[0].desc) < 0) {
          productVoucherOrder.productNameCustom = productVoucherOrder.productName;
        }
      }
      if (!this.orderSelected.extraConfig) {
        this.orderSelected.extraConfig = {
          svc5: 0,
        };
      }
      // this.orderSelected.productReturns?.forEach(productReturn => {
      //   productReturn.productUniqueId = generateRandomString();
      // })
      this.orderSelected.products.forEach(product => {
        product.quantityInitial = product.quantity;
        !product.voucherProducts && (product.voucherProducts = []);
        product.productUniqueId = generateRandomString();
        product.voucherProducts?.forEach(productVoucher => {
          productVoucher.parentProductUniqueId = product.productUniqueId;
          productVoucher.quantityInitial = productVoucher.quantity;
          productVoucher.productUniqueId = generateRandomString();
        });
        product.toppings?.forEach(productTopping => {
          productTopping.quantityInitial = productTopping.quantity;
        });
        this.updateOrderConfiguration(
          product,
          this.orderSelected,
          this.invoiceConfiguration.invoiceType,
          this.invoiceConfiguration.typeDiscount,
          this.invoiceConfiguration.discountVat
        );
        if (product.productCode == SPCKProduct.code) {
          this.productDiscountTaxOrder = product;
          product.typeDiscount = TypeDiscount.VALUE;
        }
      });
      updateProductQuantityReturnOrder(this.orderSelected);
      this.listOrder.push(this.orderSelected);
      let listOrderLocal;
      this.listOrderInitial && (listOrderLocal = JSON.parse(this.listOrderInitial));
      if (listOrderLocal?.length) {
        let index = listOrderLocal.findIndex(order => order.id == this.orderSelected.id);
        if (index >= 0) {
          listOrderLocal[index] = this.orderSelected;
        } else {
          listOrderLocal = [this.orderSelected];
        }
      } else {
        listOrderLocal = [this.orderSelected];
      }
      this.listOrderInitial = JSON.stringify(listOrderLocal);
      this.getListVoucher();
      this.checkStatusNotifyProduct();
    });
  }

  updateOrderConfiguration(product, order, invoiceType, invoiceDiscount, discountVat?: number) {
    if (product.feature === 1) {
      if (invoiceType === InvoiceType.invoiceSale) {
        if (discountVat !== 1) {
          product.vatRate = -1;
          order.vatRate = -1;
          order.vatAmount = 0;
        }
      }
      if (invoiceType === InvoiceType.invoiceOne) {
        product.vatRate = order.vatRate;
      }
      if (invoiceType === InvoiceType.invoiceMultiple) {
        order.vatRate = 0;
      }
      if (invoiceDiscount === InvoiceDiscount.noDiscount) {
        product.discountAmount = 0;
        order.discountAmount = 0;
      }
      if (invoiceDiscount === InvoiceDiscount.productValue) {
        order.discountAmount = 0;
      }
      if (invoiceDiscount === InvoiceDiscount.orderValue) {
        product.discountAmount = 0;
      }
    } else {
      if (product.productCode == SPCKProduct.code) {
        if (invoiceDiscount === InvoiceDiscount.noDiscount || invoiceDiscount === InvoiceDiscount.productValue) {
          order.products = order.products.filter(product => product.productCode !== SPCKProduct.code);
        } else {
          if (invoiceType === InvoiceType.invoiceSale) {
            product.vatRate = -1;
          }
          if (invoiceType === InvoiceType.invoiceOne) {
            product.vatRate = order.vatRate;
          }
        }
      }
      if (invoiceType !== InvoiceType.invoiceSale || discountVat !== DiscountVat.discount) {
        order.products = order.products.filter(product => product.feature !== SPGCProduct.feature);
        order.discountVatRate = 0;
        order.discountVatAmount = 0;
        order.haveDiscountVat = false;
      }
    }
    if (product.feature == 1 && product.productCode !== SPDVProduct.code) {
      this.changeProductSelected(product);
    }
    if (product.feature == 1 && product.productCode === SPDVProduct.code) {
      this.orderSelected.checkSPDV = true;
      this.updateSPDV();
      this.updateOrder();
    }
  }

  trackId = (_index: number, item: IProduct): number => item.id;

  getListProductGroup(): void {
    this.productGroupService.query(this.filterProductGroup).subscribe({
      next: res => {
        this.afterViewInit = true;
        this.listProductGroup = res.body;
        this.listProductGroup?.forEach(productGroup => {
          productGroup.status = false;
        });
      },
    });
  }

  getListProduct() {
    this.loadingProduct = true;
    let compareProduct;
    this.productService.query(this.filterProduct).subscribe(res => {
      this.loadingProduct = false;
      const result = res.body;
      if (!result?.length) {
        this.runOutOfProduct = true;
      }
      result?.forEach(item => {
        if (item.productCode == SPCKProduct.code) {
          this.SPCKProduct = item;
        }
        if (item.productCode == SPVoucherProduct.code) {
          this.SPVoucherProduct = item;
        }
        if (item.productCode == SPGCProduct.code) {
          this.SPGCProduct = item;
        }
        if (item.productCode == SPDVProduct.code) {
          this.SPDVProduct = item;
        }
        if (item.isPrimary) {
          compareProduct = item;
        }
        this.listProduct?.push(item);
      });
      this.scanBarCode && this.scanBarCodeProduct.nativeElement.select();
      if (this.scanBarCode && result?.length) {
        let multipleProduct = false;
        result.forEach(product => {
          if (product.productId !== compareProduct.productId) {
            multipleProduct = true;
          }
        });
        if (!multipleProduct) {
          this.addProductToCustomerOrder(compareProduct);
        }
      }
    });
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
        switchMap(() => this.customerService.query(this.filterCustomer))
      )
      .subscribe(res => {
        this.listCustomer = res.body;

        if (!res.body.length) {
          const nullCustomer = {
            id: 0,
            comId: 0,
            name: 'Không tồn tại khách hàng',
            code2: '',
            type: 0,
            address: '',
            city: '',
            district: '',
            phoneNumber: '',
            email: '',
            taxcode: '',
            idNumber: '',
            description: '',
            disabled: true,
          };
          this.listCustomer.push(nullCustomer);
        }
      });
  }

  loadMoreCustomer() {
    this.filterCustomer.page++;
    this.getListCustomer();
  }

  getListCustomer() {
    this.customerService.query(this.filterCustomer).subscribe(res => {
      this.listCustomer = this.listCustomer.concat(res.body);
      if (!res.body.length) {
        this.filterCustomer.page -= 1;
      }
      if (this.orderSelected && !this.orderSelected.customerId) {
        this.orderSelected.customerId = res.body[0].id;
        this.orderSelected.customerName = res.body[0].name;
        this.orderSelected.pointBalanceCustomer = res.body[0].pointBalance || 0;
        this.orderSelected.moneyBalanceCustomer = res.body[0].moneyBalance || 0;
        this.orderSelected.cardCustomerInfo = res.body[0].cardInformation;
        this.listOrder.length === 1 && (this.listOrderInitial = JSON.stringify(this.listOrder));
        this.getListVoucher();
      }
    });
  }

  changeCustomer() {
    this.listCustomer.forEach(customer => {
      if (customer.id === this.orderSelected.customerId) {
        this.orderSelected.customerName = customer.name;
        this.orderSelected.pointBalanceCustomer = customer.pointBalance || 0;
        this.orderSelected.moneyBalanceCustomer = customer.moneyBalance || 0;
        this.orderSelected.cardCustomerInfo = customer.cardInformation;
      }
    });
    this.resetCustomer();
    this.getListVoucher();
  }

  resetCustomer() {
    this.orderSelected.vouchers = [];
    this.orderSelected.voucherAmount = 0;
    this.orderSelected.products = this.orderSelected.products.filter(
      product => product.productCode !== SPVoucherProduct.code && product.feature !== SPKMProduct.feature
    );
    this.productVoucherOrder = {};
    this.orderSelected.products.forEach(productSelected => {
      productSelected.voucherProducts = [];
    });
    this.updateSPDV();
    this.updateOrder();
  }

  getListArea() {
    this.areaService.query(this.filterArea).subscribe(res => {
      this.listArea = res.body;
      this.listAreaShow = this.listArea;
      this.areaSelected = this.listArea[0];
      if (this.idTableSelected) {
        this.listArea.forEach(area => {
          area.units.forEach(areaUnit => {
            if (areaUnit.id === this.idTableSelected) {
              this.orderSelected.areaId = area.id;
              this.orderSelected.areaName = area.name;
              this.orderSelected.areaUnitId = areaUnit.id;
              this.orderSelected.areaUnitName = areaUnit.name;
              this.orderSelected.deliveryType = DeliveryType.AT_TABLE;
            }
          });
        });
      }
    });
  }

  enterListCustomer(event) {
    event.preventDefault();
  }

  getListAreaUpdate() {
    this.areaService.query(this.filterArea).subscribe(res => {
      this.listArea = res.body;
      if (this.activeBorderArea) {
        this.listAreaShow = this.listArea.filter(area => area.id === this.activeBorderArea);
      } else {
        this.listAreaShow = this.listArea;
      }
      // this.activeBorderArea = this.areaSelected.id;
    });
  }

  modifyDeliverytypeOrder() {
    this.orderSelected.areaId = NaN;
    this.orderSelected.areaName = '';
    this.orderSelected.areaUnitId = NaN;
    this.orderSelected.areaUnitName = '';
  }

  async createOrder(event?) {
    const order: IBillPayment = new IBillPayment();
    order.payment = new Payment();
    order.deliveryType = DeliveryType.GET_AWAY;
    order.taxAuthorityCode = '00-00-00000-00000000000';
    order.billDate = dayjs();
    order.status = StatusOrder.NOT_COMPLETE;
    order.products = [];
    order.countProduct = 0;
    order.vatRate = 0;
    order.amount = 0;
    order.discountAmount = 0;
    order.totalPreTax = 0;
    order.vatAmount = 0;
    order.totalAmount = 0;
    order.vouchers = [];
    order.voucherAmount = 0;
    order.quantity = 0;
    order.typeInv = this.invoiceConfiguration.invoiceType || 0;
    order.checkboxVatRateDiscountProduct = false;
    order.vatRateDiscountProductName = '';
    order.haveDiscountVat = false;
    order.checkSPDV = false;
    order.listVoucher = this.listVoucherInitial && JSON.parse(JSON.stringify(this.listVoucherInitial));
    order.extraConfig = {
      svc5: 0,
    };

    this.listVoucher = order.listVoucher;

    if (!this.invoiceConfiguration) {
      const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
      this.invoiceConfiguration = invoiceConfiguration.data;
    }

    if (this.invoiceConfiguration.serviceChargeConfig && this.invoiceConfiguration.invoiceType == this.invoiceType.invoiceMultiple) {
      order.checkSPDV = true;
    }

    if (
      this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale &&
      this.invoiceConfiguration.discountVat == 1 &&
      this.invoiceConfiguration.taxReductionType == 0
    ) {
      order.discountVatRate = 0;
    }

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale) {
      order.vatRate = -1;
    }

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceOne) {
      order.discountVatRate = null;
      order.discountVatAmount = null;
    }

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple) {
      order.vatRate = 0;
      order.discountVatRate = null;
      order.discountVatAmount = null;
    }
    order.code = 'ĐH ' + this.indexOrder.toString();
    this.indexOrder++;
    if (this.listCustomer.length) {
      order.customerId = this.listCustomer[0].id;
      order.customerName = this.listCustomer[0].name;
    }
    this.getTaxAuthorityCode(order);
    this.orderSelected = order;
    if (event) {
      this.changeCustomer();
    }
    this.resetSPCKAndSPVoucherProduct();
    this.updateOrder();
    this.listOrder.push(this.orderSelected);
  }

  resetSPCKAndSPVoucherProduct() {
    this.productDiscountTaxOrder = this.orderSelected.products.find(product => product.productCode == this.spckProduct.code);
    this.productVoucherOrder = this.orderSelected.products.find(product => product.productCode == this.spVoucherProduct.code);
  }

  async getTaxAuthorityCode(order) {
    const invoiceParttern = this.lastCompany.invoicePattern ? this.lastCompany.invoicePattern.substring(0, 1) : '1';
    const taxAuthCodePrefix = this.lastCompany.taxAuthCodePrefix ? this.lastCompany.taxAuthCodePrefix : '00000';
    const deviceCode = this.lastCompany.deviceCode ? this.lastCompany.deviceCode : '000';
    order.comId = this.lastCompany.id;
    if (this.lastCompany.invoicePattern && this.lastCompany.taxAuthCodePrefix && this.lastCompany.deviceCode) {
      order.taxAuthorityCode =
        'M' +
        invoiceParttern +
        '-' +
        (new Date().getFullYear() % 2000) +
        '-' +
        taxAuthCodePrefix +
        '-' +
        deviceCode +
        Math.floor(Date.now()).toString().substr(-8, 8);
    }
    this.listOrder.length === 1 && (this.listOrderInitial = JSON.stringify(this.listOrder));
    // order.taxAuthorityCode = `M${invoicePerttern.toString()}-${new Date().getFullYear()%2000}-${taxAuthCodePrefix}-${deviceCode}${deviceCode + Math.floor(Date.now() / 100000)}`
  }

  checkConditionNotify() {
    this.statusNotifyOrder = checkConditionNotify(this.invoiceConfiguration);
    return this.statusNotifyOrder;
  }

  saveOrder(statusConfirmDialog = true) {
    if (statusConfirmDialog) {
      if (!this.checkOrder(this.orderSelected)) {
        return;
      }
    }

    updatePosition(this.orderSelected);
    checkProductVoucherOrder(this.orderSelected);

    if (!statusConfirmDialog || checkConditionNotify(this.invoiceConfiguration)) {
      this.saveOrderDetail(statusConfirmDialog);
      return;
    }

    this.dialogRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    this.dialogRef.componentInstance.value = new DialogModal(
      ModalHeader.SAVE_ORDER,
      ModalContent.SAVE_ORDER,
      ModalBtn.SAVE_ORDER,
      'check',
      'btn-save'
    );
    // @ts-ignore
    this.dialogRef.componentInstance.formSubmit.subscribe(async res => {
      if (res) {
        this.dialogRef.close();
        this.saveOrderDetail(statusConfirmDialog);
      }
    });
  }

  checkProcessingOrder(orderSelected) {
    for (let i = 0; i < orderSelected.products.length; i++) {
      if (orderSelected.products[i].quantity > (orderSelected.products[i].quantityInitial || 0)) {
        return true;
      }
      for (let j = 0; j < orderSelected.products[i]?.voucherProducts.length; j++) {
        if (orderSelected.products[i]?.voucherProducts[j].quantity > (orderSelected.products[i]?.voucherProducts[j].quantityInitial || 0)) {
          return true;
        }
      }
    }
    return false;
  }

  pushNotify() {
    let title = this.md5Service.convertToMd5(this.lastCompany.id + '/kitchen');
    this.socketMessage.action = 'UPDATE-ORDER';
    this.mqttService.publish(title, JSON.stringify(this.socketMessage)).subscribe(
      () => {},
      error => {}
    );
  }
  checkCancelProductPromoOrder(orderSelected) {
    let orderSelectedInitial;
    let listProductPromoInitial: any[] = [];
    let listProductPromo: any[] = [];
    orderSelectedInitial = JSON.parse(this.listOrderInitial).find(order => (order.id || 0) == this.orderSelected.id);
    orderSelectedInitial.products.forEach(productSelected => {
      productSelected.voucherProducts?.forEach(productPromo => {
        listProductPromoInitial.push(productPromo);
      });
      if (productSelected.feature == SPKMProduct.feature) {
        listProductPromoInitial.push(productSelected);
      }
    });

    orderSelected.products.forEach(productSelected => {
      productSelected.voucherProducts?.forEach(productPromo => {
        listProductPromo.push(productPromo);
      });
      if (productSelected.feature == SPKMProduct.feature) {
        listProductPromoInitial.push(productSelected);
      }
    });

    for (const productPromoInitial of listProductPromoInitial) {
      let findProductPromo = false;
      for (const productPromo of listProductPromo) {
        if (productPromo.quantity < productPromo.quantityInitial) {
          return false;
        }
        if (productPromoInitial.productUniqueId == productPromo.productUniqueId) {
          findProductPromo = true;
        }
      }
      if (!findProductPromo) {
        return false;
      }
    }
    return true;
  }

  async saveOrderDetail(statusConfirmDialog) {
    let listVoucherId = this.orderSelected.vouchers?.map(voucher => voucher.id);
    if (listVoucherId?.length) {
      let checkVoucher: any = await lastValueFrom(this.customerService.checkVoucher(listVoucherId, this.orderSelected.customerId));
      if (checkVoucher.body.data) {
        this.toast.warning('Khuyến mại voucher đã hết hạn, vui lòng kiểm tra lại');
        return;
      }
    }
    if (this.orderSelected.id) {
      if (!this.orderSelected.products?.length) {
        this.orderService
          .cancel({
            billCode: this.orderSelected.code,
            billId: this.orderSelected.id,
          })
          .subscribe((res: any) => {
            let itemSendSocket: ISocketConfigModel = new ISocketConfigModel();
            itemSendSocket.message = 'Thông báo bếp' + this.orderSelected.code;
            itemSendSocket.type = this.statusNotify.cancel;
            itemSendSocket.refId = this.orderSelected.id.toString();
            itemSendSocket.reason = 'Gửi tool in in hộ';
            itemSendSocket.comId = this.lastCompany.id;
            this.websocketService.sendMessage(itemSendSocket);
            this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
            this.listOrderInitial = JSON.stringify(this.listOrder);
            this.router.navigate([`/${BILL_ORDER}`], {
              relativeTo: this.activatedRoute,
              queryParams: {
                display: 1,
              },
            });
            this.toast.success(res.body?.reason);
          });
        return;
      }
      this.orderService.update({ ...this.orderSelected }).subscribe(
        (res: any) => {
          statusConfirmDialog = this.checkCancelProductPromoOrder(this.orderSelected) && statusConfirmDialog;
          this.listOrder = this.listOrder.filter(order => order.id !== this.orderSelected.id);
          this.toast.success(res.body.reason);
          if (checkConditionNotify(this.invoiceConfiguration)) {
            let itemSendSocket: ISocketConfigModel = new ISocketConfigModel();
            itemSendSocket.message = 'Thông báo bếp' + this.orderSelected.code;
            itemSendSocket.type = this.statusNotify.processing;
            itemSendSocket.refId = this.orderSelected.id.toString();
            itemSendSocket.reason = 'Gửi tool in in hộ';
            itemSendSocket.comId = this.lastCompany.id;
            if (this.checkProcessingOrder(this.orderSelected)) {
              this.websocketService.sendMessage(itemSendSocket);
            }

            if (!statusConfirmDialog) {
              this.websocketService.sendMessage({ ...itemSendSocket, type: this.statusNotify.cancel });
            }
            this.getOrderDetailById(this.idOrderSelected);
          } else {
            this.listOrderInitial = JSON.stringify(this.listOrder);
            this.router.navigate([`/${BILL}`]);
          }
          // this.checkStatusNotifyProduct();
        },
        error => {
          this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
          this.getOrderDetailById(this.idOrderSelected);
        }
      );
    } else {
      this.getTaxAuthorityCode(this.orderSelected);
      this.orderService.create({ ...this.orderSelected }).subscribe((res: any) => {
        this.orderService.find(res.body.data.billId).subscribe((result: any) => {
          if (checkConditionNotify(this.invoiceConfiguration)) {
            this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
            this.toast.success(res.body.reason);
            this.router.navigate([`/${BILL_ORDER}`], {
              relativeTo: this.activatedRoute,
              queryParams: {
                id_order: result.body.data.id,
                display: 1,
              },
            });
            const item = result.body.data;
            let itemSendSocket: ISocketConfigModel = new ISocketConfigModel();
            itemSendSocket.message = 'Thông báo bếp' + item.code;
            itemSendSocket.type = this.statusNotify.processing;
            itemSendSocket.refId = item.id;
            itemSendSocket.reason = 'Gửi tool in in hộ';
            itemSendSocket.comId = this.lastCompany.id;
            this.websocketService.sendMessage(itemSendSocket);
            return;
          }

          const posInvoiceRef = this.modalService.open(PosInvoiceComponent, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'margin-5',
          });
          posInvoiceRef.componentInstance.orderSelected = result.body.data;
          posInvoiceRef.closed.subscribe(() => {
            this.toast.success(res.body.reason);
            this.handleViewAfterCreateOrder();
          });
        });
      });
    }
  }

  handleViewAfterCreateOrder() {
    this.getListArea();
    this.listOrder = this.listOrder.filter(order => order.code !== this.orderSelected.code);
    this.productDiscountTaxOrder = {};
    this.productVoucherOrder = {};
    if (this.listOrder.length) {
      this.orderSelected = this.listOrder[0];
      this.listVoucher = this.orderSelected.listVoucher;
      this.resetSPCKAndSPVoucherProduct();
    } else {
      this.createOrder();
    }
    this.listOrderInitial = JSON.stringify(this.listOrder);
  }

  async openConfirmCheckoutForm() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.returnOrder = this.returnOrder;
    if (this.idOrderSelected) {
      dialogRef.componentInstance.statusUpdateOrder = true;
    }
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.orderService.find(res[0]).subscribe((result: any) => {
          const posInvoiceRef = this.modalService.open(PosInvoiceComponent, {
            size: 'lg',
            backdrop: 'static',
            windowClass: 'margin-5',
          });
          posInvoiceRef.componentInstance.orderSelected = result.body.data;
          posInvoiceRef.componentInstance.type = 1;
          posInvoiceRef.closed.subscribe(() => {
            this.listProduct = [];
            this.getListProduct();
            this.filterCustomer.page = 0;
            this.listCustomer = [];
            this.getListCustomer();
            this.toast.success(res[1]);
            this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
            this.productDiscountTaxOrder = {};
            this.productVoucherOrder = {};
            if (this.idOrderSelected) {
              this.router.navigate([`/${BILL_ORDER}`], {
                relativeTo: this.activatedRoute,
                queryParams: {
                  display: 1,
                },
              });
              return;
            }
            if (this.listOrder.length) {
              this.orderSelected = this.listOrder[0];
            } else {
              this.createOrder();
            }
            this.listOrderInitial = JSON.stringify(this.listOrder);
          });
        });
      }
    });
  }

  checkOrder(orderSelected: IBillPayment) {
    const listProperty = ['customerId', 'products'];
    const listValue = ['khách hàng', 'sản phẩm'];
    if (this.orderSelected.deliveryType === DeliveryType.AT_TABLE) {
      listProperty.push('areaUnitId');
      listValue.push('bàn');
    }
    for (let i = 0; i < listProperty.length; i++) {
      if (!validVariable(orderSelected[listProperty[i]])) {
        this.toast.warning(`Vui lòng chọn ${listValue[i]}`);
        return false;
      }
    }
    return true;
  }

  changeOrderSelected(order: IBillPayment) {
    this.orderSelected = order;
    this.listVoucher = this.orderSelected.listVoucher;
    this.resetSPCKAndSPVoucherProduct();
    this.checkStatusNotifyProduct();
  }

  checkProductDiscountTaxOrder() {
    let productDiscountTaxOrder;
    this.orderSelected.products.forEach(product => {
      if (product.productCode === SPCKProduct.code) {
        productDiscountTaxOrder = product;
      }
    });
    if (!productDiscountTaxOrder) {
      const productOrder = this.listProduct?.find(product => product.productCode === SPCKProduct.code) || this.SPCKProduct;
      productOrder && (productDiscountTaxOrder = this.addProductToCustomerOrder(productOrder));
    }
    return productDiscountTaxOrder;
  }

  changeDiscountAmountOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    let productDiscountTaxOrder = this.checkProductDiscountTaxOrder();
    if (!productDiscountTaxOrder) {
      this.toast.warning('Không tìm thấy sản phẩm chiết khấu');
      return;
    }
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.productDiscountTaxOrder = productDiscountTaxOrder;
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = 'discountAmount';
    dialogRef.closed.subscribe(res => {
      if (res && res.amount) {
        const index = this.orderSelected.products.findIndex(product => product.productCode === SPCKProduct.code);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
          this.productDiscountTaxOrder = res;
          this.updateSPDV();
          this.updateOrder();
        }
      } else {
        this.orderSelected.products = this.orderSelected.products.filter(product => product.productCode !== SPCKProduct.code);
        this.productDiscountTaxOrder = {};
        this.updateSPDV();
        this.updateOrder();
      }
    });

    dialogRef.dismissed.subscribe(res => {
      if (!productDiscountTaxOrder.amount) {
        this.orderSelected.products = this.orderSelected.products.filter(product => product.productCode !== SPCKProduct.code);
        this.productDiscountTaxOrder = {};
        this.updateSPDV();
        this.updateOrder();
      }
    });
  }

  addVatRateDiscountProductOrder() {
    let vatRateDiscountProductOrder;
    this.orderSelected.products.forEach(product => {
      if (product.feature === SPGCProduct.feature) {
        vatRateDiscountProductOrder = product;
      }
    });
    if (!vatRateDiscountProductOrder) {
      const productOrder = this.listProduct?.find(product => product.productCode === SPGCProduct.code) || this.SPGCProduct;
      productOrder && (vatRateDiscountProductOrder = this.addProductToCustomerOrder(productOrder));
    }
    return vatRateDiscountProductOrder;
  }

  changeVatRateDiscountProductOrder() {
    let title;
    if (
      this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale &&
      this.invoiceConfiguration.discountVat == DiscountVat.discount &&
      this.invoiceConfiguration.taxReductionType == TaxReductionType.product
    ) {
      title = 'discountVatRateProductOrder';
    }

    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(VatRateDiscountOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = title;
    dialogRef.closed.subscribe(res => {
      if (res && res.discountVatAmount) {
        const vatRateDiscountProductOrder = this.addVatRateDiscountProductOrder();
        vatRateDiscountProductOrder.productName = res.vatRateDiscountProductName;
        this.orderSelected.discountVatRate = res.discountVatRate;
        this.orderSelected.discountVatAmount = res.discountVatAmount;
        this.orderSelected.haveDiscountVat = true;
        this.orderSelected.totalAmount =
          this.orderSelected.totalPreTax + (this.orderSelected.vatAmount || 0) - (this.orderSelected.discountVatAmount || 0);
        this.checkStatusNotifyProduct();
      } else {
        this.orderSelected.products = this.orderSelected.products.filter(product => product.feature !== SPGCProduct.feature);
        this.orderSelected.discountVatRate = undefined;
        this.orderSelected.discountVatAmount = undefined;
        this.orderSelected.haveDiscountVat = false;
        if (!title) {
          this.updateSPDV();
          this.updateOrder();
        } else {
          this.orderSelected.totalAmount = this.orderSelected.totalPreTax + (this.orderSelected.vatAmount || 0);
        }
      }
    });
  }

  changeVatRateOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = 'vatRate';
    dialogRef.closed.subscribe(res => {
      if (res) {
        this.orderSelected.vatRate = res.vatRate;
        if (res.vatRate < 0) {
          this.orderSelected.vatRateName = this.listVat.find(vat => vat.value === res.vatRate)?.name || '';
        }
        this.orderSelected.vatAmount = res.vatAmount;
        this.orderSelected.totalAmount = this.orderSelected.totalPreTax + this.orderSelected.vatAmount;
        this.orderSelected.products.forEach(product => {
          product.toppings?.forEach(productTopping => {
            productTopping.vatRate = this.orderSelected.vatRate;
            this.changeProductSelected(productTopping);
          });
          product.voucherProducts?.forEach(productVoucher => {
            productVoucher.vatRate = this.orderSelected.vatRate;
            this.changeProductSelected(productVoucher);
          });
          if (product.feature === SPCKProduct.feature) {
            product.vatRate = this.orderSelected.vatRate;
          }
          if (product.feature === ProductNormal.feature || product.feature == SPKMProduct.feature) {
            product.vatRate = this.orderSelected.vatRate;
            this.changeProductSelected(product);
          } else {
            product.vatAmount = (product.totalPreTax * (product.vatRate > 0 ? product.vatRate : 0)) / 100;
            product.totalAmount = product.vatAmount + product.totalPreTax;
          }
        });
      }
    });
  }

  changeVatAmountOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = 'vatAmount';
    dialogRef.closed.subscribe(vatAmount => {
      if (vatAmount) {
        this.orderSelected.vatAmount = vatAmount;
        this.orderSelected.totalAmount = this.orderSelected.totalPreTax + vatAmount + (this.orderSelected?.extraConfig?.svc5 || 0);
        this.checkStatusNotifyProduct();
      }
    });
  }

  openDiscountTaxProductComponent(productSelected: ProductBill) {
    if (productSelected.feature !== SPKMProduct.feature) {
      const dialogRef = this.modalService.open(DiscountTaxProductComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
      dialogRef.componentInstance.productSelected = productSelected;
      dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
      dialogRef.componentInstance.lastCompany = this.lastCompany;
      dialogRef.componentInstance.returnOrder = this.returnOrder;
      dialogRef.closed.subscribe((res?: ProductBill) => {
        if (res) {
          const index = this.orderSelected.products.findIndex(product => product.productUniqueId === res.productUniqueId);
          if (index !== -1) {
            this.orderSelected.products[index] = res;
            res.vatRateName === 'Khác' && (this.orderSelected.products[index].vatRateName = 'Khác: (' + res.vatRate + '%)');
          }
          this.updateSPDV();
          if (res.productCode == this.spdvProduct.code) {
            this.updateOrder(false);
          } else {
            this.updateOrder(true);
          }

          // if(res.quantity < res.quantityInitial){
          //   this.saveOrder(false)
          // }
        }
      });
    }
  }

  async selectedAreaUnit(event) {
    this.menuType = 0;
    if (event.type) {
      if (event.idOrder) {
        let findOrder = this.listOrder.find(order => order.id == event.idOrder);
        if (findOrder) {
          this.orderSelected = findOrder;
          this.resetSPCKAndSPVoucherProduct();
        } else {
          // const newOrder: any = await lastValueFrom(this.orderService.find(event.idOrder));
          // this.orderSelected = newOrder.body.data;
          // this.orderSelected.billDate = dayjs(this.orderSelected.billDate);
          // this.resetSPCKAndSPVoucherProduct();
          // this.listOrder.push(this.orderSelected);
          this.getOrderDetailById(event.idOrder);
        }
      } else {
        this.createOrder();
        this.resetSPCKAndSPVoucherProduct();
        this.setAreaUnitToOrder(event);
      }
    } else {
      this.setAreaUnitToOrder(event);
      this.orderSelected.deliveryType = DeliveryType.AT_TABLE;
    }
  }

  setAreaUnitToOrder(data) {
    this.orderSelected.areaId = data.areaId;
    this.orderSelected.areaName = data.areaName;
    this.orderSelected.areaUnitId = data.id;
    this.orderSelected.areaUnitName = data.name;
    this.orderSelected.deliveryType = DeliveryType.AT_TABLE;
  }

  showListArea() {
    this.activeBorderArea = 0;
    this.listAreaShow = this.listArea;
  }

  changeArea(area) {
    this.activeBorderArea = area.id;
    this.areaSelected = area;
    if (this.areaSelected) {
      this.listAreaShow = [this.areaSelected];
    }
  }

  filterProductByProductGroupId(event?: any, groupProduct?: any) {
    if (event && groupProduct) {
      groupProduct.status = !groupProduct.status;
      if (groupProduct.status) {
        this.activeTotalProductGroup = true;
        this.filterProduct.groupIds.push(groupProduct.id);
        this.listProductGroup?.forEach(productGroup => {
          if (productGroup.status === false) {
            this.activeTotalProductGroup = false;
          }
        });
      } else {
        this.filterProduct.groupIds = this.filterProduct.groupIds.filter(group => group !== groupProduct.id);
        this.activeTotalProductGroup = false;
      }
    } else {
      this.activeTotalProductGroup = !this.activeTotalProductGroup;
      this.filterProduct.groupIds = [];
      this.listProductGroup?.forEach(productGroup => {
        productGroup.status = this.activeTotalProductGroup;
        if (productGroup.status) {
          this.filterProduct.groupIds.push(productGroup.id);
        }
      });
    }
    this.filterProduct.page = Page.PAGE_NUMBER;
    this.listProduct = [];
    this.getListProduct();
  }

  showDetailProduct(product: IProduct) {
    const dialogRef = this.modalService.open(ProductToppingListComponent, {
      size: 'xl',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.product = product;
    dialogRef.closed.subscribe(res => {
      if (res) {
        this.checkVatProduct(product);
      }
    });
  }

  addProduct() {
    // if (!this.checkReturn) {
    const dialogRef = this.modalService.open(ModalCreateProductComponent, { size: 'xl', backdrop: 'static' });
    dialogRef.componentInstance.id = null;
    dialogRef.closed.subscribe((res?: any) => {
      if (res.message) {
        this.listProduct?.unshift({
          ...res.data,
          productName: res.data.name,
          productCode: res.data.code,
          productProductUnitId: res.data.conversionUnits[0].id,
        });
      }
    });
    // } else {
    //   this.toast.error('Không thể chọn sản phẩm');
    // }
  }

  getCustomerById(customerId) {
    this.customerService.find(customerId).subscribe((result: any) => {
      let customerInfo = result.body.data;
      this.orderSelected.pointBalanceCustomer = customerInfo.pointBalance || 0;
      this.orderSelected.moneyBalanceCustomer = customerInfo.moneyBalance || 0;
      this.orderSelected.cardCustomerInfo = customerInfo.cardInformation;
    });
  }

  addCustomer() {
    // open customer popup component
    const dialogRef = this.modalService.open(CustomerSaveComponent, {
      size: 'xl',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.closed.subscribe(result => {
      this.listCustomer = this.listCustomer.concat(result);
      this.orderSelected.customerId = result.id;
      this.orderSelected.customerName = result.name;
      this.getCustomerById(this.orderSelected.customerId);
    });
  }

  toggleStatusDesciptionOrder() {
    this.statusDesciptionOrder = !this.statusDesciptionOrder;
    if (this.statusDesciptionOrder) {
      setTimeout(() => {
        this.descriptionOrder.nativeElement.focus();
      }, 100);
    }
  }

  addVoucherOrder() {
    let productVoucherOrder;
    this.orderSelected.products.forEach(product => {
      if (product.productCode === SPVoucherProduct.code) {
        productVoucherOrder = product;
      }
    });
    if (!productVoucherOrder) {
      const productOrder = this.listProduct?.find(product => product.productCode === SPVoucherProduct.code) || this.SPVoucherProduct;
      productOrder && (productVoucherOrder = this.addProductToCustomerOrder(productOrder));
    }
    return productVoucherOrder;
  }

  changeVoucherOrder(event?: any, productSelected?: ProductBill) {
    event?.stopPropagation();
    if (!this.checkOrder(this.orderSelected) || this.returnOrder) {
      return;
    }
    let productVoucherOrder;
    if (!productSelected) {
      productVoucherOrder = this.addVoucherOrder();
      if (!productVoucherOrder) {
        this.toast.warning('Không tìm thấy sản phẩm chiết khấu');
        return;
      }
    }

    let listVoucherLocal: any[] = [];
    if (productSelected) {
      this.listVoucher?.forEach(voucher => {
        if (voucher.type == VoucherType.type200) {
          for (const productVoucher of voucher.conditions) {
            if (productVoucher.buyProductProductUnitId.includes(productSelected.productProductUnitId)) {
              listVoucherLocal.push(voucher);
              break;
            }
          }
        }
      });
    } else {
      this.listVoucher?.forEach(voucher => {
        if (voucher.type !== VoucherType.type200) {
          listVoucherLocal.push(voucher);
        }
      });
    }

    const dialogRef = this.modalService.open(VoucherOrderComponent, { size: 'xl', backdrop: 'static', windowClass: 'margin-5' });
    if (!productSelected) {
      dialogRef.componentInstance.productVoucherOrder = productVoucherOrder;
    }
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.listVoucher = listVoucherLocal;
    dialogRef.componentInstance.listProductGroup = this.listProductGroup;
    dialogRef.componentInstance.productSelected = productSelected;
    dialogRef.closed.subscribe(res => {
      let voucherOrderBefore = JSON.parse(JSON.stringify(this.orderSelected.vouchers));
      if (res && res.orderSelected.vouchers?.length) {
        this.orderSelected.vouchers = res.orderSelected.vouchers;
        this.orderSelected.discountAmount = res.orderSelected.discountAmount;
        this.orderSelected.products = res.orderSelected.products;
        let findVoucherCheckBox = this.orderSelected.vouchers.filter(
          voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300
        );
        this.productVoucherOrder = this.orderSelected.products.find(product => product.productCode == SPVoucherProduct.code);
        if (!this.productVoucherOrder && findVoucherCheckBox.length) {
          this.productVoucherOrder = this.addVoucherOrder();
        }
      } else {
        this.orderSelected.vouchers = [];
        this.orderSelected.products = res.orderSelected.products;
        this.orderSelected.products = this.orderSelected.products.filter(
          productSelected => productSelected.productCode !== SPVoucherProduct.code
        );
        this.orderSelected.discountAmount = 0;
        this.productVoucherOrder = {};
      }

      for (let i = 0; i < this.listVoucher.length; i++) {
        for (let j = 0; j < this.listVoucher[i].conditions.length; j++) {
          for (let m = 0; m < res.listVoucher.length; m++) {
            for (let n = 0; n < res.listVoucher[m].conditions.length; n++) {
              if (this.listVoucher[i].conditions[j].desc == res.listVoucher[m].conditions[n].desc) {
                this.listVoucher[i].conditions[j] = res.listVoucher[m].conditions[n];
                this.listVoucher[i].check = res.listVoucher[m].check || false;
              }
            }
          }
        }
      }

      if (this.invoiceConfiguration.combineVoucherApply) {
        let voucherOrderAfter = this.orderSelected.vouchers;
        voucherOrderBefore.forEach(voucherBefore => {
          let findVoucherBefore = voucherOrderAfter.find(voucher => voucherBefore.code == voucher.code);
          if (!findVoucherBefore) {
            this.listVoucher.forEach(voucher => {
              if (voucher.code == voucherBefore.code && voucher.differentExtConditions) {
                voucher.differentExtConditions.autoApplyVoucher = false;
              }
            });
          }
        });
      } else {
        this.listVoucher.forEach(voucher => {
          if (voucher.differentExtConditions?.autoApplyVoucher) {
            voucher.differentExtConditions.autoApplyVoucher = false;
          }
        });
      }

      if (res.voucherRemove) {
        if (
          res.voucherRemove.voucherRemove.id == voucherOrderBefore[0].id &&
          !this.orderSelected.vouchers.find(voucher => voucher.id == res.voucherRemove.voucherRemove.id)
        ) {
          this.orderSelected.vouchers.push(res.voucherRemove.voucherRemove);
        }
        removeVoucherOrder(
          this.orderSelected,
          res.voucherRemove.voucherRemove,
          this.listVoucher,
          this.lastCompany,
          res.voucherRemove.productSelected
        );
      }

      this.updateSPDV();
      this.updateOrder();
    });

    dialogRef.dismissed.subscribe(res => {
      if (!this.orderSelected.vouchers?.length) {
        this.orderSelected.vouchers = [];
        this.orderSelected.products = this.orderSelected.products.filter(product => product.productCode !== SPVoucherProduct.code);
        this.orderSelected.discountAmount = 0;
        this.productVoucherOrder = {};
        this.updateSPDV();
        this.updateOrder();
      }
    });
  }

  checkVatProduct(product: IProduct) {
    if (this.returnOrder) {
      return;
    }
    if (!this.invoiceConfiguration.overStock && product.inventoryCount < 1 && product.inventoryTracking) {
      this.toast.warning(`Sản phẩm ${product.productName} xuất quá số lượng tồn (${product.inventoryCount} ${product.unit || ''})`);
      return;
    }

    if (
      product.vatRate &&
      product.vatRate === this.listVat[this.listVat.length - 1].value &&
      this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple
    ) {
      const dialogRef = this.modalService.open(SetVatRateProductComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.productName = product.productName;
      dialogRef.closed.subscribe(res => {
        product.vatRate = res;
        if (product.haveTopping) {
          this.openProductToppingComponent(product, this.orderSelected);
        }
      });
    } else {
      if (product.haveTopping) {
        this.openProductToppingComponent(product, this.orderSelected);
      } else {
        this.addProductToCustomerOrder(product);
      }
    }
  }

  checkProductQuantity(productSelected) {
    if (!this.returnOrder) {
      if (productSelected.quantity == 0 || !productSelected.quantity) {
        this.removeProductFromCustomerOrder(productSelected);
      }
      if (checkConditionNotify(this.invoiceConfiguration) && productSelected.quantity < productSelected.quantityInitial) {
        this.cancelProductFromCustomerOrder(
          productSelected,
          take_decimal_number(productSelected.quantityInitial - productSelected.quantity, this.lastCompany.roundScaleQuantity)
        );
      }
    }
  }

  openProductToppingComponent(product?: IProduct, orderSelected?: IBillPayment) {
    const listProductOrder: ProductBill[] = [];
    orderSelected?.products.forEach(productOrder => {
      if (productOrder.productProductUnitId == product?.productProductUnitId && productOrder.feature == 1) {
        listProductOrder.push(productOrder);
      }
    });

    const dialogRef = this.modalService.open(ProductOrderToppingComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.product = product;
    dialogRef.componentInstance.orderSelected = orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.listProductOrder = listProductOrder;
    dialogRef.closed.subscribe(res => {
      // this.getVatRateName(res.productOrder);
      if (res != null && !res.statusUpdateOrder) {
        addProductSelectedToList(res.productOrder, this.orderSelected);
      } else {
        const index = this.orderSelected.products.findIndex(product => product.productUniqueId == res.productOrder.productUniqueId);
        if (index !== -1) {
          this.orderSelected.products[index] = res.productOrder;
        }
      }
      this.changeProductSelected(res.productOrder);
      this.orderSelected.products.forEach(productOrder => {
        this.checkVatRateDiscountProductOrder(productOrder);
      });
    });
  }

  openProductToppingListComponent(product?: IProduct) {
    let productSelected;
    this.orderSelected.products.forEach(productOrder => {
      if (product?.productId == productOrder.productId) {
        productSelected = productOrder;
      }
    });
    const dialogRef = this.modalService.open(ProductToppingListComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef.componentInstance.product = product;
    dialogRef.componentInstance.productOrder = productSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.closed.subscribe(res => {
      product = res;
      product && this.addProductToCustomerOrder(product);
    });
  }

  openToppingProduct(event, productSelected) {
    if (!(checkConditionNotify(this.invoiceConfiguration) && this.orderSelected.id && productSelected.id)) {
      event.stopPropagation();
      const dialogRef = this.modalService.open(ProductToppingListComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.productOrder = productSelected;
      dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
      dialogRef.componentInstance.lastCompany = this.lastCompany;
      dialogRef.componentInstance.orderSelected = this.orderSelected;
      dialogRef.closed.subscribe(res => {
        const index = this.orderSelected.products.findIndex(product => product.productUniqueId == res.productUniqueId);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
          this.changeProductSelected(this.orderSelected.products[index]);
        }
      });
    }
  }

  checkProductQuantityKeypress(productSelected, event: any) {
    if (checkConditionNotify(this.invoiceConfiguration) && event.key < productSelected.quantityInitial) {
      this.cancelProductFromCustomerOrder(productSelected, productSelected.quantityInitial - event.key);
    }
  }

  changeProductSelectedQuantity(productSelected, event?: any) {
    this.checkOverStockProduct(productSelected, this.invoiceConfiguration);
    this.changeProductSelected(productSelected);
  }

  checkOverStockProduct(productSelected, invoiceConfiguration) {
    if (
      !invoiceConfiguration.overStock &&
      productSelected.quantity > productSelected.quantityProduct &&
      productSelected.inventoryTracking
    ) {
      this.toast.warning(
        `Sản phẩm ${productSelected.productName} xuất quá số lượng tồn (${productSelected.quantityProduct} ${productSelected.unit || ''})`
      );
      if (productSelected.quantityProduct > 0) {
        productSelected.quantity = productSelected.quantityProduct;
      }
      return false;
    }
    return true;
  }

  getprocessedQuantityProduct(productSelected) {
    let tooltip = '';
    if (productSelected.deliveredQuantity) {
      tooltip += `Đã cung ứng: ${productSelected.deliveredQuantity}\n`;
    }
    if (productSelected.processedQuantity) {
      tooltip += `Đã chế biến: ${productSelected.processedQuantity}\n`;
    }
    if (productSelected.processingQuantity) {
      tooltip += `Chờ chế biến: ${productSelected.processingQuantity}\n`;
    }
    return tooltip;
  }

  addProductToCustomerOrder(product: IProduct, vatRate?: number) {
    let findProduct = false;
    let newProductSelected;
    this.orderSelected.products.forEach(productSelected => {
      if (
        productSelected.productProductUnitId === product.productProductUnitId &&
        productSelected.feature == ProductNormal.feature &&
        productSelected.productCode !== SPDVProduct.code
      ) {
        productSelected.quantity && productSelected.quantity++;
        newProductSelected = productSelected;
        this.checkOverStockProduct(newProductSelected, this.invoiceConfiguration);
        findProduct = true;
      }
    });
    if (!findProduct) {
      let vatRateName = this.listVat.find(item => item.value === (product.vatRate ?? 0))?.name;
      if (!vatRateName) {
        if (product.vatRate === NoTaxProduct.id) {
          product.vatRate = 0;
          vatRateName = 'Thuế suất 0%';
        } else {
          vatRateName = 'Thuế suất ' + product.vatRate + '%';
        }
      }

      if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale) {
        product.vatRate = -1;
      }

      if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceOne) {
        if (this.orderSelected.vatRate) {
          product.vatRate = this.orderSelected.vatRate;
        } else {
          product.vatRate = 0;
        }
      }

      newProductSelected = {
        productId: product.productId,
        imageUrl: product.imageUrl,
        productProductUnitId: product.productProductUnitId,
        productName: product.productCode === SPCKProduct.code ? 'Chiết khấu' : product.productName,
        productCode: product.productCode,
        quantity:
          product.productCode === SPGCProduct.code ||
          product.productCode === SPCKProduct.code ||
          product.productCode === SPDVProduct.code ||
          product.productCode === SPVoucherProduct.code
            ? 0
            : getQuantityProduct(product),
        quantityProduct: product.inventoryCount || 0,
        unit: product.unit,
        unitId: product.unitId,
        unitPrice: product.salePrice,
        discountAmount: 0,
        amount: 0,
        totalPreTax: 0,
        vatRate: vatRate || (product.vatRate ?? 0),
        vatRateName,
        vatAmount: 0,
        inventoryTracking: product.inventoryTracking,
        totalAmount: 0,
        feature:
          product.productCode === SPCKProduct.code || product.productCode === SPVoucherProduct.code
            ? SPCKProduct.feature
            : product.productCode === SPGCProduct.code
            ? SPGCProduct.feature
            : 1,
        typeDiscount: 'Giảm giá trị',
        discountPercent: 0,
        position: this.orderSelected.products.length + 1,
        displayAmount: 0,
        productNameCustom: '',
        productUniqueId: generateRandomString(),
        discountVatRate: product.discountVatRate || 0,
        totalDiscount: 0,
        displayVatAmount: 0,
        displayTotalAmount: 0,
        voucherProducts: [],
      };
      if (
        this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple ||
        this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceOne
      ) {
        newProductSelected.discountVatRate = null;
        newProductSelected.totalDiscount = null;
      }
      this.checkOverStockProduct(newProductSelected, this.invoiceConfiguration);
      addProductSelectedToList(newProductSelected, this.orderSelected);
    }
    if (newProductSelected.feature == ProductNormal.feature && newProductSelected.productCode !== SPDVProduct.code) {
      this.changeProductSelected(newProductSelected);
    }
    this.checkVatRateDiscountProductOrder(newProductSelected);
    return newProductSelected;
  }

  checkVatRateDiscountProductOrder(productOrder) {
    if (
      this.invoiceConfiguration.invoiceType == this.invoiceType.invoiceSale &&
      this.invoiceConfiguration.discountVat == DiscountVat.discount &&
      this.invoiceConfiguration.taxReductionType == TaxReductionType.product &&
      productOrder.feature !== this.spgcProduct.feature
    ) {
      if (productOrder.discountVatRate > 0) {
        const vatRateDiscountProductOrder = this.addVatRateDiscountProductOrder();
        this.orderSelected.haveDiscountVat = true;
        vatRateDiscountProductOrder.productName = getProductNameSPGC(this.orderSelected.discountVatAmount || 0);
      } else {
        productOrder.toppings?.forEach(productTopping => {
          if (productTopping.discountVatRate > 0) {
            const vatRateDiscountProductOrder = this.addVatRateDiscountProductOrder();
            this.orderSelected.haveDiscountVat = true;
            vatRateDiscountProductOrder.productName = getProductNameSPGC(this.orderSelected.discountVatAmount || 0);
          }
        });
      }
    }

    if (
      this.invoiceConfiguration.invoiceType == this.invoiceType.invoiceSale &&
      this.invoiceConfiguration.discountVat == DiscountVat.discount &&
      this.invoiceConfiguration.taxReductionType == 0
    ) {
      productOrder.discountVatRate = null;
      productOrder.totalDiscount = null;
    }
  }

  changeProductSelected(productSelected?: ProductBill) {
    if (productSelected) {
      changeProductSelected(productSelected, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
      this.checkVatRateDiscountProductOrder(productSelected);
    }
    this.updateSPDV();
    this.updateOrder();
    if (productSelected) {
      if (productSelected.quantity < productSelected.quantityInitial) {
        this.statusNotifyProduct = false;
      }
    }
  }

  updateOrder(updateSPDV = true) {
    updateOrder(this.orderSelected, this.invoiceConfiguration, this.invoiceType, this.lastCompany, updateSPDV, this, this.returnOrder);
    this.checkStatusNotifyProduct();
  }

  checkDiscountVatRateProduct(orderSelected: IBillPayment) {
    orderSelected.discountVatRate = 0;
    let listProductNormal = orderSelected.products.filter(product => product.feature == ProductNormal.feature);
    if (!orderSelected.discountVatAmount) {
      orderSelected.products = orderSelected.products.filter(product => product.feature !== SPGCProduct.feature);
    } else {
      let vatRateProduct = listProductNormal[0].vatRate;
      for (let i = 0; i < listProductNormal.length; i++) {
        if (listProductNormal[i].feature == 1) {
          if (listProductNormal[i].vatRate !== vatRateProduct) {
            break;
          } else {
            if (i == listProductNormal.length - 1) {
              orderSelected.discountVatRate = vatRateProduct;
            }
          }
        }
      }
    }
  }

  changeProductVoucherQuantity(productSelected) {
    const dialogRef2 = this.modalService.open(ProductVoucherQuantityComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef2.componentInstance.productSelected = productSelected;
    dialogRef2.componentInstance.lastCompany = this.lastCompany;
    dialogRef2.closed.subscribe(res => {
      if (res) {
        const index = this.orderSelected.products.findIndex(product => product.productUniqueId === productSelected.productUniqueId);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
          this.orderSelected.products[index].voucherProducts.forEach(productVoucher => {
            this.changeProductSelected(productVoucher);
          });
        }
      }
    });
  }

  updateProductDiscountTaxOrder(productDiscountTaxOrder, orderSelected) {
    if (productDiscountTaxOrder.discountPercent) {
      productDiscountTaxOrder.amount =
        productDiscountTaxOrder.unitPrice =
        productDiscountTaxOrder.totalPreTax =
          take_decimal_number((orderSelected.amount * productDiscountTaxOrder.discountPercent) / 100, this.lastCompany.roundScaleAmount);
    }
    if (productDiscountTaxOrder.vatRate) {
      productDiscountTaxOrder.vatAmount = take_decimal_number(
        (productDiscountTaxOrder.totalPreTax * getPositiveNumber(productDiscountTaxOrder.vatRate)) / 100 || 0,
        this.lastCompany.roundScaleAmount
      );
    }
    productDiscountTaxOrder.totalAmount = productDiscountTaxOrder.totalPreTax + productDiscountTaxOrder.vatAmount;
  }

  cancel() {
    this.activeRemoveProductSelected = '';
  }

  confirmRemoveProductSelected(product: ProductBill) {
    if (checkConditionNotify(this.invoiceConfiguration)) {
      this.cancelProductFromCustomerOrder(product, product.quantity);
    } else {
      this.activeRemoveProductSelected = product.productUniqueId;
    }
  }

  checkProductQuantityWithProductRemote(productLocal: ProductBill) {
    if (checkConditionNotify(this.invoiceConfiguration)) {
      let productRemote;
      JSON.parse(this.listOrderInitial).forEach(order => {
        order.products.forEach(productSelected => {
          if (productSelected.productUniqueId == productLocal.productUniqueId) {
            productRemote = productSelected;
          }
        });
      });
      if (productRemote.quantity > productLocal.quantity) {
        // this.cancelProductFromCustomerOrder(productLocal);
        return true;
      }
    }
    return false;
  }

  cancelProductFromCustomerOrder(product: ProductBill, productQuantityRemove: number) {
    if (product.id && this.listOrderInitial) {
      const dialogRef2 = this.modalService.open(RemoveProductComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
      dialogRef2.componentInstance.productSelected = product;
      dialogRef2.componentInstance.lastCompany = this.lastCompany;
      dialogRef2.componentInstance.orderSelected = this.orderSelected;
      dialogRef2.componentInstance.productQuantityRemove = productQuantityRemove;
      dialogRef2.closed.subscribe(res => {
        if (res) {
          product.quantity = product.quantityInitial - res;
          if (!product.quantity) {
            this.orderSelected.products = this.orderSelected.products.filter(
              productOrder => productOrder.productUniqueId !== product.productUniqueId
            );
            if (product.feature == SPKMProduct.feature) {
              removeProductPromoOrder(this.orderSelected, product, this.listVoucher, this.toast);
            }
          }
          this.changeProductSelected(product);
          let findVoucherCheckBox = this.orderSelected.vouchers.filter(
            voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300
          );
          if (!findVoucherCheckBox.length) {
            this.orderSelected.products = this.orderSelected.products.filter(product => product.productCode !== SPVoucherProduct.code);
          }
          this.saveOrder(false);
        }
      });
      dialogRef2.dismissed.subscribe(() => {
        if (product.quantity < product.quantityInitial) {
          product.quantity = product.quantityInitial;
          this.changeProductSelected(product);
        }
      });
    } else {
      this.removeProductFromProductListSelected(product);
    }
  }

  removeProductFromCustomerOrder(product: ProductBill) {
    if (checkConditionNotify(this.invoiceConfiguration) && product.productCode !== this.spdvProduct.code) {
      this.cancelProductFromCustomerOrder(product, product.quantity);
    } else {
      this.removeProductFromProductListSelected(product);
    }
  }

  removeProductFromProductListSelected(product: any) {
    if (product.voucherProducts.length) {
      let voucherRemoveOrder = this.orderSelected.vouchers.find(voucher => voucher.id == product.voucherProducts[0].voucherId);
      checkCancelSPKMOrder(this.orderSelected, this.listVoucher, this.lastCompany, this.toast, voucherRemoveOrder, product);
    }

    this.orderSelected.products = this.orderSelected.products.filter(
      productSelected => product.productUniqueId !== productSelected.productUniqueId
    );

    if (this.orderSelected.checkSPDV) {
      for (const [indexProduct, productOrder] of this.orderSelected.products.entries()) {
        if (productOrder.vatRate == product.vatRate && productOrder.productCode !== SPDVProduct.code) {
          break;
        } else {
          if (indexProduct == this.orderSelected.products.length - 1) {
            this.orderSelected.products = this.orderSelected.products.filter(
              productSelected => product.vatRate !== productSelected.vatRate
            );
          }
        }
      }
    }

    if (product.feature == SPKMProduct.feature) {
      removeProductPromoOrder(this.orderSelected, product, this.listVoucher, this.toast);
    }

    if (this.orderSelected.products.length == 1 && this.orderSelected.products[0].feature == SPGCProduct.feature) {
      this.orderSelected.discountVatRate = undefined;
      this.orderSelected.discountVatAmount = undefined;
      this.orderSelected.haveDiscountVat = false;
      this.orderSelected.checkboxVatRateDiscountProduct = false;
      this.orderSelected.products = [];
    }

    const listSPDV = this.orderSelected.products.filter(product => product.productCode == SPDVProduct.code);

    if (!listSPDV.length) {
      this.orderSelected.checkSPDV = false;
    }

    this.activeRemoveProductSelected = '';
    if (product.productCode !== SPDVProduct.code) {
      this.changeProductSelected();
    } else {
      this.updateSPDV();
      this.updateOrder();
    }
  }

  convertPointCustomer() {
    if (!this.orderSelected.cardCustomerInfo.redeemValue) {
      const dialogRef = this.modalService.open(ConfirmDialogComponent, {
        size: 'lg',
        backdrop: 'static',
        windowClass: 'margin-5',
      });
      dialogRef.componentInstance.value = new DialogModal(
        ModalHeader.CONTINUE_CONVERT_POINT,
        ModalContent.CONTINUE_CONVERT_POINT,
        ModalBtn.AGREE,
        'check',
        'btn-save'
      );
      dialogRef.componentInstance.formSubmit.subscribe(res => {
        dialogRef.dismiss();
        if (res) {
          this.openConvertPointCustomer();
        }
      });
    } else {
      this.openConvertPointCustomer();
    }
  }

  openConvertPointCustomer() {
    const dialogRef2 = this.modalService.open(ConvertPointComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
    });
    dialogRef2.componentInstance.orderSelected = this.orderSelected;
    dialogRef2.componentInstance.lastCompany = this.lastCompany;
    dialogRef2.closed.subscribe(() => {
      this.getCustomerById(this.orderSelected.customerId);
    });
  }

  removeOrderFromOrderList(event, orderSelected: IBillPayment) {
    event.stopPropagation();
    // if (this.checkReturn) {
    //   this.toast.error('Không thể chọn sản phẩm');
    //   return;
    // }
    this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== orderSelected.taxAuthorityCode);
    if (!this.listOrder.length) {
      this.createOrder();
    } else {
      this.orderSelected = this.listOrder[0];
    }
  }

  increaseProductQuantity(product: ProductBill) {
    if (product.quantity !== undefined) {
      if (this.returnOrder) {
        if (product.quantity + 1 <= product.quantityInitial) {
          product.quantity++;
        }
      } else {
        product.quantity++;
      }
    }
    this.checkOverStockProduct(product, this.invoiceConfiguration);
    this.changeProductSelected(product);
    this.checkVatRateDiscountProductOrder(product);
  }

  decreaseProductQuantity(product: ProductBill) {
    if (this.returnOrder) {
      if (product.quantity >= 1) {
        product.quantity--;
      }
    } else {
      if (product.quantity <= 1) {
        this.confirmRemoveProductSelected(product);
      } else {
        if (product.quantity - 1 < product.quantityInitial && checkConditionNotify(this.invoiceConfiguration)) {
          this.cancelProductFromCustomerOrder(product, 1);
        } else {
          product.quantity && product.quantity--;
        }
      }
    }
    this.changeProductSelected(product);
  }

  loadMore(event) {
    if (event.target.scrollTop === 0) {
      return;
    }
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight) {
      this.filterProduct.page !== undefined && (this.filterProduct.page += 1);
      this.getListProduct();
    }
  }

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  updateSPDV() {
    if (this.orderSelected.checkSPDV) {
      const listProduct = this.orderSelected.products?.filter(product => product.productCode !== SPDVProduct.code);
      const uniqueVatRate = [...new Set(listProduct?.map(data => data.vatRate))].filter(item => item > 0);
      const listSPDVOld = this.orderSelected.products.filter(product => product.productCode == SPDVProduct.code);

      for (const [indexProductDv, productDv] of listSPDVOld.entries()) {
        for (const [indexVatRate, vatRate] of uniqueVatRate.entries()) {
          if (productDv.vatRate == vatRate) {
            break;
          } else {
            if (indexVatRate == uniqueVatRate.length - 1) {
              this.removeProductFromCustomerOrder(productDv);
            }
          }
        }
      }

      const listSPDV = this.orderSelected.products.filter(product => product.productCode == SPDVProduct.code);
      for (const [indexVatRate, vatRate] of uniqueVatRate.entries()) {
        for (const [indexProductDv, productDv] of listSPDV.entries()) {
          if (productDv.vatRate == vatRate) {
            break;
          } else {
            if (indexProductDv == listSPDV.length - 1) {
              this.addProductToCustomerOrder(this.SPDVProduct, vatRate);
            }
          }
        }
      }

      if (!listSPDV.length) {
        uniqueVatRate.forEach(vatRate => {
          this.addProductToCustomerOrder(this.SPDVProduct, vatRate);
        });
      }

      this.orderSelected.products.forEach(productSelected => {
        if (productSelected.productCode == SPDVProduct.code) {
          this.orderSelected.checkSPDV = true;
        }
      });
    }
  }

  toggleSPDV() {
    this.orderSelected.checkSPDV = !this.orderSelected.checkSPDV;
    this.orderSelected.extraConfig.svc5 = 0;
    if (!this.orderSelected.checkSPDV) {
      this.orderSelected.products = this.orderSelected.products.filter(product => product.productCode !== SPDVProduct.code);
    } else {
      this.updateSPDV();
    }
    this.updateOrder();
  }

  ngAfterViewChecked() {
    if (this.invoiceConfiguration && !this.afterViewInit && this.scanBarCodeProduct) {
      this.scanBarCodeProduct.nativeElement.focus();
    }
  }

  togglePosSidebarFooter() {
    this.posSidebarFooterStatus = !this.posSidebarFooterStatus;
  }

  openPopup() {
    this.modalService.open(ModalDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
  }

  convertStringToNumber(value) {
    return parseFloat(value.replace(/,/g, ''));
  }

  toggleListOrderShowStatus(event) {
    event.stopPropagation();
    this.listOrderShowStatus = !this.listOrderShowStatus;
  }

  hiddenListOrderShowStatus() {
    this.listOrderShowStatus = false;
    this.checkStatusNotifyProduct();
  }

  checkStatusNotifyProduct() {
    // if (this.listOrderInitial !== JSON.stringify(this.listOrder)) {
    //   this.statusNotifyProduct = true;
    // } else {
    //   this.statusNotifyProduct = false;
    // }
    if (this.listOrderInitial) {
      let listOrderInitial = JSON.parse(this.listOrderInitial);
      let orderSelectedInitial = listOrderInitial.find(order => (order.id || 0) == this.orderSelected.id);
      if (JSON.stringify(orderSelectedInitial) !== JSON.stringify(this.orderSelected)) {
        this.statusNotifyProduct = true;
      } else {
        this.statusNotifyProduct = false;
      }
      if (!orderSelectedInitial) {
        this.statusNotifyProduct = true;
      }
    }
  }

  numberOnly(event: any): boolean {
    return numberOnly(event);
  }

  // checkReturnQuantity(productSelected: any) {
  //   if (productSelected.quantity === null || productSelected === undefined) {
  //     productSelected.quantity = 0;
  //   }
  // }
  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
    this.contentOption.isHiddenOrder = false;
    if (this.statusNotifyOrder) {
      this.mqttService.disconnect();
    }
  }

  protected readonly ICON_DELETE_SM = ICON_DELETE_SM;
  protected readonly ICON_BAR_CODE = ICON_BAR_CODE;
  protected readonly ICON_TOPPING = ICON_TOPPING;
  protected readonly ICON_VOUCHER_BLUE = ICON_VOUCHER_BLUE;
}
