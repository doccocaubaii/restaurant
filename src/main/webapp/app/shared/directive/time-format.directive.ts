import { Directive, ElementRef, HostListener } from '@angular/core';

@Directive({
  selector: '[jhiTimeFormat]',
})
export class TimeFormatDirective {
  private readonly TIME_REGEX_FINAL = /^"(([0-1]?[0-9]|2[0-3]?):([0-5]?[0-9]?))"$/;
  private readonly TIME_REGEX = /^(([0-1][0-9])|2[0-3]):?([0-5][0-9]?)?$/;
  private readonly NUMBER_ONLY_REGEX = /^[0-9]$/;

  constructor(private el: ElementRef) {}

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');
    if (this.TIME_REGEX_FINAL.test(pastedText)) {
      event.preventDefault();
    }
  }

  @HostListener('keypress', ['$event']) onKeypressEvent(event: KeyboardEvent) {
    const lastChar = String.fromCharCode(event.keyCode || event.which);
    let inputValue = this.el.nativeElement.value;
    if (this.NUMBER_ONLY_REGEX.test(lastChar)) {
      const lastCharNum: number = +lastChar;
      if (inputValue.length > 0 && inputValue.length <= 4 && this.TIME_REGEX.test(inputValue + lastCharNum)) {
        if (inputValue.length === 1) {
          this.updateValue(inputValue + lastCharNum + ':');
        } else if (inputValue.length === 2 && !inputValue.includes(':')) {
          this.updateValue(inputValue + ':' + lastCharNum);
        } else {
          this.updateValue(inputValue + lastCharNum);
        }
        event.preventDefault();
      } else if (inputValue.length === 0) {
        if (lastCharNum >= 3 && lastCharNum <= 9) {
          this.updateValue('0' + lastCharNum + ':');
        } else {
          this.updateValue(inputValue + lastCharNum);
        }
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
    this.el.nativeElement.dispatchEvent(new Event('input', { bubbles: true })); // Cập nhật giá trị của biến liên kết với [(ngModel)]
  }
}
