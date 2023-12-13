import { Component, Input, OnInit, Output, EventEmitter } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { Location } from '@angular/common';
import { ToastrService } from 'ngx-toastr';
import { UtilsService } from 'app/utils/Utils.service';
import { CustomerService } from '../../service/customer.service';
import { InvoiceType, LIST_VAT, SPCKProduct, SPKMProduct, SPVoucherProduct, VoucherType } from 'app/pages/const/customer-order.const';
import {
  addProductSelectedToList,
  changeProductSelected,
  checkMultipleVoucherRadio,
  checkOverStockProductPromo,
  checkStatusSPKM,
  convertProductToProductOrder,
  getMaxValueProductPromo,
  removeAccents,
  removeVoucherOrderType200,
  updateDiscountAmountProductPromo,
  updateOrder,
} from 'app/pages/const/function';
import { IBillPayment, InvoiceConfiguration, LastCompany, ProductBill } from '../../model/bill-payment.model';
import { LoadingOption } from '../../../../utils/loadingOption';
import { ICON_CANCEL, ICON_SAVE } from '../../../../shared/other/icon';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { DialogModal } from '../../model/dialogModal.model';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';

@Component({
  selector: 'jhi-voucher-order',
  templateUrl: './voucher-order.component.html',
  styleUrls: ['./voucher-order.component.scss'],
})
export class VoucherOrderComponent implements OnInit {
  productVoucherOrder: ProductBill;
  lastCompany: LastCompany;
  orderSelected;
  invoiceConfiguration: InvoiceConfiguration;
  orderSelectedLocal: IBillPayment;
  listProductGroup: any;
  productVoucherOrderLocal;
  filterVoucher = { page: 1, size: 10, customerId: null, totalItem: 0 };
  listVoucher;
  listVat = JSON.parse(JSON.stringify(LIST_VAT));
  statusCheckAllVoucher = false;
  voucherCode: string;
  statusVatRateInput = false;
  invoiceType = InvoiceType;
  spkmProduct = SPKMProduct;
  listProductId = [];
  listProductGroupId = [];
  listVoucherLocal;
  listVoucherLocalShowInitial: any[] = [];
  listVoucherLocalShow: any[] = [];
  type: number = 1;
  voucherSelected;
  productSelected;
  productVoucherSelected;
  showEndTimeVoucher = false;
  showChooseProductVoucher = false;
  showSP1Product = false;
  keySearchVoucher = '';

  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private location: Location,
    private toast: ToastrService,
    protected utilsService: UtilsService,
    public customerService: CustomerService,
    public loading: LoadingOption
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  checkShowApplyProductVoucher(listVoucher) {
    let findVoucherSelected = false;
    listVoucher.forEach(voucher => {
      if (voucher.endTime) {
        this.showEndTimeVoucher = true;
      }
      voucher.conditions.forEach(productVoucher => {
        productVoucher.newGetQuantity = getMaxValueProductPromo(
          null,
          productVoucher,
          this.invoiceConfiguration,
          this.lastCompany,
          voucher,
          this.productSelected,
          true
        );
        if (productVoucher.status) {
          if (productVoucher.listProductPromo?.length) {
            this.showChooseProductVoucher = true;
          }
          if (this.invoiceConfiguration.combineVoucherApply) {
            if (productVoucher.applyProductVoucher?.length) {
              findVoucherSelected = this.checkVoucherOrder(this.productSelected, productVoucher, voucher) || findVoucherSelected;
            }
          } else {
            findVoucherSelected = this.checkVoucherOrder(this.productSelected, productVoucher, voucher) || findVoucherSelected;
          }
        }
      });
    });
    if (!findVoucherSelected) {
      this.voucherCode = '';
      this.productVoucherSelected = null;
      this.voucherSelected = null;
    }
  }

