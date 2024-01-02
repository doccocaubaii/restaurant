import { Directive, ElementRef, Renderer2 } from '@angular/core';
import { LoadingOption } from '../../utils/loadingOption';

@Directive({
  selector: '[disableDiv]',
})
export class DisableDirective {
  constructor(private elementRef: ElementRef, private renderer: Renderer2, public loading: LoadingOption) {}

  ngOnInit() {
    if (this.loading.isLoading) {
      this.renderer.setAttribute(this.elementRef.nativeElement, 'disabled', 'true');
      this.renderer.setStyle(this.elementRef.nativeElement, 'pointer-events', 'none');
    }
  }
}
