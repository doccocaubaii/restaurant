import { AfterViewInit, Component, ElementRef, OnDestroy, OnInit, Renderer2, ViewChild } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import {
  DeliveryType,
  InvoiceDiscount,
  InvoiceType,
  LIST_PRODUCT,
  LIST_VAT,
  MenuType,
  NoTaxProduct,
  Page,
  PaymentMethod,
  StatusExit,
  StatusOrder,
  TypeDiscount,
} from 'app/pages/const/customer-order.const';
import { Location } from '@angular/common';
import { ProductService } from '../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductGroupService } from '../service/product-group.service';
import { Subject, debounceTime, distinctUntilChanged, lastValueFrom, of, pluck, switchMap, tap } from 'rxjs';
import { IProduct } from '../model/product.model';
import { IProductGroup } from '../model/product-group.model';
import { FormControl } from '@angular/forms';
import { getPositiveNumber, numberOnly, take_decimal_number, validVariable } from 'app/pages/const/function';
import { CustomerService } from '../service/customer.service';
import { AreaService } from '../service/area.service';
import { AreaUnitService } from '../service/area-unit.service';
import { Customer } from '../model/customer.model';
import { IBillPayment, Payment, ProductBill } from '../model/bill-payment.model';
import { BillService } from '../service/bill.service';
import dayjs from 'dayjs/esm';
import { ToastrService } from 'ngx-toastr';
import { ConfirmCheckoutComponent } from '../customer-order/confirm-checkout/confirm-checkout.component';
import { DiscountTaxOrderComponent } from '../customer-order/discount-tax-order/discount-tax-order.component';
import { FilterProduct } from '../model/filterProduct.model';
import { FilterProductGroup } from '../model/filterProductGroup.mode';
import { FilterCustomer } from '../model/filterCustomer.model';
import { FilterArea } from '../model/filterArea.model';
import { DiscountTaxProductComponent } from '../customer-order/discount-tax-product/discount-tax-product.component';
import { SetVatRateProductComponent } from '../customer-order/set-vat-rate-product/set-vat-rate-product.component';
import { ProductDetailComponent } from '../customer-order/product-detail/product-detail.component';
import { BaseComponent } from 'app/shared/base/base.component';
import { IArea } from 'app/entities/area/area.model';
import { CreateStaffComponent } from '../../staff/create-staff/create-staff.component';
import { PosInvoiceComponent } from '../customer-order/pos-invoice/pos-invoice.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { LoadingOption } from 'app/utils/loadingOption';
import { CheckDeactivate } from '../customer-order/check-deactivate';
import { ConfirmExitDialogComponent } from 'app/shared/modal/confirm-exit-dialog/confirm-exit-dialog.component';
import { ModalCreateProductComponent } from 'app/pages/product/modal-create-product/modal-create-product.component';
import { InvoiceService } from 'app/pages/invoice/service/invoice.service';
import { UtilsService } from 'app/utils/Utils.service';
import { ContentOption } from '../../../utils/contentOption';
import { offline_bill } from 'app/object-stores.constants';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
@Component({
  selector: 'customer-order-offline',
  templateUrl: './customer-order-offline.component.html',
  styleUrls: ['./customer-order-offline.component.scss'],
})
export class PosCustomerOrderOfflinePage extends BaseComponent implements OnInit, OnDestroy, CheckDeactivate, AfterViewInit {
  appSettings = appSettings;
  posMobileSidebarToggled = true;
  deliveryType = DeliveryType;
  clicks = 0;
  loadingProduct = true;
  indexOrder = 1;
  afterViewInit = false;
  @ViewChild('desciptionOrder') desciptionOrder: ElementRef;
  @ViewChild('searchProduct') scanBarCodeProduct: ElementRef;

