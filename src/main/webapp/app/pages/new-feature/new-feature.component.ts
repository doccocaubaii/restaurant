import { Component, ElementRef, HostListener, OnInit } from '@angular/core';
import { ICON_CLOSE_NEW_FEATURE, ICON_NEW_FEATURE } from 'app/shared/other/icon';
import { BaseComponent } from '../../shared/base/base.component';
import { NewFeatureService } from './new-feature.service';
import { ToastrService } from 'ngx-toastr';
import { TranslateService } from '@ngx-translate/core';
import { VersionUpdateService } from '../../shared/service/version_update.service';
import { last_user } from '../../object-stores.constants';

@Component({
  selector: 'jhi-new-feature',
  templateUrl: './new-feature.component.html',
  styleUrls: ['./new-feature.component.scss'],
})
export class NewFeatureComponent extends BaseComponent implements OnInit {
  filterNewFeature: any = {};
  newFeatures: any = [];
  protected readonly ICON_NEW_FEATURE = ICON_NEW_FEATURE;
  protected readonly ICON_CLOSE_NEW_FEATURE = ICON_CLOSE_NEW_FEATURE;
  isClose: boolean = false;
  newImg: boolean = true;
  drag: boolean = true;
  topPosition: any;
  leftPosition: any;
  newestNewFeature: any;
  showNewFeature: { userId: any; idVersionUpdate: any };
  lastUser: any;
  userId: any;
  isOpen: boolean = true;

  constructor(
    private service: NewFeatureService,
    private toastr: ToastrService,
    private translateService: TranslateService,
    private elementRef: ElementRef,
    private versionUpdateService: VersionUpdateService
  ) {
    super();
  }

  async ngOnInit() {
    this.lastUser = await this.getFirstItemIndexDB(last_user);
    this.userId = this.lastUser.id;
    this.getAllNewFeature();
  }

  getAllNewFeature() {
    const systems: any[] = [0, 2];
    const types: any[] = ['TINHNANGMOI'];
    this.filterNewFeature.systems = systems;
    this.filterNewFeature.types = types;
    const req = Object.assign({}, this.filterNewFeature);
    this.service.getAllNewFeature(req).subscribe(value => {
      this.newFeatures = JSON.parse(JSON.stringify(value.body.data));
      for (const item of this.newFeatures) {
        item.versionUpdateItem.startDate = this.formatDate(item.versionUpdateItem.startDate);
      }
      this.newestNewFeature = this.versionUpdateService.newestNewFeature;
      this.versionUpdateService.newestNewFeature = null;
      this.showNewFeature = JSON.parse(<string>localStorage.getItem('showNewFeature'));
      if (
        this.showNewFeature &&
        this.showNewFeature.userId == this.userId &&
        this.showNewFeature.idVersionUpdate == this.newFeatures[0].versionUpdateItem.id
      ) {
        this.newestNewFeature = null;
      }
    });
  }

  formatDate(inputDate: string): string {
    const parts = inputDate.split('-'); // Tách chuỗi thành mảng các phần tử
    if (parts.length === 3) {
      const [year, month, day] = parts; // Lấy năm, tháng và ngày
      const formattedDate = `${day}/${month}/${year}`; // Định dạng lại ngày tháng
      return formattedDate;
    } else {
      return 'Invalid date format';
    }
  }

  onOpen() {
    this.isOpen = true;
    if (!this.drag) {
      this.drag = true;
      return;
    }
    if (this.newFeatures.length < 1) {
      this.toastr.success('Không có tính năng mới nào', this.translateService.instant('global.info.notify'));
    } else {
      this.isClose = !this.isClose;
      if (!this.isClose) {
        // Lấy thẻ div
        const divElement = document.getElementById('iconNewFeature');
        // Kiểm tra xem thẻ div có tồn tại không
        if (divElement) {
          divElement.style.left = this.leftPosition + 'px';
          divElement.style.top = this.topPosition + 'px';
        }
      }
    }
  }

  @HostListener('document:click', ['$event'])
  onHide(event: Event) {
    if (!this.drag) {
      this.drag = true;
    }
    if (!this.isOpen) {
      const divElement = document.getElementById('div-new-feature'); // Lấy tham chiếu đến phần tử khối div cụ thể
      if (divElement && !divElement.contains(event.target as HTMLElement)) {
        if (this.isClose) {
          this.isClose = false;
        }
      }
    }
    this.isOpen = false;
    setTimeout(() => {
      this.newestNewFeature = null;
    }, 5000);
  }

  onStart() {
    this.drag = true;
  }

  onEnd() {
    this.drag = false;
    const div: HTMLElement = this.elementRef.nativeElement.querySelector('.iconNewFeature');
    if (div) {
      const rect = div.getBoundingClientRect();
      this.leftPosition = rect.left + window.scrollX;
      this.topPosition = rect.top + window.scrollY;
    }
  }

  hideNewest() {
    this.showNewFeature = { userId: this.userId, idVersionUpdate: this.newFeatures[0].versionUpdateItem.id };
    localStorage.setItem('showNewFeature', JSON.stringify(this.showNewFeature));
    this.newestNewFeature = null;
  }
}
