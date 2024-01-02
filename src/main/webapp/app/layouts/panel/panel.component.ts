import { Component, ViewChild, AfterViewInit, Input } from '@angular/core';

@Component({
  selector: 'panel',
  templateUrl: './panel.component.html',
})
export class PanelComponent implements AfterViewInit {
  @ViewChild('panelFooter', { static: false }) panelFooter;
  @Input() title: any;
  @Input() variant: any;
  @Input() noBody: any;
  @Input() noButton: any;
  @Input() headerClass: any;
  @Input() bodyClass: any;
  @Input() footerClass: any;
  @Input() panelClass: any;
  expand = false;
  reload = false;
  collapse = false;
  remove = false;
  showFooter = false;

  ngAfterViewInit() {
    setTimeout(() => {
      this.showFooter = this.panelFooter ? this.panelFooter.nativeElement && this.panelFooter.nativeElement.children.length > 0 : false;
    });
  }

  panelExpand() {
    this.expand = !this.expand;
  }
  panelReload() {
    this.reload = true;

    setTimeout(() => {
      this.reload = false;
    }, 1500);
  }
  panelCollapse() {
    this.collapse = !this.collapse;
  }
  panelRemove() {
    this.remove = !this.remove;
  }
}
