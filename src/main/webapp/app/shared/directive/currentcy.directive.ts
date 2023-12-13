import { Directive, ElementRef, EventEmitter, HostListener, Input, OnChanges, Output, Renderer2, SimpleChanges } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';
import { BaseComponent } from '../base/base.component';

@Directive({
  selector: '[epCurrency]',
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      useExisting: CurrencyDirective,
      multi: true,
    },
  ],
})
export class CurrencyDirective extends BaseComponent implements ControlValueAccessor, OnChanges {
  @Input('isPrice')
  isPrice: boolean = true;
  value: string;
  private onChange: (value: any) => void;
  private onTouched: (value: any) => void;
  private valueItem: any;
  @Output()
  valueChange: EventEmitter<number> = new EventEmitter<number>();
  ngModel: EventEmitter<number> = new EventEmitter<number>();
  lastCompany: any;

  constructor(private el: ElementRef, private renderer: Renderer2) {
    super();
  }

  registerOnTouched(fn: any): void {
    this.onTouched = fn;
  }

  setDisabledState?(isDisabled: boolean): void {}

  async ngOnInit() {
    this.lastCompany = await this.getCompany();
    // console.log(this.value);
    // console.log(this.ngModel);
    // console.log(this.el.nativeElement.value);
  }

  // Định dạng số khi hiển thị giá trị
  writeValue(value: any): void {
    const formattedValue = this.formatNumber(value);
    this.valueItem = formattedValue;
    this.el.nativeElement.value = formattedValue;
    this.valueItem = this.transformValue(this.valueItem);
    this.valueItem = parseFloat(this.valueItem);
    this.valueChange.emit(this.valueItem);
  }

  // Gọi khi giá trị thay đổi
  registerOnChange(fn: any): void {
    this.onChange = fn;
  }

  // Xử lý khi người dùng nhập vào input
  @HostListener('input', ['$event.target.value'])
  onInput(value: any): void {
    const formattedValue = this.formatNumber(value);
    this.valueItem = formattedValue;
    this.el.nativeElement.value = formattedValue;
    this.valueItem = this.transformValue(this.valueItem);
    this.valueItem = parseFloat(this.valueItem);
    this.valueChange.emit(this.valueItem);
    // this.renderer.setProperty(this.el.nativeElement, 'value', formattedValue);
  }

  // Xử lý khi người dùng chạm vào input
  @HostListener('blur')
  onBlur(): void {
    this.valueItem = this.transformValue(this.valueItem);
    this.valueChange.emit(this.valueItem);
  }

  // Định dạng số theo yêu cầu (Ví dụ: 1000 -> 1,000)
  private formatNumber(value: any): any {
    // Định dạng số ở đây, ví dụ:
    value = value + '';
    if (value) {
      if (value.indexOf('.') == value.length - 1) {
        return value;
      }
      value = this.transformValue(value);
      const valueConvert = parseFloat(value);
      const options = {
        useGrouping: true, // Sử dụng ngăn cách phần nghìn
        decimalSeparator: '.', // Dấu phân cách phần thập phân
        groupingSeparator: ',', // Dấu phân cách phần nghìn
        maximumFractionDigits: this.isPrice
          ? this.lastCompany && this.lastCompany.roundScaleUnitPrice
            ? this.lastCompany.roundScaleUnitPrice
            : 6
          : this.lastCompany.roundScaleQuantity,
      };
      return valueConvert.toLocaleString(undefined, options);
    }
    return value ? value : '';
  }

  // Loại bỏ các ký tự không phải số để lấy giá trị nguyên thủy
  private transformValue(value: string): any {
    // Xử lý giá trị ở đây (ví dụ: loại bỏ dấu phẩy)
    return value ? value.replace(/,/g, '') : value;
  }

  ngOnChanges(changes: SimpleChanges): void {
    // console.log(this.value);
    // console.log(this.ngModel);
    // console.log(this.el.nativeElement.value);
    // console.log(changes);
  }
}
