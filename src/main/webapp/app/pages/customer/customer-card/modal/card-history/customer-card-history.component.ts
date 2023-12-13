import { Component, Input, OnInit } from '@angular/core';
import { CustomerService } from '../../../customer.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ConvertResponse } from '../../../../../config/convert-response';
import { UtilsService } from '../../../../../utils/Utils.service';
import { formatDate } from '@angular/common';
import { CustomerBillHistory, CustomerCardHistory, ReceivableBillHistory } from '../../../../../entities/customer/customer-history';
import { STATUS_MAP } from '../../../../../constants/invoice.constants';

@Component({
  selector: 'jhi-customer-card-history',
  templateUrl: './customer-card-history.component.html',
  styleUrls: ['./customer-card-history.component.scss'],
})
export class CustomerCardHistoryComponent implements OnInit {
  @Input() data: any;
  title: string = 'Chi tiết lịch sử tích điểm';

  constructor(private customerService: CustomerService, private activeModal: NgbActiveModal, protected utilsService: UtilsService) {}

  ngOnInit(): void {
    switch (this.data.type) {
      case 2:
        this.title = 'Chi tiết lịch sử chi tiêu';
        this.getAllCardHistory();
        break;
      case 3:
        this.title = 'Chi tiết nợ phải thu';
        this.getAllBillHistory();
        break;
      case 4:
        this.title = 'Chi tiết bán hàng';
        this.getAllBillHistory();
        break;
    }
  }

  dismiss($event: MouseEvent) {
    this.activeModal.dismiss();
  }

  customerCardHistory: CustomerCardHistory[] = [];
  customerBillHistory: CustomerBillHistory[] = [];
  receivableBillHistory: ReceivableBillHistory[] = [];
  // countCustomerCardHistory = 0;
  page: number = 1;
  pageSize: number = 20;
  totalSize: number = 0;
  sizes = [
    {
      id: 10,
      name: '10',
    },
    {
      id: 20,
      name: '20',
    },
    {
      id: 30,
      name: '30',
    },
  ];
  fromDate: any;
  toDate: any;
  getAllCardHistory() {
    const request = {
      comId: this.data.comId,
      customerId: this.data.customerId,
      getWithPaging: true,
      type: this.data.type,
      page: this.page - 1,
      size: 20,
      fromDate: this.fromDate ? this.utilsService.convertDate(this.fromDate).toString().slice(0, 19).replace('T', ' ') : null,
      toDate: this.toDate ? this.utilsService.convertDate(this.toDate).toString().slice(0, 19).replace('T', ' ') : null,
    };

    this.customerService.getAllCustomerCardHistory(request).subscribe(
      response => {
        if (response.ok) {
          const responseConvert = ConvertResponse.getDataFromServer(response, false);
          this.customerCardHistory = responseConvert.data;
          // this.countCustomerCardHistory = responseConvert.count;
          this.totalSize = responseConvert.count;
        }
      },
      error => {
        this.customerCardHistory = [];
        this.totalSize = 0;
      }
    );
  }

  getAllBillHistory() {
    const type = this.data.type === 3 ? 1 : 2;
    const request = {
      comId: this.data.comId,
      customerId: this.data.customerId,
      type: type,
      page: this.page - 1,
      size: 20,
      fromDate: this.fromDate ? this.utilsService.convertDate(this.fromDate).toString().slice(0, 19).replace('T', ' ') : null,
      toDate: this.toDate ? this.utilsService.convertDate(this.toDate).toString().slice(0, 19).replace('T', ' ') : null,
    };

    this.customerService.getAllCustomerBillHistory(request).subscribe(
      response => {
        if (response.ok) {
          const responseConvert = ConvertResponse.getDataFromServer(response, false);
          this.totalSize = responseConvert.count;
          if (type === 1) {
            this.receivableBillHistory = responseConvert.data;
          } else {
            this.customerBillHistory = responseConvert.data;
          }
        }
      },
      error => {
        this.customerBillHistory = [];
        this.receivableBillHistory = [];
        this.totalSize = 0;
      }
    );
  }

  loadMore() {
    if (this.data.type < 3) {
      this.getAllCardHistory();
    } else {
      this.getAllBillHistory();
    }
  }

  compactDate(date, pattern) {
    if (date) {
      const myDate = new Date(date);
      return formatDate(myDate, pattern, 'en-US');
    }
    return date;
  }

  isValidData = true;
  checkValidData() {
    switch (this.data.type) {
      case 2: {
        if (!this.customerCardHistory || this.customerCardHistory.length == 0) {
          this.isValidData = false;
        }
        break;
      }
      case 3:
      case 4: {
        if (!this.customerBillHistory || this.customerBillHistory.length == 0) {
          this.isValidData = false;
        }
        break;
      }
    }
  }

  protected readonly STATUS_MAP = STATUS_MAP;
}
