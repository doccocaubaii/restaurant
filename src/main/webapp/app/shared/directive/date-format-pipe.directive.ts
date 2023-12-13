import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'dateFormatCustom',
})
export class DateFormatCustomPipe implements PipeTransform {
  transform(value: string): string {
    // Split the date string into day, month, and year
    const parts = value.split('/');

    // Format the date as "DD-MM-YYYY"
    const formattedDate = `${parts[0]}-${parts[1]}-${parts[2]}`;

    return formattedDate;
  }
}
