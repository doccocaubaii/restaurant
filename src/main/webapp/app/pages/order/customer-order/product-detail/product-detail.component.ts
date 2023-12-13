import { Component, Input, OnDestroy, OnInit } from '@angular/core';
import { IProduct } from '../../model/product.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-product-detail',
  templateUrl: './product-detail.component.html',
  styleUrls: ['./product-detail.component.scss'],
})
export class ProductDetailComponent implements OnInit, OnDestroy {
  @Input() product: any;
  @Input() productOrder: any;
  listProductTopping: any = [];
  productLocal;
  productOrderLocal;
  constructor(
    protected productService: ProductService,
    private toast: ToastrService,
    public activeModal: NgbActiveModal,
    private location: Location
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    this.product = {
      barcode: '',
      code2: '1309',
      comId: 437,
      conversionUnits: null,
      createTime: '2023-07-03 16:11:57',
      description: '',
      groupToppings: null,
      groups: null,
      haveTopping: true,
      imageUrl: null,
      inventoryCount: 58,
      inventoryTracking: true,
      isPrimary: true,
      isTopping: true,
      productCode: 'SP128',
      productId: 28392,
      productName: '"BVS Diana Sensi Siêu mỏng cánh 8 "',
      productProductUnitId: 24475,
      purchasePrice: 15706.431034,
      salePrice: 26000,
      unit: 'Gói',
      unitId: 16926,
      updateTime: '2023-07-04 16:21:04',
      vatRate: -4,
    };

    this.productLocal = JSON.parse(JSON.stringify(this.product || null));
    this.productOrderLocal = JSON.parse(JSON.stringify(this.productOrder || null));
    if (!this.productOrderLocal) {
      this.convertProductToProductOrder(this.productLocal);
    }
    this.getProductTopping(29192);
  }

  getProductTopping(idProduct: number) {
    this.productService.getProductTopping(idProduct).subscribe((res: any) => {
      this.listProductTopping = res.body;

      if (this.productOrderLocal) {
        this.productOrderLocal.toppings.forEach(toppingProduct => {
          this.listProductTopping.products.forEach(eachProductTopping => {
            if (eachProductTopping.productId == toppingProduct.productId) {
              eachProductTopping.quantityProductOrder = toppingProduct.quantity;
            }
          });
        });
      }
    });
  }

  convertProductToProductOrder(product: IProduct) {
    const productSelected = {
      productId: product.productId,
      imageUrl: product.imageUrl,
      productProductUnitId: product.productProductUnitId,
      productName: product.productCode === 'SP1' ? 'Chiết khấu đơn hàng' : product.productName,
      productCode: product.productCode,
      quantity: product.productCode === 'SPGC' || product.productCode === 'SP1' ? 0 : 1,
      quantityProduct: product.inventoryCount || 0,
      unit: product.unit,
      unitId: product.unitId,
      unitPrice: product.salePrice,
      discountAmount: 0,
      amount: 0,
      totalPreTax: 0,
      vatRate: product.vatRate ?? 0,
      vatAmount: 0,
      inventoryTracking: product.inventoryTracking,
      totalAmount: 0,
      feature: product.productCode === 'SP1' ? 3 : product.productCode === 'SPGC' ? 4 : 1,
      typeDiscount: 'Giảm giá trị',
      discountPercent: 0,
      position: this.productOrderLocal?.products?.length || 0 + 1,
      toppings: [],
    };
    return productSelected;
  }

  addToppingToProduct(productTopping) {
    // let findProductTopping = false;
    // this.productOrderLocal.products.forEach(productOrder => {
    //   if(productOrder.productId == productTopping.productId){
    //     this.productOrderLocal.quantity++;
    //     findProductTopping = true;
    //   }
    // })
    // if(!findProductTopping){
    //   this.productOrderLocal.
    // }
    this.productOrderLocal.products.push({
      ...productTopping,
      parentProductId: this.productOrderLocal.productProductUnitId,
    });
  }

  checkOverStockProduct(productToppingOrder, productTopping, invoiceConfiguration) {
    if (!invoiceConfiguration.overStock && productToppingOrder.quantity > productTopping.quantity && productTopping.inventoryTracking) {
      this.toast.warning(
        `Sản phẩm ${productTopping.productName} xuất quá số lượng tồn (${productTopping.quantityProduct} ${productTopping.unit})`
      );
      productToppingOrder.quantity = productTopping.quantity;
    }
  }

  increaseToppingInProduct(productTopping) {
    productTopping.quantity++;
  }

  updateUnitPriceProductOrer() {
    this.productOrderLocal.unitPrice = 0;
    this.productOrderLocal.products.forEach(productOrder => {});
  }

  saveProduct() {
    if (this.productOrderLocal) {
      this.activeModal.close(this.productOrderLocal);
    } else {
      this.activeModal.close(this.productLocal);
    }
  }

  closeModal() {
    this.activeModal.dismiss();
  }

  ngOnDestroy() {
    this.activeModal.dismiss();
  }
}
