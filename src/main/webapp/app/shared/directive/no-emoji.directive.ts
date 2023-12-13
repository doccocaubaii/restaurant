import { Directive, ElementRef, HostListener } from '@angular/core';
import { NgControl } from '@angular/forms';

@Directive({
  selector: '[jhiNoEmoji]',
})
export class NoEmojiDirective {
  private element: HTMLInputElement;

  constructor(private el: ElementRef, private control: NgControl) {
    this.element = el.nativeElement;
  }

  private filterEmoji(event: Event): void {
    const transformedInput: string = this.element.value.replace(/[\uD83C-\uDBFF\uDC00-\uDFFF]+/g, '');

    if (this.element.value !== transformedInput) {
      this.element.value = '';
      if (this.control.control) {
        this.control.control.setValue(null);
      }
    } else {
      if (this.control.control) {
        // Gọi callback khi giá trị thay đổi
        this.control.control.setValue(transformedInput);
      }
    }

    event.stopPropagation();
  }

  @HostListener('input', ['$event']) onInputChange(event) {
    this.filterEmoji(event);
  }

  @HostListener('keypress', ['$event'])
  onKeyPress(event: KeyboardEvent) {
    this.filterEmoji(event);
  }

  @HostListener('paste', ['$event'])
  onPaste(event: ClipboardEvent) {
    this.filterEmoji(event);
  }
}
