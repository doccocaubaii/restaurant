import { product } from 'app/object-stores.constants';
import { PosCustomerOrderPage } from '../order/customer-order/customer-order.component';
import { IBillPayment, InvoiceConfiguration, LastCompany, ProductBill } from '../order/model/bill-payment.model';
import { IProduct } from '../order/model/product.model';
import {
  DiscountVat,
  ProductNormal,
  SPCKProduct,
  SPDVProduct,
  SPGCProduct,
  SPKMProduct,
  SPVoucherProduct,
  TaxReductionType,
  VoucherType,
} from './customer-order.const';

export function validVariable(value: any) {
  if (value !== null && value !== undefined && value.toString().trim() !== '') {
    return true;
  }
  return false;
}

export function generateRandomString() {
  let result = '';
  const characters = 'abcdefghijklmnopqrstuvwxyz0123456789';
  const charactersLength = characters.length;

  for (let i = 0; i < 20; i++) {
    result += characters.charAt(Math.floor(Math.random() * charactersLength));
  }

  return result;
}

export function take_decimal_number(num, n) {
  const base = 10 ** n;
  const result = Math.round(num * base) / base;
  return result;
}

export function getPositiveNumber(number: number): number {
  if (number < 0) {
    return 0;
  } else {
    return number;
  }
}

export function numberOnly(event: any): boolean {
  const charCode = event.which ? event.which : event.keyCode;
  if (charCode > 31 && ((charCode < 48 && charCode !== 46) || charCode > 57)) {
    return false;
  }
  return true;
}

export function checkInput(event: any) {
  const charCode = event.which ? event.which : event.keyCode;
  if (charCode > 31 && ((charCode < 48 && charCode !== 46) || charCode > 57)) {
    return false;
  } else {
    if (event.target.value >= 10) {
      event.target.value = 100;
      return 100;
    } else {
      return true;
    }
  }
}

export function updatePosition(orderSelected) {
  orderSelected.products.sort((a, b) => {
    return a.feature - b.feature;
  });
  let position = 0;
  orderSelected.products.forEach((product, index) => {
    position++;
    product.position = position;
    product.toppings?.forEach(topping => {
      position++;
      topping.position = position;
    });
    product.voucherProducts?.forEach(productVoucher => {
      position++;
      productVoucher.position = position;
    });
  });
}

