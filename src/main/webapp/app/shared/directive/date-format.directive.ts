import { Directive, ElementRef, Input, Renderer2 } from '@angular/core';
import { DatePipe } from '@angular/common';

@Directive({
  selector: '[appDateFormat]',
})
export class DateFormatDirective {
  constructor(private el: ElementRef, private renderer: Renderer2, private datePipe: DatePipe) {}

  ngOnChanges() {
    const formattedDate = this.datePipe.transform(this.el.nativeElement.value, 'DD-MM-YYYY');
    this.renderer.setProperty(this.el.nativeElement, 'value', formattedDate);
  }
}
