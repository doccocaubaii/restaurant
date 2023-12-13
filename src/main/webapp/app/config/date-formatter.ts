import { NgbDateParserFormatter, NgbDateStruct } from '@ng-bootstrap/ng-bootstrap';
import { Injectable } from '@angular/core';
import dayjs from 'dayjs/esm';
import { DATE_FORMAT_DMY, DATE_FORMAT_DMY2 } from './input.constants';
function padNumber(value: any | null) {
  if (!isNaN(value) && value !== null) {
    return `0${value}`.slice(-2);
  } else {
    return '';
  }
}

@Injectable()
export class NgbDateCustomParserFormatter extends NgbDateParserFormatter {
  parse(value: string): NgbDateStruct | null {
    if (value) {
      const dateParts = value.trim().split('/');

      let dateObj: NgbDateStruct = { day: <any>null, month: <any>null, year: <any>null };
      const dateLabels = Object.keys(dateObj);

      dateParts.forEach((datePart, idx) => {
        dateObj[dateLabels[idx]] = parseInt(datePart, 10) || <any>null;
      });
      return dateObj;
    }
    return null;
  }

  format(date: NgbDateStruct | null): string {
    return date ? this.convertNgbDateToDayjs(date).format(DATE_FORMAT_DMY2).toString() : '';
  }
  convertNgbDateToDayjs(ngbDate: NgbDateStruct): any {
    const dateString = `${padNumber(ngbDate.day)}/${padNumber(ngbDate.month)}/${ngbDate.year || ''}`;
    return dayjs(dateString, 'DD/MM/YYYY');
  }
}
