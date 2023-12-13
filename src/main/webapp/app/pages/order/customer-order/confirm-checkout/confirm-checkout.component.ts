import { Component, Input, OnInit, Output, EventEmitter, ViewChild, ElementRef } from '@angular/core';
import { CompleteOrder, IBillPayment, InvoiceConfiguration, LastCompany } from '../../model/bill-payment.model';
import { BillService } from '../../service/bill.service';
import dayjs from 'dayjs/esm';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceType, PaymentMethod, StatusNotify } from 'app/pages/const/customer-order.const';
import { AppDB } from 'app/db';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { LoadingOption } from 'app/utils/loadingOption';
import { checkProductVoucherOrder, getOrderReturnFromOrderSelected, numberOnly, updatePosition } from 'app/pages/const/function';
import { BaseComponent } from 'app/shared/base/base.component';
import { offline_bill } from 'app/object-stores.constants';
import { ToastrService } from 'ngx-toastr';
import { lastValueFrom } from 'rxjs';
import { CustomerService } from '../../service/customer.service';
import { WebsocketService } from 'app/config/service/websocket-service.service';
import { ISocketConfigModel } from 'app/entities/socket/socket-config.model';
import { ICON_CANCEL } from '../../../../shared/other/icon';

@Component({
  selector: 'jhi-confirm-checkout',
  templateUrl: './confirm-checkout.component.html',
  styleUrls: ['./confirm-checkout.component.scss'],
})
export class ConfirmCheckoutComponent extends BaseComponent implements OnInit {
  @Input() orderSelected!: IBillPayment;
  @Input() status?: string = 'online';
  @Input() statusUpdateOrder?: boolean;
  @Output() itemSubmited: EventEmitter<[boolean, string]> = new EventEmitter();
  togglePaymentMethod = false;
  debtMoney = 0;
  lastCompany: LastCompany;
  invoiceConfiguration: InvoiceConfiguration;
  isLoading = false;
  paymentMethod = PaymentMethod;
  invoiceType = InvoiceType;
  isCancelOrder = false;
  cancelPayment = 0;
  returnOrder = false;
  @ViewChild('focus') focus: ElementRef;

  constructor(
    private websocketService: WebsocketService,
    protected orderService: BillService,
    public activeModal: NgbActiveModal,
    public loading: LoadingOption,
    public customerService: CustomerService,
    private toast: ToastrService
  ) {
    super();
  }

  async ngOnInit() {
    this.websocketService.connect();
    if (this.isCancelOrder) {
      if (this.orderSelected.payment.amount) {
        if (this.orderSelected.payment.refund !== null && this.orderSelected.payment.refund !== undefined) {
          this.cancelPayment = this.orderSelected.payment.amount - this.orderSelected.payment.refund;
        } else {
          this.cancelPayment = this.orderSelected.payment.amount;
        }
      }
    } else {
      if (!this.returnOrder) {
        if (this.orderSelected.status) {
          this.debtMoney = this.orderSelected.payment.debt || 0;
          this.orderSelected.totalAmount = this.debtMoney;
          this.orderSelected.payment.amount = this.debtMoney;
          this.orderSelected.payment.debt = 0;
        } else {
          this.orderSelected.payment.amount = this.orderSelected.totalAmount;
        }
        if (!this.orderSelected.payment.paymentMethod) {
          this.orderSelected.payment.paymentMethod = PaymentMethod.cash;
        }
        if (
          this.orderSelected.payment.paymentMethod !== PaymentMethod.transfer &&
          this.orderSelected.payment.paymentMethod !== PaymentMethod.cash
        ) {
          this.togglePaymentMethod = true;
        }
      }
    }
  }

  ngAfterViewInit() {
    this.focus?.nativeElement.focus();
  }

  showReasonInput() {
    if (!this.togglePaymentMethod) {
      this.togglePaymentMethod = true;
      this.orderSelected.payment.paymentMethod = '';
    }
  }

