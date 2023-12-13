import { Component, ElementRef, OnInit } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Router } from '@angular/router';

import { StateStorageService } from 'app/core/auth/state-storage.service';
import moment from 'moment';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { DatePipe } from '@angular/common';
import { EventManager } from '../../../core/util/event-manager.service';
// import {Renderer} from "@angular/compiler-cli/ngcc/src/rendering/renderer";
import { ParseLinks } from '../../../core/util/parse-links.service';
import { Principal } from '../../../core/auth/principal.service';
import { TypeGroupService } from './type-group.service';
import { ViewVoucherService } from './view-voucher.service';
import { ITEMS_PER_PAGE } from '../../../config/pagination.constants';
import { LoginService } from '../../../pages/login/login.service';
import { ROLE } from 'app/app.constants';
import { UtilsService } from '../../../utils/Utils.service';

@Component({
  selector: 'eb-ref-modal',
  templateUrl: './ref.component.html',
  styleUrls: ['./ref.component.css'],
})
export class EbRefModalComponent implements OnInit {
  data: any[];
  recorded: any;
  status: any;
  fromDate;
  any;
  toDate: any;
  typeGroups: any;
  typeGroup: any;
  typeSearch: number;
  viewVouchers: any[];
  viewVouchersAll: any[];
  no: any;
  invoiceNo: any;
  totalItems: number;
  page: number;
  itemsPerPage = ITEMS_PER_PAGE;
  links: any;
  queryCount: any;
  newList: any[];
  timeLineVoucher: any;
  listTimeLine: any[];
  objTimeLine: { dtBeginDate?: string; dtEndDate?: string };
  account: any;
  paramCheckAll: boolean;
  checkSave: any[];
  checkItems: number;

  constructor(
    private eventManager: EventManager,
    private loginService: LoginService,
    private stateStorageService: StateStorageService,
    private elementRef: ElementRef,
    // private renderer: Renderer,
    private router: Router,
    private activeModal: NgbActiveModal,
    private viewVoucherService: ViewVoucherService,
    private typeGroupService: TypeGroupService,
    private parseLinks: ParseLinks,
    public utilsService: UtilsService,
    private toastrService: ToastrService,
    private translateService: TranslateService,
    private principal: Principal,
    private datepipe: DatePipe
  ) {
    this.principal.identity().then(account => {
      this.account = account;
      this.typeSearch = 1;
      this.viewVouchers = [];
      this.listTimeLine = this.utilsService.getCbbTimeLine();
      this.timeLineVoucher = this.listTimeLine[4].value;
      this.selectChangeBeginDateAndEndDate(this.timeLineVoucher);
    });
  }

  ngOnInit(): void {
    // this.search();
    this.typeGroupService.queryForPopup().subscribe(res => {
      // @ts-ignore
      this.typeGroups = res.body.filter(a => a.id !== 51 && a.id !== 61);
    });

    this.page = 1;
    this.newList = [];
    this.checkItems = 0;
    if (this.data) {
      this.newList.push(...this.data);
    }
    this.totalItems = 0;
  }

  search(isSearch?: any) {
    if (isSearch) {
      this.paramCheckAll = false;
    }
    if ((!this.typeGroup && this.typeSearch === 1) || (!this.no && this.typeSearch === 2) || (!this.invoiceNo && this.typeSearch === 3)) {
      this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.typeGroup'));
      return;
    }
    if (!this.fromDate) {
      this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.fromDateNull'));
      return;
    }
    if (!this.toDate) {
      this.toastrService.error(this.translateService.instant('ebwebApp.muaHang.muaDichVu.toastr.toDateNull'));
      return;
    }
    if (this.fromDate > this.toDate) {
      this.toastrService.error(this.translateService.instant('ebwebApp.mCAudit.errorDate'));
      return;
    }
    this.viewVoucherService
      .query({
        page: this.page - 1,
        size: this.itemsPerPage,
        typeSearch: this.typeSearch ? this.typeSearch : '',
        status: this.status !== null && this.status !== undefined ? this.status : '',
        fromDate: this.fromDate ? this.fromDate : '',
        toDate: this.toDate ? this.toDate : '',
        typeGroup: this.typeGroup && this.typeGroup.id ? this.typeGroup.id : '',
        recorded: this.recorded !== null && this.recorded !== undefined ? this.recorded : '',
        no: this.no ? this.no : '',
        invoiceNo: this.invoiceNo ? this.invoiceNo : '',
        isThamChieu: true,
      })
      .subscribe(res => {
        this.viewVouchers = res.body;
        this.links = this.parseLinks.parse(res.headers.get('link'));
        this.totalItems = parseInt(res.headers.get('X-Total-Count'), 10);
        // this.checkCount = res.body.filter(x => this.newList != null && !this.newList.some(m => m.no === x.no))
        // this.totalItems = this.totalItems + this.checkCount.length;
        this.queryCount = this.totalItems;
        if (this.paramCheckAll) {
          this.viewVouchers.forEach(n => {
            n.checked = true;
          });
        } else {
          this.viewVouchers.forEach(n => {
            n.checked = this.newList.some(m => m.refID2 === n.refID2);
          });
        }
        this.viewVoucherService
          .queryAll({
            typeSearch: this.typeSearch ? this.typeSearch : '',
            status: this.status !== null && this.status !== undefined ? this.status : '',
            fromDate: this.fromDate ? this.fromDate : '',
            toDate: this.toDate ? this.toDate : '',
            typeGroup: this.typeGroup && this.typeGroup.id ? this.typeGroup.id : '',
            recorded: this.recorded !== null && this.recorded !== undefined ? this.recorded : '',
            no: this.no ? this.no : '',
            invoiceNo: this.invoiceNo ? this.invoiceNo : '',
          })
          .subscribe(res2 => {
            this.viewVouchersAll = res2.body;
            let index = [];
            this.newList.forEach(m => {
              if (this.viewVouchersAll.some(n => m.refID2 === n.refID2)) {
                // @ts-ignore
                index.push(m);
              }
            });
            this.newList = index;
          });
      });
    this.viewVouchers.forEach(n => {
      n.checked = this.newList.some(m => n.refID2 === m.refID2);
    });
  }

