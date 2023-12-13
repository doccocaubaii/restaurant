import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiTaxCode]',
})
export class TaxCodeDirective {
  constructor(private el: ElementRef) {}

  private readonly REGEX_TAX_CODE = /^(?:\d{10}|\d{10}-\d{3})$/;
  private readonly NUMBER_ONLY_REGEX = /^\d$/;

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');
    if (!this.REGEX_TAX_CODE.test(pastedText)) {
      event.preventDefault();
    }
  }

  @HostListener('keypress', ['$event']) onKeypressEvent(event: KeyboardEvent) {
    const lastChar = String.fromCharCode(event.keyCode || event.which);
    let inputValue = this.el.nativeElement.value;
    if (this.NUMBER_ONLY_REGEX.test(lastChar)) {
      const lastCharNum: number = +lastChar;
      if (inputValue.length <= 13) {
        if (inputValue.length === 10) {
          this.updateValue(inputValue + '-' + lastCharNum);
        } else {
          this.updateValue(inputValue + lastCharNum);
        }
        event.preventDefault();
      } else {
        event.preventDefault();
      }
    } else if (lastChar === '-') {
      if (inputValue.length === 10) {
        this.updateValue(inputValue + lastChar);
        event.preventDefault();
      } else {
        event.preventDefault();
      }
    } else {
      event.preventDefault();
    }
  }
  private updateValue(newValue: string) {
    this.el.nativeElement.value = newValue;
    this.el.nativeElement.dispatchEvent(new Event('input', { bubbles: true }));
  }
}
