import { Component, OnDestroy, OnInit } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import {
  DeliveryType,
  LIST_PRODUCT,
  MenuType,
  Page,
  PaymentMethod,
  StatusOrder
} from 'app/pages/const/customer-order.const';
import { ProductService } from '../service/product.service';
import { ActivatedRoute, Router } from '@angular/router';
import { debounceTime, pluck, Subscription, tap } from 'rxjs';
import { IProduct } from '../model/product.model';
import { FormControl } from '@angular/forms';
import { validVariable } from 'app/pages/const/function';
import { IBillPayment, Payment, ProductBill } from '../model/bill-payment.model';
import { BillService } from './../service/bill.service';
import dayjs from 'dayjs/esm';
import { ToastrService } from 'ngx-toastr';
import { ConfirmCheckoutComponent } from './confirm-checkout/confirm-checkout.component';
import { FilterProduct } from '../model/filterProduct.model';
import { DiscountTaxProductComponent } from './discount-tax-product/discount-tax-product.component';
import { ProductDetailComponent } from './product-detail/product-detail.component';
import { BaseComponent } from 'app/shared/base/base.component';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { IArea } from 'app/entities/area/area.model';
import { PosInvoiceComponent } from './pos-invoice/pos-invoice.component';
import { RxStompService } from '../../../rxStomp/rx-stomp.service';