  menuType: number = MenuType.ProductGroup;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, groupIds: [] };
  filterProductGroup: FilterProductGroup = { page: Page.PAGE_NUMBER, size: 40 };
  filterCustomer: FilterCustomer = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, sort: 'id,asc', type: 1, totalPage: 0 };
  filterArea: FilterArea = { areaSize: Page.PAGE_SIZE, areaUnitSize: Page.PAGE_SIZE };
  query: FormControl = new FormControl('');
  keywordCustomer$ = new Subject<string>();

  listProduct?: IProduct[] | null = [];
  listProductGroup?: IProductGroup[] | null = [];
  listCustomer: Customer[] = [];
  listArea: IArea[] = [];
  listAreaShow: IArea[] = [];
  listVat = LIST_VAT;
  listOrder: IBillPayment[] = [];
  listOrderInitial: string;
  orderSelected!: IBillPayment;
  areaSelected?: IArea;
  listProductSelected?: IProduct[] = [];
  productDiscountTaxOrder;
  productDiscountTaxOrderSelected;
  lastCompany;

  invoiceConfiguration = {
    typeDiscount: 1,
    invoiceType: 1,
  };
  statusDesciptionOrder = false;
  invoiceType = InvoiceType;
  invoiceDiscount = InvoiceDiscount;
  posSidebarFooterStatus = true;
  listOrderShowStatus = false;
  idOrderSelected: number;
  idTableSelected: number;
  activeRemoveProductSelected = 0;
  activeTotalProductGroup = false;
  activeBorderArea = 0;
  activeCreateOrder: boolean | null = null;
  runOutOfProduct = false;
  scanBarCode = false;

  constructor(
    public utilService: UtilsService,
    protected productService: ProductService,
    protected customerService: CustomerService,
    protected areaService: AreaService,
    protected areaUnitService: AreaUnitService,
    protected productGroupService: ProductGroupService,
    protected activatedRoute: ActivatedRoute,
    protected orderService: BillService,
    public router: Router,
    private toast: ToastrService,
    protected modalService: NgbModal,
    public loading: LoadingOption,
    private invoiceService: InvoiceService,
    public contentOption: ContentOption,
    private renderer: Renderer2,
    private location: Location
  ) {
    super();
    this.contentOption.isHiddenOrder = true;
    this.appSettings.appEmpty = true;
    this.appSettings.appContentFullHeight = true;
  }

  async ngOnInit() {
    this.searchProduct();
    this.lastCompany = await this.getCompany();
    // const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    // this.invoiceConfiguration = invoiceConfiguration.data;

    this.listArea = await this.getAllByObjectStore('area');
    this.getListProduct();
    this.getListProductGroup();
    this.listAreaShow = JSON.parse(JSON.stringify(this.listArea));
    this.areaSelected = this.listArea[0];
    this.listCustomer = await this.getAllByObjectStore('customer');

    this.activatedRoute.queryParamMap.subscribe((params: any) => {
      // this.scanBarCodeProduct.nativeElement.focus();
      this.idOrderSelected = +params.get('id_order');
      this.idTableSelected = +params.get('table');
      if (this.idOrderSelected) {
        this.getOrderDetailById(this.idOrderSelected);
      } else {
        this.createOrder();
      }
    });
  }

  returnToOrderList() {
    this.location.back();
  }

  checkDeactivate() {
    if (this.listOrderInitial === JSON.stringify(this.listOrder)) {
      return of(true);
    } else {
      const resultSubject: Subject<boolean> = new Subject<boolean>();
      const dialogRef = this.modalService.open(ConfirmExitDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
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
            const result: any = await lastValueFrom(this.orderService.update({ ...this.orderSelected, billDate: dayjs() }));
            if (result.status) {
              dialogRef.componentInstance.dismiss();
              resultSubject.next(!!res);
              resultSubject.complete();
              this.toast.success(result.body.reason);
            }
          } else {
            const result: any = await lastValueFrom(this.orderService.create({ ...this.orderSelected, billDate: dayjs() }));
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
      // return of(confirm('Đơn hàng chưa lưu, bạn có chắc chắn muốn thoát?'));
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

  searchProductFunction(event) {
    this.filterProduct.keyword = event.target.value;
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
  }

  async getOrderDetailById(id) {
    const orderDetail = await this.findByID(offline_bill, id);
    console.log(orderDetail);
    if (orderDetail) {
      this.orderSelected = orderDetail;
      this.orderSelected.billDate = dayjs(this.orderSelected.billDate);
      this.orderSelected.countProduct = 0;
      this.orderSelected.typeInv = this.invoiceConfiguration.invoiceType;
      this.orderSelected.products.forEach(product => {
        this.orderSelected.countProduct++;
        this.updateOrderConfiguration(
          product,
          this.orderSelected,
          this.invoiceConfiguration.invoiceType,
          this.invoiceConfiguration.typeDiscount
        );
        if (product.feature === 3) {
          this.orderSelected.countProduct--;
          this.productDiscountTaxOrderSelected = this.orderSelected.products.find(product => product.feature === 3);
          product.typeDiscount = TypeDiscount.VALUE;
        }
        const vatRateName = this.listVat.find(item => item.value === (product.vatRate ?? 0))?.name;
        if (!vatRateName) {
          if (product.vatRate === NoTaxProduct.id) {
            product.vatRateName = 'Thuế suất 0%';
            product.vatRate = 0;
          } else {
            product.vatRateName = 'Thuế suất ' + product.vatRate + '%';
          }
        } else {
          product.vatRateName = vatRateName;
        }
      });
      this.listOrder.push(this.orderSelected);
      this.listOrderInitial = JSON.stringify(this.listOrder);
    } else {
      this.createOrder();
      this.toast.warning('Không tồn tại đơn hàng');
    }
  }

  updateOrderConfiguration(product, order, invoiceType, invoiceDiscount) {
    if (product.feature === 1) {
      if (invoiceType === InvoiceType.invoiceSale) {
        product.vatRate = -1;
        order.vatRate = -1;
        order.vatAmount = 0;
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
      if (invoiceDiscount === InvoiceDiscount.noDiscount || invoiceDiscount === InvoiceDiscount.productValue) {
        order.products = order.products.filter(product => product.feature === 1);
      } else {
        if (invoiceType !== InvoiceType.invoiceMultiple) {
          product.vatRate = 0;
        }
      }
    }
    this.changeProductSelected(product);
  }

  trackId = (_index: number, item: IProduct): number => item.id;

  async getListProductGroup() {
    let listProductGroupLocal = await this.getAllByObjectStore('product_group');
    this.afterViewInit = true;
    this.listProductGroup = listProductGroupLocal;
    this.listProductGroup?.forEach(productGroup => {
      productGroup.status = false;
    });
  }

  async getListProduct() {
    this.loadingProduct = true;
    let compareProduct;

    let result = await this.getProductListtWithPaging('product', this.filterProduct);

    this.loadingProduct = false;
    if (!result?.length) {
      this.runOutOfProduct = true;
    }
    result?.forEach(item => {
      if (item.productCode == 'SP1') {
        this.productDiscountTaxOrder = item;
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

    console.log(this.listProduct);
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
    // this.filterCustomer.page++;
    // this.getListCustomer();
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
        this.listOrder.length === 1 && (this.listOrderInitial = JSON.stringify(this.listOrder));
      }
    });
  }

  changeCustomer() {
    this.listCustomer.forEach(customer => {
      if (customer.id === this.orderSelected.customerId) {
        this.orderSelected.customerName = customer.name;
      }
    });
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

  async createOrder() {
    const order: IBillPayment = new IBillPayment();
    order.payment = new Payment();
    order.payment.paymentMethod = PaymentMethod.transfer;
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

    // if (!this.invoiceConfiguration) {
    //   const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    //   this.invoiceConfiguration = invoiceConfiguration.data;
    // }

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale) {
      order.vatRate = -1;
    }
    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple) {
      order.vatRate = 0;
    }
    order.code = 'ĐH ' + this.indexOrder.toString();
    this.indexOrder++;
    if (this.listCustomer.length) {
      order.customerId = this.listCustomer[0].id;
      order.customerName = this.listCustomer[0].name;
    }
    this.getTaxAuthorityCode(order);
    this.orderSelected = order;
    this.listOrder.push(this.orderSelected);
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

  saveOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }

    const dialogRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.value = new DialogModal(
      ModalHeader.SAVE_ORDER,
      ModalContent.SAVE_ORDER,
      ModalBtn.SAVE_ORDER,
      'check',
      'btn-save'
    );
    dialogRef.componentInstance.formSubmit.subscribe(async res => {
      if (res) {
        if (this.orderSelected.id) {
          const res: any = await this.updateById(offline_bill, this.orderSelected.id, {
            ...this.orderSelected,
            billDate: this.orderSelected.billDate.format(DATE_TIME_FORMAT),
          });
          if (res) {
            dialogRef.close();
            this.toast.success('Cập nhật đơn hàng offline thành công');
          }
        } else {
          const res: any = await this.addItem(offline_bill, {
            ...this.orderSelected,
            billDate: this.orderSelected.billDate.format(DATE_TIME_FORMAT),
          });
          if (res) {
            dialogRef.close();
            this.toast.success('Lưu đơn hàng offline thành công');
            this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
            if (this.listOrder.length) {
              this.orderSelected = this.listOrder[0];
            } else {
              this.createOrder();
            }
          }
        }
      }
    });
  }

  openConfirmCheckoutForm() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.status = 'offline';
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.toast.success('Thanh toán đơn hàng thành công !');
        this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== this.orderSelected.taxAuthorityCode);
        if (this.listOrder.length) {
          this.orderSelected = this.listOrder[0];
        } else {
          this.createOrder();
        }
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
  }

  checkProductDiscountTaxOrder() {
    let productDiscountTaxOrder;
    this.orderSelected.products.forEach(product => {
      if (product.feature === 3) {
        productDiscountTaxOrder = product;
      }
    });
    if (!productDiscountTaxOrder) {
      const productOrder = this.listProduct?.find(product => product.productCode === 'SP1') || this.productDiscountTaxOrder;
      productOrder && (productDiscountTaxOrder = this.addProductToCustomerOrder(productOrder));
    }
    return productDiscountTaxOrder;
  }

  changeDiscountAmountOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    let productDiscountTaxOrder = this.checkProductDiscountTaxOrder();
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.productDiscountTaxOrder = productDiscountTaxOrder;
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = 'discountAmount';
    dialogRef.closed.subscribe(res => {
      if (res) {
        const index = this.orderSelected.products.findIndex(product => product.feature === 3);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
          this.productDiscountTaxOrderSelected = { ...res };
          this.updateOrder();
        }
      }
    });
  }

  changeVatRateOrder() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
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
          product.vatRate = this.orderSelected.vatRate;
          if (product.feature === 1) {
            product.vatRateName = 'Thuế suất' + product.vatRate + '%';
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
    let productDiscountTaxOrder = this.checkProductDiscountTaxOrder();
    const dialogRef = this.modalService.open(DiscountTaxOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.productSelected = productDiscountTaxOrder;
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.componentInstance.title = 'vatAmount';
    dialogRef.closed.subscribe(vatAmount => {
      if (vatAmount) {
        this.orderSelected.vatAmount = vatAmount;
        this.orderSelected.totalAmount = this.orderSelected.totalPreTax + vatAmount;
      }
    });
  }

  openDiscountTaxProductComponent(productSelected: ProductBill) {
    const dialogRef = this.modalService.open(DiscountTaxProductComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.productSelected = productSelected;
    dialogRef.componentInstance.invoiceConfiguration = this.invoiceConfiguration;
    dialogRef.componentInstance.lastCompany = this.lastCompany;
    dialogRef.closed.subscribe((res?: ProductBill) => {
      if (res) {
        const index = this.orderSelected.products.findIndex(product => product.productProductUnitId === res.productProductUnitId);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
          res.vatRateName === 'Khác' && (this.orderSelected.products[index].vatRateName = 'Khác: (' + res.vatRate + '%)');
        }
        this.updateOrder();
      }
    });
  }

  selectedAreaUnit(event) {
    this.orderSelected.areaId = event.areaId;
    this.orderSelected.areaName = event.areaName;
    this.orderSelected.areaUnitId = event.id;
    this.orderSelected.areaUnitName = event.name;
    this.menuType = 0;
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
    this.activeTotalProductGroup = true;
    if (event && groupProduct) {
      if (event.target.checked) {
        groupProduct.status = event.target.checked;
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
      this.filterProduct.groupIds = [];
      this.listProductGroup?.forEach(productGroup => {
        productGroup.status = event.target.checked;
        if (event.target.checked) {
          this.filterProduct.groupIds.push(productGroup.id);
        }
      });
    }
    this.filterProduct.page = Page.PAGE_NUMBER;
    this.listProduct = [];
    this.getListProduct();
  }

  showDetailProduct(product: IProduct) {
    const dialogRef = this.modalService.open(ProductDetailComponent, { size: 'xl', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.product = product;
    dialogRef.closed.subscribe(res => {
      if (res) {
        this.checkVatProduct(product);
      }
    });
  }

  addProduct() {
    const dialogRef = this.modalService.open(ModalCreateProductComponent, { size: 'fullscreen', backdrop: 'static' });
    dialogRef.componentInstance.id = null;
    dialogRef.closed.subscribe((res?: any) => {
      if (res) {
        this.listProduct?.unshift({
          ...res.data,
          productName: res.data.name,
          productCode: res.data.code,
          productProductUnitId: res.data.conversionUnits[0].id,
        });
      }
    });
  }

  addCustomer() {
    // open customer popup component
    const dialogRef = this.modalService.open(CreateStaffComponent, { size: 'xl', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.closed.subscribe(result => {
      this.listCustomer = this.listCustomer.concat(result);
      this.orderSelected.customerId = result.id;
      this.orderSelected.customerName = result.name;
    });
  }

  toggleStatusDesciptionOrder() {
    this.statusDesciptionOrder = !this.statusDesciptionOrder;
    if (this.statusDesciptionOrder) {
      this.desciptionOrder.nativeElement.focus();
    }
  }

  checkVatProduct(product: IProduct) {
    // this.clicks++;
    // if (this.clicks === 1) {
    //   setTimeout(() => {
    //     if (this.clicks === 1) {

    //     }
    //     this.clicks = 0;
    //   }, 200);
    // }

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
        this.addProductToCustomerOrder(product);
      });
    } else {
      this.addProductToCustomerOrder(product);
    }
  }

  changeProductSelectedQuantity(productSelected) {
    this.changeProductSelected(productSelected);
  }

  addProductToCustomerOrder(product: IProduct) {
    let findProduct = false;
    let newProductSelected;
    this.orderSelected.products.forEach(productSelected => {
      if (productSelected.productProductUnitId === product.productProductUnitId) {
        productSelected.quantity && productSelected.quantity++;
        newProductSelected = productSelected;
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
        product.vatRate = 0;
      }

      newProductSelected = {
        imageUrl: product.imageUrl,
        productProductUnitId: product.productProductUnitId,
        productName: product.productCode === 'SP1' ? 'Chiết khấu đơn hàng' : product.productName,
        productCode: product.productCode,
        quantity: 1,
        unit: product.unit,
        unitId: product.unitId,
        unitPrice: product.salePrice,
        discountAmount: 0,
        amount: 0,
        totalPreTax: 0,
        vatRate: product.vatRate ?? 0,
        vatRateName,
        vatAmount: 0,
        totalAmount: 0,
        feature: product.productCode === 'SP1' ? 3 : 1,
        typeDiscount: 'Giảm giá trị',
        discountPercent: 0,
        position: this.orderSelected.products.length + 1,
      };
      this.orderSelected.products.push(newProductSelected);
      newProductSelected.feature === 1 && this.orderSelected.countProduct++;
    }
    this.changeProductSelected(newProductSelected);
    return newProductSelected;
  }

  changeProductSelected(productSelected?: ProductBill) {
    if (productSelected) {
      const vatRate = productSelected.vatRate > 0 ? productSelected.vatRate : 0;
      productSelected.amount = take_decimal_number(productSelected.quantity * productSelected.unitPrice, this.lastCompany.roundScaleAmount);
      if (productSelected.discountPercent) {
        productSelected.discountAmount = take_decimal_number(
          (productSelected.discountPercent * productSelected.amount) / 100,
          this.lastCompany.roundScaleAmount
        );
      }
      // Thành tiền sản phẩm
      productSelected.totalPreTax = take_decimal_number(
        productSelected.amount - (productSelected.discountAmount || 0),
        this.lastCompany.roundScaleAmount
      );
      productSelected.vatAmount = take_decimal_number(productSelected.totalPreTax * (vatRate / 100), this.lastCompany.roundScaleAmount);
      productSelected.totalAmount = take_decimal_number(
        productSelected.totalPreTax + productSelected.vatAmount,
        this.lastCompany.roundScaleAmount
      );
    }
    this.updateOrder();
  }

  updateOrder() {
    let productDiscountTaxOrder;
    this.orderSelected.totalAmount = 0;
    this.orderSelected.amount = 0;
    this.orderSelected.productDiscountAmount = 0;
    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple) {
      this.orderSelected.vatAmount = 0;
    }
    this.orderSelected.productTaxAmount = 0;
    // const vatRate = this.orderSelected.vatRate > 0 ? this.orderSelected.vatRate : 0;

    this.orderSelected.products.forEach(productSelected => {
      if (productSelected.feature === 1) {
        this.orderSelected.quantity += productSelected.quantity;
        // tạm tính
        this.orderSelected.amount += productSelected.totalPreTax;
        this.orderSelected.productDiscountAmount += productSelected.discountAmount ? productSelected.discountAmount : 0;
        if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple) {
          this.orderSelected.vatAmount += productSelected.vatAmount;
        }
      } else {
        productDiscountTaxOrder = productSelected;
      }
    });

    productDiscountTaxOrder && this.updateProductDiscountTaxOrder(productDiscountTaxOrder, this.orderSelected);

    // Giảm giá đơn hàng
    this.orderSelected.discountAmount = productDiscountTaxOrder?.totalPreTax || 0;
    // Tổng tiền trước thuế
    this.orderSelected.totalPreTax = this.orderSelected.amount - (productDiscountTaxOrder?.totalPreTax || 0);
    this.orderSelected.quantity = this.orderSelected.countProduct;

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceMultiple) {
      this.orderSelected.vatAmount = this.orderSelected.vatAmount - (productDiscountTaxOrder?.vatAmount || 0);
    }
    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceOne) {
      this.orderSelected.vatAmount = take_decimal_number(
        this.orderSelected.totalPreTax * (getPositiveNumber(this.orderSelected.vatRate) / 100),
        this.lastCompany.roundScaleAmount
      );
    }
    this.orderSelected.totalAmount = this.orderSelected.totalPreTax + (this.orderSelected.vatAmount || 0);
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
    this.activeRemoveProductSelected = 0;
  }

  confirmRemoveProductSelected(product: ProductBill) {
    this.activeRemoveProductSelected = product.productProductUnitId;
  }

  removeProductFromCustomerOrder(product: ProductBill) {
    this.orderSelected.products = this.orderSelected.products.filter(
      productSelected => product.productProductUnitId !== productSelected.productProductUnitId
    );
    this.orderSelected.countProduct--;
    this.activeRemoveProductSelected = 0;
    this.changeProductSelected();
  }

  removeOrderFromOrderList(event, orderSelected: IBillPayment) {
    event.stopPropagation();
    this.listOrder = this.listOrder.filter(order => order.taxAuthorityCode !== orderSelected.taxAuthorityCode);
    if (!this.listOrder.length) {
      this.createOrder();
    } else {
      this.orderSelected = this.listOrder[0];
    }
  }

  increaseProductQuantity(product: ProductBill) {
    product.quantity && product.quantity++;
    this.changeProductSelected(product);
  }

  decreaseProductQuantity(product: ProductBill) {
    if (product.quantity <= 1) {
      this.confirmRemoveProductSelected(product);
    } else {
      product.quantity && product.quantity--;
    }
    this.changeProductSelected(product);
  }

  async loadMore(event) {
    if (event.target.scrollTop === 0) {
      return;
    }
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight) {
      this.filterProduct.page !== undefined && (this.filterProduct.page += 1);
      let listProductLocal = await this.getProductListtWithPaging('product', this.filterProduct);
      console.log(listProductLocal);
      this.listProduct?.push(...listProductLocal);
    }
  }

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  ngAfterViewChecked() {
    // if (this.invoiceConfiguration && !this.afterViewInit) {
    //   this.scanBarCodeProduct.nativeElement.focus();
    // }
  }

  ngAfterViewInit() {
    const targets = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));
    targets.map(function (target: any) {
      target.onclick = function (e) {
        e.preventDefault();
        const targetBtn = e.target;
        const targetFilter = targetBtn.getAttribute('data-filter');
        targetBtn.classList.add('active');
        const allFilter = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));
        allFilter.map(function (filterElm: any) {
          const filterElmFilter = filterElm.getAttribute('data-filter');
          if (targetFilter !== filterElmFilter) {
            filterElm.classList.remove('active');
          }
        });

        const allContent = [].slice.call(document.querySelectorAll('.pos-content [data-type]'));
        allContent.map(function (contentElm: any) {
          const contentType = contentElm.getAttribute('data-type');

          if (targetFilter === 'all') {
            contentElm.classList.remove('d-none');
          } else {
            if (contentType !== targetFilter) {
              contentElm.classList.add('d-none');
            } else {
              contentElm.classList.remove('d-none');
            }
          }
        });
      };
    });
  }

  togglePosSidebarFooter() {
    this.posSidebarFooterStatus = !this.posSidebarFooterStatus;
  }

  openPopup() {
    this.modalService.open(ModalDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
  }

  printPage() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(PosInvoiceComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
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
  }

  numberOnly(event: any): boolean {
    return numberOnly(event);
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }
}
