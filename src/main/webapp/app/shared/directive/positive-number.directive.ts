import { Directive, ElementRef, HostListener, forwardRef, Input, OnInit } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Directive({
  selector: '[jhiPositiveNumber]',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: forwardRef(() => PositiveNumberDirective),
      multi: true,
    },
  ],
})
export class PositiveNumberDirective implements ControlValueAccessor {
  @Input('numberRound') numberRound;
  @Input('maxvalue') maxValue: number;

  constructor(private el: ElementRef) {}

  private formattedValue: string;
  private onChange: (value: any) => void;
  private onTouched: () => void;

  ngOnInit() {
    // Format và làm tròn khi khởi tạo
    this.el.nativeElement.setAttribute('autocomplete', 'off');
    this.writeValue(this.el.nativeElement.value);
  }

  roundNumber(value: string) {
    if (this.numberRound >= 0) {
      let parts = value.split('.');
      if (parts[1] && parts[1].length > this.numberRound) {
        if (this.numberRound > 0) {
          parts[1] = parseFloat(`0.${parts[1]}`).toFixed(this.numberRound).slice(2);
        } else {
          return Math.round(parseFloat(value)).toString();
        }
      }
      return parts.join('.');
    } else {
      return value;
    }
  }

  @HostListener('input', ['$event'])
  onInput(event: InputEvent) {
    let inputValue = (event.target as HTMLInputElement).value;
    if (inputValue === '') {
      this.onChange(null);
      return;
    }
    let dotIndex = inputValue.indexOf('.');
    if (dotIndex >= 0 && this.numberRound) {
      if (inputValue.length - 1 - dotIndex > this.numberRound) {
        inputValue = inputValue.substring(0, dotIndex + this.numberRound + 1);
      }
    }

    const lastChar = inputValue[inputValue.length - 1];

    if (lastChar.match(/[0-9]/)) {
      this.formattedValue = this.formatNumber(inputValue);
    } else if (lastChar === '.' && !inputValue.slice(0, -1).includes('.')) {
      this.formattedValue = this.formatNumber(inputValue.slice(0, -1)) + '.';
    } else {
      const newValue = inputValue.slice(0, -1);
      this.formattedValue = this.formatNumber(newValue);
    }
    this.formattedValue = this.roundNumber(this.formattedValue);
    (event.target as HTMLInputElement).value = this.formattedValue;
    this.onChange(-Number(this.formattedValue.replace(/,/g, '')));

    if (this.maxValue) {
      const numberValue = Number(this.formattedValue.replace(/,/g, ''));
      if (numberValue > this.maxValue) {
        // Nếu giá trị nhập vào lớn hơn max value
        this.el.nativeElement.value = this.formatNumber(this.maxValue.toString()).replace(/-/g, '');
        this.onChange(-this.maxValue);
      } else if (numberValue === this.maxValue && (event.data || '').match(/[0-9.]/)) {
        // Nếu giá trị nhập vào bằng max value và người dùng vẫn đang thêm các số khác
        event.preventDefault();
      } else {
        this.el.nativeElement.classList.remove('invalid');
        this.onChange(-numberValue);
      }
    } else {
      this.el.nativeElement.classList.remove('invalid');
      this.onChange(-Number(this.formattedValue.replace(/,/g, '')));
    }
  }

  registerOnChange(fn: (value: number) => void): void {
    this.onChange = fn;
  }

  registerOnTouched(fn: () => void): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {
    (this.el.nativeElement as HTMLInputElement).disabled = isDisabled;
  }

  // writeValue(value: number): void {
  //   if (value != null) {
  //     this.el.nativeElement.value = this.formatNumber(value.toString());
  //   } else {
  //     this.el.nativeElement.value = null;
  //   }
  // }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    const clipboardData = event.clipboardData || (window as any).clipboardData;
    const pastedText = clipboardData.getData('text');
    const pattern = /^(\d*\.)?\d+$/;
    if (!pastedText.match(pattern)) {
      event.preventDefault();
    }
  }

  @HostListener('keypress', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    const inputChar = String.fromCharCode(event.keyCode || event.which);

    if (!inputChar.match(/[0-9.]/)) {
      event.preventDefault();
    } else if (inputChar === '.' && this.el.nativeElement.value.includes('.').replace(/-/g, '')) {
      event.preventDefault();
    }
  }

  private formatNumber(value: string): string {
    const parts = value.split('.');
    parts[0] = parts[0].replace(/,/g, '').replace(/\B(?=(\d{3})+(?!\d))/g, ',');
    return parts.join('.');
  }

  writeValue(value: number): void {
    if (value != null) {
      const formattedValue = this.formatNumber(value.toString());
      this.el.nativeElement.value = this.roundNumber(formattedValue).replace(/-/g, '');
    } else {
      this.el.nativeElement.value = null;
    }
  }
}
