import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { InvoiceType, SPKMProduct, VoucherType } from 'app/pages/const/customer-order.const';
import {
  addProductSelectedToList,
  changeProductSelected,
  checkOverStockProduct,
  checkOverStockProductPromo,
  convertProductToProductOrder,
  generateRandomString,
  getMaxValueProductPromo,
  removeAccents,
  updateDiscountAmountProductPromo,
} from 'app/pages/const/function';
import { IBillPayment, InvoiceConfiguration, LastCompany, ProductBill } from 'app/pages/order/model/bill-payment.model';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'jhi-product-voucher',
  templateUrl: './product-voucher.component.html',
  styleUrls: ['./product-voucher.component.scss'],
})
export class ProductVoucherComponent implements OnInit {
  @Input() voucher: any;
  @Input() productVoucher: any;
  @Input() productSelected?: any;
  @Input() invoiceConfiguration: InvoiceConfiguration;
  @Input() lastCompany: LastCompany;
  @Input() orderSelected: any;
  @Output() formSubmit: EventEmitter<any> = new EventEmitter<any>();
  invoiceType = InvoiceType;
  searchProduct: string;

  constructor(private toast: ToastrService) {}

  ngOnInit(): void {
    this.voucher = JSON.parse(JSON.stringify(this.voucher));
    this.productVoucher = JSON.parse(JSON.stringify(this.productVoucher));
    this.productVoucher.listProductPromoShow = this.productVoucher.listProductPromo;
    this.orderSelected = JSON.parse(JSON.stringify(this.orderSelected));
    this.productSelected &&
      (this.productSelected = this.orderSelected.products.find(
        productOrder => productOrder.productUniqueId == this.productSelected?.productUniqueId
      ));
  }

  convertProductToProductOrder(product, productVoucher) {
    return convertProductToProductOrder(
      product,
      this.voucher,
      productVoucher,
      this.invoiceConfiguration,
      this.invoiceType,
      this.orderSelected,
      this.productSelected
    );
  }

  searchProductPromo() {
    this.productVoucher.listProductPromoShow = [];
    this.productVoucher.listProductPromo.forEach(product => {
      if (removeAccents(product.productName).toLowerCase().search(removeAccents(this.searchProduct).toLowerCase()) !== -1) {
        this.productVoucher.listProductPromoShow.push(product);
      }
    });
  }

  getMaxValueProductPromo(product) {
    return getMaxValueProductPromo(
      product,
      this.productVoucher,
      this.invoiceConfiguration,
      this.lastCompany,
      this.voucher,
      this.productSelected
    );
  }

  addPromoToProduct(product, productVoucher) {
    if (!this.invoiceConfiguration.overStock && product.inventoryCount <= 0 && product.inventoryTracking) {
      this.toast.warning(`Sản phẩm ${product.productName} xuất quá số lượng tồn (${product.inventoryCount} ${product.unit || ''})`);
      return;
    }
    let productVoucherOrder = this.convertProductToProductOrder(product, productVoucher);
    if (
      !checkOverStockProductPromo(
        { ...productVoucherOrder, quantity: productVoucherOrder.quantity },
        this.voucher,
        this.productVoucher,
        this.invoiceConfiguration,
        this.lastCompany,
        this.toast,
        this.productSelected
      )
    ) {
      return;
    } else {
      product.inProductOrder = true;
      product.quantity = 1;
    }
    this.productVoucher.applyProductVoucher.push(productVoucherOrder);
    if (this.productVoucher.type == VoucherType.type102) {
      addProductSelectedToList(productVoucherOrder, this.orderSelected);
    } else {
      this.productSelected.voucherProducts.push(productVoucherOrder);
    }
    this.changeProductPromoQuantity(product);
  }

