import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, ParamMap } from '@angular/router';
import { firstValueFrom, Subscription } from 'rxjs';
import { ToastrService } from 'ngx-toastr';
import { RxStompService } from '../../../../rxStomp/rx-stomp.service';
import { ProductService } from '../../service/product.service';
import { Page } from '../../../const/customer-order.const';
import { IProduct } from '../../model/product.model';
import { IBillPayment, ProductBill } from '../../model/bill-payment.model';
import { validVariable } from '../../../const/function';
import dayjs from 'dayjs/esm';
import { BillService } from '../../service/bill.service';
import { WatchBillComponent } from '../watch-bill/watch-bill.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-khachqr-sc',
  templateUrl: './khachqr-sc.component.html',
  styleUrls: ['./khachqr-sc.component.scss']
})
export class KhachqrScComponent implements OnInit, OnDestroy {
  isAdmin = false;
  idAmin: any = 0;
  idTable: any = 0;
  orderSelected: any = {
    totalAmount: 0,
    products: [],
    deliveryType: 1
  };
  menuType = 0;
  receivedMessages: any[] = [];
  objectInfo: any = {};
  message: string = '';
  listProduct: any = [];
  loading = true;
  filterProduct: any = { page: Page.PAGE_NUMBER, size: 15, companyId: null , keyword :''};
  activeRemoveProductSelected: any = 0;
  private topicSubscription: Subscription | undefined;
  private isStopLoading = false;

  constructor(
    protected productService: ProductService,
    private rxStompService: RxStompService,
    private route: ActivatedRoute,
    protected orderService: BillService,
    private toast: ToastrService,
    protected modalService: NgbModal) {
  }

  async ngOnInit(): Promise<void> {
    try {
      const params: ParamMap = await firstValueFrom(this.route.paramMap);
      this.idAmin = params.get('idA');
      this.idTable = params.get('idT');
      this.filterProduct.companyId = this.idAmin;
      console.log('idAmin:', this.idAmin);
      console.log('idTable:', this.idTable);
      this.topicSubscription = this.rxStompService.watch(`/topic/messages/${this.idAmin}/${this.idTable}`).subscribe((res: any) => {
        let data = JSON.parse(res.body);
        this.receivedMessages.push(data);
      });
      this.getListProduct();
      // Sử dụng idA và idB ở đây
    } catch (error) {
      console.error('Error fetching route params:', error);
    }
  }

  getListProduct() {
    this.productService.query(this.filterProduct).subscribe(res => {
      this.loading = false;
      const result = res.body;
      result?.forEach(item => {
        this.listProduct?.push(item);
      });
      if (result && result.length == 0) this.isStopLoading = true;
      console.log(this.listProduct);
    });
  }

  ngOnDestroy() {
    if (this.topicSubscription) this.topicSubscription.unsubscribe();
  }

  onSendMessage() {
    if (!this.message) return;
    if (!this.idAmin || !this.idTable) {
      this.toast.error('Error url not valid!');
      console.log('adminId = ' + this.idAmin);
      console.log('tableId = ' + this.idTable);
      return;
    }
    let senderObj = {
      type: 1,
      userId: this.idAmin,
      tableId: this.idTable,
      content: this.message
    };
    this.rxStompService.publish({ destination: '/app/sendMessage', body: JSON.stringify(senderObj) });
    this.message = '';
  }


  addProductToOrder(product: IProduct) {
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
    this.orderSelected.quantity = 0;
    this.orderSelected.products.forEach(productSelected => {
      this.orderSelected.quantity += productSelected.quantity;
      this.orderSelected.amount += productSelected.amount;
    });
    this.orderSelected.totalAmount = this.orderSelected.amount;
  }

  removeProductFromCustomerOrder(product: ProductBill) {
    this.orderSelected.products = this.orderSelected.products.filter(
      productSelected => product.productId !== productSelected.productId
    );
    this.activeRemoveProductSelected = 0;
    this.changeProductSelected();
  }

  increaseProductQuantity(product: ProductBill) {
    product.quantity && product.quantity++;
    this.changeProductSelected(product);
  }

  decreaseProductQuantity(product: ProductBill) {
    if (product.quantity === 1) {
      this.activeRemoveProductSelected = product.productId;
    } else {
      product.quantity && product.quantity--;
    }
    this.changeProductSelected(product);
  }

  goiMon() {
    if (!this.checkOrder(this.orderSelected)) {
      return;
    }
    this.orderService.create3({
      ...this.orderSelected,
      billDate: dayjs(),
      tableId: this.idTable,
      comId: this.idAmin
    }).subscribe(res => {
      if (res.body?.status) {
        this.toast.success('Thành công');
        this.orderSelected = {
          totalAmount: 0,
          products: [],
          deliveryType: 1
        };
      }
    });
  }

  checkOrder(orderSelected: IBillPayment) {
    const listProperty = ['products'];
    const listValue = ['sản phẩm'];
    for (let i = 0; i < listProperty.length; i++) {
      if (!validVariable(orderSelected[listProperty[i]])) {
        this.toast.warning(`Vui lòng chọn ${listValue[i]}`);
        return false;
      }
    }
    return true;
  }

  loadMore($event) {
    if (this.isStopLoading) return;
    console.log("load more");
    this.filterProduct.page ++;
    this.getListProduct();
  }

  xemBill() {
    this.orderService.findByTableAndComId({ tableId: this.idTable, comId: this.idAmin })
      .subscribe(
        (res) => {
          const dialogRef = this.modalService.open(WatchBillComponent, {
            size: 'md',
            backdrop: 'static',
            windowClass: 'margin-5'
          });
          dialogRef.componentInstance.orderSelected = res.body.data;
        }, error => {
          this.toast.success('Bạn chưa gọi sản phẩm nào');
        }
      );
  }
}
