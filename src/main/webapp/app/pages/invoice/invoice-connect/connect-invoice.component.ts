import { Component, OnDestroy, AfterViewInit, OnInit } from '@angular/core';
import appSettings from '../../../config/app-settings';
import { NgbModal, NgbToast } from '@ng-bootstrap/ng-bootstrap';
import { ModalDialogComponent } from '../../../shared/modal/modal-dialog.component';
import { InvoiceService } from '../service/invoice.service';
import { ToastrModule, ToastrService } from 'ngx-toastr';

@Component({
  selector: 'customer-order',
  templateUrl: './connect-invoice.component.html',
  styleUrls: ['./connect-invoice.component.scss'],
})
export class ConnectInvoiceComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  posMobileSidebarToggled = false;

  menuType: any;

  filter: any;
  loginForm: any = {};
  companies: any;
  errorMessage: boolean;

  togglePosMobileSidebar() {
    this.posMobileSidebarToggled = !this.posMobileSidebarToggled;
  }

  constructor(private modalService: NgbModal, private invoiceService: InvoiceService, public toast: ToastrService) {
    this.menuType = 0;
  }

  ngAfterViewInit() {
    var targets = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));

    targets.map(function (target: any) {
      target.onclick = function (e) {
        e.preventDefault();

        var targetBtn = e.target;
        var targetFilter = targetBtn.getAttribute('data-filter');

        targetBtn.classList.add('active');

        var allFilter = [].slice.call(document.querySelectorAll('.pos-menu [data-filter]'));

        allFilter.map(function (filterElm: any) {
          var filterElmFilter = filterElm.getAttribute('data-filter');

          if (targetFilter != filterElmFilter) {
            filterElm.classList.remove('active');
          }
        });

        var allContent = [].slice.call(document.querySelectorAll('.pos-content [data-type]'));
        allContent.map(function (contentElm: any) {
          var contentType = contentElm.getAttribute('data-type');

          if (targetFilter == 'all') {
            contentElm.classList.remove('d-none');
          } else {
            if (contentType != targetFilter) {
              contentElm.classList.add('d-none');
            } else {
              contentElm.classList.remove('d-none');
            }
          }
        });
      };
    });
  }

  ngOnDestroy() {
    this.appSettings.appEmpty = false;
    this.appSettings.appContentFullHeight = false;
  }

  openPopup() {
    this.modalService.open(ModalDialogComponent, { size: 'lg', backdrop: 'static', windowClass: 'margin-5' });
  }

  ngOnInit() {
    this.filter = 'all';
  }

  printPage() {
    window.print();
  }

  login() {
    this.invoiceService.updateInfoConfig(this.loginForm).subscribe(res => {
      this.toast.success(res.reason);
    });
  }
}