  increaseProduct(product) {
    let productApply = this.getProductApplyFromProductItem(product);
    if (
      !checkOverStockProductPromo(
        { ...productApply, quantity: productApply.quantity + 1 },
        this.voucher,
        this.productVoucher,
        this.invoiceConfiguration,
        this.lastCompany,
        this.toast,
        this.productSelected
      )
    ) {
      return;
    }
    product.quantity++;
    this.changeProductPromoQuantity(product);
  }

  decreaseProduct(product) {
    if (product.quantity > 1) {
      product.quantity--;
      this.changeProductPromoQuantity(product);
    } else {
      product.quantity = 0;
      product.inProductOrder = false;
      if (this.productVoucher.type == VoucherType.type102) {
        this.orderSelected.products = this.orderSelected.products.filter(productOrder => {
          return !(productOrder.productProductUnitId == product.productProductUnitId && productOrder.feature == SPKMProduct.feature);
        });
      } else {
        this.orderSelected.products.forEach(productOrder => {
          if (productOrder.productProductUnitId == this.productSelected.productProductUnitId && productOrder.voucherProducts) {
            productOrder.voucherProducts = productOrder.voucherProducts.filter(
              productVoucher => productVoucher.productProductUnitId !== product.productProductUnitId
            );
          }
        });
      }
      this.productVoucher.applyProductVoucher = this.productVoucher.applyProductVoucher.filter(productApply => {
        return !(
          productApply.productProductUnitId == product.productProductUnitId &&
          (this.productSelected ? productApply.parentProductId == this.productSelected.productProductUnitId : true)
        );
      });
    }
  }

  getProductApplyFromProductItem(product) {
    let productApplyLocal;
    this.productVoucher.applyProductVoucher?.forEach(productApply => {
      if (productApply.productProductUnitId == product.productProductUnitId) {
        productApplyLocal = productApply;
      }
    });
    return productApplyLocal;
  }

  changeProductPromoQuantity(product) {
    let productSelected = this.productVoucher.applyProductVoucher.find(productVoucher => {
      return (
        productVoucher.productProductUnitId == product.productProductUnitId &&
        (this.productSelected ? productVoucher.parentProductId == this.productSelected?.productProductUnitId : true)
      );
    });
    productSelected.quantity = product.quantity;
    if (this.productSelected) {
      this.productSelected?.voucherProducts.forEach(productVoucher => {
        if (productVoucher.productProductUnitId == productSelected.productProductUnitId) {
          productVoucher.quantity = productSelected.quantity;
          updateDiscountAmountProductPromo(productVoucher, this.productVoucher);
          changeProductSelected(productVoucher, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
        }
      });
    } else {
      this.orderSelected.products.forEach(productPromo => {
        if (productPromo.feature == SPKMProduct.feature && productPromo.productProductUnitId == productSelected.productProductUnitId) {
          productPromo.quantity = productSelected.quantity;
        }
      });
    }
    updateDiscountAmountProductPromo(productSelected, this.productVoucher);
    changeProductSelected(productSelected, this.lastCompany, this.invoiceConfiguration, this.invoiceType);
  }

  checkProductPromoQuantity(product) {
    if (!product.quantity) {
      product.inProductOrder = false;
      if (this.productVoucher.type == VoucherType.type102) {
        this.orderSelected.products = this.orderSelected.products.filter(productOrder => {
          return productOrder.productProductUnitId !== product.productProductUnitId;
        });
      } else {
        this.productSelected.voucherProducts = this.productSelected.voucherProducts.filter(
          productVoucher => productVoucher.productProductUnitId !== product.productProductUnitId
        );
      }
      this.productVoucher.applyProductVoucher = this.productVoucher.applyProductVoucher.filter(productApply => {
        return productApply.productProductUnitId !== product.productProductUnitId;
      });
    }
  }

  closeModal() {
    this.formSubmit.emit(false);
  }

  saveProductVoucher() {
    this.formSubmit.emit({
      orderSelected: this.orderSelected,
      productVoucher: this.productVoucher,
    });
  }
}