export function convertNumberToString(value: number): string {
  const newValue = Math.abs(value).toString();
  const parts = newValue.split('.');
  parts[0] = parts[0].replace(/,/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
  return parts.join('.');
}

export function changeProductTopping(
  productTopping: ProductBill,
  lastCompany: LastCompany,
  invoiceConfiguration: InvoiceConfiguration,
  invoiceType?: any,
  productOrder?: ProductBill
) {
  if (invoiceConfiguration.invoiceType == invoiceType.invoiceMultiple) {
    productTopping.vatRate = productOrder?.vatRate || 0;
  }
  productTopping.quantity = take_decimal_number(
    productTopping.displayQuantity * (productOrder?.quantity || 0),
    lastCompany.roundScaleQuantity
  );
  const vatRate = getPositiveNumber(productTopping.vatRate);
  const discountVatRate = getPositiveNumber(productTopping.discountVatRate);
  productTopping.amount = take_decimal_number(productTopping.quantity * productTopping.unitPrice, lastCompany.roundScaleAmount);
  productTopping.totalPreTax = productTopping.amount;

  if (
    invoiceConfiguration.invoiceType == invoiceType.invoiceSale &&
    invoiceConfiguration.discountVat == DiscountVat.discount &&
    invoiceConfiguration.taxReductionType == TaxReductionType.product
  ) {
    productTopping.totalDiscount = take_decimal_number(
      ((productTopping.totalPreTax * discountVatRate) / 100) * 0.2,
      lastCompany.roundScaleAmount
    );
  } else {
    productTopping.vatAmount = take_decimal_number((productTopping.totalPreTax * vatRate) / 100, lastCompany.roundScaleAmount);
  }
  productTopping.totalAmount = productTopping.totalPreTax + productTopping.vatAmount - productTopping.totalDiscount;
}

export function changeProductSelected(
  productSelected: ProductBill,
  lastCompany: LastCompany,
  invoiceConfiguration?: any,
  invoiceType?: any
) {
  if (productSelected.productCode !== SPDVProduct.code) {
    updateInputWidth(productSelected);
    productSelected.toppings?.forEach(productTopping => {
      changeProductTopping(productTopping, lastCompany, invoiceConfiguration, invoiceType, productSelected);
    });
    productSelected.totalAmountTopping = 0;
    productSelected.displayAmount = productSelected.amount = take_decimal_number(
      productSelected.quantity * productSelected.unitPrice,
      lastCompany.roundScaleAmount
    );
    productSelected.displayVatAmount = 0;

    const vatRate = getPositiveNumber(productSelected.vatRate);
    const discountVatRate = getPositiveNumber(productSelected.discountVatRate);
    productSelected.toppings?.forEach(product => {
      productSelected.totalAmountTopping += product.amount;
      productSelected.displayAmount += product.amount;
      productSelected.displayVatAmount += product.vatAmount;
      productSelected.displayVatAmount += product.totalDiscount;
    });

    if (productSelected.discountPercent) {
      productSelected.discountAmount = take_decimal_number(
        (productSelected.discountPercent * productSelected.amount) / 100,
        lastCompany.roundScaleAmount
      );
    }
    if (productSelected.feature == SPKMProduct.feature) {
      productSelected.discountAmount = 0;
    }
    // Thành tiền sản phẩm
    productSelected.totalPreTax = take_decimal_number(
      productSelected.amount - (productSelected.discountAmount || 0),
      lastCompany.roundScaleAmount
    );

    productSelected.totalPreTax = productSelected.totalPreTax < 0 ? 0 : productSelected.totalPreTax;

    if (
      invoiceConfiguration.invoiceType == invoiceType.invoiceSale &&
      invoiceConfiguration.discountVat == DiscountVat.discount &&
      invoiceConfiguration.taxReductionType == TaxReductionType.product
    ) {
      productSelected.totalDiscount = take_decimal_number(
        ((productSelected.totalPreTax * discountVatRate) / 100) * 0.2,
        lastCompany.roundScaleAmount
      );
      productSelected.displayVatAmount += productSelected.totalDiscount;
      productSelected.totalAmount = productSelected.totalPreTax - productSelected.totalDiscount;
      productSelected.displayTotalAmount =
        productSelected.totalPreTax + productSelected.totalAmountTopping - productSelected.displayVatAmount;
    } else {
      productSelected.vatAmount = take_decimal_number((productSelected.totalPreTax * vatRate) / 100, lastCompany.roundScaleAmount);
      productSelected.displayVatAmount += productSelected.vatAmount;
      productSelected.totalAmount = productSelected.totalPreTax + productSelected.vatAmount;
      productSelected.displayTotalAmount =
        productSelected.totalPreTax + productSelected.totalAmountTopping + productSelected.displayVatAmount;
    }
  } else {
    productSelected.displayVatAmount = productSelected.vatAmount = take_decimal_number(
      (getPositiveNumber(productSelected.vatRate) * productSelected.totalPreTax) / 100,
      lastCompany.roundScaleAmount
    );
    productSelected.displayTotalAmount = productSelected.totalAmount = productSelected.totalPreTax + productSelected.vatAmount;
  }
}

export function updateVoucher(orderSelected, lastCompany, productVoucherOrder, returnOrder) {
  productVoucherOrder.amount = 0;
  productVoucherOrder.totalPreTax = 0;
  // orderSelected.voucherAmount = 0;
  if (!returnOrder) {
    orderSelected.vouchers?.forEach(voucher => {
      if ((voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300) && voucher.voucherPercent) {
        voucher.voucherValue = take_decimal_number((orderSelected.amount * voucher.voucherPercent) / 100, lastCompany.roundScaleAmount);
      }
      orderSelected.discountAmount += voucher.voucherValue;
      productVoucherOrder.amount = productVoucherOrder.totalPreTax += voucher.voucherValue;
    });
  } else {
    orderSelected.voucherReturns?.forEach(voucher => {
      if ((voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300) && voucher.voucherPercent) {
        voucher.voucherValue = take_decimal_number((orderSelected.amount * voucher.voucherPercent) / 100, lastCompany.roundScaleAmount);
      }
      orderSelected.discountAmount += voucher.voucherValue;
      productVoucherOrder.amount = productVoucherOrder.totalPreTax += voucher.voucherValue;
    });
  }
}

export function updateOrder(
  orderSelected: any,
  invoiceConfiguration: InvoiceConfiguration,
  invoiceType,
  lastCompany: LastCompany,
  updateSPDV = true,
  posCustomerOrder?: PosCustomerOrderPage,
  returnOrder?: boolean
) {
  let productDiscountTaxOrder;
  let productVoucherOrder;
  let vatRateDiscountOrder;
  orderSelected.totalAmount = 0;
  orderSelected.amount = 0;
  orderSelected.countProduct = 0;
  orderSelected.productDiscountAmount = 0;
  if (invoiceConfiguration.invoiceType === invoiceType.invoiceSale) {
    orderSelected.discountVatAmount = 0;
  }
  orderSelected.quantity = 0;
  orderSelected.discountAmount = 0;
  orderSelected.extraConfig.svc5 = 0;
  if (
    invoiceConfiguration.invoiceType === invoiceType.invoiceMultiple ||
    (invoiceConfiguration.invoiceType === invoiceType.invoiceSale &&
      invoiceConfiguration.discountVat == DiscountVat.discount &&
      invoiceConfiguration.taxReductionType == TaxReductionType.product)
  ) {
    orderSelected.vatAmount = 0;
  }
  orderSelected.productTaxAmount = 0;

  orderSelected.products.forEach(productSelected => {
    if (productSelected.feature == 1 || productSelected.feature == SPKMProduct.feature) {
      orderSelected.countProduct++;
      if (productSelected.voucherProducts?.length) {
        orderSelected.countProduct += productSelected.voucherProducts.length;
      }
    }
    if (productSelected.feature === 1 && productSelected.productCode !== SPDVProduct.code) {
      orderSelected.quantity = take_decimal_number(orderSelected.quantity + productSelected.quantity, lastCompany.roundScaleQuantity);
      // tạm tính
      orderSelected.amount += getPositiveNumber(
        take_decimal_number(productSelected.displayAmount - (productSelected.discountAmount || 0), lastCompany.roundScaleAmount)
      );
      orderSelected.productDiscountAmount += productSelected.discountAmount ? productSelected.discountAmount : 0;

      if (invoiceConfiguration.invoiceType === invoiceType.invoiceMultiple) {
        orderSelected.vatAmount += productSelected.vatAmount;
        productSelected.toppings?.forEach(productTopping => {
          orderSelected.vatAmount += productTopping.vatAmount;
        });
      }
      if (
        invoiceConfiguration.invoiceType === invoiceType.invoiceSale &&
        invoiceConfiguration.discountVat == DiscountVat.discount &&
        invoiceConfiguration.taxReductionType == TaxReductionType.product
      ) {
        if (orderSelected.discountVatAmount !== undefined) {
          orderSelected.discountVatAmount += Math.abs(productSelected.totalDiscount);
          productSelected.toppings?.forEach(productTopping => {
            orderSelected.discountVatAmount += Math.abs(productTopping.totalDiscount);
          });
        }
      }
      productSelected.voucherProducts?.forEach(productVoucher => {
        orderSelected.amount += getPositiveNumber(
          take_decimal_number(productVoucher.amount - (productVoucher.discountAmount || 0), lastCompany.roundScaleAmount)
        );
        orderSelected.productDiscountAmount += productVoucher.discountAmount ? productVoucher.discountAmount : 0;
        orderSelected.vatAmount += productVoucher.vatAmount;
        orderSelected.discountVatAmount += Math.abs(productVoucher.totalDiscount);
      });
    } else {
      if (productSelected.feature == SPCKProduct.feature) {
        if (productSelected.productCode == SPVoucherProduct.code) {
          productVoucherOrder = productSelected;
        } else {
          productDiscountTaxOrder = productSelected;
        }
      }
      if (productSelected.feature == SPGCProduct.feature) {
        vatRateDiscountOrder = productSelected;
      }
    }
  });
  if (posCustomerOrder && invoiceConfiguration.voucherApply) {
    let result = checkCancelSPKMOrder(
      orderSelected,
      posCustomerOrder.listVoucher,
      lastCompany,
      !returnOrder ? posCustomerOrder.toast : null,
      null,
      null,
      returnOrder,
      posCustomerOrder
    );
    if (result) {
      updateOrder(orderSelected, invoiceConfiguration, invoiceType, lastCompany, updateSPDV, posCustomerOrder, returnOrder);
      return;
    }
    checkStatusSPKM(orderSelected, posCustomerOrder.listVoucher);
    if (!orderSelected.id) {
      productVoucherOrder = posCustomerOrder.checkApplySPKMOrder();
    }
    posCustomerOrder.listVoucher?.forEach(voucher => {
      voucher.check = false;
      orderSelected.vouchers.forEach(voucherOrder => {
        if (voucherOrder.code == voucher.code) {
          voucher.check = true;
        }
      });
    });
  }

  productDiscountTaxOrder && updateProductDiscountTaxOrder(productDiscountTaxOrder, orderSelected, lastCompany);
  // Giảm giá đơn hàng
  productVoucherOrder && updateVoucher(orderSelected, lastCompany, productVoucherOrder, returnOrder);
  productVoucherOrder && updateProductVoucherOrder(productVoucherOrder, orderSelected, lastCompany, returnOrder);

  orderSelected.totalPreTax = orderSelected.amount - orderSelected.discountAmount;

  if (orderSelected.totalPreTax < 0) {
    orderSelected.totalPreTax = 0;
    orderSelected.discountAmount = orderSelected.amount;
    productVoucherOrder && updateProductVoucherOrder(productVoucherOrder, orderSelected, lastCompany, returnOrder);
  }

  if (orderSelected.checkSPDV && updateSPDV) {
    updateSPDVOrder(orderSelected, invoiceConfiguration, lastCompany);
  }

  orderSelected.products.forEach(productSelected => {
    if (productSelected.productCode == SPDVProduct.code) {
      orderSelected.extraConfig.svc5 += productSelected.displayAmount;
      orderSelected.vatAmount += productSelected.vatAmount;
    }
  });

  if (vatRateDiscountOrder) {
    if (
      invoiceConfiguration.invoiceType === invoiceType.invoiceSale &&
      invoiceConfiguration.discountVat == DiscountVat.discount &&
      invoiceConfiguration.taxReductionType == TaxReductionType.order
    ) {
      orderSelected.discountVatAmount = take_decimal_number(
        ((orderSelected.totalPreTax * getPositiveNumber(orderSelected.discountVatRate || 0)) / 100) * 0.2 || 0,
        lastCompany.roundScaleAmount
      );
    } else {
      // this.checkDiscountVatRateProduct(this.orderSelected);
    }
    vatRateDiscountOrder.productName = getProductNameSPGC(orderSelected.discountVatAmount);
    if (orderSelected.discountVatAmount > 0) {
      orderSelected.haveDiscountVat = true;
    }
  }
  // orderSelected.quantity = orderSelected.countProduct;

  if (
    invoiceConfiguration.invoiceType === invoiceType.invoiceMultiple ||
    (invoiceConfiguration.invoiceType === invoiceType.invoiceSale &&
      invoiceConfiguration.discountVat == DiscountVat.discount &&
      invoiceConfiguration.taxReductionType == TaxReductionType.product)
  ) {
    orderSelected.vatAmount = orderSelected.vatAmount - (productVoucherOrder?.vatAmount || 0) - (productDiscountTaxOrder?.vatAmount || 0);
  }

  if (invoiceConfiguration.invoiceType === invoiceType.invoiceOne) {
    orderSelected.vatAmount = take_decimal_number(
      orderSelected.totalPreTax * (getPositiveNumber(orderSelected.vatRate) / 100),
      lastCompany.roundScaleAmount
    );
  }

  orderSelected.totalAmount = -(orderSelected.discountVatAmount || 0);
  invoiceConfiguration.totalAmount?.forEach(item => {
    if (item.variable === 'total_pre_tax') {
      orderSelected.totalAmount += orderSelected.totalPreTax;
    }
    if (item.variable === 'vat_amount') {
      orderSelected.totalAmount += orderSelected.vatAmount || 0;
    }
    if (item.variable === 'svc_5') {
      orderSelected.totalAmount += orderSelected.extraConfig.svc5 || 0;
    }
  });

  if (!invoiceConfiguration.totalAmount) {
    orderSelected.totalAmount += orderSelected.totalPreTax + (orderSelected.vatAmount || 0) + (orderSelected.extraConfig.svc5 || 0);
  }
  if (
    invoiceConfiguration.invoiceType === invoiceType.invoiceSale &&
    invoiceConfiguration.discountVat == DiscountVat.discount &&
    invoiceConfiguration.taxReductionType == TaxReductionType.product
  ) {
    checkDiscountVatRateProduct(orderSelected);
  }
  let findVoucherCheckBox = orderSelected.vouchers.filter(
    voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300
  );
  if (!findVoucherCheckBox.length) {
    orderSelected.products = orderSelected.products.filter(product => product.productCode !== SPVoucherProduct.code);
    posCustomerOrder?.productVoucherOrder && (posCustomerOrder.productVoucherOrder = {});
  }
}

export function checkProductVoucherOrder(orderSelected) {
  let findVoucherCheckBox = orderSelected.vouchers?.filter(
    voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300
  );
  if (!findVoucherCheckBox?.length) {
    orderSelected.products = orderSelected.products.filter(product => product.productCode !== SPVoucherProduct.code);
  }
}

export function checkStatusSPKM(orderSelected, listVoucher, productSelectedOrder?) {
  let orderAmount = getOrderAmount(orderSelected);
  orderSelected.products.forEach(productSelected => {
    productSelected.promoValid = false;
  });
  listVoucher?.forEach(voucher => {
    voucher.statusValid = false;
    voucher.conditions?.forEach(productVoucher => {
      switch (productVoucher.type) {
        case VoucherType.type100:
        case VoucherType.type102:
          if (productVoucher.minValue <= orderAmount && (productVoucher.maxValue ? productVoucher.maxValue >= orderAmount : true)) {
            productVoucher.status = true;
            voucher.statusValid = true;
          } else {
            productVoucher.status = false;
          }
          break;
        case VoucherType.type200:
          if (!productSelectedOrder) {
            productVoucher.status = false;
            for (const productOrder of orderSelected.products) {
              for (const productProductUnitId of productVoucher.buyProductProductUnitId) {
                if (productOrder.productProductUnitId == productProductUnitId) {
                  productOrder.promoValid = true;
                  if (productOrder.quantity >= productVoucher.buyQuantity) {
                    productVoucher.status = true;
                    voucher.statusValid = true;
                  }
                  break;
                }
              }
            }
          } else {
            if (productVoucher.buyProductProductUnitId.indexOf(productSelectedOrder.productProductUnitId) !== -1) {
              voucher.statusValid = true;
              productSelectedOrder.promoValid = true;
              if (productSelectedOrder.quantity >= productVoucher.buyQuantity) {
                productVoucher.status = true;
              } else {
                productVoucher.status = false;
              }
            } else {
              productVoucher.status = false;
            }
          }
          break;
        case VoucherType.type300:
          productVoucher.status = true;
          voucher.statusValid = true;
          break;
        default:
      }
    });
  });
}

export function updateProductVoucherOrder(productVoucherOrder, orderSelected: IBillPayment, lastCompany: LastCompany, returnOrder) {
  productVoucherOrder.productName = productVoucherOrder.productNameCustom || '';
  if (!productVoucherOrder.productName) {
    if (!returnOrder) {
      orderSelected.vouchers.forEach(voucher => {
        if (voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300) {
          if (!productVoucherOrder.productName) {
            productVoucherOrder.productName = voucher.desc;
          } else {
            productVoucherOrder.productName += '+' + voucher.desc;
          }
        }
      });
    } else {
      orderSelected.voucherReturns.forEach(voucher => {
        if (voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300) {
          if (!productVoucherOrder.productName) {
            productVoucherOrder.productName = voucher.desc;
          } else {
            productVoucherOrder.productName += '+' + voucher.desc;
          }
        }
      });
    }
  }

  // productVoucherOrder.amount = productVoucherOrder.totalPreTax = orderSelected.discountAmount || 0;
  productVoucherOrder.vatAmount = take_decimal_number(
    (productVoucherOrder.totalPreTax * getPositiveNumber(productVoucherOrder.vatRate)) / 100 || 0,
    lastCompany.roundScaleAmount
  );
  productVoucherOrder.totalAmount = productVoucherOrder.totalPreTax + productVoucherOrder.vatAmount;
}

export function updateProductDiscountTaxOrder(productDiscountTaxOrder, orderSelected, lastCompany) {
  if (productDiscountTaxOrder.discountPercent) {
    productDiscountTaxOrder.amount = productDiscountTaxOrder.totalPreTax = take_decimal_number(
      (orderSelected.amount * productDiscountTaxOrder.discountPercent) / 100,
      lastCompany.roundScaleAmount
    );
  }
  if (productDiscountTaxOrder.vatRate) {
    productDiscountTaxOrder.vatAmount = take_decimal_number(
      (productDiscountTaxOrder.totalPreTax * getPositiveNumber(productDiscountTaxOrder.vatRate)) / 100 || 0,
      lastCompany.roundScaleAmount
    );
  }
  productDiscountTaxOrder.totalAmount = productDiscountTaxOrder.totalPreTax + productDiscountTaxOrder.vatAmount;
  orderSelected.discountAmount += productDiscountTaxOrder.totalPreTax;
}
export function getProductNameSPGC(discountVatAmount) {
  return `Đã giảm ${convertNumberToString(
    discountVatAmount
  )} đồng tương ứng 20% mức tỷ lệ % để tính thuế giá trị gia tăng theo Nghị quyết số 101/2023/QH15`;
}

export function checkDiscountVatRateProduct(orderSelected: IBillPayment) {
  orderSelected.discountVatRate = 0;
  if (!orderSelected.discountVatAmount) {
    orderSelected.products = orderSelected.products.filter(product => product.feature !== SPGCProduct.feature);
    orderSelected.haveDiscountVat = false;
  }
}

export function getQuantityProduct(product: IProduct) {
  if (product.inventoryCount > 0 && product.inventoryCount < 1) {
    return product.inventoryCount;
  } else {
    return 1;
  }
}

export function checkOverStockProduct(productSelected, invoiceConfiguration, toast, updateQuantityStatus = true) {
  if (!invoiceConfiguration.overStock && productSelected.quantity > productSelected.quantityProduct && productSelected.inventoryTracking) {
    toast.warning(
      `Sản phẩm ${productSelected.productName} xuất quá số lượng tồn (${productSelected.quantityProduct} ${productSelected.unit || ''})`
    );
    if (productSelected.quantityProduct > 0 && updateQuantityStatus) {
      productSelected.quantity = productSelected.displayQuantity = productSelected.quantityProduct;
    }
    return false;
  }
  return true;
}

export function updateSPDVOrder(orderSelected, invoiceConfiguration, lastCompany) {
  const listSPDVNew = orderSelected.products.filter(product => {
    return product.productCode == SPDVProduct.code;
  });

  if (!invoiceConfiguration.serviceChargeConfig[0].type) {
    listSPDVNew.forEach(productDV => {
      productDV.totalPreTax = 0;
      orderSelected.products.forEach(product => {
        if (product.vatRate == productDV.vatRate && product.productCode !== SPDVProduct.code) {
          if (product.feature == 1) {
            productDV.displayAmount =
              productDV.amount =
              productDV.totalPreTax =
                productDV.totalPreTax +
                take_decimal_number(product.totalPreTax * invoiceConfiguration.serviceChargeConfig[0].value, lastCompany.roundScaleAmount);
          } else {
            if (product.feature == SPCKProduct.feature) {
              productDV.displayAmount =
                productDV.amount =
                productDV.totalPreTax =
                  productDV.totalPreTax -
                  take_decimal_number(
                    product.totalPreTax * invoiceConfiguration.serviceChargeConfig[0].value,
                    lastCompany.roundScaleAmount
                  );
            }
          }
          product.toppings?.forEach(productTopping => {
            productDV.displayAmount =
              productDV.amount =
              productDV.totalPreTax +=
                take_decimal_number(
                  productTopping.totalPreTax * invoiceConfiguration.serviceChargeConfig[0].value,
                  lastCompany.roundScaleAmount
                );
          });
          productDV.vatRate = product.vatRate;
          productDV.displayVatAmount = productDV.vatAmount = take_decimal_number(
            (getPositiveNumber(productDV.vatRate) * productDV.totalPreTax) / 100,
            lastCompany.roundScaleAmount
          );

          productDV.displayTotalAmount = productDV.totalAmount = productDV.totalPreTax + productDV.vatAmount;
        }
      });
    });
  } else {
    listSPDVNew.forEach(productDV => {
      productDV.totalPreTax = 0;
      let totalPreTaxAmount = 0;
      orderSelected.products.forEach(product => {
        if (product.vatRate == productDV.vatRate && product.productCode !== SPDVProduct.code) {
          if (product.feature == 1) {
            totalPreTaxAmount += product.totalPreTax;
          } else {
            if (product.feature == SPCKProduct.feature) {
              totalPreTaxAmount = totalPreTaxAmount - product.totalPreTax;
            }
          }
          productDV.vatRate = product.vatRate;
          product.toppings?.forEach(productTopping => {
            totalPreTaxAmount += productTopping.totalPreTax;
          });
        }
      });
      productDV.displayAmount =
        productDV.amount =
        productDV.totalPreTax =
          take_decimal_number(totalPreTaxAmount * invoiceConfiguration.serviceChargeConfig[0].value, lastCompany.roundScaleAmount);

      productDV.displayVatAmount = productDV.vatAmount = take_decimal_number(
        (getPositiveNumber(productDV.vatRate) * productDV.totalPreTax) / 100,
        lastCompany.roundScaleAmount
      );
      productDV.displayTotalAmount = productDV.totalAmount = productDV.totalPreTax + productDV.vatAmount;
    });
  }

  listSPDVNew.forEach(productDV => {
    if (!productDV.totalPreTax) {
      orderSelected.products = orderSelected.products.filter(product => product.productUniqueId !== productDV.productUniqueId);
    }
  });
}

export function checkConditionNotify(invoiceConfiguration: InvoiceConfiguration) {
  let displayConfig = invoiceConfiguration?.displayConfig.find(config => config.code == 'KITCHEN');
  if (displayConfig?.active) {
    return true;
  } else {
    return false;
  }
}

export function updateInputWidth(productSelected) {
  productSelected.inputWidth = (productSelected.quantity.toString().length + 1) * 10; // Điều chỉnh dựa trên chiều dài nội dung
  if (productSelected.inputWidth < 60) {
    productSelected.inputWidth = 60;
  }
}

export function checkCancelSPKMOrder(
  orderSelected: IBillPayment,
  listVoucher: any,
  lastCompany,
  toast?,
  voucherRemoveOrder?,
  productSelectedRemove?,
  returnOrder?,
  posCustomerOrder?
) {
  let productOrderRemove;
  let orderAmount = getOrderAmount(orderSelected);
  let updateOrder = false;
  for (let voucher of orderSelected.vouchers) {
    let voucherAmountOrder = voucher.voucherValue;

    switch (voucher.type) {
      case VoucherType.type100:
        if (
          orderAmount < voucher.minValue ||
          (voucher.maxValue ? orderAmount > voucher.maxValue : false) ||
          voucher.code == voucherRemoveOrder?.code
        ) {
          if (!returnOrder) {
            toast && toast.warning(`Đơn hàng đã tự động hủy tiền khuyến mại ${convertNumberToString(voucherAmountOrder)}`);
            orderSelected.vouchers = orderSelected.vouchers.filter(voucherSelected => voucherSelected.id !== voucher.id);
            let findVoucherCheckBox = orderSelected.vouchers.filter(
              voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300
            );
            if (!findVoucherCheckBox.length) {
              orderSelected.products = orderSelected.products.filter(product => product.productCode !== SPVoucherProduct.code);
            }
            listVoucher?.forEach(voucherItem => {
              if (voucherItem.id == voucher.id) {
                voucherItem.check = false;
              }
            });
            updateOrder = true;
          } else {
            if (!orderSelected.voucherReturns.find(voucherReturn => voucherReturn.id == voucher.id)) {
              orderSelected.voucherReturns.push(voucher);
            }
          }
        } else {
          if (returnOrder) {
            if (orderSelected.voucherReturns.find(voucherReturn => voucherReturn.id == voucher.id)) {
              orderSelected.voucherReturns = orderSelected.voucherReturns.filter(voucherReturn => voucherReturn.id !== voucher.id);
            }
          }
        }
        break;
      case VoucherType.type102:
        if (
          orderAmount < voucher.minValue ||
          (voucher.maxValue ? orderAmount > voucher.maxValue : false) ||
          voucher.code == voucherRemoveOrder?.code
        ) {
          toast && toast.warning(`Đơn hàng đã tự động hủy chương trình khuyến mại ${voucher.desc}`);
          orderSelected.vouchers = orderSelected.vouchers.filter(voucherSelected => voucherSelected.id !== voucher.id);
          if (!returnOrder) {
            orderSelected.products = orderSelected.products.filter(product => product.feature !== SPKMProduct.feature);
          }
          listVoucher?.forEach(voucherItem => {
            if (voucherItem.id == voucher.id) {
              voucherItem.conditions.forEach(voucherProduct => {
                if (voucherProduct.applyProductVoucher?.length) {
                  let applyProductVoucher = JSON.parse(JSON.stringify(voucherProduct.applyProductVoucher));
                  voucherProduct.applyProductVoucher = [];
                  voucherProduct.listProductPromo.forEach(product => {
                    applyProductVoucher.forEach(productVoucher => {
                      if (product.productProductUnitId == productVoucher.productProductUnitId) {
                        product.inProductOrder = false;
                        product.quantity = 0;
                      }
                    });
                  });
                }
              });
            }
          });
          updateOrder = true;
        }
        break;
      case VoucherType.type200:
        // code block
        let cancelSPKM = true;
        let listProductPromo: any[] = [];

        for (const productSelected of orderSelected.products) {
          if (productSelected.productUniqueId == productSelectedRemove?.productUniqueId) {
            productOrderRemove = productSelected;
          }
          let findVoucherType200 = false;
          productSelected.voucherProducts?.forEach(productPromo => {
            if (productPromo.voucherId == voucher.id) {
              listProductPromo = productSelected.voucherProducts;
              findVoucherType200 = true;
            }
          });
          if (findVoucherType200) {
            listVoucher?.forEach(voucherItem => {
              if (voucherItem.id == voucher.id) {
                voucherItem.conditions.forEach(productVoucher => {
                  if (productVoucher.status || returnOrder) {
                    let statusVoucher = true;
                    if (!productVoucher.buyProductProductUnitId.includes(productSelected.productProductUnitId)) {
                      statusVoucher = false;
                    }
                    productSelected.voucherProducts.forEach((productPromo: any) => {
                      if (productPromo.voucherId !== voucher.id) {
                        statusVoucher = false;
                      }
                    });

                    if (statusVoucher) {
                      if (returnOrder) {
                        changeProductVoucherQuantity(
                          orderSelected,
                          productSelected,
                          productVoucher,
                          voucher,
                          toast,
                          returnOrder,
                          lastCompany,
                          posCustomerOrder
                        );
                      } else {
                        if (voucher.code == voucherRemoveOrder?.code && productOrderRemove) {
                          removeProductVoucher(
                            orderSelected,
                            productOrderRemove || productSelected,
                            productVoucher,
                            voucher,
                            toast,
                            returnOrder,
                            lastCompany
                          );
                          updateOrder = true;
                        }
                        let maxValueProductPromo = productVoucher.getQuantity;
                        if (productSelected.quantity >= productVoucher.buyQuantity) {
                          if (!voucherItem.differentExtConditions?.isFixedQuantity) {
                            maxValueProductPromo = getProductVoucherQuantityMaxValue(
                              productSelected.quantity,
                              productVoucher,
                              lastCompany,
                              voucher
                            );
                            let totalProductPromoOrder = 0;
                            productSelected.voucherProducts.forEach(productPromo => {
                              totalProductPromoOrder += productPromo.quantity;
                            });
                            if (totalProductPromoOrder > maxValueProductPromo) {
                              removeProductVoucher(
                                orderSelected,
                                productSelected,
                                productVoucher,
                                voucher,
                                toast,
                                returnOrder,
                                lastCompany
                              );
                              updateOrder = true;
                            }
                          }
                        } else {
                          removeProductVoucher(orderSelected, productSelected, productVoucher, voucher, toast, returnOrder, lastCompany);
                          updateOrder = true;
                        }
                      }
                    }
                  }
                });
              }
            });
          }

          if (productOrderRemove) {
            break;
          }
        }
        break;
      default:
    }
  }
  return updateOrder;
}

export function removeVoucherOrderType200(orderSelected, voucher) {
  let removeVoucher = true;
  orderSelected.products.forEach(product => {
    product.voucherProducts.forEach(productVoucher => {
      if (productVoucher.voucherId == voucher.id) {
        removeVoucher = false;
      }
    });
  });
  if (removeVoucher) {
    orderSelected.vouchers = orderSelected.vouchers.filter(voucherSelected => voucherSelected.id !== voucher.id);
  }
}

export function removeProductVoucher(orderSelected, productSelected, productVoucher, voucher, toast, returnOrder, lastCompany) {
  if (!returnOrder) {
    toast && toast.warning(`Đơn hàng đã tự động hủy chương trình khuyến mại ${productVoucher.desc}`);
    productSelected.voucherProducts = [];
    removeVoucherOrderType200(orderSelected, voucher);
    productVoucher.applyProductVoucher = productVoucher.applyProductVoucher.filter(
      productApply => productApply.parentProductId !== productSelected.productProductUnitId
    );
    productVoucher.listProductPromo.forEach(product => {
      product.inProductOrder = false;
      product.quantity = 0;
    });
  }
}

export function changeProductVoucherQuantity(
  orderSelected,
  productSelected,
  productVoucher,
  voucher,
  toast,
  returnOrder,
  lastCompany,
  posCustomerOrder
) {
  let productSelectedInitial = JSON.parse(JSON.stringify(productSelected));
  if (!productSelected.quantity) {
    productSelected.voucherProducts?.forEach(productVoucher => {
      productVoucher.quantity = 0;
      updateProductVoucher(productSelected, productSelectedInitial, productVoucher, posCustomerOrder);
    });
  } else {
    if (!voucher.differentExtConditions?.isFixedQUantity) {
      let quantityProductVoucher = 0;
      productSelected.voucherProducts.forEach(productVoucher => {
        quantityProductVoucher += productVoucher.quantityInitial;
      });
      let quantityProductVoucherReturn =
        quantityProductVoucher -
        getProductVoucherQuantityMaxValue(productSelected.quantityInitial - productSelected.quantity, productVoucher, lastCompany, voucher);
      productSelected.voucherProducts.forEach(productVoucher => {
        if (quantityProductVoucherReturn > 0) {
          productVoucher.quantity =
            productVoucher.quantityInitial - quantityProductVoucherReturn < 0
              ? productVoucher.quantityInitial
              : quantityProductVoucherReturn;
          quantityProductVoucherReturn -= productVoucher.quantityInitial;
          updateProductVoucher(productSelected, productSelectedInitial, productVoucher, posCustomerOrder);
        } else {
          productVoucher.quantity = 0;
          updateProductVoucher(productSelected, productSelectedInitial, productVoucher, posCustomerOrder);
        }
      });
    } else {
      productVoucher.quantity = productVoucher.quantityInitial;
      updateProductVoucher(productSelected, productSelectedInitial, productVoucher, posCustomerOrder);
    }
  }
}

export function updateProductVoucher(productSelected, productSelectedInitial, productVoucher, posCustomerOrder) {
  if (JSON.stringify(productSelected) !== JSON.stringify(productSelectedInitial)) {
    posCustomerOrder.changeProductSelected(productVoucher);
  }
}

export function getProductVoucherQuantityMaxValue(productSelectedQuantity, productVoucher, lastCompany, voucher) {
  let maxValueProductVoucherQuantity;
  let multiplier = take_decimal_number(Math.floor(productSelectedQuantity / productVoucher.buyQuantity), lastCompany.roundScaleQuantity);
  maxValueProductVoucherQuantity = take_decimal_number(productVoucher.getQuantity * multiplier, lastCompany.roundScaleQuantity);
  return maxValueProductVoucherQuantity;
}

export function checkOverStockProductPromo(
  productApply,
  voucher,
  productVoucher,
  invoiceConfiguration,
  lastCompany,
  toast,
  productSelected?
) {
  if (!checkOverStockProduct(productApply, invoiceConfiguration, toast, false)) {
    return;
  }
  let countQuantity = productApply.quantity;
  let newGetQuantity = getMaxValueProductPromo(productApply, productVoucher, invoiceConfiguration, lastCompany, voucher, productSelected);
  if (countQuantity > newGetQuantity) {
    toast.warning('Số lượng hàng tặng không được lớn hơn số lượng tối đa cho phép');
    return false;
  }
  return true;
}

export function getMaxValueProductPromo(
  productApply,
  productVoucher,
  invoiceConfiguration,
  lastCompany?,
  voucher?,
  productSelected?,
  returnMaxValue = false
) {
  let newGetQuantity = 0;
  let voucherLocal = voucher;
  if (voucherLocal) {
    if (!voucher?.differentExtConditions?.isFixedQuantity) {
      if (productSelected?.quantity > productVoucher.buyQuantity) {
        let multiplier = take_decimal_number(
          Math.floor(productSelected?.quantity / productVoucher.buyQuantity),
          lastCompany.roundScaleQuantity
        );
        newGetQuantity = take_decimal_number(productVoucher.getQuantity * multiplier, lastCompany.roundScaleQuantity);
      }
    }
  }
  let maxValue = newGetQuantity ? newGetQuantity : productVoucher.getQuantity;
  if (returnMaxValue) {
    return maxValue;
  }
  productVoucher.applyProductVoucher?.forEach(productPromo => {
    if (productApply.productProductUnitId !== productPromo.productProductUnitId) {
      if (
        productSelected &&
        productPromo.parentProductId == productSelected.productProductUnitId &&
        productPromo.parentProductUniqueId == productSelected.productUniqueId
      ) {
        maxValue -= productPromo.quantity;
      }
      if (!productSelected) {
        maxValue -= productPromo.quantity;
      }
    }
  });

  if (!invoiceConfiguration.overStock && productApply.inventoryTracking) {
    if (maxValue > productApply.quantityProduct) {
      maxValue = productApply.quantityProduct;
    }
  }

  if (maxValue > 0) {
    return maxValue;
  } else {
    return 0;
  }
}

export function updateApplyProductPromoAndListProductPromo(orderSelected, listVoucher) {
  if (orderSelected.id) {
    orderSelected.products.forEach(productSelected => {
      let listProductPromo = [];
      if (productSelected.voucherProducts?.length) {
        listProductPromo = productSelected.voucherProducts;
        listVoucher?.forEach(voucher => {
          if (voucher.check && voucher.type == VoucherType.type200) {
            voucher.conditions.forEach(productVoucher => {
              if (productVoucher.status) {
                let statusVoucher = true;
                listProductPromo.forEach((productPromo: any) => {
                  productPromo.discountPercent = productVoucher.discountPercent || 0;
                  if (!productVoucher.buyProductProductUnitId.includes(productSelected.productProductUnitId)) {
                    statusVoucher = false;
                  }
                  if (productPromo.voucherId !== voucher.id) {
                    statusVoucher = false;
                  }
                });
                if (statusVoucher) {
                  if (!productVoucher.applyProductVoucher) {
                    productVoucher.applyProductVoucher = [];
                  }
                  productVoucher.applyProductVoucher = productVoucher.applyProductVoucher.concat(listProductPromo);
                  productVoucher.listProductPromo?.forEach(productItem => {
                    listProductPromo.forEach((productVoucher: any) => {
                      if (productItem.productProductUnitId == productVoucher.productProductUnitId) {
                        productItem.inProductOrder = true;
                        productItem.quantity = productVoucher.quantity;
                      }
                    });
                  });
                }
              }
            });
          }
        });
      }
    });

    let voucherCodeType102 = orderSelected.vouchers.find(voucher => voucher.type == VoucherType.type102)?.code || '';
    let listProductType102 = orderSelected.products.filter(productOrder => productOrder.feature === SPKMProduct.feature);
    let voucherType102 = listVoucher.find(voucher => voucher.code == voucherCodeType102);
    if (voucherType102) {
      voucherType102.conditions.forEach(productVoucher => {
        if (productVoucher.status) {
          productVoucher.applyProductVoucher = listProductType102;
          productVoucher.listProductPromo?.forEach(productItem => {
            listProductType102.forEach((productVoucher: any) => {
              if (productItem.productProductUnitId == productVoucher.productProductUnitId) {
                productItem.inProductOrder = true;
                productItem.quantity = productVoucher.quantity;
              }
            });
          });
        }
      });
    }
  }
}

export function convertVoucherUpdateToCreate(orderSelected) {
  let orderAmount = getOrderAmount(orderSelected);
  let vouchers: any[] = [];
  orderSelected.vouchers?.forEach(voucherOrder => {
    voucherOrder.conditions.forEach(productVoucher => {
      switch (voucherOrder.type) {
        case VoucherType.type100:
        case VoucherType.type102:
          if (orderAmount >= productVoucher.minValue && (productVoucher.maxValue ? orderAmount <= productVoucher.maxValue : true)) {
            addVoucherToVoucherList(voucherOrder, productVoucher, vouchers);
          }
          break;
        case VoucherType.type300:
          addVoucherToVoucherList(voucherOrder, productVoucher, vouchers);
          break;
        case VoucherType.type200:
          addVoucherToVoucherList(voucherOrder, productVoucher, vouchers);
          break;
      }
    });
  });
  orderSelected.vouchers = vouchers;
}

export function addVoucherToVoucherList(voucherOrder, productVoucher, vouchers) {
  let voucherOrderExit = vouchers.find(voucher => voucher.code == voucherOrder.code);
  if (voucherOrderExit) {
    return;
  } else {
    let voucher = {
      id: voucherOrder.id,
      code: voucherOrder.code,
      voucherValue: voucherOrder.voucherValue,
      voucherPercent: productVoucher.discountPercent || 0,
      desc: productVoucher.desc,
      type: voucherOrder.type,
      minValue: productVoucher.minValue,
      maxValue: productVoucher.maxValue,
    };
    vouchers.push(voucher);
  }
}

export function addProductSelectedToList(productSelected, orderSelected) {
  let position = orderSelected.products.length;
  for (let i = orderSelected.products.length - 1; i >= 0; i--) {
    // nếu sp thêm ko phải spdv
    if (productSelected.productCode !== SPDVProduct.code) {
      // sp thêm có feature < sp hiện tại trong đơn hàng
      if (productSelected.feature < (orderSelected.products[i].feature || 0)) {
        position = i;
        continue;
      }
      // sp hiện tại là spdv và sp thêm có feature < 3
      if (orderSelected.products[i].productCode === SPDVProduct.code && productSelected.feature <= SPCKProduct.feature) {
        position = i;
        continue;
      }
    }
    // nếu sp thêm là spdv và sp hiện tại có feature > 3
    if (productSelected.productCode === SPDVProduct.code && (orderSelected.products[i].feature || 0) > SPCKProduct.feature) {
      position = i;
      continue;
    }
  }
  orderSelected.products.splice(position, 0, productSelected);
}

export function removeProductPromoOrder(orderSelected, productSelected, listVoucher, toast) {
  let spkmProduct = orderSelected.products.filter(productSelected => productSelected.feature == SPKMProduct.feature);
  let voucherCode = orderSelected.vouchers.find(voucher => voucher.type == VoucherType.type102).code;
  listVoucher?.forEach(voucher => {
    if (voucher.code == voucherCode) {
      voucher.conditions.forEach(productVoucher => {
        if (productVoucher.status) {
          productVoucher.applyProductVoucher = productVoucher.applyProductVoucher.filter(
            productApply => productApply.productProductUnitId !== productSelected.productProductUnitId
          );
          let product = productVoucher.listProductPromo.find(
            productItem => productItem.productProductUnitId == productSelected.productProductUnitId
          );
          product.inProductOrder = false;
          product.quantity = 0;
        }
      });
    }
  });
  if (!spkmProduct.length) {
    let voucherRemoveOrder = orderSelected.vouchers.find(voucher => voucher.type == VoucherType.type102);
    orderSelected.vouchers = orderSelected.vouchers.filter(voucher => voucher.type !== VoucherType.type102);
    toast && toast.warning(`Đơn hàng đã tự động hủy chương trình khuyến mại ${voucherRemoveOrder.desc}`);
  }
}

export function checkMultipleVoucherRadio(orderSelectedOriginal, orderSelectedNew, productSelected) {
  let voucherOld;
  let voucherNew;
  let productSelectedLocal;
  if (productSelected) {
    voucherNew = orderSelectedNew.vouchers.find(voucher => voucher.type == VoucherType.type200);
    voucherOld = orderSelectedOriginal.vouchers[0];

    if (voucherOld?.type == VoucherType.type200) {
      for (const product of orderSelectedOriginal.products) {
        for (const productPromo of product.voucherProducts) {
          if (productPromo.voucherId == voucherOld.id && product.productProductUnitId !== productSelected.productProductUnitId) {
            productSelectedLocal = product;
          }
          if (productPromo.voucherId == voucherOld.id && product.productProductUnitId == productSelected.productProductUnitId) {
            return false;
          }
        }
      }
    }
  } else {
    voucherOld = orderSelectedOriginal.vouchers.find(voucher => voucher.type == VoucherType.type200);
    voucherNew = orderSelectedNew.vouchers.find(voucher => voucher.type !== VoucherType.type200);
    orderSelectedOriginal.products.forEach(productSelected => {
      productSelected.voucherProducts.forEach(productVoucher => {
        if (productVoucher.voucherId == voucherOld.id) {
          productSelectedLocal = productSelected;
        }
      });
    });
  }
  if (voucherOld && voucherNew) {
    return { voucherRemove: voucherOld, productSelected: productSelectedLocal };
  } else {
    return false;
  }
}

export function removeAccents(str: string) {
  return str
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/đ/g, 'd')
    .replace(/Đ/g, 'D');
}

