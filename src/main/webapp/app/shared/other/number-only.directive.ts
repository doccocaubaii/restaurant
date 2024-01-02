import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiNumberOnly]',
})
export class NumberOnlyDirective {
  constructor(private el: ElementRef) {}
  @HostListener('keypress', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    const inputChar = String.fromCharCode(event.keyCode || event.which);

    // Chặn ký tự chữ và ký tự đặc biệt
    const pattern = /^[0-9]*$/; // Chỉ cho phép số
    if (!pattern.test(inputChar)) {
      event.preventDefault();
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');

    const pattern = /^[0-9]*$/; // Chỉ cho phép số

    if (!pattern.test(pastedText)) {
      event.preventDefault();
    }
  }
  ngOnInit() {
    // Tắt gợi ý tự động
    (this.el.nativeElement as HTMLInputElement).setAttribute('autocomplete', 'off');
  }
}
