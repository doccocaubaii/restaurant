import { NgbDatepickerI18n, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Injectable } from '@angular/core';
import { TranslationWidth } from '@angular/common';

@Injectable()
export class NgbDatepickerI18nVi extends NgbDatepickerI18n {
  private readonly MONTHS_SHORT = [
    'Thg 1',
    'Thg 2',
    'Thg 3',
    'Thg 4',
    'Thg 5',
    'Thg 6',
    'Thg 7',
    'Thg 8',
    'Thg 9',
    'Thg 10',
    'Thg 11',
    'Thg 12',
  ];
  private readonly WEEKDAYS_SHORT = ['T2', 'T3', 'T4', 'T5', 'T6', 'T7', 'CN'];
  private readonly WEEKDAYS = ['Thứ 2', 'Thứ 3', 'Thứ 4', 'Thứ 5', 'Thứ 6', 'Thứ 7', 'Chủ Nhật'];

  getWeekdayShortName(weekday: number): string {
    return this.WEEKDAYS_SHORT[weekday - 1];
  }

  getWeekdayLabel(weekday: number): string {
    return this.WEEKDAYS_SHORT[weekday - 1];
  }

  getMonthShortName(month: number): string {
    return this.MONTHS_SHORT[month - 1];
  }

  getMonthFullName(month: number): string {
    return this.getMonthShortName(month);
  }

  getDayAriaLabel(date: NgbDateStruct): string {
    return `${date.day}/${date.month}/${date.year}`;
  }
}