  apply() {
    let listVoucher = this.newList;
    listVoucher.forEach(a => {
      if (typeof a.date === 'string' && a.date.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/)) {
      } else if (a.date) {
        a.date = this.datepipe.transform(a.date, 'dd/MM/yyyy');
      }
      if (typeof a.postedDate === 'string' && a.postedDate.match(/^(\d{1,2})\/(\d{1,2})\/(\d{4})$/)) {
      } else if (a.postedDate) {
        a.postedDate = this.datepipe.transform(a.postedDate, 'dd/MM/yyyy');
      }
    });
    // @ts-ignore
    this.eventManager.broadcast({
      name: 'selectViewVoucher',
      content: listVoucher,
    });
    this.activeModal.dismiss(true);
  }

  close() {
    this.activeModal.dismiss(false);
    // @ts-ignore
    this.eventManager.broadcast({
      name: 'closeViewVoucher',
      content: false,
    });
  }

  getCurrentDate() {
    const _date = moment();
    return { year: _date.year(), month: _date.month() + 1, day: _date.date() };
  }

  isCheckAll() {}

  checkAll() {
    this.paramCheckAll = !this.paramCheckAll;
    if (this.paramCheckAll) {
      this.newList = [];
      this.newList.push(...this.viewVouchersAll);
      this.viewVouchers.forEach(n => {
        n.checked = true;
      });
    } else {
      this.newList = [];
      this.viewVouchers.forEach(n => {
        n.checked = false;
      });
    }
  }

  check(viewVoucher) {
    viewVoucher.checked = !viewVoucher.checked;
    if (this.paramCheckAll) {
      this.paramCheckAll = false;
      this.newList = this.newList.filter(m => viewVoucher.refID2 !== m.refID2);
    } else {
      if (viewVoucher.checked) {
        let index = [];
        // @ts-ignore
        index = this.viewVouchersAll.filter(m => m.refID2 === viewVoucher.refID2);
        // @ts-ignore
        if (!this.newList.some(n => n.refID2 === index[0].refID2)) {
          this.newList.push(index[0]);
        }
      } else {
        this.newList = this.newList.filter(n => n.refID2 !== viewVoucher.refID2);
      }
    }
  }

  // nếu là loại chứng từ thì chỉ cho chọn 1 giá trị có cùng type group, bỏ các giá trị khác
  changeTypeSearch(viewVoucher = null) {
    /* if (this.newList && this.newList.length) {
        if (viewVoucher && viewVoucher.typeGroupID !== this.newList[0].typeGroupID) {
            this.newList = [];
            this.viewVouchers.forEach(item => {
                item.checked = !(!viewVoucher || item.no !== viewVoucher.no);
            });
        }
    } */
  }

  selectChangeBeginDateAndEndDate(intTimeLine: String) {
    if (intTimeLine) {
      this.objTimeLine = this.utilsService.getTimeLine(intTimeLine, this.account);
      this.fromDate = moment(this.objTimeLine.dtBeginDate).format('YYYYMMDD');
      this.toDate = moment(this.objTimeLine.dtEndDate).format('YYYYMMDD');
    }
  }

  view(voucher, $event) {
    $event.stopPropagation();
    let url = '';
    switch (voucher.typeGroupID) {
      // Hàng bán trả lại
      case 33:
        if (this.hasAuthority(ROLE.ROLE_REPORT)) {
          url = `/#/thong-ke-loi-nhuan/${voucher.refID2}/edit/from-ref`;
        }
    }
    if (url) {
      window.open(url, '_blank');
    }
  }

  private hasAuthority(auth): boolean {
    if (this.account.authorities.includes(ROLE.ROLE_ADMIN) || this.account.authorities.includes(auth)) {
      return true;
    }
    this.toastrService.error(this.translateService.instant('ebwebApp.quyTrinh.notHasAccess'));
    return false;
  }

  newArr(length: number): any[] {
    if (length > 0) {
      return new Array(length);
    } else {
      return new Array(1);
    }
  }

  getNumberSelect() {
    if (this.paramCheckAll) {
      return this.totalItems;
    } else {
      return this.newList.length;
    }
  }
}
