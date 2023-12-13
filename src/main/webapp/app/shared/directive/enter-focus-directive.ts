import { Directive, HostListener, ElementRef, Input } from '@angular/core';

@Directive({
  selector: '[appEnterKeyFocus]',
})
export class EnterFocusDirective {
  @Input('appEnterKeyFocus') nextFocusSelector: string | null;

  constructor(private el: ElementRef) {}

  @HostListener('keydown.tab', ['$event'])
  @HostListener('keydown.enter', ['$event'])
  onEnterKey(event: Event): void {
    event.preventDefault();
    let nextElement: HTMLElement | null = null;
    // nếu không truyền gì thì nhảy đến input tiếp theo
    if (!this.nextFocusSelector) {
      const inputs = document.querySelectorAll('input');
      const currentInput = this.el.nativeElement;
      const currentIndex = Array.from(inputs).indexOf(currentInput);
      if (currentIndex < inputs.length - 1) {
        inputs[currentIndex + 1].focus();
      }
    } else if (this.nextFocusSelector.startsWith('#')) {
      // Tìm theo ID

      const id = this.nextFocusSelector.substring(1);
      nextElement = document.getElementById(id);
      if (nextElement) {
        nextElement.focus();
      }
    } else if (this.nextFocusSelector.startsWith('.')) {
      // Tìm theo class
      const className = this.nextFocusSelector.substring(1);
      const elementsWithClass = document.querySelectorAll('.' + className);
      if (elementsWithClass.length > 0) {
        nextElement = elementsWithClass[0] as HTMLElement;
      }
      if (nextElement) {
        nextElement.focus();
      }
    }
  }
}