  checkVoucherOrder(productSelected, productVoucher, voucher) {
    let findVoucherSelected = false;
    if (this.productSelected) {
      productVoucher.showApplyProductVoucher = false;
      productVoucher.applyProductVoucher?.forEach(productApply => {
        if (
          productApply.parentProductId == this.productSelected.productProductUnitId &&
          productApply.parentProductUniqueId == this.productSelected.productUniqueId
        ) {
          findVoucherSelected = true;
          productVoucher.showApplyProductVoucher = true;
          this.productVoucherSelected = productVoucher;
          this.voucherSelected = voucher;
          this.voucherCode = voucher.code;
        }
      });
      productVoucher.listProductPromo?.forEach(productPromo => {
        productPromo.inProductOrder = false;
        productPromo.quantity = 0;
        productVoucher.applyProductVoucher?.forEach(productApply => {
          if (
            productPromo.productProductUnitId == productApply.productProductUnitId &&
            productApply.parentProductId == this.productSelected.productProductUnitId &&
            productApply.parentProductUniqueId == this.productSelected.productUniqueId
          ) {
            productPromo.inProductOrder = true;
            productPromo.quantity = productApply.quantity;
          }
        });
      });
    } else {
      if (this.invoiceConfiguration.combineVoucherApply) {
        this.showChooseProductVoucher = true;
        findVoucherSelected = true;
        this.voucherCode = voucher.code;
        productVoucher.showApplyProductVoucher = true;
        this.productVoucherSelected = productVoucher;
        this.voucherSelected = voucher;
      } else {
        if (voucher.code == this.orderSelectedLocal.vouchers[0]?.code) {
          voucher.conditions.forEach(productVoucher => {
            this.showChooseProductVoucher = true;
            findVoucherSelected = true;
            this.voucherCode = voucher.code;
            productVoucher.showApplyProductVoucher = true;
            this.productVoucherSelected = productVoucher;
            this.voucherSelected = voucher;
          });
        }
      }
    }
    return findVoucherSelected;
  }

  searchVoucher(keySearch) {
    this.listVoucherLocalShow = [];
    let keySearchLocal = removeAccents(keySearch).toLowerCase();
    this.listVoucherLocalShowInitial.forEach(voucher => {
      let voucherCode = removeAccents(voucher.code).toLowerCase();
      let voucherName = removeAccents(voucher.name).toLowerCase();
      if (voucherCode.includes(keySearchLocal) || voucherName.includes(keySearchLocal)) {
        this.listVoucherLocalShow.push(voucher);
      }
    });
  }

  ngOnInit() {
    this.orderSelectedLocal = JSON.parse(JSON.stringify(this.orderSelected));
    this.productVoucherOrderLocal = this.orderSelectedLocal.products.find(product => product.productCode == SPVoucherProduct.code);
    this.productSelected &&
      (this.productSelected = this.orderSelectedLocal.products.find(
        product => product.productUniqueId == this.productSelected.productUniqueId
      ));
    this.listVoucherLocal = JSON.parse(JSON.stringify(this.listVoucher));
    this.productSelected && checkStatusSPKM(this.orderSelectedLocal, this.listVoucherLocal, this.productSelected);

    let listVoucherApply: any[] = [];
    for (const voucher of this.listVoucherLocal) {
      if (voucher.statusValid) {
        for (const productVoucher of voucher.conditions) {
          if (productVoucher.status) {
            if (this.productSelected) {
              if (productVoucher.buyProductProductUnitId.includes(this.productSelected.productProductUnitId)) {
                if (
                  voucher.check ||
                  (!this.invoiceConfiguration.combineVoucherApply && voucher.id == this.orderSelectedLocal.vouchers[0]?.id)
                ) {
                  listVoucherApply.unshift({ ...voucher, conditions: [productVoucher] });
                } else {
                  this.listVoucherLocalShowInitial.unshift({ ...voucher, conditions: [productVoucher] });
                }
              }
            } else {
              if (
                voucher.check ||
                (!this.invoiceConfiguration.combineVoucherApply && voucher.id == this.orderSelectedLocal.vouchers[0]?.id)
              ) {
                listVoucherApply.unshift({ ...voucher, conditions: [productVoucher] });
              } else {
                this.listVoucherLocalShowInitial.unshift({ ...voucher, conditions: [productVoucher] });
              }
            }
          } else {
            if (this.productSelected) {
              if (productVoucher.buyProductProductUnitId.includes(this.productSelected.productProductUnitId)) {
                this.listVoucherLocalShowInitial.push({ ...voucher, conditions: [productVoucher] });
              }
            } else {
              this.listVoucherLocalShowInitial.push({ ...voucher, conditions: [productVoucher] });
            }
          }
        }
      } else {
        for (const productVoucher of voucher.conditions) {
          this.listVoucherLocalShowInitial.push({ ...voucher, conditions: [productVoucher] });
        }
      }
    }

    this.listVoucherLocalShowInitial = listVoucherApply.concat(this.listVoucherLocalShowInitial);
    this.listVoucherLocalShow = JSON.parse(JSON.stringify(this.listVoucherLocalShowInitial));
    this.checkShowApplyProductVoucher(this.listVoucherLocalShow);
    if (this.orderSelectedLocal.vouchers.find(voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300)) {
      this.showSP1Product = true;
    }
  }