@Component({
  selector: 'customer-order',
  templateUrl: './customer-order.component.html',
  styleUrls: ['./customer-order.component.scss']
})
export class PosCustomerOrderPage extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  posMobileSidebarToggled = true;
  deliveryType = DeliveryType;
  clicks = 0;
  loading = true;

  menuType: number = MenuType.ProductGroup;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: 15 };
  query: FormControl = new FormControl('');

  listProduct?: IProduct[] | null = [];
  listArea: IArea[] = [];
  listAreaShow: IArea[] = [];
  listOrder: IBillPayment[] = [];
  orderSelected!: IBillPayment;

  posSidebarFooterStatus = true;
  idOrderSelected: number;
  activeRemoveProductSelected = 0;
  activeBorderProductGroup = 0;
  activeBorderArea = 0;
  activeCreateOrder: boolean | null = null;
  receivedMessages: any[] = [];
  message: string = '';
  idTable: any = 1;
  private lastCompany: any;
  private topicSubscription: Subscription | undefined;


  constructor(
    private rxStompService: RxStompService,
    protected productService: ProductService,
    protected activatedRoute: ActivatedRoute,
    protected orderService: BillService,
    public router: Router,
    private confirmDialog: ConfirmDialogService,
    private toast: ToastrService,
    protected modalService: NgbModal
  ) {
    super();
    this.appSettings.appEmpty = true;
    this.appSettings.appContentFullHeight = true;
  }

  async ngOnInit() {
    this.searchProduct();
    this.getListProduct();
    this.lastCompany = await this.getCompany();
    this.activatedRoute.params.pipe(pluck('id')).subscribe(id => {
      if (id) {
        this.idOrderSelected = id;
        this.getOrderDetailById(id);
      } else {
        this.createOrder();
      }
    });
    try {
      this.topicSubscription = this.rxStompService.watch(`/topic/messages/${this.lastCompany.id}`).subscribe((res: any) => {
        let data = JSON.parse(res.body);
        this.receivedMessages.push(data);
      });
      // Sử dụng idA và idB ở đây
    } catch (error) {
      console.error('Error fetching route params:', error);
    }
  }

  searchProduct() {
    this.query.valueChanges
      .pipe(
        debounceTime(500),
        tap(value => {
          this.filterProduct.page = 0;
          this.filterProduct.keyword = value;
          this.listProduct = [];
        })
      )
      .subscribe(() => this.getListProduct());
  }

  getOrderDetailById(id) {
    this.orderService.find(id).subscribe((res: any) => {
      this.orderSelected = res.body.data;
      this.listOrder.push(this.orderSelected);
    });
  }

  trackId = (_index: number, item: IProduct): number => item.id;

  getListProduct() {
    this.productService.query(this.filterProduct).subscribe(res => {
      this.loading = false;
      const result = res.body;
      result?.forEach(item => {
        this.listProduct?.push(item);
      });
    });
  }

  onSendMessage() {
    if (!this.message) return;
    if (!this.lastCompany || !this.idTable) {
      this.toast.error('Error url not valid!');
      console.log('adminId = ' + this.lastCompany);
      console.log('tableId = ' + this.idTable);
      return;
    }
    let senderObj = {
      type: 0,
      userId: this.lastCompany.id,
      tableId: this.idTable,
      content: this.message
    };
    this.rxStompService.publish({ destination: '/app/sendMessage', body: JSON.stringify(senderObj) });
    this.message = '';
  }

  async createOrder() {
    const order: IBillPayment = new IBillPayment();
    order.payment = new Payment();
    order.payment.paymentMethod = PaymentMethod.cash;
    order.deliveryType = DeliveryType.GET_AWAY;
    order.billDate = dayjs();
    order.status = StatusOrder.NOT_COMPLETE;
    order.products = [];
    order.customerName = 'khách lẻ';
    order.comId = this.lastCompany.id;
    this.orderSelected = order;
    this.listOrder.push(this.orderSelected);
  }

  openConfirmCheckoutForm() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.toast.success('Thanh toán đơn hàng thành công !');
        //TODO fix taxAuthorityCode
        this.listOrder = this.listOrder.filter(order => order.code !== this.orderSelected.code);
        if (this.listOrder.length) {
          this.orderSelected = this.listOrder[0];
        } else {
          this.createOrder();
        }
      }
    });
  }

  checkOrder(orderSelected: IBillPayment) {
    const listProperty = ['customerName', 'products'];
    const listValue = ['khách hàng', 'sản phẩm'];
    if (this.orderSelected.deliveryType === DeliveryType.AT_TABLE) {
      listProperty.push('areaId');
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

  openDiscountTaxProductComponent(productSelected: ProductBill) {
    const dialogRef = this.modalService.open(DiscountTaxProductComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.productSelected = productSelected;
    dialogRef.closed.subscribe((res?: ProductBill) => {
      if (res) {
        const index = this.orderSelected.products.findIndex(product => product.productProductUnitId === res.productProductUnitId);
        if (index !== -1) {
          this.orderSelected.products[index] = res;
        }
        this.updateOrder();
      }
    });
  }

  filterProductByProductGroupId(groupId?: number) {
    if (groupId) {
      this.filterProduct.groupId = groupId;
      this.activeBorderProductGroup = groupId;
    } else {
      this.filterProduct.groupId = undefined;
      this.activeBorderProductGroup = 0;
    }
    this.filterProduct.page = Page.PAGE_NUMBER;
    this.listProduct = [];
    this.getListProduct();
  }

  showDetailProduct(product: IProduct) {
    const dialogRef = this.modalService.open(ProductDetailComponent, {
      size: 'xl',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.product = product;
    dialogRef.closed.subscribe(res => {
      if (res) {
        this.checkVatProduct(product);
      }
    });
  }

  addProduct() {
    this.router.navigate([`${LIST_PRODUCT}`]);
  }

  checkVatProduct(product: IProduct) {
    this.addProductToCustomerOrder(product);
  }

  changeProductSelectedQuantity(productSelected) {
    if (productSelected.quantity === 0) {
      this.removeProductFromCustomerOrder(productSelected);
    } else {
      this.changeProductSelected(productSelected);
    }
  }

  addProductToCustomerOrder(product: IProduct) {
    let findProduct = false;
    let newProductSelected;
    this.orderSelected.products.forEach(productSelected => {
      if (productSelected.productCode === product.code) {
        productSelected.quantity && productSelected.quantity++;
        newProductSelected = productSelected;
        findProduct = true;
      }
    });
    if (!findProduct) {
      newProductSelected = {
        productId: product.id,
        imageUrl: product.imageUrl,
        productName: product.name,
        productCode: product.code,
        quantity: 1,
        unit: product.unit,
        unitPrice: product.salePrice,
        amount: 0,
        totalPreTax: 0,
        totalAmount: 0,
        position: this.orderSelected.products.length + 1
      };
      this.orderSelected.products.push(newProductSelected);
    }
    this.changeProductSelected(newProductSelected);
  }

  changeProductSelected(newProductSelected?: ProductBill) {
    if (newProductSelected) {
      newProductSelected.amount = newProductSelected.quantity * newProductSelected.unitPrice;
      newProductSelected.totalAmount = newProductSelected.amount;
    }
    this.updateOrder();
  }

  updateOrder() {
    this.orderSelected.totalAmount = 0;
    this.orderSelected.amount = 0;

    this.orderSelected.products.forEach(productSelected => {
      this.orderSelected.quantity += productSelected.quantity;
      this.orderSelected.amount += productSelected.amount;
    });
    this.orderSelected.totalAmount = this.orderSelected.amount;
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
    this.activeRemoveProductSelected = 0;
    this.changeProductSelected();
  }

  removeOrderFromOrderList(orderSelected: IBillPayment) {
    // TODO fix taxAuthorityCode
    this.listOrder = this.listOrder.filter(order => order.code !== orderSelected.code);
  }

  increaseProductQuantity(product: ProductBill) {
    product.quantity && product.quantity++;
    this.changeProductSelected(product);
  }

  decreaseProductQuantity(product: ProductBill) {
    if (product.quantity === 1) {
      this.confirmRemoveProductSelected(product);
    } else {
      product.quantity && product.quantity--;
    }
    this.changeProductSelected(product);
  }

  loadMore(event) {
    console.log(this.listProduct);
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

  ngAfterViewInit() {
    const targets = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));

    targets.map(function(target: any) {
      target.onclick = function(e) {
        e.preventDefault();
        const targetBtn = e.target;
        const targetFilter = targetBtn.getAttribute('data-filter');
        targetBtn.classList.add('active');
        const allFilter = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));
        allFilter.map(function(filterElm: any) {
          const filterElmFilter = filterElm.getAttribute('data-filter');
          if (targetFilter !== filterElmFilter) {
            filterElm.classList.remove('active');
          }
        });

        const allContent = [].slice.call(document.querySelectorAll('.pos-content [data-type]'));
        allContent.map(function(contentElm: any) {
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
    const dialogRef = this.modalService.open(PosInvoiceComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5'
    });
    dialogRef.componentInstance.orderSelected = this.orderSelected;
  }

  ngOnDestroy() {
    if (this.topicSubscription) this.topicSubscription.unsubscribe();
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  routerHome() {
    this.router.navigate(['tong-quan']);
  }
}
