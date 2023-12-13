import { Directive, Input, ElementRef, Renderer2, HostListener, ChangeDetectorRef } from '@angular/core';
import { StaticMapService } from '../other/StaticMapService';
@Directive({
  selector: '[jhiJsonCustomAnd]',
})
// tất các childrenCodes truyền vào có active = false --> ẩn luôn parentCode
export class JhiJsonCustomAndDirective {
  map: Map<string, any>;
  activeArray: any[] = [];
  @Input() childrenCodes: string[] = [];
  @Input() parentCode = '';

  constructor(private el: ElementRef, private renderer: Renderer2, private mapService: StaticMapService) {
    mapService.eventEmitter.subscribe(e => {
      this.doTask();
    });
  }

  ngOnInit() {
    this.doTask();
  }

  doTask() {
    this.activeArray = [];
    this.map = this.mapService.getMap();
    if (!this.map || !this.childrenCodes || this.childrenCodes.length == 0 || !this.parentCode) {
      return;
    }
    for (const element of this.childrenCodes) {
      const value = this.map.get(element) || '';
      this.activeArray.push(value.active);
    }

    const value = this.map.get(this.parentCode) || '';
    if (!value) {
      return;
    }
    value.active = true;
    let check = false;
    for (const element of this.activeArray) {
      if (element) {
        check = true;
        break;
      }
    }
    if (!check) {
      value.active = false;
    }
    this.map.set(value.code, value);
  }
}