  hideReasonInput() {
    this.togglePaymentMethod = false;
  }

  async checkout() {
    if (this.isCancelOrder) {
      this.cancelOrder();
    } else {
      if (this.returnOrder) {
        this.returnOrderCustomer();
      } else {
        this.completeOrder();
      }
    }
  }

  returnOrderCustomer() {
    let req = getOrderReturnFromOrderSelected(this.orderSelected);
    this.orderService.returnBill(req).subscribe(
      value => {
        this.toast.success(value.message[0].message);
        this.isLoading = false;
        this.activeModal.close(2);
      },
      error => {
        this.isLoading = false;
      }
    );
  }

  async getTaxAuthorityCode(order) {
    const invoiceParttern = this.lastCompany.invoicePattern ? this.lastCompany.invoicePattern.substring(0, 1) : '1';
    const taxAuthCodePrefix = this.lastCompany.taxAuthCodePrefix ? this.lastCompany.taxAuthCodePrefix : '00000';
    const deviceCode = this.lastCompany.deviceCode ? this.lastCompany.deviceCode : '000';
    order.comId = this.lastCompany.id;
    if (this.lastCompany.invoicePattern && this.lastCompany.taxAuthCodePrefix && this.lastCompany.deviceCode) {
      order.taxAuthorityCode =
        'M' +
        invoiceParttern +
        '-' +
        (new Date().getFullYear() % 2000) +
        '-' +
        taxAuthCodePrefix +
        '-' +
        deviceCode +
        Math.floor(Date.now()).toString().substr(-8, 8);
    }
  }

  async completeOrder() {
    let orderSelectedLocal = JSON.parse(JSON.stringify(this.orderSelected));
    this.isLoading = true;
    const db = new AppDB();
    updatePosition(this.orderSelected);
    checkProductVoucherOrder(this.orderSelected);

    let listVoucherId = this.orderSelected.vouchers?.map(voucher => voucher.id);
    if (listVoucherId?.length) {
      let checkVoucher: any = await lastValueFrom(this.customerService.checkVoucher(listVoucherId, this.orderSelected.customerId));
      if (checkVoucher.body.data) {
        this.toast.warning('Khuyến mại voucher đã hết hạn, vui lòng kiểm tra lại');
        return;
      }
    }

    if (!this.orderSelected.id) {
      if (this.status === 'online') {
        this.getTaxAuthorityCode(this.orderSelected);
        this.orderService.create({ ...this.orderSelected, status: 1 }).subscribe((res: any) => {
          this.isLoading = false;
          this.activeModal.close([res.body.data.billId, res.body.reason]);
        });
      } else {
        const res: any = await this.addItem(offline_bill, {
          ...this.orderSelected,
          status: 1,
          billDate: dayjs().format(DATE_TIME_FORMAT),
        });
        if (res) {
          this.activeModal.close([true, res.body.reason]);
        }
      }
    } else {
      if (this.debtMoney) {
        this.orderService
          .payOffDebt({
            id: this.orderSelected.id,
            amount: this.orderSelected.payment.amount,
            cardAmount: this.orderSelected.payment.cardAmount ?? 0,
          })
          .subscribe((res: any) => {
            this.isLoading = false;
            this.activeModal.close([true, res.body.reason]);
          });
      } else {
        if (this.statusUpdateOrder) {
          await lastValueFrom(this.orderService.update({ ...this.orderSelected, status: 1 }));
        }
        const completeOrderForm: CompleteOrder = {
          billId: this.orderSelected.id,
          billCode: this.orderSelected.code,
          paymentMethod: this.orderSelected.payment.paymentMethod,
          paymentTime: dayjs(),
          amount: this.orderSelected.payment.amount,
          refund: this.orderSelected.payment.refund ?? 0,
          debt: this.orderSelected.payment.debt ?? 0,
          cardAmount: this.orderSelected.payment.cardAmount ?? 0,
        };
        this.orderService.complete(completeOrderForm).subscribe((res: any) => {
          this.isLoading = false;
          this.activeModal.close([this.orderSelected.id, res.body.reason]);
        });
      }
    }
    this.orderSelected.status = 0;
  }