  getMaxValueProductPromo(productApply, productVoucher, voucher) {
    return getMaxValueProductPromo(
      productApply,
      productVoucher,
      this.invoiceConfiguration,
      this.lastCompany,
      voucher,
      this.productSelected
    );
  }

  filterProductId(listProductId) {
    let listProductIdLocal = [...new Set(listProductId)];
    this.orderSelectedLocal.products.forEach(product => {
      listProductIdLocal.forEach(productVoucherId => {
        if (product.productProductUnitId == productVoucherId) {
          // @ts-ignore
          this.listProductId.push(productVoucherId);
        }
      });
    });
  }

  openProductVoucherComponent(voucher, productVoucher) {
    if (this.voucherCode == voucher.code) {
      this.type = 2;
      this.voucherSelected = voucher;
      this.productVoucherSelected = productVoucher;
    }
  }

  clearRadioButton(voucher: any, productVoucher) {
    if (this.voucherCode == voucher.code) {
      this.voucherCode = '';
      if (voucher.type == VoucherType.type100) {
        this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => voucherOrder.code !== voucher.code);
      }
      if (voucher.type == VoucherType.type102) {
        this.orderSelectedLocal.products = this.orderSelectedLocal.products.filter(
          productOrder => productOrder.feature !== SPKMProduct.feature
        );
        productVoucher.applyProductVoucher = [];
        this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => voucherOrder.code !== voucher.code);
      }
      if (voucher.type == VoucherType.type200) {
        this.productSelected.voucherProducts = [];
        productVoucher.applyProductVoucher = productVoucher.applyProductVoucher.filter(
          productApply => productApply.parentProductId !== this.productSelected?.productProductUnitId
        );
        removeVoucherOrderType200(this.orderSelectedLocal, voucher);
      }
      productVoucher.listProductPromo?.forEach(productPromo => {
        productPromo.quantity = 0;
        productPromo.inProductOrder = false;
      });
      this.checkShowApplyProductVoucher(this.listVoucherLocalShow);
      return;
    }
  }

  handleSubmit(event) {
    this.type = 1;
    if (event) {
      this.orderSelectedLocal.products = event.orderSelected.products;
      this.productVoucherSelected.applyProductVoucher = event.productVoucher.applyProductVoucher;
      this.productVoucherSelected.listProductPromo = event.productVoucher.listProductPromo;
      if (this.productSelected) {
        this.productSelected = this.orderSelectedLocal.products.find(
          product => product.productUniqueId == this.productSelected.productUniqueId
        );
        if (!this.productSelected.voucherProducts.length) {
          this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(
            voucherOrder => voucherOrder.id !== this.voucherSelected.id
          );
        }
      }
      this.checkShowApplyProductVoucher(this.listVoucherLocalShow);
    }
  }

  changeVoucherOrder(voucher, productVoucher) {
    if (productVoucher.status) {
      if (this.invoiceConfiguration.combineVoucherApply && (voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300)) {
        this.changeVoucherOrderCheckbox(voucher, productVoucher);
      } else {
        if (this.voucherCode == voucher.code) {
          this.clearRadioButton(voucher, productVoucher);
        } else {
          this.voucherCode = voucher.code;
          if (productVoucher.listProductPromo?.length == 1) {
            this.addPromoToProduct(productVoucher.listProductPromo[0], productVoucher, voucher);
          } else {
            this.changeVoucherOrderRadio(voucher, productVoucher);
          }
        }
      }
      if (this.orderSelectedLocal.vouchers.find(voucher => voucher.type == VoucherType.type100 || voucher.type == VoucherType.type300)) {
        this.showSP1Product = true;
      } else {
        this.showSP1Product = false;
      }
    }
  }

  changeVoucherOrderRadio(voucher, productVoucher) {
    switch (productVoucher.type) {
      case VoucherType.type100:
      case VoucherType.type300:
        let voucherAmountOrder;
        if (productVoucher.discountPercent && !productVoucher.discountType) {
          voucherAmountOrder = (this.orderSelectedLocal.amount * productVoucher.discountPercent) / 100;
        }
        this.orderSelectedLocal.vouchers = [
          {
            id: voucher.id,
            code: voucher.code,
            voucherValue: voucherAmountOrder || productVoucher.discountValue,
            voucherPercent: productVoucher.discountPercent || 0,
            desc: productVoucher.desc,
            type: voucher.type,
            minValue: productVoucher.minValue || 0,
            maxValue: productVoucher.maxValue || 0,
          },
        ];

        if (!this.invoiceConfiguration.combineVoucherApply) {
          this.orderSelectedLocal.products = this.orderSelectedLocal.products.filter(product => product.feature !== SPKMProduct.feature);
        }

        break;
      case VoucherType.type102:
        this.orderSelectedLocal.products = this.orderSelectedLocal.products.filter(productOrder => {
          return productOrder.feature !== SPKMProduct.feature;
        });
        this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => {
          return voucherOrder.type !== VoucherType.type102;
        });
        this.handleVoucherRaido(voucher, productVoucher);
        break;
      case VoucherType.type200:
        if (this.invoiceConfiguration.combineVoucherApply) {
          this.orderSelectedLocal.products.forEach(product => {
            product.voucherProducts?.forEach(productVoucher => {
              if (productVoucher.parentProductId == this.productSelected.productProductUnitId) {
                this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(
                  voucher => voucher.id !== productVoucher.voucherId
                );
              }
            });
          });

          this.orderSelectedLocal.vouchers.forEach(voucherOrder => {
            if (voucherOrder.type == VoucherType.type200) {
              let findVoucher = false;
              this.orderSelectedLocal.products.forEach((product: any) => {
                if (product.voucherProducts[0]?.voucherId == voucherOrder.id) {
                  findVoucher = true;
                }
              });
              if (!findVoucher) {
                this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucher => voucher.id !== voucherOrder.id);
              }
            }
          });
        } else {
          this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => {
            return voucherOrder.type !== VoucherType.type200;
          });
        }
        this.productSelected.voucherProducts = [];
        this.handleVoucherRaido(voucher, productVoucher);
        break;
      default:
    }
    if (this.productVoucherSelected) {
      this.productVoucherSelected.applyProductVoucher = this.productVoucherSelected.applyProductVoucher.filter(
        productApply => productApply.parentProductId !== (this.productSelected?.productProductUnitId || null)
      );
      this.productVoucherSelected.listProductPromo.forEach(productPromo => {
        productPromo.quantity = 0;
        productPromo.inProductOrder = false;
      });
    }
    this.productVoucherSelected = productVoucher;

    updateOrder(this.orderSelectedLocal, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  handleVoucherRaido(voucher, productVoucher) {
    const voucherOrder = {
      id: voucher.id,
      code: voucher.code,
      voucherValue: 0,
      voucherPercent: 0,
      desc: productVoucher.desc,
      type: voucher.type,
      minValue: productVoucher.minValue || 0,
      maxValue: productVoucher.maxValue || 0,
    };
    // chọn 1
    if (!this.invoiceConfiguration.combineVoucherApply) {
      this.orderSelectedLocal.vouchers = [voucherOrder];
    }
    // chọn nhiều
    else {
      let voucherOrderExist = this.orderSelectedLocal.vouchers.find(voucher => voucher.code == voucherOrder.code);
      if (voucherOrderExist) {
        voucherOrderExist = voucherOrder;
      } else {
        this.orderSelectedLocal.vouchers.push(voucherOrder);
      }
    }
    // this.orderSelectedLocal.products.push(productVoucher.applyProductVoucher);
  }

  changeVoucherOrderCheckbox(voucher, productVoucher) {
    voucher.check = !voucher.check;
    let voucherAmountOrder;
    if (productVoucher.discountPercent) {
      voucherAmountOrder = (this.orderSelectedLocal.amount * productVoucher.discountPercent) / 100;
    }
    if (voucher.check) {
      if (!(this.orderSelectedLocal.vouchers?.length > 0)) {
        this.orderSelectedLocal.vouchers = [];
      }
      this.orderSelectedLocal.vouchers.push({
        id: voucher.id,
        code: voucher.code,
        voucherValue: voucherAmountOrder || productVoucher.discountValue,
        voucherPercent: productVoucher.discountPercent || 0,
        desc: productVoucher.desc,
        type: voucher.type,
        minValue: productVoucher.minValue || 0,
        maxValue: productVoucher.maxValue || 0,
      });
      voucher.check = true;
    } else {
      this.statusCheckAllVoucher = false;
      this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => voucherOrder.code !== voucher.code);
      voucher.check = false;
    }
    updateOrder(this.orderSelectedLocal, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  changeVat(vatRate) {
    const findIndex = this.listVat.findIndex(item => item.value === vatRate && item.id === this.listVat.length);
    // Không phải thuế suất khác
    if (findIndex < 0) {
      this.productVoucherOrderLocal.vatRate = vatRate;
      this.statusVatRateInput = false;
    } else {
      this.statusVatRateInput = true;
      this.productVoucherOrderLocal.vatRate = 0;
    }
    updateOrder(this.orderSelectedLocal, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  changeProductNameCustom(productNameCustom) {
    this.productVoucherOrderLocal.productName = productNameCustom || 'Chiết khấu';
  }

  changeVatRateOrder(vatRate) {
    this.listVat[this.listVat.length - 1].value = vatRate || -3;
    this.listVat = [...this.listVat];
    this.productVoucherOrderLocal.vatRate = vatRate || -3;
    updateOrder(this.orderSelectedLocal, this.invoiceConfiguration, this.invoiceType, this.lastCompany);
  }

  toggleAllVoucher(event) {
    if (event?.target.checked) {
      this.orderSelectedLocal.vouchers = [];
      this.listVoucherLocalShow.forEach(voucher => {
        voucher.conditions?.forEach(productVoucher => {
          if (productVoucher.status && (productVoucher.type == 100 || productVoucher.type == 300)) {
            voucher.check = false;
            this.changeVoucherOrderCheckbox(voucher, productVoucher);
          }
        });
      });
    } else {
      this.listVoucherLocalShow.forEach(voucher => {
        voucher.check = false;
      });
      this.orderSelectedLocal.vouchers = [];
    }
  }

  decreaseProduct(productApply, productVoucher, listProductPromo, voucher) {
    if (productApply.quantity > 1) {
      productApply.quantity--;
      listProductPromo.forEach(productPromo => {
        if (productPromo.productProductUnitId == productApply.productProductUnitId) {
          productPromo.quantity--;
        }
      });
      this.changeProductSelected(productApply, productVoucher);
    } else {
      this.removeProductVoucher(productApply, productVoucher, listProductPromo, voucher);
    }
  }

  increaseProduct(productApply, listProductPromo, productVoucher) {
    if (
      !checkOverStockProductPromo(
        { ...productApply, quantity: productApply.quantity + 1 },
        this.voucherSelected,
        productVoucher,
        this.invoiceConfiguration,
        this.lastCompany,
        this.toast,
        this.productSelected
      )
    ) {
      return;
    }
    productApply.quantity++;
    listProductPromo.forEach(productPromo => {
      if (productPromo.productProductUnitId == productApply.productProductUnitId) {
        productPromo.quantity++;
      }
    });
    this.changeProductSelected(productApply, productVoucher);
  }

  changeProductOrderPromo(productApply) {
    this.orderSelectedLocal.products.forEach(productPromo => {
      if (productPromo.feature == SPKMProduct.feature && productPromo.productProductUnitId == productApply.productProductUnitId) {
        productPromo.quantity = productApply.quantity;
      }
    });
  }

  changeProductPromoQuantity(productApply, productVoucher, listProductPromo) {
    listProductPromo.forEach(productPromo => {
      if (productPromo.productProductUnitId == productApply.productProductUnitId) {
        productPromo.quantity = productApply.quantity;
      }
    });
    this.changeProductSelected(productApply, productVoucher);
  }

  checkProductVoucherApplyQuantity(productApply, productVoucher, listProductPromo, voucher) {
    if (!productApply.quantity) {
      this.removeProductVoucher(productApply, productVoucher, listProductPromo, voucher);
    }
  }

  removeProductVoucher(productApply, productVoucher, listProductPromo, voucher) {
    if (this.productSelected) {
      this.productSelected.voucherProducts = this.productSelected.voucherProducts.filter(
        productVoucherOrder => productVoucherOrder.productProductUnitId !== productApply.productProductUnitId
      );
    } else {
      this.orderSelectedLocal.products = this.orderSelected.products.filter(
        productSelected =>
          !(productSelected.productProductUnitId == productApply.productProductUnitId && productSelected.feature == SPKMProduct.feature)
      );
    }

    productVoucher.applyProductVoucher = productVoucher.applyProductVoucher?.filter(
      productPromo =>
        !(
          productPromo.productProductUnitId == productApply.productProductUnitId &&
          (this.productSelected ? productPromo.parentProductId == productApply.parentProductId : true)
        )
    );
    listProductPromo.forEach(productPromo => {
      if (productPromo.productProductUnitId == productApply.productProductUnitId) {
        productPromo.quantity = 0;
        productPromo.inProductOrder = false;
      }
    });

    if (!productVoucher.applyProductVoucher.length) {
      this.voucherCode = '';
      this.orderSelectedLocal.vouchers = this.orderSelectedLocal.vouchers.filter(voucherOrder => {
        return voucherOrder.code !== voucher.code;
      });
    }
    // this.checkShowApplyProductVoucher(this.listVoucherLocalShow);
  }

  changeProductSelected(productApply, productVoucher) {
    updateDiscountAmountProductPromo(productApply, productVoucher);
    changeProductSelected(productApply, this.lastCompany, this.invoiceConfiguration, InvoiceType);
    if (this.productSelected) {
      this.productSelected.voucherProducts.forEach(productVoucher => {
        if (productVoucher.productProductUnitId == productApply.productProductUnitId) {
          productVoucher.quantity = productApply.quantity;
          updateDiscountAmountProductPromo(productVoucher, productVoucher);
          changeProductSelected(productVoucher, this.lastCompany, this.invoiceConfiguration, InvoiceType);
        }
      });
    } else {
      this.changeProductOrderPromo(productApply);
    }
  }

  closeModal() {
    this.activeModal.dismiss(this.orderSelected);
  }

  preventIncrement(e) {
    if (e.keyCode === 38 || e.keyCode === 40) {
      e.preventDefault();
    }
  }

  onChangedPage(event: any): void {
    this.filterVoucher.page = event;
  }

  saveVoucherOrder() {
    for (let voucherOrder of this.orderSelectedLocal.vouchers) {
      if (voucherOrder.type == VoucherType.type102) {
        let listProductPromo = this.orderSelectedLocal.products.filter(productOrder => productOrder.feature == SPKMProduct.feature);
        if (!listProductPromo.length) {
          this.toast.warning('Bạn chưa chọn sản phẩm khuyến mại!');
          return;
        }
      }
      if (voucherOrder.type == VoucherType.type200) {
        let findProductPromo = false;
        this.orderSelectedLocal.products.forEach(product => {
          product.voucherProducts.forEach(productVoucher => {
            if (productVoucher.voucherId == voucherOrder.id) {
              if (voucherOrder.code == this.voucherCode) {
                if (productVoucher.parentProductId == this.productSelected.productProductUnitId) {
                  findProductPromo = true;
                }
              } else {
                findProductPromo = true;
              }
            }
          });
        });

        if (!findProductPromo) {
          this.toast.warning('Bạn chưa chọn sản phẩm khuyến mại');
          return;
        }
      }
    }

    if (this.invoiceConfiguration.combineVoucherApply) {
      this.activeModal.close({
        orderSelected: this.orderSelectedLocal,
        voucherProduct: this.productVoucherOrderLocal,
        listVoucher: this.listVoucherLocalShow,
      });
    } else {
      this.type = 3;
      let voucherRemove = checkMultipleVoucherRadio(this.orderSelected, this.orderSelectedLocal, this.productSelected);
      if (voucherRemove) {
        const dialogRef = this.modalService.open(ConfirmDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
        dialogRef.componentInstance.value = new DialogModal(
          ModalHeader.SAVE_VOUCHER,
          ModalContent.SAVE_VOUCHER,
          ModalBtn.SAVE_VOUCHER,
          'check',
          'btn-save'
        );
        // @ts-ignore
        dialogRef.componentInstance.formSubmit.subscribe(async res => {
          if (res) {
            dialogRef.close();
            this.activeModal.close({
              orderSelected: this.orderSelectedLocal,
              voucherProduct: this.productVoucherOrderLocal,
              listVoucher: this.listVoucherLocalShow,
              voucherRemove: voucherRemove,
            });
          } else {
            this.type = 1;
          }
        });
      } else {
        this.activeModal.close({
          orderSelected: this.orderSelectedLocal,
          voucherProduct: this.productVoucherOrderLocal,
          listVoucher: this.listVoucherLocalShow,
        });
      }
    }
  }

  addPromoToProduct(product, productVoucher, voucher) {
    if (productVoucher.status) {
      if (!this.invoiceConfiguration.overStock && product.inventoryCount <= 0 && product.inventoryTracking) {
        this.toast.warning(`Sản phẩm ${product.productName} xuất quá số lượng tồn (${product.inventoryCount} ${product.unit || ''})`);
        return;
      }
      let productVoucherOrder = convertProductToProductOrder(
        product,
        voucher,
        productVoucher,
        this.invoiceConfiguration,
        this.invoiceType,
        this.orderSelectedLocal,
        this.productSelected
      );
      if (
        !checkOverStockProductPromo(
          { ...productVoucherOrder, quantity: productVoucherOrder.quantity },
          this.voucherSelected,
          productVoucher,
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
        this.voucherCode = voucher.code;
        this.changeVoucherOrderRadio(voucher, productVoucher);
      }
      productVoucher.applyProductVoucher.push(productVoucherOrder);
      if (productVoucher.type == VoucherType.type102) {
        addProductSelectedToList(productVoucherOrder, this.orderSelectedLocal);
      } else {
        this.productSelected.voucherProducts.push(productVoucherOrder);
      }
      this.checkShowApplyProductVoucher(this.listVoucherLocalShow);
      this.changeProductPromoQuantity(productVoucherOrder, productVoucher, productVoucher.listProductPromo);
    }
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
