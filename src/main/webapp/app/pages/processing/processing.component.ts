import { AfterViewChecked, Component, ElementRef, OnDestroy, OnInit, Renderer2 } from '@angular/core';
import { HttpHeaders } from '@angular/common/http';
import { TOTAL_COUNT_RESPONSE_HEADER } from 'app/config/pagination.constants';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import dayjs from 'dayjs/esm';
import { AfterViewInit } from '@angular/core';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import { lastValueFrom, Subscription } from 'rxjs';
import { BaseComponent } from 'app/shared/base/base.component';
import { ConfirmDialogComponent } from 'app/shared/modal/confirm-dialog/confirm-dialog.component';
import { ModalBtn, ModalContent, ModalHeader } from 'app/constants/modal.const';
import { UtilsService } from 'app/utils/Utils.service';
import { BILL_OFFLINE, BILL_ORDER } from 'app/constants/app.routing.constants';
import { formatDate } from '@angular/common';
import { last_company } from '../../object-stores.constants';
import appSettings from '../../config/app-settings';
import { ContentOption } from '../../utils/contentOption';
import { ProcessingService } from './processing.service';
import { EventManager, EventWithContent } from '../../core/util/event-manager.service';
import { NotificationService } from './notification.service';
import { TranslateService } from '@ngx-translate/core';
import { MqttService } from 'ngx-mqtt';
import { SocketMessage, Md5Service } from '../../constants/socket.message.constant';
import { ISocketConfigModel } from '../../entities/socket/socket-config.model';
import { CheckDeactivate } from '../order/customer-order/check-deactivate';
import { checkConditionNotify } from '../const/function';
import { InvoiceConfiguration } from '../order/model/bill-payment.model';
import { InvoiceService } from '../invoice/service/invoice.service';
import { WebsocketService } from '../../config/service/websocket-service.service';
import { DataEncrypt } from '../../entities/indexDatabase/data-encrypt.model';
import { Authority } from '../../config/authority.constants';
import {
    ICON_ARROW_SINGLE, ICON_ARROW_SINGLE_BLUE, ICON_BILL_ORDER,
    ICON_DELETE_RED,
    ICON_DOUBLE_ARROW, ICON_GO_BACK, ICON_PROCESSING_DONE,
    ICON_PROCESSING_LG, ICON_SHOW_ACTION,
    ICON_SHOW_NOTICE, ICON_WAIT_PROCESSING
} from "../../shared/other/icon";

@Component({
  selector: 'processing',
  templateUrl: './processing.component.html',
  styleUrls: ['./processing.component.scss'],
})
export class ProcessingComponent extends BaseComponent implements OnInit, OnDestroy {
  appSettings = appSettings;
  waitingPage: any = 1;
  donePage: any = 1;
  pageSize: number = 20;
  totalWaitingSize: any;
  totalDoneSize: any;
  type = 1;
  showNoti = false;
  showAction = false;
  lastCompany: any;
  waitingNotification = true;
  processNotification = true;
  supplyNotification = true;
  waitingList: any;
  doneList: any;
  notificationList: any;
  cleanHttpErrorListener: Subscription;
  isLoading = false;
  statusNotifyOrder = false;
  invoiceConfiguration: InvoiceConfiguration;
  socketMessage: SocketMessage = {
    action: '',
    reason: '',
    data: null,
  };
  private eventSubscription: Subscription;
  autoCallApi: any;
  authorWAITING = Authority.KITCHEN_CHANGE_WAITING;
  authorDONE = Authority.KITCHEN_CHANGE_DONE;
  authorBILL_ADD = Authority.BILL_ADD;

  httpErrorListener: Subscription;
  constructor(
    protected modalService: NgbModal,
    protected utilsService: UtilsService,
    protected service: ProcessingService,
    protected notificationService: NotificationService,
    private translateService: TranslateService,
    private elementRef: ElementRef,
    private route: Router,
    private toast: ToastrService,
    private contentOption: ContentOption,
    protected activatedRoute: ActivatedRoute,
    private eventManager: EventManager,
    private mqttService: MqttService,
    private md5Service: Md5Service,
    private renderer: Renderer2,
    private invoiceService: InvoiceService
  ) {
    super();
    this.contentOption.isHiddenOrder = true;
  }

  ngOnDestroy() {
    this.eventManager.destroy(this.cleanHttpErrorListener);
    this.mqttService.disconnect();
  }

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    const invoiceConfiguration = await lastValueFrom(this.invoiceService.getCompanyConfig(this.lastCompany.id));
    this.invoiceConfiguration = invoiceConfiguration.data;
    this.invoiceConfiguration.displayConfig = JSON.parse(this.invoiceConfiguration.displayConfig);
    if (this.lastCompany.waitingNotification != undefined) {
      this.waitingNotification = this.lastCompany.waitingNotification;
    }
    if (this.lastCompany.processNotification != undefined) {
      this.processNotification = this.lastCompany.processNotification;
    }
    if (this.lastCompany.supplyNotification != undefined) {
      this.supplyNotification = this.lastCompany.supplyNotification;
    }
    await this.getWaitingList();
    await this.getDoneList();
    await this.getListNotification();