export function convertProductToProductOrder(
  product,
  voucher,
  productVoucher,
  invoiceConfiguration,
  invoiceType,
  orderSelected,
  productSelected?
) {
  if (invoiceConfiguration.invoiceType === invoiceType.invoiceSale) {
    product.vatRate = -1;
  }

  if (invoiceConfiguration.invoiceType === invoiceType.invoiceOne) {
    if (orderSelected.vatRate) {
      product.vatRate = orderSelected.vatRate;
    } else {
      product.vatRate = 0;
    }
  }

  const productOrder = {
    productId: product.productId,
    imageUrl: product.imageUrl,
    productProductUnitId: product.productProductUnitId,
    productName: product.productName,
    productCode: product.productCode,
    quantity: 1,
    displayQuantity: 1,
    quantityProduct: product.inventoryCount || 0,
    unit: product.unit,
    unitId: product.unitId,
    unitPrice: product.salePrice || 0,
    discountAmount: productSelected
      ? productVoucher.discountValue > product.salePrice
        ? product.salePrice
        : productVoucher.discountValue
      : 0,
    amount: 0,
    totalPreTax: 0,
    vatRate: product.vatRate !== -4 ? product.vatRate : 0,
    vatAmount: 0,
    inventoryTracking: product.inventoryTracking,
    totalAmount: 0,
    feature: productSelected ? 1 : 2,
    typeDiscount: 'Giảm giá trị',
    discountPercent: productVoucher.discountPercent || 0,
    position: 1,
    toppings: [],
    isTopping: product.isTopping,
    displayAmount: 0,
    totalAmountTopping: 0,
    haveTopping: false,
    productUniqueId: generateRandomString(),
    discountVatRate: product.discountVatRate || 0,
    totalDiscount: 0,
    parentProductId: productSelected?.productProductUnitId || null,
    parentProductUniqueId: productSelected?.productUniqueId || null,
    promoValid: false,
    voucherId: voucher.id,
    voucherProducts: [],
  };
  return productOrder;
}

