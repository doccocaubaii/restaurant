import { Location } from '@angular/common';
import { Component, HostListener, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as QRCode from 'qrcode';
import { BaseComponent } from '../../../../../shared/base/base.component';
import { ConfigService } from '../../../../../layouts/config/service/config.service';
import { UtilsService } from '../../../../../utils/Utils.service';
import { ProductBill } from 'app/pages/order/model/bill-payment.model';

@Component({
  selector: 'jhi-pos-invoice-print',
  templateUrl: './pos-invoice-print.component.html',
  styleUrls: ['./pos-invoice-print.component.scss'],
})
export class PosInvoicePrintComponent extends BaseComponent implements OnInit {
  @Input() orderSelected?: any = {};
  @Input() isHide?: boolean = false;
  @Input() type: number = 0;
  statusPrint = false;
  @Input() printConfigMap: Map<any, any>;
  urlQR: any;
  lastCompany: any;
  listHeader = ['StoreName', 'PhoneNumber', 'StoreAddress', 'Title'];
  listContent = ['InvTime', 'PhoneNumber', 'StoreAddress', 'Title'];
  @Input() isDefault = false;
  constructor(
    public activeModal: NgbActiveModal,
    protected modalService: NgbModal,
    private configService: ConfigService,
    public utilsService: UtilsService,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    this.orderSelected.products.forEach(productOrder => {
      productOrder.totalAmountTopping = 0;
      productOrder.displayTotalAmount = productOrder.totalAmount;
      productOrder.displayVatAmount = productOrder.vatAmount;
      productOrder.toppings?.forEach(productTopping => {
        productOrder.totalAmountTopping += productTopping.amount;
        productOrder.displayVatAmount += productTopping.vatAmount;
        productOrder.displayTotalAmount += productTopping.totalAmount;
      });
      // productOrder.displayTotalAmount = productOrder.totalAmount + productOrder.totalAmountTopping + productOrder.displayVatAmount;
      let arrayProductToppingName = this.getProductToppingName(productOrder);
      productOrder.productToppingName = arrayProductToppingName.join(' - ');
    });
  }

  getProductToppingName(productOrder) {
    let arrayProductToppingName: string[] = [];
    productOrder.toppings?.forEach((productTopping: ProductBill) => {
      arrayProductToppingName.push('x' + productTopping.displayQuantity + ' ' + productTopping.productName);
    });
    return arrayProductToppingName;
  }

  closeModal() {
    this.activeModal.close();
  }

  printInvoid() {
    this.statusPrint = !this.statusPrint;
    setTimeout(() => {
      window.print();
    }, 100);
  }

  @HostListener('window:afterprint')
  onafterprint() {
    this.statusPrint = !this.statusPrint;
  }

  getValue(code: string) {
    let value =
      this.printConfigMap && this.printConfigMap.get(code) && this.printConfigMap.get(code).value
        ? this.printConfigMap.get(code).value
        : '';
    if (code === 'PortalLink') {
      this.genQrCode(value.content);
    }
    return value;
  }

  genQrCode(value: string) {
    if (value) {
      QRCode.toDataURL(value, (err, url) => {
        if (err) {
          console.error(err);
          return;
        }
        this.urlQR = url;
        // In ra URL của mã QR (có thể sử dụng để hiển thị hoặc tải xuống)
      });
    }
  }
}