  cancelOrder() {
    if (this.orderSelected.statusInvoice === 1) {
      this.activeModal.close(1);
    } else {
      this.isLoading = true;
      this.orderService
        .billCancel({
          comId: this.orderSelected.comId,
          billId: this.orderSelected.id,
          paymentMethod: this.orderSelected.payment.paymentMethod,
        })
        .subscribe(
          value => {
            this.toast.success(value.message[0].message);
            this.isLoading = false;
            this.activeModal.close(2);
          },
          error => {
            this.isLoading = false;
          }
        );
    }
  }

  checkMoneyBalanceCustomer(event) {
    if (event.target.checked) {
      this.orderSelected.statusPaymentBycard = true;
      // ko đủ tiền
      if (this.orderSelected.moneyBalanceCustomer < this.orderSelected.totalAmount) {
        this.orderSelected.payment.amount = this.orderSelected.totalAmount - this.orderSelected.moneyBalanceCustomer;
        this.orderSelected.payment.cardAmount = this.orderSelected.moneyBalanceCustomer;
        this.orderSelected.payment.debt = 0;
      } else {
        this.orderSelected.payment.amount = 0;
        this.orderSelected.payment.cardAmount = this.orderSelected.totalAmount;
        this.orderSelected.payment.debt = 0;
      }
    } else {
      this.orderSelected.statusPaymentBycard = false;
      this.orderSelected.payment.amount = this.orderSelected.totalAmount;
      this.orderSelected.payment.cardAmount = 0;
      this.orderSelected.payment.debt = 0;
    }
  }

  changeMoneybalanceCustomerAmount() {
    if (this.orderSelected.payment.cardAmount >= (this.orderSelected.status ? this.debtMoney : this.orderSelected.totalAmount)) {
      this.orderSelected.payment.amount = 0;
      this.changeCustomerAmount();
    } else {
      this.orderSelected.payment.amount =
        (this.orderSelected.status ? this.debtMoney : this.orderSelected.totalAmount) - this.orderSelected.payment.cardAmount;
      this.orderSelected.payment.debt = 0;
      this.orderSelected.payment.refund = 0;
    }
  }

  changeCustomerAmount() {
    const priceRefund =
      (this.orderSelected.payment.cardAmount || 0) +
      this.orderSelected.payment.amount -
      (this.orderSelected.status ? this.debtMoney : this.orderSelected.totalAmount);
    if (priceRefund >= 0) {
      this.orderSelected.payment.refund = priceRefund;
      this.orderSelected.payment.debt = undefined;
    } else {
      this.orderSelected.payment.debt = Math.abs(priceRefund);
      this.orderSelected.payment.refund = undefined;
    }
  }

  onCheckboxChange(event) {
    if (event.target.checked) {
      this.orderSelected.statusPaymentBycard = false;
      this.orderSelected.payment.debt = this.orderSelected.totalAmount;
      this.orderSelected.payment.cardAmount = 0;
      this.orderSelected.payment.amount = 0;
    } else {
      this.orderSelected.payment.debt = 0;
      this.orderSelected.payment.amount = this.orderSelected.totalAmount;
    }
    this.orderSelected.payment.refund = 0;
  }

  convertStringToNumber(value) {
    return parseFloat(value.replace(/,/g, ''));
  }

  closeModal() {
    this.orderSelected.payment.amount = 0;
    this.orderSelected.payment.debt = 0;
    this.orderSelected.payment.refund = 0;
    this.orderSelected.payment.cardAmount = 0;
    this.orderSelected.payment.paymentMethod = '';
    this.orderSelected.statusPaymentBycard = false;
    this.activeModal.dismiss();
  }

  numberOnly(event: any): boolean {
    return numberOnly(event);
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
}
