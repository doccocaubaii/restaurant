import { Directive, Input, ElementRef, Renderer2, HostListener, ChangeDetectorRef } from '@angular/core';
import { StaticMapService } from '../other/StaticMapService';
@Directive({
  selector: '[jhiJsonCustom]',
})
export class JhiJsonCustomDirective {
  map: Map<string, any>;
  @Input() jhiJsonCustom: string = '';
  private jsonString: string;

  constructor(private el: ElementRef, private renderer: Renderer2, private mapService: StaticMapService) {
    mapService.eventEmitter.subscribe(e => {
      this.doTask();
    });
  }

  ngOnInit() {
    this.doTask();
  }

  doTask() {
    this.map = this.mapService.getMap();
    if (!this.map || !this.jhiJsonCustom) {
      return;
    }
    const value = this.map.get(this.jhiJsonCustom) || '';
    if (value.displayName) {
      this.renderer.setProperty(this.el.nativeElement, 'textContent', value.displayName);
    }
  }
}