export function updateDiscountAmountProductPromo(productSelected, productVoucher) {
  if (productVoucher.discountValue) {
    if (productVoucher.discountValue > productSelected.unitPrice) {
      productSelected.discountAmount = productSelected.unitPrice * productSelected.quantity;
    } else {
      productSelected.discountAmount = productVoucher.discountValue * productSelected.quantity;
    }
  }
}

export function removeVoucherOrder(orderSelected, voucherRemove, listVoucher, lastCompany, productSelectedRemove) {
  checkCancelSPKMOrder(orderSelected, listVoucher, lastCompany, null, voucherRemove, productSelectedRemove);
}

export function getOrderAmount(orderSelected) {
  let orderAmount = 0;
  orderSelected.products.forEach(productSelected => {
    if (productSelected.feature === ProductNormal.feature && productSelected.productCode !== SPDVProduct.code) {
      orderAmount += productSelected.displayAmount;
      productSelected.voucherProducts?.forEach(productVoucher => {
        orderAmount += productVoucher.amount - productVoucher.discountAmount;
      });
    }
  });
  return orderAmount;
}

export function updateProductQuantityReturnOrder(orderSelected) {
  orderSelected.products.forEach(product => {
    product.toppings?.forEach(productTopping => {
      orderSelected.productReturns?.forEach(productReturn => {
        if (productTopping.productProductUnitId == productReturn.productProductUnitId) {
          productTopping.quantityInitial = productTopping.quantityInitial - productReturn.quantity;
        }
      });
    });
    product.voucherProducts?.forEach(productVoucher => {
      orderSelected.productReturns?.forEach(productReturn => {
        if (productVoucher.productProductUnitId == productReturn.productProductUnitId) {
          productVoucher.quantityInitial = productVoucher.quantityInitial - productReturn.quantity;
        }
      });
    });
    orderSelected.productReturns?.forEach(productReturn => {
      if (product.productProductUnitId == productReturn.productProductUnitId) {
        product.quantityInitial = product.quantityInitial - productReturn.quantity;
      }
    });
  });
}

