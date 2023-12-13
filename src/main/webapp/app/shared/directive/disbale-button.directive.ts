import { Directive, ElementRef, HostListener } from '@angular/core';
import { ResponseInterceptor } from '../../core/interceptor/response.interceptor';

@Directive({
  selector: '[appDisableButton]',
})
export class DisableButtonDirective {
  constructor(private el: ElementRef, private responseInterceptor: ResponseInterceptor) {}

  ngOnInit() {}

  @HostListener('click')
  onClick() {
    this.el.nativeElement.disabled = true;
    this.responseInterceptor.setElementRef(this.el);
  }
}
