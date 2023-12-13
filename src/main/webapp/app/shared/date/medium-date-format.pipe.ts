import dayjs from 'dayjs/esm';
import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'mediumDateFormat',
})
export class MediumDateFormatPipe implements PipeTransform {
  transform(value: string): string {
    const originalDate = new Date(value);
    const hour = originalDate.getHours().toString().padStart(2, '0');
    const minute = originalDate.getMinutes().toString().padStart(2, '0');
    const day = originalDate.getDate().toString().padStart(2, '0');
    const month = (originalDate.getMonth() + 1).toString().padStart(2, '0'); // add 1 to get the month in the range 1-12
    const year = originalDate.getFullYear();

    const timeString = `${hour}:${minute}`;
    const dateString = `${day}/${month}/${year}`;

    return `${timeString} ${dateString}`;
  }
}
