import { Location } from '@angular/common';
import { Component, HostListener, Input, OnInit } from '@angular/core';
import { NgbActiveModal, NgbModal } from '@ng-bootstrap/ng-bootstrap';
@Component({
  selector: 'jhi-print-invoice',
  templateUrl: './print-invoice.component.html',
  styleUrls: ['./print-invoice.component.scss'],
})
export class PrintInvoiceComponent implements OnInit {
  @Input() orderSelected!: any;
  statusPrint = false;

  constructor(public activeModal: NgbActiveModal, protected modalService: NgbModal, private location: Location) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {}

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
}
