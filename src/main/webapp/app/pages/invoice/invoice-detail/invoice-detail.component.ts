import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { InvoiceService } from '../service/invoice.service';
import { ToastrService } from 'ngx-toastr';
import { BILL } from '../../../constants/app.routing.constants';
import {ICON_RESULTS_NULL} from "../../../shared/other/icon";

@Component({
  selector: 'jhi-invoice-detail',
  templateUrl: './invoice-detail.component.html',
  styleUrls: ['./invoice-detail.component.scss'],
})
export class InvoiceDetailComponent implements OnInit {
  @Input() id!: number;
  invoice!: any;

  constructor(
    public activeModal: NgbActiveModal,
    private invoiceService: InvoiceService,
    private route: Router,
    private toast: ToastrService
  ) {}

  ngOnInit(): void {
    this.getInvoiceDetailById(this.id);
  }

  getInvoiceDetailById(id) {
    this.invoiceService.find(id).subscribe((res: any) => {
      this.invoice = res.data;
    });
  }

  closeModal() {
    this.activeModal.close();
  }

  createNewOrder() {
    this.activeModal.close();
    this.route.navigate([BILL]);
  }

  publishInvoice() {
    const request = [
      {
        invoiceId: this.id,
        invoiceIKey: this.invoice.ikey,
      },
    ];
    this.invoiceService.publishInvoice(request).subscribe(
      res => {
        // console.log(res);
        if (res.status) {
          this.toast.success(res.reason);
          this.activeModal.close();
        } else {
          this.toast.success(res.message[0].message);
        }
      },
      error => {
        // console.log(error);
        this.toast.success(error.error.message[0].message);
      }
    );
  }

  editInvoice() {}

  deleteInvoice() {
    this.invoiceService.deleteInvoice(this.id).subscribe(
      res => {
        // console.log(res);
        if (res.status) {
          this.toast.success(res.reason);
          this.activeModal.close();
        } else {
          this.toast.success(res.message[0].message);
        }
      },
      error => {
        // console.log(error);
        this.toast.success(error.error.message[0].message);
      }
    );
  }

  viewInvoice() {}

  sendMail() {}

    protected readonly ICON_RESULTS_NULL = ICON_RESULTS_NULL;
}
