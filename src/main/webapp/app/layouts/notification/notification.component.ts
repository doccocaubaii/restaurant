import { Component, OnInit } from '@angular/core';
import { BaseComponent } from '../../shared/base/base.component';
import { LANGUAGES } from '../../config/language.constants';
import { ICON_DOUBLE_CHECK_MARK, ICON_NOTIFICATION } from '../../shared/other/icon';
import { lastValueFrom, switchMap } from 'rxjs';
import { InvoiceConfiguration, LastCompany } from '../../pages/order/model/bill-payment.model';
import { InvoiceService } from '../../pages/invoice/service/invoice.service';
import { NotificationService } from '../../pages/processing/notification.service';
import { BILL_ORDER } from '../../constants/app.routing.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { NotifyComponent } from '../modal/notify/notify.component';
import { NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'jhi-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.scss'],
})
export class NotificationComponent extends BaseComponent implements OnInit {
  typeSelect: any = null;
  notifications: any[] = [];
  tabs = [
    {
      type: null,
      name: 'Tất cả',
    },
    {
      type: 1,
      name: 'Đơn hàng',
    },
    {
      type: 2,
      name: 'Hệ thống',
    },
  ];

  showLoading = false;
  lastCompany: LastCompany;
  invoiceConfiguration: InvoiceConfiguration;
  isLoading = false;
  showDetail = false;
  image: any = '';
  page = 1;
  size = 10;
  private modalRef: NgbModalRef | undefined;

  constructor(
    private invoiceService: InvoiceService,
    protected ngbModal: NgbModal,
    private notificationService: NotificationService,
    private route: Router,
    protected activatedRoute: ActivatedRoute
  ) {
    super();
  }

  async ngOnInit() {
    this.isLoading = true;
    this.lastCompany = await this.getCompany();
    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
    this.invoiceConfiguration.displayConfig = JSON.parse(this.invoiceConfiguration.displayConfig);

    let displayConfig = this.invoiceConfiguration?.displayConfig.find(config => config.code == 'KITCHEN');
    await this.getListNotification();
    this.isLoading = false;
    if (displayConfig?.active) {
      this.tabs.splice(2, 0, {
        type: 0,
        name: 'Bếp',
      });
    }
  }

  async searchByType(type?: any) {
    this.notifications = [];
    this.page = 1;
    this.typeSelect = type;
    await this.getListNotification();
  }

  async getListNotification() {
    this.showLoading = true;
    let req = {
      page: this.page - 1,
      size: this.size,
      type: this.typeSelect,
      isToday: false,
    };
    this.notificationService.getUnReadNotification(req).subscribe(res => {
      const result = res.body.data;
      result?.forEach(item => {
        this.notifications?.push(item);
      });
      this.showLoading = false;
    });
  }

  openNotifyModal(notify: any[]) {
    if (this.modalRef) {
      this.modalRef.close();
    }
    const modalRef1 = this.ngbModal.open(NotifyComponent, {
      size: 'lg',
      backdrop: 'static',
      windowClass: 'margin-5',
      centered: true,
    });
    modalRef1.componentInstance.notifies = notify;
    modalRef1.closed.subscribe(res => {});
  }

  clickNotification(notification: any) {
    if (notification.image && notification.type == 2) {
      this.isLoading = true;
      setTimeout(() => {
        this.isLoading = false;
        let notify = {
          image: notification.image,
        };
        let notifies: any[] = [];
        notifies.push(notify);
        this.openNotifyModal(notifies);
      }, 500);
    }
    if (!notification.isRead) {
      this.notificationService
        .updateNotification({
          id: notification.id,
          readAll: false,
        })
        .subscribe();
    }
    notification.isRead = true;
    if (notification.billId && notification.type != 2) {
      this.route.navigate([`/${BILL_ORDER}`], {
        relativeTo: this.activatedRoute,
        queryParams: {
          id_order: notification.billId,
        },
      });
    }
  }

  async readAllNotification() {
    this.notificationService
      .updateNotification({
        readAll: true,
      })
      .pipe(
        switchMap(async () => {
          this.notifications = [];
          this.page = 1;
          await this.getListNotification();
        })
      )
      .subscribe();
  }

  closeDetail() {
    this.isLoading = true;
    setTimeout(() => {
      this.showDetail = false;
      this.image = '';
      this.isLoading = false;
    }, 500);
  }

  closePopup() {
    let icon = document.getElementById('notification-icon');
    if (icon) {
      this.closeDetail();
      icon.click();
    }
  }

  async loadMore(event) {
    if (event.target.scrollTop === 0) {
      return;
    }
    if (event.target.offsetHeight + event.target.scrollTop >= event.target.scrollHeight) {
      this.page !== undefined && (this.page += 1);
      await this.getListNotification();
    }
  }

  formatDateTime2(input: any) {
    if (!input) {
      return;
    }
    const dateTimeParts = input.split(' ');
    const dateParts = dateTimeParts[0].split('-');
    const timeParts = dateTimeParts[1].split(':');

    const formattedDate = `${dateParts[2]}/${dateParts[1]}/${dateParts[0]}`;
    const formattedTime = `${timeParts[0]}:${timeParts[1]}`;

    return `${formattedDate} ${formattedTime}`;
  }

  formatDateTime(dateTimeStr: string) {
    const inputDate = new Date(dateTimeStr);
    const today = new Date();
    today.setHours(0, 0, 0, 0);

    if (inputDate >= today) {
      if (inputDate.getDate() === today.getDate()) {
        return `Hôm nay, lúc ${inputDate.toLocaleTimeString().substring(0, inputDate.toLocaleTimeString().length - 3)}`;
      } else if (inputDate.getDate() === today.getDate() - 1) {
        return `Hôm qua, lúc ${inputDate.toLocaleTimeString().substring(0, inputDate.toLocaleTimeString().length - 3)}`;
      } else {
        return this.formatDateTime2(dateTimeStr);
      }
    }
    return this.formatDateTime2(dateTimeStr);
  }

  protected readonly PROCESSING = 'PROCESSING';
  protected readonly PROCESSED = 'PROCESSED';
  protected readonly CANCEL = 'CANCEL';
  protected readonly ICON_DOUBLE_CHECK_MARK = ICON_DOUBLE_CHECK_MARK;
}
