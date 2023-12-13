import { Component, OnInit } from '@angular/core';
import { ToastrService } from 'ngx-toastr';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TranslateService } from '@ngx-translate/core';
import { Location } from '@angular/common';
import { UtilsService } from '../../../../utils/Utils.service';
import { BaseComponent } from '../../../../shared/base/base.component';
import dayjs from 'dayjs/esm';
import { HOME } from '../../../../constants/app.routing.constants';
import { Router } from '@angular/router';
import { ICON_CANCEL, ICON_SAVE } from '../../../../shared/other/icon';

@Component({
  selector: 'jhi-modal-search-init',
  templateUrl: './modal-search-init.component.html',
  styleUrls: ['./modal-search-init.component.scss'],
})
export class SearchInitRevenueCommonComponent extends BaseComponent implements OnInit {
  fromDate: dayjs.Dayjs | any;
  toDate: dayjs.Dayjs | any;
  fromHour: any = '';
  toHour: any = '';
  type: any;
  period: any;
  checkUpdate: false;
  minDate: dayjs.Dayjs | any;
  checkHours: any;
  listType = [
    {
      id: 1,
      name: 'Thống kê theo tháng',
    },
    {
      id: 2,
      name: 'Thống kê theo ngày',
    },
    {
      id: 3,
      name: 'Thống kê theo giờ',
    },
  ];
  periods: any = [
    {
      id: 0,
      name: 'Chọn kỳ báo cáo',
    },
    {
      id: 1,
      name: 'Tháng 1',
    },
    {
      id: 2,
      name: 'Tháng 2',
    },
    {
      id: 3,
      name: 'Tháng 3',
    },
    {
      id: 4,
      name: 'Tháng 4',
    },
    {
      id: 5,
      name: 'Tháng 5',
    },
    {
      id: 6,
      name: 'Tháng 6',
    },
    {
      id: 7,
      name: 'Tháng 7',
    },
    {
      id: 8,
      name: 'Tháng 8',
    },
    {
      id: 9,
      name: 'Tháng 9',
    },
    {
      id: 10,
      name: 'Tháng 10',
    },
    {
      id: 11,
      name: 'Tháng 11',
    },
    {
      id: 12,
      name: 'Tháng 12',
    },
    {
      id: 13,
      name: 'Quý 1',
    },
    {
      id: 14,
      name: 'Quý 2',
    },
    {
      id: 15,
      name: 'Quý 3',
    },
    {
      id: 16,
      name: 'Quý 4',
    },
    {
      id: 17,
      name: 'Năm nay',
    },
    {
      id: 18,
      name: 'Năm trước',
    },
  ];
  constructor(
    private toastr: ToastrService,
    public activeModal: NgbActiveModal,
    private translateService: TranslateService,
    private location: Location,
    protected utilsService: UtilsService,
    private router: Router
  ) {
    super();
    this.location.subscribe(() => {
      this.activeModal.dismiss();
    });
  }

  async ngOnInit() {
    const currentMonth = dayjs().month() + 1;
    const currentQuarter = Math.ceil(currentMonth / 3);
    this.minDate = dayjs().subtract(4, 'years');
    if (!this.checkUpdate) {
      this.period = currentMonth;
      this.updateDates(this.period);
      this.type = 1;
      this.checkHours = 1;
      this.fromHour = dayjs().startOf('day').format('HH:mm'); // Gán giờ bắt đầu của ngày dưới dạng chuỗi thời gian
      this.toHour = dayjs().endOf('day').format('HH:mm'); // Gán giờ kết thúc của ngày dưới dạng chuỗi thời gian
    }
    this.periods = this.periods.map(p =>
      (p.id <= 12 && p.id > currentMonth) || (p.id <= 16 && p.id > 12 && p.id - 12 > currentQuarter) ? { ...p, disabled: true } : p
    );
    JSON.parse(JSON.stringify(this.periods));
  }
  updateDates(event) {
    if (event >= 1 && event <= 12) {
      // It's a month
      if (this.period == dayjs().month() + 1) {
        this.fromDate = dayjs().startOf('month');
        this.toDate = dayjs();
      } else {
        this.fromDate = dayjs()
          .month(event - 1)
          .startOf('month');
        this.toDate = dayjs()
          .month(event - 1)
          .endOf('month');
      }
    } else if (event > 12 && event <= 16) {
      // Đây là quý
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
    let req: any = {
      fromDate: this.fromDate,
      toDate: this.toDate,
      type: this.type,
      period: this.period,
      checkHours: this.checkHours,
      fromHour: null,
      toHour: null,
    };
    if (this.checkHours === 2) {
      req.fromHour = this.fromHour;
      req.toHour = this.toHour;
    }
    this.activeModal.close(req);
  }
  onBack($event) {
    if (this.checkUpdate) {
      this.activeModal.dismiss();
    } else {
      this.router.navigate([`/` + HOME]);
      this.activeModal.close();
    }
  }

  changeFromDate(event) {
    const form = 'DD/MM/YYYY';
    if (event.length === form.length) {
      this.fromDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.fromDate)) {
      this.changeDate();
    }
  }
  changeToDate(event) {
    const form = 'DD/MM/YYYY';
    if (event.length === form.length) {
      this.toDate = this.convertStringToDate(event);
      if (this.fromDate.isAfter(this.toDate) || this.fromDate.isSame(this.toDate)) {
        this.fromDate = this.toDate;
      }
    }
    if (dayjs.isDayjs(this.toDate)) {
      this.changeDate();
    }
  }

  changeDate() {
    if (!this.fromDate || !this.toDate) return;
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

    if (
      fromMonth === (fromQuarter - 1) * 3 + 1 &&
      toMonth === fromQuarter * 3 &&
      this.fromDate.date() === 1 &&
      this.toDate.date() === dayjs(this.toDate).endOf('month').date()
    ) {
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
  onRadioChange(value: number) {
    if (value === 1) {
      // Nếu checkHours bằng 1
      this.fromHour = dayjs().startOf('day').format('HH:mm'); // Gán giờ bắt đầu của ngày dưới dạng chuỗi thời gian
      this.toHour = dayjs().endOf('day').format('HH:mm'); // Gán giờ kết thúc của ngày dưới dạng chuỗi thời gian
    } else if (value === 2) {
      // Nếu checkHours bằng 2
      // Thực hiện các xử lý khác nếu cần thiết
    }
  }
  checkHoursValidity(checkHours: any) {
    const startOfDay = dayjs().startOf('day').format('HH:mm');
    const endOfDay = dayjs().endOf('day').format('HH:mm');
    this.checkHours = checkHours;
    if (checkHours == 1) {
      this.fromHour = startOfDay;
      this.toHour = endOfDay;
    }
  }

  protected readonly ICON_CANCEL = ICON_CANCEL;
  protected readonly ICON_SAVE = ICON_SAVE;
}