export function getOrderReturnFromOrderSelected(orderSelected) {
  let req: any = {
    comId: orderSelected.comId,
    billId: orderSelected.id,
    paymentMethod: orderSelected.payment.paymentMethod,
    amount: orderSelected.totalAmount,
    taxAuthorityCode: orderSelected.taxAuthorityCode,
    products: [],
  };
  orderSelected.products.forEach(productSelected => {
    getProductReturnFromProductSelected(req, productSelected);
    productSelected.toppings?.forEach(productTopping => {
      getProductReturnFromProductSelected(req, productTopping);
    });
    productSelected.voucherProducts?.forEach(productVoucher => {
      getProductReturnFromProductSelected(req, productVoucher);
    });
  });
  return req;
}

export function getProductReturnFromProductSelected(req, productSelected) {
  if (productSelected.feature == SPCKProduct.feature ? productSelected.amount : true) {
    let prod = {
      productProductUnitId: productSelected.productProductUnitId,
      productCode: productSelected.productCode,
      productId: productSelected.productId,
      productName: productSelected.productName,
      unitId: productSelected.unitId,
      unitName: productSelected.unit,
      quantity: productSelected.quantity,
      unitPrice: productSelected.unitPrice,
      amount: productSelected.amount,
      discountAmount: productSelected.discountAmount,
      vatRate: productSelected.vatRate,
      vatAmount: productSelected.vatAmount,
      totalPreTax: productSelected.totalPreTax,
      totalAmount: productSelected.totalAmount,
    };
    req.products.push(prod);
  }
}
