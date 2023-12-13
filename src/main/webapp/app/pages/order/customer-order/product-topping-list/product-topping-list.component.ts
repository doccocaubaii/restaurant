import { Component, Input, OnInit } from '@angular/core';
import { IProduct } from '../../model/product.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ProductService } from '../../service/product.service';
import { ToastrService } from 'ngx-toastr';
import { InvoiceType } from 'app/pages/const/customer-order.const';
import { changeProductSelected, checkOverStockProduct, generateRandomString, getQuantityProduct } from 'app/pages/const/function';
import { InvoiceConfiguration, LastCompany, ProductBill } from '../../model/bill-payment.model';

@Component({
  selector: 'jhi-product-topping-list',
  templateUrl: './product-topping-list.component.html',
  styleUrls: ['./product-topping-list.component.scss'],
})
export class ProductToppingListComponent implements OnInit {
  @Input() product: IProduct;
  @Input() productOrder: ProductBill;
  invoiceConfiguration: InvoiceConfiguration;
  lastCompany: LastCompany;

  listProductTopping: any = [];
  productLocal;
  productOrderLocal;
  orderSelected: any;
  invoiceType = InvoiceType;

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
    this.productLocal = JSON.parse(JSON.stringify(this.product || null));
    this.productOrderLocal = JSON.parse(JSON.stringify(this.productOrder || null));
    if (!this.productOrderLocal) {
      this.productOrderLocal = this.convertProductToProductOrder(this.productLocal);
    }
    this.getProductTopping(this.productOrderLocal.productId);
  }

  getProductTopping(idProduct: number) {
    this.productService.getProductTopping(idProduct).subscribe((res: any) => {
      this.listProductTopping = res.body;

      if (this.productOrderLocal) {
        this.productOrderLocal.toppings?.forEach(toppingProduct => {
          this.listProductTopping.forEach(productTopping => {
            productTopping.products.forEach(eachProductTopping => {
              if (eachProductTopping.productProductUnitId == toppingProduct.productProductUnitId) {
                eachProductTopping.quantity = toppingProduct.displayQuantity;
                eachProductTopping.inProductOrder = true;
              }
            });
          });
        });
      }
    });
  }

  convertProductToProductOrder(product: IProduct) {
    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceSale) {
      product.vatRate = -1;
    }

    // if (
    //   this.invoiceConfiguration.invoiceType == this.invoiceType.invoiceSale &&
    //   this.invoiceConfiguration.discountVat == 1 &&
    //   this.invoiceConfiguration.taxReductionType == 1
    // ) {
    //   product.vatRate = product.discountVatRate || -1;
    // }

    if (this.invoiceConfiguration.invoiceType === this.invoiceType.invoiceOne) {
      if (this.orderSelected.vatRate) {
        product.vatRate = this.orderSelected.vatRate;
      } else {
        product.vatRate = 0;
      }
    }
    const productSelected = {
      productId: product.productId,
      imageUrl: product.imageUrl,
      productProductUnitId: product.productProductUnitId,
      productName: product.productCode === 'SP1' ? 'Chiết khấu đơn hàng' : product.productName,
      productCode: product.productCode,
      quantity: product.productCode === 'SPGC' || product.productCode === 'SP1' ? 0 : getQuantityProduct(product),
      displayQuantity: product.productCode === 'SPGC' || product.productCode === 'SP1' ? 0 : getQuantityProduct(product),
      quantityProduct: product.inventoryCount || 0,
      unit: product.unit,
      unitId: product.unitId,
      unitPrice: product.salePrice,
      discountAmount: 0,
      amount: product.salePrice,
      totalPreTax: 0,
      vatRate: product.vatRate !== -4 ? product.vatRate : 0,
      vatAmount: 0,
      inventoryTracking: product.inventoryTracking,
      totalAmount: 0,
      feature: product.productCode === 'SP1' ? 3 : product.productCode === 'SPGC' ? 4 : 1,
      typeDiscount: 'Giảm giá trị',
      discountPercent: 0,
      position: this.productOrderLocal?.toppings?.length || 0 + 1,
      toppings: [],
      isTopping: product.isTopping,
      displayAmount: product.salePrice,
      totalAmountTopping: 0,
      parentProductId: this.productOrderLocal?.productProductUnitId || null,
      haveTopping: true,
      productUniqueId: generateRandomString(),
      discountVatRate: product.discountVatRate || 0,
      totalDiscount: 0,
      voucherProducts: [],
    };
    return productSelected;
  }

  checkProductToppingQuantity(productTopping) {
    if (productTopping.quantity == 0 || !productTopping.quantity) {
      productTopping.inProductOrder = false;
      this.removeProductFromProductOrder(productTopping);
      this.syncProductTopping(productTopping);
      this.updateProductOrder(this.productOrderLocal);
    }
  }

  removeProductFromProductOrder(productTopping) {
    this.productOrderLocal.toppings = this.productOrderLocal.toppings.filter(topping => {
      return topping.productProductUnitId !== productTopping.productProductUnitId;
    });
  }

  changeProductToppingQuantity(productTopping) {
    this.syncProductTopping(productTopping);
    const productToppingLocal = this.productOrderLocal.toppings.find(
      product => product.productProductUnitId == productTopping.productProductUnitId
    );
    productToppingLocal.quantity = productToppingLocal.displayQuantity = productTopping.quantity;
    this.updateProductTopping(productToppingLocal);
  }

  syncProductTopping(productTopping) {
    this.listProductTopping.forEach(products => {
      products.products.forEach(productToppingItem => {
        if (productToppingItem.productProductUnitId == productTopping.productProductUnitId) {
          productToppingItem.quantity = productTopping.quantity;
          productToppingItem.inProductOrder = productTopping.inProductOrder;
        }
      });
    });
  }

  addToppingToProduct(productTopping) {
    let checkOverStock = !this.invoiceConfiguration.overStock && productTopping.inventoryTracking;
    if (checkOverStock && productTopping.inventoryCount < 1) {
      this.toast.warning(
        `Sản phẩm ${productTopping.productName} xuất quá số lượng tồn (${productTopping.inventoryCount} ${productTopping.unit || ''})`
      );
      return;
    }

    let productToppingLocal;
    this.listProductTopping.forEach(products => {
      products.products.forEach(productToppingItem => {
        if (productToppingItem.productProductUnitId == productTopping.productProductUnitId) {
          productToppingItem.inProductOrder = true;
          productToppingItem.quantity = getQuantityProduct(productTopping);
          productToppingLocal = productToppingItem;
        }
      });
    });

    let productToppingOrder: any = this.convertProductToProductOrder(productToppingLocal);
    if (this.productOrderLocal.toppings) {
      this.productOrderLocal.toppings.push(productToppingOrder);
    } else {
      this.productOrderLocal.toppings = [productToppingOrder];
    }
    this.updateProductTopping(productToppingOrder);
    // this.convertProductToProductOrder(productTopping);
  }

  checkProductToppingGroup() {
    for (let i = 0; i < this.listProductTopping.length; i++) {
      if (this.listProductTopping[i].requiredOptional) {
        let findToppingProduct = false;
        this.productOrderLocal.toppings.forEach(toppingProduct => {
          this.listProductTopping[i].products.forEach(topping => {
            if (toppingProduct.productProductUnitId == topping.productProductUnitId) {
              findToppingProduct = true;
            }
          });
        });
        if (!findToppingProduct) {
          this.toast.warning('Bạn chưa chọn nhóm sản phẩm bắt buộc');
          return false;
        } else {
          return true;
        }
      }
    }
    return true;
  }

  increaseProduct(product, statusProductLocal) {
    product.quantity++;
    if (statusProductLocal) {
      this.updateProductOrder(product);
    } else {
      let productTopping;
      this.syncProductTopping(product);
      this.productOrderLocal.toppings.forEach(topping => {
        if (topping.productProductUnitId == product.productProductUnitId) {
          topping.displayQuantity++;
          topping.quantity++;
          productTopping = topping;
        }
      });
      let result = this.updateProductTopping(productTopping);
      if (!result) {
        product.quantity--;
        this.syncProductTopping(product);
      }
    }
  }

  decreaseProduct(product, statusProductLocal) {
    if (statusProductLocal) {
      product.quantity--;
      this.updateProductOrder(product);
    } else {
      this.listProductTopping.forEach(products => {
        products.products.forEach(productToppingItem => {
          if (productToppingItem.productProductUnitId == product.productProductUnitId) {
            productToppingItem.quantity--;
            if (productToppingItem.quantity <= 0) {
              productToppingItem.inProductOrder = false;
              this.productOrderLocal.toppings = this.productOrderLocal.toppings.filter(
                productTopping => product.productProductUnitId !== productTopping.productProductUnitId
              );
            }
          }
        });
      });

      this.productOrderLocal.toppings.forEach(topping => {
        if (topping.productProductUnitId == product.productProductUnitId) {
          topping.displayQuantity--;
          topping.quantity--;
        }
      });
      this.updateProductTopping(product);
    }
  }

  updateProductOrder(product) {
    checkOverStockProduct(product, this.invoiceConfiguration, this.toast);
    changeProductSelected(product, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
  }

  updateProductTopping(topping) {
    // changeProductTopping(topping, this.lastCompany, this.invoiceConfiguration, this.invoiceType, this.productOrderLocal)
    let result = checkOverStockProduct(topping, this.invoiceConfiguration, this.toast);
    this.updateProductOrder(this.productOrderLocal);
    return result;
  }

  saveProduct() {
    if (this.productOrderLocal.quantity <= 0) {
      this.toast.warning('Số lượng sản phẩm phải lớn hơn 0');
      return;
    }
    let check = this.checkProductToppingGroup();
    if (!check) {
      return;
    }
    this.activeModal.close(this.productOrderLocal);
  }

  closeModal() {
    this.activeModal.dismiss();
  }
}
