import dayjs from 'dayjs/esm';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'shortDateFormat',
})
export class ShortDateFormatPipe implements PipeTransform {
  transform(value: string): string {
    const date = new Date(value);
    const day = date.getDate().toString().padStart(2, '0');
    const month = (date.getMonth() + 1).toString().padStart(2, '0');
    const year = date.getFullYear().toString();
    return `${day}/${month}/${year}`;
  }
}