    if (this.checkConditionNotify()) {
      this.mqttService.connect();
    }

    const notificationDiv = this.elementRef.nativeElement.querySelector('#notification-overlay-container');
    const actionDiv = this.elementRef.nativeElement.querySelector('#action-overlay-container');
    const actionIcon = this.elementRef.nativeElement.querySelector('#action-icon');
    const notificationIcon = this.elementRef.nativeElement.querySelector('#notification-icon');

    this.renderer.listen('document', 'click', event => {
      if (actionDiv && actionIcon && !actionDiv.contains(event.target) && !actionIcon.contains(event.target) && this.showAction) {
        this.showAction = false;
      }
      if (actionDiv && actionIcon && !notificationDiv.contains(event.target) && !notificationIcon.contains(event.target) && this.showNoti) {
        this.showNoti = false;
      }
    });
    let topicKitchen = this.md5Service.convertToMd5(this.lastCompany.id + '/kitchen');
    this.mqttService.observe(topicKitchen).subscribe(res => {
      this.getWaitingList();
      this.getDoneList();
      this.getListNotification();
    });
    let topicOrder = this.md5Service.convertToMd5(this.lastCompany.id + '/order');
    this.mqttService.observe(topicOrder).subscribe(res => {
      this.getWaitingList();
      this.getDoneList();
    });
  }

  checkConditionNotify() {
    this.statusNotifyOrder = checkConditionNotify(this.invoiceConfiguration);
    return this.statusNotifyOrder;
  }

  async getWaitingList() {
    let waitingReq = {
      type: this.type,
      status: 0,
      page: this.waitingPage - 1,
    };
    this.service.getForProcessing(waitingReq).subscribe(res => {
      this.waitingList = res.body.data;
      this.totalWaitingSize = res.body.count;
    });
  }

  async getDoneList() {
    let doneReq = {
      type: 1,
      status: 1,
      page: this.donePage - 1,
    };
    this.service.getForProcessing(doneReq).subscribe(res => {
      this.doneList = res.body.data;
      this.totalDoneSize = res.body.count;
    });
  }

  async getListNotification() {
    let req = {
      page: 0,
      size: 20,
      isUnRead: true,
      type: 1,
    };
    this.notificationService.getUnReadNotification(req).subscribe(res => {
      this.notificationList = res.body.data;
      if (this.notificationList && this.notificationList.length > 0) {
        for (const element of this.notificationList) {
          this.toast.success(element.content, this.translateService.instant('easyPos.product.info.message'));
          // Settimeout đêr bắn về casc client khac
          setTimeout(() => {
            this.updateNotify(element.id);
          }, 100);
        }
      }
    });
  }
  updateNotify(id: any) {
    this.notificationService
      .updateNotification({
        id: id,
        readAll: false,
      })
      .subscribe(res => {});
  }
  formatTopping(data: any) {
    let result = '';
    for (const element of data.toppings) {
      result += ' + ' + element.quantity + ' ' + element.productName + ', ';
    }
    return result.substring(0, result.lastIndexOf(', '));
  }

  async changeNotification(type: any) {
    if (type == 0) {
      this.waitingNotification = !this.waitingNotification;
      this.lastCompany.waitingNotification = this.waitingNotification;
    } else if (type == 1) {
      this.processNotification = !this.processNotification;
      this.lastCompany.processNotification = this.processNotification;
    } else if (type == 2) {
      this.supplyNotification = !this.supplyNotification;
      this.lastCompany.supplyNotification = this.supplyNotification;
    }
    await this.updateById(last_company, this.lastCompany.id, new DataEncrypt(this.lastCompany.id, this.encryptFromData(this.lastCompany)));
  }

  async changeType(type: any) {
    this.type = type;
    this.waitingPage = 1;
    await this.getWaitingList();
  }

  changeShowNoti() {
    this.showNoti = !this.showNoti;
    if (this.showAction) {
      this.showAction = !this.showAction;
    }
  }
  changeShowAction() {
    this.showAction = !this.showAction;
    if (this.showNoti) {
      this.showNoti = !this.showNoti;
    }
  }

  back() {
    history.back();
  }

  async changeSingleDish(id: any, status: any, billId: any, data: any) {
    this.isLoading = true;
    let haveTopping = data.toppings != null && data.toppings.length > 0;
    let req = {
      quantity: 1.0,
      status: status, // 0- cho che bien, 1 - cho cung ung
      type: this.type, // Uu tien, theo ban, theo mon
      billId: billId,
      id: id,
      haveTopping: haveTopping,
      productProductUnitId: data.productProductUnitId,
    };

    if (status == 0) {
      if (data.processingQuantity < 1) {
        req.quantity = data.processingQuantity;
      }
    } else if (status == 1) {
      if (data.processedQuantity < 1) {
        req.quantity = data.processedQuantity;
      }
    }

    this.service.changeProcessStatus(req).subscribe(
      res => {
        if (res.status) {
          this.getWaitingList();
          this.getDoneList();
        }
        this.isLoading = false;
        // let topicOrder = this.md5Service.convertToMd5(this.lastCompany.id + '/order');
        // let data: ISocketConfigModel = new ISocketConfigModel();
        // data.refId = billId;
        // this.socketMessage.action = 'ORDER';
        // this.socketMessage.reason = 'Chuyen trang thai mon an';
        // this.socketMessage.data = data;
        // this.mqttService.publish(topicOrder, JSON.stringify(this.socketMessage)).subscribe(
        //   () => {},
        //   error => {}
        // );
      },
      error => (this.isLoading = false)
    );
  }

  makeBoxHeightsEqual() {
    const box1 = document.getElementById('box1');
    const box2 = document.getElementById('box2');

    if (box1 && box2) {
      const maxHeight = Math.max(box1.clientHeight, box2.clientHeight);
      box1.style.height = `${maxHeight}px`;
      box2.style.height = `${maxHeight}px`;
    }
  }

  formatDateTime(input: any) {
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

  checkShowButton(data: any, status: any) {
    let show = false;
    if (data) {
      if (data.processingQuantity == 0 && data.deletes != null && data.deletes.length > 0 && status == 0) {
        show = true;
      } else if (data.processedQuantity == 0 && data.deletes != null && data.deletes.length > 0 && status == 1) {
        show = true;
      }
    }
    return show;
  }

  async deleteDish(data: any) {
    this.isLoading = true;
    let req = {
      billId: data.billId,
      id: data.id,
    };

    this.service.deleteDish(req).subscribe(
      res => {
        if (res.status) {
          this.getWaitingList();
          this.getDoneList();
        }
        this.isLoading = false;
      },
      error => (this.isLoading = false)
    );
  }

  async changeAllDish(id: any, status: any, billId: any, data: any, quantity: any, doneWithTable?: any) {
    this.isLoading = true;
    let haveTopping = data.toppings != null && data.toppings.length > 0;
    let req = {
      quantity: quantity,
      status: status, // 0- cho che bien, 1 - cho cung ung
      type: this.type, // Uu tien, theo ban, theo mon
      billId: billId,
      id: id,
      doneWithTable: doneWithTable,
      haveTopping: haveTopping,
      productProductUnitId: data.productProductUnitId,
    };

    this.service.changeProcessStatus(req).subscribe(
      res => {
        if (res.status) {
          this.getWaitingList();
          this.getDoneList();
        }
        this.isLoading = false;
        // let topicOrder = this.md5Service.convertToMd5(this.lastCompany.id + '/order');
        // let data: ISocketConfigModel = new ISocketConfigModel();
        // data.refId = billId;
        // this.socketMessage.action = 'ORDER';
        // this.socketMessage.reason = 'Chuyen trang thai mon an';
        // this.socketMessage.data = data;
        // this.mqttService.publish(topicOrder, JSON.stringify(this.socketMessage)).subscribe(
        //   () => {},
        //   error => {}
        // );
      },
      error => (this.isLoading = false)
    );
  }

  protected handleNavigation(page: number, size: number, status?: number): void {
    const queryParamsObj = {
      page,
      size,
      status,
    };
    this.route.navigate(['./'], {
      relativeTo: this.activatedRoute,
      queryParams: queryParamsObj,
    });
  }

  getLink() {
    if (this.utilsService.isOnline()) {
      this.route.navigate([BILL_ORDER]);
    }
  }

    protected readonly ICON_ARROW_SINGLE = ICON_ARROW_SINGLE;
  protected readonly ICON_DOUBLE_ARROW = ICON_DOUBLE_ARROW;
  protected readonly ICON_DELETE_RED = ICON_DELETE_RED;
  protected readonly ICON_PROCESSING_LG = ICON_PROCESSING_LG;
  protected readonly ICON_SHOW_NOTICE = ICON_SHOW_NOTICE;
  protected readonly ICON_SHOW_ACTION = ICON_SHOW_ACTION;
  protected readonly ICON_PROCESSING_DONE = ICON_PROCESSING_DONE;
  protected readonly ICON_WAIT_PROCESSING = ICON_WAIT_PROCESSING;
    protected readonly ICON_BILL_ORDER = ICON_BILL_ORDER;
  protected readonly ICON_GO_BACK = ICON_GO_BACK;
    protected readonly ICON_ARROW_SINGLE_BLUE = ICON_ARROW_SINGLE_BLUE;
}
