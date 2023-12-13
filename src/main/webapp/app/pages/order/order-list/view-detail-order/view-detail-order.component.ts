import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceType, LIST_VAT, NoTaxProduct } from 'app/pages/const/customer-order.const';
import { ConfirmCheckoutComponent } from 'app/pages/order/customer-order/confirm-checkout/confirm-checkout.component';
import { BillService } from 'app/pages/order/service/bill.service';
import { ToastrService } from 'ngx-toastr';
import { ShowListActionOrderComponent } from './show-list-action-order/show-list-action-order.component';
import { ConfirmDialogService } from 'app/shared/service/confirm-dialog.service';
import { lastValueFrom } from 'rxjs';
import { InvoiceService } from 'app/pages/invoice/service/invoice.service';
import { BaseComponent } from 'app/shared/base/base.component';
import { InvoiceConfiguration } from '../../model/bill-payment.model';

@Component({
  selector: 'jhi-view-detail-order',
  templateUrl: './view-detail-order.component.html',
  styleUrls: ['./view-detail-order.component.scss'],
})
export class ViewDetailOrderComponent extends BaseComponent implements OnInit {
  @Input() idOrderSelected!: number;
  @Input() orderSelected?: any;
  @Output() dataOnChange = new EventEmitter<boolean>();
  orderDetail!: any;
  listVat = LIST_VAT;
  invoiceType = InvoiceType;
  invoiceConfiguration: InvoiceConfiguration;
  indexTab = 0;

  constructor(
    private confirmDialog: ConfirmDialogService,
    public orderService: BillService,
    private route: Router,
    private toast: ToastrService,
    protected modalService: NgbModal,
    private invoiceService: InvoiceService
  ) {
    super();
  }

  async ngOnInit() {
    console.log('hello mother fucker');
    const lastCompany = await this.getCompany();
    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
    this.getOrderDetailById(this.idOrderSelected);
  }

  getOrderDetailById(id) {
    let index = 0;
    this.orderService.find(id).subscribe((res: any) => {
      this.orderDetail = res.body.data;
      this.orderDetail.products.sort((a, b) => {
        return a.feature - b.feature;
      });
      this.orderDetail.products.forEach(product => {
        if (product !== 3) {
          index++;
          product.index = index;
          product.toppings?.forEach(productTopping => {
            index++;
            productTopping.index = index;
          });
          product.voucherProducts?.forEach(productVoucher => {
            index++;
            productVoucher.index = index;
          });
        }
        const vatRate = this.listVat.find(item => item.value === (product.vatRate ?? 0));
        if (!vatRate) {
          product.vatRateName = NoTaxProduct.name;
        } else {
          product.vatRateName = vatRate.value >= 0 ? vatRate.value + '%' : vatRate.name;
        }
      });
      // this.orderDetail.products.sort((a, b) => a.position - b.position);
    });
  }

  checkoutOrderCustomer() {
    const dialogRef = this.modalService.open(ConfirmCheckoutComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderSelected = this.orderDetail;
    dialogRef.closed.subscribe(res => {
      if (res[0]) {
        this.toast.success('Thanh toán đơn hàng thành công !');
        this.dataOnChange.emit(true);
      }
    });
  }

  showListAction() {
    const dialogRef = this.modalService.open(ShowListActionOrderComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
    dialogRef.componentInstance.orderDetail = this.orderDetail;
    dialogRef.closed.subscribe(res => {
      // if(res){this.activeModal.close()}
    });
  }
}
