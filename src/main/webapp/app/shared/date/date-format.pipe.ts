import dayjs from 'dayjs/esm';
import { Pipe, PipeTransform } from '@angular/core';
import { DATE_NEW_FORMAT, DATE_TIME_FORMAT } from 'app/config/input.constants';

@Pipe({
  name: 'dateFormat',
})
export class DateFormatPipe implements PipeTransform {
  transform(value: dayjs.Dayjs, format = 'dd-MM-yyyy'): string {
    return value.format(DATE_NEW_FORMAT);
  }
}
