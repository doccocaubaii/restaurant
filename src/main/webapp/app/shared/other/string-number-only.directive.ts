import { Directive, ElementRef, HostListener, Input } from '@angular/core';
@Directive({
  selector: '[jhiStringNumberOnly]',
})
export class StringNumberOnlyDirective {
  constructor(private el: ElementRef) {}

  @Input() maxvalue: number;
  @HostListener('keypress', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    const inputChar = String.fromCharCode(event.keyCode || event.which);

    // Chặn ký tự chữ và ký tự đặc biệt
    const pattern = /^[0-9_-]*$/; // Chỉ cho phép số
    if (!pattern.test(inputChar)) {
      event.preventDefault();
    } else {
      const inputValue = parseInt((this.el.nativeElement as HTMLInputElement).value + inputChar, 10);
      if (!isNaN(this.maxvalue) && inputValue > this.maxvalue) {
        event.preventDefault();
      }
    }
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');

    const pattern = /^[0-9]*$/; // Chỉ cho phép số

    if (!pattern.test(pastedText)) {
      event.preventDefault();
    } else {
      const inputValue = parseInt((this.el.nativeElement as HTMLInputElement).value + pastedText, 10);
      if (!isNaN(this.maxvalue) && inputValue > this.maxvalue) {
        event.preventDefault();
      }
    }
  }
  ngOnInit() {
    // Tắt gợi ý tự động
    (this.el.nativeElement as HTMLInputElement).setAttribute('autocomplete', 'off');
    this.formatValue();
  }
  private formatValue() {
    const inputElement = this.el.nativeElement as HTMLInputElement;
    const value = inputElement.value;

    // Xóa tất cả dấu phẩy và khoảng trắng trong giá trị
    const sanitizedValue = value.replace(/,/g, '').replace(/\s/g, '');

    // Định dạng lại giá trị với dấu phẩy ngăn cách hàng nghìn
    const formattedValue = sanitizedValue.replace(/\B(?=(\d{3})+(?!\d))/g, ',');

    // Gán giá trị đã được định dạng lại vào trường đầu vào
    inputElement.value = formattedValue;
  }
}
