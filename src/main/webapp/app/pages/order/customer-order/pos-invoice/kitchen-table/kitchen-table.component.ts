import { Location } from '@angular/common';
import { Component, HostListener, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
import * as QRCode from 'qrcode';
import { ConfigService } from '../../../../../layouts/config/service/config.service';
import { BaseComponent } from '../../../../../shared/base/base.component';
import { UtilsService } from '../../../../../utils/Utils.service';
import { ProductBill } from 'app/pages/order/model/bill-payment.model';

@Component({
  selector: 'kitchen-table-print',
  templateUrl: './kitchen-table.component.html',
  styleUrls: ['./kitchen-table.component.scss'],
})
export class KitchenTableComponent extends BaseComponent implements OnInit {
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
    public utilsService: UtilsService,
    private configService: ConfigService,
    private location: Location
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }
  printStyles = `
    <style>
      @page {
        margin: 2cm; /* Set margin to 2cm */
        @top-center {
          content: "Header content"; /* Set header content */
        }
      }

      #printableContent {
        /* CSS for the printable content */
      }
    </style>
  `;
  printWindow: any;
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
