import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'numberFormat',
})
export class NumberFormatPipe implements PipeTransform {
  transform(value: unknown) {
    if (value) {
      if (typeof value === 'number') {
        return value.toLocaleString('de-DE');
      }
    } else {
      return value;
    }
  }
}
