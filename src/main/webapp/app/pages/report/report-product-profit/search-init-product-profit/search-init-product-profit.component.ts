import {Component, OnDestroy, OnInit} from '@angular/core';
import dayjs from "dayjs/esm";
import {ToastrService} from "ngx-toastr";
import {NgbActiveModal} from "@ng-bootstrap/ng-bootstrap";
import {TranslateService} from "@ngx-translate/core";
import {Location} from "@angular/common";
import {UtilsService} from "../../../../utils/Utils.service";
import {HOME} from "../../../../constants/app.routing.constants";
import {Router} from "@angular/router";
import {ProductService} from "../../../product/product.service";
import {FilterProduct, Page} from "../../../product/product";
import {distinctUntilChanged, Subject, switchMap, tap, Observable} from "rxjs";
import {WarehouseService} from "../../../warehouse/warehouse.service";
import { debounceTime, takeUntil } from 'rxjs/operators';
import { ICON_CANCEL, ICON_SAVE, ICON_SEARCH_SM } from '../../../../shared/other/icon';

@Component({
  selector: 'jhi-search-init-product-profit',
  templateUrl: './search-init-product-profit.component.html',
  styleUrls: ['./search-init-product-profit.component.scss']
})
export class SearchInitProductProfitComponent implements OnInit {

  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  period : any;
  checkUpdate = false;
  minDate: dayjs.Dayjs | any;
  totalItems : any;
  listSelected: any = [];
  paramCheckAll: boolean = false;
  paramCheckAllPage: any;
  filterProduct: FilterProduct = { page: Page.PAGE_NUMBER, size: Page.PAGE_SIZE, isCountAll: true, keywordName: '', keywordUnit: '', isDefault: false };
  lstProduct: any;
  selectedItem: any = {};
  sortType: any = 4;
  sortTypes: any = [
    {
      id: 1,
      name: 'Số lượng tăng dần',
    },
    {
      id: 2,
      name: 'Số lượng giảm dần',
    },
    {
      id: 3,
      name: 'Lợi nhuận tăng dần',
    },
    {
      id: 4,
      name: 'Lợi nhuận giảm dần',
    },
    {
      id: 5,
      name: 'Doanh thu tăng dần',
    },
    {
      id: 6,
      name: 'Doanh thu giảm dần',
    }
  ];
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
  periods : any = [
    {
      id: 0,
      name: "Chọn kỳ báo cáo"
    },
    {
      id: 1,
      name: "Tháng 1"
    },
    {
      id: 2,
      name: "Tháng 2"
    },
    {
      id: 3,
      name: "Tháng 3"
    },
    {
      id: 4,
      name: "Tháng 4"
    },
    {
      id: 5,
      name: "Tháng 5"
    },
    {
      id: 6,
      name: "Tháng 6"
    },
    {
      id: 7,
      name: "Tháng 7"
    },
    {
      id: 8,
      name: "Tháng 8"
    },
    {
      id: 9,
      name: "Tháng 9"
    },
    {
      id: 10,
      name: "Tháng 10"
    },
    {
      id: 11,
      name: "Tháng 11"
    },
    {
      id: 12,
      name: "Tháng 12"
    },
    {
      id: 13,
      name: "Quý 1"
    },
    {
      id: 14,
      name: "Quý 2"
    },
    {
      id: 15,
      name: "Quý 3"
    },
    {
      id: 16,
      name: "Quý 4"
    },
    {
      id: 17,
      name: 'Năm nay',
    },
    {
      id: 18,
      name: 'Năm trước',
    }
  ];
  constructor(
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location,
    protected utilsService: UtilsService,
    private router: Router,
    private warehouseService: WarehouseService,
  ) {
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  ngOnInit(): void {
    const currentMonth = dayjs().month() + 1;
    const currentQuarter = Math.ceil(currentMonth / 3);
    this.minDate = dayjs().subtract(4, 'years');
    this.minDate = dayjs().subtract(4, 'years');
    if (!this.checkUpdate) {
      this.period = currentMonth;
      this.updateDates(this.period);
    }
    this.periods = this.periods.map((p) =>
        (p.id <= 12 && p.id > currentMonth) || (p.id <= 16 && p.id > 12 && p.id - 12 > currentQuarter) ? { ...p, disabled: true } : p
    );
    JSON.parse(JSON.stringify(this.periods));
    this.onSearch();
  }

  updateDates(event) {
    if (event >= 1 && event <= 12) { // It's a month
      if (this.period == dayjs().month() + 1) {
        this.fromDate = dayjs().startOf('month');
        this.toDate = dayjs();
      } else {
        this.fromDate = dayjs().month(event - 1).startOf('month');
        this.toDate = dayjs().month(event - 1).endOf('month');
      }
    } else if (event > 12 && event <= 16) { // Đây là quý
      const quarterStartMonth = (event - 13) * 3;
      this.fromDate = dayjs(new Date(new Date().getFullYear(), quarterStartMonth, 1));
      const quarterEndMonth = quarterStartMonth + 2;
      this.toDate = dayjs(new Date(new Date().getFullYear(), quarterEndMonth + 1, 0));
      if (new Date() < this.toDate) {
        this.toDate = dayjs();
      }
    } else if (event === 17) {
      // It's current year
      this.fromDate = dayjs().startOf('year');
      this.toDate = dayjs();
    } else if (event === 18) {
      // It's previous year
      this.fromDate = dayjs().subtract(1, 'year').startOf('year');
      this.toDate = dayjs().subtract(1, 'year').endOf('year');
    }
  }

  onSave() {
    if (!this.fromDate) {
      this.toastr.error(this.translateService.instant('global.messages.validate.dateTime.dateInvalid'), this.translateService.instant('global.info.notify'));
    } else if (!this.toDate) {
      this.toastr.error(this.translateService.instant('global.messages.validate.dateTime.dateInvalid'), this.translateService.instant('global.info.notify'));
    } else if (this.listSelected.length == 0 && !this.paramCheckAll) {
      this.toastr.error(this.translateService.instant('easyPos.reportProductProfit.error.productError'), this.translateService.instant('global.info.notify'));
    } else {
      let req: any = {
        fromDate: this.fromDate,
        toDate: this.toDate,
        period: this.period,
        paramCheckAll: this.paramCheckAll,
        paramCheckAllPage: this.paramCheckAllPage,
        listSelected: this.listSelected,
        sortType: this.sortType,
        keywordName: this.filterProduct.keywordName,
        keywordUnit: this.filterProduct.keywordUnit,
      };
      this.onClose(req);
    }
  }
  dismiss(even: any) {
    this.activeModal.dismiss();
  }

  onClose(even: any) {
    this.activeModal.close(even);
  }

  changeFromDate(event) {
    const form = 'DD/MM/YYYY'
    if (event.length === form.length) {
      this.fromDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.fromDate)){
      this.changeDate();
    }
  }
  changeToDate(event) {
    const form = 'DD/MM/YYYY'
    if (event.length === form.length) {
      this.toDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.toDate)){
      this.changeDate();
    }
  }

  changeDate() {
    if(!this.fromDate || !this.toDate) return;
    const fromMonth = this.fromDate.month() + 1;
    const toMonth = this.toDate.month() + 1;
    const fromQuarter = Math.ceil(fromMonth / 3);
    const toQuarter = Math.ceil(toMonth / 3);
    const fromYear = this.fromDate.year();
    const toYear = this.toDate.year();

    // Nếu fromDate và toDate không cùng tháng
    if (fromMonth !== toMonth || fromYear !== toYear) {
      this.period = 0;
    }

    if (fromMonth === (fromQuarter - 1) * 3 + 1 && toMonth === fromQuarter * 3
      && this.fromDate.date() === 1
      && this.toDate.date() === dayjs(this.toDate).endOf('month').date()) {
      this.period = fromQuarter + 12;
    }
    // Nếu fromDate và toDate cùng tháng thì gán period bằng tháng đó
    if (fromMonth == toMonth && fromYear == toYear) {
      this.period = fromMonth;
    }
    return;
  }
  convertStringToDate(str: string): dayjs.Dayjs {
    const date = dayjs(str, 'DD/MM/YYYY');
    return date;
  }

  onSearch() {
    this.filterProduct.page = 1;
    this.getLstProduct();
  }

  getLstProduct() {
    let req = Object.assign({}, this.filterProduct);
    req.page = this.filterProduct.page - 1;
    req.isDefault = false;
    this.warehouseService.getProductWithPaging(req).subscribe(value => {
      if (this.paramCheckAll && this.totalItems < value.body.count) {
        this.lstProduct.forEach(prod => {
          this.listSelected.push(prod.productProductUnitId);
          this.paramCheckAll = false;
        });
      }
      this.formatDataTable(value);
    });
  }

  formatDataTable(value: any) {
    this.lstProduct = value.body.data.filter(item => !['SP1', 'SPGC', 'SPKM', 'SPDV', 'SPCK'].includes(item.productCode));
    this.totalItems = value.body.count;
    if (this.lstProduct.length === 0) {
      this.paramCheckAll = false;
      this.paramCheckAllPage = false;
    } else {
      if (this.paramCheckAll) {
        this.lstProduct.forEach(n => {
          n.check = true;
        });
      } else {
        if (this.listSelected?.length > 0) {
          this.paramCheckAllPage = true;
          this.lstProduct.forEach(n => {
            if (!this.listSelected.includes(n.productProductUnitId)) {
              this.paramCheckAllPage = false;
            }
            n.check = this.listSelected.includes(n.productProductUnitId);
          });
        }
      }
    }
  }

  loadMore($event: any) {
    this.filterProduct.page = $event;
    this.getLstProduct();
  }
  getItem(product: any) {
    this.selectedItem = product;
  }

  check(object: any, listSelected: any, paramCheckAll: boolean, selectedItem: any) {
    object.check = !object.check;
    if (object.check) {
      selectedItem = object;
    }
    if (paramCheckAll) {
      // this.isShowRecord = false;
      if (!object.check) {
        listSelected.push(object.productProductUnitId);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] === object.productProductUnitId) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
    } else {
      if (object.check) {
        listSelected.push(object.productProductUnitId);
      } else {
        for (let i = 0; i < listSelected.length; i++) {
          if (listSelected[i] === object.productProductUnitId) {
            listSelected.splice(i, 1);
            i--;
          }
        }
      }
      // this.isShowRecord = listSelected.length <= 1;
    }
  }

  checkAllForPage(objectList: any[], listSelected: any, paramCheckAllPage: boolean) {
    if (paramCheckAllPage) {
      objectList.forEach(n => {
        n.check = true;
        if (!listSelected.includes(n.productProductUnitId)) {
          listSelected.push(n.productProductUnitId);
        }
      });
    } else {
      objectList.forEach(n => {
        n.check = false;
        const index: any = listSelected.indexOf(n.productProductUnitId);
        if (index !== -1) {
          listSelected.splice(index, 1);
        }
      });
    }
  }

  protected readonly ICON_SAVE = ICON_SAVE;
  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SEARCH_SM = ICON_SEARCH_SM;
}
