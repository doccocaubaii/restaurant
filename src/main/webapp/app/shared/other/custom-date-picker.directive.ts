import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[appCustomDatepicker]',
})
export class CustomDatepickerDirective {
  constructor(private el: ElementRef) {}
  @HostListener('keypress', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    const inputChar = String.fromCharCode(event.keyCode || event.which);

    const pattern = /^[0-9-]*$/;
    if (!pattern.test(inputChar)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');

    const pattern = /^[0-9-]*$/;

    if (!pattern.test(pastedText)) {
      event.preventDefault();
    }
  }
  ngOnInit() {
    // Tắt gợi ý tự động
    (this.el.nativeElement as HTMLInputElement).setAttribute('autocomplete', 'off');
  }
}
