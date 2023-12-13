import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberFormatString',
})
export class NumberFormatPipe implements PipeTransform {
  transform(value: number) {
    if (value && value > 0) {
      return value.toLocaleString('en-US', {
        useGrouping: true,
        minimumFractionDigits: 0,
        maximumFractionDigits: 6,
      });
    }
    return;
  }
}
